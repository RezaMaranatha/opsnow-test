package com.ram.opsnow.department.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ram.opsnow.department.dto.DepartmentDTO;
import com.ram.opsnow.department.service.DepartmentService;
import com.ram.opsnow.util.PaginationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public PaginationResponse<DepartmentDTO> getAllDepartment(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "departmentName") String sortBy,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(required = false) String departmentName) {
        return departmentService.getAllDepartment(pageNumber, pageSize, sortBy, departmentCode, departmentName);
    }

    @GetMapping(value = "/{departmentCode}")
    public DepartmentDTO getDepartment(@PathVariable String departmentCode) {
        return departmentService.getDepartment(departmentCode);
    }

    @PostMapping
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.createDepartment(departmentDTO);
    }

    @PutMapping
    public DepartmentDTO updateTier(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.updateDepartment(departmentDTO);
    }

    @DeleteMapping(value = "/{departmentCode}")
    public String deleteTier(@PathVariable String departmentCode) {
        return departmentService.deleteDepartment(departmentCode);
    }
}
