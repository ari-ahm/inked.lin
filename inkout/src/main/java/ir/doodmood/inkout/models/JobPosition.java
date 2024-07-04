package ir.doodmood.inkout.models;

// TODO make names more sensible
// and what is profile change notification(implement it)

import ir.doodmood.inkout.models.request.NewUserJobPositionRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "job_positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class JobPosition {
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
    private Set<Skill> skills;

    public JobPosition(NewUserJobPositionRequest nujpr, User user, ProxiesRepository pr) {
        this.user = user;
        this.title = nujpr.getTitle();
        this.positionType = nujpr.getPositionType();
        this.company = nujpr.getCompany();
        this.address = nujpr.getAddress();
        this.workFromType = nujpr.getWorkFromType();
        this.startDate = LocalDate.parse(nujpr.getStartDate());
        this.finishDate = LocalDate.parse(nujpr.getFinishDate());
        this.description = nujpr.getDescription();
        this.currentlyWorking = nujpr.isCurrentlyWorking();
        this.skills = new HashSet<>();
        for (long i : nujpr.getSkills())
            this.skills.add(pr.getProxy(Skill.class, i));
    }
}
