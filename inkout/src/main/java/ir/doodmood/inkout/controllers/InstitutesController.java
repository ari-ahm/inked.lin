package ir.doodmood.inkout.controllers;

// TODO implement search

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.NewInstituteRequest;
import ir.doodmood.inkout.services.InstitutesService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/institutes")
public class InstitutesController {
    InstitutesService institutessService;
    @Autowired
    public InstitutesController(InstitutesService institutessService) {
        this.institutessService = institutessService;
    }

    @GetMapping("/{}")
    public void get(MashtDTO dto) {
        try {
            long id = Long.parseLong(dto.getPathVariables().getLast());
            dto.sendResponse(institutessService.findById(id));
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

        NewInstituteRequest nir = (NewInstituteRequest) dto.getRequestBody(NewInstituteRequest.class);
        if (nir == null || !nir.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }


        dto.sendResponse(institutessService.create(nir));
    }
}
