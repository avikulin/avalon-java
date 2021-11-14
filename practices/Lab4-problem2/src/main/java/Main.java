import repository.Repository;

import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        final int SIZE = 10;
        System.out.println(String.format("Creating the new repository (%d unset bits)", SIZE));

        Repository r = new Repository(SIZE);
        System.out.println("\nSetting the even indexes:");
        for (int i=0; i < SIZE; i++){
            if (i % 2 == 0){
                r.setElement(i);
            }
        }
        System.out.println(r);
        System.out.println(String.format("Count of set elements: %d", r.countTrueElements()));

        System.out.println("\nInverting all odd indexes:");
        for (int i=0; i < SIZE; i++){
            if (i % 2 == 1){
                r.invertElement(i);
            }
        }
        System.out.println(r);
        System.out.println(String.format("Count of set elements: %d", r.countTrueElements()));

        System.out.println("\nInserting \"False\" in all odd indexes:");
        for (int i=0; i < SIZE; i++){
            if (i % 2 == 1){
                r.put(i, false);
            }
        }
        System.out.println(r);
        System.out.println(String.format("Count of set elements: %d", r.countTrueElements()));

        System.out.println("\nUnset all the even elements:");
        for (int i=0; i < SIZE; i++){
            if (i % 2 == 0){
                r.unsetElement(i);
            }
        }
        System.out.println(r);
        System.out.println(String.format("Count of set elements: %d", r.countTrueElements()));

        System.out.println("\nSetting the first and last indexes:");
        r.setElement(0);
        r.setElement(SIZE - 1);
        System.out.println(String.format("Actual content of repository: %s", r));

        System.out.println("\n---end---");
    }
}
