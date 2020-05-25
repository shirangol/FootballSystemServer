package FootballSystem.System.Users;

import FootballSystem.DataAccess.GameSQL;
import FootballSystem.System.Controller;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Exeptions.NoRefereePermissions;
import FootballSystem.System.Exeptions.NoSuchEventException;
import FootballSystem.System.FootballObjects.Event.*;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.IShowable;
import FootballSystem.System.I_Observer.IObserverGame;
import FootballSystem.System.I_Observer.ISubjectGame;
import FootballSystem.System.SystemEventLog;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;


public class Referee extends User implements IObserverGame,IShowable {

    //<editor-fold desc="Fields">
    private RefereeType type;
    private List<ISubjectGame> subjectGame;
    private List<Game> games;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Referee(@JsonProperty("name")String name, @JsonProperty("type") RefereeType type, @JsonProperty("id") int id,  @JsonProperty("pass")String pass,  @JsonProperty("userName")String userName) {
        super(id, name, pass, userName);
        this.type = type;
        games = new LinkedList<>();
        subjectGame = new LinkedList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public List<ISubjectGame> getSubjectGame() {
        return subjectGame;
    }

    public RefereeType getRefereeType(){
        return type;
    }

    public List<Game> getGames() {
        if(games.size()==0){
            games= Controller.getInstance().getAllGamesForReferee(this.userName);
        }
        Collections.sort(games, new Comparator<Game>() {
            public int compare(Game o1, Game o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return games;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setType(RefereeType type) {
        this.type = type;
    }

    private void setSubjectGame(List<ISubjectGame> subjectGame) {
        this.subjectGame = subjectGame;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * add game to the games and register the referee to alerts from the game
     * @param g
     */
    public void addGame(Game g){
        g.registerRefereeToAlert(this);
        games.add(g);
        SystemEventLog.getInstance().writeToLog("a Game added to referee list. Referee's username:"+getUserName());
    }

    /**
     * remove game from his games and remove alerts
     * @param g
     */
    public void removeGame(Game g){
        g.removeAlertToReferee(this);
        games.remove(g);
        SystemEventLog.getInstance().writeToLog("a Game remove from referee list. Referee's username:"+getUserName());

    }

    /**
     * edit an event in the game and till 5 hours from the end of the game . only by the main Referee
     * @param game concret game
     * @param oldEvent old event to edit
     * @param type type of event
     */
    public void editEventAfterGame(Game game, AEvent oldEvent, String type,String playerName, String teamName,String time) throws NoRefereePermissions, NoSuchEventException {
        long diffHours = (new Date(System.currentTimeMillis()).getTime() - game.getDate().getTime()) / (60 * 60 * 1000);
        if (diffHours <= 6.5 && this.type == RefereeType.MAIN) {// 1.5 hours after the beginning
            AEvent editedEvent = createEvent(type, Integer.valueOf(time),playerName,teamName);
            game.getEventLog().removeEvent(oldEvent);
            game.addEventToLogEvent(editedEvent);
            SystemEventLog.getInstance().writeToLog("The referee " + getUserName() + " edited event" + "(event Id:" + oldEvent.getId() + ").");
        } else {
            SystemEventLog.getInstance().writeToLog("The referee " + getUserName() + " edited event" + "(event Id:" + oldEvent.getId() + ").");
            throw new NoRefereePermissions();
        }
    }
    //UC-40

    /**
     * add new event in the middle of the game - by every referee while it is in the middle of the game
     *
     * @param game current game
     * @param type type of the event
     * @param min min of the occasion
     */
    public void addEventMidGame(Game game, String type, int min, String playerName, String teamName) throws NoRefereePermissions, NoSuchEventException {
        long diffHours = (new Date(System.currentTimeMillis()).getTime() - game.getDate().getTime()) / (60 * 60 * 1000);

        if (diffHours < 1.5 && (game.getMainReferee().getId() == getId() || game.getAssistantRefereeTwo().getId() == getId() || game.getAssistantRefereeOne().getId() == getId())) {
            game.addEventToLogEvent(createEvent(type, min, playerName, teamName));
            SystemEventLog.getInstance().writeToLog("Event was added to the log event game " + game.getId() + " by the referee " + getUserName() + ".");
        } else {
            SystemEventLog.getInstance().writeToLog("Event wasn't added to the log event game " + game.getId() + " by the referee " + getUserName() + ".");
            throw new NoRefereePermissions();
        }
    }

    /**
     * add event to the game log event
     * @param game current game
     * @param type of the event
     * @param minute in the game
     */
    public void addEventToLogEvent(Game game, String type, int minute, String playerName, String teamName) throws NoSuchEventException {
        AEvent newEvent = createEvent(type, minute,playerName,teamName);
        game.addEventToLogEvent(newEvent);
        SystemEventLog.getInstance().writeToLog("Event was added to the event log game "+game.getId()+" by the referee " + getUserName()+"." );
    }

    /**
     * create gameReport
     * @param game current game
     * @return string with the report of the Referee
     */
    public String createGameReport(Game game) throws NoRefereePermissions {
        String report = "";
        long diffHours = (game.getDate().getTime()-new Date(System.currentTimeMillis()).getTime() ) / (60 * 60 * 1000);

        if (type == RefereeType.MAIN && game.getMainReferee().getUserName().equals(getUserName()) && diffHours>=1.5) {
            report = "Report for the game:" + game.getHome().getName() + " vs " + game.getAway().getName() + "\n";
            report += "Main Referee:" + game.getMainReferee().getName() + ".\n";
            report += "Assistant Referee:" + game.getAssistantRefereeOne().getName() + ".\n";
            report += "Assistant Referee:" + game.getAssistantRefereeTwo().getName() + ".\n";
            for (AEvent a : game.getEventLog().getEventList()) {
                report += a.getMinute() + ". " + a.getClass().toString().substring(35, a.getClass().toString().length()) + "\n";
            }
            SystemEventLog.getInstance().writeToLog("Report for the game:" + game.getHome().getName() + "vs" + game.getAway().getName() + "was created by the referee " + getUserName() + ".");
            return report;
        } else {
            SystemEventLog.getInstance().writeToLog("Referee no have permissions to createGameReport. username's Referee:"+getUserName());
            throw new NoRefereePermissions();
        }
    } //UC-41

    /**
     * return a list of future games of the referee
     * @return games for future
     */
    public List<Game> getFutureGames(){
        List<Game> futureGames = new ArrayList<>();
        for (Game game : games) {
            long diffHours = (new Date(System.currentTimeMillis()).getTime() - game.getDate().getTime()) / (60 * 60 * 1000);
            if (diffHours <= 0) {//to check it
                futureGames.add(game);
            }
        }
        Collections.sort(futureGames, new Comparator<Game>() {
            public int compare(Game o1, Game o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        SystemEventLog.getInstance().writeToLog("The referee "+getUserName()+" (username: "+getUserName() +")pull his future games.");
        return futureGames;
    }
    /**
     * Referee get the games that he\she was scheduled for them in the season
     * @param season current season to get the relevant games
     * @return
     */

    public List<Game> getGamesForSeason(Season season){
        List<LeagueInformation> seasonGames= season.getLeaguesInformation();
        List<Game> relevantGames= new ArrayList<>();

        for (int i = 0; i <seasonGames.size() ; i++) {
            LeagueInformation leagueInformation=seasonGames.get(i);

            for (int j = 0; j <leagueInformation.getGames().size() ; j++) {
                Game game = leagueInformation.getGames().get(j);
                if (game.getMainReferee().getUserName() == getUserName()) {
                    relevantGames.add(game);
                } else if (game.getAssistantRefereeOne().getUserName() == getUserName()) {
                    relevantGames.add(game);
                } else if (game.getAssistantRefereeTwo().getUserName() == getUserName()) {
                    relevantGames.add(game);
                }
            }
        }
        //to check it
        Collections.sort(relevantGames, new Comparator<Game>() {
            public int compare(Game o1, Game o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        SystemEventLog.getInstance().writeToLog("The referee pull his games for "+ season.getYear()+" season. "+"("+getUserName() +",username: "+getUserName()+")");
        return relevantGames;
    } //UC-39
    //</editor-fold>

    //<editor-fold desc="Override Methods">
    @Override
    public void update(String alert) {
        SystemEventLog.getInstance().writeToLog("Referee was updated by a game. Referee's id:"+getId());
    }

    /**
     * Add a game to get alert (adding to subjectGame list)
     * @param iSubjectGame
     */
    @Override
    public void registerAlert(ISubjectGame iSubjectGame) {
        subjectGame.add(iSubjectGame);
    }

    /**
     * Remove a game to get alert (adding to subjectGame list)
     * @param iSubjectGame
     */
    @Override
    public void removeAlert(ISubjectGame iSubjectGame) {
        subjectGame.remove(iSubjectGame);}

    @Override
    public String getType() {
        return "Referee";
    }

    @Override
    public String getDetails() {
        return "@name:"+this.name+"@role:"+type.toString();
    }
    //</editor-fold>

    //<editor-fold desc="private Methods">
    /**
     * create new event
     * @param type of the event
     * @param minute of the occasion
     * @return new AEvent by his type and his minute
     */
    private AEvent createEvent(String type, int minute, String playerName, String teamName) throws NoSuchEventException {
        switch (type) {
            case "Goal":
                return new Goal(minute,playerName,teamName);
            case "Injury":
                return new Injury(minute,playerName,teamName);
            case "Offense":
                return new Offense(minute,playerName,teamName);
            case "Offside":
                return new Offside(minute,playerName,teamName);
            case "RedCard":
                return new RedCard(minute,playerName,teamName);
            case "YellowCard":
                return new YellowCard(minute,playerName,teamName);
        }
        SystemEventLog.getInstance().writeToLog("Referee tried to non exists event in createEvent. username: "+getUserName());
        throw new NoSuchEventException();
    }

    @Override
    public void removeUser() {



    }
    //</editor-fold>

}
