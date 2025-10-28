# OpsNow Frontend

A comprehensive React frontend for the OpsNow management system.

## Features

- **Authentication**: JWT-based login system
- **Employee Management**: CRUD operations for employees
- **Department Management**: Manage organizational departments
- **Location Management**: Manage office locations
- **Tier Management**: Manage employee tiers/levels
- **API Logging Dashboard**: Monitor API usage and performance
- **Responsive Design**: Material-UI based modern interface

## Setup Instructions

### 1. Install Dependencies

```bash
cd opsnow-fe
npm install
```

### 2. Configure API URL

Create a `.env` file in the root directory:

```
REACT_APP_API_URL=http://localhost:8080/api
```

### 3. Start Development Server

```bash
npm start
```

The application will open at `http://localhost:3000`

## Usage

### Login

1. Navigate to `http://localhost:3000`
2. Use the login credentials from your backend
3. Default test user: `test@example.com` / `password123`

### Navigation

- **Dashboard**: Overview of system statistics
- **Employees**: Manage employee records
- **Departments**: Manage organizational departments
- **Locations**: Manage office locations
- **Tiers**: Manage employee tiers
- **API Logs**: Monitor API usage and performance

## API Integration

The frontend integrates with all backend APIs:

- **Authentication**: `/api/auth/login`, `/api/auth/logout`
- **Employees**: `/api/employees/*`
- **Departments**: `/api/departments/*`
- **Locations**: `/api/locations/*`
- **Tiers**: `/api/tiers/*`
- **Logging**: `/api/logging/*`

## Components Structure

```
src/
├── components/
│   ├── auth/
│   │   ├── Login.js
│   │   └── ProtectedRoute.js
│   ├── dashboard/
│   │   └── Dashboard.js
│   ├── employees/
│   │   └── EmployeeManagement.js
│   ├── departments/
│   │   └── DepartmentManagement.js
│   ├── locations/
│   │   └── LocationManagement.js
│   ├── tiers/
│   │   └── TierManagement.js
│   ├── logging/
│   │   └── ApiLoggingDashboard.js
│   └── layout/
│       └── Layout.js
├── contexts/
│   └── AuthContext.js
├── services/
│   └── api.js
└── App.js
```

## Technologies Used

- **React 19**: Frontend framework
- **Material-UI**: UI component library
- **React Router**: Client-side routing
- **Axios**: HTTP client for API calls
- **Day.js**: Date manipulation
- **MUI X Data Grid**: Advanced data tables
- **MUI X Date Pickers**: Date selection components

## Features by Component

### Employee Management

- View all employees in a data grid
- Add new employees with form validation
- Edit existing employee information
- Delete employees with confirmation
- Search and filter capabilities

### Department Management

- CRUD operations for departments
- Department code and name management
- Description field for additional details

### Location Management

- Manage office locations
- Address, city, state, country fields
- Location code system

### Tier Management

- Employee tier/level management
- Tier codes and descriptions
- Level hierarchy support

### API Logging Dashboard

- Real-time API call monitoring
- Filter by date range, user, endpoint, method, status
- Performance metrics (response times)
- Pagination for large datasets
- Status code color coding

## Authentication Flow

1. User enters credentials on login page
2. Frontend sends credentials to `/api/auth/login`
3. Backend returns JWT token and user information
4. Token stored in localStorage
5. All subsequent API calls include token in Authorization header
6. Token automatically refreshed or user redirected to login on expiration

## Error Handling

- Global error handling for API calls
- User-friendly error messages
- Automatic logout on authentication errors
- Loading states for better UX

## Responsive Design

- Mobile-first approach
- Responsive navigation drawer
- Adaptive data grids
- Touch-friendly interface

## Development

### Available Scripts

- `npm start`: Start development server
- `npm build`: Build for production
- `npm test`: Run tests
- `npm eject`: Eject from Create React App

### Environment Variables

- `REACT_APP_API_URL`: Backend API base URL (default: http://localhost:8080/api)

## Production Deployment

1. Build the application:

   ```bash
   npm run build
   ```

2. Deploy the `build` folder to your web server

3. Configure environment variables for production API URL

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure backend has CORS configured for frontend URL
2. **Authentication Issues**: Check JWT token configuration
3. **API Connection**: Verify backend is running and accessible
4. **Build Errors**: Check for missing dependencies

### Debug Mode

Enable debug logging by setting:

```
REACT_APP_DEBUG=true
```

This will show detailed API request/response information in the browser console.
