package FootballSystem.System.FootballObjects;

import FootballSystem.System.FootballObjects.Event.AEvent;
import FootballSystem.System.FootballObjects.Event.EventLog;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.I_Observer.IObserverGame;
import FootballSystem.System.I_Observer.ISubjectGame;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.SystemEventLog;
import FootballSystem.System.Users.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Game implements ISubjectGame {

    //<editor-fold desc="Fields">
    private static int ID=1;
    private int id;
    private Date date;
    private int hour;
    private String result;//1:0 format home:away
    private Referee mainReferee;
    private Referee assistantRefereeOne;
    private Referee assistantRefereeTwo;
    private Team away;
    private Team home;
    private EventLog eventLog;
    private List<IObserverGame> iObserverGameListForFans;
    private List<IObserverGame> iObserverGameListForReferees;
    private LeagueInformation leagueInformation;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    /**
     * Constructor
     * @param date
     * @param hour
     * @param mainReferee
     * @param assistantRefereeOne
     * @param assistantRefereeTwo
     * @param away
     * @param home
     */
    public Game(Date date, int hour, Referee mainReferee, Referee assistantRefereeOne, Referee assistantRefereeTwo, Team away, Team home) {
        this.id= ID;
        ID++;
        this.date = date;
        this.hour = hour;
        this.mainReferee = mainReferee;
        this.assistantRefereeOne = assistantRefereeOne;
        this.assistantRefereeTwo = assistantRefereeTwo;
        this.away=away;
        this.home=home;
        this.eventLog = new EventLog();
        this.iObserverGameListForFans= new LinkedList<>();
        this.iObserverGameListForReferees =new LinkedList<>();
    }

    public Game(int id,Date date,int hour,String result,  Referee mainReferee, Referee assistantRefereeOne, Referee assistantRefereeTwo, Team away, Team home, EventLog eventLog, LeagueInformation leagueInformation) {
        this.id= id;
        this.date = date;
        this.hour = hour;
        this.result=result;
        this.mainReferee = mainReferee;
        this.assistantRefereeOne = assistantRefereeOne;
        this.assistantRefereeTwo = assistantRefereeTwo;
        this.away=away;
        this.home=home;
        this.eventLog = eventLog;
        this.iObserverGameListForFans= new LinkedList<>();
        this.iObserverGameListForReferees =new LinkedList<>();
        this.leagueInformation=leagueInformation;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId(){
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public String getResult() {
        return result;
    }

    public Referee getMainReferee() {
        return mainReferee;
    }

    public Referee getAssistantRefereeOne() {
        return assistantRefereeOne;
    }

    public Referee getAssistantRefereeTwo() {
        return assistantRefereeTwo;
    }

    public Team getAway() {
        return away;
    }

    public Team getHome() {
        return home;
    }

    public String getHome1() {
        return home.getName();
    }

    public EventLog getEventLog() {
        return eventLog;
    }

    public List<IObserverGame> getiObserverGameListForFans() {
        return iObserverGameListForFans;
    }

    public List<IObserverGame> getiObserverGameListForReferees() {
        return iObserverGameListForReferees;
    }

    public LeagueInformation getLeagueInformation(){return  leagueInformation;}
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setDate(Date date) {
        this.date = date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Set result to the game and update the table league
     * @param home
     * @param away
     */
    public void setResult(int home, int away) {
        if(home<away){
            leagueInformation.updateScoreTeamInLeagueTable(this.home,"LOSS");
            leagueInformation.updateScoreTeamInLeagueTable(this.away,"WIN");
        }else if(home>away){
            leagueInformation.updateScoreTeamInLeagueTable(this.home,"WIN");
            leagueInformation.updateScoreTeamInLeagueTable(this.away,"LOSS");
        }
        else{
            leagueInformation.updateScoreTeamInLeagueTable(this.home,"TIE");
            leagueInformation.updateScoreTeamInLeagueTable(this.away,"TIE");
        }
        this.result = home+":"+away;
    }

    public void setMainReferee(Referee mainReferee) {
        this.mainReferee = mainReferee;
    }

    public void setAssistantRefereeOne(Referee assistantRefereeOne) {
        this.assistantRefereeOne = assistantRefereeOne;
    }

    public void setAssistantRefereeTwo(Referee assistantRefereeTwo) {
        this.assistantRefereeTwo = assistantRefereeTwo;
    }

    public void setAway(Team away) {
        this.away = away;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public void setEventLog(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    public void setiObserverGameListForFans(List<IObserverGame> iObserverGameListForFans) {
        this.iObserverGameListForFans = iObserverGameListForFans;
    }

    public void setiObserverGameListForReferees(List<IObserverGame> iObserverGameListForReferees) {
        this.iObserverGameListForReferees = iObserverGameListForReferees;
    }

    public void setLeagueInformation(LeagueInformation leagueInformation) {
        this.leagueInformation = leagueInformation;
    }

    public void setScore(int home,int away){
        this.result= home+":"+away;
    }

    //</editor-fold>

    //<editor-fold desc="Override Methods">
    /**
     * Adding event to log event
     * @param event
     */
    public void addEventToLogEvent(AEvent event){
        eventLog.addEventToLog(event);
        notifyFan(event);
    }

    /**
     * Adding a fan to alert list (iObserverGameListForFans)
     * @param fan
     */
    @Override
    public void registerFanToAlert(IObserverGame fan) { //UC-9
        this.iObserverGameListForFans.add(fan);
        fan.registerAlert(this);
        SystemEventLog.getInstance().writeToLog("Fan "+((User)fan).getName() +" (id: "+ ((User)fan).getId()+") register to receive a game (id: " + id +") alert");

    } //UC-9

    /**
     * Adding a referee to alert list (iObserverGameListForReferees)
     * @param referee
     */
    @Override
    public void registerRefereeToAlert(IObserverGame referee) {
        this.iObserverGameListForReferees.add(referee);
        referee.registerAlert(this);
        SystemEventLog.getInstance().writeToLog("Referee "+((User)referee).getName() +" (id: "+ ((User)referee).getId()+") register to receive a game (id: " + id +") alert");

    } //UC-9

    /**
     * Removing a fan from the alert list (iObserverGameListForFans)
     * @param fan
     */
    @Override
    public void removeAlertToFan(IObserverGame fan) {
        this.iObserverGameListForFans.remove(fan);
        fan.removeAlert(this);
        SystemEventLog.getInstance().writeToLog("Fan "+((User)fan).getName() +" (id: "+ ((User)fan).getId()+") removed alert to game (id: " + id +") alert");

    }

    /**
     * Removing a referee from the alert list (iObserverGameListForReferees)
     * @param referee
     */
    @Override
    public void removeAlertToReferee(IObserverGame referee) {
        this.iObserverGameListForReferees.remove(referee);
        referee.removeAlert(this);
        SystemEventLog.getInstance().writeToLog("Referee "+((User)referee).getName() +" (id: "+ ((User)referee).getId()+") removed alert to game (id: " + id +") alert");
    }

    /**
     * Send an alert to all fans in iObserverGameListForFans
     */
    @Override
    public void notifyFan (AEvent event) { //UC-10
        for (IObserverGame fan: iObserverGameListForFans) {
            fan.update(event.toString());
        }
    }

    /**
     * Send an alert to all referee in iObserverGameListForReferees
     */
    @Override
    public void notifyReferee (){
        for (IObserverGame referee: iObserverGameListForReferees) {
            referee.update(null);
        }
    }
    //</editor-fold>

}
