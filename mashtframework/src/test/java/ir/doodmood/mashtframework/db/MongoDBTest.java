package ir.doodmood.mashtframework.db;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class MongoDBTest {
//    private final MongoDB mongo = new MongoDB("mongodb://root:root@localhost:27017");
//
//    @Test
//    public void DataBaseConnection() {
//        mongo.close();
//        Assertions.assertNotNull(mongo);
//    }
//
//    @Test
//    public void DataBaseNewCollection() {
//        mongo.createCollection("users");
//    }
//
//    @Test
//    public void DataBaseInsertDocumentInCollection() {
//        DocumentSchema[] newDoc = {new DocumentSchema("username", "ari_ahm"), new DocumentSchema("email", "test@gmail.com"), new DocumentSchema("pass", "123")};
//        mongo.insertDocument("users", newDoc);
//    }
//
//    @Test
//    public void DataBaseFind() {
//        FindIterable<Document> doc = mongo.find("users", "username", "ari_ahm");
//        Object username = null;
//        if (doc != null && doc.first() != null)
//            username = doc.first().get("username");
//
//        Assertions.assertEquals(username.toString(), "ari_ahm");
//        mongo.close();
//    }
//
//    @Test
//    public void DataBaseWrongCollectionFind() {
//        FindIterable<Document> doc = mongo.find("users2", "username", "ari_ahm");
//        Object username = null;
//        if (doc != null && doc.first() != null)
//            username = doc.first().get("username");
//
//        Assertions.assertNull(username);
//        mongo.close();
//    }
//
//    @Test
//    public void DataBaseWrongFieldFind() {
//        FindIterable<Document> doc = mongo.find("users", "username2", "ari_ahm");
//        Object username = null;
//        if (doc != null && doc.first() != null)
//            username = doc.first().get("username");
//
//        Assertions.assertNull(username);
//        mongo.close();
//    }
//
//    @Test
//    public void DataBaseWrongUsernameFind() {
//        FindIterable<Document> doc = mongo.find("users", "username", "ari_ahmmm");
//        Object username = null;
//        if (doc != null && doc.first() != null)
//            username = doc.first().get("username");
//
//        Assertions.assertNull(username);
//        mongo.close();
//    }
}
