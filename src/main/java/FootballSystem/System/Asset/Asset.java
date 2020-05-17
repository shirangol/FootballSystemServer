package FootballSystem.System.Asset;

import FootballSystem.System.Exeptions.HasTeamAlreadyException;
import FootballSystem.System.FootballObjects.Team.Team;

public interface Asset {

    /**
     * Edit the asset value with a new value
     * @param newVal
     */
    void editAssetValue(int newVal);

    /**
     * Every asset connect to team , when this function call the team of the asset restart
     */
    void resetMyTeam();

    /**
     * In case the asset connect to more then one team, this function remove the team which we get as parameter
     * @param team
     */
    void resetMyTeam(Team team);

    /**
     * Every asset should be connect to team , when this function call the team which we get as parameter set as the asset team
     * @param team
     * @throws HasTeamAlreadyException
     */
    void addMyTeam(Team team) throws HasTeamAlreadyException;

    /**
     * This function return the asset salary
     * @return
     */
    int getSalary();

    /**
     * This function return the team which the asset connect to.
     * @return
     */
    Team getMyTeam();

    /**
     * This function return the value of this asset
     * @return
     */
    int getAssetValue();

    String getDetails();

}
