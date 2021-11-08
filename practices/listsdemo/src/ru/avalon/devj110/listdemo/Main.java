package ru.avalon.devj110.listdemo;

import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;
import ru.avalon.devj110.listdemo.repository.ListRepository;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ListRepository<Integer> repository = new ListRepository<>();
        System.out.println("New repo created...\n");

        System.out.println("Pushed \"10\" from the left.");
        repository.pushFirst(10);

        System.out.println("Pushed sequence \"[99,99,11,12,13,99]\" from the right.");
        repository.pushLast(new Integer[]{99, 99, 11, 12, 13, 99});

        System.out.println("Pushed \"14\" from the right.");
        repository.pushLast(14);

        System.out.println("Pushed sequence \"[7,99,99,99,8,9]\" from the left.");
        repository.pushFirst(new Integer[]{7, 99, 99, 99, 8, 9});

        System.out.println("\nContent list #1:");
        System.out.println(repository.toString());
        System.out.println();
        System.out.println(repository.getMemoryDump());

        System.out.println("\nSearching \"99\":");
        SearchResultsContainer res = repository.searchElement(99);

        System.out.println(String.format("\nIs value found? %s", !res.isEmpty()));
        System.out.println(String.format("Founded offsets: %s", res));

        System.out.println("\nDeleting element \"99\"...");
        repository.removeElement(99);

        System.out.println("\nSearching \"99\" again:");
        res = repository.searchElement(99);
        System.out.println(String.format("\nIs value found after deletion? %s", !res.isEmpty()));
        System.out.println(String.format("Founded offsets: %s", res));
        System.out.println("\nContent list #2:");
        System.out.println(repository.toString());
        System.out.println();
        System.out.println(repository.getMemoryDump());

        System.out.println("\nAppending sequence from both sides with content of the long dynamic collections...");
        List<Integer> l1 = new ArrayList<>();
        for (int i = -25; i < 7; i++) {
            l1.add((i < 0) ? 0 : i);
        }
        repository.pushFirst(l1);

        List<Integer> l2 = new ArrayList<>();
        for (int i = 15; i < 35; i++) {
            l2.add((i > 24) ? 0 : i);
        }
        repository.pushLast(l2);
        System.out.println("\nContent list #3:");
        System.out.println(repository.toString());
        System.out.println();
        System.out.println(repository.getMemoryDump());

        System.out.println("\nDeleting element \"99\" again...");
        repository.removeElement(99);
        System.out.println("\nContent list #4:");
        System.out.println(repository.toString());

        System.out.println("\nDeleting element \"0\"...");
        repository.removeElement(0);

        System.out.println("\nContent list #5:");
        System.out.println(repository.toString());
        System.out.println();
        System.out.println(repository.getMemoryDump());

        System.out.println("\nPerform storage area fast compaction (dropping empty segments)...");
        repository.performCompaction();

        System.out.println("\nContent list #6:");
        System.out.println(repository.toString());
        System.out.println();
        System.out.println(repository.getMemoryDump());

        System.out.println("\nPerform storage area normalization (elimination of the empty cells)...");
        repository.performNormalization();

        System.out.println("\nContent list #6:");
        System.out.println(repository.toString());
        System.out.println();
        System.out.println(repository.getMemoryDump());

        System.out.println("\nPerforming activity (System.out.println) per each element in repository");
        repository.mapAction(x->System.out.println(x));
        System.out.println("\ndone!");
    }

}
