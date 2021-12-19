package ru.avalon.javaapp.dev120.onedirlist.demo;

public class Main {
    public static void main(String[] args) {
        OneDirList<String> lst = new OneDirList();
        lst.addToHead("111");
        lst.addToTail("222");
        lst.addToTail("333");
        lst.addToHead("AAA");

        for(String s: lst){
            System.out.println(s);
        }
        System.out.println("----");
        for(String s: lst.after("333")){
            System.out.println(s);
        }
        System.out.println("----");
        for(String s: lst.after("AAA")){
            System.out.println(s);
        }
        System.out.println("----");

    }
}
