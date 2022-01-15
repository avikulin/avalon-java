package ru.avalon.javaapp.dev120.phonenum.demo;

import ru.avalon.javaapp.dev120.phonenum.demo.models.PhoneNumber;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        PhoneNumber pn = new PhoneNumber("981", "0234567");
        PhoneNumber pn1 = new PhoneNumber("981", "0234567");
        String s = String.format("My phone number is %s", pn);
        System.out.println(s);

        Set<PhoneNumber> set = new HashSet<>();
        set.add(pn);
        set.add(pn1);

        for(PhoneNumber p: set){
            System.out.println(p);
        }
    }
}
