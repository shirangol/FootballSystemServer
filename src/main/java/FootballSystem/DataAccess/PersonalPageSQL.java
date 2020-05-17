package FootballSystem.DataAccess;

import FootballSystem.System.PersonalPages.PersonalPage;

import java.sql.SQLException;
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
    public Optional<PersonalPage> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<PersonalPage> getAll() {
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
}
