package ir.doodmood.inkout.models.request;

import ir.doodmood.inkout.util.RegexPatterns;
import ir.doodmood.inkout.models.UserGoal;
import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import ir.doodmood.mashtframework.annotation.gson.Optional;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class UserRegisterRequest extends BasicRequest {
    private String password;
    private String email;
    private String first_name;
    private String last_name;
    private UserGoal goal;
    @Optional
    private String bg_image;
    private String additional_name;
    @Optional
    private String profile_image;
    private String bio;
    private Long location;
    @Optional
    private Long passion;

    public boolean validate() {
        if (password.length() < 8 || password.length() > 40 ||
            email.length() > 40 ||
            first_name.length() > 20 ||
            last_name.length() > 40 ||
            additional_name.length() > 40 ||
            bio.length() > 40 ||
            !first_name.matches("^[a-zA-Z\\s]*$") ||
            !last_name.matches("^[a-zA-Z\\s]*$") ||
            (!RegexPatterns.getEmailPattern().matcher(email).matches()) ||
            (!password.contains("[a-z]") || !password.contains("[A-Z]") || !password.contains("[0-9]")) ||
            (bg_image != null && !bg_image.matches("^[0-9a-zA-Z]{32}$")) ||
            (profile_image != null && !profile_image.matches("^[0-9a-zA-Z]{32}$")))
                return false;

        // TODO check image resolutions.

        return true;
    }
}
