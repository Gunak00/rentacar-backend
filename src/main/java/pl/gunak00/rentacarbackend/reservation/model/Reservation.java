package pl.gunak00.rentacarbackend.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private Long id;
    private String recipient;
    private String carName;
    private String carModel;
    private Integer numberOfDays;
    private Integer priceForDay;
    private Integer priceAll;
    private String pickUpLocation;
    private String returnLocation;
    private String startDate;
    private String endDate;
}
