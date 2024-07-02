package ir.doodmood.inkout.models.request;

import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class NewPostRequest extends BasicRequest {
    private String body;

    public boolean validate() {
        return body.length() <= 3000;
    }
}