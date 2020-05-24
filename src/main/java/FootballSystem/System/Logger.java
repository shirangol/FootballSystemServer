package FootballSystem.System;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public abstract class Logger {

    public File createLogFile(String env,String name){
        //Determine if a logs directory exists or not.
        File logsFolder = new File(env + '/' + "logs");
        if(!logsFolder.exists()){
            //Create the directory
            System.err.println("INFO: Creating new logs directory in " + env);
            logsFolder.mkdir();
        }
        //Get the current date and time
        Calendar cal = Calendar.getInstance();

        //Create the name of the file from the path and current time
        String logName =  name + ".log";
        File file = new File(logsFolder.getName(), logName);
        try{
            if(file.createNewFile()){
                System.err.println("INFO: Creating new log file");
            }
        }catch(IOException e){
            System.err.println("ERROR: Cannot create log file");
            System.exit(1);
        }
        return file;
    }

    /**
     * write a string to the log file of the system
     * @param message to be write in the log file
     */
    public void writeToLog(String message,File file){
        try{
            FileWriter out = new FileWriter(file, true);
            Date date =new Date();
            String callerClassName = new Exception().getStackTrace()[2].getClassName().substring(7);
            message = date +" "+callerClassName+"."+Thread.currentThread().getStackTrace()[3].getMethodName()+": "+message+"\n";
            out.write(message.toCharArray());
            System.err.println(message);
            out.close();
        }catch(IOException e){
            System.err.println("ERROR: Could not write to log file");
        }
    }


    /**
     * present the log file as string
     * @return
     */
    public String getLog(String name) throws IOException {
        String strLine="";
        String logToString="";
        try{
            String path = System.getProperty("user.dir") + '/' + "logs/"+name+".log";
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            /* read log line by line */
            strLine = br.readLine();
            while (strLine != null) {

                /* parse strLine to obtain what you want */

                logToString+= strLine;
                logToString+="\n";
                strLine = br.readLine();
            }
            fstream.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return logToString;
    } //UC-28
    }