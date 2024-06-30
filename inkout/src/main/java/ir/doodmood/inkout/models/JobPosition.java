package ir.doodmood.inkout.models;

// TODO make names more sensible
// and what is profile change notification(implement it)

import ir.doodmood.inkout.models.request.NewUserJobPositionRequest;
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

    @ManyToOne
    private Company company;
    private WorkFromType workFromType;
    private boolean currentlyWorking;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String description;
    @ManyToMany
    private ArrayList<Skill> skills;

    public JobPosition(NewUserJobPositionRequest nujpr, long userId) {
        this.user = User.builder().id(userId).build();
        this.title = nujpr.getTitle();
        this.positionType = nujpr.getPositionType();
        this.company = Company.builder().id(nujpr.getCompany()).build();
        this.workFromType = nujpr.getWorkFromType();
        this.startDate = nujpr.getStartDate();
        this.finishDate = nujpr.getFinishDate();
        this.description = nujpr.getDescription();
        this.currentlyWorking = nujpr.isCurrentlyWorking();
        this.skills = new ArrayList<>();
        for (long i : nujpr.getSkills())
            this.skills.add(Skill.builder().id(i).build());
    }
}
