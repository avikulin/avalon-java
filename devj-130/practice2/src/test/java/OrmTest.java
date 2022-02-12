import dto.Author;
import dto.Document;
import fabrics.StatementFabric;

public class OrmTest {
    public static void main(String[] args) {


        Author[] authors = new Author[]{
                new Author(1, "name1"),
                new Author(2, null),
                new Author(3, null, "comment1"),
                new Author(4, "name2", "comment2"),
        };


        for (Author author : authors) {
            System.out.println("------");
            System.out.println(author);
            System.out.println("");
            StatementFabric fabric = new StatementFabric(author);
            System.out.println("Mode: "+fabric.getMode());
            System.out.println(fabric.getSelectStatement());
            System.out.println(fabric.getInsertStatement());
            if (author.getAuthor_id() != 2) {
                System.out.println(fabric.getUpdateStatement());
            }
            System.out.println(fabric.getDeleteStatement());
        }

        Document[] documents = new Document[]{
                new Document(1, null, null, -1),
                new Document(2, "title1", null, 1),
                new Document(3, null, "text1", 2),
                new Document(4, "title2", "text2", 3)
        };

        System.out.println("\n");

        for (Document document : documents) {
            System.out.println("------");
            System.out.println(document);
            System.out.println("");
            StatementFabric fabric = new StatementFabric(document);
            System.out.println("Mode: "+fabric.getMode());
            System.out.println(fabric.getSelectStatement());
            System.out.println(fabric.getInsertStatement());
            System.out.println(fabric.getUpdateStatement());
            System.out.println(fabric.getDeleteStatement());
        }
    }
}
