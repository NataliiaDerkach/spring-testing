package com.example;

import org.hibernate.SessionFactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;




@SpringBootTest(classes = SpringBootCrudApplication.class)
public class SpringBootCrudApplicationTest {
    @Test
    void contextLoads() {
    }

    private SessionFactory sessionFactory;
    private Session session;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().rollback();
        session.close();
        HibernateUtil.shutdown();
    }

}
