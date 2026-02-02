package com.dairyncia.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyncia.entities.MilkRate;
import com.dairyncia.enums.MilkType;
import com.dairyncia.repository.MilkRateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MilkRateServiceImpl implements MilkRateService {

    private final MilkRateRepository milkRateRepository;

    @Override
    public void saveAllMilkRates(List<MilkRate> milkRates, MilkType milkType) {

        milkRateRepository.deleteByRateType(milkType);
        

        milkRateRepository.saveAll(milkRates);
        
        log.info("Saved {} milk rates for type {}", milkRates.size(), milkType);
    }

    @Override
    public Page<MilkRate> getMilkRates(MilkType milkType, Pageable pageable) {
        return milkRateRepository.findByRateType(milkType, pageable);
    }

    @Override
    public MilkRate findExactRate(BigDecimal fat, BigDecimal snf, MilkType milkType) {
        return milkRateRepository.findByFatAndSnfAndRateType(fat, snf, milkType)
                .orElse(null);
    }

    @Override
    public MilkRate findNearestRate(BigDecimal fat, BigDecimal snf, MilkType milkType) {
        return milkRateRepository.findNearestRate(fat, snf, milkType)
                .stream()
                .findFirst()
                .orElse(null);
    }
}