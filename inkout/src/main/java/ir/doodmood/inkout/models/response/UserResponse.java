package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.*;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String email;
    private String first_name;
    private String last_name;
    private String bg_image;
    private String additional_name;
    private String profile_image;
    private String bio;
    private UserGoal goal;

//    private ArrayList<JobPosition> jobPositions;
//    private ArrayList<Education> education;
//    private GeoLocation location;
//    private Passion passion;
//    private ContactInfo contact;
//    private ArrayList<Certificate> certificates;
//    private ArrayList<Skill> skills;


    public UserResponse(User u) {
        this.id = u.getId();
        this.first_name = u.getFirst_name();
        this.last_name = u.getLast_name();
        this.email = u.getEmail();
        this.bg_image = u.getBg_image();
        this.additional_name = u.getAdditional_name();
        this.profile_image = u.getProfile_image();
        this.bio = u.getBio();
        this.goal = u.getGoal();
//        this.jobPositions = u.getJobPositions();
//        this.education = u.getEducation();
//        this.location = u.getLocation();
//        this.passion = u.getPassion();
//        this.contact = u.getContact();
//        this.certificates = u.getCertificates();
//        this.skills = u.getSkills();
    }
}
