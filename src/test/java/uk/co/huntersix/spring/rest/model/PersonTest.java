package uk.co.huntersix.spring.rest.model;

import org.junit.Test;

import static junit.framework.TestCase.*;

public class PersonTest {
    @Test
    public void shouldAssignIdWhenCreated() {
        Person classUnderTest = new Person("John", "Smith");

        assertNotNull(classUnderTest.getId());
    }

    @Test
    public void idsShouldBeDifferent() {
        Person classUnderTest1 = new Person("John", "Smith");
        Person classUnderTest2 = new Person("Harry", "Brown");
        System.out.println(classUnderTest1.getId());
        System.out.println(classUnderTest2.getId());

        assertFalse(classUnderTest1.getId().equals(classUnderTest2.getId()));
    }


    @Test
    public void shouldBeDifferent() {
        Person classUnderTest1 = new Person("John", "Smith");
        Person classUnderTest2 = new Person("John", "Smith");

        assertNotSame(classUnderTest1, classUnderTest2);
    }
}
