package ru.avalon.javapp.devj110.persondemo;

import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        Person[] persons = new Person[]{
                new Teacher("Ronald Turner", Sex.MALE, "Computer science", Degree.PHD, "Programming paradigms"),
                new Teacher("Puth Hollings", Sex.FEMALE, "Jurisprudence", Degree.DOCTOR, "Domestic arbitration"),
                new RegularStudent("Leo Wilkinson", Sex.MALE, "Computer science", 3, Stage.BACHELOR),
                new RegularStudent("Anna Canningham", Sex.FEMALE, "World economy", 1, Stage.BACHELOR),
                new RegularStudent("Jill Lundquist", Sex.FEMALE, "Jurisprudence", 1, Stage.MASTER),
                new PhDStudent("Ronald Correa", Sex.MALE, "Computer science", "Design of functional programming language")
        };
        Person.printAll(persons);
    }
}
