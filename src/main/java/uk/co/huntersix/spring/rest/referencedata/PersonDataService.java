package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = new ArrayList<>(Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown"),
        new Person("Collin", "BalckSmith")
    ));

    public Optional<Person> findPerson(String lastName, String firstName) {
        return PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public List<Person> findPersonsBySurname(String surName) {
        if (surName == null || surName.trim().isEmpty())
            return new ArrayList<>();

        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().toLowerCase().contains(surName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Person> addPerson(Person person) {
        //check if person is already exists
        Optional<Person> results = this.findPerson(person.getLastName(), person.getFirstName());
        if (results.isPresent())
            return Optional.empty();
        else {
            PERSON_DATA.add(person);
            return Optional.of(person);
        }
    }
}
