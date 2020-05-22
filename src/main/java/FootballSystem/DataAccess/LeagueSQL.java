package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.League;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LeagueSQL implements DataBase<League> {


    private static LeagueSQL ourInstance = new LeagueSQL();

    public static LeagueSQL getInstance() {
        return ourInstance;
    }

    private LeagueSQL() {
    }

    @Override
    public Object get(long id) {
        return Optional.empty();
    }

    @Override
    public List<String> getAll() {
        return null;
    }

    @Override
    public void save(League league) throws SQLException {

    }

    @Override
    public void update(League league, String[] params) {

    }

    @Override
    public void delete(League league) {

    }
}
