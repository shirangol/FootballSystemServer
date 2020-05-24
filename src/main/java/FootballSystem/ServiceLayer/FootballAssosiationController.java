package FootballSystem.ServiceLayer;
import FootballSystem.ServiceLayer.Exceptions.*;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Exeptions.IllegalInputException;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.UserNameAlreadyExistException;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.Controller;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.FootballObjects.Team.IScoreMethodPolicy;
import FootballSystem.System.FootballObjects.Team.ITeamAllocatePolicy;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.SystemErrorLog;
import FootballSystem.System.Users.FootballAssociation;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.Users.TeamOwner;
import FootballSystem.System.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api/FootballAssociation")
@RestController
public class FootballAssosiationController {

    private static FootballAssosiationController ourInstance = new FootballAssosiationController();

    //<editor-fold desc="Constructor">
    private FootballAssosiationController() {
    }

    ;
    //</editor-fold>

    //<editor-fold desc="Getters">
    public static FootballAssosiationController getInstance() {
        return ourInstance;
    }

    /**
     * Get all teams in the system
     *
     * @return
     */
    public List<Team> getAllTeams() {
        return Controller.getInstance().getAllTeams();
    }

    /**
     * Get all referee in the system
     *
     * @return
     */
    public List<Referee> getAllReferee() {
        return Controller.getInstance().getAllReferee();
    }


    /**
     * Get all LeagueInformation of FootballAssociation
     *
     * @param footballAssociation
     * @return
     */
    public List<LeagueInformation> getMyLeagueInformation(FootballAssociation footballAssociation) {
        List<LeagueInformation> leagueInformations = new LinkedList<>();
        for (Map.Entry l : footballAssociation.getLeagueInformations().entrySet()) {
            leagueInformations.add((LeagueInformation) l.getValue());
        }
        return leagueInformations;
    }

    /**
     * Get all games for League information
     *
     * @param leagueInformation
     * @return
     */
    public List<Game> getAllGames(LeagueInformation leagueInformation) {
        return leagueInformation.getGames();
    }

    /**
     * Get League table
     *
     * @param leagueInfo
     * @return
     */
    public HashMap<Team, Integer> getLeagueTable(LeagueInformation leagueInfo) {
        return leagueInfo.getLeagueTable();
    }

    //</editor-fold>

    //<editor-fold desc="Methods">

    /**
     * Create a new league in the system and place teams there
     *
     * @param name
     * @param teams
     * @return
     * @throws LeagueNameAlreadyExist
     */
    public League initEmptyLeague(String name, List<Team> teams) throws LeagueNameAlreadyExist {
        Controller controller = Controller.getInstance();
        if (controller.isLeagueExist(name)) {
            throw new LeagueNameAlreadyExist();
        }
        League league = new League(name, teams);
        controller.addLeague(league);
        return league;
    }

    /**
     * The function init empty LeagueInformation by linking the season and league
     * after the object is created the football association can allocate games and referees
     *
     * @param footballAssociation
     * @param league
     * @param year
     * @return
     */
    public LeagueInformation initLeague(FootballAssociation footballAssociation, League league, String year) {
        //Check if season already exist in the system
        Controller controller = Controller.getInstance();
        Season season = footballAssociation.getSeasonFromController(year);
        if (season == null) {
            season = new Season(Integer.valueOf(year));
            controller.addSeason(season);
        }

        //create empty LeagueInformation
        return footballAssociation.initLeague(season, league);
    }

    /**
     * Add a new referee to system
     *
     * @param footballAssociation
     * @param name
     * @param type
     * @param id
     * @param pass
     * @param userName
     * @return
     * @throws UserNameAlreadyExistException
     */
    public Referee addReferee(FootballAssociation footballAssociation, String name, RefereeType type, int id, String pass, String userName) throws UserNameAlreadyExistException {
        try {
            Referee referee = footballAssociation.addNewReferee(name, type, id, pass, userName);
            return referee;
        }
        catch (UserNameAlreadyExistException e){
            SystemErrorLog.getInstance().writeToLog("Type: "+(new UserNameAlreadyExistException()).toString());
            throw new UserNameAlreadyExistException();
        }
    }

    /**
     * Remove exist referee from the system
     * if there are games that are related to the referee it cannot be deleted
     *
     * @param footballAssociation
     * @param referee
     * @throws IllegalInputException
     */
    private void removeReferee(FootballAssociation footballAssociation, Referee referee) throws IllegalInputException, NoSuchAUserNamedException {
        footballAssociation.removeReferee(referee);
    }

    /**
     * Scheduling referee for LeagueInformation
     * only if exist game to LeagueInformation
     *
     * @param footballAssociation
     * @param leagueInformation
     * @param referees
     * @throws CantSchedulingRefereeWithoutGames
     */
    public void schedulingReferee(FootballAssociation footballAssociation, LeagueInformation leagueInformation, List<Referee> referees) throws CantSchedulingRefereeWithoutGames, MustHaveLeastOneMainReferee, MustHaveLeastTwoSideReferee {
        if (leagueInformation.getGames().isEmpty()) {
            throw new CantSchedulingRefereeWithoutGames();
        }
        int mainNum = 0;
        int sideNum = 0;
        for (Referee ref : referees) {
            if (ref.getRefereeType() == RefereeType.MAIN) {
                mainNum++;
            } else if (ref.getRefereeType() == RefereeType.ASSISTANT) {
                sideNum++;
            }
        }

        if (mainNum == 0) {
            SystemErrorLog.getInstance().writeToLog("Type: "+(new MustHaveLeastOneMainReferee()).toString());
            throw new MustHaveLeastOneMainReferee();
        }
        if (sideNum < 2) {
            SystemErrorLog.getInstance().writeToLog("Type: "+(new MustHaveLeastTwoSideReferee()).toString());
            throw new MustHaveLeastTwoSideReferee();
        }

        footballAssociation.schedulingReferee(leagueInformation, referees);
    }

    /**
     * Edit score policy at the beginning of the season
     *
     * @param leagueInformation
     * @param policy
     * @throws IsNotStartOFSeason
     */
    public void editScorePolicy(LeagueInformation leagueInformation, IScoreMethodPolicy policy) throws IsNotStartOFSeason {
        if (isStartOfSeason(leagueInformation)) {
            leagueInformation.editScoreSchedulingPolicy(policy);
        }
        throw new IsNotStartOFSeason();
    }

    /**
     * Edit team allocate policy at the beginning of the season
     *
     * @param leagueInformation
     * @param policy
     * @throws IsNotStartOFSeason
     */
    public void editTeamAllocatePolicy(LeagueInformation leagueInformation, ITeamAllocatePolicy policy) throws IsNotStartOFSeason {
        if (isStartOfSeason(leagueInformation)) {
            leagueInformation.editGameSchedulingPolicy(policy);
            return;
        }
        SystemErrorLog.getInstance().writeToLog("Type: "+(new IsNotStartOFSeason()).toString());
        throw new IsNotStartOFSeason();
    }

    /**
     * Scheduling games for LeagueInformation
     *
     * @param footballAssociation
     * @param leagueInformation
     */
    public void schedulingGames(FootballAssociation footballAssociation, LeagueInformation leagueInformation) throws MustHaveLeastTwoTeams {
        if (leagueInformation.getLeague().getTeams().size() < 2) {
            SystemErrorLog.getInstance().writeToLog("Type: "+(new MustHaveLeastTwoTeams()).toString());
            throw new MustHaveLeastTwoTeams();
        }
        footballAssociation.initLeagueInformation(leagueInformation);
    }

    /**
     * Replace referee from exist and delete referee user
     *
     * @param footballAssociation
     * @param leagueInformation
     * @param referees
     * @param referee
     * @throws IllegalInputException
     * @throws NoSuchAUserNamedException
     */
    private void replaceReferee(FootballAssociation footballAssociation, LeagueInformation leagueInformation, List<Referee> referees, Referee referee) throws IllegalInputException, NoSuchAUserNamedException {
        footballAssociation.manualChangingReferee(leagueInformation, referees, referee);
        footballAssociation.removeReferee(referee);//remove referee's user after chang
    }

    /**
     * Create a new team to system
     *
     * @param teamName
     * @param teamOwner
     * @return
     */
    public Team createTeam(String teamName, TeamOwner teamOwner) {
        Team team = new Team(teamName, teamOwner);
        Controller.getInstance().addTeam(team);
        teamOwner.addTeamToMyTeamList(team);
        return team;
    }

    @PostMapping(path = "/createTeam")
    public ResponseEntity createTeam(@RequestBody Map<String,String> body) {
        for (TeamOwner tO :  Controller.getInstance().getAllTeamOwner()) {
            if (tO.getUserName().equals(body.get("team_owner"))) {
                createTeam(body.get("team_name"), tO);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    /**
     * Update game result and score in league table
     *
     * @param game
     * @param homeScore
     * @param awayScore
     */
    private void updateResultToGame(Game game, int homeScore, int awayScore) {
        game.setResult(homeScore, awayScore);
    }
    //</editor-fold>

    //<editor-fold desc="Private Methods">
    private boolean isStartOfSeason(LeagueInformation leagueInformation) {
        if (leagueInformation.getGames().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Get all leagues in the system
     *
     * @return
     */

    @GetMapping(path = "/getAllLeague")
    public ResponseEntity getAllLeague() {
        List<String> list = new ArrayList<>();
        for (League l : Controller.getInstance().getAllLeagues()) {
            list.add(l.getName());
        }
        return new ResponseEntity(list, HttpStatus.ACCEPTED) ;
    }

    /**
     * get string list of the policies
     *
     * @return
     */
    @GetMapping(path = "/getScorePolicy")
    public ResponseEntity getScorePolicy() {
        return new ResponseEntity(Controller.getInstance().getScorePoliciesString(),HttpStatus.ACCEPTED) ;
    }

    /**
     * get string list of the policies
     *
     * @return
     */
    @GetMapping(path = "/getAllocatePolicy")
    public ResponseEntity getAllocatePolicy()
    {
        return new ResponseEntity(Controller.getInstance().getMethodAllocatePoliciesString(),HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/getAllTeamOwner")
    public ResponseEntity getAllTeamOwner() {
        List<String> list = new ArrayList<>();
        for (TeamOwner t : Controller.getInstance().getAllTeamOwner()) {
            list.add(t.getUserName());
        }
        return new ResponseEntity(list,HttpStatus.ACCEPTED);
    }

    /**
     * get all league information by League name
     *
     * @param leagueName to search
     * @return
     */
    @GetMapping(path = "/getLeagueInformation/{league_name}")
    public ResponseEntity getLeagueInformation(@PathVariable("league_name") String leagueName) {
        List<String> list = new ArrayList<>();
        for (League l : Controller.getInstance().getAllLeagues()) {
            if (l.getName().equals(leagueName)) {
                for (LeagueInformation leagueInfo : l.getLeagueInformation()) {
                    list.add(leagueInfo.getSeason().getYear());
                }
            }
        }
        return new ResponseEntity(list,HttpStatus.ACCEPTED);
    }

    /**
     * edit the league policy (use case)

     */
    @PostMapping(value = "/editLeaguePolicy")
    public ResponseEntity editLeaguePolicy(@RequestBody Map<String,String> body) {
        String league= body.get("league_name");
        String season= body.get("season_year");
        String scoreMethodPolicy= body.get("scoreMethodPolicy");
        String schedulingPolicy=body.get("schedulingPolicy");
        for (League l : Controller.getInstance().getAllLeagues()) {
            if (l.getName().equals(league)) {
                for (LeagueInformation leagueInfo : l.getLeagueInformation()) {
                    if (leagueInfo.getSeason().getYear().equals(season)) {
                        leagueInfo.editGameSchedulingPolicy(Controller.getInstance().getMethodAllocatePolicies().get(schedulingPolicy));
                        leagueInfo.editScoreSchedulingPolicy(Controller.getInstance().getScorePolicies().get(scoreMethodPolicy));
//                        SystemEventLog.getInstance().writeToLog("Policy of the league "+league +" in season "+season +" was changed by "+  );
                        break;
                    }
                }
            }

        }
        //</editor-fold>
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
