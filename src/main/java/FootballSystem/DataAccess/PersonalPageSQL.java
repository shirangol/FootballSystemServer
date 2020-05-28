package FootballSystem.DataAccess;

import FootballSystem.System.PersonalPages.PersonalPage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class PersonalPageSQL implements DataBase<PersonalPage> {
    private static PersonalPageSQL ourInstance = new PersonalPageSQL();

    public static PersonalPageSQL getInstance() {
        return ourInstance;
    }

    private PersonalPageSQL() {
    }

    @Override
    public String get(long id) {
        return null;
    }

    @Override
    public List<String> getAll() {
        return null;
    }

    @Override
    public void save(PersonalPage personalPage) throws SQLException {

    }

    @Override
    public void update(PersonalPage personalPage, String[] params) {

    }

    @Override
    public void delete(PersonalPage personalPage) {

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM personal_page";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                size++;
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return size;
    }
}
