package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.JWTTokenExpiredException;
import ir.doodmood.mashtframework.exception.JWTVerificationFailedException;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

@Getter
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
    public void post(MashtDTO dt) throws Exception {
        dt.sendResponse(((John) dt.readJWTToken(John.class)).getName());
    }
}

public class JWTTest {
    @Test
    public void simpleTest() {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        Assertions.assertEquals(true, jwt.verify(jwt.getToken(new John("salam"), 10)));
    }

    @Test
    public void verificationTest() throws Exception {
        Assertions.assertThrowsExactly(JWTVerificationFailedException.class, () -> {
            JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
            jwt.getPayload(John.class, jwt.getToken(new John("salam"), 10) + "sa", null);
        });
    }

    @Test
    public void expiredTest2() throws Exception {
        Assertions.assertThrowsExactly(JWTTokenExpiredException.class, () -> {
            JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
            String token = jwt.getToken(new John("salam"), 1);
            Thread.sleep(2000);
            jwt.getPayload(John.class, token, null);
        });
    }

    @Test
    public void csrfTest() throws Exception {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        String token = jwt.getToken(new John("salam"), 1, jwt.generateCSRFToken(16));
        ((Logger) ComponentFactory.factory(Logger.class).getNew()).debug(token);
    }

//    @Test
//    public void cookieTest() throws Exception {
//        try {
//            MashtApplication.run(JWTTest.class);
//        } catch (Exception e) {
//
//        }
//
//        Thread.sleep(1000000);
//
//          // tests were done in postman bc using the cookieManager was too hard to write just one test.
//    }
}
