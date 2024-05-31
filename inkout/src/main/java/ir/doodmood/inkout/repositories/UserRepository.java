package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.User;
import ir.doodmood.inkout.models.request.UserRegister;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
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

    public User addUser(UserRegister user) {
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
}
