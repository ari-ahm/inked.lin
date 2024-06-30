package ir.doodmood.inkout.models.request;

import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import ir.doodmood.mashtframework.annotation.gson.Optional;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Optional
    private String bg_image;
    private String additional_name;
    @Optional
    private String profile_image;
    private String bio;
    private long location;
    private long passion;

    private static Pattern emailPattern;
    public static Pattern getEmailPattern() { // TODO move this function to a util class.
        if (emailPattern == null) {
            emailPattern = Pattern.compile("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$");
        }
        return emailPattern;
    }

    public boolean validate() {
        if (password.length() < 8 || password.length() > 40 ||
            email.length() > 40 ||
            first_name.length() > 20 ||
            last_name.length() > 40 ||
            additional_name.length() > 40 ||
            bio.length() > 40 ||
            !first_name.matches("^[a-zA-Z\\s]*$") ||
            !last_name.matches("^[a-zA-Z\\s]*$") ||
            (!getEmailPattern().matcher(email).matches()) ||
            (!password.contains("[a-z]") || !password.contains("[A-Z]") || !password.contains("[0-9]")) ||
            (!bg_image.matches("^[0-9a-zA-Z]{32}$") || !profile_image.matches("^[0-9a-zA-Z]{32}$")))
                return false;

        // TODO check image resolutions.

        return true;
    }
}
