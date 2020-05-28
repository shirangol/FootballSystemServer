package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Event.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class EventSQL implements DataBase<AEvent> {

    private static EventSQL ourInstance = new EventSQL();

    public static EventSQL getInstance() {
        return ourInstance;
    }

    private EventSQL() {
    }

    @Override
    public String get(long id) {
        return null;
    }

    @Override
    public List<String> getAll() {
        return null;
    }

    @Override
    public void save(AEvent event) throws SQLException {


    }

    @Override
    public void update(AEvent event, String[] params) {

    }

    @Override
    public void delete(AEvent event) {
        try {
            Connection con = DBConnector.getConnection();
            String query = "DELETE FROM Event WHERE eventID="+event.getId();
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.execute();
                query = "DELETE FROM event_log WHERE eventID="+event.getId();
                preparedStmt = con.prepareStatement(query);
                preparedStmt.execute();

            } catch (SQLException err){
                throw new RuntimeException("Cannot delete a non-existing user", err);
            }
            con.close();
        } catch (SQLException err) {
            throw new RuntimeException("Error connecting to the database", err);
        }

    }

    public void save(AEvent event, EventLog eventLog){
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement ps = con.prepareStatement("insert into Event(eventID , date , Minute,playerName, teamName,type) "
                    + "values (?,?,?,?,?,?)");
            ps.setInt(1,event.getId() );
            ps.setString(2,event.getDate().toString() );
            ps.setInt(3,event.getMinute() );
            ps.setString(4,event.getPlayerName());
            ps.setString(5,event.getTeamName());
            if(event instanceof Goal){
                ps.setInt(6,1 );
            }else if(event instanceof Injury){
                ps.setInt(6,2 );
            }else if(event instanceof Offense){
                ps.setInt(6,3 );
            }else if(event instanceof Offside){
                ps.setInt(6,4 );
            }else if(event instanceof RedCard){
                ps.setInt(6,5 );
            }else if(event instanceof YellowCard){
                ps.setInt(6,6 );
            }
            ps.execute();
            ps = con.prepareStatement("insert into Event_log(eventLogID,eventID) "
                    + "values (?,?)");
            ps.setInt(1,eventLog.getId() );
            ps.setInt(2,event.getId() );
            ps.execute();
            con.close();
        }catch (SQLException err) {
            err.printStackTrace();
        }
    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM event";
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
