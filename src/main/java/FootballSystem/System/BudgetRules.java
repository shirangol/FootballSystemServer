package FootballSystem.System;

import java.util.List;

public class BudgetRules {

    //<editor-fold desc="Fields">
    private List<String> rules;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    private static BudgetRules ourInstance = new BudgetRules();

    public static BudgetRules getInstance() {
        return ourInstance;
    }

    private BudgetRules() {
    }
    //</editor-fold>

}
