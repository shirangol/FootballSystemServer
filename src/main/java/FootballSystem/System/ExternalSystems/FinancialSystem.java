package FootballSystem.System.ExternalSystems;

import FootballSystem.ServiceLayer.Exceptions.OnlyForReferee;
import FootballSystem.System.SystemErrorLog;
import FootballSystem.System.SystemEventLog;

public class FinancialSystem implements IFinancialSystem {

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        return true;
    }

    public boolean connect(){
        SystemEventLog.getInstance().writeToLog("Successful confection to Financial System");
        return true;
    }

}
