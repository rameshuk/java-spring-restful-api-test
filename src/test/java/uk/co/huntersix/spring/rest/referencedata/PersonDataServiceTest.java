package uk.co.huntersix.spring.rest.referencedata;

import org.junit.Test;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class PersonDataServiceTest {

    private PersonDataService service = new PersonDataService();

    //test for findPerson
    @Test
    public void shouldReturnPersonWithMatch() {
        Optional<Person> results = service.findPerson("Smith", "Mary");
        assertTrue(results.isPresent());
        assertEquals("Mary", results.get().getFirstName());
        assertEquals("Smith", results.get().getLastName());
    }

    @Test
    public void shouldReturnEmptyResults() {
        Optional<Person> results = service.findPerson("abc", "xyz");
        assertFalse(results.isPresent());
    }

    //test for findPersonsBySurname
    @Test
    public void shouldReturnPersonsWithSameSurnameMatch() {
        List<Person> results = service.findPersonsBySurname("Smith");
        assertEquals(2, results.size());
        assertEquals("Smith", results.get(0).getLastName());
        assertEquals("BalckSmith", results.get(1).getLastName());
    }

    @Test
    public void shouldReturnEmptyResultsIfNoSurNameMatched() {
        List<Person> results = service.findPersonsBySurname("unknown");
        assertEquals(0, results.size());
    }

    @Test
    public void shouldReturnEmptyResultsIfSurnameIsEmpty() {
        List<Person> results = service.findPersonsBySurname("");
        assertEquals(0, results.size());
    }

    @Test
    public void shouldReturnEmptyResultsIfSurnameIsNull() {
        List<Person> results = service.findPersonsBySurname(null);
        assertEquals(0, results.size());
    }

    //add Person test cases
    @Test
    public void shouldReturnPersonAfterAddedSuccessfully() {
        Optional<Person> results = service.addPerson(new Person("new", "person"));
        assertTrue(results.isPresent());
        assertEquals("new", results.get().getFirstName());
        assertEquals("person", results.get().getLastName());
    }

    @Test
    public void shouldReturnEmptyOptionalIfPersonExistInList() {
        Optional<Person> results = service.addPerson(new Person("Mary", "Smith"));
        assertFalse(results.isPresent());
    }

}
