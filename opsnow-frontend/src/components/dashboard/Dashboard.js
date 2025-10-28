import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  People as PeopleIcon,
  Business as BusinessIcon,
  LocationOn as LocationIcon,
  Star as StarIcon,
  History as HistoryIcon,
} from '@mui/icons-material';
import {
  employeeAPI,
  departmentAPI,
  locationAPI,
  tierAPI,
} from '../../services/api';

const Dashboard = () => {
  const [stats, setStats] = useState({
    employees: 0,
    departments: 0,
    locations: 0,
    tiers: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);

      // Fetch counts for each entity
      const [employeesRes, departmentsRes, locationsRes, tiersRes] =
        await Promise.all([
          employeeAPI.getAll({ size: 1 }),
          departmentAPI.getAll({ size: 1 }),
          locationAPI.getAll({ size: 1 }),
          tierAPI.getAll({ size: 1 }),
        ]);

      setStats({
        employees: employeesRes.data.totalElements || 0,
        departments: departmentsRes.data.totalElements || 0,
        locations: locationsRes.data.totalElements || 0,
        tiers: tiersRes.data.totalElements || 0,
      });
    } catch (error) {
      setError('Failed to fetch dashboard statistics');
      console.error('Error fetching stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    {
      title: 'Employees',
      value: stats.employees,
      icon: <PeopleIcon sx={{ fontSize: 40 }} />,
      color: 'primary',
    },
    {
      title: 'Departments',
      value: stats.departments,
      icon: <BusinessIcon sx={{ fontSize: 40 }} />,
      color: 'secondary',
    },
    {
      title: 'Locations',
      value: stats.locations,
      icon: <LocationIcon sx={{ fontSize: 40 }} />,
      color: 'success',
    },
    {
      title: 'Tiers',
      value: stats.tiers,
      icon: <StarIcon sx={{ fontSize: 40 }} />,
      color: 'warning',
    },
    {
      title: 'API Logs',
      value: 'View',
      icon: <HistoryIcon sx={{ fontSize: 40 }} />,
      color: 'info',
    },
  ];

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
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      <Grid container spacing={3}>
        {statCards.map((card, index) => (
          <Grid item xs={12} sm={6} md={2.4} key={index}>
            <Card
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                textAlign: 'center',
                p: 2,
                background: `linear-gradient(135deg, ${card.color}.main 0%, ${card.color}.dark 100%)`,
                color: 'white',
              }}
            >
              <CardContent>
                <Box sx={{ mb: 2 }}>{card.icon}</Box>
                <Typography variant="h3" component="div" gutterBottom>
                  {card.value}
                </Typography>
                <Typography variant="h6" component="div">
                  {card.title}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Box mt={4}>
        <Typography variant="h5" gutterBottom>
          Welcome to OpsNow Management System
        </Typography>
        <Typography variant="body1" color="text.secondary">
          This dashboard provides an overview of your organization's data and
          system activity. Use the navigation menu to manage employees,
          departments, locations, and tiers. Monitor API usage through the
          logging dashboard.
        </Typography>
      </Box>
    </Box>
  );
};

export default Dashboard;
