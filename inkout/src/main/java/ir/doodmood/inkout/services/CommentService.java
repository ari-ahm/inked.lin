package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.Comment;
import ir.doodmood.inkout.models.response.CommentResponse;
import ir.doodmood.inkout.repositories.CommentRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponse getById(Long id) throws NotFoundException {
        Comment c = commentRepository.getById(id);
        if (c == null) throw new NotFoundException();
        return new CommentResponse(c);
    }
}
