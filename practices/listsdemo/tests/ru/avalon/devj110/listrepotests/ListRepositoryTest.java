package ru.avalon.devj110.listrepotests;

import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;
import ru.avalon.devj110.listdemo.repository.ListRepository;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ListRepositoryTest {
    public static void main(String[] args) {
        /*
         * Без JUnit-а приходится делать на скорую руку. Через Ant он заводится криво, так как приходится настраивать
         * зависимости локально - в таком виде проект у вас сходу не соберется.
         * Можно ли заменить Ant на Maven в следующих лаб. работах ?
         */
        try {
            ListRepository<Integer> repository = new ListRepository<>();

            assert !repository.isEmpty(): "Wrong determination of empty status in isEmpty() method";
            assert repository.getNumberOfSegments() == 1: "Wrong value of number of segments initialized";
            System.out.println("#1 passed");

            try {
                repository.peekFirst();
                throw new AssertionError("Exception must be thrown on reading empty repo in peekFirst(..)");
            }catch (IllegalStateException ise){
                System.out.println("#2 passed");
            }

            try {
                repository.popFirst();
                throw new AssertionError("Exception must be thrown on reading empty repo in popFirst(..)");
            }catch (IllegalStateException ise){
                System.out.println("#3 passed");
            }

            try {
                repository.peekLast();
                throw new AssertionError("Exception must be thrown on reading empty repo in peekLast(..)");
            }catch (IllegalStateException ise){
                System.out.println("#4 passed");
            }

            try {
                repository.popLast();
                throw new AssertionError("Exception must be thrown on reading empty repo in popLast(..)");
            }catch (IllegalStateException ise){
                System.out.println("#5 passed");
            }

            try {
                Integer v = null;
                repository.pushFirst(v);
                throw new AssertionError("Exception must be thrown on passing null value in pushFirst(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#6 passed");
            }

            try {
                Integer v = null;
                repository.pushLast(v);
                throw new AssertionError("Exception must be thrown on passing null value in pushLast(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#7 passed");
            }

            try {
                Integer[] v = null;
                repository.pushFirst(v);
                throw new AssertionError("Exception must be thrown on passing null-array in pushFirst(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#8 passed");
            }

            try {
                Integer v[] = null;
                repository.pushLast(v);
                throw new AssertionError("Exception must be thrown on passing null-array in pushLast(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#9 passed");
            }

            try {
                Integer[] v = new Integer[]{};
                repository.pushFirst(v);
                throw new AssertionError("Exception must be thrown on passing empty array in pushFirst(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#10 passed");
            }

            try {
                Integer[] v = new Integer[]{};
                repository.pushLast(v);
                throw new AssertionError("Exception must be thrown on passing empty array in pushLast(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#11 passed");
            }

            try {
                Iterable<Integer> v = null;
                repository.pushFirst(v);
                throw new AssertionError("Exception must be thrown on passing null-reference to iterable " +
                                                     "collection in pushFirst(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#12 passed");
            }

            try {
                Iterable<Integer> v = null;
                repository.pushLast(v);
                throw new AssertionError("Exception must be thrown on passing null-reference to iterable " +
                                                     "collection in pushLast(..)");
            }catch (IllegalArgumentException iae){
                System.out.println("#13 passed");
            }

            repository.pushFirst(10);
            assert repository.getNumberOfSegments() == 1: "Wrong segment layout after pushFirst() on empty repo";
            System.out.println("#15 passed");

            assert repository.peekFirst() == 10: "Wrong result on peekFirst(..) the single-element sequence";
            assert repository.peekLast() == 10: "Wrong result on peekFirst(..) the single-element sequence";
            System.out.println("#16 passed");

            assert repository.popLast() == 10: "Wrong result on popLast(..) the single-element sequence";
            System.out.println("#17 passed");

            assert repository.toString().equals("[]"): "Wrong serialization of empry collection";
            System.out.println("#18 passed");

            repository.pushLast(20);
            assert repository.getNumberOfSegments() == 1: "Wrong segment layout after pushLast() on empty repo";
            System.out.println("#19 passed");


            assert repository.peekFirst() == 20: "Wrong result on peekFirst(..) the single-element sequence";
            assert repository.peekLast() == 20: "Wrong result on peekFirst(..) the single-element sequence";
            System.out.println("#20 passed");

            assert repository.popFirst() == 20: "Wrong result on popFirst(..) the single-element sequence";
            System.out.println("#21 passed");

            repository.pushLast(new Integer[]{10, 99, 99, 11, 12, 13, 99});
            assert repository.getNumberOfSegments() == 2: "Wrong segment layout after pushLast() array in empty repo";
            assert repository.toString().equals("[10, 99, 99, 11, 12, 13, 99]"):"Wrong data layout after pushLast() " +
                                                                                "array in empty repo";
            System.out.println("#21 passed");

            repository.pushLast(14);
            assert repository.getNumberOfSegments() == 2: "Wrong segment layout after single value pushLast() in " +
                                                          "non-empty repo";
            assert repository.toString().equals("[10, 99, 99, 11, 12, 13, 99, 14]"):"Wrong data layout after pushLast() " +
                    "single value in non-empty repo";
            System.out.println("#22 passed");


            repository.pushFirst(new Integer[]{7, 99, 99, 99, 8, 9});
            assert repository.getNumberOfSegments() == 2: "Wrong segment layout after array pushFirst() in " +
                                                          "non-empty repo";
            assert repository.toString().equals("[7, 99, 99, 99, 8, 9, 10, 99, 99, 11, 12, 13, 99, 14]"):"Wrong data layout " +
                                                                        "after pushLast() single value in non-empty repo";
            System.out.println("#23 passed");

            SearchResultsContainer res = repository.searchElement(99);
            assert !res.isEmpty(): "Wrong behaviour on search existing elements";
            assert res.toString().equals("[[1, 2, 3, 7, 8], [2]]"): "Wrong behaviour on search existing elements";
            System.out.println("#24 passed");

            repository.removeElement(99);
            SearchResultsContainer res2 = repository.searchElement(99);
            assert res2.isEmpty(): "Wrong behaviour on search element which is not present in the sequence";
            assert res2.toString().equals("[]"): "Wrong behaviour on search not-present element";
            System.out.println("#25 passed");

            repository.removeElement(99);
            assert repository.getNumberOfSegments() == 2: "Wrong segment layout after repeated deletion of non-present " +
                                                          "element in the non-empty repo";
            assert repository.toString().equals("[7, 8, 9, 10, 11, 12, 13, 14]"):"Wrong data layout " +
                    "after repeated deletion of non-present element in non-empty repo";
            System.out.println("#26 passed");

            List<Integer> l1 = new ArrayList<>();
            for (int i = -25; i < 7; i++) {
                l1.add((i < 0) ? 0 : i);
            }
            repository.pushFirst(l1);
            assert repository.getNumberOfSegments() == 6: "Wrong segment layout after collection pushFirst() in " +
                    "non-empty repo";
            assert repository.toString().equals("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, " +
                                                "0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]"):
                                                "Wrong data layout after pushFirst() long-sequenced " +
                                                "collection in non-empty repo";

            System.out.println("#27 passed");

            List<Integer> l2 = new ArrayList<>();
            for (int i = 15; i < 35; i++) {
                l2.add((i > 24) ? 0 : i);
            }
            repository.pushLast(l2);

            assert repository.getNumberOfSegments() == 9: "Wrong segment layout after collection pushFirst() in " +
                    "non-empty repo";
            assert repository.toString().equals("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, " +
                    "0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, " +
                    "24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]"): "Wrong data layout after pushLast() long-sequenced collection " +
                    "in non-empty repo";
            System.out.println("#28 passed");

            repository.removeElement(0);
            assert repository.getNumberOfSegments() == 9: "Wrong segment layout after deletion repeated element in first" +
                    "and last segments of the non-empty repo";
            assert repository.toString().equals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, " +
                    "19, 20, 21, 22, 23, 24]"): "Wrong data layout after deletion repeated element in first " +
                                                "and last segments of the non-empty repo";

            System.out.println("#29 passed");

            repository.performCompaction();
            assert repository.getNumberOfSegments() == 5: "Wrong segment layout after compaction";
            assert repository.toString().equals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, " +
                    "19, 20, 21, 22, 23, 24]"): "Wrong data layout after compaction";
            System.out.println("#30 passed");

            repository.performNormalization();
            assert repository.getNumberOfSegments() == 3: "Wrong segment layout after normalization";
            assert repository.toString().equals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, " +
                    "19, 20, 21, 22, 23, 24]"): "Wrong data layout after normalization";
            System.out.println("#31 passed");
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
