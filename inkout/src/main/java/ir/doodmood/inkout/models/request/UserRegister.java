package ir.doodmood.inkout.models.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegister {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String first_name;
    private String last_name;
}
