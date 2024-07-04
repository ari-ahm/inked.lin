package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    private Set<JobPositionResponse> jobPositions;
    private Set<EducationResponse> education;
    private GeoLocationResponse location;
    private PassionResponse passion;
    private ContactInfoResponse contact;
    private Set<CertificateResponse> certificates;
    private Set<SkillResponse> skills;
    private Set<Long> following;
    private Set<Long> followers;
    private Set<Long> outGoingConnectionRequests;
    private Set<Long> incomingConnectionRequests;
    private Set<Long> connections;
    private Set<PostResponse> posts;
    private Set<Long> likedPosts;


    public UserResponse(User u, Long requestingUserId) { // TODO implement dobShowPolicy
        this.id = u.getId();
        this.first_name = u.getFirst_name();
        this.last_name = u.getLast_name();
        this.email = u.getEmail();
        this.bg_image = u.getBg_image();
        this.additional_name = u.getAdditional_name();
        this.profile_image = u.getProfile_image();
        this.bio = u.getBio();
        this.goal = u.getGoal();
        this.jobPositions = new HashSet<>();
        for (JobPosition p : u.getJobPositions())
            this.jobPositions.add(new JobPositionResponse(p));
        this.education = new HashSet<>();
        for (Education p : u.getEducation())
            this.education.add(new EducationResponse(p));
        this.location = new GeoLocationResponse(u.getLocation());
        this.passion = new PassionResponse(u.getPassion());
        this.contact = new ContactInfoResponse(u.getContact());
        this.certificates = new HashSet<>();
        for (Certificate certificate : u.getCertificates())
            this.certificates.add(new CertificateResponse(certificate));
        this.skills = new HashSet<>();
        for (Skill skill : u.getSkills())
                this.skills.add(new SkillResponse(skill));
        this.followers = new HashSet<>();
        for (User v : u.getFollowers())
            this.followers.add(v.getId());
        this.following = new HashSet<>();
        for (User v : u.getFollowing())
            this.following.add(v.getId());
        this.outGoingConnectionRequests = new HashSet<>();
        for (User v : u.getOutGoingConnectionRequests())
            this.outGoingConnectionRequests.add(v.getId());
        this.incomingConnectionRequests = new HashSet<>();
        for (User v : u.getIncomingConnectionRequests())
            this.incomingConnectionRequests.add(v.getId());
        this.connections = new HashSet<>();
        for (User v : u.getConnections())
            this.connections.add(v.getId());
        this.posts = new HashSet<>();
        for (Post i : u.getPosts())
            this.posts.add(new PostResponse(i));
        this.likedPosts = new HashSet<>();
        for (Post i : u.getLikedPosts())
            this.likedPosts.add(i.getId());
    }
}
