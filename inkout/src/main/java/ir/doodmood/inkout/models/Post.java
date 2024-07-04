package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewPostRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class Post {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private User user;
    private String body;
    private LocalDateTime createdAt;
    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> liked_by;
    @OneToMany
    private Set<Comment> comments;

    public Post(NewPostRequest npr, User u) {
        this.user = u;
        this.body = npr.getBody();
        this.createdAt = LocalDateTime.now();
    }
}
