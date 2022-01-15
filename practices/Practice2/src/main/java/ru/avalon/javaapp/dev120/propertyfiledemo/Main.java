package ru.avalon.javaapp.dev120.propertyfiledemo;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PropertyFile pf = new PropertyFile();

        pf.set("address","127.0.0.1");
        pf.set("port","12345");
        pf.set("user","testUser");

        System.out.println(pf.contains("login"));
        System.out.println(pf.contains("password"));
        System.out.println(pf.contains("address"));

        pf.save("test.props");
    }
}
