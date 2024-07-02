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
public class NewCommentRequest extends BasicRequest {
    private String content;
    private Long post;

    public boolean validate() {
        return content.length() <= 1250;
    }
}