package ru.avalon.javapp.devj110.countrydemo;

public class Main {
    public static void main(String[] args) {

        Country[] countries = new Country[]{
                new Country("Russia", 17_100_000, 146_700_000, "Moscow", 12_600_000),
                new Country("Finland", 338_000, 5_500_000, "Helsinki", 655_000),
                new Country("France", 643_800, 67_800_000, "Paris", 2_100_00),
                new Country("Andorra", 467, 85_400, "Andorra la Vella", 22_600),
                new Country("Singapoore",725,5_500_000)
        };

        Country.printAll(countries);
    }

}
