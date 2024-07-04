package ir.doodmood.inkout.models;

// TODO make names more sensible
// and what is profile change notification(implement it)

import ir.doodmood.inkout.models.request.NewEducationRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "education_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class Education {
    @ManyToOne
    private User user;
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Institute institute;
    private String major;
    private boolean currentlyStudying;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String description;
    private String grade;
    private String activities;
    @ManyToMany
    private Set<Skill> skills;

    public Education(NewEducationRequest ner, User user, ProxiesRepository pr) {
        this.user = user;
        this.institute = pr.getProxy(Institute.class, ner.getInstitute());
        this.major = ner.getMajor();
        this.currentlyStudying = ner.isCurrentlyStudying();
        this.startDate = LocalDate.parse(ner.getStartDate());
        this.finishDate = LocalDate.parse(ner.getFinishDate());
        this.description = ner.getDescription();
        this.grade = ner.getGrade();
        this.activities = ner.getActivities();
        this.skills = new HashSet<>();
        for (long i : ner.getSkills())
            this.skills.add(pr.getProxy(Skill.class, i));
    }
}
