package com.dairyncia.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyncia.custom_exception.ResourceNotFoundException;
import com.dairyncia.dto.FarmerDashboardDto;
import com.dairyncia.dto.FarmerMilkDto;
import com.dairyncia.entities.Farmer;
import com.dairyncia.entities.MilkCollection;
import com.dairyncia.entities.User;
import com.dairyncia.enums.PaymentStatus;
import com.dairyncia.repository.BankDetailsRepository;
import com.dairyncia.repository.FarmerRepository;
import com.dairyncia.repository.MilkCollectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final MilkCollectionRepository milkCollectionRepository;

    @Override
    @Transactional(readOnly = true)
    public FarmerDashboardDto getCompleteDashboard(User user) {

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        FarmerDashboardDto dashboard = new FarmerDashboardDto();

        // ================= BASIC INFO =================
        dashboard.setFarmerId(farmer.getId());
        dashboard.setFullName(user.getFullName());
        dashboard.setEmail(user.getEmail());
        dashboard.setPhone(user.getPhoneNumber());

        // ================= BANK DETAILS =================
        bankDetailsRepository.findByUserId(user.getId()).ifPresent(bank -> {
            dashboard.setBankName(bank.getBankName());
            dashboard.setAccountNumber(bank.getAccountNumber());
            dashboard.setIfsc(bank.getIfsc());
            dashboard.setAccountHolderName(bank.getAccountHolderName());
        });

        // ✅ FETCH JOIN METHOD (VERY IMPORTANT)
        List<MilkCollection> collections =
                milkCollectionRepository.findByFarmerIdWithManager(farmer.getId());

        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;

        List<FarmerMilkDto> milkDtos = new ArrayList<>();

        for (MilkCollection mc : collections) {

            FarmerMilkDto dto = new FarmerMilkDto();

            dto.setDairyCenterName(
                mc.getManager() != null ? mc.getManager().getFullName() : "N/A"
            );
            dto.setMilkType(mc.getMilkType().name());
            dto.setMilkShift(mc.getMilkShift().name());
            dto.setQuantity(mc.getQuantity());
            dto.setTotalAmount(mc.getTotalAmount());
            dto.setPaymentStatus(mc.getPaymentStatus().name());

            // Rate per liter (safe divide)
            if (mc.getQuantity() != null &&
                mc.getQuantity().compareTo(BigDecimal.ZERO) > 0 &&
                mc.getTotalAmount() != null) {

                dto.setRatePerLiter(
                    mc.getTotalAmount()
                      .divide(mc.getQuantity(), 2, RoundingMode.HALF_UP)
                );
            }

            // ================= SUMMARY =================
            if (mc.getPaymentStatus() == PaymentStatus.PAID) {
                totalPaid = totalPaid.add(mc.getTotalAmount());
            } else {
                totalPending = totalPending.add(mc.getTotalAmount());
            }

            milkDtos.add(dto);
        }

        dashboard.setMilkCollections(milkDtos);
        dashboard.setTotalPaidAmount(totalPaid);
        dashboard.setTotalPendingAmount(totalPending);

        return dashboard;
    }
}
