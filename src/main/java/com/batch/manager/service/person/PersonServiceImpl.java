package com.batch.manager.service.person;

import com.batch.manager.entity.Person.Person;
import com.batch.manager.entity.Person.UpdatedPersonDto;
import com.batch.manager.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
public class PersonServiceImpl implements PersonService{
    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person getPersonById(Long pId) {
        Optional<Person> person = personRepository.findById(pId);
        if (person.isEmpty()) {
            throw new RuntimeException("Person not found");
        }
        return person.get();
    }

    @Override
    @Transactional
    public void savePerson(Person pPerson) {
        this.validatePerson(pPerson);
        this.personAlreadyExists(pPerson);
        this.personRepository.save(pPerson);
    }

    @Override
    public void savePersonList(List<Person> pPerson) {
        personRepository.saveAll(pPerson);
    }

    @Override
    @Transactional
    public Person updatePerson(UpdatedPersonDto pPerson) {
        Person personUpdated = this.getPersonById(pPerson.idPerson());
        if(pPerson.fullName() != null){
            personUpdated.setFullName(pPerson.fullName());
        } else if (pPerson.codeDevice() != null) {
            personUpdated.setCodeDevice(pPerson.codeDevice());
        }
        return personUpdated;
    }

    @Override
    @Transactional
    public void deletePerson(Long pId) {
        Person person = this.getPersonById(pId);
        personRepository.delete(person);
    }

    @Override
    public Set<Person> getAllPersons() {
        Set<Person> setPersons = new HashSet<>();
        setPersons.addAll(this.personRepository.findAll());
        return setPersons;
    }


    public void validatePerson(Person pPerson) {
        if(pPerson == null ||
                pPerson.getIdText() == null ||
                pPerson.getIdText().isBlank() ||
                pPerson.getFullName() == null ||
                pPerson.getFullName().isBlank() ||
                pPerson.getCodeDevice() == null ||
                pPerson.getCodeDevice().isBlank()) {
            throw new IllegalArgumentException("Some field is null or empty.");
        }
    }

    @Override
    public Person findByFullName(String fullName) {
        Optional<Person> personExists = personRepository.findByFullName(fullName);
        if (personExists.isEmpty()) {
            throw new RuntimeException("Person not found 42");
        }
        return personExists.get();
    }

    private void personAlreadyExists(Person pPerson){
        Optional<Person> personExists = personRepository.findByIdText(pPerson.getIdText());
        if (personExists.isPresent()) {
            throw new RuntimeException("Person already exists");
        }
    }
}
