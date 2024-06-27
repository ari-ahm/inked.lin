package ir.doodmood.inkout.controllers;

// TODO add authentication for put method.

import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PutMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/fileauth")
public class FileAuthController {
    @GetMapping
    public void getAuth(MashtDTO dto) {
        dto.sendResponse(200);
    }
    @PutMapping
    public void putAuth(MashtDTO dto) {
        dto.sendResponse(200);
    }
}
