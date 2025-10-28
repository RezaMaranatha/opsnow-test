import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Alert,
  CircularProgress,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import axios from 'axios';

const DepartmentAnalysis = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDepartmentAnalysis();
  }, []);

  const fetchDepartmentAnalysis = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('authToken');
      const response = await axios.get(
        'http://localhost:8080/api/employee/department-analysis',
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setData(response.data || []);
    } catch (error) {
      setError('Failed to fetch department analysis data');
      console.error('Error fetching department analysis:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
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
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        flexDirection={{ xs: 'column', sm: 'row' }}
        gap={{ xs: 2, sm: 0 }}
        mb={{ xs: 2, sm: 3 }}
      >
        <Typography
          variant="h4"
          component="h1"
          sx={{ fontSize: { xs: '1.5rem', sm: '2rem' } }}
        >
          Department Analysis
        </Typography>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell align="center">Location Name</TableCell>
              <TableCell align="center">
                Department with Most Employees
              </TableCell>
              <TableCell align="center">Employee Count</TableCell>
              <TableCell align="center">Lowest Dept Avg Salary</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row, index) => (
              <TableRow key={index}>
                <TableCell align="center">{row.locationName}</TableCell>
                <TableCell align="center">{row.deptWithMostEmployee}</TableCell>
                <TableCell align="center">{row.deptEmployeeCount}</TableCell>
                <TableCell align="center">
                  {row.lowestDeptAvgSalary?.toLocaleString()}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default DepartmentAnalysis;
