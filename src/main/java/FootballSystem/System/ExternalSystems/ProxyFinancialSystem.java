package FootballSystem.System.ExternalSystems;

import FootballSystem.System.SystemEventLog;

public class ProxyFinancialSystem implements IFinancialSystem {

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        FinancialSystem financialSystem = new FinancialSystem();
        return financialSystem.addPayment(teamName,date,amount);
    }
    public boolean connect(){
        SystemEventLog.getInstance().writeToLog("Successful confection to Financial System");
        return true;
    }

}
