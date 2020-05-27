package FootballSystem.System.Users;

import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.Controller;
import FootballSystem.System.BudgetRules;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.SystemEventLog;
import FootballSystem.System.Exeptions.*;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.FinancialReport;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class FootballAssociation extends User {

    //<editor-fold desc="Fields">
    private HashMap<Integer,LeagueInformation> leaguesInformation;
    private BudgetRules budgetRules;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public FootballAssociation(@JsonProperty("id")int id, @JsonProperty("name")String name,@JsonProperty("password") String password, @JsonProperty("userName")String userName) {
        super(id,name,password,userName);
        leaguesInformation=new HashMap<>();
    }
    public FootballAssociation(@JsonProperty("id")int id, @JsonProperty("name")String name,@JsonProperty("password") String password, @JsonProperty("userName")String userName, LeagueInformation leagueInformation) {
        super(id,name,password,userName);
        leaguesInformation=new HashMap<>();
        this.leaguesInformation.put(leagueInformation.getId(),leagueInformation);
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public Season getSeasonFromController(String year){
        return Controller.getInstance().getSeason(year);
    }

    public HashMap<Integer, LeagueInformation> getLeagueInformations() {
        return leaguesInformation;
    }

    /**
     * financial report by the order of the association football
     * @param team to get financial report about it
     * @return
     */
    public List<FinancialReport> getFinancialReport(Team team){
        SystemEventLog.getInstance().writeToLog("The football association representative got financial report about the team:"+team.getName()+" id's representative:"+getId());
        return team.getFinancialReport();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * Init new League
     * @param season
     * @param league
     */
    //UC-29
    public LeagueInformation initLeague(Season season, League league) {
        //init League Information with league, season from service Layer
        LeagueInformation leagueInformation= new LeagueInformation(league,season, this);
        //add new leagueInformation to list
        leaguesInformation.put(leagueInformation.getId(), leagueInformation);

        //update pointers
        season.addLeagueInformation(leagueInformation);
        league.addLeagueInformation(leagueInformation);

        //add to log
        SystemEventLog.getInstance().writeToLog("Football assosiation -Init new League. Name:"+ league.getName());

        //return to service Layer
        return leagueInformation;
    } //UC-29

    /**
     * init leagueInformation policy-  Team Allocate Policy AND Score Method Policy.
     * @param leagueInformation
     */
    public void initLeagueInformation(LeagueInformation leagueInformation){
        leagueInformation.initLeagueInformation();
    }

    /**
     * init scheduling Referee for league Information.
     * MUST USE THIS FUNCTION AFTER  initLeagueInformation!!! (schedulingReferee need that list of game dont be empty)
     * @param leagueInformation
     * @param referees
     */
    public void schedulingReferee(LeagueInformation leagueInformation, List<Referee> referees){
        leagueInformation.schedulingReferee(referees);
    }

    /**
     *  Add New Referee
     * @param name
     * @param type
     * @param id
     * @param pass
     * @param userName
     * @throws UserNameAlreadyExistException
     */
    //UC-30
    public Referee addNewReferee(String name, RefereeType type, int id, String pass, String userName)  throws UserNameAlreadyExistException {
        Referee referee= new Referee(name,type,id,pass,userName);
        Controller controller= Controller.getInstance();
        for (HashMap.Entry me : controller.getUsers().entrySet()) {
            if(me.getKey().equals(userName)){
                throw new UserNameAlreadyExistException();
            }
        }
        controller.addUser(userName,referee);
        SystemEventLog.getInstance().writeToLog("Football assosiation -Add new referee. id: "+referee.getId()+ "name: "+ referee.getName());
        return referee;

    }   //UC-30

    /**
     * Remove referee- Before deleting,we check the referee has some games to judge.
     * If so we will use the function: 'manuallChangingReferee' to manuall Changing Referee.
     * @param referee
     * @throws IllegalInputException
     */
    //UC-31
    public void removeReferee(Referee referee) throws IllegalInputException, NoSuchAUserNamedException {
        Controller controller = Controller.getInstance();

        if(!(controller.getAllReferee().contains(referee))){
            throw new IllegalInputException();
        }
        String userName = referee.getUserName();
        for(Game g:referee.getGames()) {
            referee.removeAlert(g);
        }

        if (referee.getFutureGames().size() > 0) {
            throw new IllegalInputException();
        }
            controller.removeUser(userName);

        SystemEventLog.getInstance().writeToLog("Football assosiation -remove referee. id: "+referee.getId()+ "name: "+ referee.getName());
    }

    /**
     * Manually swapping all games that the referee we want to delete should be judged in the future
     * @param leagueInformation
     * @param referees
     * @param referee
     */
    public void manualChangingReferee(LeagueInformation leagueInformation, List<Referee> referees, Referee referee){
        for(Game game:leagueInformation.getGames()){
            // If the referee is not in the specific game
            if(game.getMainReferee().getId()!=referee.getId() && game.getAssistantRefereeOne().getId()!=referee.getId() && game.getAssistantRefereeTwo().getId()!=referee.getId() ){
                continue;
            }
            for(Referee newReferee:referees){
                if(newReferee.equals(referee)){//skip the old referee!
                    continue;
                }

                if(newReferee.getRefereeType().equals(referee.getRefereeType())){
                    if(referee.getRefereeType()==RefereeType.MAIN){
                        game.setMainReferee(newReferee);
                        newReferee.addGame(game);
                        referee.removeGame(game);
                        break;

                    }
                    else{
                        if(referee.getId()==game.getAssistantRefereeOne().getId() && game.getAssistantRefereeTwo().getId()!=newReferee.getId()) {
                            game.setAssistantRefereeOne(newReferee);
                            newReferee.addGame(game);
                            referee.removeGame(game);
                            break;

                        }
                        else if(referee.getId()==game.getAssistantRefereeTwo().getId()&& game.getAssistantRefereeOne().getId()!=newReferee.getId()){
                            game.setAssistantRefereeTwo(newReferee);
                            newReferee.addGame(game);
                            referee.removeGame(game);
                            break;

                        }
                    }
                }
            }

        }


    }
    //UC-31

    public void addSeason(int year) {
        Season season = new Season(year);
        Controller.getInstance().addSeason(season);
    }
    //</editor-fold>

    //<editor-fold desc="Override Methods">
    @Override
    public void removeUser() {

    }
    //</editor-fold>

    //public void addBudgetRule(String rule){} //UC-33



}