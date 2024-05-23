package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.HasFactoryMethod;
import ir.doodmood.mashtframework.core.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Component(singleton = true)
public class HibernateSessionFactoryWrapper implements HasFactoryMethod {
    private static SessionFactory hibernateSessionFactory;

    HibernateSessionFactoryWrapper() {
        try {
            this.hibernateSessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            Logger logger = (Logger) ComponentFactory.factory(Logger.class).getNew();
            logger.error("could not connect to database", e);
        }
    }

    public Object getNew() {
        return hibernateSessionFactory;
    }
}
