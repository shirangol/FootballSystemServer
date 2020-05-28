package FootballSystem.DataAccess;

import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.FootballObjects.Field;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.PersonalPages.PersonalPage;
import FootballSystem.System.Users.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamSQL implements DataBase<Team> {
    private static TeamSQL ourInstance = new TeamSQL();
    DBConnector dbc;

    public static TeamSQL getInstance() {
        return ourInstance;
    }

    private TeamSQL() {
        dbc = DBConnector.getInstance();
    }

    @Override
    public String get(long id) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from Team where teamID="+id;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int teamID = rs.getInt("teamID");
                String name = rs.getString("name");
                int status = rs.getInt("TeamStatus");
                int fieldID = rs.getInt("fieldID");
                int pPersonalPage = rs.getInt("pPersonalPage");
                int income = rs.getInt("income");
                int expense = rs.getInt("expense");
                int pLeague = rs.getInt("pLeague");


                String p = teamID + " " + name + " " + status + " " + fieldID + " " + pPersonalPage + " " + income+ " " +expense + " " +pLeague;
                System.out.println(p);
                return (p);
            }
            con.close();

        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;
    }

    @Override
    public List<String> getAll() {
        List<String> teams= new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from Team";

            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                int teamID = rs.getInt("teamID");
                String name = rs.getString("name");
                int status = rs.getInt("TeamStatus");
                int fieldID = rs.getInt("fieldID");
                int pPersonalPage = rs.getInt("pPersonalPage");
                int income = rs.getInt("income");
                int expense = rs.getInt("expense");
                int pLeague = rs.getInt("pLeague");

                String p = teamID + " " + name + " " + status + " " + fieldID + " " + pPersonalPage + " " + income+ " " +expense + " " +pLeague;
                System.out.println(p);
                teams.add(p);
            }

            con.close();
            return teams;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }
    public List<String> getAllForLeague(int id) {
        List<String> teams= new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from Team where pLeague="+id;

            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                int teamID = rs.getInt("teamID");
                String name = rs.getString("name");
                int status = rs.getInt("TeamStatus");
                int fieldID = rs.getInt("fieldID");
                int pPersonalPage = rs.getInt("pPersonalPage");
                int income = rs.getInt("income");
                int expense = rs.getInt("expense");
                int pLeague = rs.getInt("pLeague");

                String p = teamID + " " + name + " " + status + " " + fieldID + " " + pPersonalPage + " " + income+ " " +expense + " " +pLeague;
                System.out.println(p);
                teams.add(p);
            }

            con.close();
            return teams;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    @Override
    public void save(Team team) throws SQLException {
        try {
            Connection connection = DBConnector.getConnection();
            int id= team.getId();
            String name= team.getName();

            int status=0;
            TeamStatus teamStatus= team.getTeamStatus();
            if(teamStatus==TeamStatus.Active){
                status=1;
            }else if(teamStatus==TeamStatus.Close){
                status=2;
            }else{
                status=3;
            }
            int in=team.getIncome();
            // int exp=team.getExpense();
            int exp=1000;


            PreparedStatement ps=connection.prepareStatement("insert into Team(teamID, name, TeamStatus,fieldID,pPersonalPage,income,expense,pLeague) "
                    + "values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, status);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, in);
            ps.setInt(7, exp);
            ps.setInt(8, 0);

            ps.executeUpdate();
            connection.close();

            System.out.println("Team:"+ team.getName()+" saved in DB");
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    @Override
    public void update(Team team, String[] params) {

    }

    @Override
    public void delete(Team team) {
        int id= team.getId();

        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();

            //String sql = "DELETE FROM users WHERE ID="+id;

            String query = "DELETE FROM Team WHERE teamID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);

            // execute the preparedstatement
            preparedStmt.execute();

            con.close();

            System.out.println("Team:"+ team.getName()+" delete from DB");



        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM team";
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
