package FootballSystem.System;

import java.io.File;
import java.io.IOException;

public class SystemEventLog extends Logger{


        //<editor-fold desc="Fields">
        //Implements a singleton logger instance
        private static final SystemEventLog instance = new SystemEventLog();
        //Retrieve the execution directory. Note that this is whereEver this process was launched
        protected String env = System.getProperty("user.dir");
        private static File logFile;
        //</editor-fold>

        //<editor-fold desc="Constructor">
        private SystemEventLog(){
            if (instance != null){
                //Prevent Reflection
                throw new IllegalStateException("Cannot instantiate a new singleton instance of log");
            }
            this.createLogFile();
        }

        public static SystemEventLog getInstance(){
            return instance;
        }
        //</editor-fold>

        //<editor-fold desc="Methods">
        public void createLogFile(){
            SystemEventLog.logFile=createLogFile(env,"Football_Association_System_Event_Log");
        }

        /**
         * write a string to the log file of the system
         * @param message to be write in the log file
         */
        public void writeToLog(String message){
            writeToLog(message, SystemEventLog.logFile);
        }

        /**
         * present the log file as string
         * @return
         */
        public String getLog() throws IOException {
            return getLog("Football_Association_System_Event_Log");

        } //UC-28
        //</editor-fold>

    }

