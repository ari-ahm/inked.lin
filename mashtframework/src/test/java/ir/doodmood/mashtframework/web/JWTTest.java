package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.JWTTokenExpiredException;
import ir.doodmood.mashtframework.exception.JWTVerificationFailedException;
import org.junit.Assert;
import org.junit.Test;

class John {
    private String name;
    public John(String name) {
        this.name = name;
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
}
