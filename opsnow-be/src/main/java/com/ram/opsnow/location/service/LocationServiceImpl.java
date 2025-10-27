package com.ram.opsnow.location.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ram.opsnow.exception.DataAlreadyExistException;
import com.ram.opsnow.exception.DataNotFoundException;
import com.ram.opsnow.exception.InvalidDataException;
import com.ram.opsnow.location.dto.LocationDTO;
import com.ram.opsnow.location.entity.Location;
import com.ram.opsnow.location.repository.LocationRepository;
import com.ram.opsnow.util.LocationSpecification;
import com.ram.opsnow.util.PaginationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    public LocationServiceImpl(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaginationResponse<LocationDTO> getAllLocation(int pageNumber, int pageSize, String sortBy,
            String locationCode, String locationName, String locationAddress) {
        Sort orderBy = Sort.by(Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, orderBy);

        Specification<Location> spec = Specification.allOf(LocationSpecification.hasLocationCode(locationCode),
                LocationSpecification.hasLocationName(locationName),
                LocationSpecification.hasLocationAddress(locationAddress));

        var locations = locationRepository.findAll(spec, pageable);
        List<LocationDTO> locationDTOs = locations.getContent().stream()
                .map(tier -> modelMapper.map(tier, LocationDTO.class))
                .toList();

        return new PaginationResponse<>(
                locationDTOs,
                locations.getNumber(),
                locations.getSize(),
                locations.getTotalElements(),
                locations.getTotalPages());
    }

    @Override
    public LocationDTO getLocation(String locationCode) {
        var tier = locationRepository.findById(locationCode)
                .orElseThrow(() -> new DataNotFoundException("Location not found with code: " + locationCode));
        return modelMapper.map(tier, LocationDTO.class);
    }

    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        if (locationDTO.getLocationCode() == null || locationDTO.getLocationCode().isEmpty()) {
            throw new InvalidDataException("Location code cannot be null or empty.");
        }

        if (locationRepository.existsById(locationDTO.getLocationCode())) {
            throw new DataAlreadyExistException(
                    "Location with code " + locationDTO.getLocationCode() + " already exists.");
        }

        var location = locationRepository.save(modelMapper.map(locationDTO, Location.class));
        return modelMapper.map(location, LocationDTO.class);
    }

    @Override
    public LocationDTO updateLocation(LocationDTO locationDTO) {
        if (!locationRepository.existsById(locationDTO.getLocationName())) {
            throw new DataNotFoundException("Location not found with code: " + locationDTO.getLocationCode());
        }
        var location = locationRepository.save(modelMapper.map(locationDTO, Location.class));
        return modelMapper.map(location, LocationDTO.class);
    }

    @Override
    public String deleteLocation(String locationCode) {
        try {
            locationRepository.deleteById(locationCode);
        } catch (Exception e) {
            return "Failed to delete location with code: " + locationCode;
        }
        return "Location with code " + locationCode + " deleted successfully.";
    }
}