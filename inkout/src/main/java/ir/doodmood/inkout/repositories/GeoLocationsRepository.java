package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.GeoLocation;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Repository
public class GeoLocationsRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    private GeoLocationsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<GeoLocation> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return (List<GeoLocation>) session.createQuery("FROM GeoLocation").list();
        } catch (Exception e) {
            return null;
        }
    }
}
