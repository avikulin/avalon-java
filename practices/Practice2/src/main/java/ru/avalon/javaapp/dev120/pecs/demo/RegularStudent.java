package ru.avalon.javaapp.dev120.pecs.demo;

public class RegularStudent extends AbstractStudent {
    private int course;
    private Stage stage;

    public RegularStudent(String name, Sex sex, String dep, int course, Stage stage) {
        super(name, sex, dep);
        setCourse(course);
        setStage(stage);
    }
    
    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        if(course <= 0 || course > 6)
            throw new IllegalArgumentException("course must be a number betwwen 1 and 6.");
        this.course = course;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        if(stage == null)
            throw new IllegalArgumentException("stage can't be null.");
        this.stage = stage;
    }

    @Override
    public String getDetailInfo() {
        return getSex().getPronoun() + " is " + course + "'th year "
                + stage + " student.";
    }
}
