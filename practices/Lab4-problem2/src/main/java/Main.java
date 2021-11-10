import repository.Repository;

import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        final int SIZE = 10;
        System.out.println("Creating the new repository");

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



        System.out.println("\nCreating string representation of content with \"forEach(..)\" function:");
        final String[] s = new String[1];
        s[0] = "";

        r.forEach((b)->{if(b) s[0] = s[0].concat("*"); else s[0] = s[0].concat("-"); });
        System.out.println(String.format("String representation: %s", s[0]));
        System.out.println(String.format("Actual content of repository: %s", r));
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

        System.out.println("\nIterating all the elements in array with pair-wise \"XOR\" operation:");
        StringJoiner log = new StringJoiner(" XOR ");
        boolean res = false;
        boolean initialState = true;
        for (boolean b: r){
            if (initialState){
                initialState = false;
                res = b;
            } else {
                res ^= b;
            }
            log = log.add(String.format("(%b)",b));
        }
        System.out.println(String.format("%s = %b", log.toString(), res));
        System.out.println(String.format("Actual content of repository: %s", r));

        System.out.println("\n---end---");
    }
}
