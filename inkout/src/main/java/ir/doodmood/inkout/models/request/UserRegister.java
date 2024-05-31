package ir.doodmood.inkout.models.request;

import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class UserRegister {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String first_name;
    private String last_name;
}
