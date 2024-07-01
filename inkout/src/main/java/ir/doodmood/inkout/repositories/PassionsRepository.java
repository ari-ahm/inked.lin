package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.Passion;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

@Repository
public class PassionsRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    private PassionsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Passion save(Passion passion) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(passion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            return null;
        }

        return passion;
    }

    public Optional<Passion> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(Passion.class).loadOptional(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
