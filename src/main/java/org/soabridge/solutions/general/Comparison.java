package org.soabridge.solutions.general;

import java.util.UUID;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Comparison {

    public static void main(String[] args) {
        Comparable comp1 = new Comparable(12, "Hello World");
        Comparable comp2 = new Comparable(24, "Good-bye");
        Comparable comp3 = new Comparable(comp1.getId(), 12, "Hello World");

        System.out.printf("comp1 = %s%n", comp1);
        System.out.printf("comp2 = %s%n", comp2);
        System.out.printf("comp3 = %s%n", comp3);

        System.out.printf("comp1 == comp1 : %s%n", comp1.equals(comp1));
        System.out.printf("comp1 == comp2 : %s%n", comp1.equals(comp2));
        System.out.printf("comp1 == comp3 : %s%n", comp1.equals(comp3));
    }

    public static class Comparable {
        private int number;
        private String text;
        private final UUID id;

        public Comparable(int number, String text) {
            this(null, number, text);
        }

        public Comparable(UUID id, int number, String text) {
            this.id = (id == null? UUID.randomUUID() : id);
            this.text = text;
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }

        public UUID getId() {
            return id;
        }

        @Override
        public int hashCode() {
            // Initial prime number, then choosing another prime to multiply hash code with
            int hash = 13;
            // 'number' is an integer primitive therefore doesn't have hashCode(), using value instead
            hash = hash * 7 + number;
            // 'text' is a variable instance field and therefore can be NULL. So we check field, return 0 if it is null
            // otherwise call the String's hashCode() method
            hash = hash * 7 + (text != null ? text.hashCode() : 0);
            // 'id' is a constant and we know that it cannot be NULL ever so we don't bother to check it.
            hash = hash * 7 + id.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            // If it is the same object return true
            if (obj == this)
                return true;
            // If object is NULL or not of the same type as this class return false
            if (obj == null || !(obj instanceof Comparable))
                return false;
            // Now check every field independently
            Comparable comp = (Comparable) obj;
            return id.equals(comp.getId()) &&
                    number == comp.getNumber() &&
                    (text == null ? comp.getText() == null : text.equals(comp.getText()));
        }
    }
}
