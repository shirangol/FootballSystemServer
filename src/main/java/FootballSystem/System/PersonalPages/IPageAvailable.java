package FootballSystem.System.PersonalPages;

import FootballSystem.System.Exeptions.PersonalPageAlreadyExist;

public interface IPageAvailable {

    PersonalPage getPersonalPage();
    String showDetails();
    String getName();
    PersonalPage createPersonalPage() throws PersonalPageAlreadyExist;

}