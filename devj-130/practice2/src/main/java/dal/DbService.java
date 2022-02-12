package dal;

import dto.Author;
import dto.Document;
import enums.Mode;
import exceptions.DocumentException;
import interfaces.IDbService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class DbService implements IDbService {
    private final Connection connection;

    public DbService(String connectionString) {
        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalArgumentException("Connection string reference must be not-null and non-empty");
        }

        try {
            this.connection = DriverManager.getConnection(connectionString);
            this.connection.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new IllegalStateException("Can't open connection with params: ".concat(connectionString));
        }
    }

    private Mode getMode(Author author) throws DocumentException {
        String name = author.getAuthor();
        String comment = author.getNotes();

        if (name == null && comment == null) {
            throw new DocumentException("Incorrect object passed");
        }

        return (name != null && !name.isEmpty()) ? Mode.INSERT_OR_UPDATE : Mode.UPDATE_ONLY;
    }

    private void insertAuthor(Author author) throws SQLException {
        String sqlTemplate = "insert into \"Authors\" (id, name, comment) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlTemplate)) {
            statement.setInt(1, author.getAuthor_id());
            statement.setString(2, author.getAuthor());
            statement.setString(3, author.getNotes());
            statement.executeUpdate();
        }
    }

    private void updateAuthor(Author author) throws SQLException {
        int id = author.getAuthor_id();
        String name = author.getAuthor();
        String comment = author.getNotes();

        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append("update \"Authors\" set");
        if (name != null) {
            sqlCommand.append(" name = ?");
        }
        if (comment != null) {
            sqlCommand.append(" comment = ?");
        }
        sqlCommand.append(" where id = ?");

        try (PreparedStatement statement = connection.prepareStatement(sqlCommand.toString())) {
            int offset = 1;
            if (name != null) {
                statement.setString(offset, name);
                offset++;
            }
            if (comment != null) {
                statement.setString(offset, comment);
                offset++;
            }
            statement.setInt(offset, id);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean addAuthor(Author author) throws DocumentException {
        if (author == null) {
            throw new DocumentException("Author reference param must be not-null");
        }

        Mode mode = getMode(author);

        try {
            if (mode == Mode.INSERT_OR_UPDATE) {
                insertAuthor(author);
            } else {
                updateAuthor(author);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {}

            throw new DocumentException(
                    String.format(
                            "Can't %s the author with id #%d: %s",
                            (mode == Mode.INSERT_OR_UPDATE) ? "create" : "update",
                            author.getAuthor_id(),
                            e.getMessage()
                    )
            );
        }

        return mode == Mode.INSERT_OR_UPDATE;
    }

    private Mode getMode(Document doc) throws DocumentException {
        String title = doc.getTitle();
        String content = doc.getText();
        Date created = doc.getDate();

        if (title == null && content == null && created == null) {
            throw new DocumentException("Incorrect object passed");
        }

        return (title != null && !title.isEmpty() && created != null) ? Mode.INSERT_OR_UPDATE : Mode.UPDATE_ONLY;
    }


    private void insertDocument(Document doc) throws SQLException {
        String sqlTemplate = "insert into \"Documents\" (id, title, content, created, author) values (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlTemplate)) {
            statement.setInt(1, doc.getDocument_id());
            statement.setString(2, doc.getTitle());
            statement.setString(3, doc.getText());
            statement.setDate(4, new java.sql.Date(doc.getDate().getTime()));
            statement.setInt(5, doc.getAuthor_id());
            statement.executeUpdate();
        }
    }

    private void updateDocument(Document doc) throws SQLException {
        int id = doc.getDocument_id();
        String title = doc.getTitle();
        String content = doc.getText();
        Date created = doc.getDate();
        int author = doc.getAuthor_id();

        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append("update \"Documents\" set");
        if (title != null) {
            sqlCommand.append(" title = ?");
        }
        if (content != null) {
            sqlCommand.append(" content = ?");
        }

        if (created != null) {
            sqlCommand.append(" created = ?");
        }
        sqlCommand.append(" author = ?");
        sqlCommand.append(" where id = ?");

        try (PreparedStatement statement = connection.prepareStatement(sqlCommand.toString())) {
            int offset = 1;
            if (title != null) {
                statement.setString(offset, title);
                offset++;
            }
            if (content != null) {
                statement.setString(offset, content);
                offset++;
            }
            if (created != null) {
                statement.setDate(offset, new java.sql.Date(created.getTime()));
                offset++;
            }
            statement.setInt(offset, author);
            offset++;
            statement.setInt(offset, id);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean addDocument(Document doc, Author author) throws DocumentException {
        if (author == null) {
            throw new DocumentException("Author reference param must be not-null");
        }
        if (doc == null) {
            throw new DocumentException("Document reference param must be not-null");
        }

        if (doc.getAuthor_id() != author.getAuthor_id()) {
            throw new DocumentException("Inconsistency in document and author detected");
        }

        Mode mode = getMode(doc);

        try {
            addAuthor(author);
            if (mode == Mode.INSERT_OR_UPDATE) {
                insertDocument(doc);
            } else {
                updateDocument(doc);
            }
            connection.commit();
        } catch (DocumentException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            throw new DocumentException(
                    String.format(
                        "Can't %s the document with id #\"%d\": %s",
                        (mode == Mode.INSERT_OR_UPDATE) ? "create" : "update",
                        doc.getDocument_id(),
                        e.getMessage()
                    )
            );
        }

        return mode == Mode.INSERT_OR_UPDATE;
    }

    @Override
    public Document[] findDocumentByAuthor(Author author) throws DocumentException {
        String template =
                "select D.id,D.title,D.content, D.created, A.id " +
                "from \"Documents\" D " +
                "inner join \"Authors\" A " +
                "on D.author = A.id\n" +
                "where ";
        return new Document[0];
    }

    @Override
    public Document[] findDocumentByContent(String content) throws DocumentException {
        return new Document[0];
    }

    @Override
    public boolean deleteAuthor(Author author) throws DocumentException {
        return false;
    }

    @Override
    public boolean deleteAuthor(int id) throws DocumentException {
        return false;
    }

    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
