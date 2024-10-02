package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery("""
                                CREATE TABLE IF NOT EXISTS users (
                                id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(255),
                                last_name VARCHAR(255),
                                age SMALLINT)
                            """).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("При создании таблицы пользователей произошло исключение");
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("При удалении таблицы произошло исключение");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Во время сохранения пользователя произошло исключение");
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            session.delete(session.get(User.class, id));
            tx.commit();
        } catch (Exception e) {
            System.out.println("При удалении пользователя по id произошло исключение");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            users = session.createQuery("from User").getResultList();
            tx.commit();
        } catch (Exception e) {
            System.out.println("При попытке достать всех пользователей из базы данных произошло исключение");
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery("DELETE FROM users").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("При очистке таблицы пользователей произошло исключение");
        }
    }
}
