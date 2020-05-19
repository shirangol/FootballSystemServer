package FootballSystem.System.Users;

import FootballSystem.System.Asset.Asset;
import FootballSystem.System.Exeptions.HasTeamAlreadyException;
import FootballSystem.System.Exeptions.PersonalPageAlreadyExist;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.IShowable;
import FootballSystem.System.I_Observer.ISubjectTeam;
import FootballSystem.System.SystemEventLog;
import FootballSystem.System.PersonalPages.IPageAvailable;
import FootballSystem.System.PersonalPages.PersonalPage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class Coach extends User implements Asset, IPageAvailable, IShowable {

    //<editor-fold desc="Fields">
    private String preparation;
    private String role;
    private PersonalPage personalPage;
    private  int assetValue;
    private Team myTeam;
    private int salary;
    private List<ISubjectTeam> subjectTeam;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    /**
     * Initialize variables
     * @param id
     * @param name
     * @param password
     * @param userName
     * @param preparation
     * @param role
     * @param assetValue
     * @param salary
     */
    public Coach(@JsonProperty("id")int id,@JsonProperty("name") String name,@JsonProperty("password") String password,@JsonProperty("userName") String userName, @JsonProperty("preparation")String preparation,@JsonProperty("role") String role,@JsonProperty("assetValue") int assetValue,@JsonProperty("salary") int salary) {
        super(id, name, password, userName);
        this.preparation = preparation;
        this.role = role;
        this.assetValue = assetValue;
        this.salary = salary;
        this.subjectTeam=new LinkedList<>();
        this.myTeam=null;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "Coach";
    }

    @Override
    public String getDetails() {
        String str = "@name:"+name+"@preparation:"+preparation+"@role:"+role+"@team:";
        if(this.myTeam!=null)
            str=str+myTeam.getName()+"";
        return str;
    }

    public String getPreparation() {
        return preparation;
    }

    public String getRole() {
        return role;
    }

    public PersonalPage getPersonalPage() {
        return personalPage;
    }

    @Override
    public int getAssetValue() {
        return assetValue;
    }

    @Override
    public Team getMyTeam() {
        return myTeam;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setName(String name) {
        this.name = name;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPersonalPage(PersonalPage personalPage) {
        this.personalPage = personalPage;
    }

    private void setAssetValue(int assetValue) {
        this.assetValue = assetValue;
    }

    public void setMyTeam(Team myTeam) {
        this.myTeam = myTeam;
    }
    //</editor-fold>

    //<editor-fold desc="Override Methods">
    /**
     * Edit the asset value with a new value
     * @param newVal
     */
    @Override
    public void editAssetValue(int newVal) {
        this.setAssetValue(newVal);
        SystemEventLog.getInstance().writeToLog("The asset value for coach : "+getName()+" id : "+getId() +"was edit.");
    }

    /**
     * Every asset connect to team , when this function call the team of the asset restart
     */
    @Override
    public void resetMyTeam() {
        this.myTeam=null;
        SystemEventLog.getInstance().writeToLog("The team for coach : "+getName()+" id : "+getId() +"was restart.");

    }

    @Override
    public void resetMyTeam(Team team) {
        this.myTeam=null;
        SystemEventLog.getInstance().writeToLog("The team for coach : "+getName()+" id : "+getId() +"was restart.");

    }

    /**
     * Every asset should be connect to team , when this function call the team which we get as parameter set as the asset team
     * @param team
     * @throws HasTeamAlreadyException
     */
    @Override
    public void addMyTeam(Team team) throws HasTeamAlreadyException {
       if(this.myTeam != null) {
           throw new HasTeamAlreadyException();
       }
       else{
           this.myTeam = team;
           SystemEventLog.getInstance().writeToLog("The team for coach : "+getName()+" id : "+getId() +"was added.");

       }
    }

    /**
     * This function return the asset salary
     * @return
     */
    @Override
    public int getSalary() {
        return salary;
    }

    @Override
    public String showDetails() {
        return this.getDetails();
    }

    /**
     * This function create a new personal page to the coach
     * @return
     * @throws PersonalPageAlreadyExist
     */
    @Override
    public PersonalPage createPersonalPage() throws PersonalPageAlreadyExist {
        if(personalPage== null){
            PersonalPage newPersonalPage= new PersonalPage(this);
            this.personalPage=newPersonalPage;
            SystemEventLog.getInstance().writeToLog("The PersonalPage for coach : "+getName()+" id : "+getId() +"was created.");
            return this.personalPage;
        }
        throw new PersonalPageAlreadyExist();
    }

    /**
     * remove coach from everything he connected to
     */
    @Override
    public void removeUser()  {
        preparation=null;
        role=null;
        personalPage=null;
        assetValue=0;
        myTeam=null;
        subjectTeam=null;
        salary=0;
    }
    //</editor-fold>

}
