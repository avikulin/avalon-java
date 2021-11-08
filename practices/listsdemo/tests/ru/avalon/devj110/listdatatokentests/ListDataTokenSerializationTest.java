package ru.avalon.devj110.listdatatokentests;

import ru.avalon.devj110.listdemo.enums.JustificationTypes;
import ru.avalon.devj110.listdemo.repository.ListDataToken;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ListDataTokenSerializationTest {
    public static void main(String[] args) {
        try{
            ListDataToken<Integer> ldt = new ListDataToken<>(JustificationTypes.LEFT_TO_RIGHT);
            ldt.pushLast(20);
            ldt.pushLast(10);
            ldt.pushLast(20);
            ldt.pushLast(20);
            ldt.pushLast(30);
            ldt.pushLast(20);
            ldt.pushLast(20);
            ldt.pushLast(10);
            ldt.pushLast(20);

            ldt.removeElement(20);
            String segmentStr = ldt.toString();
            assert segmentStr.equals("10, 30, 10") : "Error in forming memory dump";
            System.out.println("#1 passed");
        } catch (RuntimeException|AssertionError errorInfo) { // ловим на уровне базовых классов и выводим
                                                              // развернутую информацию (вкл. стек вызовов).

            System.out.println(String.format("Exception! %s. \n\tStack trace:\n\t",
                    errorInfo.getMessage()));
            StringWriter stringWriter = new StringWriter();
            errorInfo.printStackTrace(new PrintWriter(stringWriter));
            System.out.println(stringWriter.toString());
        }
    }
}
