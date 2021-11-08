package ru.avalon.devj110.listrepotests;

import ru.avalon.devj110.listdemo.enums.JustificationTypes;
import ru.avalon.devj110.listdemo.repository.ListRepository;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ListRepositoryCaptureTest {
    public static void main(String[] args) {
        try{
            {
                ListRepository<Integer> repositoryOne = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

                repositoryOne.pushFirst(30);
                repositoryOne.pushFirst(20);
                repositoryOne.pushFirst(10);
                repositoryOne.pushLast(40);
                repositoryOne.pushLast(50);
                repositoryOne.pushLast(60);

                ListRepository<Integer> repositoryTwo = new ListRepository<>();
                repositoryTwo.uptakeToHead(repositoryOne);
                String segmentStr = repositoryTwo.toString();
                assert segmentStr.equals("[10, 20, 30, 40, 50, 60]") : "Error in captured segments layout";
                assert repositoryOne.toString().equals("[]"): "Error in resetting source repository after capturing";
                System.out.println("#1 passed");
            }
            {
                ListRepository<Integer> repositoryOne = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

                repositoryOne.pushFirst(30);
                repositoryOne.pushFirst(20);
                repositoryOne.pushFirst(10);
                repositoryOne.pushLast(40);
                repositoryOne.pushLast(50);
                repositoryOne.pushLast(60);

                ListRepository<Integer> repositoryTwo = new ListRepository<>();
                repositoryTwo.uptakeToTail(repositoryOne);
                String segmentStr = repositoryTwo.toString();
                assert segmentStr.equals("[10, 20, 30, 40, 50, 60]") : "Error in captured segments layout";
                assert repositoryOne.toString().equals("[]"): "Error in resetting source repository after capturing";
                System.out.println("#2 passed");
            }
            {
                ListRepository<Integer> repositoryOne = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

                repositoryOne.pushFirst(30);
                repositoryOne.pushFirst(20);
                repositoryOne.pushFirst(10);
                repositoryOne.pushLast(40);
                repositoryOne.pushLast(50);
                repositoryOne.pushLast(60);

                ListRepository<Integer> repositoryTwo = new ListRepository<>();
                repositoryTwo.pushFirst(70);
                repositoryTwo.uptakeToHead(repositoryOne);
                String segmentStr = repositoryTwo.toString();
                assert segmentStr.equals("[10, 20, 30, 40, 50, 60, 70]") : "Error in captured segments layout";
                assert repositoryOne.toString().equals("[]"): "Error in resetting source repository after capturing";
                System.out.println("#3 passed");
            }
            {
                ListRepository<Integer> repositoryOne = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

                repositoryOne.pushFirst(30);
                repositoryOne.pushFirst(20);
                repositoryOne.pushFirst(10);
                repositoryOne.pushLast(40);
                repositoryOne.pushLast(50);
                repositoryOne.pushLast(60);

                ListRepository<Integer> repositoryTwo = new ListRepository<>();
                repositoryTwo.pushFirst(5);
                repositoryTwo.uptakeToTail(repositoryOne);
                String segmentStr = repositoryTwo.toString();
                assert segmentStr.equals("[5, 10, 20, 30, 40, 50, 60]") : "Error in captured segments layout";
                assert repositoryOne.toString().equals("[]"): "Error in resetting source repository after capturing";
                System.out.println("#4 passed");
            }
            {
                ListRepository<Integer> repositoryOne = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

                repositoryOne.pushFirst(30);
                repositoryOne.pushFirst(20);
                repositoryOne.pushFirst(10);
                repositoryOne.pushLast(40);
                repositoryOne.pushLast(50);
                repositoryOne.pushLast(60);

                ListRepository<Integer> repositoryTwo = new ListRepository<>();
                repositoryTwo.pushLast(70);
                repositoryTwo.uptakeToHead(repositoryOne);
                String segmentStr = repositoryTwo.toString();
                assert segmentStr.equals("[10, 20, 30, 40, 50, 60, 70]") : "Error in captured segments layout";
                assert repositoryOne.toString().equals("[]"): "Error in resetting source repository after capturing";
                System.out.println("#5 passed");
            }
            {
                ListRepository<Integer> repositoryOne = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);

                repositoryOne.pushFirst(30);
                repositoryOne.pushFirst(20);
                repositoryOne.pushFirst(10);
                repositoryOne.pushLast(40);
                repositoryOne.pushLast(50);
                repositoryOne.pushLast(60);

                ListRepository<Integer> repositoryTwo = new ListRepository<>();
                repositoryTwo.pushLast(5);
                repositoryTwo.uptakeToTail(repositoryOne);
                String segmentStr = repositoryTwo.toString();
                assert segmentStr.equals("[5, 10, 20, 30, 40, 50, 60]") : "Error in captured segments layout";
                assert repositoryOne.toString().equals("[]"): "Error in resetting source repository after capturing";
                System.out.println("#6 passed");
            }
            {
                try {
                    ListRepository<Integer> repository = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);
                    repository.uptakeToHead(null);
                    throw new AssertionError("Exception should be rise on uptakeToHead(..) the null-referenced repo");
                }catch (IllegalArgumentException iae){
                    System.out.println("#7 passed");
                }
            }
            {
                try {
                    ListRepository<Integer> repository = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);
                    repository.uptakeToTail(null);
                    throw new AssertionError("Exception should be rise on uptakeToTail(..) the null-referenced repo");
                }catch (IllegalArgumentException iae){
                    System.out.println("#8 passed");
                }
            }
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
