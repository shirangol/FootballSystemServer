package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Game;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GameSQL implements DataBase<Game> {
    private static GameSQL ourInstance = new GameSQL();

    public static GameSQL getInstance() {
        return ourInstance;
    }

    private GameSQL() {
    }

    @Override
    public Object get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Game> getAll() {
        return null;
    }

    @Override
    public void save(Game game) throws SQLException {

    }

    @Override
    public void update(Game game, String[] params) {

    }

    @Override
    public void delete(Game game) {

    }
}
