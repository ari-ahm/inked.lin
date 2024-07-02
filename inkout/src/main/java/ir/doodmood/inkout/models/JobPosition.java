package ir.doodmood.inkout.models;

// TODO make names more sensible
// and what is profile change notification(implement it)

import ir.doodmood.inkout.models.request.NewUserJobPositionRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "job_positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosition {
    @Id
    @ManyToOne
    private User user;
    @Id
    @GeneratedValue
    private long id;

    private String title;
    private PositionType positionType;

    private String company;
    private String address;
    private WorkFromType workFromType;
    private boolean currentlyWorking;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String description;
    @ManyToMany
    private ArrayList<Skill> skills;

    public JobPosition(NewUserJobPositionRequest nujpr, User user, ProxiesRepository pr) {
        this.user = user;
        this.title = nujpr.getTitle();
        this.positionType = nujpr.getPositionType();
        this.company = nujpr.getCompany();
        this.address = nujpr.getAddress();
        this.workFromType = nujpr.getWorkFromType();
        this.startDate = nujpr.getStartDate();
        this.finishDate = nujpr.getFinishDate();
        this.description = nujpr.getDescription();
        this.currentlyWorking = nujpr.isCurrentlyWorking();
        this.skills = new ArrayList<>();
        for (long i : nujpr.getSkills())
            this.skills.add(pr.getProxy(Skill.class, i));
    }
}
