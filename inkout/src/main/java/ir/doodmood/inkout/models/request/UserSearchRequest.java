package ir.doodmood.inkout.models.request;

import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class UserSearchRequest extends BasicRequest {
    private String first_name;
    private String last_name;
    private String additional_name;

    public boolean validate() {
        return true;
    }
}
