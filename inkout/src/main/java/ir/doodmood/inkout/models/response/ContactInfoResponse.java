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
        if (contact.getPhone() != null)
            this.phone = contact.getPhone();
        if (phoneType != null)
            this.phoneType = contact.getPhoneType();
        if (address != null)
            this.address = contact.getAddress();
        if (dobShowPolicy != null)
            this.dateOfBirth = contact.getDateOfBirth().toString();
        if (dobShowPolicy != null)
            this.dobShowPolicy = contact.getDobShowPolicy();
        if (instantContact != null)
            this.instantContact = contact.getInstantContact();
    }
}
