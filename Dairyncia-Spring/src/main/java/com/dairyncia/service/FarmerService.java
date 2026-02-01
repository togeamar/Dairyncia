package com.dairyncia.service;

import java.util.List;

import com.dairyncia.dto.FarmerDashboardDto;
import com.dairyncia.dto.FarmerListDTO;
import com.dairyncia.entities.User;

public interface FarmerService {

    FarmerDashboardDto getCompleteDashboard(User loggedInUser);
//    List<FarmerListDTO> getAllFarmers();
}
