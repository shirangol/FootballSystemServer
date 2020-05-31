package DataAccessTest;

import FootballSystem.DataAccess.TeamSQL;
import FootballSystem.DataAccess.UserSQL;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.Fan;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class TeamSqlTest {

    @Test
    public void getTeamFromDB(){
        String teamStr= TeamSQL.getInstance().get(3);
        String[] arr = teamStr.split(" ");
        assert(arr[0].equals("3"));
        assert (arr[1].equals("Liverpool"));
    }

    @Test
    public void addTeam() throws SQLException {
        Team t = new Team(7654,"teamName", TeamStatus.Active,null,null,0,0);
        TeamSQL.getInstance().save(t);
        String userStr=TeamSQL.getInstance().get(7654);
        String[] arr = userStr.split(" ");
        assert(arr[0].equals("7654"));
        assert (arr[1].equals("teamName"));
        TeamSQL.getInstance().delete(t);
    }

}
