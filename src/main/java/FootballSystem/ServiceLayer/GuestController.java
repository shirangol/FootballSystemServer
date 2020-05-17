package FootballSystem.ServiceLayer;

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
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RequestMapping("/api/user")
@RestController
public class GuestController extends MainUserController {

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

    public Fan signUp(int id, String name, String password, String userName) throws UserNameAlreadyExistException {
        Controller controller = Controller.getInstance();
        Fan newFan = controller.signUp(id,name,password,userName);
        return newFan;
    }

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

    @Override
    public void logOut(User user){
        throw new UnsupportedOperationException();
    }

    @PostMapping
    public void signUp2(@RequestBody Fan fan) throws UserNameAlreadyExistException {
        Controller controller = Controller.getInstance();
        Fan newFan = controller.signUp(2,fan.getName(),fan.getPassword(),fan.getUserName());
   //     return newFan;
    }

    @GetMapping(path = "{user_Name}")
    public User login(@PathVariable("user_Name")String userName ) throws WrongPasswordException, NoSuchAUserNamedException {
        return (Controller.getInstance().login(userName,"1234"));
    }

}
