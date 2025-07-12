package com.kutluaylav.data_chart_backend.ChartService.dto.request;

import lombok.Data;

@Data
public class DbConnectionRequest {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
}
