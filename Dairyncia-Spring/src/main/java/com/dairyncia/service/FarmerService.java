package com.dairyncia.service;

import com.dairyncia.dto.FarmerDashboardDto;
import com.dairyncia.entities.User;

public interface FarmerService {

    FarmerDashboardDto getCompleteDashboard(User loggedInUser);
}
