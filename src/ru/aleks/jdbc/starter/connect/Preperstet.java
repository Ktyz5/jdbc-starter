package ru.aleks.jdbc.starter.connect;

import ru.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Preperstet {
    public static void main(String[] args) throws SQLException {
        Long id = 2L;
        System.out.println(getTiketsByFlayId(id));

    }
    private static List<Long> getTiketsByFlayId(Long flightsId) throws SQLException {
        List <Long> rez = new ArrayList<>();
        String sql = """
                select id FROM ticket where flight_id = ?
                """;
       try (var conect = ConnectionManager.open();
       var preperstat = conect.prepareStatement(sql)){
           preperstat.setLong(1,flightsId);
           var result = preperstat.executeQuery();
           while (result.next()){
               rez.add(result.getObject("id",Long.class));
           }
       }
       return rez;
    }
}
