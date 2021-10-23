package ru.avalon.javapp.devj110.persondemo;

import java.util.Set;

enum Sex{
    MALE("He", "His"),
    FEMALE("She", "Her");
    private final String pronoun;
    private final String pronounGenerative;

    Sex(String pronoun, String pronounGenerative) {
        this.pronoun = pronoun;
        this.pronounGenerative = pronounGenerative;
    }

    public String getPronoun() {
        return pronoun;
    }

    public String getPronounGenerative(){
        return pronounGenerative;
    }
}

public abstract class Person {
    private String name;
    private String dep;
    private Sex sex;
    private String verb;

    public Person(String name, String dep, Sex sex, String verb) {
        setName(name);
        setDep(dep);
        setSex(sex);
        if (verb == null){
            throw new IllegalArgumentException("Verb must not be null");
        }
        this.verb = verb;
    }

    public String getName() {
        return name;
    }

    public String getDep() {
        return dep;
    }

    public Sex getSex() {
        return sex;
    }

    public void setName(String name) {
        if (name == null){
            throw new IllegalArgumentException("Name must not be null");
        }
        this.name = name;
    }

    public void setDep(String dep) {
        if (dep == null){
            throw new IllegalArgumentException("Department must not be null");
        }
        this.dep = dep;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void print(int pos){

        String template = "%d)\tThis is %s. %s %s at \"%s\"." +
                          "\n\t%s.\n";
        String outputString = String.format(template, pos, name, sex.getPronoun(), verb, dep, getDetailedInfo());
        System.out.println(outputString);
    }

    public abstract String getDetailedInfo();

    public static void printAll(Person[] persons){
        System.out.println("University persons introduction:");
        System.out.println("--------------------------------");
        int positionInList = 1;
        for (Person person:persons){
            person.print(positionInList);
            positionInList++;
        }
    }
}
