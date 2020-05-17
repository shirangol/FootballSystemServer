package FootballSystem.System;

import java.io.File;
import java.io.IOException;

public class SystemErrorLog extends Logger{


        //<editor-fold desc="Fields">
        //Implements a singleton logger instance
        private static final SystemErrorLog instance = new SystemErrorLog();
        //Retrieve the execution directory. Note that this is whereEver this process was launched
        protected String env = System.getProperty("user.dir");
        private static File logFile;
        //</editor-fold>

        //<editor-fold desc="Constructor">
        private SystemErrorLog(){
            if (instance != null){
                //Prevent Reflection
                throw new IllegalStateException("Cannot instantiate a new singleton instance of log");
            }
            this.createLogFile();
        }

        public static SystemErrorLog getInstance(){
            return instance;
        }
        //</editor-fold>

        //<editor-fold desc="Methods">
        public void createLogFile(){
            SystemErrorLog.logFile=createLogFile(env,"Football_Association_System_Error_Log");
        }

        /**
         * write a string to the log file of the system
         * @param message to be write in the log file
         */
        public void writeToLog(String message){
            writeToLog(message, SystemErrorLog.logFile);
        }

        /**
         * present the log file as string
         * @return
         */
        public String getLog() throws IOException {
            return getLog("Football_Association_System_Error_Log");

        } //UC-28
        //</editor-fold>

    }

