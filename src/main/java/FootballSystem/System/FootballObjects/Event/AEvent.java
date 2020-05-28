package FootballSystem.System.FootballObjects.Event;

import FootballSystem.System.SystemEventLog;

import java.util.Date;

public abstract class AEvent {

    //<editor-fold desc="Fields">
    private static int ID=1;
    private int id;
    private Date date;
    private int minute;
    private String playerName;
    private String teamName;
    //</editor-fold>

    //<editor-fold desc="Constructor">

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Abstract constructor for all the events that can occur in game
     * @param minuteInTheGame
     */
    public AEvent(int minuteInTheGame, String playerName ,String teamName ) {
        this.minute = minuteInTheGame;
        id=ID;
        ID++;
        date=new Date(System.currentTimeMillis());
        SystemEventLog.getInstance().writeToLog("New Event was created. ("+id+")");
        this.playerName = playerName;
        this.teamName = teamName;
    }
    public AEvent(int id,Date date, int minuteInTheGame, String playerName ,String teamName ) {
        this.minute = minuteInTheGame;
        this.id=id;
        this.date=date ;
        this.playerName = playerName;
        this.teamName = teamName;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public int getMinute(){
        return minute;
    }

    public Date getDate() {
        return date;
    }

    public String getTeamName() {
        return teamName;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    public static void setID(int ID) {
        AEvent.ID = ID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public abstract String getType();
    public abstract String getTypeToAlert();

    public String toString(){
        return this.teamName+": the player "+ this.playerName +" got " + getTypeToAlert()+" in minute "+ this.minute;
    }


    //</editor-fold>

}
