package com.batch.manager.controller;

import com.batch.manager.entity.Person.Person;
import com.batch.manager.entity.Person.UpdatedPersonDto;

import com.batch.manager.service.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/person")
@CrossOrigin
public class PersonController {

    @Autowired
    private PersonService personService;


    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person pPerson) {
        this.personService.savePerson(pPerson);
        return ResponseEntity.status(201).body(pPerson);
    }

    @PostMapping("/all")
    public ResponseEntity<List<Person>> addListPerson(@RequestBody List<Person> pPerson) {
        this.personService.savePersonList(pPerson);
        return ResponseEntity.status(201).body(pPerson);
    }

    @GetMapping
    public ResponseEntity<Set<Person>> getAllPersons() {
        Set<Person> persons = this.personService.getAllPersons();
        return ResponseEntity.status(200).body(persons);
    }

    @GetMapping("/{pId}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long pId) {
        Person person = this.personService.getPersonById(pId);
        return ResponseEntity.status(200).body(person);
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody UpdatedPersonDto pPerson) {
        Person person = this.personService.updatePerson(pPerson);
        return ResponseEntity.status(200).body(person);
    }


    @DeleteMapping
    public ResponseEntity<String> deletePerson(@PathVariable Long pId) {
        this.personService.deletePerson(pId);
        String msg = new String("Person deleted successfully");
        return ResponseEntity.status(200).body(msg);
    }
}