package ir.doodmood.inkout.controllers;

// TODO implement search

import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.NewPostRequest;
import ir.doodmood.inkout.services.UsersService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/post")
public class PostController {
    private final UsersService usersService;
    @Autowired
    public PostController(UsersService usersService) {
        this.usersService = usersService;
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
}
