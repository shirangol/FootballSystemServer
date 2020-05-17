package FootballSystem.ServiceLayer;

import FootballSystem.System.PersonalPages.PersonalPage;
import FootballSystem.System.Users.Player;
import FootballSystem.System.Exeptions.*;

import java.util.Date;

public class PlayerController extends MainUserController{

    private static PlayerController ourInstance = new PlayerController();

    public static PlayerController getInstance() {
        return ourInstance;
    }

    private PlayerController() {
    }

    public String getDetails(Player player){
        return player.getDetails();
    }

    public void setDetails(Player player, int id, String name, String password, Date date, String role){
        FanController fanController = FanController.getInstance();
        fanController.editDetails(player,id,name,password);
        if(date!=null){
            player.setBirthDate(date);
        }
        if(!role.equals("")){
            player.setRole(role);
        }
    }

    public void createPersonalPage(Player player) throws AlreadyHasPageException {
        if(player.getPersonalPage()!=null) {
            throw new AlreadyHasPageException();
        }
        else{
            PersonalPage personalPage = new PersonalPage(player);
            player.setPersonalPage(personalPage);
        }
    }

    public void editPersonalPage(Player player, String newContent){
        PersonalPage personalPage = player.getPersonalPage();
        personalPage.upload(newContent);
    }

}
