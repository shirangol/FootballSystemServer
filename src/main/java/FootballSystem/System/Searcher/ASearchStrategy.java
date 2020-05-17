package FootballSystem.System.Searcher;
import FootballSystem.System.Enum.SearchCategory;
import FootballSystem.System.FootballObjects.League;
import FootballSystem.System.FootballObjects.Season;
import FootballSystem.System.FootballObjects.Team.Team;
import FootballSystem.System.IShowable;
import FootballSystem.System.Users.Coach;
import FootballSystem.System.Users.Player;
import FootballSystem.System.Controller;

import java.util.LinkedList;
import java.util.List;

public abstract class ASearchStrategy {

    //<editor-fold desc="Methods">
    /**
     * Liste filter by category
     * @param list
     * @param filter
     * @return
     */
    public List<IShowable> filter(List<IShowable> list, SearchCategory filter){
        if(filter==SearchCategory.PLAYER){
            return getPlayers(list);
        }
        else if(filter==SearchCategory.COACH){
            return getCoaches(list);
        }
        else if (filter== SearchCategory.TEAM){
            return  getTeams(list);
        }
        else if (filter== SearchCategory.LEAGUE){
            return getLeagues(list);
        }
        else if(filter== SearchCategory.SEASON){
            return getSeasons(list);
        }

        return list;
    }

    /**
     * Get list by category
     * @param category
     * @return
     */
    protected List<IShowable> getListByCategory(SearchCategory category){
        Controller controller=Controller.getInstance();
        List<IShowable> list= new LinkedList<IShowable>();
        if(category==SearchCategory.DEFAULT){
            return getDefaultList();
        }
        if(category==SearchCategory.PLAYER){
            for (Player obj:controller.getAllPlayers()){
                list.add(obj);
            }
            return list;
        }
        if(category==SearchCategory.COACH){
            for (Coach obj:controller.getAllCoach()){
                list.add(obj);
            }
            return list;
        }
        if(category==SearchCategory.TEAM){
            for (Team obj:controller.getAllTeams()){
                list.add(obj);
            }
            return list;
        }
        if(category==SearchCategory.LEAGUE){
            for (League obj:controller.getAllLeagues()){
                list.add(obj);
            }
            return list;
        }
        else {
            for (Season obj:controller.getAllSeasons()){
                list.add(obj);
            }
            return list;
        }
    }

    public abstract List<IShowable> search(SearchCategory category, String str);
    //</editor-fold>

    //<editor-fold desc="Private Functions">
    /**
     * Get filtered list by player
     * @param list
     * @return
     */
    private List<IShowable> getPlayers(List<IShowable> list){
        List<IShowable> filterList= new LinkedList<IShowable>();
        for(IShowable obj: list){
            if(obj instanceof Player){
                filterList.add(obj);
            }
        }
        return filterList;
    }

    /**
     * Get filtered list by coach
     * @param list
     * @return
     */
    private List<IShowable> getCoaches(List<IShowable> list){
        List<IShowable> filterList= new LinkedList<IShowable>();
        for(IShowable obj: list){
            if(obj instanceof Coach){
                filterList.add(obj);
            }
        }
        return filterList;
    }

    /**
     * Get filtered list by team
     * @param list
     * @return
     */
    private List<IShowable> getTeams(List<IShowable> list){
        List<IShowable> filterList= new LinkedList<IShowable>();
        for(IShowable obj: list){
            if(obj instanceof Team){
                filterList.add(obj);
            }
        }
        return filterList;
    }

    /**
     * Get filtered list by season
     * @param list
     * @return
     */
    private List<IShowable> getSeasons(List<IShowable> list){
        List<IShowable> filterList= new LinkedList<IShowable>();
        for(IShowable obj: list){
            if(obj instanceof Season){
                filterList.add(obj);
            }
        }
        return filterList;
    }

    /**
     * Get filtered list by league
     * @param list
     * @return
     */
    private List<IShowable> getLeagues(List<IShowable> list){
        List<IShowable> filterList= new LinkedList<IShowable>();
        for(IShowable obj: list){
            if(obj instanceof League){
                filterList.add(obj);
            }
        }
        return filterList;
    }

    /**
     * Get list by default category
     * @return
     */
    private List<IShowable> getDefaultList(){
        Controller controller= Controller.getInstance();
        List<IShowable> list= new LinkedList<IShowable>();
        List<Player> playerList= controller.getAllPlayers();
        List<Coach> coachList= controller.getAllCoach();
        List<Team> teamList= controller.getAllTeams();
        List<League> leagueList= controller.getAllLeagues();
        List<Season> seasonList=controller.getAllSeasons();

        for(Player obj:playerList){
            list.add(obj);
        }
        for(Coach obj: coachList){
            list.add(obj);
        }
        for(Team obj: teamList){
            list.add(obj);
        }
        for(League obj: leagueList){
            list.add(obj);
        }
        for(Season obj: seasonList){
            list.add(obj);
        }
        return list;
    }
    //</editor-fold>

}
