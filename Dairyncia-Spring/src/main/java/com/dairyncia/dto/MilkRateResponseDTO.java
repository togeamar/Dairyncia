package com.dairyncia.dto;

import java.util.List;

import com.dairyncia.entities.MilkRate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilkRateResponseDTO {
    
    private Long totalCount;
    private Integer pageNumber;
    private Integer pageSize;
    private List<MilkRate> data;
}