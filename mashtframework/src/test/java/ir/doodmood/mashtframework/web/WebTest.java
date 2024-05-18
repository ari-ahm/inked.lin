package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.OutputStream;

@RestController("/ali")
class MammadController {
    @GetMapping("javad/")
    void jafar(MashtDTO dt) throws Exception {
        dt.sendResponse(200, "salam");
    }

    @GetMapping
    void jafar2(MashtDTO dt) throws Exception {
        dt.sendResponse(200, "khodafez");
    }
}

@RestController("mammad/{}/1")
class jafar2Controller {
    @GetMapping("{}")
    private void ali(MashtDTO dt) throws Exception {
        String d = dt.getPathVariables().size() +
                   dt.getPathVariables().get(0) +
                   dt.getPathVariables().get(1);
        dt.sendResponse(200, d);
    }
}

@RestController("{}/")
class t1Controller {
    @GetMapping("ali/")
    public void ali(MashtDTO dt) throws Exception {
        dt.sendResponse(200, "from t1");
    }
}

@RestController("nmd/")
class t2Controller {
    @PostMapping("ali/")
    public void ali(MashtDTO dt) throws Exception {
        dt.sendResponse(200, "from t2");
    }
}

public class WebTest {
    @Test
    public void simpleTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/ali/javad?id=7");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("\"salam\"", new String(res.getEntity().getContent().readAllBytes()));
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
        Assert.assertEquals("\"khodafez\"", new String(res.getEntity().getContent().readAllBytes()));
    }

    @Test
    public void pathVariableTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/mammad/1234a5/1/tamano?id=tir");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("\"21234a5tamano\"", new String(res.getEntity().getContent().readAllBytes()));
    }

    @Test
    public void subPackageTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/javad/ali");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("\"salamAzMa\"", new String(res.getEntity().getContent().readAllBytes()));
    }

    @Test
    public void _404Test() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/javad/mammad");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(404, res.getStatusLine().getStatusCode());
    }

    @Test
    public void _404Tes2t() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {

        }

        HttpUriRequest req = new HttpPost("http://localhost:8080/javad/ali");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(404, res.getStatusLine().getStatusCode());
    }

    @Test
    public void postTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {
        }

        HttpUriRequest req = new HttpPost("http://localhost:8080/javad/mammad");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("\"salamAzMa\"", new String(res.getEntity().getContent().readAllBytes()));
    }

    @Test
    public void generalPathTest() throws Exception {
        try {
            MashtApplication.run(WebTest.class);
        } catch (Exception e) {
        }

        HttpUriRequest req = new HttpGet("http://localhost:8080/nmd/ali");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assert.assertEquals(200, res.getStatusLine().getStatusCode());
        Assert.assertEquals("\"from t1\"", new String(res.getEntity().getContent().readAllBytes()));
    }
}
