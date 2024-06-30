package ir.doodmood.inkout.models.request;

import ir.doodmood.inkout.models.*;
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
public class NewUserJobPositionRequest extends BasicRequest {
    private long user;
    private String title;
    private PositionType positionType;
    private long company;
    private WorkFromType workFromType;
    private boolean currentlyWorking;
    private LocalDate startDate;
    @Optional
    private LocalDate finishDate;
    private String description;
    private ArrayList<Long> skills;

    public boolean validate() {
        if ((finishDate == null && !currentlyWorking) ||
                (title.length() > 40) ||
                (description.length() > 1000) ||
                (skills.size() > 5))
                return false;

        return true;
    }
}
