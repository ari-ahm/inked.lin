package ir.doodmood.inkout.models.request;

import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import ir.doodmood.mashtframework.annotation.gson.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class NewEducationRequest extends BasicRequest {
    private long institute;
    private String major;
    private boolean currentlyStudying;
    private LocalDate startDate;
    @Optional
    private LocalDate finishDate;
    private String description;
    private String grade;
    private String activities;
    private ArrayList<Long> skills;

    public boolean validate() {
        if ((finishDate == null && !currentlyStudying) ||
                description.length() > 1000 ||
                grade.length() > 5 ||
                activities.length() > 500 ||
                major.length() > 40 ||
                skills.size() > 5)
            return false;

        return true;
    }
}
