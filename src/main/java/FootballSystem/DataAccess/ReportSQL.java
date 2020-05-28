package FootballSystem.DataAccess;

import FootballSystem.System.Report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ReportSQL implements DataBase<Report> {
    private static ReportSQL ourInstance = new ReportSQL();

    public static ReportSQL getInstance() {
        return ourInstance;
    }

    private ReportSQL() {
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
    public void save(Report report) throws SQLException {

    }

    @Override
    public void update(Report report, String[] params) {

    }

    @Override
    public void delete(Report report) {

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM reports";
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
