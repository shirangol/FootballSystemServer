package FootballSystem.DataAccess;

import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.Users.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class LeagueSQL implements DataBase<League> {

    DBConnector dbc;

    private static LeagueSQL ourInstance = new LeagueSQL();

    public static LeagueSQL getInstance() {
        return ourInstance;
    }

    private LeagueSQL() {
        dbc = DBConnector.getInstance();
    }

    @Override
    public String get(long id) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM league where leagueID="+ id;
            ResultSet rs = stat.executeQuery(sql);
            int leagueID_col=0;
            String name_col="";
            while (rs.next()) {
                leagueID_col = rs.getInt("leagueID");
                name_col = rs.getString("name");
            }
            con.close();
            String str=leagueID_col+" "+name_col;
            return str;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    public String get(League league){
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM league where name='"+league.getName()+"'";
            ResultSet rs = stat.executeQuery(sql);
            int leagueID_col=0;
            String name_col="";
            while (rs.next()) {
                leagueID_col = rs.getInt("leagueID");
                name_col = rs.getString("name");
            }
            con.close();
            String str=leagueID_col+" "+name_col;
            return str;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public List<String> getAll() {
        try {
            List<String>listToReturn=new LinkedList<>();
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from league" ;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int leagueID_col = rs.getInt("leagueID");
                String name_col = rs.getString("name");

                String p = leagueID_col + " " + name_col ;
                listToReturn.add(p);
                System.out.println(p);
            }
            con.close();
            return listToReturn;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }



    @Override
    public void save(League league) throws SQLException {
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement ps=con.prepareStatement("insert into league(leagueID, name) "
                    + "values (?,?)");
            ps.setInt(1, league.getid());
            ps.setString(2, league.getName());
            ps.executeUpdate();
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public void update(League league, String[] params) {

    }

    @Override
    public void delete(League league) {
        try {
            Connection con = DBConnector.getConnection();
            String query = "DELETE FROM league WHERE name='"+league.getName()+"'";
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.execute();
            } catch (SQLException err){
                throw new RuntimeException("Cannot delete a non-existing user", err);
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    public void delete(String leagueName){
        try {
            Connection con = DBConnector.getConnection();
            String query = "DELETE FROM league WHERE name='"+leagueName+"'";
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.execute();
            } catch (SQLException err){
                throw new RuntimeException("Cannot delete a non-existing user", err);
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM league";
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
