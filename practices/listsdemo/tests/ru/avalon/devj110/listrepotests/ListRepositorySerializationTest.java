package ru.avalon.devj110.listrepotests;

import ru.avalon.devj110.listdemo.enums.JustificationTypes;
import ru.avalon.devj110.listdemo.interfaces.ldsrepository.LinearDataSegmentsRepository;
import ru.avalon.devj110.listdemo.repository.ListRepository;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ListRepositorySerializationTest {
    public static void main(String[] args) {
        try{
            LinearDataSegmentsRepository<Integer> repository = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

            repository.pushFirst(30);
            repository.pushFirst(20);
            repository.pushFirst(10);
            repository.pushLast(40);
            repository.pushLast(50);
            repository.pushLast(60);

            String segmentStr = repository.toString();
            assert segmentStr.equals("[10, 20, 30, 40, 50, 60]") : "Error in forming memory dump";
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
