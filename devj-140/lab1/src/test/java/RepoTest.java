import contracts.Repository;
import dal.RepositoryImpl;
import model.TaskItem;
import model.TaskPriority;

import java.time.LocalDate;

public class RepoTest {
    public static void main(String[] args) {
        Repository<TaskItem> repository = new RepositoryImpl<>();
        repository.addItem(
                new TaskItem(1,
                        TaskPriority.LOW,
                        "Task 1",
                        LocalDate.of(2022, 03, 20)
                )
        );
        TaskItem tiOne = repository.getItem(0);
        tiOne.setDescription("Task #1");
        tiOne.setPriority(TaskPriority.HIGH);
        repository.deleteItem(tiOne);

        repository.addItem(
                new TaskItem(2,
                        TaskPriority.NORMAL,
                        "Task 2",
                        LocalDate.of(2022, 04, 20)
                )
        );
        repository.deleteItem(repository.getItem(0));

        repository.addItem(
                new TaskItem(3,
                        TaskPriority.HIGH,
                        "Task 3",
                        LocalDate.of(2022, 05, 20)
                )
        );
        repository.getItem(0).setPriority(TaskPriority.LOW);

        repository.saveToDb();
    }
}
