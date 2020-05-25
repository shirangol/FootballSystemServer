package FootballSystem.System.FootballObjects.Event;

import java.util.Date;

public class YellowCard extends AEvent {

    //<editor-fold desc="Constructor">
    public YellowCard(int minuteInTheGame, String playerName ,String teamName) {
        super(minuteInTheGame,playerName,teamName);
    }
    public YellowCard(int id, Date date, int minuteInTheGame, String playerName , String teamName) {

        super( id,date,minuteInTheGame,playerName ,teamName);
    }

    //</editor-fold>
    @Override
    public String getType() {
        return "YellowCard";
    }

    @Override
    public String getTypeToAlert() {
        return "yellow card";
    }
}
