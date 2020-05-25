package FootballSystem.System.FootballObjects.Event;

import java.util.Date;

public class RedCard extends AEvent {

    //<editor-fold desc="Constructor">
    public RedCard(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }
    public RedCard(int id, Date date, int minuteInTheGame, String playerName , String teamName) {

        super( id,date,minuteInTheGame,playerName ,teamName);
    }
    //</editor-fold>

    @Override
    public String getType() {
        return "RedCard";
    }

    @Override
    public String getTypeToAlert() {
        return "red card";
    }
}
