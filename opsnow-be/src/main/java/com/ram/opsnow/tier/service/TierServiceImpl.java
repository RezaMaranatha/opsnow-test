package com.ram.opsnow.tier.service;

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
import com.ram.opsnow.tier.dto.TierDTO;
import com.ram.opsnow.tier.entity.Tier;
import com.ram.opsnow.tier.repository.TierRepository;
import com.ram.opsnow.util.PaginationResponse;
import com.ram.opsnow.util.TierSpecification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TierServiceImpl implements TierService {
	private final TierRepository tierRepository;
	private final ModelMapper modelMapper;

	public TierServiceImpl(TierRepository tierRepository, ModelMapper modelMapper) {
		this.tierRepository = tierRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public PaginationResponse<TierDTO> getAllTier(int pageNumber, int pageSize, String sortBy, String tierCode,
			String tierName) {
		Sort orderBy = Sort.by(Sort.Order.asc(sortBy));
		Pageable pageable = PageRequest.of(pageNumber, pageSize, orderBy);

		Specification<Tier> spec = Specification.allOf(TierSpecification.hasTierCode(tierCode),
				TierSpecification.hasTierName(tierName));

		var tiers = tierRepository.findAll(spec, pageable);
		List<TierDTO> tierDTOs = tiers.getContent().stream()
				.map(tier -> modelMapper.map(tier, TierDTO.class))
				.toList();

		return new PaginationResponse<>(
				tierDTOs,
				tiers.getNumber(),
				tiers.getSize(),
				tiers.getTotalElements(),
				tiers.getTotalPages());
	}

	@Override
	public TierDTO getTier(String tierCode) {
		var tier = tierRepository.findById(tierCode)
				.orElseThrow(() -> new DataNotFoundException("Tier not found with code: " + tierCode));
		return modelMapper.map(tier, TierDTO.class);
	}

	@Override
	public TierDTO createTier(TierDTO tierDTO) {
		if (tierDTO.getTierCode() == null || tierDTO.getTierCode().isEmpty()) {
			throw new InvalidDataException("Tier code cannot be null or empty.");
		}

		if (tierRepository.existsById(tierDTO.getTierCode())) {
			throw new DataAlreadyExistException("Tier with code " + tierDTO.getTierCode() + " already exists.");
		}

		var tier = tierRepository.save(modelMapper.map(tierDTO, Tier.class));
		return modelMapper.map(tier, TierDTO.class);
	}

	@Override
	public String deleteTier(String tierCode) {
		try {
			tierRepository.deleteById(tierCode);
		} catch (Exception e) {
			return "Failed to delete tier with code: " + tierCode;
		}
		return "Tier with code " + tierCode + " deleted successfully.";
	}

	@Override
	public TierDTO updateTier(TierDTO tierDTO) {
		if (!tierRepository.existsById(tierDTO.getTierCode())) {
			throw new DataNotFoundException("Tier not found with code: " + tierDTO.getTierCode());
		}
		var tier = tierRepository.save(modelMapper.map(tierDTO, Tier.class));
		return modelMapper.map(tier, TierDTO.class);
	}
}