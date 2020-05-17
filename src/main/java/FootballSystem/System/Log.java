package FootballSystem.System;

import java.io.*;

public class Log extends Logger {

    //<editor-fold desc="Fields">
    //Implements a singleton logger instance
    private static final Log instance = new Log();
    //Retrieve the execution directory. Note that this is whereEver this process was launched
    protected String env = System.getProperty("user.dir");
    private static File logFile;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    private Log(){
        if (instance != null){
            //Prevent Reflection
            throw new IllegalStateException("Cannot instantiate a new singleton instance of log");
        }
        this.createLogFile();
    }

    public static Log getInstance(){
        return instance;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void createLogFile(){
        Log.logFile=createLogFile(env,"Football_Association_System_Log");
    }

    /**
     * write a string to the log file of the system
     * @param message to be write in the log file
     */
    public void writeToLog(String message){
        writeToLog(message,Log.logFile);
    }

    /**
     * present the log file as string
     * @return
     */
    public String getLog() throws IOException {
        return getLog("Football_Association_System_Log");

    } //UC-28
    //</editor-fold>

}