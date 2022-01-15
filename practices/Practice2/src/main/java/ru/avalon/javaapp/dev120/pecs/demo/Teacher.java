package ru.avalon.javaapp.dev120.pecs.demo;

public class Teacher extends Person {
    private Degree degree;
    private String spec;

    public Teacher(String name, Sex sex, String dep, Degree degree, String spec) {
        super(name, sex, dep, "teaches");
        setDegree(degree);
        setSpec(spec);
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        if(degree == null)
            throw new IllegalArgumentException("degree can't be null.");
        this.degree = degree;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        if(spec == null)
            throw new IllegalArgumentException("speciality can't be null.");
        this.spec = spec;
    }

    @Override
    public String getDetailInfo() {
        return getSex().getPronoun() + " has " + degree + " in " + spec + ".";
    }
}
