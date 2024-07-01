package ir.doodmood.inkout.repositories;

import ir.doodmood.inkout.models.Skill;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

@Repository
public class SkillsRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    private SkillsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Skill save(Skill skill) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(skill);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            return null;
        }

        return skill;
    }

    public Optional<Skill> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(Skill.class).loadOptional(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
