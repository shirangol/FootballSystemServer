package DataAccessTest;

import FootballSystem.DataAccess.LeagueSQL;
import FootballSystem.DataAccess.TeamSQL;
import FootballSystem.DataAccess.UserSQL;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.Team.Team;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LeagueSqlTest {

    @Test
	public void getLeagueTestByLeageParam(){
		List<Team> teams=new LinkedList<>();
		League l=new League("Premier_League",teams);
        String leagueStr= LeagueSQL.getInstance().get(l);
        String[] arr = leagueStr.split(" ");
        assert(arr[0].equals("1"));
        assert (arr[1].equals("Premier_League"));
	}

    @Test
    public void getLeagueTestByIDParam(){
        String leagueStr= LeagueSQL.getInstance().get(1);
        String[] arr = leagueStr.split(" ");
        assert(arr[0].equals("1"));
        assert (arr[1].equals("Premier_League"));
    }


    @Test
    public void saveLeagueTest() throws SQLException {
        List<Team> teams=new LinkedList<>();
        League l=new League(12345,"The_best_League_ever777",teams);
        LeagueSQL.getInstance().save(l);
        String leagueStr=LeagueSQL.getInstance().get(12345);
        String[] arr = leagueStr.split(" ");
        assert(arr[0].equals("12345"));
        assert (arr[1].equals("The_best_League_ever777"));
        LeagueSQL.getInstance().delete("The_best_League_ever777");
    }


}
