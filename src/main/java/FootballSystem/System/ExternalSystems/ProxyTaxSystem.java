package FootballSystem.System.ExternalSystems;

import FootballSystem.System.SystemEventLog;

public class ProxyTaxSystem implements ITaxSystem {

    @Override
    public double getTaxRate(double revenueAmount) {
        TaxSystem taxSystem = new TaxSystem();
        return taxSystem.getTaxRate(revenueAmount);
    }

    public boolean connect(){
        SystemEventLog.getInstance().writeToLog("Successful confection to Tax System");
        return true;
    }

}
