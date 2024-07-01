package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.Institute;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

@Repository
public class InstitutesRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    private InstitutesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Institute save(Institute institute) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(institute);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            return null;
        }

        return institute;
    }

    public Optional<Institute> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(Institute.class).loadOptional(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
