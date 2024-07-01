package ir.doodmood.inkout.models;

// TODO make names more sensible
// and what is profile change notification(implement it)

import ir.doodmood.inkout.models.request.NewEducationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "education_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    @Id
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
    private ArrayList<Skill> skills;

    public Education(NewEducationRequest ner, long userId) {
        this.user = User.builder().id(userId).build();
        this.institute = Institute.builder().id(ner.getInstitute()).build();
        this.major = ner.getMajor();
        this.currentlyStudying = ner.isCurrentlyStudying();
        this.startDate = ner.getStartDate();
        this.finishDate = ner.getFinishDate();
        this.description = ner.getDescription();
        this.grade = ner.getGrade();
        this.activities = ner.getActivities();
        this.skills = new ArrayList<>();
        for (long i : ner.getSkills())
            this.skills.add(Skill.builder().id(i).build());
    }
}
