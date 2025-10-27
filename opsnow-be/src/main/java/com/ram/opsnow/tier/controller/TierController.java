package com.ram.opsnow.tier.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ram.opsnow.tier.dto.TierDTO;
import com.ram.opsnow.tier.service.TierService;
import com.ram.opsnow.util.PaginationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tier")
public class TierController {
    private final TierService tierService;

    public TierController(TierService tierService) {
        this.tierService = tierService;
    }

    @GetMapping
    public PaginationResponse<TierDTO> getAllTier(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "tierCode") String sortBy,
            @RequestParam(required = false) String tierCode,
            @RequestParam(required = false) String tierName) {
        return tierService.getAllTier(pageNumber, pageSize, sortBy, tierCode, tierName);
    }

    @GetMapping(value = "/{tierCode}")
    public TierDTO getTier(@PathVariable String tierCode) {
        return tierService.getTier(tierCode);
    }

    @PostMapping
    public TierDTO createTier(@RequestBody TierDTO tierDTO) {
        return tierService.createTier(tierDTO);
    }

    @PutMapping
    public TierDTO updateTier(@RequestBody TierDTO tierDTO) {
        return tierService.updateTier(tierDTO);
    }

    @DeleteMapping(value = "/{tierCode}")
    public String deleteTier(@PathVariable String tierCode) {
        return tierService.deleteTier(tierCode);
    }
}
