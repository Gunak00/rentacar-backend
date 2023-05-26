package pl.gunak00.rentacarbackend.reservation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gunak00.rentacarbackend.reservation.model.EmailDetails;
import pl.gunak00.rentacarbackend.reservation.model.Reservation;

@AllArgsConstructor
@Service
public class ReservationService {

    private final EmailService emailService;

    public String sendEmail(Reservation reservation) {

        return emailService.sendSimpleMail(getEmailDetails(reservation));
    }

    private EmailDetails getEmailDetails(Reservation reservation) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(reservation.getRecipient());
        emailDetails.setSubject("Potwierdzenie rezerwacji samochodu");

        StringBuilder sb = new StringBuilder();

        sb.append("Szanowny Kliencie, \n\n");
        sb.append("Dziękujemy za dokonanie rezerwacji samochodu w naszej wypożyczalni." +
                " Z przyjemnością informujemy, że Twoja rezerwacja została pomyślnie zarejestrowana. " +
                "Poniżej znajdują się szczegóły Twojej rezerwacji: \n\n");

        sb.append("- Nazwa samochodu: ").append(reservation.getCarName()).append("\n");
        sb.append("- Model samochodu: ").append(reservation.getCarModel()).append("\n");
        sb.append("- Ilość dni wynajmu: ").append(reservation.getNumberOfDays()).append("\n");
        sb.append("- Cena za dzień: ").append(reservation.getNumberOfDays()).append("\n");
        sb.append("- Cena całkowita: ").append(reservation.getPriceAll()).append("\n");
        sb.append("- Miejsce odbioru samochodu: ").append(reservation.getPickUpLocation()).append("\n");
        sb.append("- Miejsce zwrotu samochodu: ").append(reservation.getReturnLocation()).append("\n");
        sb.append("- Data odbioru: ").append(reservation.getStartDate()).append("\n");
        sb.append("- Data zwrotu: ").append(reservation.getEndDate()).append("\n\n");

        sb.append("Dziękujemy za wybór naszej wypożyczalni. Jesteśmy przekonani, że nasz samochód spełni Twoje oczekiwania" +
                " i umili Ci podróż. \n\n");
        sb.append("Życzymy udanej podróży!\n\n");
        sb.append("Z poważaniem,\n");
        sb.append("Zespół Wypożyczalni Samochodów");

        String msgBody = sb.toString();
        emailDetails.setMsgBody(msgBody);

        return emailDetails;
    }

}
