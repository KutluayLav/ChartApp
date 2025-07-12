package com.kutluaylav.data_chart_backend.ChartService.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataResponse {

    private List<String> labels;
    private List<List<Object>> data;

}
