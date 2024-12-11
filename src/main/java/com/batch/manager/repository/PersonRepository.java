package com.batch.manager.repository;

import com.batch.manager.entity.Person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByIdText(String idText);

    Optional<Person> findByFullName(String fullName);
}
