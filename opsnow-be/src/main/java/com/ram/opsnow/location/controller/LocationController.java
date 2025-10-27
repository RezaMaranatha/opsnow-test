package com.ram.opsnow.location.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ram.opsnow.location.dto.LocationDTO;
import com.ram.opsnow.location.service.LocationService;
import com.ram.opsnow.util.PaginationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public PaginationResponse<LocationDTO> getAllTier(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "locationName") String sortBy,
            @RequestParam(required = false) String locationCode,
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) String locationAddress) {
        return locationService.getAllLocation(pageNumber, pageSize, sortBy, locationCode, locationName,
                locationAddress);
    }

    @GetMapping(value = "/{locationCode}")
    public LocationDTO getTier(@PathVariable String locationCode) {
        return locationService.getLocation(locationCode);
    }

    @PostMapping
    public LocationDTO createTier(@RequestBody LocationDTO locationDTO) {
        return locationService.createLocation(locationDTO);
    }

    @PutMapping
    public LocationDTO updateTier(@RequestBody LocationDTO locationDTO) {
        return locationService.updateLocation(locationDTO);
    }

    @DeleteMapping(value = "/{locationCode}")
    public String deleteTier(@PathVariable String locationCode) {
        return locationService.deleteLocation(locationCode);
    }
}