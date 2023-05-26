package pl.gunak00.rentacarbackend.reservation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gunak00.rentacarbackend.reservation.model.Reservation;
import pl.gunak00.rentacarbackend.reservation.service.ReservationService;

@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/sendReservation")
    public ResponseEntity<?> sendConfirmation(@RequestBody Reservation reservation){
        reservationService.sendEmail(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
