package ir.doodmood.inkout.models.request;

import ir.doodmood.mashtframework.annotation.gson.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest extends BasicRequest {
    private String email;
    @Required
    private String password;

    public boolean validate() {
        return true;
    }
}
