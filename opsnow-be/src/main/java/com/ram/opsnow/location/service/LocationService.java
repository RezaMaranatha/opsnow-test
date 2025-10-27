package com.ram.opsnow.location.service;

import com.ram.opsnow.location.dto.LocationDTO;
import com.ram.opsnow.util.PaginationResponse;

public interface LocationService {
    PaginationResponse<LocationDTO> getAllLocation(int pageNumber, int pageSize, String sortBy, String locationCode,
            String locationName, String locationAddress);

    LocationDTO getLocation(String locationCode);

    LocationDTO createLocation(LocationDTO locationDTO);

    LocationDTO updateLocation(LocationDTO locationDTO);

    String deleteLocation(String locationCode);
}
