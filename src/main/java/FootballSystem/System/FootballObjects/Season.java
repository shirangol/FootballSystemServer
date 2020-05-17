package FootballSystem.System.FootballObjects;

import FootballSystem.System.IShowable;

import java.util.ArrayList;
import java.util.List;

public class Season implements IShowable {

    //<editor-fold desc="Fields">
    private int year;
    private List<LeagueInformation> leaguesInformation;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Season(int year){
        this.year=year;
        leaguesInformation =new ArrayList<>();

        //iTeamAllocatePolicy= new DefaultAllocate();

    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public String getYear() {
        return String.valueOf(year);
    }

    public int getIntYear() {
        return year;
    }

    @Override
    public String getName() {
        return "Season:"+year;
    }

    @Override
    public String getType() {
        return "Season";
    }

    @Override
    public String getDetails() {
        String str = "@year:"+year;
        return str;
    }
    public List<LeagueInformation> getLeaguesInformation() {
        return leaguesInformation;
    }
    //</editor-fold>

    ///<editor-fold desc="Methods">
    public void addLeagueInformation(LeagueInformation leagueInformation) {
        this.leaguesInformation.add(leagueInformation);
    }
    //</editor-fold>

}
