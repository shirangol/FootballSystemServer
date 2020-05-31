package DataAccessTest;

import FootballSystem.DataAccess.LeagueInformationSQL;
import FootballSystem.DataAccess.TeamSQL;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.FootballObjects.Team.DefaultAllocate;
import FootballSystem.System.FootballObjects.Team.DefaultMethod;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.FootballAssociation;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeagueInformationSqlTest {

    @Test
    public void getLeagueInformationFromDB(){
        String LeagueInformationtr= (String) LeagueInformationSQL.getInstance().get(1);
        String[] arr = LeagueInformationtr.split(" ");
        assert(arr[0].equals("1"));
        assert (arr[1].equals("Premie_League_2020"));
    }

    @Test
    public void addLeagueInformation() throws SQLException {
        List<Team> aa=new ArrayList<>();
        League league=new League("league123",aa);
        Season season=new Season(2030);
        FootballAssociation footballAssociation=new FootballAssociation(10,"123","acc","123");
        LeagueInformation leagueI=new LeagueInformation(7,league,season,footballAssociation,new DefaultAllocate(),new DefaultMethod(),3,1,0);
        LeagueInformationSQL.getInstance().save(leagueI);
        String leagueIStr= (String) LeagueInformationSQL.getInstance().get(7);
        String[] arr = leagueIStr.split(" ");
        assert(arr[0].equals("7"));
        assert (arr[1].equals(leagueI.getName()));
        LeagueInformationSQL.getInstance().delete(leagueI);
    }

}
