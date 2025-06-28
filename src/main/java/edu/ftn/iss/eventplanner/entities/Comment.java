package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Status;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;
    private int rating;
    private LocalDate date;
    private Status status;      // pending, accepted, rejected, deleted - LOGICAL DELETION

    // one of these two will be null
    @ManyToOne
    @JoinColumn(name = "product_id")    // after buying product
    private Product product;

    @ManyToOne
    @JoinColumn(name = "service_id")    // after booking service
    private Service service;

    //who commented whom?
    @ManyToOne
    @JoinColumn(name = "commenter_id")
    private User commenter;
}
