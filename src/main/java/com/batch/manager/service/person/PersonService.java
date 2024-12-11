package com.batch.manager.service.person;

import com.batch.manager.entity.Person.Person;
import com.batch.manager.entity.Person.UpdatedPersonDto;

import java.util.List;
import java.util.Set;

public interface PersonService {
    public Person getPersonById(Long pId);

    public void savePerson(Person pPerson);

    public void savePersonList(List<Person> pPerson);

    public Person updatePerson(UpdatedPersonDto pPerson);

    public void deletePerson(Long pId);

    public Set<Person> getAllPersons();

    public void validatePerson(Person pPerson);

    public Person findByFullName(String fullName);
}
