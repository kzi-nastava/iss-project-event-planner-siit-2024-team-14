package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("ADMIN") // Maps to role = "ADMIN"
public class Admin extends User {

    @Column(name = "profile_picture_admin")
    private String profilePicture;
    @Column(name = "first_name_admin")
    private String firstName;
    @Column(name = "last_name_admin")
    private String lastName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
