package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String first_name;
    private String last_name;

    public UserResponse(User u) {
        this.id = u.getId();
        this.first_name = u.getFirst_name();
        this.last_name = u.getLast_name();
    }
}
