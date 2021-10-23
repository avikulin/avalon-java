package ru.avalon.javapp.devj110.persondemo;

enum Degree{
    CANDIDATE,
    DOCTOR,
    PHD
}

public class Teacher extends Person {
    private Degree degree;
    private String speciality;

    public Teacher(String name, Sex sex, String dep, Degree degree, String speciality) {
        super(name, dep, sex, "teaches");
        setDegree(degree);
        setSpeciality(speciality);
    }

    @Override
    public String getDetailedInfo() {
        String template = "%s has %s degree in \"%s\" speciality";
        String outputString = String.format(template, getSex().getPronoun(), degree, speciality);
        return outputString;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setSpeciality(String speciality) {
        if (speciality == null){
            throw new IllegalArgumentException("Speciality must not be null");
        }
        this.speciality = speciality;
    }

    public Degree getDegree() {
        return degree;
    }

    public String getSpeciality() {
        return speciality;
    }
}
