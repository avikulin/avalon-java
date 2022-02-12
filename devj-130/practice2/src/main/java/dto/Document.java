package dto;

import annotations.DataClass;
import annotations.DataField;
import annotations.Mandatory;
import annotations.PrimaryKey;

import java.util.Date;
import java.util.Objects;

/**
 * Класс представляет общие сведения о документе и его содержание. Таблица
 * Documents имеет следующую структуру: - идентификатор документа,
 * представленный целым числом, является первичным ключом таблицы; - поле
 * названия документа длиной до 64 символов включительно, которое не может быть
 * пустым; - поле с основным текстом документа длиной до 1024 символов
 * включительно; - поле даты создания документа, которое должно быть обязательно
 * заполнено; - поле ссылки на автора документа, которое является внешним
 * ключом, ссылающимся на первичный ключ таблицы Authors, и которое также не
 * может быть пустым.
 *
 * @author (C)Y.D.Zakovryashin, 12.11.2020
 */

@DataClass(tableName = "Document")
public class Document {
    public static final int VERSION = 267384;
    private final int document_id;
    private String title;
    private String text;
    private Date date;
    private int author_id;

    public Document(int document_id, String title, String text, int author_id) {
        this(document_id, title, text,
                new Date(System.currentTimeMillis()), author_id);
    }

    public Document(int document_id, String title, String text, Date date, int author_id) {
        this.document_id = document_id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.author_id = author_id;
    }

    @PrimaryKey
    @DataField(fieldName = "id", order = 1)
    public int getDocument_id() {
        return document_id;
    }

    @Mandatory
    @DataField(fieldName = "title", order = 2)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DataField(fieldName = "content", order = 3)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @DataField(fieldName = "created", order = 4)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Mandatory
    @DataField(fieldName = "author", order = 5)
    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    @Override
    public int hashCode() {
        return VERSION + this.document_id + Objects.hashCode(this.title) + this.author_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Document other = (Document) obj;
        return !(this.document_id != other.document_id
                || this.author_id != other.author_id
                || !Objects.equals(this.title, other.title));
    }

    @Override
    public String toString() {
        return "Document{" +
                "document_id=" + document_id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", author_id=" + author_id +
                '}';
    }
}