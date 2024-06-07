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
public class UserLogin {
    private String email;
    private String username;
    @Required
    private String password;
}
