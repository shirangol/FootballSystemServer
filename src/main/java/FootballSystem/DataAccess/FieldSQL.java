package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Field;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class FieldSQL implements DataBase<Field> {
    private static FieldSQL ourInstance = new FieldSQL();

    public static FieldSQL getInstance() {
        return ourInstance;
    }

    private FieldSQL() {
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
    public void save(Field field) throws SQLException {

    }

    @Override
    public void update(Field field, String[] params) {

    }

    @Override
    public void delete(Field field) {

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM field";
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
