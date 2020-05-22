package FootballSystem.DataAccess;

import FootballSystem.System.Controller;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.Users.User;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class GameSQL implements DataBase<Game> {
    private static GameSQL ourInstance = new GameSQL();

    public static GameSQL getInstance() {
        return ourInstance;
    }
    DBConnector dbc;
    private GameSQL() {
        dbc = DBConnector.getInstance();
    }

    @Override
    public String get(long id) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from Games where gameID="+id;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String date = rs.getString("date");
                int hour = rs.getInt("hour");
                String result = rs.getString("result");
                int pTeamAway = rs.getInt("pTeamAway");
                int pTeamHome = rs.getInt("pTeamAway");
                String pMainReferee = rs.getString("pMainReferee");
                String pAssistant1Referee = rs.getString("pAssistant1Referee");
                String pAssistant2Referee = rs.getString("pAssistant2Referee");
                int pEventLogID = rs.getInt("pEventLogID");
                int pLeagueInformation = rs.getInt("pLeagueInformation");

                String p = gameID + " " + date + " " + hour + " " + result + " " + pTeamAway + " " + pTeamHome+ " " +pMainReferee + " " +pAssistant1Referee+ " " + pAssistant2Referee+ " " +pEventLogID + " " +pLeagueInformation;
                System.out.println(p);
                return p;

            }
            con.close();

        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;
    }

    @Override
    public List<String> getAll() {
        List<String> games=new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from Games";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int teamID = rs.getInt("gameID");
                String date = rs.getString("date");
                int hour = rs.getInt("hour");
                String result = rs.getString("result");
                int pTeamAway = rs.getInt("pTeamAway");
                int pTeamHome = rs.getInt("pTeamAway");
                String pMainReferee = rs.getString("pMainReferee");
                String pAssistant1Referee = rs.getString("pAssistant1Referee");
                String pAssistant2Referee = rs.getString("pAssistant2Referee");
                int pEventLogID = rs.getInt("pEventLogID");
                int pLeagueInformation = rs.getInt("pLeagueInformation");

                String p = teamID + " " + date + " " + hour + " " + result + " " + pTeamAway + " " + pTeamHome+ " " +pMainReferee + " " +pAssistant1Referee+ " " + pAssistant2Referee+ " " +pEventLogID + " " +pLeagueInformation;
                System.out.println(p);
                games.add(p);

            }
            con.close();
            return games;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public void save(Game game) throws SQLException {
        try {
            Connection connection = DBConnector.getConnection();

            int id= game.getId();
            String name= game.getDate().toString();
            int hour = game.getHour();
            String result= game.getResult();
            int away=game.getAway().getId();
            int home=game.getHome().getId();
            String main=game.getMainReferee().getName();
            String ass1= game.getAssistantRefereeOne().getName();
            String ass2= game.getAssistantRefereeTwo().getName();
            //int pEventLogID= game.getEventLog().toString();
            int pLeagueInformation= game.getLeagueInformation().getId();




            PreparedStatement ps=connection.prepareStatement("insert into Games(gameID, date, hour,result,pTeamAway,pTeamAway,pMainReferee,pAssistant1Referee,pAssistant2Referee,pEventLogID,pLeagueInformation ) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, hour);
            ps.setString(4, result);
            ps.setInt(5, away);
            ps.setInt(6, home);
            ps.setString(7, main);
            ps.setString(8, ass1);
            ps.setString(9, ass2);
            ps.setInt(10, 0);
            ps.setInt(11, pLeagueInformation);


            ps.executeUpdate();
            connection.close();

            System.out.println("Game: "+ game.getId()+" saved in DB");
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    @Override
    public void update(Game game, String[] params) {

    }

    @Override
    public void delete(Game game) {
        int id= game.getId();

        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();

            //String sql = "DELETE FROM users WHERE ID="+id;

            String query = "DELETE FROM Games WHERE ID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);

            // execute the preparedstatement
            preparedStmt.execute();

            con.close();

            System.out.println("Game:"+ game.getId()+" delete from DB");



        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    public List<String> getAllgamesForReferee(String username) {
        List<String> games=new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM games WHERE pMainReferee ="+ username +"OR pAssistant1Referee ="+ username +"OR pAssistant2Referee ="+ username ;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int teamID = rs.getInt("gameID");
                String date = rs.getString("date");
                Date date2= new Date();
                int temp= Integer.parseInt(date);
                date2.setTime(temp);

                int hour = rs.getInt("hour");
                String result = rs.getString("result");
                int pTeamAway = rs.getInt("pTeamAway");
                Team t1= Controller.getInstance().getTeam(pTeamAway);

                int pTeamHome = rs.getInt("pTeamAway");
                Team t2= Controller.getInstance().getTeam(pTeamHome);

                String pMainReferee = rs.getString("pMainReferee");
                Referee main= (Referee)Controller.getInstance().getUser(pMainReferee);

                String pAssistant1Referee = rs.getString("pAssistant1Referee");
                Referee ass1= (Referee)Controller.getInstance().getUser(pAssistant1Referee);

                String pAssistant2Referee = rs.getString("pAssistant2Referee");
                Referee ass2= (Referee)Controller.getInstance().getUser(pAssistant2Referee);

                int pEventLogID = rs.getInt("pEventLogID");
                int pLeagueInformation = rs.getInt("pLeagueInformation");
                String p = teamID + " " + date + " " + hour + " " + result + " " + pTeamAway + " " + pTeamHome+ " " +pMainReferee + " " +pAssistant1Referee+ " " + pAssistant2Referee+ " " +pEventLogID + " " +pLeagueInformation;
                System.out.println(p);
                //Game game=new Game(teamID,date2,hour,result,t1,t2,main,ass1,ass2,pEventLogID,null);
                games.add(p);
            }
            con.close();
            return games;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }
}
