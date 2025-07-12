package com.kutluaylav.data_chart_backend.ChartService.service;

import com.kutluaylav.data_chart_backend.ChartService.dto.request.DataFetchRequest;
import com.kutluaylav.data_chart_backend.ChartService.dto.request.DbConnectionRequest;
import com.kutluaylav.data_chart_backend.ChartService.dto.response.ChartDataResponse;
import com.kutluaylav.data_chart_backend.ChartService.dto.response.DataObjectDto;

import java.sql.SQLException;
import java.util.List;

public interface IChartService {
    ChartDataResponse fetchChartData(DataFetchRequest request) throws SQLException;
    List<DataObjectDto> listDatabaseObjects(DbConnectionRequest connection) throws SQLException;
}
