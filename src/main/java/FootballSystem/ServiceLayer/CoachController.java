package FootballSystem.ServiceLayer;

import FootballSystem.System.Exeptions.AlreadyHasPageException;
import FootballSystem.System.PersonalPages.PersonalPage;
import FootballSystem.System.Users.Coach;


public class CoachController extends MainUserController {

    private static CoachController ourInstance = new CoachController();

    public static CoachController getInstance() {
        return ourInstance;
    }

    private CoachController() {
    }

    public String getDetails(Coach coach){
        return coach.getDetails();
    }

    public void setDetails(Coach coach, int id, String name, String password,String preparation){
        FanController fanController = FanController.getInstance();
        fanController.editDetails(coach,id,name,password);
        if(!preparation.equals("")){
            coach.setPreparation(preparation);
        }

    }

    public void createPersonalPage(Coach coach) throws AlreadyHasPageException {
        if(coach.getPersonalPage()!=null) {
            throw new AlreadyHasPageException();
        }
        else{
            PersonalPage personalPage = new PersonalPage(coach);
            coach.setPersonalPage(personalPage);
        }
    }

    public void editPersonalPage(Coach coach, String newContent){
        PersonalPage personalPage = coach.getPersonalPage();
        personalPage.upload(newContent);
    }

}
