package ru.aleks.jdbc.starter.entity;

import java.time.LocalDateTime;

public record Flight(Long id,
                     String flightNo,
                     LocalDateTime departureData,
                     String departureAirportCod,
                     LocalDateTime arrivalData,
                     String arrivalAirportCod,
                     Integer aircraftId,
                     String status) {
}
