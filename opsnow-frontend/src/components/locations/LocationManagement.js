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
import { locationAPI } from '../../services/api';

const LocationManagement = () => {
  const [locations, setLocations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingLocation, setEditingLocation] = useState(null);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });
  const [rowCount, setRowCount] = useState(0);
  const [formData, setFormData] = useState({
    locationCode: '',
    locationName: '',
    locationAddress: '',
  });

  useEffect(() => {
    fetchLocations();
  }, [paginationModel.page, paginationModel.pageSize]);

  const fetchLocations = async () => {
    try {
      setLoading(true);
      const response = await locationAPI.getAll({
        pageNumber: paginationModel.page,
        pageSize: paginationModel.pageSize,
      });
      // Handle paginated response structure
      const locationData = response.data.data || [];
      setLocations(locationData);
      setRowCount(response.data.totalElements || 0);
    } catch (error) {
      setError('Failed to fetch locations');
      console.error('Error fetching locations:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (location = null) => {
    if (location) {
      setEditingLocation(location);
      setFormData({
        locationCode: location.locationCode || '',
        locationName: location.locationName || '',
        locationAddress: location.locationAddress || '',
      });
    } else {
      setEditingLocation(null);
      setFormData({
        locationCode: '',
        locationName: '',
        locationAddress: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingLocation(null);
    setFormData({
      locationCode: '',
      locationName: '',
      locationAddress: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingLocation) {
        await locationAPI.update({
          locationCode: formData.locationCode,
          locationName: formData.locationName,
          locationAddress: formData.locationAddress,
        });
      } else {
        await locationAPI.create(formData);
      }
      fetchLocations();
      handleCloseDialog();
    } catch (error) {
      setError('Failed to save location');
      console.error('Error saving location:', error);
    }
  };

  const handleDelete = async (locationCode) => {
    if (window.confirm('Are you sure you want to delete this location?')) {
      try {
        await locationAPI.delete(locationCode);
        fetchLocations();
      } catch (error) {
        setError('Failed to delete location');
        console.error('Error deleting location:', error);
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
          Location Management
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
        >
          Add Location
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
              <TableCell align="center">Location Code</TableCell>
              <TableCell align="center">Location Name</TableCell>
              <TableCell align="center">Location Address</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {locations.map((location) => (
              <TableRow key={location.locationCode}>
                <TableCell align="center">{location.locationCode}</TableCell>
                <TableCell align="center">{location.locationName}</TableCell>
                <TableCell align="center">{location.locationAddress}</TableCell>
                <TableCell align="center">
                  <IconButton
                    onClick={() => handleOpenDialog(location)}
                    color="primary"
                    size="small"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => handleDelete(location.locationCode)}
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
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>
          {editingLocation ? 'Edit Location' : 'Add New Location'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Location Code"
                  name="locationCode"
                  value={formData.locationCode}
                  onChange={(e) =>
                    setFormData({ ...formData, locationCode: e.target.value })
                  }
                  required
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Location Name"
                  name="locationName"
                  value={formData.locationName}
                  onChange={(e) =>
                    setFormData({ ...formData, locationName: e.target.value })
                  }
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Location Address"
                  name="locationAddress"
                  value={formData.locationAddress}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      locationAddress: e.target.value,
                    })
                  }
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button type="submit" variant="contained">
              {editingLocation ? 'Update' : 'Create'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default LocationManagement;
