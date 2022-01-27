package dto;

import annotations.DataClass;
import annotations.DataField;
import annotations.Mandatory;
import annotations.PrimaryKey;

import java.util.Objects;

/**
 * Класс представляет одну запись из таблицы Authors базы данных, т.е. данные об
 * авторах документов. Таблица Authors имеет следующую структуру: -
 * идентификатор автора, представленный целым числом, является первичным ключом
 * таблицы; - поле с именем и фамилией автора длиной до 64 символов
 * включительно, которое не может быть пустым; - поле примечания длиной до 255
 * символов включительно.
 *
 * @author (( C)Y.D.Zakovryashin, 12.11.2020
 */

@DataClass(tableName = "Authors")
public class Author {
    public static final int VERSION = 79897;
    private final int author_id;
    private String author;
    private String notes;

    public Author(int author_id, String author) {
        this(author_id, author, null);
    }

    public Author(int author_id, String author, String notes) {
        this.author_id = author_id;
        this.author = author;
        this.notes = notes;
    }

    @PrimaryKey
    @DataField(fieldName = "id", order = 1)
    public int getAuthor_id() {
        return author_id;
    }

    @Mandatory
    @DataField(fieldName = "name", order = 2)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @DataField(fieldName = "comment", order = 3)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int hashCode() {
        return VERSION + this.author_id + Objects.hashCode(this.author);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Author)) {
            return false;
        }

        final Author other = (Author) obj;
        return !(this.author_id != other.author_id
                || !Objects.equals(this.author, other.author));
    }

    @Override
    public String toString() {
        return "Author{" +
                "author_id=" + author_id +
                ", author='" + author + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
