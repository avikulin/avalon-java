package ru.avalon.javapp.devj110.countrydemo;

public class Country {
    private String name;
    private int square;
    private int population;
    private String capitalName;

    public String getName() {
        return name;
    }

    public int getSquare() {
        return square;
    }

    public int getPopulation() {
        return population;
    }

    public String getCapitalName() {
        return capitalName;
    }

    public int getCapitalPopulation() {
        return capitalPopulation;
    }

    private int capitalPopulation;

    public Country(String name, int square, int population) {
        setName(name);
        setSquare(square);
        setPopulation(population);
    }

    public Country(String name, int square, int population, String capitalName, int capitalPopulation) {
        this(name,square,population);
        this.capitalName = capitalName;
        this.capitalPopulation = capitalPopulation;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Value must be not null.");
        this.name = name;
    }

    public void setSquare(int square) {
        if (square <=0)
            throw new IllegalArgumentException("Value must be over zero.");
        this.square = square;
    }

    public void setPopulation(int population) {
        if (population <=0)
            throw new IllegalArgumentException("Value must be over zero.");
        this.population = population;
    }

    public void setCapital(String capitalName, int capitalPopulation) {
        if (capitalName !=null){
            if (capitalPopulation <=0){
                throw new IllegalArgumentException("Value must be over zero.");
            } else {
                this.population = 0;
            }
        }
        this.capitalName = capitalName;
        this.population = capitalPopulation;
    }

    public float getPopulationDensity(){
        return (float) this.population / this.square;
    }

    public void print(){
        String s= "Country{" +
                "name='" + name + '\'' +
                ", square=" + square +
                ", population=" + population +
                ((capitalName!=null)? ", capitalName='" + capitalName:"") +
                ((capitalPopulation>0)? ", capitalPopulation=" + capitalPopulation:"") +
                ", density = " + getPopulationDensity()+
                '}';
        System.out.println(s);
    }

    public static void printAll(Country[] countries){
        for (Country c:countries){
            c.print();
        }
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", square=" + square +
                ", population=" + population +
                ", capitalName='" + capitalName + '\'' +
                ", capitalPopulation=" + capitalPopulation +
                ", density = " + getPopulationDensity()+
                '}';
    }
}
