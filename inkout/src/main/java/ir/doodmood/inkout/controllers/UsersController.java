package ir.doodmood.inkout.controllers;

// TODO: implement CRUD and forgetPassword
// TODO implement access modifiers to contact info

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.User;
import ir.doodmood.inkout.models.request.*;
import ir.doodmood.inkout.models.response.UserResponse;
import ir.doodmood.inkout.services.UsersService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.*;
import ir.doodmood.mashtframework.web.MashtDTO;
import org.hibernate.Remove;

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
        User u;
        try {
            u = usersService.find(request);
        } catch (NotFoundException e) {
            dto.sendResponse(404, "Not Found");
            return;
        }
        dto.sendResponse(new UserResponse(u));
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

        User u;
        try {
            u = usersService.register(request);
        } catch (AlreadyExistsException e) {
            dto.sendResponse(409, e.getMessage());
            return;
        }
        dto.sendResponse(new UserResponse(u));
    }

    @DeleteMapping
    private void delete(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        usersService.removeUser(jwtAuth.getId());
        dto.sendResponse(200, "OK");
    }

    @PutMapping
    private void put(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        UserRegisterRequest request = (UserRegisterRequest) dto.getRequestBody(UserRegisterRequest.class);
        if (request == null || !request.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.update(request, jwtAuth.getId())));
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

        dto.sendResponse(new UserResponse(usersService.addCertificate(ncr, jwtAuth.getId())));
    }

    @DeleteMapping("/certificates")
    private void remCert(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        RemoveCertificateRequest rcr = (RemoveCertificateRequest) dto.getRequestBody(RemoveCertificateRequest.class);
        if (rcr == null || !rcr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.removeCertificate(rcr, jwtAuth.getId())));
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

        dto.sendResponse(new UserResponse(usersService.addEducation(ner, jwtAuth.getId())));
    }

    @DeleteMapping("/education")
    private void remEdu(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        RemoveEducationRequest rer = (RemoveEducationRequest) dto.getRequestBody(RemoveEducationRequest.class);
        if (rer == null || !rer.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.removeEducation(rer, jwtAuth.getId())));
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

        dto.sendResponse(new UserResponse(usersService.addJobPos(nujpr, jwtAuth.getId())));
    }

    @DeleteMapping("/positions")
    private void remJobPos(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        RemoveJobPositionRequest rjpr = (RemoveJobPositionRequest) dto.getRequestBody(RemoveJobPositionRequest.class);
        if (rjpr == null || !rjpr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.removeJobPos(rjpr, jwtAuth.getId())));
    }

    @PostMapping("/skills")
    private void addSkill(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        AddSkill2UserRequest as2ur = (AddSkill2UserRequest) dto.getRequestBody(AddSkill2UserRequest.class);
        if (as2ur == null || !as2ur.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.addSkill(as2ur, jwtAuth.getId())));
    }

    @DeleteMapping("/skills")
    private void remSkill(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        RemoveSkillRequest rsr = (RemoveSkillRequest) dto.getRequestBody(RemoveSkillRequest.class);
        if (rsr == null || !rsr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.removeSkill(rsr, jwtAuth.getId())));
    }

    @PostMapping("/contact")
    private void setContactInfo(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        SetContactInfoRequest scir = (SetContactInfoRequest) dto.getRequestBody(SetContactInfoRequest.class);
        if (scir == null || !scir.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        dto.sendResponse(new UserResponse(usersService.setContactInfo(scir, jwtAuth.getId())));
    }


    @GetMapping("/search") // TODO
    private void search(MashtDTO dto) {
        UserSearchRequest scir = (UserSearchRequest) dto.getRequestBody(UserSearchRequest.class);
        if (scir == null || !scir.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }
    }
}
