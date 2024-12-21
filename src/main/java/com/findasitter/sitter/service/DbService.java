package com.findasitter.sitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class DbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getColumnCount(String tableName) {
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            int columnCount = 0;
            while (columns.next()) {
                columnCount++;
            }
            columns.close();
            return columnCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
