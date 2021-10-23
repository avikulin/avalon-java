package ru.avalon.javapp.devj110.persondemo;

public class PhDStudent extends AbstractStudent{
    private String thesisTopic;
    public PhDStudent(String name, Sex sex, String dep, String thesisTopic) {
        super(name, dep, sex);
        setThesisTopic(thesisTopic);
    }

    public String getThesisTopic() {
        return thesisTopic;
    }

    public void setThesisTopic(String thesisTopic) {
        if (thesisTopic == null){
            throw new IllegalArgumentException("Thesis topic must not be null");
        }
        this.thesisTopic = thesisTopic;
    }

    @Override
    public String getDetailedInfo() {
        String template = "%s thesis title is \"%s\"";
        String outputString = String.format(template, getSex().getPronounGenerative(), thesisTopic);
        return outputString;
    }
}
