package FootballSystem.System.FootballObjects.Event;
import FootballSystem.DataAccess.EventLogSQL;
import FootballSystem.DataAccess.EventSQL;
import FootballSystem.System.SystemEventLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventLog {

    private static int ID=1;
    //<editor-fold desc="Fields">
    private int id;
    private List<AEvent> aEventList;
    private String report;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public EventLog(){
        id=ID;
        ID++;
        aEventList=new ArrayList<AEvent>();
    }
    public EventLog(int id, List<AEvent>aEventList){
        this.id=id;
        this.aEventList=aEventList;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId(){return id;}
    public List<AEvent> getEventList(){
        return aEventList;
    }


    public String getReport() {
        return report;
    }

    public void setReport(String report) throws SQLException {
        EventLogSQL.getInstance().save(this);
        this.report = report;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * Adding event to log event
     * @param event
     */
    public void addEventToLog(AEvent event){
        aEventList.add(event);
        EventSQL.getInstance().save(event,this);
        sortEventLog();
        SystemEventLog.getInstance().writeToLog("Event was added to eventLog. Id:"+event.getId());
        //**************save to db
    }

    /**
     * Removes event from log event
     * @param event
     */
    public void removeEvent(AEvent event){
        aEventList.remove(event);
        EventSQL.getInstance().delete(event);

        sortEventLog();
        SystemEventLog.getInstance().writeToLog("Event was removed from eventLog. Id:"+event.getId());

    }

    /**
     * Sorting event log by time
     */
    public void  sortEventLog(){
        Collections.sort(this.aEventList, new Comparator<AEvent>() {
            public int compare(AEvent o1, AEvent o2) {
                if(o1.getMinute()==o2.getMinute()){
                    return 0;
                }
                if(o1.getMinute()>o2.getMinute() ){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }
    //</editor-fold>

}
