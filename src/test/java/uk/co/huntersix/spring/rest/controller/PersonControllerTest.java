package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(Optional.of(new Person("Mary", "Smith")));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNotFoundIfPersonNotExists() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/person/firstName/lastName"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnOnEmptyListIfPersonNotFoundWithSurname() throws Exception {
        when(personDataService.findPersonsBySurname(any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/search/unknown"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*]", hasSize(0)));
    }

    @Test
    public void shouldReturnOneEntryInResponseForOneMatch() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Mary", "Smith"));
        when(personDataService.findPersonsBySurname(any())).thenReturn(persons);
        this.mockMvc.perform(get("/search/Smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*]", hasSize(1)))
                .andExpect(jsonPath("[0].firstName").value("Mary"))
                .andExpect(jsonPath("[0].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnMultipleEntriesInResponseForOneMatch() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Mary", "Smith"));
        persons.add(new Person("Scott", "BlackSmith"));
        when(personDataService.findPersonsBySurname(any())).thenReturn(persons);
        this.mockMvc.perform(get("/search/Smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*]", hasSize(2)))
                .andExpect(jsonPath("[0].lastName").value("Smith"))
                .andExpect(jsonPath("[1].lastName").value("BlackSmith"));
    }

    //add new person
    @Test
    public void shouldAddNewPerson() throws Exception {
        Optional<Person> newPerson = Optional.of(new Person("Scott", "Tiger"));
        when(personDataService.addPerson(any())).thenReturn(newPerson);
        this.mockMvc.perform(post("/person")
                .content(toJson(newPerson.get()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Scott"))
                .andExpect(jsonPath("lastName").value("Tiger"));
    }

    @Test
    public void shouldThrowConflictIfPersonExist() throws Exception {
        Optional<Person> newPerson = Optional.of(new Person("Mary", "Smith"));
        when(personDataService.addPerson(any())).thenReturn(Optional.empty());
        this.mockMvc.perform(post("/person")
                .content(toJson(newPerson.get()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnBadRequestIfErrorPayload() throws Exception {
        this.mockMvc.perform(post("/person")
                .content(toJson("some String"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenFirstNameIsNull() throws Exception {
        Optional<Person> newPerson = Optional.of(new Person(null, "Tiger"));
        this.mockMvc.perform(post("/person")
                .content(toJson(newPerson.get()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenLastNameIsNull() throws Exception {
        Optional<Person> newPerson = Optional.of(new Person("Scott", null));
        this.mockMvc.perform(post("/person")
                .content(toJson(newPerson.get()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNameIsEnpty() throws Exception {
        Optional<Person> newPerson = Optional.of(new Person("", ""));
        this.mockMvc.perform(post("/person")
                .content(toJson(newPerson.get()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    private static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}