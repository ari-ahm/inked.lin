package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPositionResponse {
    private long user;
    private long id;
    private String title;
    private PositionType positionType;
    private String company;
    private String address;
    private WorkFromType workFromType;
    private boolean currentlyWorking;
    private String startDate;
    private String finishDate;
    private String description;
    private Set<SkillResponse> skills;

    public JobPositionResponse(JobPosition jp) {
        this.user = jp.getUser().getId();
        this.id = jp.getId();
        this.title = jp.getTitle();
        this.positionType = jp.getPositionType();
        this.company = jp.getCompany();
        this.address = jp.getAddress();
        this.workFromType = jp.getWorkFromType();
        this.currentlyWorking = jp.isCurrentlyWorking();
        this.startDate = jp.getStartDate().toString();
        if (jp.getFinishDate() != null)
            this.finishDate = jp.getFinishDate().toString();
        this.description = jp.getDescription();
        this.skills = new HashSet<>();

    }
}
