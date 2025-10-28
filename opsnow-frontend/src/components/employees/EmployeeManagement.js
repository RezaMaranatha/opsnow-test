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
import { employeeAPI } from '../../services/api';

const EmployeeManagement = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });
  const [rowCount, setRowCount] = useState(0);
  const [formData, setFormData] = useState({
    employeeNumber: '',
    employeeName: '',
    email: '',
    password: '',
    tierCode: '',
    locationCode: '',
    departmentCode: '',
    supervisorCode: '',
    salary: '',
  });

  useEffect(() => {
    fetchEmployees();
  }, [paginationModel.page, paginationModel.pageSize]);

  const fetchEmployees = async () => {
    try {
      setLoading(true);
      const response = await employeeAPI.getAll({
        pageNumber: paginationModel.page,
        pageSize: paginationModel.pageSize,
      });
      console.log('Employee API response:', response.data);
      // Handle paginated response structure
      const employeeData = response.data.data || [];
      setEmployees(employeeData);
      setRowCount(response.data.totalElements || 0);
    } catch (error) {
      setError('Failed to fetch employees');
      console.error('Error fetching employees:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (employee = null) => {
    if (employee) {
      setEditingEmployee(employee);
      setFormData({
        employeeNumber: employee.employeeNumber || '',
        employeeName: employee.employeeName || '',
        email: employee.email || '',
        password: '',
        tierCode: employee.tierCode || '',
        locationCode: employee.locationCode || '',
        departmentCode: employee.departmentCode || '',
        supervisorCode: employee.supervisorCode || '',
        salary: employee.salary || '',
      });
    } else {
      setEditingEmployee(null);
      setFormData({
        employeeNumber: '',
        employeeName: '',
        email: '',
        password: '',
        tierCode: '',
        locationCode: '',
        departmentCode: '',
        supervisorCode: '',
        salary: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingEmployee(null);
    setFormData({
      employeeNumber: '',
      employeeName: '',
      email: '',
      password: '',
      tierCode: '',
      locationCode: '',
      departmentCode: '',
      supervisorCode: '',
      salary: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingEmployee) {
        await employeeAPI.update({
          employeeNumber: formData.employeeNumber,
          employeeName: formData.employeeName,
          email: formData.email,
          password: formData.password || undefined,
          tierCode: formData.tierCode,
          locationCode: formData.locationCode,
          departmentCode: formData.departmentCode,
          supervisorCode: formData.supervisorCode,
          salary: formData.salary,
        });
      } else {
        await employeeAPI.create(formData);
      }
      fetchEmployees();
      handleCloseDialog();
    } catch (error) {
      setError('Failed to save employee');
      console.error('Error saving employee:', error);
    }
  };

  const handleDelete = async (employeeNumber) => {
    if (window.confirm('Are you sure you want to delete this employee?')) {
      try {
        await employeeAPI.delete(employeeNumber);
        fetchEmployees();
      } catch (error) {
        setError('Failed to delete employee');
        console.error('Error deleting employee:', error);
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
        flexDirection={{ xs: 'column', sm: 'row' }}
        gap={{ xs: 2, sm: 0 }}
        mb={{ xs: 2, sm: 3 }}
      >
        <Typography
          variant="h4"
          component="h1"
          sx={{ fontSize: { xs: '1.5rem', sm: '2rem' } }}
        >
          Employee Management
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
          fullWidth={{ xs: true, sm: false }}
          sx={{ minWidth: { sm: '150px' } }}
        >
          Add Employee
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
              <TableCell align="center">Employee Number</TableCell>
              <TableCell align="center">Name</TableCell>
              <TableCell align="center">Email</TableCell>
              <TableCell align="center">Tier</TableCell>
              <TableCell align="center">Location</TableCell>
              <TableCell align="center">Department</TableCell>
              <TableCell align="center">Salary</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {employees.map((employee) => (
              <TableRow key={employee.employeeNumber}>
                <TableCell align="center">{employee.employeeNumber}</TableCell>
                <TableCell align="center">{employee.employeeName}</TableCell>
                <TableCell align="center">{employee.email}</TableCell>
                <TableCell align="center">{employee.tierCode}</TableCell>
                <TableCell align="center">{employee.locationCode}</TableCell>
                <TableCell align="center">{employee.departmentCode}</TableCell>
                <TableCell align="center">
                  {employee.salary?.toLocaleString()}
                </TableCell>
                <TableCell align="center">
                  <IconButton
                    onClick={() => handleOpenDialog(employee)}
                    color="primary"
                    size="small"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => handleDelete(employee.employeeNumber)}
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
        fullScreen={false}
        PaperProps={{
          sx: {
            m: { xs: 1, sm: 2 },
            maxHeight: { xs: '95vh', sm: '90vh' },
          },
        }}
      >
        <DialogTitle>
          {editingEmployee ? 'Edit Employee' : 'Add New Employee'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Employee Name"
                  name="employeeName"
                  value={formData.employeeName}
                  onChange={(e) =>
                    setFormData({ ...formData, employeeName: e.target.value })
                  }
                  required
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Email"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={(e) =>
                    setFormData({ ...formData, email: e.target.value })
                  }
                  required
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Password"
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={(e) =>
                    setFormData({ ...formData, password: e.target.value })
                  }
                  required={!editingEmployee}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Tier Code"
                  name="tierCode"
                  value={formData.tierCode}
                  onChange={(e) =>
                    setFormData({ ...formData, tierCode: e.target.value })
                  }
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Location Code"
                  name="locationCode"
                  value={formData.locationCode}
                  onChange={(e) =>
                    setFormData({ ...formData, locationCode: e.target.value })
                  }
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Department Code"
                  name="departmentCode"
                  value={formData.departmentCode}
                  onChange={(e) =>
                    setFormData({ ...formData, departmentCode: e.target.value })
                  }
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Supervisor Code"
                  name="supervisorCode"
                  value={formData.supervisorCode}
                  onChange={(e) =>
                    setFormData({ ...formData, supervisorCode: e.target.value })
                  }
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Salary"
                  name="salary"
                  type="number"
                  value={formData.salary}
                  onChange={(e) =>
                    setFormData({ ...formData, salary: e.target.value })
                  }
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button type="submit" variant="contained">
              {editingEmployee ? 'Update' : 'Create'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default EmployeeManagement;
