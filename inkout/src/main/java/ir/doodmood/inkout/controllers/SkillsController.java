package ir.doodmood.inkout.controllers;

// TODO implement search

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.NewPassionRequest;
import ir.doodmood.inkout.models.request.NewSkillRequest;
import ir.doodmood.inkout.services.PassionsService;
import ir.doodmood.inkout.services.SkillsService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/skills")
public class SkillsController {
    SkillsService skillsService;
    @Autowired
    public SkillsController(SkillsService passionsService) {
        this.skillsService = passionsService;
    }

    @GetMapping("/{}")
    public void get(MashtDTO dto) {
        try {
            long id = Long.parseLong(dto.getPathVariables().getLast());
            dto.sendResponse(skillsService.findById(id));
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

        NewSkillRequest nsr = (NewSkillRequest) dto.getRequestBody(NewSkillRequest.class);
        if (nsr == null || !nsr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }


        dto.sendResponse(skillsService.create(nsr));
    }
}
