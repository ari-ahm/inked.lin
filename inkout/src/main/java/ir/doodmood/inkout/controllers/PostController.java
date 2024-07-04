package ir.doodmood.inkout.controllers;

// TODO implement search

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.NewCommentRequest;
import ir.doodmood.inkout.models.request.NewPostRequest;
import ir.doodmood.inkout.services.CommentService;
import ir.doodmood.inkout.services.UsersService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

import java.text.ParseException;

@RestController("/post")
public class PostController {
    private final UsersService usersService;
    private final CommentService commentService;
    @Autowired
    public PostController(UsersService usersService, CommentService commentService) {
        this.usersService = usersService;
        this.commentService = commentService;
    }

    @PostMapping
    private void post(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        NewPostRequest request = (NewPostRequest) dto.getRequestBody(NewPostRequest.class);
        if (request == null || !request.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        usersService.post(request, jwtAuth.getId());
        dto.sendResponse(200, "OK");
    }

    @GetMapping("/comments/{}")
    private void getComment(MashtDTO dto) {
        try {
            dto.sendResponse(commentService.getById(Long.parseLong(dto.getPathVariables().getLast())));
        } catch (NotFoundException e) {
            dto.sendResponse(404, "Not Found");
        } catch (ParseException e) {

        }
    }

    @PostMapping("/comments")
    private void comment(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        NewCommentRequest request = (NewCommentRequest) dto.getRequestBody(NewCommentRequest.class);
        if (request == null || !request.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        usersService.comment(request, jwtAuth.getId());
        dto.sendResponse(200, "OK");
    }

    @GetMapping
    private void get(MashtDTO dto) {
        // TODO.
    }
}
