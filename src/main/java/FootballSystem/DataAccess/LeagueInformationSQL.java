package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Team.DefaultAllocate;
import FootballSystem.System.FootballObjects.Team.IScoreMethodPolicy;
import FootballSystem.System.FootballObjects.Team.ITeamAllocatePolicy;
import FootballSystem.System.FootballObjects.Team.OneGameAllocatePolicy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeagueInformationSQL implements DataBase<LeagueInformation> {
    private static LeagueInformationSQL ourInstance = new LeagueInformationSQL();

    public static LeagueInformationSQL getInstance() {
        return ourInstance;
    }
    DBConnector dbc;
    private LeagueInformationSQL() {
        dbc = DBConnector.getInstance();
    }

    @Override
    public Object get(long id) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from League_Information where leagueInformationID="+id;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int leagueInformationID = rs.getInt("leagueInformationID");
                String name = rs.getString("name");
                int winScore = rs.getInt("winScore");
                int lossScore = rs.getInt("lossScore");
                int tieScore = rs.getInt("tieScore");
                int allocatePolicyCode = rs.getInt("allocatePolicyCode");
                int scorePolicyCode = rs.getInt("scorePolicyCode");
                String pFootballAssociation = rs.getString("pFootballAssociation");
                int pLeague = rs.getInt("pLeague");
                int PSeason = rs.getInt("PSeason");


                String p = leagueInformationID + " " + name + " " + winScore + " " + lossScore + " " + tieScore + " " + allocatePolicyCode+ " " +scorePolicyCode + " " +pFootballAssociation+ " " + pLeague+ " " +PSeason ;
                System.out.println(p);
                return p;

            }
            con.close();

        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;

    }

    public Object getBypFootballAssociation(String footballAssociation) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from League_Information where pFootballAssociation="+"'"+footballAssociation+"'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int leagueInformationID = rs.getInt("leagueInformationID");
                String name = rs.getString("name");
                int winScore = rs.getInt("winScore");
                int lossScore = rs.getInt("lossScore");
                int tieScore = rs.getInt("tieScore");
                int allocatePolicyCode = rs.getInt("allocatePolicyCode");
                int scorePolicyCode = rs.getInt("scorePolicyCode");
                String pFootballAssociation = rs.getString("pFootballAssociation");
                int pLeague = rs.getInt("pLeague");
                int PSeason = rs.getInt("PSeason");


                String p = leagueInformationID + " " + name + " " + winScore + " " + lossScore + " " + tieScore + " " + allocatePolicyCode+ " " +scorePolicyCode + " " +pFootballAssociation+ " " + pLeague+ " " +PSeason ;
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
        List<String> leagueInformation=new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from League_Information where leagueInformationID";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int leagueInformationID = rs.getInt("leagueInformationID");
                String name = rs.getString("name");
                int winScore = rs.getInt("winScore");
                int lossScore = rs.getInt("lossScore");
                int tieScore = rs.getInt("tieScore");
                int allocatePolicyCode = rs.getInt("allocatePolicyCode");
                int scorePolicyCode = rs.getInt("scorePolicyCode");
                String pFootballAssociation = rs.getString("pFootballAssociation");
                int pLeague = rs.getInt("pLeague");
                int PSeason = rs.getInt("PSeason");


                String p = leagueInformationID + " " + name + " " + winScore + " " + lossScore + " " + tieScore + " " + allocatePolicyCode+ " " +scorePolicyCode + " " +pFootballAssociation+ " " + pLeague+ " " +PSeason ;
                System.out.println(p);
                leagueInformation.add(p);

            }
            con.close();
            return leagueInformation;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public void save(LeagueInformation leagueInformation) throws SQLException {
        try {
            Connection connection = DBConnector.getConnection();
            int leagueInformationID= leagueInformation.getId();
            String name= leagueInformation.getName();
            int winScore = leagueInformation.getWIN();
            int lossScore = leagueInformation.getLOSS();
            int tieScore = leagueInformation.getTIE();

            ITeamAllocatePolicy iTeamAllocatePolicy = leagueInformation.getiTeamAllocatePolicy();
            int piTeamAllocatePolicy=0;
            if(iTeamAllocatePolicy instanceof DefaultAllocate){
                piTeamAllocatePolicy=1;
            }
            else if(iTeamAllocatePolicy instanceof OneGameAllocatePolicy){
                piTeamAllocatePolicy=2;
            }
            IScoreMethodPolicy iScoreMethodPolicy= leagueInformation.getiScoreMethodPolicy();
            int piScoreMethodPolicy=1;
            //if(iScoreMethodPolicy.){}

            String FootballAssociation=leagueInformation.getFootballAssociation().getUserName();

            int pLeague= leagueInformation.getLeague().getid();
            int PSeason = leagueInformation.getSeason().getIntYear();



            PreparedStatement ps=connection.prepareStatement("insert into League_Information(leagueInformationID, name, winScore,lossScore,tieScore,allocatePolicyCode,scorePolicyCode,pFootballAssociation,pLeague,PSeason ) "
                    + "values (?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, leagueInformationID);
            ps.setString(2, name);
            ps.setInt(3, winScore);
            ps.setInt(4, lossScore);
            ps.setInt(5, tieScore);
            ps.setInt(6, piTeamAllocatePolicy);
            ps.setInt(7, piScoreMethodPolicy);
            ps.setString(8, FootballAssociation);
            ps.setInt(9, pLeague);
            ps.setInt(10, PSeason);


            ps.executeUpdate();
            connection.close();

            System.out.println("Game: "+ leagueInformation.getId()+" saved in DB");
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    @Override
    public void update(LeagueInformation leagueInformation, String[] params) {

    }

    @Override
    public void delete(LeagueInformation leagueInformation) {
        int id= leagueInformation.getId();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();

            //String sql = "DELETE FROM users WHERE ID="+id;

            String query = "DELETE FROM League_Information WHERE leagueInformationID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);

            // execute the preparedstatement
            preparedStmt.execute();

            con.close();

            System.out.println("Game:"+ leagueInformation.getId()+" delete from DB");



        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    public List<String> getAllLegueInformation(int leagueID) {
        List<String> leagueInformation=new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from League_Information where pLeague="+leagueID;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int leagueInformationID = rs.getInt("leagueInformationID");
                String name = rs.getString("name");
                int winScore = rs.getInt("winScore");
                int lossScore = rs.getInt("lossScore");
                int tieScore = rs.getInt("tieScore");
                int allocatePolicyCode = rs.getInt("allocatePolicyCode");
                int scorePolicyCode = rs.getInt("scorePolicyCode");
                String pFootballAssociation = rs.getString("pFootballAssociation");
                int pLeague = rs.getInt("pLeague");
                int PSeason = rs.getInt("PSeason");





                String p = leagueInformationID + " " + name + " " + winScore + " " + lossScore + " " + tieScore + " " + allocatePolicyCode+ " " +scorePolicyCode + " " +pFootballAssociation+ " " + pLeague+ " " +PSeason ;
                System.out.println(p);
                leagueInformation.add(p);
            }
            con.close();
            return leagueInformation;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    public void updateAllocatePolicy(int id, ITeamAllocatePolicy iTeamAllocatePolicy) {
        int type;
        if(iTeamAllocatePolicy instanceof  DefaultAllocate){
            type=1;
        }else{
            type=2;
        }
        try {
            Connection connection = DBConnector.getConnection();

            PreparedStatement ps=connection.prepareStatement("update League_Information set allocatePolicyCode="+type+" where leagueInformationID= "+id);
            ps.executeUpdate();
            connection.close();

        } catch (SQLException err) {
//            throw new RuntimeException("Error connecting to the database", err);
            err.printStackTrace();
        }

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM league_information";
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
