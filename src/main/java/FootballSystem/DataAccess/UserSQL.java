package FootballSystem.DataAccess;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Users.*;


import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class UserSQL implements DataBase<User> {

    private static final UserSQL instance = new UserSQL();
    DBConnector dbc;

    //<editor-fold desc="Constructors">
    /**
     * private constructor to avoid client applications to use constructor
     * @return
     */
    public static UserSQL getInstance(){
        return instance;
    }

    /**
     * constructor
     */
    private UserSQL() {
        dbc = DBConnector.getInstance();
    }
    //</editor-fold>s

    //<editor-fold desc="Save function for different users">
    private void saveReferee(Referee user ,Connection connection) {
        try {
            int type=0;
            if(user.getRefereeType()== RefereeType.MAIN){
                type=1;
            }else if(user.getRefereeType()== RefereeType.ASSISTANT){
                type=2;
            }
            PreparedStatement ps=connection.prepareStatement("insert into referee(username,refreeType) "
                    + "values (?,?)");
            ps.setString(1, user.getUserName());
            ps.setInt(2, type);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveCoach(Coach user,Connection connection) {
        System.out.println("Coach table dosent exist");
    }

    private void savePlayer(Player user,Connection connection) {
        try {
            PreparedStatement ps=connection.prepareStatement("insert into player(username,birthday,roll,assetValue,Salary,pPersonalPage,pTeam) "
                    + "values (?,?,?,?,?,?,?)");
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getBirthDate().toString());
            ps.setInt(3, 0);//?????????????there isnt code for that
            ps.setInt(4, user.getAssetValue());
            ps.setInt(5, user.getSalary());
            ps.setInt(6, 0);//???????????
            ps.setInt(7, 0);//?????????????
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveFootballAssociation(FootballAssociation user,Connection connection ){

    }

    private void saveSyatemManager(SystemManager user,Connection connection){
        System.out.println("SystemManager table dosent exist");

    }

    private void saveTeamOwner(TeamOwner user,Connection connection) {
        try {
            PreparedStatement ps=connection.prepareStatement("insert into team_owner(username,salary,coach,teamManager,player) "
                    + "values (user.getUserName(),0,null ,null ,null )");
        }catch (SQLException e) {
            e.printStackTrace();
        }
        //table TeamOwner  username,salary,coach,teamManager,player
    }

    private void saveTeamManager(TeamManager user,Connection connection) {
        try {
            PreparedStatement ps=connection.prepareStatement("insert into team_manager(username,teamID,teamOwnerID,assetValue,salary) "
                    + "values (user.getUserName(),0,0 ,0 ,0 )");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //</editor-fold>

    //<editor-fold desc="Override">
    @Override
    public Object get(long id) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from users where Id="+id;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                String username_col = rs.getString("username");
                int id_col = rs.getInt("Id");
                String fullName_col = rs.getString("name");
                String password_col = rs.getString("password");
                int UserTypeCode = rs.getInt("UserTypeCode");
                switch(UserTypeCode) {
                    case 1:
                         //Fan fan=new Fan(id_col,fullName_col,password_col,username_col);
                         String fanStr=id_col+" "+fullName_col+" "+password_col+" "+username_col;
                         con.close();
                         return fanStr;
                    case 2:
                         RefereeType type=getRefereeTypeSQL(username_col);
                         //Referee referee=new Referee(fullName_col, type, id_col, password_col, username_col);
                         String refereeStr=fullName_col+" "+ type.toString()+" "+ id_col+" "+ password_col+" "+ username_col;
                         con.close();
                         return refereeStr;
                    case 3:
                         //Coach coach=new Coach(id_col, fullName_col, password_col,username_col, null, null, 0, 0);
                        String coachStr=id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                         return coachStr;
                    case 4:
                        //Player player=new Player(id_col, fullName_col, password_col, username_col, null, null, 0, 0);
                        String playerStr=id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                        return playerStr;
                    case 5:
                        //FootballAssociation footballAssociation=new FootballAssociation(id_col, fullName_col, password_col, username_col);
                        String faStr=id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                        return faStr;
//                    case 6:
//                        SystemManager systemManager=new SystemManager(id_col, fullName_col, password_col, username_col);
//                        con.close();
//                        return systemManager;
//                    case 7:
//                        TeamOwner teamOwner=new TeamOwner(id_col, fullName_col, password_col, username_col,0);
//                        con.close();
//                        return teamOwner;
//                    case 8:
//                         TeamManager teamManager=new TeamManager(id_col, fullName_col, password_col, username_col,0,0);
//                         con.close();
//                         return teamManager;

                }//close switch
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;
    }

    @Override
    public void update(User user, String[] params) {

    }

    @Override
    public void delete(User user) {
        int id= user.getId();
        try {
            Connection con = DBConnector.getConnection();
            String query = "DELETE FROM users WHERE Id ="+id;
             try {
                 PreparedStatement preparedStmt = con.prepareStatement(query);
                 preparedStmt.execute();
             }catch (SQLException err){
                 throw new RuntimeException("Cannot delete a non-existing user", err);
             }
             con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public List<String> getAll() {
        try {
            List<User>listToReturn=new LinkedList<>();
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from users";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                String username_col = rs.getString("username");
                int id_col = rs.getInt("Id");
                String fullName_col = rs.getString("name");
                String password_col = rs.getString("password");
                int UserTypeCode = rs.getInt("UserTypeCode");
                switch(UserTypeCode) {
                    case 1:
                        Fan fan=new Fan(id_col,fullName_col,password_col,username_col);
                        listToReturn.add(fan);
                    case 2:
                        Referee referee=new Referee(fullName_col, null, id_col, password_col, username_col);
                        listToReturn.add(referee);
                    case 3:
                        Coach coach=new Coach(id_col, fullName_col, password_col,username_col, null, null, 0, 0);
                        listToReturn.add(coach);
                    case 4:
                        Player player=new Player(id_col, fullName_col, password_col, username_col, null, null, 0, 0);
                        listToReturn.add(player);
                    case 5:
                        FootballAssociation footballAssociation=new FootballAssociation(id_col, fullName_col, password_col, username_col);
                        listToReturn.add(footballAssociation);
                    case 6:
                        SystemManager systemManager=new SystemManager(id_col, fullName_col, password_col, username_col);
                        listToReturn.add(systemManager);
                    case 7:
                        TeamOwner teamOwner=new TeamOwner(id_col, fullName_col, password_col, username_col,0);
                        listToReturn.add(teamOwner);
                    case 8:
                        TeamManager teamManager=new TeamManager(id_col, fullName_col, password_col, username_col,0,0);
                        listToReturn.add(teamManager);
                    // default:
                }//close switch
                String p = id_col + " " + username_col + " " + fullName_col + " " + password_col + " " + UserTypeCode ;
                System.out.println(p);
            }
            con.close();
            return listToReturn;
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    @Override
    public void save(User user)  {
        try {
            Connection connection = DBConnector.getConnection();
            String userName=user.getUserName();
            PreparedStatement ps=connection.prepareStatement("insert into users(username, Id, name,password,status,userTypeCode) "
                    + "values (?,?,?,?,?,?)");
            ps.setString(1,userName );
            ps.setInt(2, user.getId());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPassword());
            ps.setInt(5, 1);
            ps.setInt(6, 2);
            ps.executeUpdate();

            //<editor-fold desc="Set the user type">
            if(user instanceof Fan){
                try{
                   ps.setInt(2,1);
                   ps.executeUpdate();}catch (Exception e){}
            }else if(user instanceof Referee){
                try{
                   ps.setInt(2,2);
                   ps.executeUpdate();}catch (Exception e){}
                   saveReferee((Referee) user,connection);
            }else if(user instanceof Coach){
                try{
                   ps.setInt(2,3);
                   ps.executeUpdate();}catch (Exception e){}
                   saveCoach((Coach) user,connection);
            }
            else if(user instanceof Player){
                try{
                    ps.setInt(2,4);
                    ps.executeUpdate();}catch (Exception e){}
                    savePlayer((Player) user,connection);
            }
            else if(user instanceof FootballAssociation){
                try{
                    ps.setInt(2,5);
                    ps.executeUpdate();}catch (Exception e){}
                    saveFootballAssociation((FootballAssociation) user,connection);
            }
            else if(user instanceof SystemManager){
                try{
                    ps.setInt(2,6);
                    ps.executeUpdate();}catch (Exception e){}
                    saveSyatemManager((SystemManager) user,connection);
            }
            else if(user instanceof TeamOwner){
                try{
                    ps.setInt(2,7);
                    ps.executeUpdate();}catch (Exception e){}
                    saveTeamOwner((TeamOwner) user,connection);
            }
            else if(user instanceof TeamManager){
                try{
                    ps.setInt(2,8);
                    ps.executeUpdate();}catch (Exception e){}
                    saveTeamManager((TeamManager) user,connection);
            }
            //</editor-fold>

            connection.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }
    //</editor-fold>

    public void deleteByUserName(String userName) {
        try {
            Connection con = DBConnector.getConnection();
            String query = "DELETE FROM users WHERE username ="+ "'" +userName + "'";
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.execute();
            }catch (SQLException err){
                throw new RuntimeException("Cannot delete a non-existing user", err);
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
    }

    public RefereeType getRefereeTypeSQL (String username){
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from referee where username="+ "'" + username + "'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int refereeType = rs.getInt("refreeType");
                if(refereeType==1){
                    return RefereeType.MAIN;
                }else{
                    return RefereeType.ASSISTANT;
                }
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;

    }

    public Object get(String userName) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from users where username= " + " '"+userName + "'";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                int id_col = rs.getInt("Id");
                String fullName_col = rs.getString("name");
                String password_col = rs.getString("password");
                int UserTypeCode = rs.getInt("UserTypeCode");
                switch(UserTypeCode) {
                    case 1:
                        //Fan fan=new Fan(id_col,fullName_col,password_col,username_col);
                        String fanStr=id_col+" "+fullName_col+" "+password_col+" "+userName;
                        con.close();
                        return fanStr;
                    case 2:
                        RefereeType type=getRefereeTypeSQL(userName);
                        //Referee referee=new Referee(fullName_col, type, id_col, password_col, username_col);
                        String refereeStr=fullName_col+" "+ type.toString()+" "+ id_col+" "+ password_col+" "+ userName;
                        System.out.println(refereeStr);//*********************************
                        con.close();
                        return refereeStr;
                    case 3:
                        //Coach coach=new Coach(id_col, fullName_col, password_col,username_col, null, null, 0, 0);
                        String coachStr=id_col+" "+ fullName_col+" "+ password_col+" "+userName;
                        con.close();
                        return coachStr;
                    case 4:
                        //Player player=new Player(id_col, fullName_col, password_col, username_col, null, null, 0, 0);
                        String playerStr=id_col+" "+ fullName_col+" "+ password_col+" "+userName;
                        con.close();
                        return playerStr;
                    case 5:
                        //FootballAssociation footballAssociation=new FootballAssociation(id_col, fullName_col, password_col, username_col);
                        String faStr=id_col+" "+ fullName_col+" "+ password_col+" "+userName;
                        con.close();
                        return faStr;
//                    case 6:
//                        SystemManager systemManager=new SystemManager(id_col, fullName_col, password_col, userName);
//                        con.close();
//                        return systemManager;
//                    case 7:
//                        TeamOwner teamOwner=new TeamOwner(id_col, fullName_col, password_col, userName,0);
//                        con.close();
//                        return teamOwner;
//                    case 8:
//                         TeamManager teamManager=new TeamManager(id_col, fullName_col, password_col, userName,0,0);
//                         con.close();
//                         return teamManager;

                }//close switch
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;
    }
}
