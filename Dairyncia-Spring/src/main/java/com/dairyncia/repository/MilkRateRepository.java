package com.dairyncia.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dairyncia.entities.MilkRate;
import com.dairyncia.enums.MilkType;

@Repository
public interface MilkRateRepository extends JpaRepository<MilkRate, Long> {
    
    void deleteByRateType(MilkType rateType);
    
    Page<MilkRate> findByRateType(MilkType rateType, Pageable pageable);
    
    Optional<MilkRate> findByFatAndSnfAndRateType(BigDecimal fat, BigDecimal snf, MilkType rateType);
    
    @Query("""
        SELECT m FROM MilkRate m
        WHERE m.rateType = :milkType
        ORDER BY (ABS(m.fat - :fat) + ABS(m.snf - :snf)) ASC,m.fat ASC,m.snf ASC""")
    List<MilkRate> findNearestRate(
            @Param("fat") BigDecimal fat, 
            @Param("snf") BigDecimal snf, 
            @Param("milkType") MilkType milkType);
}