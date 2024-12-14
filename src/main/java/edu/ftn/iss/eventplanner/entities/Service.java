package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.ReservationType;
import lombok.*;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Service extends Solution {

    private Duration minDuration;
    private Duration maxDuration;
    private Duration duration;
    private ReservationType reservationType;
    private Duration reservationPeriod;
    private Duration cancellationPeriod;

}