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
import { departmentAPI } from '../../services/api';

const DepartmentManagement = () => {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingDepartment, setEditingDepartment] = useState(null);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });
  const [rowCount, setRowCount] = useState(0);
  const [formData, setFormData] = useState({
    departmentCode: '',
    departmentName: '',
  });

  useEffect(() => {
    fetchDepartments();
  }, [paginationModel.page, paginationModel.pageSize]);

  const fetchDepartments = async () => {
    try {
      setLoading(true);
      const response = await departmentAPI.getAll({
        pageNumber: paginationModel.page,
        pageSize: paginationModel.pageSize,
      });
      // Handle paginated response structure
      const departmentData = response.data.data || [];
      setDepartments(departmentData);
      setRowCount(response.data.totalElements || 0);
    } catch (error) {
      setError('Failed to fetch departments');
      console.error('Error fetching departments:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (department = null) => {
    if (department) {
      setEditingDepartment(department);
      setFormData({
        departmentCode: department.departmentCode || '',
        departmentName: department.departmentName || '',
      });
    } else {
      setEditingDepartment(null);
      setFormData({
        departmentCode: '',
        departmentName: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingDepartment(null);
    setFormData({
      departmentCode: '',
      departmentName: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingDepartment) {
        await departmentAPI.update({
          departmentCode: formData.departmentCode,
          departmentName: formData.departmentName,
        });
      } else {
        await departmentAPI.create(formData);
      }
      fetchDepartments();
      handleCloseDialog();
    } catch (error) {
      setError('Failed to save department');
      console.error('Error saving department:', error);
    }
  };

  const handleDelete = async (departmentCode) => {
    if (window.confirm('Are you sure you want to delete this department?')) {
      try {
        await departmentAPI.delete(departmentCode);
        fetchDepartments();
      } catch (error) {
        setError('Failed to delete department');
        console.error('Error deleting department:', error);
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
          Department Management
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
        >
          Add Department
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
              <TableCell align="center">Department Code</TableCell>
              <TableCell align="center">Department Name</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {departments.map((department) => (
              <TableRow key={department.departmentCode}>
                <TableCell align="center">
                  {department.departmentCode}
                </TableCell>
                <TableCell align="center">
                  {department.departmentName}
                </TableCell>
                <TableCell align="center">
                  <IconButton
                    onClick={() => handleOpenDialog(department)}
                    color="primary"
                    size="small"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => handleDelete(department.departmentCode)}
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
        <DialogTitle>
          {editingDepartment ? 'Edit Department' : 'Add New Department'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Department Code"
                  name="departmentCode"
                  value={formData.departmentCode}
                  onChange={(e) =>
                    setFormData({ ...formData, departmentCode: e.target.value })
                  }
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Department Name"
                  name="departmentName"
                  value={formData.departmentName}
                  onChange={(e) =>
                    setFormData({ ...formData, departmentName: e.target.value })
                  }
                  required
                />
              </Grid>
              {/* Description removed as it's not part of DepartmentDTO */}
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button type="submit" variant="contained">
              {editingDepartment ? 'Update' : 'Create'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default DepartmentManagement;
