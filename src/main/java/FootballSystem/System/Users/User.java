package FootballSystem.System.Users;

import FootballSystem.System.Enum.UserStatus;
import FootballSystem.System.SystemEventLog;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public abstract class User extends Guest {

    //<editor-fold desc="Fields">
    protected int id;
    protected String name;
    protected String password;
    protected String userName;
    private List<String> searchHistory;
    protected UserStatus status;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public User( @JsonProperty("id")int id,@JsonProperty("name") String name,@JsonProperty("password") String password, @JsonProperty("user_Name")String userName) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userName = userName;
        this.searchHistory= new LinkedList<>();
        this.status= UserStatus.INACTIVE;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getSearchHistory() {
        return searchHistory;
    }

    public UserStatus getStatus() {
        return status;
    }

    /**
     * Get string of personal details
     * @return
     */
    public String getPersonalSDetails(){
        String str= "ID: "+ id+ "\n" + "User name: "+ userName+"\n";
        return str;
    } //UC-13
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
        SystemEventLog.getInstance().writeToLog(name+"(id: "+ id+ ") update his name");

    }

    public void setPassword(String password) {
        this.password = password;
        SystemEventLog.getInstance().writeToLog(name+"(id: "+ id+ ") update his password");
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SystemEventLog.getInstance().writeToLog(name+"(id: "+ id+ ") update his userName");

    }

    public void setSearchHistory(List<String> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
        SystemEventLog.getInstance().writeToLog(name+"(id: "+ id+ ") update his status");

    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    /**
     * Adding search to search history
     * @param line
     * @return
     */
    public List<String> addSearchHistory(String line){
        this.searchHistory.add(line);
        return searchHistory;
    }

    public abstract void removeUser() ;
    //</editor-fold>

}
