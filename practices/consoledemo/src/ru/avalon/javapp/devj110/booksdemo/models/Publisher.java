package ru.avalon.javapp.devj110.booksdemo.models;

public class Publisher {
    private String name;
    private String location;

    public Publisher(String name, String location) {
        setName(name);
        setLocation(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("Name must be set to non-blank value");
        }
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if ((location == null) || location.isEmpty()) {
            throw new NullPointerException("Location must be set to non-blank value");
        }
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, location);
    }

}
