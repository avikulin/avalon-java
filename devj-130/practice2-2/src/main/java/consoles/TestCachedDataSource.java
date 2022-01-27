package consoles;

import dal.CachingDataSource;
import dal.DbContext;
import interfaces.metadata.ColumnMetadata;
import interfaces.dal.DataCache;
import interfaces.dal.DbConnection;
import utils.Tracer;

import java.util.stream.Collectors;

public class TestCachedDataSource {
    public static void main(String[] args) {
        try {
            DbConnection connection = DbContext.get();
            connection.connectTo("jdbc:derby://localhost:1527/toursdb");
            System.out.println("Tables:");
            System.out.println("--------");
            connection.getTables().forEach(x -> System.out.println(x));

            connection.setCurrentTable("COUNTRIES");
            DataCache ds = new CachingDataSource(connection);
            System.out.println("\n\nConnecting to table...");

            System.out.println("Table name: ".concat(ds.getTableName()));
            System.out.println(String.format("Total number of rows: %d", ds.getNumberOfRows()));
            //System.out.println(String.format("Total number of pages: %d", connection.getNumberOfPages()));
            System.out.println("Column structure:");
            System.out.println(
                    ds.getDataStructure()
                            .stream()
                            .map(ColumnMetadata::toString)
                            .collect(Collectors.joining(", "))
            );

            System.out.println("\nPrinting first row...");
            System.out.println(String.format("%s %s %s",
                    ds.getData(1, 1).toString(),
                    ds.getData(1, 2).toString(),
                    ds.getData(1, 3).toString()));

            System.out.println("\nPrinting last row...");
            System.out.println(String.format("%s %s %s",
                    ds.getData(114, 1).toString(),
                    ds.getData(114, 2).toString(),
                    ds.getData(114, 3).toString()));

            System.out.println("\nPrinting row in the middle...");
            System.out.println(String.format("%s %s %s",
                    ds.getData(55, 1).toString(),
                    ds.getData(55, 2).toString(),
                    ds.getData(55, 3).toString()));

            System.out.println("\n\nDiagnostic data:");
            System.out.println(Tracer.get().getLog());
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("\nDiagnostic log:");
            System.out.println(Tracer.get().getLog());
        }
    }
}
