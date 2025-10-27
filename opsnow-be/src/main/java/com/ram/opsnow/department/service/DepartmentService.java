package com.ram.opsnow.department.service;

import com.ram.opsnow.department.dto.DepartmentDTO;
import com.ram.opsnow.util.PaginationResponse;

public interface DepartmentService {
    PaginationResponse<DepartmentDTO> getAllDepartment(int pageNumber, int pageSize, String sortBy,
            String departmentCode,
            String departmentName);

    DepartmentDTO getDepartment(String departmentCode);

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO updateLocation(DepartmentDTO departmentDTO);

    String deleteLocation(String departmentCode);
}
