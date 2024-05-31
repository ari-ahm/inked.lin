package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.BeforeAllExtension;
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
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;


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
        Transaction transaction = session.beginTransaction();
        session.persist(testEntity);
        transaction.commit();
        session.close();
        dt.sendResponse(200, "added");
    }

    @PostMapping
    public void post(MashtDTO dt) throws Exception {
        Session session = sessionFactory.openSession();
        TestEntity testEntity = session.get(TestEntity.class, 1L);
        dt.sendResponse(200, testEntity.getName() + testEntity.getAge() + testEntity.getAddress());
        session.close();
    }
}

@ExtendWith({BeforeAllExtension.class})
public class HibernateTest {
    private static MashtApplication application;

    @Test
    public void simpleTest() throws Exception {
        HttpUriRequest req = new HttpGet("http://localhost:8080/dbtest");
        HttpResponse res = HttpClientBuilder.create().build().execute(req);

        Assertions.assertEquals(200, res.getStatusLine().getStatusCode());

        req = new HttpPost("http://localhost:8080/dbtest");
        res = HttpClientBuilder.create().build().execute(req);

        Assertions.assertEquals(200, res.getStatusLine().getStatusCode());
        Assertions.assertEquals("\"ali20Iran\"", new String(res.getEntity().getContent().readAllBytes()));
    }

}
