package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Event.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public Object get(long id) {
        return Optional.empty();
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
}
