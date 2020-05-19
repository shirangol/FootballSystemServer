package FootballSystem.ServiceLayer;
import FootballSystem.System.*;
import FootballSystem.ServiceLayer.Exceptions.TeamHasAFutureGame;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.UserNameAlreadyExistException;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.*;

import java.io.IOException;
import java.util.*;

public class SystemManagerController {


    private static SystemManagerController ourInstance = new SystemManagerController();

    public static SystemManagerController getInstance() {
        return ourInstance;
    }

    private SystemManagerController() {
    }

    //<editor-fold desc="Getters">
    /**
     * Get all teams in the system
     * @return
     */
    public List<Team> getAllTeams(){
        Controller controller= Controller.getInstance();
        return  controller.getAllTeams();
    }

    /**
     * Gets all users in the system
     * @return
     */
    public List<User> getAllUsers(){
        Controller controller= Controller.getInstance();
        List<User> users= new LinkedList<>();
        for(Map.Entry user: controller.getUsers().entrySet()){
            users.add((User)user.getValue());
        }
        return users;
    }

    /**
     * Get all inactive users in the system
     * @return
     */
    public List<User> getAllInactiveUsers(){
        List<User> users= new LinkedList<>();
        for(Map.Entry user: Controller.getInstance().getRemovedUsers().entrySet()){
            users.add((User)user.getValue());
        }
        return users;
    }

    /**
     * Gets all open reports in system
     * @return
     */
    public HashMap<Integer, Report> getAllOpenReports(SystemManager systemManager){

        return systemManager.getOpenReports();
    }

    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * Permanently close a team
     * @param systemManager
     * @param team
     */
    public void permanentlyCloseTeam(SystemManager systemManager, Team team) throws TeamHasAFutureGame {
        if(!isAFutureGame(team)){
            throw new TeamHasAFutureGame();
        }
        systemManager.closeTeam(team);
    }

    public void restartUser(SystemManager systemManager, User user) throws NoSuchAUserNamedException {
        systemManager.restartRemovedUser(user.getUserName());
    }
    /**
     * Removes user from the system
     * @param systemManager
     * @param user
     */
    public void removeUser(SystemManager systemManager,User user) throws NoSuchAUserNamedException {
            systemManager.removeUser(user.getUserName());

    }

    /**
     * Watch an open report details
     * @param report
     * @return
     */
    public String showReport(Report report){
        return report.getReportDetails();
    }
    /**
     * Answer report
     * @param systemManager
     * @param report
     * @param answer
     */
    public void answerReport(SystemManager systemManager, Report report, String answer){
        systemManager.answerReport(report,answer);
    }

    /**
     * Watch system log details
     * @param systemManager
     * @return
     * @throws IOException
     */
    public String showLog(SystemManager systemManager) throws IOException {
        return systemManager.getLog();
    }

    public void activeRecommendationSystem(){
        //?????????????
    }

    //<editor-fold desc="Create Users">
    public Player createNewPlayer(SystemManager systemManager,int id, String name, String password, String userName, Date birthDate, String role, int assetValue, int salary) throws UserNameAlreadyExistException {
        return systemManager.createNewPlayer(id,name,password,userName,birthDate,role,assetValue,salary);
    }
    public Coach createNewCoach(SystemManager systemManager,int id, String name, String password, String userName, String preparation, String role, int assetValue, int salary) throws UserNameAlreadyExistException {
        return systemManager.createNewCoach(id,name,password,userName,preparation,role,assetValue,salary);
    }
    public Fan createNewFan(SystemManager systemManager, int id, String name, String password, String userName) throws UserNameAlreadyExistException {
        return systemManager.createNewFan(id,name,password,userName);
    }


    public Referee createNewReferee(SystemManager systemManager, int id, String name, String password, String userName, RefereeType refereeType) throws UserNameAlreadyExistException
    { return systemManager.createNewReferee(id,  name,  password,  userName,refereeType);
    }

    public FootballAssociation createNewFootballAssociation(SystemManager systemManager, int id, String name, String password, String userName)throws UserNameAlreadyExistException {
        return systemManager.createNewFootballAssociation(id,name,password,userName);}

    public TeamManager createNewTeamManager(SystemManager systemManager,int id, String name, String password, String userName, int assetValue, int salary) throws UserNameAlreadyExistException {
        return systemManager.createNewTeamManager(id,name,password,userName,assetValue,salary);
    }
    public TeamOwner createNewTeamOwner(SystemManager systemManager,int id, String name, String password, String userName, int assetValue, int salary) throws UserNameAlreadyExistException{
        return systemManager.createNewTeamOwner(id,name,password,userName,salary);}

    public SystemManager createNewSystemManager(SystemManager systemManager,int id, String name, String password, String userName) throws UserNameAlreadyExistException {
        return systemManager.createNewSystemManager(id,name,password,userName);
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="Private Methods">
    /**
     * Checks if has a future game to team
     * @param team
     * @return
     */
    private boolean isAFutureGame(Team team){
        return team.getFutureGames().isEmpty();
    }
    //</editor-fold>

}

