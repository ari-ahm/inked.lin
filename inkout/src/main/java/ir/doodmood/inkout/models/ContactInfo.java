package ir.doodmood.inkout.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "contact_infos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {
    @Id
    @OneToOne
    private User user;
    private String phone;
    private PhoneType phoneType;
    private String address;
    private LocalDate dateOfBirth;
    private ShowPolicy dobShowPolicy;
    private String instantContact;
}
