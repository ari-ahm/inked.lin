package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.JWTTokenExpiredException;
import ir.doodmood.mashtframework.exception.JWTVerificationFailedException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

class John {
    private String name;
    public John(String name) {
        this.name = name;
    }
}

@RestController("/aliton")
class aliton {
    @GetMapping
    public void alit(MashtDTO dt) throws IOException {
        dt.setJWTToken(new John("johnathan"), 1000);
        dt.sendResponse(new Object() {
            String status = "done";
        });
    }

    @PostMapping
    public void post(MashtDTO dt) throws IOException {
        dt.readJWTToken(John.class);
    }
}

public class JWTTest {
    @Test
    public void simpleTest() {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        Assert.assertEquals(true, jwt.verify(jwt.getToken(new John("salam"), 10)));
    }

    @Test(expected = JWTVerificationFailedException.class)
    public void verificationTest() throws Exception {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        jwt.getPayload(John.class, jwt.getToken(new John("salam"), 10) + "sa");
    }

    @Test(expected = JWTTokenExpiredException.class)
    public void expiredTest2() throws Exception {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        String token = jwt.getToken(new John("salam"), 1);
        Thread.sleep(2000);
        jwt.getPayload(John.class, token);
    }

    @Test
    public void csrfTest() throws Exception {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        String token = jwt.getToken(new John("salam"), 1, jwt.generateCSRFToken(16));
        ((Logger) ComponentFactory.factory(Logger.class).getNew()).debug(token);
    }

//    @Test
//    public void serverTest() throws Exception {
//        try {
//            MashtApplication.run(JWTTest.class);
//        } catch (Exception e) {
//
//        }
//
//        Thread.sleep(10000000);
//    }
}
