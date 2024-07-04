package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewCommentRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class Comment {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;

    private String content;
    private LocalDateTime createdAt;

    public Comment(NewCommentRequest ncr, User u, ProxiesRepository pr) {
        this.user = u;
        this.content = ncr.getContent();
        this.createdAt = LocalDateTime.now();
        this.post = pr.getProxy(Post.class, ncr.getPost());
    }
}
