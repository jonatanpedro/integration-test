package com.example.integrationtest.flow;

import com.example.integrationtest.dto.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.jdbc.JdbcMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JDBCOutboundMessageHandler implements MessageHandler{


    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        Flow flow = (Flow) message.getHeaders().get("flow");
        Map<String, Object> objectMap = (Map<String, Object>) message.getPayload();

        List<AbstractMap.SimpleEntry<String, String>> fields = flow.getDestinationMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getSource() != null)
                .map(entry ->{
                    Map.Entry<String, Object> stringObjectEntry = objectMap
                            .entrySet()
                            .stream()
                            .filter(entry1 -> entry.getValue().getSource().equals(entry1.getKey()))
                            .findFirst()
                            .orElse(null);
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), new String());
                })
                .collect(Collectors.toList());


                /*.map(filteredEntry -> new AbstractMap.SimpleEntry<>(filteredEntry.getKey(), filteredEntry.getValue().getSource()))
                .collect(Collectors.toList());*/

        JdbcMessageHandler jdbcMessageHandler = new JdbcMessageHandler(dataSource,
                "INSERT INTO " +
                        flow.getSelectedTable() +
                        "( " +
                        fields.stream().map(entry -> entry.getKey()).collect(Collectors.joining(",")) +
                        " ) VALUES ( " +
                        fields.stream().map(entry -> "?").collect(Collectors.joining(",")) +
                        " )");



        /*jdbcMessageHandler.setPreparedStatementSetter((ps, m) -> {
            definePreparedStatementColumns(columns, ps);
        });*/

        //return jdbcMessageHandler;
    }

    public void definePreparedStatementColumns(List<Map.Entry<String, Object>> columns, PreparedStatement ps){

    }
}
