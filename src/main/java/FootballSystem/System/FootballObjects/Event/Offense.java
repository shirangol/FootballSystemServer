package FootballSystem.System.FootballObjects.Event;

import java.util.Date;

public class Offense extends AEvent {

    //<editor-fold desc="Constructor">
    public Offense(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }
    public Offense(int id, Date date, int minuteInTheGame, String playerName , String teamName) {

        super( id,date,minuteInTheGame,playerName ,teamName);
    }


    //</editor-fold>
    @Override
    public String getType() {
        return "Offense";
    }

    @Override
    public String getTypeToAlert() {
        return "offense";
    }
}
