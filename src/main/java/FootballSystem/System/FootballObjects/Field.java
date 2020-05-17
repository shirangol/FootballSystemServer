package FootballSystem.System.FootballObjects;

import FootballSystem.System.Asset.Asset;
import FootballSystem.System.Exeptions.HasTeamAlreadyException;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.I_Observer.ISubjectTeam;
import java.util.LinkedList;
import java.util.List;
import FootballSystem.System.SystemEventLog;

public class Field implements Asset {

    //<editor-fold desc="Fields">
    private int id;
    private String name;
    private List<Team> homeTeams;
    private int assetValue;
    private List<ISubjectTeam> subjectTeam;
    private int maintenanceCost;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    /**
     * Initialize variables
     * @param id
     * @param name
     * @param assetValue
     * @param maintCost
     */
    public Field(int id, String name, int assetValue,int maintCost) {
        this.id = id;
        this.name = name;
        this.homeTeams = new LinkedList<>();
        this.assetValue = assetValue;
        this.subjectTeam=new LinkedList<>();
        this.maintenanceCost=maintCost;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Team> getHomeTeams() {
        return homeTeams;
    }

    public int getAssetValue() {
        return assetValue;
    }

    @Override
    public String getDetails() {
        String str = "@name:"+name+" , id : "+ this.getId();
        return str;
    }

    public int getMaintenanceCost() {  return maintenanceCost; }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHomeTeams(List<Team> homeTeams) {
        this.homeTeams = homeTeams;
    }

    private void setAssetValue(int assetValue) {
        this.assetValue = assetValue;
    }

    public void setMaintenanceCost(int maintenanceCost) {  this.maintenanceCost = maintenanceCost;  }
    //</editor-fold>

    //<editor-fold desc="Override Methods">
    /**
     * Edit the asset value with a new value
     * @param newVal
     */
    @Override
    public void editAssetValue(int newVal) {
        this.setAssetValue(newVal);
        SystemEventLog.getInstance().writeToLog("The asset value for field : "+getName()+" id : "+getId() +"was edit.");

    }

    @Override
    public void resetMyTeam() {

    }

    /**
     * In case the asset connect to more then one team, this function remove the team which we get as parameter
     * @param team
     */
    @Override
    public void resetMyTeam(Team team) {
      this.homeTeams.remove(team);
        SystemEventLog.getInstance().writeToLog("The team for coach : "+getName()+" id : "+getId() +"was restart.");

    }

    /**
     * Every asset should be connect to team , when this function call the team which we get as parameter set as the asset team
     * @param team
     * @throws HasTeamAlreadyException
     */
    @Override
    public void addMyTeam(Team team) {
      homeTeams.add(team);
        SystemEventLog.getInstance().writeToLog("The team for field : "+getName()+" id : "+getId() +"was added to the home teams list.");

    }

    /**
     * This function return the asset salary
     * @return
     */
    @Override
    public int getSalary() {
        return 0;
    }

    @Override
    public Team getMyTeam() {
        return null;
    }
    //</editor-fold>

}
