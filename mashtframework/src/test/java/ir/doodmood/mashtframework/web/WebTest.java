package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.OutputStream;

@RestController("/ali")
class MammadController {
    @GetMapping("javad/")
    void jafar(MashtDTO dt) throws Exception {
        byte[] s = "salam".getBytes();
        dt.getHttpExchange().sendResponseHeaders(200, s.length);
        OutputStream st = dt.getHttpExchange().getResponseBody();
        st.write(s);
        st.close();
    }

    @GetMapping
    void jafar2(MashtDTO dt) throws Exception {
        byte[] s = "khodafez".getBytes();
        dt.getHttpExchange().sendResponseHeaders(200, s.length);
        OutputStream st = dt.getHttpExchange().getResponseBody();
        st.write(s);
        st.close();
    }
}

@RestController("mammad/{}/1")
class jafar2Controller {
    @GetMapping("{}")
    private void ali(MashtDTO dt) throws Exception {
        String d = dt.getPathVariables().size() +
                   dt.getPathVariables().get(0) +
                   dt.getPathVariables().get(1);
        byte[] s = d.getBytes();
        dt.getHttpExchange().sendResponseHeaders(200, s.length);
        OutputStream st = dt.getHttpExchange().getResponseBody();
        st.write(s);
        st.close();
    }
}

public class WebTest {
    @Test
    public void simpleTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/ali/javad");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("salam", new String(res.getEntity().getContent().readAllBytes()));
    }

    @Test
    public void simpleTest2() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/ali");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("khodafez", new String(res.getEntity().getContent().readAllBytes()));
    }

    @Test
    public void pathVariableTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/mammad/1234a5/1/tamano");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("21234a5tamano", new String(res.getEntity().getContent().readAllBytes()));
    }


}
