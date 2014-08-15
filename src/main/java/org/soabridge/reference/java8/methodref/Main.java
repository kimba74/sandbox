package org.soabridge.reference.java8.methodref;

import java.util.Arrays;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) {

        // Method Reference 1:
        // Reference to a constructor ClannName::new
        Person speaker = Person.create(Person::new);
        Person p1 = Person.create(Person::new);
        Person p2 = Person.create(Person::new);
        Person p3 = Person.create(Person::new);
        Person p4 = Person.create(Person::new);
        Person[] persons = {p1, p2, p3, p4};

        // Method Reference 2:
        //Reference to a static method
        Arrays.asList(persons).forEach(Person::methodA);

        // Method Reference 3:
        // Reference to an instance method of a specific object
        Arrays.asList(persons).forEach(speaker::methodB);

        // Method Reference 4:
        // Reference to an instance method of an arbitrary object
        Arrays.asList(persons).forEach(Person::methodC);
    }

}
