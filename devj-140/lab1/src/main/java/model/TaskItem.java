package model;

import contracts.Extractable;
import javafx.beans.NamedArg;
import tags.DataEntityConstructor;
import tags.DataField;
import tags.PKey;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import tags.TableName;

import java.time.LocalDate;

@TableName(name = "TASKS")
public class TaskItem implements Extractable {
    @PKey
    @DataField(name = "ID", slqType = "INT")
    private final SimpleIntegerProperty Id;

    @DataField(name = "PRIORITY", slqType = "VARCHAR(10)")
    private final SimpleObjectProperty<TaskPriority> priority;

    @DataField(name = "DESCRIPTION", slqType = "VARCHAR(500)")
    private final SimpleStringProperty description;

    @DataField(name = "DUE_DATE", slqType = "DATE")
    private final SimpleObjectProperty<LocalDate> dueDate;

    @DataEntityConstructor
    public TaskItem(@NamedArg("ID") int id,
                    @NamedArg("PRIORITY") TaskPriority priority,
                    @NamedArg("DESCRIPTION") String description,
                    @NamedArg("DUE_DATE") LocalDate dueDate) {
        this.Id = new SimpleIntegerProperty(this, "ID");
        this.priority = new SimpleObjectProperty<>(this, "PRIORITY");
        this.description = new SimpleStringProperty(this, "DESCRIPTION");
        this.dueDate = new SimpleObjectProperty<>(this, "DUE_DATE");
        setId(id);
        setPriority(priority);
        setDescription(description);
        setDueDate(dueDate);
    }

    public int getId() {
        return Id.get();
    }

    public TaskPriority getPriority() {
        return priority.get();
    }

    public String getDescription() {
        return description.get();
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setId(int id) {
        Id.set(id);
    }

    public void setPriority(TaskPriority priority) {
        if (priority == null){
            throw new IllegalArgumentException("Priority object reference must be not-null");
        }
        this.priority.set(priority);
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()){
            throw new IllegalArgumentException("Description object reference must be not-null & non-empty string");
        }
        this.description.set(description);
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null){
            throw new IllegalArgumentException("Due date object reference must be not-null");
        }
        if (dueDate.compareTo(LocalDate.now()) < 0){
            throw new IllegalArgumentException("Due date can't point to date in the past");
        }
        this.dueDate.set(dueDate);
    }

    @Override
    public Observable[] extractProperties() {
        return new Observable[]{this.Id, this.description, this.priority, this.dueDate};
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "Id=" + Id.get() +
                ", priority=" + priority.get() +
                ", description=" + description.get() +
                ", dueDate=" + dueDate.get() +
                '}';
    }
}
