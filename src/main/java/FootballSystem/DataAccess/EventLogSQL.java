package FootballSystem.DataAccess;

import FootballSystem.System.FootballObjects.Event.EventLog;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EventLogSQL implements DataBase<EventLog> {
    private static EventLogSQL ourInstance = new EventLogSQL();

    public static EventLogSQL getInstance() {
        return ourInstance;
    }

    private EventLogSQL() {
    }

    @Override
    public List<String> get(long id) {
        List<String> events= new LinkedList<>();
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from event_log where eventLogID=" + id;
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {

                String sqlEvent=  "select * from event where eventID=" + rs.getInt("eventID");
                Statement statEvent = con.createStatement();
                ResultSet rsEvent = statEvent.executeQuery(sqlEvent);
                while (rsEvent.next()){
                    int eventID= rsEvent.getInt("eventID");
                    String date=rsEvent.getString("date");
                    String minute=rsEvent.getString("Minute");
                    String playerName=rsEvent.getString("playerName");
                    String teamName=rsEvent.getString("teamName");
                    int type=rsEvent.getInt("type");
                    String event= eventID+","+date+","+minute+","+playerName+","+teamName+","+type;
                    events.add(event);
                }

            }
            con.close();
            return events;
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String>getAll() {
        return null;
    }

    @Override
    public void save(EventLog eventLog) throws SQLException {
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            PreparedStatement ps = con.prepareStatement("insert into event_log_report(eventLogID,report) values (?,?) ");

            ps.setInt(1,eventLog.getId() );
            ps.setString(2,eventLog.getReport() );
            ps.execute();

        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    @Override
    public void update(EventLog eventLog, String[] params) {

    }

    @Override
    public void delete(EventLog eventLog) {

    }

    public int getTableSize(){
        int size=0;
        try {
            Connection con = DBConnector.getConnection();
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM event_log";
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
