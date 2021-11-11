package ru.avalon.devj110.listdatatokentests;

import ru.avalon.devj110.listdemo.enums.JustificationTypes;
import ru.avalon.devj110.listdemo.repository.ListDataToken;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ListDataTokenRightToLeftTests {
    public static void main(String[] args) {
        /*
         * Без JUnit-а приходится делать на скорую руку. Через Ant он заводится криво, так как приходится настраивать
         * зависимости локально - в таком виде проект у вас сходу не соберется.
         * Можно ли заменить Ant на Maven в следующих лаб. работах ?
         */
        try {
            ListDataToken<Integer> ldt = new ListDataToken<>(JustificationTypes.RIGHT_TO_LEFT);
            assert ldt.isEmpty() : "New token should be empty";
            System.out.println("#1 passed");

            assert ldt.getLength() == 0 : "Length of the new token should be 0";
            System.out.println("#2 passed");

            assert ldt.getTailPointer() == 9 : "Tail pointer of the new token should be 9";
            assert ldt.getHeadPointer() == 9 : "Head pointer of the new token should be 9";
            System.out.println("#3 passed");

            try {
                ldt.peekLast();
                throw new AssertionError("Should rise IllegalStateException on reading the empty list");
            } catch (IllegalStateException exception) {
                System.out.println("#4 passed");
            }

            try {
                ldt.popLast();
                throw new AssertionError("Should rise IllegalStateException on popping from the empty list");
            } catch (IllegalStateException exception) {
                System.out.println("#5 passed");
            }

            try {
                ldt.pushLast(null);
                throw new AssertionError("Exception should be rised on appending null-value");
            } catch (InvalidStateException | IllegalArgumentException exception) {
                System.out.println("#6 passed");
            }

            try {
                ldt.pushLast(0);
                throw new AssertionError("Exception should be rise on appending right");
            } catch (IllegalStateException | IllegalArgumentException exception) {
                System.out.println("#7 passed");
            }

            assert ldt.getLength() == 0 : "Wrong length calculation";
            System.out.println("#8 passed");

            for (int i = 0; i < 10; i++) {
                ldt.pushFirst(i);
            }
            System.out.println("#9 passed");

            assert (ldt.getTailPointer() == 9) && (ldt.getHeadPointer() == -1) :
                    "getHeadPointer() and getTailPointer returns wrong result full popping from left";
            System.out.println("#10 passed");

            assert ldt.getLength() == 10 : "getLength() returns wrong result after full pushFirst";
            System.out.println("#11 passed");

            try {
                ldt.pushFirst(11);
                ;
                throw new AssertionError("Should rise IllegalStateException after pushing left the full sequence");
            } catch (IllegalStateException exception) {
                System.out.println("#12 passed");
            }

            for (int i = 0; i < 10; i++) {
                int v3 = ldt.popLast();
                assert v3 == i : String.format("Illegal state in reading from right values, pushed from left:" +
                        "popped value: %d, expected value: %d", v3, i);
            }
            System.out.println("#12 passed");

            assert ldt.isEmpty() : "IsEmpty() returns wrong result after full popping from right values pushed from left";
            System.out.println("#13 passed");

            assert (ldt.getTailPointer() == -1) && (ldt.getHeadPointer() == -1) :
                    "getHeadPointer() and getTailPointer returns wrong result full popping from right values " +
                            "pushed from left";
            System.out.println("#14 passed");

            assert ldt.getLength() == 0 : "getLength() returns wrong result after full popping from right values " +
                    "pushed from left";
            System.out.println("#15 passed");

            try {
                ldt.popLast();
                throw new AssertionError("Should rise IllegalStateException after popping empty sequence");
            } catch (IllegalStateException exception) {
                System.out.println("#16 passed");
            }

            ldt.fill(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            for (int i = 0; i < 10; i++) {
                int v2 = ldt.popFirst();
                assert v2 == i + 1 : String.format("Wrong order on left popping from the sequence. " +
                        "Popped value: %d, expected value: %d", v2, i + 1);
            }
            System.out.println("#18 passed");

            assert ldt.isEmpty() : "IsEmpty() returns wrong result after full popping from left";
            System.out.println("#19 passed");

            assert (ldt.getTailPointer() == 9) && (ldt.getHeadPointer() == 9) :
                    "getHeadPointer() and getTailPointer  returns wrong result full popping from left";
            System.out.println("#20 passed");

            try {
                ldt.peekFirst();
                throw new AssertionError("Should rise IllegalStateException on reading the empty sequence");
            } catch (IllegalStateException exception) {
                System.out.println("#21 passed");
            }

            try {
                ldt.popFirst();
                throw new AssertionError("Should rise IllegalStateException on reading the empty sequence");
            } catch (IllegalStateException exception) {
                System.out.println("#22 passed");
            }

            assert ldt.getLength() == 0 : "getLength() returns wrong result after full popping from left";
            System.out.println("#23 passed");

            try {
                ldt.pushFirst(null);
                throw new AssertionError("Exception should be rise on appending null-value from left");
            } catch (InvalidStateException | IllegalArgumentException exception) {
                System.out.println("#24 passed");
            }

            ldt.pushFirst(9);
            assert !ldt.isEmpty() : "IsEmpty() returns wrong result after full popping from left";
            System.out.println("#25 passed");

            assert ldt.peekFirst() == ldt.peekLast() : "peekFirst() and peekLast()should be equal on one-element sequence";
            System.out.println("#26 passed");

            try {
                for (int i = 8; i > -1; i--) {
                    ldt.pushFirst(i);
                    assert ldt.getHeadPointer() == i - 1 : String.format("Wrong position return: item# - %d," +
                            " returned pos - %d", i, ldt.getTailPointer());
                }
                System.out.println("#27 passed");
            } catch (IllegalStateException exception) {
                throw new AssertionError("Runtime error on appending the sequence");
            }

            try {
                ldt.pushFirst(0);
                throw new AssertionError("Should rise IllegalStateException on appending the full list");
            } catch (IllegalStateException exception) {
                System.out.println("#28 passed");
            }

            int v = ldt.popLast();
            assert v == 9 : String.format("Wrong value returned (%d)", v);
            System.out.println("#29 passed");
            try {
                for (int i = 8; i > -1; i--) {
                    assert ldt.getTailPointer() == i : String.format("Wrong position return: item# - %d," +
                            " returned pos - %d", i, ldt.getTailPointer());
                    ldt.popLast();
                }
                System.out.println("#30 passed");
            } catch (IllegalStateException exception) {
                throw new AssertionError("Runtime error on popping values form the sequence");
            }

            assert ldt.isEmpty() : "Wrong return from \"isEmpty()\" on empty sequence";
            System.out.println("#31 passed");

            assert ldt.getTailPointer() == -1 : "Wrong return from \"getPointer()\" on empty sequence";
            System.out.println("#32 passed");

            try {
                ldt.popLast();
                throw new AssertionError("Should rise IllegalStateException on popping from the empty list");
            } catch (IllegalStateException exception) {
                System.out.println("#33 passed");
            }

            try {
                ldt.fill(null);
                throw new AssertionError("Should rise exception on filling with null-array");
            } catch (IllegalArgumentException exception) {
                System.out.println("#34 passed");
            }

            try {
                ldt.fill(new Integer[]{});
                throw new AssertionError("Should rise exception on filling with empty array");
            } catch (IllegalArgumentException exception) {
                System.out.println("#35 passed");
            }

            try {
                ldt.fill(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
                throw new AssertionError("Should rise exception on filling with too large array");
            } catch (IllegalArgumentException exception) {
                System.out.println("#35 passed");
            }

            ldt.fill(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            assert ldt.isFull() : "IsFull() returns wrong result after fill().";
            System.out.println("#36 passed");

            assert !ldt.isEmpty() : "IsEmpty() returns wrong result after fill().";
            System.out.println("#37 passed");

            assert ldt.getTailPointer() == 9 : "getPointer() returns wrong result after fill().";
            assert ldt.getHeadPointer() == -1 : "getPointer() returns wrong result after fill().";
            System.out.println("#38 passed");

            assert ldt.getLength() == 10 : "getLength() returns wrong result after fill().";
            System.out.println("#39 passed");

            for (int i = 9; i > -1; i--) {
                int v2 = ldt.popLast();
                assert v2 == i + 1 : String.format("Wrong order on popping from the sequence. " +
                        "Popped value: %d, expected value: %d", v2, i);
            }
            System.out.println("#40 passed");

            assert !ldt.isFull() : "IsFull() return wrong result after popping all elements.";
            System.out.println("#41 passed");

            assert ldt.isEmpty() : String.format("IsEmpty() return wrong result after popping all elements. " +
                    "Position in sequence: %d, sequence length: %d", ldt.getTailPointer(), ldt.getLength());
            System.out.println("#42 passed");

            assert ldt.getTailPointer() == -1 : "getTailPointer() return wrong result popping all elements.";
            assert ldt.getHeadPointer() == -1 : "getHeadPointer() return wrong result popping all elements.";
            System.out.println("#43 passed");

            try {
                ldt.peekLast();
                throw new AssertionError("Should rise IllegalStateException on reading the sequence " +
                        "after full popping");
            } catch (IllegalStateException exception) {
                System.out.println("#44 passed");
            }

            try {
                ldt.popLast();
                throw new AssertionError("Should rise IllegalStateException after popping empty sequence");
            } catch (IllegalStateException exception) {
                System.out.println("#45 passed");
            }

            try {
                ldt.searchElement(null);
                throw new AssertionError("Should rise IllegalArgumentException on searching " +
                        "the null-reference parameter");
            } catch (IllegalArgumentException iae) {
                System.out.println("#44 passed");
            }

            assert ldt.searchElement(10).isEmpty() : "Wrong behaviour on searching the empty sequence";
            System.out.println("#45 passed");

            ldt.pushLast(42);
            assert !ldt.searchElement(42).isEmpty() : "Wrong search behaviour";
            System.out.println("#46 passed");

            assert ldt.searchElement(24).isEmpty() : "Wrong search behaviour";
            System.out.println("#47 passed");

            try {
                ldt.removeElement(null);
                throw new AssertionError("Should rise IllegalArgumentException on removing " +
                        "the null-referenced element");
            } catch (IllegalArgumentException iae) {
                System.out.println("#48 passed");
            }

            ldt.removeElement(24);
            assert (ldt.getTailPointer() == 0) && (ldt.getHeadPointer() == -1) : "Error in deleting not-present element";
            assert (ldt.getLength() == 1) : "Error in deleting not-present element";
            assert !ldt.searchElement(42).isEmpty() : "Error in deleting not-present element";
            System.out.println("#49 passed");

            ldt.removeElement(42);
            assert ldt.searchElement(42).isEmpty() : "Error in deleting element behaviour";
            assert (ldt.getTailPointer() == 0) && (ldt.getHeadPointer() == 0) : "Error in resetting pointers after " +
                    "removing the element";
            assert ldt.getFreeCellsRight() == 9 : "Error in resetting pointers after removing the element";
            assert ldt.getFreeCellsLeft() == 1 : "Error in resetting pointers after removing the element";
            System.out.println("#50 passed");

            // после удаления элемента 42 в позиции 0, начинаем наполнять последовательности с индекса 1
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
            assert (ldt.getTailPointer() == 8) && (ldt.getHeadPointer() == 1) : "Error in setting pointers after removing the repeating element";
            assert ldt.getLength() == 7 : "Error calculating length() after removing the repeating element";
            assert ldt.getFreeCellsRight() == 1 : "Error in calculating getFreeCellsRight() after removing the repeating element";
            assert ldt.getFreeCellsLeft() == 2 : "Error in calculating getFreeCellsLeft() after removing the repeating element";
            System.out.println("#51 passed");

            assert ldt.popFirst() == 10 : "Error in popping left after removing the first element";
            System.out.println("#52 passed");

            assert ldt.peekFirst() == 30 : "Error in skipping the removed elements during the peekFirst(..) operation";
            System.out.println("#53 passed");

            assert ldt.popLast() == 10 : "Error in popping left after removing the last elements";
            System.out.println("#54 passed");

            assert ldt.peekLast() == 30 : "Error in skipping the removed elements during the peekLast(..) operation";
            System.out.println("#55 passed");

            ldt.removeElement(30);
            assert (ldt.getTailPointer() == 5) && (ldt.getHeadPointer() == 5) : "Error in removing the one remaining " +
                    "element in the whole sequence";
            assert ldt.getLength() == 0 : "Error in calculating getLength() after removing the one remaining " +
                    "element in the whole sequence";
            assert ldt.isEmpty() : "Error in calculating isEmpty() after removing the one remaining " +
                    "element in the whole sequence";
            assert ldt.getFreeCellsRight() == 4 : "Error in calculating getFreeCellsRight() after removing the  one " +
                    "remaining element in the whole sequence";
            assert ldt.getFreeCellsLeft() == 6 : "Error in calculating getFreeCellsLeft() after removing the  one " +
                    "remaining element in the whole sequence";
            System.out.println("#56 passed");

            ldt.fill(new Integer[]{1, 2, 3});
            assert !ldt.isEmpty() : "Error in fill(..) after removing the all the elements in sequence";
            assert ldt.getLength() == 3 : "Error in calculating getLength() after filling the sequence, which " +
                    "all the elements were removed";
            assert ldt.getFreeCellsRight() == 0 : "Error in calculating getFreeCellsRight() after filling the sequence, " +
                    "which all the elements were removed";
            assert ldt.getFreeCellsLeft() == 7 : "Error in calculating getFreeCellsLeft() after filling the sequence, " +
                    "which all the elements were removed";
            System.out.println("#57 passed");

            ldt.clearData();
            assert ldt.isEmpty() : "Error in clearData(..)";
            assert ldt.getLength() == 0 : "Error in calculating getLength() after clearing the sequence data";
            assert ldt.getFreeCellsRight() == 0 : "Error in calculating getFreeCellsRight() after clearing the " +
                    "sequence data";
            assert ldt.getFreeCellsLeft() == 10 : "Error in calculating getFreeCellsLeft() after clearing the sequence data";
            System.out.println("#58 passed");

        } catch (RuntimeException | AssertionError errorInfo) { // ловим на уровне базовых классов и выводим
            // развернутую информацию (вкл. стек вызовов).

            System.out.println(String.format("Exception! %s. \n\tStack trace:\n\t",
                    errorInfo.getMessage()));
            StringWriter stringWriter = new StringWriter();
            errorInfo.printStackTrace(new PrintWriter(stringWriter));
            System.out.println(stringWriter.toString());
        }
    }
}
