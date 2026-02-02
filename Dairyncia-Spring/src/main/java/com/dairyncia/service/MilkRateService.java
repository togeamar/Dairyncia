package com.dairyncia.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dairyncia.entities.MilkRate;
import com.dairyncia.enums.MilkType;

public interface MilkRateService {
    
    
    void saveAllMilkRates(List<MilkRate> milkRates, MilkType milkType);
    

    Page<MilkRate> getMilkRates(MilkType milkType, Pageable pageable);
    

    MilkRate findExactRate(BigDecimal fat, BigDecimal snf, MilkType milkType);
    

    MilkRate findNearestRate(BigDecimal fat, BigDecimal snf, MilkType milkType);
}