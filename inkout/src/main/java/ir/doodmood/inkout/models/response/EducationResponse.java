package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Education;
import ir.doodmood.inkout.models.Institute;
import ir.doodmood.inkout.models.Skill;
import ir.doodmood.inkout.models.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducationResponse {
    private long user;
    private long id;
    private long institute;
    private String major;
    private boolean currentlyStudying;
    private String startDate;
    private String finishDate;
    private String description;
    private String grade;
    private String activities;
    private Set<Long> skills;

    public EducationResponse(Education education) {
        this.user = education.getUser().getId();
        this.id = education.getId();
        this.institute = education.getInstitute().getId();
        this.major = education.getMajor();
        this.currentlyStudying = education.isCurrentlyStudying();
        this.startDate = education.getStartDate().toString();
        if (education.getFinishDate() != null)
            this.finishDate = education.getFinishDate().toString();
        this.description = education.getDescription();
        this.grade = education.getGrade();
        this.activities = education.getActivities();
        this.skills = new HashSet<>();
        for (Skill skill : education.getSkills())
            this.skills.add(skill.getId());
    }
}
