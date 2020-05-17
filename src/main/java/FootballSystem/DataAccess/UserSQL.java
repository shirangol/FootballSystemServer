package FootballSystem.DataAccess;
import FootballSystem.System.Users.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserSQL implements DataBase<User> {

    private static final UserSQL instance = new UserSQL();

    //private constructor to avoid client applications to use constructor
    public static UserSQL getInstance(){
        return instance;
    }
    DBConnector dbc;

    private UserSQL() {
        dbc = DBConnector.getInstance();

    }
    @Override
    public Optional<User> get(long id) {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();

            String sql = "select * from users where ID="+id;

            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                int id_col = rs.getInt("RunID");
                int id_col2 = rs.getInt("id");
                String fullName = rs.getString("fullName");
                String userName = rs.getString("UserName");
                String password = rs.getString("password");
                int status = rs.getInt("Status");
                int pointet = rs.getInt("Pointet");
                int UserTypeCode = rs.getInt("UserTypeCode");


                String p = id_col + " " + id_col2 + " " + fullName + " " + userName + " " + password + " " + status + " " + pointet + " " + UserTypeCode;
                System.out.println(p);
            }
            con.close();

        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;
        //return Optional.empty();
    }

    @Override
    public List<User> getAll() {

        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from users";

            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                int id_col = rs.getInt("RunID");
                int id_col2 = rs.getInt("id");
                String fullName = rs.getString("fullName");
                String userName = rs.getString("UserName");
                String password = rs.getString("password");
                int status = rs.getInt("Status");
                int pointet = rs.getInt("Pointet");
                int UserTypeCode = rs.getInt("UserTypeCode");


                String p = id_col + " " + id_col2 + " " + fullName + " " + userName + " " + password + " " + status + " " + pointet + " " + UserTypeCode;
                System.out.println(p);
            }
            con.close();

        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }
        return null;
    }

    @Override
    public void save(User user)  {
        try {
            Connection connection = DBConnector.getConnection();
            PreparedStatement ps=connection.prepareStatement("insert into users(id, fullName, UserName,password,Status,userTypeCode) "
                    + "values ( user.getId(), user.getName(),user.getUserName(),user.getPassword(),?,?)");
            if(user instanceof Fan){
                ps.setInt(2,1);//
            }else if(user instanceof Referee){
                ps.setInt(2,2);//
                saveReferee((Referee) user,connection);

            }else if(user instanceof Coach){
                ps.setInt(2,3);//
                saveCoach((Coach) user);

            }
            else if(user instanceof Player){
                ps.setInt(2,4);//
                savePlayer((Player) user);
            }
            else if(user instanceof FootballAssociation){
                ps.setInt(2,5);// 1= fan

            }
            else if(user instanceof SystemManager){
                ps.setInt(2,6);//
            }
            else if(user instanceof TeamOwner){
                ps.setInt(2,7);//
                saveTeamOwner((TeamOwner) user);

            }
            else if(user instanceof TeamManager){
                ps.setInt(2,8);//
                saveTeamManager((TeamManager) user);
            }


            ps.executeUpdate();
            connection.close();




        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    private void savePlayer(Player user) {
        //table Player
    }

    private void saveTeamManager(TeamManager user) {
        //table TeamManager
    }


    private void saveCoach(Coach user) {
        //table Coach
    }

    private void saveReferee(Referee user ,Connection connection) {
        //referee Table

    }

    private void saveTeamOwner(TeamOwner user) {
        //table TeamOwner
    }


    @Override
    public void update(User user, String[] params) {

    }

    @Override
    public void delete(User user) {
        int id= user.getId();

        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();

            //String sql = "DELETE FROM users WHERE ID="+id;

            String query = "DELETE FROM users WHERE ID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, id);





            // execute the preparedstatement
            preparedStmt.execute();

            con.close();



        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }
}
