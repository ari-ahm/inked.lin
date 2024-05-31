package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.UserRegister;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Config;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String first_name;
    private String last_name;

    public User(UserRegister ur) {
        this.username = ur.getUsername();
        this.passwordHash = getPasswordHash(ur.getUsername(), ur.getPassword());
        this.email = ur.getEmail();
        this.phone = ur.getPhone();
        this.first_name = ur.getFirst_name();
        this.last_name = ur.getLast_name();
    }

    public String getPasswordHash(String username, String password) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] key = ((String) ((Config) ComponentFactory.factory(Config.class).getNew()).get("user_password_salt", String.class)).getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "HmacSHA256");
            mac.init(secretKey);

            // TODO: clean

            return Base64.getUrlEncoder().encodeToString(mac.doFinal((username + "." + password).getBytes()));
        } catch (Exception e) {
            return password;
        }
    }
}
