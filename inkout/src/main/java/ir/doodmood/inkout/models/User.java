package ir.doodmood.inkout.models;

// TODO implement email verification and forgot password

import ir.doodmood.inkout.models.request.UserRegisterRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Config;
import jakarta.persistence.*;
import lombok.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passwordHash;
    private String email;
    private String first_name;
    private String last_name;
    private String bg_image;
    private String additional_name;
    private String profile_image;
    private String bio;
    private UserGoal goal;

    @OneToMany(cascade = CascadeType.ALL)
    private List<JobPosition> jobPositions;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Education> education;
    @ManyToOne
    private GeoLocation location;
    @ManyToOne
    private Passion passion;
    @OneToOne(cascade = CascadeType.ALL)
    private ContactInfo contact;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Certificate> certificates;
    @ManyToMany
    private Set<Skill> skills;
    @ManyToMany(mappedBy = "followers")
    private Set<User> following;
    @ManyToMany
    @JoinTable(name = "followings")
    private Set<User> followers;
    @ManyToMany(mappedBy = "incomingConnectionRequests")
    private Set<User> outGoingConnectionRequests;
    @ManyToMany
    @JoinTable(name = "connection_requests")
    private Set<User> incomingConnectionRequests;
    @ManyToMany
    @JoinTable(name = "connections")
    private Set<User> connections;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Post> posts;
    @ManyToMany
    private Set<Post> liked;
    @OneToMany
    private List<Comment> comments;


    public User(UserRegisterRequest ur, ProxiesRepository pr) {
        this.passwordHash = getPasswordHash(ur.getEmail(), ur.getPassword());
        this.email = ur.getEmail();
        this.first_name = ur.getFirst_name();
        this.last_name = ur.getLast_name();
        this.location = pr.getProxy(GeoLocation.class, ur.getLocation());
        if (ur.getPassion() != null)
            this.passion = pr.getProxy(Passion.class, ur.getPassion());
        this.contact = new ContactInfo();
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
