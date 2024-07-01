package ir.doodmood.inkout.controllers;

import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.ConnectRequest;
import ir.doodmood.inkout.models.request.FollowRequest;
import ir.doodmood.inkout.services.ConnectionService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController
public class ConnectionController {
    private ConnectionService connectionService;
    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/follow")
    private void follow(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        FollowRequest fr = (FollowRequest) dto.getRequestBody(FollowRequest.class);
        if (fr == null || !fr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        connectionService.follow(fr, jwtAuth.getId());
        dto.sendResponse(200, "OK");
    }

    @PostMapping("/connect")
    private void connect(MashtDTO dto) {
        JwtAuth jwtAuth;
        try {
            jwtAuth = (JwtAuth) dto.readJWTToken(JwtAuth.class);
        } catch (Exception e) {
            dto.sendResponse(401, "Unauthorized");
            return;
        }

        ConnectRequest fr = (ConnectRequest) dto.getRequestBody(ConnectRequest.class);
        if (fr == null || !fr.validate()) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        connectionService.connect(fr, jwtAuth.getId());
        dto.sendResponse(200, "OK");
    }
}
