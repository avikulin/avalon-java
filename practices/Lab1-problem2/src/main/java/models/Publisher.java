package models;

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

    public void setName(String name) throws IllegalArgumentException {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("Name must be set to non-blank value");
        }
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws IllegalArgumentException {
        if ((location == null) || location.isEmpty()) {
            throw new IllegalArgumentException("Location must be set to non-blank value");
        }
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return name.equals(publisher.name) &&
                location.equals(publisher.location);
    }
}
