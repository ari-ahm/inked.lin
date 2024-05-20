package ir.doodmood.mashtframework.web.subpackage;

import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

import java.io.IOException;

@RestController("/javad")
public class javad {
    @GetMapping("/ali")
    @PostMapping("/mammad")
    public void amir(MashtDTO dt) throws IOException {
        dt.sendResponse(200, "salamAzMa");
    }
}
