package FootballSystem.DataAccess;
////////////////////////////////////
//////////////////////////////////
import FootballSystem.System.FootballObjects.Team.Team;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TeamSQL implements DataBase<Team> {
    private static TeamSQL ourInstance = new TeamSQL();

    public static TeamSQL getInstance() {
        return ourInstance;
    }

    private TeamSQL() {
    }

    @Override
    public Object get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Team> getAll() {
        return null;
    }

    @Override
    public void save(Team team) throws SQLException {

    }

    @Override
    public void update(Team team, String[] params) {

    }

    @Override
    public void delete(Team team) {

    }
}
