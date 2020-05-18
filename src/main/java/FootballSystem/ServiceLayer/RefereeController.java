package FootballSystem.ServiceLayer;
import FootballSystem.ServiceLayer.Exceptions.OnlyForReferee;
import FootballSystem.System.Exeptions.NoRefereePermissions;
import FootballSystem.System.Exeptions.NoSuchEventException;
import FootballSystem.System.FootballObjects.Event.AEvent;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.Users.User;
import FootballSystem.System.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api/referee")
@RestController
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


    private void addEventDuringGame(Referee referee, Game game, String type, int min, String playerName, String teamName) throws NoRefereePermissions, NoSuchEventException {
        referee.addEventMidGame(game, type, min, playerName, teamName);
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

    @PostMapping(value = "/addEventDuringGame")
    public ResponseEntity addEventDuringGame(@RequestBody Map<String,String> body) throws NoRefereePermissions, NoSuchEventException, OnlyForReferee {
        User referee =  Controller.getInstance().getUser(body.get("user_name"));
        if(!(referee instanceof Referee)){
            throw new OnlyForReferee();
        }
        Game game = null;
        for (Game g : ((Referee)referee).getGames()) {
            if (g.getId() == Integer.parseInt(body.get("game"))) {
                game = g;
            }
        }
        addEventDuringGame(((Referee)referee), game, body.get("type"), Integer.valueOf(body.get("min")), body.get("playerName"), body.get("team"));
        if (body.get("type").equals("Goal")) {
            if (game.getHome().getName().equals( body.get("team"))) {
                if (game.getResult() == null) {
                    game.setResult(0, 0);
                }
                game.setResult(Integer.parseInt(game.getResult().split(":")[0]) + 1, Integer.parseInt(game.getResult().split(":")[1]));
            } else {
                if (game.getResult() == null) {
                    game.setResult(0, 0);
                }
                String home = game.getResult().split(":")[0];
                int away = (Integer.parseInt(game.getResult().split(":")[1]) + 1);
                game.setResult(Integer.parseInt(home), away);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "getMyGames/{user_name}")
    public ResponseEntity getMyGames(@PathVariable("user_name") String refereeName) throws OnlyForReferee {
        User referee =  Controller.getInstance().getUser(refereeName);
        if(!(referee instanceof Referee)){
            throw new OnlyForReferee();
        }

        List<Game> games = this.getMyGames((Referee)referee);
        LinkedList<String> output = new LinkedList<>();
        for (Game g : games) {
            String[] strA = g.getDate().toString().split(" ");
            String str = g.getHome().getName() + "," + g.getAway().getName() + "," + strA[1] + " " + strA[2] + "," + strA[3].substring(0, strA[3].length() - 3) + "," + g.getId();
            output.add(str);
        }
        return new ResponseEntity(output, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "getScore/{game_id}/{referee_name}")
    public ResponseEntity getScore(@PathVariable("game_id")String idGame,@PathVariable("referee_name") String refereeName) throws OnlyForReferee {
        String output = "";
        User referee =  Controller.getInstance().getUser(refereeName);
        if(!(referee instanceof Referee)){
            throw new OnlyForReferee();
        }

        for (Game g : ((Referee)referee).getGames()) {
            if (g.getId() == Integer.parseInt(idGame)) {
                if (g.getResult() != null) {
                    output = g.getResult();
                } else {
                    output = "0:0";
                }
            }
        }
        return new ResponseEntity(output,HttpStatus.ACCEPTED) ;
    }

    @GetMapping(path = "isGameLive/{game_id}/{referee_name}")
    public ResponseEntity isGameLive(@PathVariable("game_id")String idGame,@PathVariable("referee_name") String refereeName)throws OnlyForReferee {
        User referee =  Controller.getInstance().getUser(refereeName);
        if(!(referee instanceof Referee)){
            throw new OnlyForReferee();
        }
        for (Game g : ((Referee)referee).getGames()) {
            if (g.getId() == Integer.parseInt(idGame)) {
                Date startTime = new Date();
                Date endTime = new Date(startTime.getTime() + 2 * (3600 * 1000));
                if (startTime.after(g.getDate()))
                    if (g.getDate().before(endTime))
                        return new ResponseEntity(true,HttpStatus.ACCEPTED) ;
            }
        }
        return new ResponseEntity(false,HttpStatus.ACCEPTED) ;
    }

    @GetMapping(path = "getEvents/{game_id}/{referee_name}")
    public ResponseEntity getEvents(@PathVariable("game_id")String gameID,@PathVariable("referee_name") String refereeName)throws OnlyForReferee {
        User referee =  Controller.getInstance().getUser(refereeName);
        if(!(referee instanceof Referee)){
            throw new OnlyForReferee();
        }
        List<String> events=new LinkedList<>();
        for (Game g : ((Referee)referee).getGames()) {
            if (g.getId() == Integer.parseInt(gameID)) {
                for(AEvent event : g.getEventLog().getEventList()){
                    events.add(event.getClass().getName()+","+"'"+event.getMinute()+","+event.getPlayerName()+","+event.getTeamName());
                }
            }
        }
        return new ResponseEntity(events,HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/postEventReport")
    public ResponseEntity postEventReport(@RequestBody Map<String,String> body) throws OnlyForReferee {
        String refereeName=body.get("user_name");
        String gameID=body.get("game_id");
        String report=body.get("report");
        User referee =  Controller.getInstance().getUser(refereeName);
        if(!(referee instanceof Referee)){
            throw new OnlyForReferee();
        }

        for (Game g : ((Referee)referee).getGames()) {
            if (g.getId() == Integer.parseInt(gameID)) {
              g.getEventLog().setReport(report);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
