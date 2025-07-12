package com.kutluaylav.data_chart_backend.ChartService.dto.request;

import lombok.Data;

@Data
public class DataFetchRequest {
    private DbConnectionRequest connection;
    private QueryRequest query;
    private MappingInfo mapping;
}
