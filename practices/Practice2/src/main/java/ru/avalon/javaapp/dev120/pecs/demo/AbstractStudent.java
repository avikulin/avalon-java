package ru.avalon.javaapp.dev120.pecs.demo;

public abstract class AbstractStudent extends Person {
    public AbstractStudent(String name, Sex sex, String dep) {
        super(name, sex, dep, "studies");
    }
}
