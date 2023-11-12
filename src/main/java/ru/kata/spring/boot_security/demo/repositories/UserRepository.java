package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String name);

    //При необходимости можно использовать аннотацию Query над методом и писать запросы на HQL
    // или SQL (нужно добавить nativeQuery = true).
//    @Query(value = "SELECT nextval(pg_get_serial_sequence('t_user', 'id'))", nativeQuery = true)
//    Long getNextId();
}
