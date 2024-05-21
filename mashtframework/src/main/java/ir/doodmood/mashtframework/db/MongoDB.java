package ir.doodmood.mashtframework.db;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import ir.doodmood.mashtframework.core.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

@Deprecated
public class MongoDB {
    private final String uri;
    private final Logger logger;
    private MongoClient mongoClient = null;


    public MongoDB(String uri) {
        this.uri = uri;
        logger = new Logger();
        try {
            mongoClient = MongoClients.create(uri);
            logger.info("MongoDB connected successfully");
        } catch (Exception e) {
            logger.error("Something went wrong on Database Connection: ", e.toString());
        }
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        if (mongoClient == null) {
            throw new MongoException("mongo client couldn't connect to db!");
        }

        MongoCollection<Document> collection = null;
        try {
            MongoDatabase database = mongoClient.getDatabase("inkedLin");
            collection = database.getCollection(collectionName);
        } catch (Exception e) {
            logger.error("An Error Happened in getCollection method, arguments: (collectionName)", collectionName, "Exception: ", e);
        }

        return collection;
    }

    public FindIterable<Document> find(String collection, String field, String value) {
        FindIterable<Document> docs = null;
        try {
            docs = getCollection(collection).find(eq(field, value));
        } catch (Exception e) {
            logger.error("An Error Happened in find method, arguments: (collection, field, value)", collection, field, value, "Exception: ", e);
        }
        return docs;
    }

    public void createCollection(String collection) {
        MongoDatabase db = mongoClient.getDatabase("inkedLin");
        try {
            db.createCollection(collection);
            logger.info(String.format("New collection named %s was created successfully", collection));
        } catch (Exception e) {
            logger.error("There was an error in creating collection named:", collection, "in inkedLin database");
        }
    }

    public void insertDocument(String collection, DocumentSchema ...parameters) throws MongoException {
        MongoCollection<Document> coll = getCollection(collection);
        if (coll == null) {
            throw new MongoException("Collection does not exist");
        }
        if (parameters.length == 0) {
            throw new MongoException("Inserting document need at least one parameter");
        }
        Document doc = new Document();
        doc.append("_id", new ObjectId());
        for (DocumentSchema data : parameters) {
            doc.append(data.getKey(), data.getValue());
        }

        try {
            InsertOneResult result = coll.insertOne(doc);
            logger.info(String.format("inserted new document in %s collection successfully", collection));
        } catch (MongoException e) {
            logger.error("Unable to insert due to an error: " + e);
        }
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("MongoDB connection closed");
        }
    }
}