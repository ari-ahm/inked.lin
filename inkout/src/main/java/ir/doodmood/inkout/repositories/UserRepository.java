package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.Post;
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

import java.util.List;

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

    public User updateUser(User user) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
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

    public List<User> search(String text, long id) {
        try (Session session = sessionFactory.openSession()) {
            return (List<User>) session.createQuery("FROM User u WHERE " +
                    "u.first_name LIKE :searchText OR " +
                    "u.last_name LIKE :searchText OR " +
                    "u.additional_name LIKE :searchText OR " +
                    "u.email LIKE :searchText").setParameter("searchText", text).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Post getPost(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(Post.class).load(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Post> getFeed(long id) {
        try (Session session = sessionFactory.openSession()) {
            return (List<Post>) session.createQuery("select distinct p from User u " +
                    "join u.connections c " +
                    "join c.posts p " +
                    "where u = :user" +
                    "ORDER BY p.createdAt DESC").setParameter("user", id).setMaxResults(30).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
