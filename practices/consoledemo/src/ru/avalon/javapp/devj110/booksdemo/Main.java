package ru.avalon.javapp.devj110.booksdemo;

import ru.avalon.javapp.devj110.booksdemo.models.Book;
import ru.avalon.javapp.devj110.booksdemo.repositories.BooksRepo;
import ru.avalon.javapp.devj110.booksdemo.repositories.PublisherDictRepo;
import ru.avalon.javapp.devj110.booksdemo.utils.PrintHelper;

public class Main {
    public static void main(String[] args) {
        PublisherDictRepo publisherDictRepo = new PublisherDictRepo();
        BooksRepo booksRepo = new BooksRepo(5);

        booksRepo.addBook(new Book("Computer Science: основы программирования на Java, " +
                                         "ООП, алгоритмы и структуры данных",
                                         publisherDictRepo.getItemByIdx(1),
                              2018,
                                         new String[]{"Седжвик Роберт", "Уэйн Кевин"}));

        booksRepo.addBook(new Book("Разработка требований к программному обеспечению. 3-е издание, дополненное",
                                         publisherDictRepo.getItemByIdx(2),
                                  "Вигерс Карл",
                              2019));

        booksRepo.addBook(new Book("Java. Полное руководство, 10-е издание",
                                    publisherDictRepo.getItemByIdx(3),
                             "Шилдт Герберт",
                         2018));

        booksRepo.addBook(new Book("C/C++. Процедурное программирование",
                                    publisherDictRepo.getItemByIdx(3),
                             "Полубенцева М.И.",
                         2017));

        booksRepo.addBook(new Book("Конституция РФ",
                                         publisherDictRepo.getItemByIdx(0),
                              2020));

        System.out.println("First attempt...\n");
        PrintHelper.printAllBooks(booksRepo);

        System.out.println("\nfix location of BHV publisher...");
        publisherDictRepo.getItemByIdx(2).setLocation("Санкт-Петебург");

        System.out.println("Second attempt...\n");
        PrintHelper.printAllBooks(booksRepo);

        assert ( publisherDictRepo.getItemByIdx(1).getLocation()
                 .equals( publisherDictRepo.getItemByIdx(2).getLocation()));

        System.out.println("\n\ndone!");
    }
}
