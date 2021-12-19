package ru.avalon.javaapp.dev120.pecs.demo;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Person[] persons = {
            new Teacher("Ronald Turner", Sex.MALE, "Computer science",
                    Degree.PHD, "Programming paradigms"),
            new Teacher("Ruth Hollings", Sex.FEMALE, "Jurisprudence",
                    Degree.DOCTOR, "Domestic arbitration"),
            new RegularStudent("Leo Wilkinson", Sex.MALE, "Computer science", 
                    3, Stage.BACHELOR),
            new RegularStudent("Anna Cunningham", Sex.FEMALE, "World economy",
                    1, Stage.BACHELOR),
            new RegularStudent("Jill Lundqvist", Sex.FEMALE, "Jurisprudence",
                    1, Stage.MASTER),
            new PhdStudent("Ronald Correa", Sex.MALE, "Computer science", 
                    "Design of a functional programming language.")
        };
        
        /*Person.printAll(persons);*/

        List<Person> pl = new ArrayList<>();
        pl.add(new Teacher("Ruth Hollings", Sex.FEMALE, "Jurisprudence",
                Degree.DOCTOR, "Domestic arbitration"));

        pl.add(new RegularStudent("Jill Lundqvist", Sex.FEMALE, "Jurisprudence",
                1, Stage.MASTER));

        Person.printAll(pl);

        System.out.println("---");

        Person[] pl2 = new Person[2];
        pl2[0] = new Teacher("Ruth Hollings", Sex.FEMALE, "Jurisprudence",
                Degree.DOCTOR, "Domestic arbitration");

        pl2[1] = new RegularStudent("Jill Lundqvist", Sex.FEMALE, "Jurisprudence",
                1, Stage.MASTER);

        Person.printAll(pl2);
    }
}
