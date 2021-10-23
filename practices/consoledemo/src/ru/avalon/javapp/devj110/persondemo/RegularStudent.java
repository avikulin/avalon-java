package ru.avalon.javapp.devj110.persondemo;

enum Stage{
    BACHELOR,
    MASTER
}

public class RegularStudent extends AbstractStudent{
    private int courseNumber;
    private Stage stage;

    public RegularStudent(String name, Sex sex, String dep, int courseNumber, Stage stage) {
        super(name, dep, sex);
        setCourseNumber(courseNumber);
        setStage(stage);
    }

    public void setCourseNumber(int courseNumber) {
        if ((courseNumber <0)||(courseNumber >6)){
            throw new IllegalArgumentException("Course ID must fit the interval 0..6");
        }
        this.courseNumber = courseNumber;
    }

    public void setStage(Stage stage) {
        if (stage==null){
            throw new IllegalArgumentException("Stage must not be null");
        }
        this.stage = stage;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public String getDetailedInfo() {
        String template = "%s is %s'th year %s student";
        String outputString = String.format(template, getSex().getPronounGenerative(), courseNumber, stage);
        return outputString;
    }
}
