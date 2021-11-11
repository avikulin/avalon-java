package utils;

import models.Book;
import repositories.BooksRepo;

public class PrintHelper {
    private static void funcPrintBookItemWithFormatting(Book item){
        System.out.println("\t- ".concat(item.toString()));
    }

    public static void printAllBooks(BooksRepo repo){
        System.out.println(String.format("Storage size: %d (%.2f%% filled):",
                                         repo.getSize(),
                                         (float)100*repo.getFillState()/repo.getSize()));

        repo.forEach(PrintHelper::funcPrintBookItemWithFormatting);
    }
}
