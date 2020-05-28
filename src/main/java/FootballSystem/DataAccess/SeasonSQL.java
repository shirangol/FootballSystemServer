package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Season;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class SeasonSQL implements DataBase<Season> {
    private static SeasonSQL ourInstance = new SeasonSQL();

    public static SeasonSQL getInstance() {
        return ourInstance;
    }

    private SeasonSQL() {
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
    public void save(Season season) throws SQLException {

    }

    @Override
    public void update(Season season, String[] params) {

    }

    @Override
    public void delete(Season season) {

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM season";
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
