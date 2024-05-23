package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

@RestController("/dbtest")
class DBTestController {
    private SessionFactory sessionFactory;
    @Autowired
    private DBTestController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GetMapping
    public void get(MashtDTO dt) throws Exception {
        Session session = sessionFactory.openSession();
        TestEntity testEntity = new TestEntity();
        testEntity.setName("ali");
        testEntity.setAge(20);
        testEntity.setAddress("Iran");
        testEntity.setId(1L);
        session.save(testEntity);
        dt.sendResponse(200, "added");
        session.close();
    }

    @PostMapping
    public void post(MashtDTO dt) throws Exception {
        Session session = sessionFactory.openSession();
        TestEntity testEntity = session.get(TestEntity.class, 1L);
        dt.sendResponse(200, testEntity.getName() + testEntity.getAge() + testEntity.getAddress());
        session.close();
    }
}

public class HibernateTest {
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest").withExposedPorts(5432).withDatabaseName("inkedlin").withEnv("POSTGRES_USER", "root").withEnv("POSTGRES_PASSWORD", "root");

    @BeforeAll
    public static void init() throws Exception {
        postgres.start();
        MashtApplication.run(HibernateTest.class);
    }

    @AfterAll
    public static void close() {
        postgres.stop();
    }

    @Test
    public void simpleTest() throws Exception {
        HttpUriRequest req = new HttpGet("http://localhost:8080/dbtest");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assertions.assertEquals(res.getStatusLine().getStatusCode(), 200);

        req = new HttpPost("http://localhost:8080/dbtest");
        res = HttpClientBuilder.create().build().execute(req);

        Assertions.assertEquals(res.getStatusLine().getStatusCode(), 200);
        Assertions.assertEquals("\"ali20Iran\"", new String(res.getEntity().getContent().readAllBytes()));
    }

}
