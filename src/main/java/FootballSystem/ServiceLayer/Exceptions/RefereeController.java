package FootballSystem.ServiceLayer.Exceptions;

public class RefereeController {

    private static RefereeController ourInstance = new RefereeController();

    public static RefereeController getInstance() {
        return ourInstance;
    }

    private RefereeController() {
    }


}
