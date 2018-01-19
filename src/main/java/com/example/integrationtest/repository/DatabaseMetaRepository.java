package com.example.integrationtest.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DatabaseMetaRepository {

    public static final String CATALOG = "teste";
    public static final String TABLE_NAME = "TABLE";
    public static final int TABLE_NAME_COLUMN = 3;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    public List<String> retrieveAvaliableTables(){
        Connection connection = null;
        List<String> result = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            ResultSet resultSet = connection.getMetaData().getTables(CATALOG, null, "%", new String[]{TABLE_NAME});
            while (resultSet.next()) {
                result.add(resultSet.getString(TABLE_NAME_COLUMN));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return result;
    }
}
