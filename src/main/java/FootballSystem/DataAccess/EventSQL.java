package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Event.AEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EventSQL implements DataBase<AEvent> {
    private static EventSQL ourInstance = new EventSQL();

    public static EventSQL getInstance() {
        return ourInstance;
    }

    private EventSQL() {
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
    public void save(AEvent event) throws SQLException {

    }

    @Override
    public void update(AEvent event, String[] params) {

    }

    @Override
    public void delete(AEvent event) {

    }
}
