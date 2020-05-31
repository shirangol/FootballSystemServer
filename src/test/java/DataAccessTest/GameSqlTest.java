package DataAccessTest;

import FootballSystem.DataAccess.GameSQL;
import FootballSystem.DataAccess.TeamSQL;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.FootballObjects.Event.EventLog;
import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.LeagueInformation;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.FootballObjects.Team.DefaultAllocate;
import FootballSystem.System.FootballObjects.Team.DefaultMethod;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.Users.FootballAssociation;
import FootballSystem.System.Users.Referee;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameSqlTest {

    @Test
    public void getGameFromDB(){
        String gameStr= GameSQL.getInstance().get(1);
        String[] arr = gameStr.split(" ");
        assert(arr[0].equals("1"));
        assert (arr[1].equals("2020-05-30"));
    }

    @Test
    public void addGame() throws SQLException {
        //create leaguInformation
        List<Team> aa=new ArrayList<>();
        League league=new League("league123",aa);
        Season season=new Season(2030);
        FootballAssociation footballAssociation=new FootballAssociation(10,"123","acc","123");
        LeagueInformation leagueI=new LeagueInformation(7,league,season,footballAssociation,new DefaultAllocate(),new DefaultMethod(),3,1,0);
        //create game
        Team t = new Team(5,"aa", TeamStatus.Active,null,null,0,0);
        Team t2 = new Team(10,"asd", TeamStatus.Active,null,null,0,0);
        Referee referee =new Referee("a", RefereeType.MAIN,100,"123","a");
        Referee referee2 =new Referee("b",RefereeType.ASSISTANT,150,"123","b");
        Referee referee3 =new Referee("c",RefereeType.ASSISTANT,200,"123","c");
        Game game=new Game(100,new Date(),2000,"0:0",referee  , referee2, referee3, t, t2,new EventLog(),leagueI);
        GameSQL.getInstance().save(game);
        String gameStr=GameSQL.getInstance().get(100);
        String[] arr = gameStr.split(" ");
        assert(arr[0].equals("100"));
        assert (arr[2].equals("2000"));
        GameSQL.getInstance().delete(game);
    }

}
