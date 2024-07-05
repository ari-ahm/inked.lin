package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.ContactInfo;
import ir.doodmood.inkout.models.PhoneType;
import ir.doodmood.inkout.models.ShowPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoResponse {
    private long id;
    private String phone;
    private PhoneType phoneType;
    private String address;
    private String dateOfBirth;
    private ShowPolicy dobShowPolicy;
    private String instantContact;

    public ContactInfoResponse(ContactInfo contact) {
        this.id = contact.getId();
        this.phone = contact.getPhone();
        this.phoneType = contact.getPhoneType();
        this.address = contact.getAddress();
        this.dateOfBirth = contact.getDateOfBirth().toString();
        this.dobShowPolicy = contact.getDobShowPolicy();
        this.instantContact = contact.getInstantContact();
    }
}
