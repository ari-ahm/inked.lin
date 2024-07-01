package ir.doodmood.inkout.controllers;

// TODO: implement CRUD and forgetPassword

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.*;
import ir.doodmood.inkout.models.response.UserResponse;
import ir.doodmood.inkout.services.UsersService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    private UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/{}")
    private void get(MashtDTO dto) {
        UserFindRequest request = new UserFindRequest();
        request.setId(Long.parseLong(dto.getPathVariables().get(0)));
        UserResponse ur;
        try {
            ur = usersService.find(request);
        } catch (NotFoundException e) {
            dto.sendResponse(404, "Not Found");
            return;
        }
        dto.sendResponse(ur);
    }

    @PostMapping("/login")
    private void login(MashtDTO dto) {
        UserLoginRequest request = (UserLoginRequest) dto.getRequestBody(UserLoginRequest.class);
        if (request == null || !request.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        try {
            long id = usersService.checkPassword(request);
            dto.setJWTToken(new JwtAuth(id));
            dto.sendResponse(200, "ok");
        } catch(NotFoundException e) {
            dto.sendResponse(404, "Not Found");
        } catch(WrongPassException e) {
            dto.sendResponse(401, "Wrong Password");
        }
    }

    @PostMapping
    private void post(MashtDTO dto) {
        UserRegisterRequest request = (UserRegisterRequest) dto.getRequestBody(UserRegisterRequest.class);
        if (request == null || !request.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        UserResponse ur;
        try {
            ur = usersService.register(request);
        } catch (AlreadyExistsException e) {
            dto.sendResponse(409, e.getMessage());
            return;
        }
        dto.sendResponse(ur);
    }

    @PostMapping("/certificates")
    private void addCert(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        NewCertificateRequest ncr = (NewCertificateRequest) dto.getRequestBody(NewCertificateRequest.class);
        if (ncr == null || !ncr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        usersService.addCertificate(ncr, jwtAuth.getId());
    }

    @PostMapping("/education")
    private void addEdu(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        NewEducationRequest ner = (NewEducationRequest) dto.getRequestBody(NewEducationRequest.class);
        if (ner == null || !ner.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        usersService.addEducation(ner, jwtAuth.getId());
    }

    @PostMapping("/positions")
    private void addJobPos(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        NewUserJobPositionRequest nujpr = (NewUserJobPositionRequest) dto.getRequestBody(NewUserJobPositionRequest.class);
        if (nujpr == null || !nujpr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        usersService.addJobPos(nujpr, jwtAuth.getId());
    }


}
