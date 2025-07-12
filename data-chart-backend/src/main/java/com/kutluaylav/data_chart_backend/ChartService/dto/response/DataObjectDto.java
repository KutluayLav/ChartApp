package com.kutluaylav.data_chart_backend.ChartService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataObjectDto {
    private String name;
    private String type;
    private List<String> columns;

}
