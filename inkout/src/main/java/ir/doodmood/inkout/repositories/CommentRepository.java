package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.Comment;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Repository
public class CommentRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    private CommentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Comment getById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(Comment.class).load(id);
        } catch (Exception e) {
            return null;
        }
    }
}
