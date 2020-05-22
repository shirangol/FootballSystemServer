package FootballSystem.DataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AlertSQL implements DataBase {

    private static final AlertSQL instance = new AlertSQL();
    DBConnector dbc;

    //<editor-fold desc="Constructors">
    /**
     * private constructor to avoid client applications to use constructor
     * @return
     */
    public static AlertSQL getInstance(){
        return instance;
    }

    /**
     * constructor
     */
    private AlertSQL() {
        dbc = DBConnector.getInstance();
    }
    //</editor-fold>s

    @Override
    public List<String> get(long id) {
        return null;
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public void save(Object o) throws SQLException{
        try {
            Connection con = DBConnector.getConnection();
            String userName = ((List<String>)o).get(0);
            String content = ((List<String>)o).get(1);
            PreparedStatement ps = con.prepareStatement("insert into Alert_To_Fan(username, content) "
                    + "values (?,?)");
            ps.setString(1,userName );
            ps.setString(2,content );
            ps.executeUpdate();
            con.close();
        }catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public void update(Object o, String[] params) {

    }

    @Override
    public void delete(Object o) {

    }

    public List<String> get(String userName) {
        List<String> alerts= new LinkedList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from Alert_To_Fan where username= " + " '" + userName + "'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                alerts.add(rs.getString("content"));
            }
            con.close();
        }catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);

        }

        return alerts;
    }
}
