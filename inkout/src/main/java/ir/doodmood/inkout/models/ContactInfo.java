package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.SetContactInfoRequest;
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

    public ContactInfo(SetContactInfoRequest setContactInfoRequest, User u) {
        this.phone = setContactInfoRequest.getPhone();
        this.phoneType = setContactInfoRequest.getPhoneType();
        this.address = setContactInfoRequest.getAddress();
        this.dateOfBirth = setContactInfoRequest.getDateOfBirth();
        this.dobShowPolicy = setContactInfoRequest.getDobShowPolicy();
        this.instantContact = setContactInfoRequest.getInstantContact();
        this.user = u;
    }
}
