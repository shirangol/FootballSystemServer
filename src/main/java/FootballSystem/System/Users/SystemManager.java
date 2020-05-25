package FootballSystem.System.Users;

import FootballSystem.DataAccess.UserSQL;
import FootballSystem.System.Controller;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.UserNameAlreadyExistException;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.I_Observer.IObserverTeam;
import FootballSystem.System.I_Observer.ISubjectTeam;
import FootballSystem.System.SystemEventLog;
import FootballSystem.System.Report;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SystemManager extends User implements IObserverTeam {

    //<editor-fold desc="Fields">
    private static HashMap<Integer,Report> openReportsBox = new HashMap<Integer, Report>();
    private static HashMap<Integer,Report> closeReportsBox = new HashMap<Integer, Report>();
    private static List<ISubjectTeam> observerTeams = new LinkedList<>();
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public SystemManager(@JsonProperty("id")int id, @JsonProperty("name")String name,@JsonProperty("password") String password, @JsonProperty("userName")String userName){
        super(id,name,password,userName);
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public HashMap<Integer, Report> getOpenReports() {
        return openReportsBox;
    }
    public HashMap<Integer, Report> getCloseReports() { return closeReportsBox;}

    /**
     * present the log file
     * @return
     */
    public String getLog() throws IOException {
        SystemEventLog.getInstance().writeToLog("Log introduced by the FootballSystem.System Manager. id:"+getId());
        return SystemEventLog.getInstance().getLog();

    } //UC-28
    //</editor-fold>

    //<editor-fold desc="Setters">

    private static void setOpenReportsBox(HashMap<Integer, Report> openReportsBox) {
        SystemManager.openReportsBox = openReportsBox;
    }

    private static void setCloseReportsBox(HashMap<Integer, Report> closeReportsBox) {
        SystemManager.closeReportsBox = closeReportsBox;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * close team
     * @param team
     */
    public void closeTeam(Team team){
        team.PermanentlyCloseTeam();
        SystemEventLog.getInstance().writeToLog("The team: "+team.getName()+",Id: " +team.getId()+" closed successfully.");
    } //UC-25

    /**
     * FootballSystem.System manager command to remove system by the conroller
     * @param userName to remove
     * @throws NoSuchAUserNamedException
     */
    public void removeUser(String userName) throws NoSuchAUserNamedException {
        Controller.getInstance().removeUser(userName);

    } //UC-26//

    /**
     * Restart a user
     * @param userName
     * @throws NoSuchAUserNamedException
     */
    public void restartRemovedUser(String userName) throws NoSuchAUserNamedException{
        Controller.getInstance().restartRemoveUser(userName);
    }

    /**
     * Adding a new report to reportsHash
     * @param report
     */
    public static void addReport(Report report) {
        openReportsBox.put(report.getId(),report);
        SystemEventLog.getInstance().writeToLog("A new report added to the FootballSystem.System. id("+report.getId()+").");
    }

    /**
     * Answer to report
     * @param report
     */
    public void answerReport(Report report, String answer){
        report.answer(answer);
        closeReportsBox.put(report.getId(),report);
        openReportsBox.remove(report.getId());
        SystemEventLog.getInstance().writeToLog("A answer to report set. ("+report.getId()+").");
    }

    /**
     * create new team and add it to the user's system. register all the system managers by this new team
     * @param name
     */
    public void createTeam(String name,TeamOwner teamOwner){
        Team newTeam = new Team(name,teamOwner);
        for(SystemManager s:Controller.getInstance().getAllSystemManager()){
            newTeam.registerSystemManagerToAlert(s);
        }
        Controller.getInstance().addTeam(newTeam);
        SystemEventLog.getInstance().writeToLog("New team added to the system. ("+newTeam.getId()+", "+newTeam.getName()+")");
    }

    //<editor-fold desc="Create Users">
    /**
     * Create a new Player
     * @param id
     * @param name
     * @param password
     * @param userName
     * @param birthDate
     * @param role
     * @param assetValue
     * @param salary
     * @return
     * @throws UserNameAlreadyExistException
     */
    public Player createNewPlayer(int id, String name, String password, String userName, Date birthDate, String role, int assetValue, int salary) throws UserNameAlreadyExistException {
        if(Controller.getInstance().isUserNameExist(userName)){
            throw new UserNameAlreadyExistException();
        }
        Player user=new Player(id,name,password,userName,birthDate,role,assetValue,salary);
        Controller.getInstance().addUser(userName,user);
        SystemEventLog.getInstance().writeToLog("Add a new Player : "+ user.getUserName());
        return user;
    }

    /**
     * create new referee
     * @param id
     * @param name
     * @param password
     * @param userName
     * @param refereeType
     * @return
     * @throws UserNameAlreadyExistException
     */
    public Referee createNewReferee(int id, String name, String password, String userName, RefereeType refereeType) throws UserNameAlreadyExistException {
        if(Controller.getInstance().isUserNameExist(userName)){
            throw new UserNameAlreadyExistException();
        }
        Referee user=new Referee(name,refereeType,id,password,userName);
//        UserSQL.getInstance().save(user);
        Controller.getInstance().addUser(userName,user);
        SystemEventLog.getInstance().writeToLog("Add a new Referee : "+ user.getUserName());
        return user;
    }

    /**
     * create new football Association user
     * @param id
     * @param name
     * @param password
     * @param userName
     * @return
     * @throws UserNameAlreadyExistException
     */
    public FootballAssociation createNewFootballAssociation(int id, String name, String password, String userName) throws UserNameAlreadyExistException {
        if(Controller.getInstance().isUserNameExist(userName)){
            throw new UserNameAlreadyExistException();
        }
        FootballAssociation user=new FootballAssociation(id,name,password,userName);
//        UserSQL.getInstance().save(user);
        Controller.getInstance().addUser(userName,user);
        SystemEventLog.getInstance().writeToLog("Add a new Football Association : "+ user.getUserName());
        return user;
    }

    /**
     * Create a new coach
     * @param id
     * @param name
     * @param password
     * @param userName
     * @param preparation
     * @param role
     * @param assetValue
     * @param salary
     * @return
     * @throws UserNameAlreadyExistException
     */
    public Coach createNewCoach(int id, String name, String password, String userName, String preparation, String role, int assetValue, int salary) throws UserNameAlreadyExistException {
        if(Controller.getInstance().isUserNameExist(userName)){
            throw new UserNameAlreadyExistException();
        }
        Coach user=new Coach(id,name,password,userName,preparation,role,assetValue,salary);
        Controller.getInstance().addUser(userName,user);
        SystemEventLog.getInstance().writeToLog("Add a new Coach : " + user.getUserName());
        return user;
    }
    /**
     * Create a new fan
     * @param id
     * @param name
     * @param password
     * @param userName
     * @return
     * @throws UserNameAlreadyExistException
     */
    public Fan createNewFan(int id, String name, String password, String userName) throws UserNameAlreadyExistException {
        return Controller.getInstance().signUp(id,name,password,userName);
    }

    /**
     * Create a new team manager
     * @param id
     * @param name
     * @param password
     * @param userName
     * @param assetValue
     * @param salary
     * @return
     * @throws UserNameAlreadyExistException
     */
    public TeamManager createNewTeamManager(int id, String name, String password, String userName, int assetValue, int salary) throws UserNameAlreadyExistException {
        if(Controller.getInstance().isUserNameExist(userName)){
            throw new UserNameAlreadyExistException();
        }
        TeamManager user=new TeamManager(id,name,password,userName,assetValue,salary);
        Controller.getInstance().addUser(userName,user);
        SystemEventLog.getInstance().writeToLog("Add a new TeamManager : " + user.getUserName());

        return user;
    }

    /**
     * create new team owner
     * @param id
     * @param name
     * @param password
     * @param userName
     * @param salary
     * @return
     * @throws UserNameAlreadyExistException
     */
    public TeamOwner createNewTeamOwner(int id, String name, String password, String userName,int salary) throws UserNameAlreadyExistException {
        if(Controller.getInstance().isUserNameExist(userName)){
            throw new UserNameAlreadyExistException();
        }
        TeamOwner user=new TeamOwner(id,name,password,userName,salary);
        Controller.getInstance().addUser(userName,user);
        SystemEventLog.getInstance().writeToLog("Add a new TeamOwner : " + user.getUserName());
        return user;
        }

    /**
     * Create a new system manager
     * @param id
     * @param name
     * @param password
     * @param userName
     * @return
     * @throws UserNameAlreadyExistException
     */
    public SystemManager createNewSystemManager(int id, String name, String password, String userName) throws UserNameAlreadyExistException {
        if (Controller.getInstance().isUserNameExist(userName)) {
            throw new UserNameAlreadyExistException();
        }
        SystemManager user = new SystemManager(id, name, password, userName);
        Controller.getInstance().addUser(userName, user);
        SystemEventLog.getInstance().writeToLog("Add a new SystemManager : " + user.getUserName());
        return user;
    }
    @Override
    public void update(String msg) {
        SystemEventLog.getInstance().writeToLog("FootballSystem.System Manager was updated by a team. about the messge:"+msg+" id's SystemManager:"+getId());

    }

    @Override
    public void registerAlert(ISubjectTeam iSubjectTeam) {
        observerTeams.add(iSubjectTeam);

    }

    @Override
    public void removeAlert(ISubjectTeam iSubjectTeam) {
        observerTeams.remove(iSubjectTeam);

    }

    @Override
    public void removeUser() {

    }
    //</editor-fold>
    //</editor-fold>

}
