package FootballSystem.System.ExternalSystems;

import FootballSystem.System.SystemEventLog;

public class TaxSystem implements  ITaxSystem {

    @Override
    public double getTaxRate(double revenueAmount) {
        return 0;
    }

    public boolean connect(){
        SystemEventLog.getInstance().writeToLog("Successful confection to Tax System");
        return true;
    }

}
