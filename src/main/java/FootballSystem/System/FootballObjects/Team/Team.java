package FootballSystem.System.FootballObjects.Team;

import FootballSystem.DataAccess.TeamSQL;
import FootballSystem.System.Asset.Asset;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.Exeptions.HasTeamAlreadyException;
import FootballSystem.System.Exeptions.PersonalPageAlreadyExist;
import FootballSystem.System.FootballObjects.Field;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.I_Observer.IObserverTeam;
import FootballSystem.System.I_Observer.ISubjectTeam;
import FootballSystem.System.PersonalPages.IPageAvailable;
import FootballSystem.System.PersonalPages.PersonalPage;
import FootballSystem.System.Users.*;
import FootballSystem.System.FinancialReport;
import FootballSystem.System.IShowable;
import FootballSystem.System.SystemEventLog;

import java.util.*;

public class Team implements IPageAvailable, ISubjectTeam, IShowable {

    //<editor-fold desc="Fields">
    private static int ID;
    private int id;
    private String name;
    private TeamStatus teamStatus;
    private PersonalPage personalPage;
    private List<Asset> assets;
    private List<Game> gamesOfTeams;
    private List<TeamOwner> allTeamOwners;
    private List<TeamManager> teamManagersList;
    private Field field;
    private List<IObserverTeam> iObserverTeamListForSystemManagers;
    private List<IObserverTeam> iObserverTeamListForTeamOwnersAndManagers;
    private HashMap<TeamOwner,LinkedList<TeamOwner>> teamOwners;
    private int income;
    private int expense;
    private List<FinancialReport> financialReport;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    /**
     * Initialize variables
     * @param name,teamOwner

     */
    public Team(String name,TeamOwner teamOwner) {
        ID= TeamSQL.getInstance().getTableSize()+1;
        id=ID;
//        ID++;
        this.name = name;
        this.field = null;
        this.teamStatus = TeamStatus.Active;
        this.personalPage = null;
        this.assets = new LinkedList<>();
        this.teamManagersList = new LinkedList<>();
        this.teamOwners = new HashMap<>();
        teamOwners.put(teamOwner,new LinkedList<TeamOwner>());
        this.allTeamOwners=new LinkedList<>();
        if(teamOwner != null) {
            allTeamOwners.add(teamOwner);
            teamOwner.addTeamToMyTeamList(this);
        }
        this.financialReport = new LinkedList<FinancialReport>();
        this.iObserverTeamListForSystemManagers=new LinkedList<>();
        this.iObserverTeamListForTeamOwnersAndManagers=new LinkedList<>();
        this.gamesOfTeams= new ArrayList<>();
        this.financialReport = new LinkedList<>();
    }

    public Team( int teamID, String name, TeamStatus status, Field field, PersonalPage pPersonalPage, int income, int expense){
        id=teamID;
        this.name=name;
        this.teamStatus=status;
        this.field=field;
        this.personalPage=pPersonalPage;
        this.income=income;
        this.expense=expense;

        this.assets = new LinkedList<>();
        this.teamManagersList = new LinkedList<>();
        this.teamOwners = new HashMap<>();
//        teamOwners.put(teamOwner,new LinkedList<TeamOwner>());
//        this.allTeamOwners=new LinkedList<>();
//        if(teamOwner != null) {
//            allTeamOwners.add(teamOwner);
//            teamOwner.addTeamToMyTeamList(this);
//        }
        this.financialReport = new LinkedList<FinancialReport>();
        this.iObserverTeamListForSystemManagers=new LinkedList<>();
        this.iObserverTeamListForTeamOwnersAndManagers=new LinkedList<>();
        this.gamesOfTeams= new ArrayList<>();
        this.financialReport = new LinkedList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "Team";
    }

    /**
     * @return the team details
     */
    @Override
    public String getDetails() {
        String str = "@name:"+name;
        return str;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public List<TeamManager> getTeamManagersList() {
        return teamManagersList;
    }

    public HashMap<TeamOwner, LinkedList<TeamOwner>> getListTeamOwnersWhichappointed() {
        return teamOwners;
    }

    public LinkedList<TeamOwner> getTeamOwnerListOfThisOwner(TeamOwner appointedTeamOwner){
        LinkedList<TeamOwner> res=this.teamOwners.get(appointedTeamOwner);
        if(res!=null) {
            return res;
        }
        return res = new LinkedList<TeamOwner>();
    }

    public Field getField() {
        return field;
    }

    public int getIncome() {
        return income;
    }

    public int getExpense() {
        int sum =0;
        sum+=getPaymentSalary();
        sum+=field.getMaintenanceCost();

        return expense;
    }

    public List<FinancialReport> getFinancialReport() {
        return financialReport;
    }

    /**
     * This function summarize all the payments for each asset it's owns
     * @return the total amount the team should pay
     */
    public int getPaymentSalary() {
        int sum=0;
        for(Asset a : assets){
            sum+=a.getSalary();
        }
        return sum;
    }

    public TeamStatus getTeamStatus() {
        return teamStatus;
    }

    public PersonalPage getPersonalPage() {
        return personalPage;
    }

    public List<Game> getGamesOfTeams(){
        return gamesOfTeams;
    }

    public List<Game> getFutureGames(){
        List<Game> futureGames = new ArrayList<>();
        for(Game game:gamesOfTeams){
            long diffHours =  (new Date(System.currentTimeMillis()).getTime()-game.getDate().getTime() ) / (60 * 60 * 1000);
            if(diffHours<=0){
                futureGames.add(game);
            }
        }
        Collections.sort(futureGames, new Comparator<Game>() {
            public int compare(Game o1, Game o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        SystemEventLog.getInstance().writeToLog("The Team "+getName()+"id: "+getId() +" pull his future games.");
        return futureGames;
    }

    public List<TeamOwner> getAllTeamOwners() {
        return allTeamOwners;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamStatus(TeamStatus teamStatus) {
        this.teamStatus = teamStatus;
    }

    public void setPersonalPage(PersonalPage personalPage) {
        this.personalPage = personalPage;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public void setTeamManagersList(List<TeamManager> teamManagersList) {
        this.teamManagersList = teamManagersList;
    }

    public void setTeamOwnersWhichappointed(HashMap<TeamOwner, LinkedList<TeamOwner>> teamOwnersWhichappointed) {
        this.teamOwners = teamOwnersWhichappointed;
    }

    public void setField(Field field) {
        this.field = field;
        field.addMyTeam(this);
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public void setListOfOwnersWhichIappoint(TeamOwner teamOwner,LinkedList<TeamOwner>newList){
        this.teamOwners.put(teamOwner,newList);
    }

    public void setAllTeamOwners(List<TeamOwner> allTeamOwners) {
        this.allTeamOwners = allTeamOwners;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     *
     * @param asset
     * add a new asset to the team assets
     * according to UC-15
     *
     */
    public void addAsset(Asset asset) throws HasTeamAlreadyException {
        if(asset.getMyTeam()==null && asset!=null && !this.assets.contains(asset)) {
            this.assets.add(asset);
            asset.addMyTeam(this);//connect this team to the asset
            SystemEventLog.getInstance().writeToLog("New asset wae added to team :"+this.getName()+" , id :"+this.getId()+"Asset details : "+ asset.getDetails());
        }
    } //UC-15

    /**
     *
     * @param asset
     * remove asset from the team assets
     * according to UC-16
     *
     */
    public void removeAsset(Asset asset){
        if(asset!=null){
            this.assets.remove(asset);
            asset.resetMyTeam();//delete this team from the asset
            SystemEventLog.getInstance().writeToLog("Asset details : "+ asset.getDetails()+"was remove from team :"+this.getName()+" , id :"+this.getId());

        }
    } //UC-16

    /**
     * This function edit the value of the asset which we get as a parameter
     * @param asset
     * @param val
     */
    public void editAsset(Asset asset ,int val){
        asset.editAssetValue(val);
        SystemEventLog.getInstance().writeToLog("Asset details : "+ asset.getDetails()+"was update value.");

    } //UC-17

    public void addIncome(int income){
        this.income+=income;
        SystemEventLog.getInstance().writeToLog("A new income added to the incomes of the team id: "+ getId());

    }

    /**
     *Team can have more then one manager, this function add a new manager to the list of the managers
     * @param teamManagerNew
     * according to UC-20
     */
    public void addTeamManager(TeamManager teamManagerNew){
        if(!this.teamManagersList.contains(teamManagerNew)) {
            this.teamManagersList.add(teamManagerNew);
            teamManagerNew.setMyTeam(this);
            SystemEventLog.getInstance().writeToLog("Team manager : " + teamManagerNew.getName() + ", id :" + teamManagerNew.getId());
        }
    } //UC-20

    /**
     *Team can have more then one manager, this function remove manager from the list of the managers
     * @param teamManager
     * according to UC-21
     */
    public void removeTeamManager(TeamManager teamManager){
        this.teamManagersList.remove(teamManager);
        teamManager.setMyTeam(null);
    } //UC-21

    /**
     * Close team - this function update ths team's status to "close" and notify about the closing to the stakeholder
     * according to UC-22
     */
    public void closeTeam(){
        this.setTeamStatus(TeamStatus.Close);
        for (Asset a:assets)
            a.resetMyTeam();
        for(TeamManager m:teamManagersList)
            m.resetMyTeam();this.teamOwners.clear();
        this.teamManagersList.clear();
        if(field!=null)
         field.resetMyTeam(this);

        for(IObserverTeam ioT:iObserverTeamListForSystemManagers){
            ioT.removeAlert(this);
        }
        iObserverTeamListForSystemManagers=new LinkedList<>();

        notifySystemManager("Team Closed :"+ getName());
        notifyTeamOwnersAndManager("Team Closed :"+ getName());//needs another notify for this

    } //UC-22

    /**
     * Close team permanently
     * This function update ths team's status to "Permanently Close" and notify about the closing to the stakeholder
     */
    public void PermanentlyCloseTeam(){
        this.setTeamStatus(TeamStatus.PermanentlyClose);
        for (Asset a:assets)
            a.resetMyTeam();
        for (TeamOwner o:allTeamOwners)
            o.removeTeamFromMyList(this);
        for(TeamManager m:teamManagersList)
            m.resetMyTeam();
        this.teamOwners.clear();
        this.teamManagersList.clear();
        this.allTeamOwners.clear();
        if(field!=null)
            field.resetMyTeam(this);
        notifyTeamOwnersAndManager("Team Closed permanently:"+ getName());
        for(IObserverTeam ioT:iObserverTeamListForSystemManagers){
            ioT.removeAlert(this);
        }
        iObserverTeamListForSystemManagers=new LinkedList<>();
    }

    /**
     * This function adds new financialReport
     * @FinancialReport financialReport
     */
    public void addFinancialReport(FinancialReport financialReport) {
        this.financialReport.add(financialReport);
    }

    public void addOwnerToTeamOwnersList(TeamOwner tOwner){
        if(!this.allTeamOwners.contains(tOwner))
              this.allTeamOwners.add(tOwner);
    }

    public void removeOwnerFromTeamOwnersList(TeamOwner tOwner){
        this.allTeamOwners.remove(tOwner);
    }
    //</editor-fold>

    //<editor-fold desc="Override Methods">
    /**
     *
     * @return the details of this team
     */
    @Override
    public String showDetails() {
        return this.getDetails();
    }

    /**
     * This function register the system manager to this team alert according to changes in this team
     * @param systemManager
     */
    @Override
    public void registerSystemManagerToAlert(IObserverTeam systemManager) {
        this.iObserverTeamListForSystemManagers.add(systemManager);
        systemManager.registerAlert(this);
    }

    /**
     * This function add observer "obs" to the observers list of this team
     * @param obs
     */
    @Override
    public void registerAlert(IObserverTeam obs) {
        this.iObserverTeamListForTeamOwnersAndManagers.add(obs);
        obs.registerAlert(this);
    }

    /**
     * This function cancel the alert that the system manager receive from this team
     * @param systemManager
     */
    @Override
    public void removeAlertToSystemManager(IObserverTeam systemManager) {
        this.iObserverTeamListForSystemManagers.remove(systemManager);
        systemManager.registerAlert(this);
    }

    /**
     * This function remove observer "obs" from the observers list of this team
     * @param obs
     */
    @Override
    public void removeAlert(IObserverTeam obs) {
        this.iObserverTeamListForTeamOwnersAndManagers.remove(obs);
        obs.removeAlert(this);
    }

    /**
     * This function update each observer of this team observers
     */
    @Override
    public void notifySystemManager(String msg) {
        for(IObserverTeam observerTeam:this.iObserverTeamListForSystemManagers){
            observerTeam.update(msg);
        }
    }

    /**
     * This function update the teamOwner and the system manager in changes of this team
     */
    @Override
    public void notifyTeamOwnersAndManager(String msg) {
        for (IObserverTeam observerTeam:this.iObserverTeamListForTeamOwnersAndManagers){
            observerTeam.update(msg);
        }
    }

    /**
     * This function create a new personal page to the team
     * @return
     * @throws PersonalPageAlreadyExist
     */
    @Override
    public PersonalPage createPersonalPage() throws PersonalPageAlreadyExist {
        if(personalPage== null){
            PersonalPage newPersonalPage= new PersonalPage(this);
            this.personalPage=newPersonalPage;
            SystemEventLog.getInstance().writeToLog("The PersonalPage for team : "+getName()+" id : "+getId() +"was created.");
            return this.personalPage;
        }
        throw new PersonalPageAlreadyExist();
    }
    //</editor-fold>

}
