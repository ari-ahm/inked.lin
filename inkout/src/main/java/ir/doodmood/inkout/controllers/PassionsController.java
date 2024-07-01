package ir.doodmood.inkout.controllers;

// TODO implement search

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.NewPassionRequest;
import ir.doodmood.inkout.services.PassionsService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/passions")
public class PassionsController {
    PassionsService passionsService;
    @Autowired
    public PassionsController(PassionsService passionsService) {
        this.passionsService = passionsService;
    }

    @GetMapping("/{}")
    public void get(MashtDTO dto) {
        try {
            long id = Long.parseLong(dto.getPathVariables().getLast());
            dto.sendResponse(passionsService.findById(id));
        } catch (NotFoundException e) {
            dto.sendResponse(404, "Not Found");
        } catch (NumberFormatException e) {
            dto.sendResponse(400, "Bad Request");
        }
    }

    @PostMapping
    public void post(MashtDTO dto) {
        try {
            JwtAuth jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        NewPassionRequest npr = (NewPassionRequest) dto.getRequestBody(NewPassionRequest.class);
        if (npr == null || !npr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }


        dto.sendResponse(passionsService.create(npr));
    }
}
