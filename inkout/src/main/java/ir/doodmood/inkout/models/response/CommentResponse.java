package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Comment;

import java.time.LocalDateTime;

public class CommentResponse {
    private long id;
    private long user;
    private long post;
    private String content;
    private String createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.user = comment.getUser().getId();
        this.post = comment.getPost().getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().toString();
    }
}
