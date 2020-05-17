package FootballSystem.ServiceLayer;
import FootballSystem.ServiceLayer.Exceptions.NotHisManagerException;
import FootballSystem.ServiceLayer.Exceptions.TeamIsClosedException;
import FootballSystem.System.Asset.Asset;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.Exeptions.*;
import FootballSystem.System.FootballObjects.Field;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.*;
import FootballSystem.System.*;

public class TeamOwnerController extends MainUserController implements Observable {

    //<editor-fold desc="Singleton Constructor">

    private static TeamOwnerController ourInstance = new TeamOwnerController();

    public static TeamOwnerController getInstance() {
        return ourInstance;
    }

    private TeamOwnerController() {
    }

    //</editor-fold>

    //<editor-fold desc="Methods">

    public Field createField(int id, String name){
        Field field = new Field(id, name, 0, 0);
        Controller.getInstance().addField(field);
        return field;
    }

    public void addAssetToTeam(TeamOwner teamOwner, Team team, Asset asset) throws NotHisTeamException, TeamIsClosedException, HasTeamAlreadyException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        team.addAsset(asset);
    }

    public void removeAssetFromTeam(TeamOwner teamOwner, Team team, Asset asset) throws NotHisTeamException, TeamIsClosedException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        team.removeAsset(asset);
    }

    public void editAssetOfTeam(TeamOwner teamOwner, Team team,  Asset asset, int value) throws NotHisTeamException, TeamIsClosedException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        asset.editAssetValue(value);
    }

    public void addTeamOwner(TeamOwner teamOwner, Team team, TeamOwner newTeamOwner) throws NotHisTeamException, TeamIsClosedException, AlreadyHasTeamException, AlreadyExistThisOwner, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        if(team.getAllTeamOwners().contains(newTeamOwner)){
            throw new AlreadyExistThisOwner();
        }
        teamOwner.addTeamOwner(team,newTeamOwner);
    }

    public void removeTeamOwner(TeamOwner teamOwner, TeamOwner teamOwnerToRemove, Team team) throws NotHisTeamException, TeamIsClosedException, StillNoAppointedOwner, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        teamOwner.removeTeamOwner(team,teamOwnerToRemove);
    }

    public void addTeamManager(TeamOwner teamOwner, Team team, TeamManager teamManager) throws NotHisTeamException, TeamIsClosedException, AlreadyHasTeamException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        if(!team.getAllTeamOwners().contains(teamOwner) && !team.getTeamManagersList().contains(teamManager)){
            throw new AlreadyHasTeamException();
        }
        team.addTeamManager(teamManager);
        teamManager.setMyTeamOwner(teamOwner);
    }

    public void removeTeamManager(TeamOwner teamOwner, Team team, TeamManager teamManager) throws NotHisTeamException, NotHisManagerException, TeamIsClosedException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        if(teamManager.getMyTeamOwner()!=teamOwner){
            throw new NotHisManagerException();
        }
        team.removeTeamManager(teamManager);
        teamManager.setMyTeamOwner(null);
    }

    public void closeTeam(TeamOwner teamOwner, Team team) throws NotHisTeamException, TeamIsClosedException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        team.closeTeam();
        notifyUI();
    }

    public void openTeam(TeamOwner teamOwner, Team team) throws NotHisTeamException, TeamStatusException, TeamIsClosedException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        teamOwner.restartTeam(team);
        notifyUI();
    }

    public void sumbitReport(TeamOwner teamOwner, Team team,FinancialReport report) throws TeamIsClosedException, NotHisTeamException, NotOwnerOfThisTeamException {
        this.checkInputs(teamOwner,team);
        if(report!=null)
            team.addFinancialReport(report);
    }
    //</editor-fold>

    //<editor-fold desc="Override">

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyUI() {

    }

    private void checkInputs(TeamOwner teamOwner, Team team) throws NotHisTeamException, TeamIsClosedException, NotOwnerOfThisTeamException {
        if(!teamOwner.getTeamList().contains(team)){
            throw new NotHisTeamException();
        }
        if(team.getTeamStatus()== TeamStatus.Close){
            throw new TeamIsClosedException();
        }
        if(!team.getAllTeamOwners().contains(teamOwner))
             throw new NotOwnerOfThisTeamException();
    }

    //</editor-fold>
}

