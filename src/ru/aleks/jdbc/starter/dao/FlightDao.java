package ru.aleks.jdbc.starter.dao;

import ru.aleks.jdbc.starter.entity.Flight;
import ru.aleks.jdbc.starter.exception.DaoException;
import ru.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight> {

    private static final FlightDao INSTANCE = new FlightDao();

    private static final String FIND_BY_ID_SQL = """
            SELECT id, flight_no, departure_data, departure_airport_cod,
            arrival_data, arrival_airport_cod, aircraft_id, status
            FROM flight WHERE id = ?
            """;

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Flight save(Flight ticket) {
        return null;
    }

    @Override
    public void update(Flight ticket) {
        // TODO document why this method is empty
    }

    @Override
    public Optional<Flight> selectFindBuId(Long id) {
        try (var connection = ConnectionPool.get()) {
            return selectFindBuId(id, connection);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }

    }

    public Optional<Flight> selectFindBuId(Long id, Connection connection) {
        Flight flight;
        try (var preperstet = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preperstet.setLong(1, id);
            var rez = preperstet.executeQuery();
            flight = null;
            if (rez.next()) {
                flight = new Flight(
                        rez.getLong("id"),
                        rez.getString("flight_no"),
                        rez.getTimestamp("departure_data").toLocalDateTime(),
                        rez.getString("departure_airport_cod"),
                        rez.getTimestamp("arrival_data").toLocalDateTime(),
                        rez.getString("arrival_airport_cod"),
                        rez.getInt("aircraft_id"),
                        rez.getString("status")
                );
            }
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
        return Optional.ofNullable(flight);
    }


    @Override
    public List<Flight> findAll() {
        return Collections.emptyList();
    }
}
