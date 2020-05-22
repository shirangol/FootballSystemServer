package FootballSystem.System.ExternalSystems;

public class ProxyTaxSystem implements ITaxSystem {

    @Override
    public double getTaxRate(double revenueAmount) {
        TaxSystem taxSystem = new TaxSystem();
        return taxSystem.getTaxRate(revenueAmount);
    }

}
