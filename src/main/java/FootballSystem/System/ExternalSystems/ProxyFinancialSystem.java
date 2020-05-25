package FootballSystem.System.ExternalSystems;

public class ProxyFinancialSystem implements IFinancialSystem {

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        FinancialSystem financialSystem = new FinancialSystem();
        return financialSystem.addPayment(teamName,date,amount);
    }

}
