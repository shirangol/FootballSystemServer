package FootballSystem.DataAccess;

import java.sql.SQLException;
import java.util.*;
public interface DataBase <T> {

        Object get(long id);

        List<String> getAll();

        void save(T t) throws SQLException;

        void update(T t, String[] params);

        void delete(T t);
    }

