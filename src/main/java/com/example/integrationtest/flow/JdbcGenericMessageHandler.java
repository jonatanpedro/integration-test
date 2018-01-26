package com.example.integrationtest.flow;

import com.example.integrationtest.dto.Flow;
import org.springframework.integration.jdbc.JdbcMessageHandler;

import javax.sql.DataSource;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdbcGenericMessageHandler extends JdbcMessageHandler {

    public JdbcGenericMessageHandler(Flow flow, DataSource dataSource) {
        super(dataSource, null);

        List<Map.Entry<String, String>> fields = flow.getDestinationMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getSource() != null)
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getSource()))
                .collect(Collectors.toList());

        this.setUpdateSql(
                "INSERT INTO " +
                        flow.getSelectedTable() +
                        "( " +
                        fields.stream().map(Map.Entry::getKey).collect(Collectors.joining(",")) +
                        " ) VALUES ( " +
                        fields.stream().map(entry -> "?").collect(Collectors.joining(",")) +
                        " )"
        );

        this.setPreparedStatementSetter((preparedStatement, message) -> {

            final Map<String, Object> objectMap = (Map<String, Object>)message.getPayload();

            List<Map.Entry<String, ?>> columns = flow.getDestinationMap()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getSource() != null)
                    .map(entry -> {
                        Map.Entry<String, Object> column = objectMap
                                .entrySet()
                                .stream()
                                .filter(entry1 -> entry.getValue().getSource().equals(entry1.getKey()))
                                .findFirst()
                                .orElse(null);
                        return new AbstractMap.SimpleEntry<>(entry.getKey(), column.getValue());
                    })
                    .collect(Collectors.toList());

            int i = 1;
            for (Map.Entry<String, ?> field : columns) {
                preparedStatement.setObject(i++, field.getValue());
            }
        });
    }
}
