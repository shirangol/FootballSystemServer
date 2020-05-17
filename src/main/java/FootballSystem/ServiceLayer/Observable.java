package FootballSystem.ServiceLayer;

public interface Observable {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyUI();

}
