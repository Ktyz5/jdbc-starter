package ru.aleks.jdbc.starter.connect;

import ru.util.ConnectionManager;

import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        long flightId = 9;
        var deleteFlightSql = "DELETE FROM flight where id = ?";
        try (var connection = ConnectionManager.open()){
            var prepareStatement = connection.prepareStatement(deleteFlightSql);
             prepareStatement.setLong(1,flightId);
             prepareStatement.executeUpdate();

        }
    }
}
