package ir.doodmood.inkout.models.request;

import ir.doodmood.inkout.models.PhoneType;
import ir.doodmood.inkout.models.ShowPolicy;
import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class SetContactInfoRequest extends BasicRequest {
    private String phone;
    private PhoneType phoneType;
    private String address;
    private LocalDate dateOfBirth;
    private ShowPolicy dobShowPolicy;
    private String instantContact;

    public boolean validate() {
        if (!phone.matches("^\\+?[0-9]+$") ||
            phone.length() > 40 || address.length() > 220 ||
            instantContact.length() > 40)
            return false;

        return true;
    }
}
