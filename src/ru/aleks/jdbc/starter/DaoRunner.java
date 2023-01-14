package ru.aleks.jdbc.starter;

import ru.aleks.jdbc.starter.dao.TicketDao;
import ru.aleks.jdbc.starter.dto.TicketFilter;

public class DaoRunner {
    public static void main(String[] args) {
        var ticketFilter = new TicketFilter(10, 2);
        var tickets = TicketDao.getInstance().findAll(ticketFilter);
        System.out.println(tickets);
//        var ticketDao = TicketDao.getInstance();
//        var ticet = new Ticket();
//        var tik = ticketDao.selectFindBuId(111L);
//        System.out.println(ticketDao.findAll());
//        System.out.println(tik);
//        tik.ifPresent(ticket -> {ticket.setCost(BigDecimal.valueOf(188.88));
//        ticketDao.update(ticket);});

//        ticet.setPassengerNo("123456");
//        ticet.setPassengerName("Test");
//        ticet.setFlightId(3L);
//        ticet.setSeat_no("B3");
//        ticet.setCost(BigDecimal.TEN);
//        var seveTicet = ticketDao.seve(ticet);
//        System.out.println(seveTicet);
    }
}
