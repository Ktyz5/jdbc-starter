package ru.aleks.jdbc.starter.dao;

import ru.aleks.jdbc.starter.dto.TicketFilter;
import ru.aleks.jdbc.starter.entity.Flight;
import ru.aleks.jdbc.starter.entity.Ticket;
import ru.aleks.jdbc.starter.exception.DaoException;
import ru.util.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao implements Dao<Long, Ticket> {
    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
            DELETE FROM ticket WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?,?,?,?,?)""";

    private static final String UPDATE_SQL = """
            UPDATE ticket SET passenger_no = ?,passenger_name = ?, 
            flight_id = ?, seat_no = ?, cost = ?
            WHERE  id = ?""";

    private static final String FIND_ALL_SQL = """
            SELECT ticket.id, ticket.passenger_no, ticket.passenger_name, ticket.flight_id, ticket.seat_no, ticket.cost,
            f.id, f.flight_no, f.departure_data, f.departure_airport_cod, f.arrival_data, f.arrival_airport_cod, f.aircraft_id, f.status 
            FROM ticket join flight f on f.id = ticket.flight_id
                       
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE ticket.id = ?
            """;
    private final FlightDao flightDao = FlightDao.getInstance();

    private TicketDao() {
    }

    public List<Ticket> findAll(TicketFilter ticketFilter) {
        List<Object> parametrs = new ArrayList<>();
        parametrs.add(ticketFilter.limit());
        parametrs.add(ticketFilter.offset());
        var sql = FIND_ALL_SQL + """
                limit ?
                offset ?
                """;
        try (var connection = ConnectionPool.get();
             var preperstetm = connection.prepareStatement(sql)) {
            for (int i = 0; i < parametrs.size(); i++) {
                preperstetm.setObject(i + 1, parametrs.get(i));
            }
            var rez = preperstetm.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (rez.next()) {
                tickets.add(builtTicket(rez));
            }
            return tickets;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }

    }

    public List<Ticket> findAll() {
        try (var connection = ConnectionPool.get();
             var preperstetm = connection.prepareStatement(FIND_ALL_SQL)) {
            var rez = preperstetm.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (rez.next()) {
                tickets.add(builtTicket(rez));
            }
            return tickets;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Ticket> selectFindBuId(Long id) {
        try (var connection = ConnectionPool.get();
             var preperstetm = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preperstetm.setLong(1, id);
            var rez = preperstetm.executeQuery();
            Ticket ticket = null;
            if (rez.next()) {
                ticket = builtTicket(rez);
            }
            return Optional.ofNullable(ticket);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private Ticket builtTicket(ResultSet rez) throws SQLException {
        var flight = new Flight(
                rez.getLong("flight_id"),
                rez.getString("flight_no"),
                rez.getTimestamp("departure_data").toLocalDateTime(),
                rez.getString("departure_airport_cod"),
                rez.getTimestamp("arrival_data").toLocalDateTime(),
                rez.getString("arrival_airport_cod"),
                rez.getInt("aircraft_id"),
                rez.getString("status")
        );
        return new Ticket(rez.getLong("id"),
                rez.getString("passenger_no"),
                rez.getString("passenger_name"),
                flightDao.selectFindBuId(rez.getLong("flight_id"),
                        rez.getStatement().getConnection()).orElse(null),
                rez.getString("seat_no"),
                rez.getBigDecimal("cost"));
    }

    public void update(Ticket ticket) {
        try (var connection = ConnectionPool.get();
             var preperstetm = connection.prepareStatement(UPDATE_SQL)) {
            preperstetm.setString(1, ticket.getPassengerNo());
            preperstetm.setString(2, ticket.getPassengerName());
            preperstetm.setLong(3, ticket.getFlight().id());
            preperstetm.setString(4, ticket.getSeatNo());
            preperstetm.setBigDecimal(5, ticket.getCost());
            preperstetm.setLong(6, ticket.getId());
            preperstetm.executeUpdate();

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionPool.get();
             var preperstetm = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preperstetm.setString(1, ticket.getPassengerNo());
            preperstetm.setString(2, ticket.getPassengerName());
            preperstetm.setLong(3, ticket.getFlight().id());
            preperstetm.setString(4, ticket.getSeatNo());
            preperstetm.setBigDecimal(5, ticket.getCost());
            preperstetm.executeUpdate();
            var generatedKeys = preperstetm.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }

    }

    public boolean delete(Long id) {
        try (var connection = ConnectionPool.get();
             var preperstet = connection.prepareStatement(DELETE_SQL)) {
            preperstet.setLong(1, id);
            return preperstet.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }

    }


    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
