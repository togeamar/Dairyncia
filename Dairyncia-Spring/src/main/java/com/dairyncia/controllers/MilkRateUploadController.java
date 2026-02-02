package com.dairyncia.controllers;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.CalculateRateDto;
import com.dairyncia.dto.MilkRateResponseDTO;
import com.dairyncia.entities.MilkRate;
import com.dairyncia.enums.MilkType;
import com.dairyncia.service.MilkRateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/milk-rate")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Milk Rate Management", description = "APIs for uploading and managing milk rate matrices")
public class MilkRateUploadController {

    private final MilkRateService milkRateService;

    @Operation(
        summary = "Upload milk rates from Excel file",
        description = "Upload an Excel file containing milk rate matrix. File should have FAT values in first column, SNF values in header row, and rates in cells."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid file or data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Admin role required"
        )
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadMilkRates(
            @Parameter(description = "Excel file (.xlsx or .xls) containing milk rates", required = true)
            @RequestPart("file") MultipartFile file,
            
            @Parameter(description = "Type of milk (COW or BUFFALO)", required = true, example = "COW")
            @RequestParam("milkType") MilkType milkType) {

        // Validate file
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Upload MilkRates in Excel", "ERROR"));
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") 
                && !fileName.toLowerCase().endsWith(".xls"))) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Only Excel Files Are Allowed", "ERROR"));
        }

        try {
            List<MilkRate> milkRates = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            // Read Excel file
            try (InputStream inputStream = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet worksheet = workbook.getSheetAt(0);
                if (worksheet == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Excel file is Empty or invalid", "ERROR"));
                }

                int rowCount = worksheet.getLastRowNum() + 1;
                if (rowCount < 2) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(
                                    "Excel file must contain headers and at least one data row", 
                                    "ERROR"));
                }

                // Get first row to determine column count
                Row headerRow = worksheet.getRow(0);
                int colCount = headerRow.getLastCellNum();

                // Process each row (starting from row 2, index 1)
                for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
                    Row row = worksheet.getRow(rowIndex);
                    if (row == null) continue;

                    try {
                        // Get FAT value from first column
                        String fatValue = getCellValueAsString(row.getCell(0));
                        if (fatValue == null || fatValue.trim().isEmpty()) continue;

                        // Process each column for SNF values
                        for (int colIndex = 1; colIndex < colCount; colIndex++) {
                            // Get SNF value from header row
                            String snfValue = getCellValueAsString(headerRow.getCell(colIndex));
                            if (snfValue == null || snfValue.trim().isEmpty()) continue;

                            // Get rate value from current cell
                            String rateValue = getCellValueAsString(row.getCell(colIndex));
                            if (rateValue != null && !rateValue.trim().isEmpty()) {
                                try {
                                    BigDecimal fat = new BigDecimal(fatValue.trim());
                                    BigDecimal snf = new BigDecimal(snfValue.trim());
                                    BigDecimal rate = new BigDecimal(rateValue.trim());

                                    // Validate ranges
                                    if (snf.compareTo(BigDecimal.ZERO) < 0 
                                            || snf.compareTo(new BigDecimal("12")) > 0) {
                                        errors.add("Row " + (rowIndex + 1) 
                                                + ": SNF must be between 0 and 12");
                                        continue;
                                    }

                                    if (rate.compareTo(BigDecimal.ZERO) < 0 
                                            || rate.compareTo(new BigDecimal("1000")) > 0) {
                                        errors.add("Row " + (rowIndex + 1) 
                                                + ": Rate must be between 0 and 1000");
                                        continue;
                                    }

                                    // Create MilkRate entity
                                    MilkRate milkRate = new MilkRate();
                                    milkRate.setFat(fat);
                                    milkRate.setSnf(snf);
                                    milkRate.setRate(rate);
                                    milkRate.setRateType(milkType);

                                    milkRates.add(milkRate);

                                } catch (NumberFormatException e) {
                                    errors.add("Row " + (rowIndex + 1) 
                                            + ": Invalid numeric value - " + e.getMessage());
                                }
                            }
                        }
                    } catch (Exception ex) {
                        errors.add("Row " + (rowIndex + 1) + ": " + ex.getMessage());
                    }
                }
            }

            if (milkRates.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(
                                "No valid data found in the Excel file. Errors: " 
                                        + String.join(", ", errors), 
                                "ERROR"));
            }

            // Save to database
            milkRateService.saveAllMilkRates(milkRates, milkType);

            log.info("Successfully imported {} milk rates", milkRates.size());

            return ResponseEntity.ok(new ApiResponse(
                    "Successfully uploaded " + milkRates.size() + " rate entries.", 
                    "SUCCESS"));

        } catch (Exception ex) {
            log.error("Error uploading milk rates", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(
                            "Error processing file: " + ex.getMessage(), 
                            "ERROR"));
        }
    }

    @Operation(
        summary = "Get all milk rates with pagination",
        description = "Retrieve milk rates for a specific milk type with pagination support"
    )
    @GetMapping("/getallrates")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MilkRateResponseDTO> getMilkRates(
            @Parameter(description = "Page number (1-indexed)", example = "1")
            @RequestParam(defaultValue = "1") int pageNumber,
            
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,
            
            @Parameter(description = "Type of milk", required = true, example = "COW")
            @RequestParam MilkType milkType) {

        // Convert to 0-based page index
        Pageable pageable = PageRequest.of(
                pageNumber - 1, 
                pageSize, 
                Sort.by("fat").ascending().and(Sort.by("snf").ascending()));

        Page<MilkRate> milkRatesPage = milkRateService.getMilkRates(milkType, pageable);

        MilkRateResponseDTO response = MilkRateResponseDTO.builder()
                .totalCount(milkRatesPage.getTotalElements())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .data(milkRatesPage.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Calculate milk rate",
        description = "Get the rate for given FAT and SNF values. Returns exact match if available, otherwise returns nearest match."
    )
    @PostMapping("/calculate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRate(
            @Parameter(description = "Fat percentage", required = true, example = "3.5")
            @RequestParam BigDecimal fat,
            
            @Parameter(description = "SNF percentage", required = true, example = "8.7")
            @RequestParam BigDecimal snf,
            
            @Parameter(description = "Type of milk", required = true, example = "COW")
            @RequestParam MilkType milkType) {

        MilkRate exactRate = milkRateService.findExactRate(fat, snf, milkType);

        if (exactRate != null) {
            return ResponseEntity.ok(new CalculateRateDto(
                    true,
                    exactRate.getFat(),
                    exactRate.getSnf(),
                    exactRate.getRate(),
                    exactRate.getRateType(),
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }

        // Find nearest rate
        MilkRate nearestRate = milkRateService.findNearestRate(fat, snf, milkType);

        if (nearestRate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(
                            "No rate found for Fat: " + fat + ", SNF: " + snf 
                                    + ", Type: " + milkType, 
                            "ERROR"));
        }

        return ResponseEntity.ok(new CalculateRateDto(
                false,
                nearestRate.getFat(),
                nearestRate.getSnf(),
                nearestRate.getRate(),
                nearestRate.getRateType(),
                "Exact rate not found, returning nearest match",
                fat,
                snf,
                nearestRate.getFat(),
                nearestRate.getSnf()
        ));
    }

    /**
     * Helper method to extract cell value as String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            try {
                return String.valueOf(cell.getNumericCellValue());
            } catch (Exception e) {
                return cell.getStringCellValue();
            }
        }

        return null;
    }
}