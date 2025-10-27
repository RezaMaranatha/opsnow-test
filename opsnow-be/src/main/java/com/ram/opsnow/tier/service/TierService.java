package com.ram.opsnow.tier.service;

import com.ram.opsnow.tier.dto.TierDTO;
import com.ram.opsnow.util.PaginationResponse;

public interface TierService {
    PaginationResponse<TierDTO> getAllTier(int pageNumber, int pageSize, String sortBy, String tierCode,
            String tierName);

    TierDTO getTier(String tierCode);

    TierDTO createTier(TierDTO tierDTO);

    TierDTO updateTier(TierDTO tierDTO);

    String deleteTier(String tierCode);
}
