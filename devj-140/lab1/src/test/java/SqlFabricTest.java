import contracts.Reflector;
import contracts.SqlFabric;
import dal.SqlFabricImpl;
import model.TaskItem;
import utils.DataEntityReflector;

public class SqlFabricTest {
    public static void main(String[] args) {
        SqlFabric<TaskItem> sqlFabric = new SqlFabricImpl<>(new DataEntityReflector<>(TaskItem.class));
        /*System.out.println(sqlFabric.createTable(TaskItem.class));
        System.out.println(sqlFabric.getAllRecords(TaskItem.class));*/
    }
}
