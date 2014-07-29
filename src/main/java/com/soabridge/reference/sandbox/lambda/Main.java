package com.soabridge.reference.sandbox.lambda;

/**
 * Missing documentation
 *
 * @since 1.0
 */
public class Main {

    public static void main(String... args) {
        SimplePrint p1 = System.out::println;
        p1.print("Hello World");

        SimplePrint p2 = s -> System.out.println("Message: " + s);
        p2.print("Hello World");

        DualPrint d1 = (s1,s2) -> System.out.println("First: " + s1 + ", Second: " + s2);
        d1.print("Hello", "World");

        SimpleFormat f1 = s -> "** " + s + " **";
        p1.print(f1.format("Hello World"));

        SimpleFormat prefix = s -> "<< " + s;
        SimpleFormat suffix = s -> s + " >>";
        SimpleFormat f2 = prefix.andThen(suffix);
        p1.print(f2.format("Hello World"));

        SimpleMath division = n -> n / 2;
        SimpleMath addition = n -> n + 2;
        SimpleMath m1 = division.andThen(addition);
        SimpleMath m2 = division.compose(addition);
        p1.print("(8.0 / 2) + 2 = " + m1.evaluate(8));
        p1.print("(8.0 + 2) / 2 = " + m2.evaluate(8));
    }

}
