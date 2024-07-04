package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Post;
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
public class PostResponse {
    private long id;
    private long user;
    private String body;
    private String createdAt;
    private Set<Long> liked_by;
    private Set<Long> comments;

    public PostResponse(Post post) { // TODO implement media and files
        this.id = post.getId();
        this.user = post.getUser().getId();
        this.body = post.getBody();
        this.createdAt = post.getCreatedAt().toString();
        this.liked_by = new HashSet<>();
        this.comments = new HashSet<>();
    }
}
