package ru.avalon.javaapp.dev120.pecs.demo;

public enum Sex {
    MALE("He", "His"),
    FEMALE("She", "Her");
    
    private final String pronoun;
    private final String pronounGen;

    private Sex(String pronoun, String pronounGen) {
        this.pronoun = pronoun;
        this.pronounGen = pronounGen;
    }

    public String getPronoun() {
        return pronoun;
    }

    public String getPronounGen() {
        return pronounGen;
    }
}
