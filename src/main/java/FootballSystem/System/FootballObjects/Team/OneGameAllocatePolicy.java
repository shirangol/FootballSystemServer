package FootballSystem.System.FootballObjects.Team;

import FootballSystem.System.FootballObjects.Game;
import FootballSystem.System.SystemEventLog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OneGameAllocatePolicy implements ITeamAllocatePolicy {

    //<editor-fold desc="Methods">
    /**
     * Inaugural games for the league - one game for each league team
     * @param team list of team in the league
     * @param games
     */
    public void setTeamPolicy(List<Team> team, List<Game> games){
        Date date=new Date();
        int noOfDays = 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i=0;i<team.size();i++){
            for(int j=i+1; j<team.size();j++){
                //add game1
                Team home= team.get(i);
                Team away= team.get(j);
                Game game=new Game(date, 2000, null, null,null, away, home);
                calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
                date = calendar.getTime();
                games.add(game);

                home.getGamesOfTeams().add(game);
                away.getGamesOfTeams().add(game);
            }
        }
        SystemEventLog.getInstance().writeToLog("League Information inaugural games for the league - one game for each league team");

    }
    //</editor-fold>

}
