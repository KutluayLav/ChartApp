package com.kutluaylav.data_chart_backend.ChartService.service;


import com.kutluaylav.data_chart_backend.ChartService.dto.request.DataFetchRequest;
import com.kutluaylav.data_chart_backend.ChartService.dto.request.DbConnectionRequest;
import com.kutluaylav.data_chart_backend.ChartService.dto.response.ChartDataResponse;
import com.kutluaylav.data_chart_backend.ChartService.dto.response.DataObjectDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChartService implements IChartService {

    @Override
    public ChartDataResponse fetchChartData(DataFetchRequest request) throws SQLException {
        String url = String.format(
                "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
                request.getConnection().getHost(),
                request.getConnection().getPort(),
                request.getConnection().getDatabase()
        );

        String username = request.getConnection().getUsername();
        String password = request.getConnection().getPassword();
        String objectName = request.getQuery().getObjectName();
        String objectType = request.getQuery().getObjectType();

        // Mapping bilgileri
        String xAxis = request.getMapping() != null ? request.getMapping().getXAxis() : null;
        String yAxis = request.getMapping() != null ? request.getMapping().getYAxis() : null;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql;

            switch (objectType.toUpperCase()) {
                case "STORED_PROCEDURE":
                    sql = "{call " + objectName + "}";
                    break;
                case "VIEW":
                case "TABLE":
                    sql = "SELECT * FROM " + objectName;
                    break;
                case "FUNCTION":
                    sql = "SELECT * FROM dbo." + objectName + "()";
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported object type: " + objectType);
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<String> labels = new ArrayList<>();
            List<List<Object>> data = new ArrayList<>();

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();


            if (xAxis != null && yAxis != null) {
                labels.add(xAxis);
                labels.add(yAxis);
            } else {
                for (int i = 1; i <= columnCount; i++) {
                    labels.add(meta.getColumnLabel(i));
                }
            }

            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                if (xAxis != null && yAxis != null) {

                    int xIndex = -1, yIndex = -1;
                    for (int i = 1; i <= columnCount; i++) {
                        String colName = meta.getColumnLabel(i);
                        if (colName.equalsIgnoreCase(xAxis)) {
                            xIndex = i;
                        }
                        if (colName.equalsIgnoreCase(yAxis)) {
                            yIndex = i;
                        }
                    }
                    if (xIndex == -1 || yIndex == -1) {
                        throw new IllegalArgumentException("Mapping error: xAxis or yAxis column not found.");
                    }
                    row.add(rs.getObject(xIndex));
                    row.add(rs.getObject(yIndex));
                } else {

                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                }
                data.add(row);
            }

            ChartDataResponse response = new ChartDataResponse();
            response.setLabels(labels);
            response.setData(data);
            return response;
        }
    }

    @Override
    public List<DataObjectDto> listDatabaseObjects(DbConnectionRequest connection) throws SQLException {
        String url = String.format(
                "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
                connection.getHost(),
                connection.getPort(),
                connection.getDatabase()
        );

        log.info("Connecting to database at URL: {}", url);

        try (Connection conn = DriverManager.getConnection(url, connection.getUsername(), connection.getPassword())) {
            log.info("Connected successfully with user: {}", connection.getUsername());

            List<DataObjectDto> result = new ArrayList<>();


            String tableViewQuery = "SELECT TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE IN ('BASE TABLE', 'VIEW')";
            try (PreparedStatement ps = conn.prepareStatement(tableViewQuery);
                 ResultSet rs = ps.executeQuery()) {
                log.info("Fetching tables and views...");
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");

                    List<String> columns = new ArrayList<>();
                    String columnQuery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
                    try (PreparedStatement psCols = conn.prepareStatement(columnQuery)) {
                        psCols.setString(1, tableName);
                        try (ResultSet rsCols = psCols.executeQuery()) {
                            while (rsCols.next()) {
                                columns.add(rsCols.getString("COLUMN_NAME"));
                            }
                        }
                    }

                    log.info("Found {} with type {}, columns: {}", tableName, tableType, columns);
                    result.add(new DataObjectDto(tableName, tableType, columns));
                }
            }

            String procedureQuery = "SELECT SPECIFIC_NAME FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_TYPE = 'PROCEDURE'";
            try (PreparedStatement ps = conn.prepareStatement(procedureQuery);
                 ResultSet rs = ps.executeQuery()) {
                log.info("Fetching stored procedures...");
                while (rs.next()) {
                    String procedureName = rs.getString("SPECIFIC_NAME");
                    log.info("Found stored procedure: {}", procedureName);
                    result.add(new DataObjectDto(procedureName, "STORED_PROCEDURE", null));
                }
            }

            log.info("Total objects fetched: {}", result.size());
            return result;
        } catch (SQLException ex) {
            log.error("Database error during listDatabaseObjects: {}", ex.getMessage());
            throw ex;
        }
    }


}
