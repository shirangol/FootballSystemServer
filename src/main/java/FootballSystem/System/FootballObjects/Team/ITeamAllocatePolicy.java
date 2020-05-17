package FootballSystem.System.FootballObjects.Team;

import FootballSystem.System.FootballObjects.Game;
import java.util.List;

/**
 * Interface for setting games policy
 */
public interface ITeamAllocatePolicy {

    void setTeamPolicy(List<Team> teams, List<Game> games);

}