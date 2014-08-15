package org.soabridge.reference.java8.methodref;

import java.util.function.Supplier;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Person {

    public static Person create(Supplier<Person> supplier){
        Person person = supplier.get();
        System.out.println("'create()' has been called and created class " + person);
        return person;
    }

    public static void methodA(Person person) {
        System.out.println("static 'methodA()' of class Person has been called with class " + person.toString() + " as parameter");
    }

    public void methodB(Person person) {
        System.out.println("'methodB()' of class " + this.toString() + " was called with class " + person.toString() + " as parameter");
    }

    public void methodC() {
        System.out.println("'methodC()' of class " + this.toString() + " was called");
    }
}
