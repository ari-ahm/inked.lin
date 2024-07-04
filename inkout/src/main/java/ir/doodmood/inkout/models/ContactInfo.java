package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.SetContactInfoRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "contact_infos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfo {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;
    private String phone;
    private PhoneType phoneType;
    private String address;
    private LocalDate dateOfBirth;
    private ShowPolicy dobShowPolicy;
    private String instantContact;

    public ContactInfo(SetContactInfoRequest setContactInfoRequest, User u) {
        this.phone = setContactInfoRequest.getPhone();
        this.phoneType = setContactInfoRequest.getPhoneType();
        this.address = setContactInfoRequest.getAddress();
        this.dateOfBirth = LocalDate.parse(setContactInfoRequest.getDateOfBirth());
        this.dobShowPolicy = setContactInfoRequest.getDobShowPolicy();
        this.instantContact = setContactInfoRequest.getInstantContact();
        this.user = u;
    }
}
