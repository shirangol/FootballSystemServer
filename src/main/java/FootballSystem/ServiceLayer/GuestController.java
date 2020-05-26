package FootballSystem.ServiceLayer;

import FootballSystem.ServiceLayer.Exceptions.OnlyForReferee;
import FootballSystem.System.*;
import FootballSystem.System.Enum.SearchCategory;
import FootballSystem.System.Exeptions.UserNameAlreadyExistException;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.WrongPasswordException;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Searcher.ASearchStrategy;
import FootballSystem.System.Users.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/user")
@RestController
public class GuestController extends MainUserController {




    public List<IShowable> getInfoToShow(String name){
        List<IShowable> result = new LinkedList<IShowable>();
        switch (name){
            case "Player":{
                for(Player player : Controller.getInstance().getAllPlayers()){
                    result.add(player);
                }
            }
            case "Coach":{
                for(Coach coach : Controller.getInstance().getAllCoach()){
                    result.add(coach);
                }
            }
            case "Team":{
                for(Team team : Controller.getInstance().getAllTeams()){
                    result.add(team);
                }
            }
            case "League":{
                for(League league : Controller.getInstance().getAllLeagues()){
                    result.add(league);
                }
            }
            case "Season":{
                for(Season season : Controller.getInstance().getAllSeasons()){
                    result.add(season);
                }
            }
            case "Referee":{
                for(Referee referee : Controller.getInstance().getAllReferee()){
                    result.add(referee);
                }
            }
        }
        return result;
    }

    public List<IShowable> searchShowables(User user, ASearchStrategy aSearchStrategy, SearchCategory searchCategory, String query){
        List<IShowable> results = aSearchStrategy.search(searchCategory,query);
        user.addSearchHistory(query);
        return results;
    }

    public List<IShowable> filterResults(ASearchStrategy aSearchStrategy, SearchCategory searchCategory, List<IShowable> iShowableList){
        List<IShowable> results = aSearchStrategy.filter(iShowableList, searchCategory);
        return results;
    }


    //Sign-Up for fan only
    @PostMapping (value = "/signUp")
    public ResponseEntity signUp(@RequestBody Map<String,String> body ) throws UserNameAlreadyExistException{
        try {
            Controller controller = Controller.getInstance();
            controller.signUp(Integer.parseInt(body.get("id")), body.get("name"), body.get("password"), body.get("user_name"));
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        catch (UserNameAlreadyExistException u){
            SystemErrorLog.getInstance().writeToLog("Type: "+(new UserNameAlreadyExistException()).toString());
            throw new UserNameAlreadyExistException();
        }
    }

    //Login
    @PostMapping (value = "/login")
    public ResponseEntity login( @RequestBody Map<String,String> body, HttpServletRequest request ) throws WrongPasswordException, NoSuchAUserNamedException {
        try {
            int type = getUserType(body.get("user_name"), body.get("password"));
            return new ResponseEntity(type, HttpStatus.ACCEPTED);
        }
        catch (WrongPasswordException w){
            SystemErrorLog.getInstance().writeToLog("Type: "+(new WrongPasswordException()).toString());
            throw new WrongPasswordException();
        }
        catch (NoSuchAUserNamedException w){
            SystemErrorLog.getInstance().writeToLog("Type: "+(new NoSuchAUserNamedException()).toString());
            throw new NoSuchAUserNamedException();
        }
    }

    //Log-Out
    @GetMapping(path = "logOut/{user_name}")
    public ResponseEntity logOut(@PathVariable("user_name")String userName){
        Controller controller = Controller.getInstance();
        User user= controller.getUser(userName);
        controller.logOut(user);
        if(user instanceof Fan){
            ObserverController.removeOnlineUser(userName);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public int getUserType(String userName , String password) throws WrongPasswordException, NoSuchAUserNamedException {
        Controller controller = Controller.getInstance();
        User existUser = controller.login(userName,password);
        if(existUser instanceof Fan){
            return 1;
        }
        if(existUser instanceof Referee){
            return 2;
        }
        if(existUser instanceof Coach){
            return 3;
        }
        if(existUser instanceof Player){
            return 4;
        }
        if(existUser instanceof FootballAssociation){
            return 5;
        }
        if(existUser instanceof SystemManager){
            return 6;
        }
        if(existUser instanceof TeamOwner){
            return 7;
        }
        if(existUser instanceof TeamManager){
            return 8;
        }
        return 0;

    }
}
