package FootballSystem.System.Searcher;
import FootballSystem.System.IShowable;
import FootballSystem.System.Enum.SearchCategory;

import java.util.List;

public class SearchByCategory extends ASearchStrategy {

    //<editor-fold desc="Override Methods">
    @Override
    public List<IShowable> search(SearchCategory category, String str) {
        return getListByCategory(category);
    }
    //</editor-fold>

}
