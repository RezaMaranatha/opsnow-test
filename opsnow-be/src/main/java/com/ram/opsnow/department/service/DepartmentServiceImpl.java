package com.ram.opsnow.department.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.ram.opsnow.department.dto.DepartmentDTO;
import com.ram.opsnow.department.entity.Department;
import com.ram.opsnow.department.repository.DepartmentRepository;
import com.ram.opsnow.exception.DataAlreadyExistException;
import com.ram.opsnow.exception.DataNotFoundException;
import com.ram.opsnow.exception.InvalidDataException;
import com.ram.opsnow.util.DepartmentSpecification;
import com.ram.opsnow.util.PaginationResponse;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaginationResponse<DepartmentDTO> getAllDepartment(int pageNumber, int pageSize, String sortBy,
            String departmentCode, String departmentName) {
        Sort orderBy = Sort.by(Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, orderBy);

        Specification<Department> spec = Specification.allOf(DepartmentSpecification.hasDepartmentCode(departmentCode),
                DepartmentSpecification.hasDepartmentName(departmentName));

        var departments = departmentRepository.findAll(spec, pageable);
        List<DepartmentDTO> departmentDTOs = departments.getContent().stream()
                .map(tier -> modelMapper.map(tier, DepartmentDTO.class))
                .toList();

        return new PaginationResponse<>(
                departmentDTOs,
                departments.getNumber(),
                departments.getSize(),
                departments.getTotalElements(),
                departments.getTotalPages());
    }

    @Override
    public DepartmentDTO getDepartment(String departmentCode) {
        var tier = departmentRepository.findById(departmentCode)
                .orElseThrow(() -> new DataNotFoundException("Department not found with code: " + departmentCode));
        return modelMapper.map(tier, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentDTO.getDepartmentCode() == null || departmentDTO.getDepartmentCode().isEmpty()) {
            throw new InvalidDataException("Department code cannot be null or empty.");
        }

        if (departmentRepository.existsById(departmentDTO.getDepartmentCode())) {
            throw new DataAlreadyExistException(
                    "Department with code " + departmentDTO.getDepartmentCode() + " already exists.");
        }

        var department = departmentRepository.save(modelMapper.map(departmentDTO, Department.class));
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO updateLocation(DepartmentDTO departmentDTO) {
        if (!departmentRepository.existsById(departmentDTO.getDepartmentCode())) {
            throw new DataNotFoundException("Department not found with code: " + departmentDTO.getDepartmentCode());
        }
        var department = departmentRepository.save(modelMapper.map(departmentDTO, Department.class));
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public String deleteLocation(String departmentCode) {
        try {
            departmentRepository.deleteById(departmentCode);
        } catch (Exception e) {
            return "Failed to delete department with code: " + departmentCode;
        }
        return "Department with code " + departmentCode + " deleted successfully.";
    }

}
