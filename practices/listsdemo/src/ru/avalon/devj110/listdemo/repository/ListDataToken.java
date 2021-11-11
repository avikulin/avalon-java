package ru.avalon.devj110.listdemo.repository;

import ru.avalon.devj110.listdemo.dto.SegmentSearchResult;
import ru.avalon.devj110.listdemo.enums.JustificationTypes;
import ru.avalon.devj110.listdemo.interfaces.basedatastructures.LinearDataSegment;
import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;
import ru.avalon.devj110.listdemo.interfaces.general.Dumpable;
import ru.avalon.devj110.listdemo.interfaces.general.Serializable;

import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Consumer;

/*      Развертка структуры данных в памяти

        ┌headPointer (всегда перед читаемым элементом)
        |
        ▼
      (-1) [0] [1] [2] [3] [4] [5] [..]
        ▲          ▲
        |           |
        |           └tailPointer (всегда указывает на читаемый элемент)
        |
        └NULL_POINTER (константный указатель на индекс -1)
 */


public class ListDataToken<T> implements LinearDataSegment<T>, Iterable<T>, Dumpable, Serializable {
    private final Object[] data;
    private JustificationTypes justification;
    private LinearDataSegment<T> next;
    private int tailPointer;
    private int headPointer;
    private static final int SIZE = 10;
    public static final int NULL_POINTER = -1;

    public ListDataToken(JustificationTypes justification) {
        data = new Object[SIZE];
        next = null;
        this.justification = justification;
        resetPointers();
    }

    private void resetPointers() {
        if (justification == JustificationTypes.LEFT_TO_RIGHT) {
            headPointer = NULL_POINTER;
            tailPointer = NULL_POINTER;
        }
        if (justification == JustificationTypes.RIGHT_TO_LEFT) {
            headPointer = data.length - 1;
            tailPointer = data.length - 1;
        }
        if (justification == JustificationTypes.CENTER){
            int middle = data.length / 2;
            headPointer = middle;
            tailPointer = middle;
        }
    }

    public boolean isEmpty() {
        return (headPointer == tailPointer);
    }

    public boolean isFull() {
        return data.length == getLength();
    }

    public int getLength() {
        return tailPointer - headPointer;
    }

    public int getHeadPointer() {
        return headPointer;
    }

    public int getTailPointer() {
        return tailPointer;
    }

    public int getFreeCellsLeft() {
        return headPointer + 1;
    }

    public int getFreeCellsRight() {
        return data.length - tailPointer - 1;
    }

    public void pushFirst(T value) throws IllegalArgumentException, IllegalStateException {
        if (value == null) {
            throw new IllegalArgumentException("Value should be not-null");
        }

        if (getFreeCellsLeft() == 0) {
            throw new IllegalStateException("Appending the beyond the left bound");
        }
        data[headPointer] = value;
        headPointer--;
    }

    public void pushLast(T value) throws IllegalArgumentException, IllegalStateException {
        if (value == null) {
            throw new IllegalArgumentException("Value should be not-null");
        }

        if (getFreeCellsRight() == 0) {
            throw new IllegalStateException("Appending the beyond the right bound");
        }
        tailPointer++;
        data[tailPointer] = value;
    }

    public T peekFirst() throws IllegalStateException {
        if (this.isEmpty()) {
            throw new IllegalStateException("Trying to read from an empty storage");
        }
        // рекурсивно пропускаем логически удаленные элементы
        if (data[headPointer + 1] == null) {
            headPointer++;
            return peekFirst();
        }
        return (T) data[headPointer + 1];
    }


    public T peekLast() throws IllegalStateException {
        if (this.isEmpty()) {
            throw new IllegalStateException("Trying to read from an empty storage");
        }
        // рекурсивно пропускаем логически удаленные элементы
        if (data[tailPointer] == null) {
            tailPointer--;
            return peekLast();
        }
        return (T) data[tailPointer];
    }


    public T popFirst() throws IllegalStateException{
        T value = peekFirst();
        data[headPointer + 1] = null;
        headPointer++;
        return value;
    }

    public T popLast() throws IllegalStateException{
        T value = peekLast();
        data[tailPointer] = null;
        tailPointer--;
        return (T) value;
    }

    public void clearData() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        resetPointers();
    }

    public void fill(T[] values) throws IllegalArgumentException {
        if ((values == null) || (values.length == 0)) {
            throw new IllegalArgumentException("Values should be not-null & non-empty sequence");
        }

        if (this.data.length < values.length) {
            throw new IllegalArgumentException("Not enough free cells to write the values");
        }

        resetPointers();
        if (justification == JustificationTypes.LEFT_TO_RIGHT) {
            for (int i = 0; i < values.length; i++) {
                this.pushLast(values[i]);
            }

            for (int i = values.length; i < data.length; i++) {
                data[i] = null;
            }
        }
        if (justification == JustificationTypes.RIGHT_TO_LEFT) {
            for (int i = values.length - 1; i > -1; i--) {
                this.pushFirst(values[i]);
            }

            for (int i = data.length - values.length - 1; i > -1; i--) {
                data[i] = null;
            }
        }
    }

    public SearchResultsContainer searchElement(T element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Searched element must not be null-reference");
        }

        if (isEmpty()) {
            return new SegmentSearchResult(data.length);
        }

        SearchResultsContainer res = new SegmentSearchResult(data.length);

        for (int i = headPointer + 1; i <= tailPointer; i++) {
            T lvalue = (T) data[i];
            if ((lvalue != null) && (lvalue.equals(element))) {
                res.append(i);
            }
        }
        return res;
    }

    public void removeElement(T element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Element-to-remove param must not be null-reference");
        }

        if (isEmpty()) {
            return;
        }

        /* Используем логическое удаление. Так как у нас контейнер ссылочных nullable-типов, то ссылку на удаленный
           элемент заменяем на null. Никакое добавленное пользователем значение не может быть равно null в соответствии
           с правилами контроля входных значений, установленных в методах pushFirst(..) и pushLast(..).
         */
        SearchResultsContainer foundPositions = searchElement(element);
        if (foundPositions.isEmpty()) {
            return;
        }

        Iterable<Integer> iterableObj = (Iterable<Integer>)foundPositions;
        for (int pos : iterableObj) {
            data[pos] = null;
            //сдвигаем левый указатель до первого неудаленного элемента
            if ((!isEmpty()) && (pos == headPointer + 1)) {
                headPointer++;
            }
        }

        //сдвигаем правый указатель до первого неудаленного элемента
        while (!isEmpty()&&(data[tailPointer]==null)&&(tailPointer > headPointer)){
            tailPointer--;
        }
    }

    @Override
    public void mapAction(Consumer<T> action) throws IllegalArgumentException {
        if (action == null){
            throw new IllegalArgumentException("Action reference param must be not-null");
        }
        forEach(action);
    }

    public void setNextSegment(LinearDataSegment<T> obj) throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("Next segment reference must not be null");
        }
        this.next = obj;
    }

    @Override
    public void clearNextSegment() {
        this.next = null;
    }

    public LinearDataSegment<T> getNextSegment() {
        return next;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int currentPosition = headPointer + 1;

            @Override
            public boolean hasNext() {
                return currentPosition <= tailPointer;
            }

            @Override
            public T next() {
                Object value = data[currentPosition];
                currentPosition++;

                //рекурсивно пропускаем ячейки с удаленными элементы
                if (value==null){
                    value = next();
                }
                return (T) value;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T element : this) {
            action.accept(element);
        }
    }

    @Override
    public String getMemoryDump() {
        StringJoiner dumpAssembler = new StringJoiner(",", "[", "]");
        for (int i = 0; i < data.length; i++) {
            T v = (T) data[i];
            if (v != null) {
                dumpAssembler.add(v.toString());
            } else {
                dumpAssembler.add("▨");
            }
        }
        return dumpAssembler.toString();
    }

    @Override
    public String toString() {
        StringJoiner assemblingObj = new StringJoiner(", ");
        for (int i = headPointer + 1; i <= tailPointer; i++) {
            if (data[i] != null) {
                assemblingObj.add(data[i].toString());
            }
        }
        return assemblingObj.toString();
    }
}
