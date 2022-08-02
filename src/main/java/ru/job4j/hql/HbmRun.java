package ru.job4j.hql;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

@Slf4j
public class HbmRun {

    public static void main(String[] args) {
        try (StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
             SessionFactory factory = new MetadataSources(registry)
                     .buildMetadata()
                     .buildSessionFactory();
             Session session = factory.openSession()) {
                session.beginTransaction();
                Candidate one = Candidate.of("One", "One Test", 3.1415);
                Candidate two = Candidate.of("Two", "Two Test", 3.141592);
                Candidate three = Candidate.of("Three", "Three Test", 3.1415926);

                session.save(one);
                session.save(two);
                session.save(three);
                session.getTransaction().commit();
                StandardServiceRegistryBuilder.destroy(registry);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    public static List findAllCandidates(Session session) {
        return session.createQuery("from Candidate").stream().toList();
    }

    public static Candidate findCandidateAtId(Session session, Long id) {
        return (Candidate) session.createQuery("from Candidate c where c.id = :fid").setParameter("fid", id).uniqueResult();
    }

    public static Candidate findCandidateAtName(Session session, String name) {
        return (Candidate) session.createQuery("from Candidate c where c.name = :fname").setParameter("fname", name).uniqueResult();
    }

    public static int updateCandidate(Session session, Candidate candidate) {
        return session.createQuery("update Candidate c set c.name = :fname, "
                        + "c.experience = :fexperience, "
                        + "c.salary = :fsalary where c.id = :fid")
                .setParameter("fname", candidate.getName())
                .setParameter("fexperience", candidate.getExperience())
                .setParameter("fsalary", candidate.getSalary())
                .setParameter("fid", candidate.getId())
                .executeUpdate();
    }

    public static int deleteCandidate(Session session, int id) {
        return session.createQuery("delete from Candidate where id = :fid")
                .setParameter("fid", id)
                .executeUpdate();
    }

}
