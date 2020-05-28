package FootballSystem.System;
/////
import FootballSystem.DataAccess.*;
import FootballSystem.ServiceLayer.FanController;
import FootballSystem.System.*;
import FootballSystem.System.Enum.TeamStatus;
import FootballSystem.System.Enum.RefereeType;
import FootballSystem.System.Enum.UserStatus;
import FootballSystem.System.Exeptions.NoSuchAUserNamedException;
import FootballSystem.System.Exeptions.UserNameAlreadyExistException;
import FootballSystem.System.Exeptions.WrongPasswordException;
import FootballSystem.System.FootballObjects.*;
import FootballSystem.System.FootballObjects.Event.*;
import FootballSystem.System.FootballObjects.Team.*;
import FootballSystem.System.Users.*;
//import javafx.scene.chart.ScatterChart;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Controller {

    //<editor-fold desc="Fields">
    private HashMap<String, User> users;
    private List<League> leagues;
    private List<Team> teams;
    private List<Season> seasons;
    private List<Field> fields;
    private HashMap<String, User> removedUser;
    private HashMap<String, IScoreMethodPolicy> scorePolicies;
    private HashMap<String, ITeamAllocatePolicy> methodAllocatePolicies;
    private UserSQL userSQL;
    private List<Game> gameList;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    private static Controller ourInstance = new Controller();

    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
        users = new HashMap<>();
        leagues = new LinkedList<>();
        removedUser = new HashMap<>();
        teams = new LinkedList<>();
        seasons = new LinkedList<>();
        fields = new LinkedList<>();
        scorePolicies = new HashMap<>();
        scorePolicies.put("DefaultMethod",new DefaultMethod());
        methodAllocatePolicies = new HashMap<>();
        methodAllocatePolicies.put("DefaultAllocate", new DefaultAllocate()) ;
        methodAllocatePolicies.put("OneGameAllocatePolicy", new OneGameAllocatePolicy()) ;
        gameList=new ArrayList<>();


//        leagues=getAllLeagues();
//        teams=getAllTeams();
//        gameList=getAllGames();
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public List<League> getAllLeagues() { //UC-4
        if (leagues.size() != 0) {
            return leagues;
        }
        //not found- get from DB
        List<String> t = LeagueSQL.getInstance().getAll();
        for (int i = 0; i < t.size(); i++) {
            String[] result = t.get(i).split(" ");

            int leagueID = Integer.parseInt(result[0]);
            String name = result[1];

            List<Team> teams= getAllTeamsForLeague(leagueID);
            League league=new League(leagueID,name,teams);
            List<String> leagueInformationStr= LeagueInformationSQL.getInstance().getAllLegueInformation(leagueID);
            //************************* split league information***********************
            for(String l : leagueInformationStr) {
                String[] splitleagueInformationString = l.split(" ");

                int leagueInformationID = Integer.parseInt(splitleagueInformationString[0]);
                String namel = splitleagueInformationString[1];
                int winScore = Integer.parseInt(splitleagueInformationString[2]);
                int lossScore = Integer.parseInt(splitleagueInformationString[3]);
                int tieScore = Integer.parseInt(splitleagueInformationString[4]);

                ITeamAllocatePolicy iTeamAllocatePolicy=null;
                int piTeamAllocatePolicy = Integer.parseInt(splitleagueInformationString[5]);
                if (piTeamAllocatePolicy == 1) {
                    iTeamAllocatePolicy = new DefaultAllocate();
                } else if (piTeamAllocatePolicy == 2) {
                    iTeamAllocatePolicy = new OneGameAllocatePolicy();
                }

                IScoreMethodPolicy iScoreMethodPolicy = new DefaultMethod();
                int piScoreMethodPolicy = Integer.parseInt(splitleagueInformationString[6]);

                String footballAssociationString = splitleagueInformationString[7];
                FootballAssociation footballAssociation = (FootballAssociation) getUser(footballAssociationString);

                int pLeague = Integer.parseInt(splitleagueInformationString[8]);
                int PSeason = Integer.parseInt(splitleagueInformationString[9]);
                Season season = new Season(PSeason);
                LeagueInformation newLeagueInformation=new LeagueInformation(leagueInformationID, league,season, null, iTeamAllocatePolicy, iScoreMethodPolicy ,winScore,lossScore,tieScore);
                league.addLeagueInformation(newLeagueInformation);
                //****************************************************************************
            }
            leagues.add(league);
        }
        return leagues;
    } //UC-4
    public League getLeague(int id) { //UC-4
        for(League league:leagues){
            if (league.getid()== id) {
                return league;
            }
        }

        //not found- get from DB
        String leagueString = LeagueSQL.getInstance().get(id);
        String[] result = leagueString.split(" ");

        int leagueID = Integer.parseInt(result[0]);
        String name = result[1];
        List<Team> teams= getAllTeamsForLeague(leagueID);
        League league=new League(leagueID,name,teams);
        leagues.add(league);
        return league;

    } //UC-4

    public List<Team> getAllTeamsForLeague(int id) {
        List<String> t = TeamSQL.getInstance().getAllForLeague(id);
        List<Team> teamsToReturn= new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            String[] result = t.get(i).split(" ");
            int teamID = Integer.parseInt(result[0]);
            String name = result[1];
            int status = Integer.parseInt(result[2]);
            int fieldID = Integer.parseInt(result[3]);
            int pPersonalPage = Integer.parseInt(result[4]);
            int income = Integer.parseInt(result[5]);
            int expense = Integer.parseInt(result[6]);
            int pLeague = Integer.parseInt(result[7]);

            TeamStatus teamStatus = null;
            if (status == 1) {
                teamStatus = TeamStatus.Active;
            } else if (status == 2) {
                teamStatus = TeamStatus.Close;
            } else {
                teamStatus = TeamStatus.PermanentlyClose;
            }

            Team team2 = new Team(teamID, name, teamStatus, null, null, income, expense);
            teamsToReturn.add(team2);
        }
        return teamsToReturn;
    } //UC-4



    public List<Team> getAllTeams() {
        if (teams.size() != 0) {
            return teams;
        }
        //not found- get from DB
        List<String> t = TeamSQL.getInstance().getAll();
        for (int i = 0; i < t.size(); i++) {
            String[] result = t.get(i).split(" ");
            int teamID = Integer.parseInt(result[0]);
            String name = result[1];
            int status = Integer.parseInt(result[2]);
            int fieldID = Integer.parseInt(result[3]);
            int pPersonalPage = Integer.parseInt(result[4]);
            int income = Integer.parseInt(result[5]);
            int expense = Integer.parseInt(result[6]);
            int pLeague = Integer.parseInt(result[7]);

            TeamStatus teamStatus = null;
            if (status == 1) {
                teamStatus = TeamStatus.Active;
            } else if (status == 2) {
                teamStatus = TeamStatus.Close;
            } else {
                teamStatus = TeamStatus.PermanentlyClose;
            }

            Team team2 = new Team(teamID, name, teamStatus, null, null, income, expense);
            teams.add(team2);
        }
        return teams;
    } //UC-4

    public List<Season> getAllSeasons() {
        return seasons;
    } //UC-4

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void addUser(String s, User u) {
        users.put(s, u);
    }

    public void addSeason(Season season) {
        seasons.add(season);
    }

    /**
     * According to proxy attitude - first check if user exist in the hashMap, otherwise we get the user from DB
     *
     * @param userName
     * @return
     */
    public User getUser(String userName) {
        User user = null;
        if (users.containsKey(userName)) {
            user = users.get(userName);
        } else {
            String userStr = UserSQL.getInstance().get(userName);
            String[] userArr = userStr.split(" ");
            switch (userArr[0]) {
                case "Fan":
                    user = new Fan(Integer.parseInt(userArr[1]), userArr[2], userArr[3], userArr[4]);
                    break;
                case "Referee":
                    RefereeType type;
                    if (userArr[2].equals("MAIN")) {
                        type = RefereeType.MAIN;
                    } else {
                        type = RefereeType.ASSISTANT;
                    }
                    user = new Referee(userArr[1], type, Integer.parseInt(userArr[3]), userArr[4], userArr[5]);
                    break;
                case "FootballAssociation":
                    user = new FootballAssociation(Integer.parseInt(userArr[1]), userArr[2], userArr[3], userArr[4]);
            }
        }
        return user;
    }

    public Team getTeam ( int id){
        for (Team team : teams) {
            if (team.getId() == id) {
                return team;
            }
        }
        //not found- get from DB
        String t = TeamSQL.getInstance().get(id);
        String[] result = t.split(" ");
        int teamID = Integer.parseInt(result[0]);
        String name = result[1];
        int status = Integer.parseInt(result[2]);
        int fieldID = Integer.parseInt(result[3]);
        int pPersonalPage = Integer.parseInt(result[4]);
        int income = Integer.parseInt(result[5]);
        int expense = Integer.parseInt(result[6]);
        int pLeague = Integer.parseInt(result[7]);

        TeamStatus teamStatus = null;
        if (status == 1) {
            teamStatus = TeamStatus.Active;
        } else if (status == 2) {
            teamStatus = TeamStatus.Close;
        } else {
            teamStatus = TeamStatus.PermanentlyClose;
        }

        Team team2 = new Team(teamID, name, teamStatus, null, null, income, expense);
        teams.add(team2);
        return team2;

    }

//    public List<Team> getTeams ( int id){
//        if (teams.size() != 0) {
//            return teams;
//        }
//        //not found- get from DB
//        List<String> t = TeamSQL.getInstance().getAll();
//        for (int i = 0; i < t.size(); i++) {
//            String[] result = t.get(i).split(" ");
//            int teamID = Integer.parseInt(result[0]);
//            String name = result[1];
//            int status = Integer.parseInt(result[2]);
//            int fieldID = Integer.parseInt(result[3]);
//            int pPersonalPage = Integer.parseInt(result[4]);
//            int income = Integer.parseInt(result[5]);
//            int expense = Integer.parseInt(result[6]);
//            int pLeague = Integer.parseInt(result[7]);
//
//            TeamStatus teamStatus = null;
//            if (status == 1) {
//                teamStatus = TeamStatus.Active;
//            } else if (status == 2) {
//                teamStatus = TeamStatus.Close;
//            } else {
//                teamStatus = TeamStatus.PermanentlyClose;
//            }
//
//            Team team2 = new Team(teamID, name, teamStatus, null, null, income, expense);
//            teams.add(team2);
//        }
//        return teams;
//    }

    public Game getGame ( int id){
        String game = GameSQL.getInstance().get(id);
        String[] seperate = game.split(" ");
        String date = seperate[1];

        Date date2 = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-dd-mm", Locale.ENGLISH);
            date2 = format.parse(date);

        } catch (Exception e) {
            System.out.println(e);
        }
        int hour = Integer.parseInt(seperate[2]);
        String result = seperate[3];

        //teams
        int pTeamAway = Integer.parseInt(seperate[4]);
        int pTeamHome = Integer.parseInt(seperate[5]);
        Team away = getTeam(pTeamAway);
        Team home = getTeam(pTeamHome);

        //String main =  UserSQL.getInstance().get(seperate[6]);
        Referee newMain= (Referee) getUser(seperate[6]);

        //String ass1 =  UserSQL.getInstance().get(seperate[7]);
        Referee newAss1= (Referee) getUser(seperate[7]);
        //String ass2 =  UserSQL.getInstance().get(seperate[8]);
        Referee newAss2= (Referee) getUser(seperate[8]);

        //EventLog
        int EventLog=  Integer.parseInt(seperate[9]);
        List<String> events= EventLogSQL.getInstance().get(EventLog);



        List<AEvent> aEvents=new ArrayList<>();
        for(String str:events) {
            String[] seperate2 = str.split(",");
            int eventID = Integer.parseInt(seperate2[0]);
            String date3 = seperate2[1];
            Date theSameDate = new Date();

            try {
                theSameDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date3);
            } catch (Exception e) {
            }

            int minute = Integer.parseInt(seperate2[2]);

            String playerName = seperate2[3];
            String teamName = seperate2[4];
            int type = Integer.parseInt(seperate2[5]);


            switch (type) {
                case 1:
                    Goal goal = new Goal(eventID, theSameDate, minute, playerName, teamName);
                    aEvents.add(goal);
                    break;
                case 2:
                    Injury injury = new Injury(eventID, theSameDate, minute, playerName, teamName);
                    aEvents.add(injury);
                    break;
                case 3:
                    Offense offense = new Offense(eventID, theSameDate, minute, playerName, teamName);
                    aEvents.add(offense);
                    break;
                case 4:
                    Offside offside = new Offside(eventID, theSameDate, minute, playerName, teamName);
                    aEvents.add(offside);
                    break;
                case 5:
                    RedCard redCard = new RedCard(eventID, theSameDate, minute, playerName, teamName);
                    aEvents.add(redCard);
                    break;
                case 6:
                    YellowCard yellowCard = new YellowCard(eventID, theSameDate, minute, playerName, teamName);
                    aEvents.add(yellowCard);
                    break;
            }
        }
        EventLog eventLog=new EventLog(EventLog, aEvents);

        //leageInformation

        LeagueInformation leagueInformation= ((LeagueInformation) LeagueInformationSQL.getInstance().get(Integer.parseInt(seperate[10])));
        Game newGame = new Game(id, date2, hour, result, newMain, newAss1, newAss2, away, home,eventLog,leagueInformation);
        return newGame;

    }
    public List<Game> getAllGames () {
        List<Game> games = new ArrayList<>();

        List<String> StringGames = GameSQL.getInstance().getAll();
        for (int i = 0; i < StringGames.size(); i++) {
            String[] seperate = StringGames.get(i).split(" ");
            int id = Integer.parseInt(seperate[0]);
            String date = seperate[1];

            Date date2 = null;
            try {
                DateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                date2 = format.parse(date);

            } catch (Exception e) {
                System.out.println(e);
            }
            int hour = Integer.parseInt(seperate[2]);
            String result = seperate[3];

            //teams
            int pTeamAway = Integer.parseInt(seperate[4]);
            int pTeamHome = Integer.parseInt(seperate[5]);
            Team away = getTeam(pTeamAway);
            Team home = getTeam(pTeamHome);

           // String main =  UserSQL.getInstance().get(seperate[6]);
            Referee newMain= (Referee) getUser(seperate[6]);

            //String ass1 =  UserSQL.getInstance().get(seperate[7]);
            Referee newAss1= (Referee) getUser(seperate[7]);
           // String ass2 =  UserSQL.getInstance().get(seperate[8]);
            Referee newAss2= (Referee) getUser(seperate[8]);
            //EventLog
            int EventLog=  Integer.parseInt(seperate[9]);
            List<String> events= EventLogSQL.getInstance().get(EventLog);



            List<AEvent> aEvents=new ArrayList<>();
            for(String str:events) {
                String[] seperate2 = str.split(",");
                int eventID = Integer.parseInt(seperate2[0]);
                String date3 = seperate2[1];
                Date theSameDate = new Date();

                try {
                    theSameDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date3);
                } catch (Exception e) {
                }

                int minute = Integer.parseInt(seperate2[2]);

                String playerName = seperate2[3];
                String teamName = seperate2[4];
                int type = Integer.parseInt(seperate2[5]);


                switch (type) {
                    case 1:
                        Goal goal = new Goal(eventID, theSameDate, minute, playerName, teamName);
                        aEvents.add(goal);
                        break;
                    case 2:
                        Injury injury = new Injury(eventID, theSameDate, minute, playerName, teamName);
                        aEvents.add(injury);
                        break;
                    case 3:
                        Offense offense = new Offense(eventID, theSameDate, minute, playerName, teamName);
                        aEvents.add(offense);
                        break;
                    case 4:
                        Offside offside = new Offside(eventID, theSameDate, minute, playerName, teamName);
                        aEvents.add(offside);
                        break;
                    case 5:
                        RedCard redCard = new RedCard(eventID, theSameDate, minute, playerName, teamName);
                        aEvents.add(redCard);
                        break;
                    case 6:
                        YellowCard yellowCard = new YellowCard(eventID, theSameDate, minute, playerName, teamName);
                        aEvents.add(yellowCard);
                        break;
                }
            }
            EventLog eventLog=new EventLog(EventLog, aEvents);

            //leageInformation
            LeagueInformation leagueInformation= ((LeagueInformation) LeagueInformationSQL.getInstance().get(Integer.parseInt(seperate[10])));
            Game newGame = new Game(id, date2, hour, result, newMain, newAss1, newAss2, away, home,eventLog,leagueInformation);
            games.add(newGame);
        }
        return games;
    }

    public List<Game> getAllGamesForReferee (String username){
        boolean flag=false;
        List<Game> games = new ArrayList<>();

        List<String> StringGames = GameSQL.getInstance().getAllgamesForReferee(username);
        for (int i = 0; i < StringGames.size(); i++) {
            flag=false;
            String[] seperate = StringGames.get(i).split(" ");
            int id = Integer.parseInt(seperate[0]);
            for(int j=0;i<gameList.size();j++){
                if(gameList.get(j).getId()==id){
                    games.add(gameList.get(j));
                    flag=true;
                    break;
                }
            }
            if(flag==false){
                String date = seperate[1];
                Date date2 = null;
                String[] dateParse= seperate[1].split("-");
                int year= Integer.parseInt(dateParse[0])-1900;
                int month= Integer.parseInt(dateParse[1])-1;
                int day= Integer.parseInt(dateParse[2]);

                date2=new Date(year,month,day);
                date2.setHours(12);
                date2.setMinutes(0);

                int hour = Integer.parseInt(seperate[2]);
                String result = seperate[3];

                //teams
                int pTeamAway = Integer.parseInt(seperate[4]);
                int pTeamHome = Integer.parseInt(seperate[5]);
                Team away = getTeam(pTeamAway);
                Team home = getTeam(pTeamHome);

                // String main =  UserSQL.getInstance().get(seperate[6]);
                Referee newMain= (Referee) getUser(seperate[6]);

                //String ass1 =  UserSQL.getInstance().get(seperate[7]);
                Referee newAss1= (Referee) getUser(seperate[7]);
                // String ass2 =  UserSQL.getInstance().get(seperate[8]);
                Referee newAss2= (Referee) getUser(seperate[8]);
                //EventLog
                int EventLog=  Integer.parseInt(seperate[9]);
                List<String> events= EventLogSQL.getInstance().get(EventLog);



                List<AEvent> aEvents=new ArrayList<>();
                for(String str:events) {
                    String[] seperate2 = str.split(",");
                    int eventID = Integer.parseInt(seperate2[0]);
                    String date3 = seperate2[1];
                    Date theSameDate = new Date();

                    try {
                        theSameDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date3);
                    } catch (Exception e) {
                    }

                    int minute = Integer.parseInt(seperate2[2]);

                    String playerName = seperate2[3];
                    String teamName = seperate2[4];
                    int type = Integer.parseInt(seperate2[5]);

                    switch (type) {
                        case 1:
                            Goal goal = new Goal(eventID, theSameDate, minute, playerName, teamName);
                            aEvents.add(goal);
                            break;
                        case 2:
                            Injury injury = new Injury(eventID, theSameDate, minute, playerName, teamName);
                            aEvents.add(injury);
                            break;
                        case 3:
                            Offense offense = new Offense(eventID, theSameDate, minute, playerName, teamName);
                            aEvents.add(offense);
                            break;
                        case 4:
                            Offside offside = new Offside(eventID, theSameDate, minute, playerName, teamName);
                            aEvents.add(offside);
                            break;
                        case 5:
                            RedCard redCard = new RedCard(eventID, theSameDate, minute, playerName, teamName);
                            aEvents.add(redCard);
                            break;
                        case 6:
                            YellowCard yellowCard = new YellowCard(eventID, theSameDate, minute, playerName, teamName);
                            aEvents.add(yellowCard);
                            break;
                    }
                }
                EventLog eventLog=new EventLog(EventLog, aEvents);

                //leageInformation
                String leagueInformationString= ((String) LeagueInformationSQL.getInstance().get(Integer.parseInt(seperate[10])));
                //String p = leagueInformationID + " " + name + " " + winScore + " " + lossScore + " " + tieScore + " " + allocatePolicyCode+ " " +scorePolicyCode + " " +pFootballAssociation+ " " + pLeague+ " " +PSeason ;
                String[] splitleagueInformationString=leagueInformationString.split(" ");

                int leagueInformationID=Integer.parseInt(splitleagueInformationString[0]) ;
                String name= splitleagueInformationString[1];
                int winScore = Integer.parseInt(splitleagueInformationString[2]) ;
                int lossScore = Integer.parseInt(splitleagueInformationString[3]) ;
                int tieScore = Integer.parseInt(splitleagueInformationString[4]) ;

                ITeamAllocatePolicy iTeamAllocatePolicy;
                int piTeamAllocatePolicy=Integer.parseInt(splitleagueInformationString[5]);
                if( piTeamAllocatePolicy==1){
                    iTeamAllocatePolicy=new DefaultAllocate();
                }
                else if( piTeamAllocatePolicy==2){
                    iTeamAllocatePolicy=new OneGameAllocatePolicy();
                }

                IScoreMethodPolicy iScoreMethodPolicy= new DefaultMethod();
                int piScoreMethodPolicy=Integer.parseInt(splitleagueInformationString[6]);


                String footballAssociationString=splitleagueInformationString[7];
                FootballAssociation footballAssociation=(FootballAssociation) getUser(footballAssociationString);

                int pLeague= Integer.parseInt(splitleagueInformationString[8]);
                int PSeason = Integer.parseInt(splitleagueInformationString[9]);
                League league=getLeague(pLeague);
                Season season=new Season(PSeason);
                LeagueInformation leagueInformation= new LeagueInformation( league,season,footballAssociation);


                Game newGame = new Game(id, date2, hour, result, newMain, newAss1, newAss2, away, home,eventLog,leagueInformation);
                gameList.add(newGame);
                games.add(newGame);
            }//close flag if
        }
        return games;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setUsers (HashMap < String, User > users){
        this.users = users;
    }

    public void setLeagues (List < League > leagues) {
        this.leagues = leagues;
    }

    public void setTeams (List < Team > teams) {
        this.teams = teams;
    }

    public void setSeasons (List < Season > seasons) {
        this.seasons = seasons;
    }

    public void setFields (List < Field > fields) {
        this.fields = fields;
    }

    public void setRemovedUser (HashMap < String, User > removedUser){
        this.removedUser = removedUser;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void initSystem () { //UC-1
        //Object TaxServer;
        //Object AccountingServer;
        //In the future we will use this objects for getting/sending information from/to tax and accounting servers
        SystemEventLog.getInstance().writeToLog("Successfully connected to Tax FootballSystem.System");
        SystemEventLog.getInstance().writeToLog("Successfully connected to Accounting FootballSystem.System");
        SystemManager systemManager = new SystemManager(0, "Administrator", "2&^4BcE#@6", "Admin");
        this.addUser("Admin", systemManager);
        IScoreMethodPolicy iScoreMethod = new DefaultMethod();
        ITeamAllocatePolicy iScoreAllocate = new DefaultAllocate();
        scorePolicies.put(iScoreMethod.getClass().getSimpleName(), iScoreMethod);
        methodAllocatePolicies.put(iScoreAllocate.getClass().getSimpleName(), iScoreAllocate);
    } //UC-1

    /**
     * add field to the system fields
     * @param field
     */
    public void addField (Field field){
        fields.add(field);
    }

    /**
     * remove field from the system fields
     * @param field
     */
    public void removeField (Field field){
        fields.remove(field);
    }

    /**
     * Checks if league name already exist
     * @param name
     * @return
     */
    public boolean isLeagueExist (String name){
        for (League l : this.leagues) {
            if (name.equals(l.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if Season exist by compering years
     * @param year
     * @return
     */
    public boolean isSeasonExsit ( int year){
        for (Season season : seasons) {
            if (season.getIntYear() == year) {
                return true;
            }
        }
        return false;
    }

    public List<TeamManager> getAllTeamManager () {
        List<TeamManager> teamMangerList = new LinkedList<>();
        for (User user : users.values()) {
            if (user instanceof TeamManager) {
                teamMangerList.add((TeamManager) user);
            }
        }
        return teamMangerList;
    }

    public List<TeamOwner> getAllTeamOwner() {
        List <TeamOwner> teamOwnerList = new LinkedList<>();
        for(User user : users.values()){
            if(user instanceof TeamOwner){
                teamOwnerList.add((TeamOwner) user);
            }
        }
        List<String>l=UserSQL.getInstance().getAll();
        for(String userStr : l){
            String[] userArr = userStr.split(" ");
            if (userArr[0].equals("TeamOwner")) {
                   TeamOwner teamOwner = new TeamOwner(Integer.parseInt(userArr[1]),userArr[2],userArr[3],userArr[4],0);
                   // String teamOwnerStr="TeamOwner "+id_col+" "+ fullName_col+" "+ password_col+" "+username_col+" "+0;
                if(users.get(teamOwner.getName())==null){
                    teamOwnerList.add(teamOwner);
                }
            }
        }
        return teamOwnerList;
    }

    /**
     * get string of the policies
     * @return
     */
    public List<String> getScorePoliciesString () {
        List<String> list = new ArrayList<>();
//        for (int i = 0; i < scorePolicies.size(); i++) {
//            list.add(scorePolicies.keySet().toArray()[i].toString());
//        }
        list.add("DefaultMethod");
        return list;
    }

    public List<Player> getAllPlayers () {
        List<Player> players = new LinkedList<>();
        for (User user : users.values()) {
            if (user instanceof Player) {
                players.add((Player) user);
            }
        }
        return players;
    } //UC-4

    public List<SystemManager> getAllSystemManager () {
        List<SystemManager> sysList = new LinkedList<>();
        for (User user : users.values()) {
            if (user instanceof SystemManager) {
                sysList.add((SystemManager) user);
            }
        }
        return sysList;
    }

    public List<Coach> getAllCoach () {
        List<Coach> Coachs = new LinkedList<>();
        for (User user : users.values()) {
            if (user instanceof Coach) {
                Coachs.add((Coach) user);
            }
        }
        return Coachs;
    } //UC-4

    /**
     * add league to the system
     * @param league
     */
    public void addLeague (League league){
        leagues.add(league);
    }

    public Season getSeason (String year){
        for (Season s : seasons) {
            if (s.getYear().equals(year))
                return s;
        }
        return null;
    }

    public HashMap<String, User> getRemovedUsers () {
        return removedUser;
    }

    /**
     * add team to the system teams
     * @param team
     */
    public void addTeam (Team team){
        try{
        TeamSQL.getInstance().save(team);
        } catch (Exception e){}
        teams.add(team);
    }

    public void removeTeam (Team team){
        teams.remove(team);
    }

    /**
     * get string of the policies
     * @return
     */
    public List<String> getMethodAllocatePoliciesString () {
        List<String> list = new ArrayList<>();
//        for (int i = 0; i < methodAllocatePolicies.size(); i++) {
//            list.add(methodAllocatePolicies.keySet().toArray()[i].toString());
//        }
        list.add("DefaultAllocate");
        list.add("OneGameAllocatePolicy");

        return list;
    }

    public HashMap<String, ITeamAllocatePolicy> getMethodAllocatePolicies () {
        if(methodAllocatePolicies.size()!=0){
            return methodAllocatePolicies;
        }
        ITeamAllocatePolicy iTeamAllocatePolicy=new DefaultAllocate();
       methodAllocatePolicies.put("DefaultAllocate",iTeamAllocatePolicy);

        ITeamAllocatePolicy iTeamAllocatePolicy2=new OneGameAllocatePolicy();
        methodAllocatePolicies.put("OneGameAllocatePolicy",iTeamAllocatePolicy2);
        return methodAllocatePolicies;
    }

    public HashMap<String, IScoreMethodPolicy> getScorePolicies () {
        return scorePolicies;
    }


    //*************************  Necessary functions for iteration 3 + 4 ***********************************
    public User login (String userName, String password) throws WrongPasswordException, NoSuchAUserNamedException { //UC-3
        User user = this.getUser(userName);
        if (user == null) {
            throw new NoSuchAUserNamedException();
        }
        if (user.getPassword().equals(password)) {
            // user.setStatus(UserStatus.ACTIVE);
            //UserSQL.getInstance().delete(user);
            //UserSQL.getInstance().save(user);
            SystemEventLog.getInstance().writeToLog("User log in to the system. id(" + user.getId() + ").");
            users.put(userName,user);
            if(user instanceof Fan){
                for (int i=0;i<gameList.size();i++){
                    if(!(gameList.get(i).getiObserverGameListForFans().contains(user))) {
                        FanController.getInstance().followGame(((Fan) user), gameList.get(i));
                    }
                }
            }
            return user;
        }
        throw new WrongPasswordException();
    } //UC-3

    public void logOut (User user){ //UC-6
        user.setStatus(UserStatus.INACTIVE);
        SystemEventLog.getInstance().writeToLog("User log out from the system. id(" + user.getUserName() + ").");
    } //UC-6

    public Fan signUp ( int id, String name, String password, String userName) throws UserNameAlreadyExistException { //UC-2
        User user = users.get(userName);
        User user1 = removedUser.get(userName);
        if (user != null || user1 != null) {
            throw new UserNameAlreadyExistException();
        }//more details
        Fan fan = new Fan(id,name, password,userName);
        users.put(fan.getUserName(),fan);
//        UserSQL.getInstance().save(fan);
        SystemEventLog.getInstance().writeToLog("A new user signUp to the system. ("+fan.getId()+","+fan.getUserName()+").");
        return fan;
    } //UC-2

    /**
     * Checks if user name exist in the system
     * @param userName
     * @return
     */
    public boolean isUserNameExist (String userName){
        User user = users.get(userName);
        User user1 = removedUser.get(userName);
        if (user != null || user1 != null) {
            return true;
        }
        return false;
    }

    public List<Fan> getAllFan () {
        List<Fan> fanList = new LinkedList<>();
        for (User user : users.values()) {
            if (user instanceof Fan) {
                fanList.add((Fan) user);
            }
        }
        return fanList;
    }

    public List<Referee> getAllReferee () {
        List<Referee> referees = new LinkedList<>();
        for (User user : users.values()) {
            if (user instanceof Referee) {
                referees.add((Referee) user);
            }
        }
        return referees;
    } //UC-4

    /**
     * add the removed users to a list of users
     * @param userName unique nickname
     */
    public void removeUser (String userName) throws NoSuchAUserNamedException {
        if (users.get(userName) == null) {
            throw new NoSuchAUserNamedException();
        }
        users.get(userName).removeUser();
        users.get(userName).setStatus(UserStatus.REMOVED);
        removedUser.put(userName, users.get(userName));
        users.remove(userName);
        SystemEventLog.getInstance().writeToLog("User removed from the system. userName(" + userName + ").");
    }

    /**
     * restart a removed user to the system
     * @param userName
     */
    public void restartRemoveUser (String userName) throws NoSuchAUserNamedException {
        if (removedUser.get(userName) == null) {
            throw new NoSuchAUserNamedException();
        }
        users.put(userName, removedUser.get(userName));
        removedUser.remove(userName);
        users.get(userName).setStatus(UserStatus.INACTIVE);
        SystemEventLog.getInstance().writeToLog("Removed user restart to the system. userName(" + userName + ").");
    }

    /**
     * get all alerts for user
     * @param userName
     * @return
     */
    public List<String> getMyAlerts(String userName) {
        List<String> list=  AlertSQL.getInstance().get(userName);
        for (String s: list){
            List<String> toDelete= new LinkedList<>();
            toDelete.add(userName);
            toDelete.add(s);
            AlertSQL.getInstance().delete(toDelete);
        }
        return list;
    }

    /**
     * save alert for user in DB
     * @param userName
     * @param event
     */
    public void saveAlertToUser (String userName, String event){
        List<String> toSave = new LinkedList<>();
        toSave.add(userName);
        toSave.add(event);
        try {
            AlertSQL.getInstance().save(toSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //</editor-fold>


    ////SQL


}



