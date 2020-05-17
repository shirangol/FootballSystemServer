package FootballSystem.System.Searcher;
import FootballSystem.System.Enum.SearchCategory;
import FootballSystem.System.IShowable;

import java.util.LinkedList;
import java.util.List;

public class SearchByKeyWord extends ASearchStrategy {

    //<editor-fold desc="Override Methods">
    @Override
    public List<IShowable> search(SearchCategory category, String str) {
        List<IShowable> list= getListByCategory(category);
        List<IShowable> searchList= new LinkedList<IShowable>();
        for (IShowable obj: list){
            if(obj.getDetails().contains(str)){
                searchList.add(obj);
            }
        }
        return searchList;
    }
    //</editor-fold>

}
