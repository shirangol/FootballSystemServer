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
                    + "values (?,?,? ,? ,? )");
            ps.setString(1, user.getUserName());
            ps.setInt(2, 0);
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setString(5, "");
            ps.executeUpdate();

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
    public String get(long id) {
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
                         String fanStr="Fan "+id_col+" "+fullName_col+" "+password_col+" "+username_col;
                         con.close();
                         return fanStr;
                    case 2:
                         RefereeType type=getRefereeTypeSQL(username_col);

                       // System.out.println(type.toString() + "*******************************");
                         //Referee referee=new Referee(fullName_col, type, id_col, password_col, username_col);
                         String refereeStr="Referee "+fullName_col+" "+ type.toString()+" "+ id_col+" "+ password_col+" "+ username_col;
                         con.close();
                         return refereeStr;
                    case 3:
                         //Coach coach=new Coach(id_col, fullName_col, password_col,username_col, null, null, 0, 0);
                        String coachStr="Coach "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                         return coachStr;
                    case 4:
                        //Player player=new Player(id_col, fullName_col, password_col, username_col, null, null, 0, 0);
                        String playerStr="Player "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                        return playerStr;
                    case 5:
                        //FootballAssociation footballAssociation=new FootballAssociation(id_col, fullName_col, password_col, username_col);
                        String faStr="FootballAssociation "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                        return faStr;
                    case 6:
                        //SystemManager systemManager=new SystemManager(id_col, fullName_col, password_col, username_col);
                        String systemManagerStr="SystemManager "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        con.close();
                        return systemManagerStr;
                    case 7:
                        //TeamOwner teamOwner=new TeamOwner(id_col, fullName_col, password_col, username_col,0);
                        String teamOwnerStr="TeamOwner "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col+" "+0;
                        con.close();
                        return teamOwnerStr;
                    case 8:
                         //TeamManager teamManager=new TeamManager(id_col, fullName_col, password_col, username_col,0,0);
                         String teamManagerStr="TeamManager "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col+" "+0+" "+0;
                         con.close();
                         return teamManagerStr;

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
        try {
//            UPDATE Customers
//            SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'
//            WHERE CustomerID = 1;


        }catch (Exception e){

        }
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

                 if (user instanceof Referee) {
                   try {
                       String queryReff = "DELETE FROM referee WHERE Id ="+id;
                       preparedStmt = con.prepareStatement(queryReff);
                       preparedStmt.execute();
                   }catch (Exception e){}

                 }
             } catch (SQLException err){
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
            List<String>listToReturn=new LinkedList<>();
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
                        //Fan fan=new Fan(id_col,fullName_col,password_col,username_col);
                        String fanStr="Fan "+id_col+" "+fullName_col+" "+password_col+" "+username_col;
                        listToReturn.add(fanStr);
                        break;
                    case 2:
                        //Referee referee=new Referee(fullName_col, null, id_col, password_col, username_col);
                        RefereeType type=getRefereeTypeSQL(username_col);
                        if(type==null)
                            type=RefereeType.ASSISTANT;
                        String refereeStr="Referee "+fullName_col+" "+ type.toString()+" "+ id_col+" "+ password_col+" "+ username_col;
                        listToReturn.add(refereeStr);
                        break;
                    case 3:
                        //Coach coach=new Coach(id_col, fullName_col, password_col,username_col, null, null, 0, 0);
                        String coachStr="Coach "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        listToReturn.add(coachStr);
                        break;
                    case 4:
                        //Player player=new Player(id_col, fullName_col, password_col, username_col, null, null, 0, 0);
                        String playerStr="Player "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        listToReturn.add(playerStr);
                    case 5:
                        //FootballAssociation footballAssociation=new FootballAssociation(id_col, fullName_col, password_col, username_col);
                        String faStr="FootballAssociation "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        listToReturn.add(faStr);
                        break;
                    case 6:
                        //SystemManager systemManager=new SystemManager(id_col, fullName_col, password_col, username_col);
                        String systemManagerStr="SystemManager "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col;
                        listToReturn.add(systemManagerStr);
                        break;
                    case 7:
                        //TeamOwner teamOwner=new TeamOwner(id_col, fullName_col, password_col, username_col,0);
                        String teamOwnerStr="TeamOwner "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col+" "+0;
                        listToReturn.add(teamOwnerStr);
                        break;
                    case 8:
                        //TeamManager teamManager=new TeamManager(id_col, fullName_col, password_col, username_col,0,0);
                        String teamManagerStr="TeamManager "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col+" "+0+" "+0;
                        listToReturn.add(teamManagerStr);
                        break;
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


            //<editor-fold desc="Set the user type">
            if(user instanceof Fan){
                try{
                   ps.setInt(6,1);
                   ps.executeUpdate();}catch (Exception e){}
            }else if(user instanceof Referee){
                try{
                   ps.setInt(6,2);
                   ps.executeUpdate();}catch (Exception e){}
                   saveReferee((Referee) user,connection);
            }else if(user instanceof Coach){
                try{
                   ps.setInt(6,3);
                   ps.executeUpdate();}catch (Exception e){}
                   saveCoach((Coach) user,connection);
            }
            else if(user instanceof Player){
                try{
                    ps.setInt(6,4);
                    ps.executeUpdate();}catch (Exception e){}
                    savePlayer((Player) user,connection);
            }
            else if(user instanceof FootballAssociation){
                try{
                    ps.setInt(6,5);
                    ps.executeUpdate();}catch (Exception e){}
                    saveFootballAssociation((FootballAssociation) user,connection);
            }
            else if(user instanceof SystemManager){
                try{
                    ps.setInt(6,6);
                    ps.executeUpdate();}catch (Exception e){}
                    saveSyatemManager((SystemManager) user,connection);
            }
            else if(user instanceof TeamOwner){
                try{
                    ps.setInt(6,7);
                    ps.executeUpdate();
                }catch (Exception e){
                    System.out.println(e);
                }
                    saveTeamOwner((TeamOwner) user,connection);
            }
            else if(user instanceof TeamManager){
                try{
                    ps.setInt(6,8);
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

                    try {
                        String queryReff = "DELETE FROM referee WHERE username ="+ "'" +userName + "'";
                        preparedStmt = con.prepareStatement(queryReff);
                        preparedStmt.execute();
                    }catch (Exception e){}

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

    public String get(String userName) {
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
                        String fanStr="Fan"+" "+id_col+" "+fullName_col+" "+password_col+" "+userName;
                        con.close();
                        return fanStr;
                    case 2:
                        RefereeType type=getRefereeTypeSQL(userName);
                        //Referee referee=new Referee(fullName_col, type, id_col, password_col, username_col);
                        String refereeStr="Referee"+" "+fullName_col+" "+ type.toString()+" "+ id_col+" "+ password_col+" "+ userName;
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
                        String faStr="FootballAssociation"+" "+id_col+" "+ fullName_col+" "+ password_col+" "+userName;
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

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM users";
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
