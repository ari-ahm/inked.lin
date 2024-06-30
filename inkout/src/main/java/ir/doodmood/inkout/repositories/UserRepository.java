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

    public User addUser(UserRegisterRequest user) {
        User userDB = new User(user);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(userDB);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            return null;
        }

        return userDB;
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
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> from = criteria.from(User.class);
            criteria.select(from).where(builder.equal(from.get("email"), email));
            TypedQuery<User> query = session.createQuery(criteria);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
