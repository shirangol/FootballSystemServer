package FootballSystem.System.FootballObjects;

import FootballSystem.DataAccess.LeagueInformationSQL;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.FootballObjects.Event.EventLog;
import FootballSystem.System.FootballObjects.Team.*;
import FootballSystem.System.Users.FootballAssociation;
import FootballSystem.System.Users.Referee;
import FootballSystem.System.SystemEventLog;
import java.util.*;


public class LeagueInformation {

    //<editor-fold desc="Fields">
    private static int ID=1;
    private int id;
    private List<Game> games;
    private League league;
    private Season season;
    private FootballAssociation footballAssociation;
    private String name;
    private HashMap<Team,Integer> leagueTable;
    private ITeamAllocatePolicy iTeamAllocatePolicy;
    private IScoreMethodPolicy iScoreMethodPolicy;
    private int WIN;
    private int LOSS;
    private int TIE;
    private Team Champion;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    /**
     * Constructor
     * @param league
     * @param season
     * @param footballAssociation
     */
    public LeagueInformation(League league, Season season, FootballAssociation footballAssociation) {
        this.id= ID;
        ID++;
        this. league=league;
        this. season= season;
        name= season.getName()+" "+league.getName();
        this.footballAssociation = footballAssociation;
        iTeamAllocatePolicy= new DefaultAllocate();
        leagueTable= new LinkedHashMap<>();
        iScoreMethodPolicy= new DefaultMethod();
        games=new ArrayList<>();
        //init league Table with 0 points to all the team.
        for(int i=0;i<league.getTeams().size();i++){
            leagueTable.put(league.getTeams().get(i),0);
        }
    }

    public LeagueInformation(int id, League league, Season season, FootballAssociation footballAssociation, ITeamAllocatePolicy iTeamAllocatePolicy,IScoreMethodPolicy iScoreMethodPolicy ,int win,int loss,int tie) {
        this.id= id;
        this. league=league;
        this. season= season;
        name= season.getName()+" "+league.getName();
        this.footballAssociation = footballAssociation;
        this.iTeamAllocatePolicy= iTeamAllocatePolicy;
        leagueTable= new LinkedHashMap<>();
        this.iScoreMethodPolicy= iScoreMethodPolicy;
        games=new ArrayList<>();
        //init league Table with 0 points to all the team.
        for(int i=0;i<league.getTeams().size();i++){
            leagueTable.put(league.getTeams().get(i),0);
        }
        WIN=win;
        LOSS=loss;
        TIE=tie;

    }


    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public List<Game> getGames() {
        return games;
    }

    public League getLeague() {
        return league;
    }

    public Season getSeason() {
        return season;
    }

    public FootballAssociation getFootballAssociation() {
        return footballAssociation;
    }

    public String getName() {
        return name;
    }

    public ITeamAllocatePolicy getiTeamAllocatePolicy() {
        return iTeamAllocatePolicy;
    }

    public IScoreMethodPolicy getiScoreMethodPolicy() {
        return iScoreMethodPolicy;
    }

    public Team getChampion() {
        return Champion;
    }

    public int getWIN() {
        return WIN;
    }

    public int getLOSS() {
        return LOSS;
    }

    public int getTIE() {
        return TIE;
    }
    //</editor-fold>

    //<editor-fold desc="Override Methods">
    /**
     *
     * @return the league table sorted by the high scoring team.
     */
    // function to sort hashMap by values
    public  HashMap<Team,Integer> getLeagueTable()
    {
        // Create a list from elements of HashMap
        List<HashMap.Entry<Team,Integer> > list =
                new LinkedList<HashMap.Entry<Team,Integer> >(leagueTable.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<HashMap.Entry<Team,Integer> >() {
            public int compare(Map.Entry<Team,Integer> o1,
                               Map.Entry<Team,Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Team,Integer> temp = new LinkedHashMap<Team,Integer>();
        for (HashMap.Entry<Team,Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * init leagueInformation policy-  Team Allocate Policy AND Score Method Policy.
     */
    public void initLeagueInformation(){
        iTeamAllocatePolicy.setTeamPolicy(league.getTeams(),games);
        for(Game game: games){
            game.setLeagueInformation(this);
        }
        //get list of score for policy
        //setSore[0]= WIN, setSore[1]=LOSS, setSore[2]=TIE
        List<Integer> setScore= iScoreMethodPolicy.setScorePolicy();
        WIN=setScore.get(0);
        LOSS=setScore.get(1);
        TIE=setScore.get(2);
    }

    /**
     * Inaugural refereeing for games during the league season
     * @param referees list of all referees
     */
    public void schedulingReferee(List<Referee> referees){
        List <Referee> mainReferee= new ArrayList<>();
        List <Referee> assistentsReferee= new ArrayList<>();

        for(Referee referee:referees){
            if(referee.getRefereeType()== RefereeType.MAIN){
                mainReferee.add(referee);
            }
            else {
                assistentsReferee.add(referee);
            }
        }
        int i=0;
        int j=0;
        Referee R1;
        Referee R2;
        Referee R3;
        for(Game game:games){
            if( i< mainReferee.size()){
                R1=mainReferee.get(i++);
            }
            else{
                i=0;
                R1=mainReferee.get(i++);
            }
            if( j< assistentsReferee.size()){
                R2= assistentsReferee.get(j++);
            }
            else{
                j=0;
                R2= assistentsReferee.get(j++);
            }
            if( j< assistentsReferee.size()){
                R3= assistentsReferee.get(j++);
            }
            else{
                j=0;
                R3= assistentsReferee.get(j++);
            }
            game.setMainReferee(R1);
            game.setAssistantRefereeOne(R2);
            game.setAssistantRefereeTwo(R3);

            R1.addGame(game);
            R2.addGame(game);
            R3.addGame(game);

        }

        SystemEventLog.getInstance().writeToLog("League information- The referees inaugural season was successfully completed. League name: "+league.getName());
    } //UC-32

    /**
     * Edit game scheduling policy with the help of Strategy DP.
     * @param iTeamAllocatePolicy Interface that refers to change policy.
     */
    public void editGameSchedulingPolicy(ITeamAllocatePolicy iTeamAllocatePolicy){
        this.iTeamAllocatePolicy=iTeamAllocatePolicy;
        LeagueInformationSQL.getInstance().updateAllocatePolicy(this.id,iTeamAllocatePolicy);
        SystemEventLog.getInstance().writeToLog("game scheduling policy set");

    } //UC-34

    /**
     * Edit score scheduling policy with the help of Strategy DP.
     * @param iScoreMethodPolicy  Interface that refers to change policy.
     */
    public void editScoreSchedulingPolicy(IScoreMethodPolicy iScoreMethodPolicy){//UC-37
        SystemEventLog.getInstance().writeToLog("Score policy set");
        this.iScoreMethodPolicy=iScoreMethodPolicy;
    }

    /**
     * Update Score by game result and score policy
     * @param t
     * @param result
     */
    public void updateScoreTeamInLeagueTable(Team t, String result){
        if(result.equals("WIN")){
            leagueTable.replace(t,leagueTable.get(t)+WIN);
        }else if(result.equals("LOSS")){
            leagueTable.replace(t,leagueTable.get(t)+LOSS);
        }else {
            leagueTable.replace(t,leagueTable.get(t)+TIE);
        }
    }

    //public void editScoreSchedulingPolicy(League league, Season season , IScoreMethodPolicy iScoreMethodPolicy){} //UC-37
    //</editor-fold>

}
