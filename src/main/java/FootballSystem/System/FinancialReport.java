package FootballSystem.System;

import FootballSystem.System.FootballObjects.Team.Team;

import java.util.Date;

public class FinancialReport {

    //<editor-fold desc="Fields">
    private final Team team;
    private int incomeFromGame;
    private int maintenanceFieldCost;
    private int payingSalary;
    private int income;
    private int expanse;
    private Date date;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public FinancialReport(Team team){
        this.team=team;
        maintenanceFieldCost=team.getField().getMaintenanceCost();
        income=team.getIncome();
        expanse=team.getExpense();
        payingSalary=team.getPaymentSalary();
        this.date=new Date(System.currentTimeMillis());
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getIncomeFromGame() {
        return incomeFromGame;
    }

    public int getMaintenanceFieldCost() {
        return maintenanceFieldCost;
    }

    public int getPayingSalary() {
        return payingSalary;
    }

    public int getIncome() {
        return income;
    }

    public int getExpanse() {
        return expanse;
    }
    //</editor-fold>

    //<editor-fold desc="Setter">
    private void setIncomeFromGame(int incomes){
        incomeFromGame=incomes;
    }

    private void setMaintenanceFieldCost(int maintenanceFieldCost) {
        this.maintenanceFieldCost = maintenanceFieldCost;
    }

    private void setPayingSalary(int payingSalary) {
        this.payingSalary = payingSalary;
    }

    private void setIncome(int income) {
        this.income = income;
    }

    private void setExpanse(int expanse) {
        this.expanse = expanse;
    }
    //</editor-fold>

}
