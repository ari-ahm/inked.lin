package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.User;
import ir.doodmood.inkout.models.request.UserRegisterRequest;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Repository
public class UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    private UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User saveUser(User user) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
            return null;
        }

        return user;
    }

    public User getUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(User.class).load(id);
        } catch (Exception e) {
            return null;
        }
    }

    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return (User) session.createQuery("FROM User u WHERE u.email = :userId").setParameter("userId", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(getUser(id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }
}
