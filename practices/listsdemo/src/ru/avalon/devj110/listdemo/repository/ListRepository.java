package ru.avalon.devj110.listdemo.repository;

import ru.avalon.devj110.listdemo.dto.RepositorySearchResult;
import ru.avalon.devj110.listdemo.dto.SegmentSearchResult;
import ru.avalon.devj110.listdemo.enums.JustificationTypes;
import ru.avalon.devj110.listdemo.interfaces.general.Dumpable;
import ru.avalon.devj110.listdemo.interfaces.general.Serializable;
import ru.avalon.devj110.listdemo.interfaces.ldsrepository.Capturable;
import ru.avalon.devj110.listdemo.interfaces.basedatastructures.LinearDataSegment;
import ru.avalon.devj110.listdemo.interfaces.ldsrepository.LinearDataSegmentsRepository;
import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;

import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Consumer;


public class ListRepository<T> implements LinearDataSegmentsRepository<T>, Iterable<LinearDataSegment<T>>,
        Capturable<ListRepository<T>>, Dumpable, Serializable {
    private LinearDataSegment<T> head;
    private LinearDataSegment<T> tail;
    private int numberOfSegments;

    public ListRepository() {
        resetRepository(JustificationTypes.CENTER);
    }

    public ListRepository(JustificationTypes justificationType) {
        resetRepository(justificationType);
    }

    private void resetRepository(JustificationTypes justificationType){
        if ((head!=null)&&(tail!=null)) {
            // чтобы не текла память, последовательно зачищаем ссылки сегментов друг на друга, если они есть.
            LinearDataSegment<T> prev = null;
            for (LinearDataSegment<T> lds : this) {
                if (prev!=null){
                    prev.clearNextSegment();
                }
                prev = lds;
            }
        }
        head = new ListDataToken<>(justificationType);
        tail = head;
        numberOfSegments = 1;
    }

    private void addSegmentBeforeHead() {
        ListDataToken<T> segment = new ListDataToken<>(JustificationTypes.RIGHT_TO_LEFT);
        segment.setNextSegment(head);
        head = segment;
        if (tail == null) {
            tail = segment;
        }
        numberOfSegments++;
    }

    private void addSegmentAfterTail() {
        ListDataToken<T> segment = new ListDataToken<>(JustificationTypes.LEFT_TO_RIGHT);
        tail.setNextSegment(segment);
        tail = segment;
        if (head == null) {
            head = segment;
        }
        numberOfSegments++;
    }

    @Override
    public int getNumberOfSegments(){return numberOfSegments;}

    @Override
    public void append(Iterable<T> collection) throws IllegalArgumentException {
        if (collection==null){
            throw new IllegalArgumentException("Collection reference must be not-null");
        }

        for(T v: collection) {
            pushLast(v);
        }
    }

    @Override
    public void pushFirst(T value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Value param must not be null");
        }

        if ((head == null) || (head.getFreeCellsLeft() == 0)) {
            addSegmentBeforeHead();
        }
        head.pushFirst(value);
    }

    @Override
    public void pushFirst(T[] values) throws IllegalArgumentException {
        if ((values == null) || (values.length == 0)) {
            throw new IllegalArgumentException("Values must not be null not-empty array");
        }

        for (int i = values.length - 1; i > -1; i--) {
            pushFirst(values[i]);
        }
    }

    @Override
    public void pushLast(T value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Value param must not be null");
        }

        if ((tail==null)||(tail.getFreeCellsRight() == 0)) {
            addSegmentAfterTail();
        }
        tail.pushLast(value);
    }

    @Override
    public void pushLast(T[] values) throws IllegalArgumentException {
        if ((values == null) || (values.length == 0)) {
            throw new IllegalArgumentException("Values must not be null not-empty array");
        }

        for (int i = 0; i < values.length; i++) {
            pushLast(values[i]);
        }
    }


    @Override
    public void pushFirst(Iterable<T> collection) throws IllegalArgumentException {
        if (collection == null) {
            throw new IllegalArgumentException("Collection reference param must not be null");
        }

        ListRepository<T> tempRepo = new ListRepository<>();
        tempRepo.append(collection);
        this.uptakeToHead(tempRepo);
    }

    @Override
    public void pushLast(Iterable<T> collection) throws IllegalArgumentException {
        if (collection == null) {
            throw new IllegalArgumentException("Collection reference param must not be null");
        }

        ListRepository<T> tempRepo = new ListRepository<>();
        tempRepo.append(collection);
        this.uptakeToTail(tempRepo);
    }

    @Override
    public T peekFirst() throws IllegalStateException {
        return head.peekFirst();
    }

    @Override
    public T peekLast() throws IllegalStateException {
        return tail.peekLast();
    }

    @Override
    public T popFirst() throws IllegalStateException {
        return head.popFirst();
    }

    @Override
    public T popLast() throws IllegalStateException {
        return tail.popLast();
    }

    @Override
    public SearchResultsContainer searchElement(T element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Element value param must not be null");
        }
        SearchResultsContainer res = new RepositorySearchResult(numberOfSegments);
        for (LinearDataSegment<T> lds : this) {
            res.append(lds.searchElement(element));
        }
        return res;
    }

    @Override
    public void removeElement(T element) throws IllegalArgumentException{
        if (element == null) {
            throw new IllegalArgumentException("Element value param must not be null");
        }

        for (LinearDataSegment<T> segment : this) {
            segment.removeElement(element);
        }
    }

    @Override
    public boolean isEmpty() {
        return (numberOfSegments == 0);
    }

    @Override
    public void mapAction(Consumer<T> action) throws IllegalArgumentException {
        if (action == null){
            throw new IllegalArgumentException("Action reference param must be not-null");
        }
        for (LinearDataSegment<T> lds : this) {
            lds.mapAction(action);
        }
    }

    @Override
    public void performCompaction() {
        LinearDataSegment<T> prevSegment = null;
        for (LinearDataSegment<T> segment : this) {
            if (segment.getLength()==0){ //удаляем сегменты нулевой длины
                if((prevSegment==null)&&(segment.getNextSegment()!=null)){ // удаление из начала цепочки
                    head = segment.getNextSegment();
                    segment.clearNextSegment();
                    numberOfSegments--;
                    prevSegment = null;
                }

                if((prevSegment!=null)&&(segment.getNextSegment()!=null)) { // удаление из середины цепочки
                    prevSegment.setNextSegment(segment.getNextSegment());
                    segment.clearNextSegment();
                    numberOfSegments--;
                    prevSegment = segment;
                }

                if((prevSegment!=null)&&(segment.getNextSegment()==null)){ // удаление из конца цепочки
                    prevSegment.clearNextSegment();
                    tail = prevSegment;
                    numberOfSegments--;
                    prevSegment = segment;
                }
            } else {
                prevSegment = segment;
            }
        }
    }

    @Override
    public void performNormalization() {
        performCompaction();
        ListRepository<T> resultingRepo = new ListRepository<>(JustificationTypes.LEFT_TO_RIGHT);
        this.mapAction(resultingRepo::pushLast);
        this.resetRepository(JustificationTypes.LEFT_TO_RIGHT);
        this.uptakeToTail(resultingRepo);

        //удаляем первый пустой сегмент
        LinearDataSegment<T> firstSegment= head;
        head = head.getNextSegment();
        firstSegment.clearNextSegment();
        numberOfSegments--;
    }

    @Override
    public Iterator<LinearDataSegment<T>> iterator() {
        return new Iterator<LinearDataSegment<T>>() {
            LinearDataSegment<T> currentSegment = null;
            LinearDataSegment<T> nextSegment = head;

            @Override
            public boolean hasNext() {
                return nextSegment != null;
            }

            @Override
            public LinearDataSegment<T> next() {
                currentSegment = nextSegment;
                nextSegment = currentSegment.getNextSegment();
                LinearDataSegment<T> segment = currentSegment;
                return currentSegment;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super LinearDataSegment<T>> action) {
        for (LinearDataSegment<T> lds : this) {
            action.accept(lds);
        }
    }

    @Override
    public String getMemoryDump() {
        if (isEmpty()) return "Memory dump is empty";

        StringJoiner res = new StringJoiner("\n");
        res.add(String.format("Memory dump (%d segments)", numberOfSegments));
        res.add("--------------------------------------");
        int numberOfSegments = 0;
        for (LinearDataSegment<T> lds : this) {
            numberOfSegments++;
            Dumpable dumpableObj = (Dumpable)lds;
            res.add(String.format("#%03d: %s", numberOfSegments, dumpableObj.getMemoryDump()));
        }
        return res.toString();
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        StringJoiner res = new StringJoiner(", ", "[", "]");
        for (LinearDataSegment<T> lds : this) {
            if(!lds.isEmpty()) {
                res.add(lds.toString());
            }
        }
        return res.toString();
    }

    @Override
    public void uptakeToHead(ListRepository<T> obj) throws IllegalArgumentException {
        if ((obj == null)||(obj.isEmpty())) {
            throw new IllegalArgumentException("Param must not be null reference to non-empty repo");
        }
        obj.tail.setNextSegment(head);
        head = obj.head;
        numberOfSegments +=obj.numberOfSegments;

        obj.head = null;
        obj.tail = null;
        obj.resetRepository(JustificationTypes.CENTER);
    }

    @Override
    public void uptakeToTail(ListRepository<T> obj) throws IllegalArgumentException {
        if ((obj == null)||(obj.isEmpty())) {
            throw new IllegalArgumentException("Param must not be null reference to non-empty repo");
        }
        tail.setNextSegment(obj.head);
        tail = obj.tail;
        numberOfSegments +=obj.numberOfSegments;

        obj.head = null;
        obj.tail = null;
        obj.resetRepository(JustificationTypes.CENTER);
    }
}
