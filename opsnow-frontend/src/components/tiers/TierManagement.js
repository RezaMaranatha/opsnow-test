import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Typography,
  Alert,
  CircularProgress,
  Grid,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
} from '@mui/icons-material';
import { tierAPI } from '../../services/api';

const TierManagement = () => {
  const [tiers, setTiers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingTier, setEditingTier] = useState(null);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });
  const [rowCount, setRowCount] = useState(0);
  const [formData, setFormData] = useState({
    tierCode: '',
    tierName: '',
  });

  useEffect(() => {
    fetchTiers();
  }, [paginationModel.page, paginationModel.pageSize]);

  const fetchTiers = async () => {
    try {
      setLoading(true);
      const response = await tierAPI.getAll({
        pageNumber: paginationModel.page,
        pageSize: paginationModel.pageSize,
      });
      // Handle paginated response structure
      const tierData = response.data.data || [];
      setTiers(tierData);
      setRowCount(response.data.totalElements || 0);
    } catch (error) {
      setError('Failed to fetch tiers');
      console.error('Error fetching tiers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (tier = null) => {
    if (tier) {
      setEditingTier(tier);
      setFormData({
        tierCode: tier.tierCode || '',
        tierName: tier.tierName || '',
      });
    } else {
      setEditingTier(null);
      setFormData({
        tierCode: '',
        tierName: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingTier(null);
    setFormData({
      tierCode: '',
      tierName: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingTier) {
        // For update, send only the request body (no path variable)
        await tierAPI.update(formData);
      } else {
        await tierAPI.create(formData);
      }
      fetchTiers();
      handleCloseDialog();
    } catch (error) {
      setError('Failed to save tier');
      console.error('Error saving tier:', error);
    }
  };

  const handleDelete = async (tierCode) => {
    if (window.confirm('Are you sure you want to delete this tier?')) {
      try {
        await tierAPI.delete(tierCode);
        fetchTiers();
      } catch (error) {
        setError('Failed to delete tier');
        console.error('Error deleting tier:', error);
      }
    }
  };

  const handleChangePage = (event, newPage) => {
    setPaginationModel((prev) => ({ ...prev, page: newPage }));
  };

  const handleChangeRowsPerPage = (event) => {
    setPaginationModel({
      page: 0,
      pageSize: parseInt(event.target.value, 10),
    });
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
        mb={3}
      >
        <Typography variant="h4" component="h1">
          Tier Management
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
        >
          Add Tier
        </Button>
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
              <TableCell align="center">Tier Code</TableCell>
              <TableCell align="center">Tier Name</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tiers.map((tier) => (
              <TableRow key={tier.tierCode}>
                <TableCell align="center">{tier.tierCode}</TableCell>
                <TableCell align="center">{tier.tierName}</TableCell>
                <TableCell align="center">
                  <IconButton
                    onClick={() => handleOpenDialog(tier)}
                    color="primary"
                    size="small"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => handleDelete(tier.tierCode)}
                    color="error"
                    size="small"
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[10, 25, 50]}
          component="div"
          count={rowCount}
          rowsPerPage={paginationModel.pageSize}
          page={paginationModel.page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>{editingTier ? 'Edit Tier' : 'Add New Tier'}</DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Tier Code"
                  name="tierCode"
                  value={formData.tierCode}
                  onChange={(e) =>
                    setFormData({ ...formData, tierCode: e.target.value })
                  }
                  required
                  disabled={editingTier !== null}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Tier Name"
                  name="tierName"
                  value={formData.tierName}
                  onChange={(e) =>
                    setFormData({ ...formData, tierName: e.target.value })
                  }
                  required
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button type="submit" variant="contained">
              {editingTier ? 'Update' : 'Create'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default TierManagement;
