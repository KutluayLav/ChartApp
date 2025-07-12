package com.kutluaylav.data_chart_backend.ChartService.controller;

import com.kutluaylav.data_chart_backend.ChartService.dto.request.DataFetchRequest;
import com.kutluaylav.data_chart_backend.ChartService.dto.request.DbConnectionRequest;
import com.kutluaylav.data_chart_backend.ChartService.dto.response.ChartDataResponse;
import com.kutluaylav.data_chart_backend.ChartService.dto.response.DataObjectDto;
import com.kutluaylav.data_chart_backend.ChartService.service.ChartService;
import com.kutluaylav.data_chart_backend.UserService.dto.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chart")
@CrossOrigin(origins = "http://localhost:3000")
public class ChartController {

    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @PostMapping("/fetch")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SuccessResponse<ChartDataResponse>> fetchChartData(@Valid @RequestBody DataFetchRequest request) throws SQLException {
        ChartDataResponse dataResponse = chartService.fetchChartData(request);

        SuccessResponse<ChartDataResponse> successResponse = SuccessResponse.<ChartDataResponse>builder()
                .message("Chart data fetched successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(dataResponse)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/listObjects")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SuccessResponse<List<DataObjectDto>>> listObjects(@Valid @RequestBody DbConnectionRequest connection) throws SQLException {
        List<DataObjectDto> objects = chartService.listDatabaseObjects(connection);

        SuccessResponse<List<DataObjectDto>> response = SuccessResponse.<List<DataObjectDto>>builder()
                .message("Database objects fetched successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(objects)
                .build();

        return ResponseEntity.ok(response);
    }


}
