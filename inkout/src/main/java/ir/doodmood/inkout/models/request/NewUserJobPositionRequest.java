package ir.doodmood.inkout.models.request;

import ir.doodmood.inkout.models.*;
import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import ir.doodmood.mashtframework.annotation.gson.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class NewUserJobPositionRequest extends BasicRequest {
    private Long user;
    private String title;
    private PositionType positionType;
    private String company;
    private String address;
    private WorkFromType workFromType;
    private boolean currentlyWorking;
    private String startDate;
    @Optional
    private String finishDate;
    private String description;
    private Set<Long> skills;

    public boolean validate() {
        if ((finishDate == null && !currentlyWorking) ||
                (title.length() > 40) ||
                (description.length() > 1000) ||
                company.length() > 40 ||
                address.length() > 40 ||
                (skills.size() > 5))
                return false;

        return true;
    }
}
