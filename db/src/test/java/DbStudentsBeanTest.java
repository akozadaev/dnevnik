import org.junit.Test;
import ru.tsu.dnevnik.db.beans.DbStudentsBean;

public class DbStudentsBeanTest {


    @Test
    public void testCreateUser() throws Exception {
        DbStudentsBean dbUsersBean = new DbStudentsBean();
        dbUsersBean.createStudent("Козадаев Алексей Сергеевич");
    }

}
