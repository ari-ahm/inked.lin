package ir.doodmood.inkout.models;

// TODO implement email verification and forgot password

import ir.doodmood.inkout.models.request.UserRegisterRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Config;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String passwordHash;
    private String email;
    private String first_name;
    private String last_name;
    private String bg_image;
    private String additional_name;
    private String profile_image;
    private String bio;
    private UserGoal goal;

    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ArrayList<JobPosition> jobPositions;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ArrayList<Education> education;
    @ManyToOne
    private GeoLocation location;
    @ManyToOne
    private Passion passion;
    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ContactInfo contact;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ArrayList<Certificate> certificates;
    @ManyToMany
    private ArrayList<Skill> skills;
    @ManyToMany(mappedBy = "followers")
    private ArrayList<User> following;
    @ManyToMany
    @JoinTable(name = "followings")
    private ArrayList<User> followers;
    @ManyToMany(mappedBy = "incomingConnectionRequests")
    private ArrayList<User> outGoingConnectionRequests;
    @ManyToMany
    @JoinTable(name = "connection_requests")
    private ArrayList<User> incomingConnectionRequests;
    @ManyToMany
    @JoinTable(name = "connections")
    private ArrayList<User> connections;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ArrayList<Post> posts;
    @ManyToMany
    private ArrayList<Post> liked;


    public User(UserRegisterRequest ur, ProxiesRepository pr) {
        this.passwordHash = getPasswordHash(ur.getEmail(), ur.getPassword());
        this.email = ur.getEmail();
        this.first_name = ur.getFirst_name();
        this.last_name = ur.getLast_name();
        this.location = pr.getProxy(GeoLocation.class, ur.getLocation());
        if (ur.getPassion() != null)
            this.passion = pr.getProxy(Passion.class, ur.getPassion());
        this.contact = pr.save(ContactInfo.builder().user(this).build());
        this.additional_name = ur.getAdditional_name();
        this.goal = ur.getGoal();
    }

    public static String getPasswordHash(String username, String password) {
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
