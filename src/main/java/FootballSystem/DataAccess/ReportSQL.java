package FootballSystem.DataAccess;

import FootballSystem.System.Report;

import java.sql.SQLException;
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
}
