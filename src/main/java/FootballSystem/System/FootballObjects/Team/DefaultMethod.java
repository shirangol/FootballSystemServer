package FootballSystem.System.FootballObjects.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Inlay scoring calculation policy and league position
 * for:
 * WIN:3 points.
 * LOSS: 0 points.
 * TIE: 1 points
 */
public class DefaultMethod implements IScoreMethodPolicy {

    //<editor-fold desc="Methods">
    public List <Integer> setScorePolicy() {
        List <Integer> toReturn=new ArrayList();
        toReturn.add(3);
        toReturn.add(0);
        toReturn.add(1);
        return toReturn;
    }
    //</editor-fold>

}
