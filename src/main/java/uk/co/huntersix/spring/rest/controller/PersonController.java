package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<Person> person(@PathVariable(value="lastName") String lastName,
                                         @PathVariable(value="firstName") String firstName) {
        Optional<Person> person = personDataService.findPerson(lastName, firstName);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{surName}")
    public ResponseEntity<List<Person>> person(@PathVariable(value="surName") String surName) {
        List<Person> persons = personDataService.findPersonsBySurname(surName);
        return ResponseEntity.ok(persons);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person newPerson) {
        Optional<Person> addedPerson = personDataService.addPerson(newPerson);
        if (addedPerson.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(newPerson);
        }
    }
}