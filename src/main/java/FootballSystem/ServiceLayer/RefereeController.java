package FootballSystem.ServiceLayer;

import FootballSystem.System.Exeptions.NoRefereePermissions;
import FootballSystem.System.Exeptions.NoSuchEventException;
import FootballSystem.System.FootballObjects.Event.AEvent;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.Users.User;
import FootballSystem.System.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RefereeController extends MainUserController {
    private static RefereeController ourInstance = new RefereeController();

    public static RefereeController getInstance() {
        return ourInstance;
    }

    private RefereeController() {
    }

    public void editDetails(Referee referee, int id, String name, String password) {
        FanController fanController = FanController.getInstance();
        fanController.editDetails(referee, id, name, password);
    }

    public List<Game> getMyGames(Referee referee) {
        return referee.getGames();
    }

    public List<Game> getMyFutureGames(Referee referee) {
        return referee.getFutureGames();
    }

    public List<Game> getMySeasonGames(Referee referee, Season s) {
        return referee.getGamesForSeason(s);
    }


    public void addEventDuringGame(Referee referee, Game game, String type, int min, String playerName, String teamName) throws NoRefereePermissions, NoSuchEventException {
        referee.addEventMidGame(game, type, min, playerName, teamName);
    }

    public void addEventDuringGame(String name, String game, String type, int min, String playerName, String team) throws NoRefereePermissions, NoSuchEventException {
        HashMap<String, User> dic = Controller.getInstance().getUsers();
        Referee referee = ((Referee) dic.get(name));
        Game game1 = null;
        for (Game g : referee.getGames()) {
            if (g.getId() == Integer.parseInt(game)) {
                game1 = g;
            }
        }
        addEventDuringGame(referee, game1, type, min, playerName, team);
        if (type.equals("Goal")) {
            if (game1.getHome().getName().equals(team)) {
                if (game1.getResult() == null) {
                    game1.setResult(0, 0);
                }
                game1.setResult(Integer.parseInt(game1.getResult().split(":")[0]) + 1, Integer.parseInt(game1.getResult().split(":")[1]));
            } else {
                if (game1.getResult() == null) {
                    game1.setResult(0, 0);
                }
                String home = game1.getResult().split(":")[0];
                int away = (Integer.parseInt(game1.getResult().split(":")[1]) + 1);
                game1.setResult(Integer.parseInt(home), away);
            }
        }
    }

    public List<AEvent> getEventsOfGame(User user, Game game) throws NoRefereePermissions {
        if (!(user instanceof Referee)) {
            throw new NoRefereePermissions();
        }
        return game.getEventLog().getEventList();
    }

    public void editEventAfterGame(Referee referee, Game game, String type, AEvent oldEvent, String playerName, String teamName) throws NoRefereePermissions, NoSuchEventException {
        referee.editEventAfterGame(game, oldEvent, type, playerName, teamName);
    }

    public void addEventAfterGame(Referee referee, Game game, String type, int minute, String playerName, String teamName) throws NoRefereePermissions, NoSuchEventException {
        if (game.getMainReferee() != referee) {
            throw new NoRefereePermissions();
        }
        referee.addEventToLogEvent(game, type, minute, playerName, teamName);
    }

    public void createGameReport(Referee referee, Game game) throws NoRefereePermissions {
        if (game.getMainReferee() != referee) {
            throw new NoRefereePermissions();
        }
        referee.createGameReport(game);
    }

    public List<String> getMyGames(String refereeName) {
        HashMap<String, User> dic = Controller.getInstance().getUsers();
        Referee referee = ((Referee) dic.get(refereeName));
        List<Game> games = this.getMyGames(referee);
        LinkedList<String> output = new LinkedList<>();
        for (Game g : games) {
            String[] strA = g.getDate().toString().split(" ");
            String str = g.getHome().getName() + "," + g.getAway().getName() + "," + strA[1] + " " + strA[2] + "," + strA[3].substring(0, strA[3].length() - 3) + "," + g.getId();
            output.add(str);
        }
        return output;
    }

    public String getScore(String idGame, String refereeName) {
        String output = "";
        HashMap<String, User> dic = Controller.getInstance().getUsers();
        Referee referee = ((Referee) dic.get(refereeName));
        for (Game g : referee.getGames()) {
            if (g.getId() == Integer.parseInt(idGame)) {
                if (g.getResult() != null) {
                    output = g.getResult();
                } else {
                    output = "0:0";
                }
            }
        }
        return output;
    }

    public Boolean isGameLive(String idGame, String refereeName) {
        HashMap<String, User> dic = Controller.getInstance().getUsers();
        Referee referee = ((Referee) dic.get(refereeName));
        for (Game g : referee.getGames()) {
            if (g.getId() == Integer.parseInt(idGame)) {
                Date startTime = new Date();
                Date endTime = new Date(startTime.getTime() + 2 * (3600 * 1000));
                if (startTime.after(g.getDate()))
                    if (g.getDate().before(endTime))
                        return true;
            }
        }
        return false;
    }

    public List<String> getEvents(String gameID, String refereeName) {
        HashMap<String, User> dic = Controller.getInstance().getUsers();
        List<String> events = new LinkedList<>();
        Referee referee = ((Referee) dic.get(refereeName));
        for (Game g : referee.getGames()) {
            if (g.getId() == Integer.parseInt(gameID)) {
                for(AEvent event : g.getEventLog().getEventList()){
                    events.add(event.getClass().getName()+","+"'"+event.getMinute()+","+event.getPlayerName()+","+event.getTeamName());
                }
            }
        }
        return events;
    }

    public void postEventReport(String userName , String gameID , String report){
        HashMap<String, User> dic = Controller.getInstance().getUsers();
        Referee referee = ((Referee) dic.get(userName));
        for (Game g : referee.getGames()) {
            if (g.getId() == Integer.parseInt(gameID)) {
              g.getEventLog().setReport(report);
            }
        }
    }
}
