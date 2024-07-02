package ir.doodmood.inkout.repositories;

import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Repository
public class ProxiesRepository {
    private SessionFactory sessionFactory;
    @Autowired
    private ProxiesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T getProxy(Class<T> clazz, Object id) {
        try (Session session = sessionFactory.openSession()) {
            return session.getReference(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T save(T object) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
            return null;
        }

        return object;
    }
}
