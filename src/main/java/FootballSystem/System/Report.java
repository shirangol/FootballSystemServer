package FootballSystem.System;

import FootballSystem.System.Users.User;

public class Report {

    //<editor-fold desc="Fields">
    private static int ID =1;
    private int id;
    private User user;
    private String report;
    private String answer;
    //</editor-fold>
    
    //<editor-fold desc="Constructor">
    public Report(User user, String report) {
        id=ID;
        ID++;
        this.user = user;
        this.report = report;
        this.answer="";
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getReport() {
        return report;
    }

    public String getAnswer() {
        return answer;
    }

    /**
     * Get report details
     * @return
     */
    public String getReportDetails(){
        String str= "Report ID: "+ id + "\n"
                + "User: "+ user.getName() +"\n"
                + "Content: "+ report
                + "Answer: " + answer;
        return str;
    } //UC-27
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * Answer to report
     * @param answer
     */
    public void answer(String answer){
        this.answer= answer;
        Log.getInstance().writeToLog("system manager receive to report "+ id);

    } //UC-27
    //</editor-fold>

}
