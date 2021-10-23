package ru.avalon.javapp.devj110.persondemo;

public abstract class AbstractStudent extends Person{
    public AbstractStudent(String name, String dep, Sex sex) {
        super(name, dep, sex, "studies");
    }

    @Override
    public abstract String getDetailedInfo();
}
