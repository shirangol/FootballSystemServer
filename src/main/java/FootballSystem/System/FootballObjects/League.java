package FootballSystem.System.FootballObjects;

import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.IShowable;

import java.util.ArrayList;
import java.util.List;

public class League implements IShowable {

    //<editor-fold desc="Fields">
    private static int ID=1;
    private int id;
    private String name;
    private List<Team> teams;
    private List<LeagueInformation> leaguesInformation;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public League(String name, List<Team> teams) {

        this.id= ID;
        ID++;
        this.name=name;
        this.teams= new ArrayList<>();
        this.leaguesInformation = new ArrayList<>();
        for (Team t : teams) {
            this.teams.add(t);
        }
    }
    public League(int id,String name, List<Team> teams) {
        this.id= id;
        this.name=name;
        this.teams= new ArrayList<>();
        this.leaguesInformation = new ArrayList<>();
        for (Team t : teams) {
            this.teams.add(t);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public String getName() {
        return name;
    }
    public int getid(){
        return id;
    }

    @Override
    public String getType() {
        return "League";
    }

    @Override
    public String getDetails() {
        String str = "@name:"+name;
        return str;
    }

    public List<LeagueInformation> getLeagueInformation() {
        return leaguesInformation;
    }

    public List<Team> getTeams(){
        return teams;
    }
    public void addTeam(Team t){
        teams.add(t);
    }

    //</editor-fold>

    //<editor-fold desc="Methods">
    public void addLeagueInformation(LeagueInformation leagueInformation) {
        this.leaguesInformation.add(leagueInformation);
    }
    //</editor-fold>

}
