import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Grid,
  Card,
  CardContent,
  TextField,
  Button,
  Alert,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Pagination,
} from '@mui/material';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import { loggingAPI } from '../../services/api';

const ApiLoggingDashboard = () => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filters, setFilters] = useState({
    startDate: dayjs().subtract(7, 'day'),
    endDate: dayjs(),
    userIdentifier: '',
    endpoint: '',
    method: '',
    status: '',
  });
  const [pagination, setPagination] = useState({
    page: 0,
    size: 20,
    total: 0,
  });

  useEffect(() => {
    fetchLogs();
  }, [pagination.page, pagination.size]);

  const fetchLogs = async () => {
    try {
      setLoading(true);
      const params = {
        page: pagination.page,
        size: pagination.size,
        startDate: filters.startDate?.format('YYYY-MM-DDTHH:mm:ss'),
        endDate: filters.endDate?.format('YYYY-MM-DDTHH:mm:ss'),
        ...(filters.userIdentifier && {
          userIdentifier: filters.userIdentifier,
        }),
        ...(filters.endpoint && { endpoint: filters.endpoint }),
        ...(filters.method && { method: filters.method }),
        ...(filters.status && { status: filters.status }),
      };

      const response = await loggingAPI.getHistoryByDateRange(params);
      // Handle paginated response structure
      setLogs(response.data.data || []);
      setPagination((prev) => ({
        ...prev,
        total: response.data.totalElements || 0,
      }));
    } catch (error) {
      setError('Failed to fetch API logs');
      console.error('Error fetching logs:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (field, value) => {
    setFilters((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleApplyFilters = () => {
    setPagination((prev) => ({ ...prev, page: 0 }));
    fetchLogs();
  };

  const handlePageChange = (event, newPage) => {
    setPagination((prev) => ({ ...prev, page: newPage - 1 }));
  };

  const getStatusColor = (status) => {
    if (status >= 200 && status < 300) return 'success';
    if (status >= 300 && status < 400) return 'info';
    if (status >= 400 && status < 500) return 'warning';
    if (status >= 500) return 'error';
    return 'default';
  };

  const getMethodColor = (method) => {
    switch (method) {
      case 'GET':
        return 'primary';
      case 'POST':
        return 'success';
      case 'PUT':
        return 'warning';
      case 'DELETE':
        return 'error';
      default:
        return 'default';
    }
  };

  if (loading && logs.length === 0) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="400px"
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        API Logging Dashboard
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      {/* Filters */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Filters
          </Typography>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={6} md={2}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                  label="Start Date"
                  value={filters.startDate}
                  onChange={(newValue) =>
                    handleFilterChange('startDate', newValue)
                  }
                  slotProps={{ textField: { size: 'small', fullWidth: true } }}
                />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} sm={6} md={2}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                  label="End Date"
                  value={filters.endDate}
                  onChange={(newValue) =>
                    handleFilterChange('endDate', newValue)
                  }
                  slotProps={{ textField: { size: 'small', fullWidth: true } }}
                />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} sm={6} md={2}>
              <TextField
                fullWidth
                size="small"
                label="User Email"
                value={filters.userIdentifier}
                onChange={(e) =>
                  handleFilterChange('userIdentifier', e.target.value)
                }
              />
            </Grid>
            <Grid item xs={12} sm={6} md={2}>
              <TextField
                fullWidth
                size="small"
                label="Endpoint"
                value={filters.endpoint}
                onChange={(e) => handleFilterChange('endpoint', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} sm={6} md={2}>
              <FormControl fullWidth size="small">
                <InputLabel>Method</InputLabel>
                <Select
                  value={filters.method}
                  label="Method"
                  onChange={(e) => handleFilterChange('method', e.target.value)}
                >
                  <MenuItem value="">All</MenuItem>
                  <MenuItem value="GET">GET</MenuItem>
                  <MenuItem value="POST">POST</MenuItem>
                  <MenuItem value="PUT">PUT</MenuItem>
                  <MenuItem value="DELETE">DELETE</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6} md={2}>
              <FormControl fullWidth size="small">
                <InputLabel>Status</InputLabel>
                <Select
                  value={filters.status}
                  label="Status"
                  onChange={(e) => handleFilterChange('status', e.target.value)}
                >
                  <MenuItem value="">All</MenuItem>
                  <MenuItem value="200">200 - Success</MenuItem>
                  <MenuItem value="400">400 - Bad Request</MenuItem>
                  <MenuItem value="401">401 - Unauthorized</MenuItem>
                  <MenuItem value="403">403 - Forbidden</MenuItem>
                  <MenuItem value="404">404 - Not Found</MenuItem>
                  <MenuItem value="500">500 - Server Error</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6} md={2}>
              <Button
                variant="contained"
                onClick={handleApplyFilters}
                fullWidth
                disabled={loading}
              >
                Apply Filters
              </Button>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* Logs Table */}
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            API Call History ({pagination.total} total)
          </Typography>

          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Timestamp</TableCell>
                  <TableCell>Method</TableCell>
                  <TableCell>Endpoint</TableCell>
                  <TableCell>User</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell>Duration (ms)</TableCell>
                  <TableCell>IP Address</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {logs.map((log) => (
                  <TableRow key={log.id}>
                    <TableCell>
                      {dayjs(log.timestamp).format('YYYY-MM-DD HH:mm:ss')}
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={log.httpMethod}
                        color={getMethodColor(log.httpMethod)}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{log.apiEndpoint}</TableCell>
                    <TableCell>{log.userIdentifier || 'Anonymous'}</TableCell>
                    <TableCell>
                      <Chip
                        label={log.responseStatus}
                        color={getStatusColor(log.responseStatus)}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{log.requestDurationMs || 'N/A'}</TableCell>
                    <TableCell>{log.clientIp || 'N/A'}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          {/* Pagination */}
          <Box display="flex" justifyContent="center" mt={2}>
            <Pagination
              count={Math.ceil(pagination.total / pagination.size)}
              page={pagination.page + 1}
              onChange={handlePageChange}
              color="primary"
            />
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default ApiLoggingDashboard;
