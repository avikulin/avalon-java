import models.Book;
import repositories.BooksRepo;
import repositories.PublisherDictRepo;
import utils.PrintHelper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

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
        publisherDictRepo.getItemByIdx(2).setLocation("Санкт-Петербург");

        System.out.println("Second attempt...\n");
        PrintHelper.printAllBooks(booksRepo);

        try {
            // проверка метода установки значнеия места издания.
            assert (publisherDictRepo.getItemByIdx(1).getLocation()
                    .equals(publisherDictRepo.getItemByIdx(2).getLocation())) : "Location must be \"Санкт-Петебург\"";

            // проверка метода serAuthors на передачу пустого массива.
            Book bookWithoutAuthors = booksRepo.getItemByIdx(4);
            bookWithoutAuthors.setAuthors(new String[]{});

            assert (Arrays.equals(bookWithoutAuthors.getAuthors(), new String[]{})) : "The authors must be " +
                    "an empty array";

            // проверка метода getAuthorByIdx на книге без авторов
            assert (bookWithoutAuthors.getAuthorByIdx(0).isEmpty()) : "The authors must be an empty string(\"\")";

            // проверка метода getAuthorByIdx на книге с одним автором
            Book bookWithSingleAuthor = booksRepo.getItemByIdx(1);
            String author = bookWithSingleAuthor.getAuthorByIdx(0);
            assert (author.equals("Вигерс Карл")) : "Author is not \"Вигерс Карл\"";

            // проверка метода getAuthorByIdx на книге с двумя авторами
            Book bookWithMultipleAuthors = booksRepo.getItemByIdx(0);
            assert (bookWithMultipleAuthors.getAuthorByIdx(0).equals("Седжвик Роберт")) : "The first author should be " +
                    "\"Седжвик Роберт\"";
            assert (bookWithMultipleAuthors.getAuthorByIdx(1).equals("Уэйн Кевин")) : "The second author should be " +
                    "\"Седжвик Роберт\"";

            // проверка метода getAuthorByIdx на передаче некорректного значения индекса
            try {
                bookWithMultipleAuthors.getAuthorByIdx(3);
                throw new AssertionError("Exception must be thrown in usage incorrect " +
                        "index in getAuthorByIdx(..)");
            } catch (RuntimeException exception) { // ловим всех наследников, в т.ч. IllegalArgumentException
                assert (exception.getMessage().
                        equals("Index must not be less 0, and must not exceed the array length")) :
                        "Wrong exception has been thrown in usage incorrect index in getAuthorByIdx(..)";
            }
        } catch (AssertionError assertion) {
            StringWriter stackTraceContext = new StringWriter();
            assertion.printStackTrace(new PrintWriter(stackTraceContext));
            System.out.println(String.format("\n!!!\tAssertion has been thrown. \n" +
                    "\tDetails:\n \t%s\n" +
                    "\tStack trace: \n" +
                    "\t%s", assertion.getMessage(), stackTraceContext.toString()));
        }

        System.out.println("\n\ndone!");
    }
}
