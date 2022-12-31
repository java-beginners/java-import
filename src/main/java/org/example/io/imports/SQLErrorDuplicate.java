package org.example.io.imports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SQLErrorDuplicate {
    Logger logger = LogManager.getLogger();
    public void PrintError(String filename, List<String> user,int lineNumBer){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Main m= new Main();
        String s= new UserQueryToString().chageString(user);
        System.out.println("{\"@timestamp\":\""+now+"\","+"\"app\":\"import users\",\"env\":\"dev\",\"fileName\":\""+m.getFilename()+"\",\"level\":\"error\",\"lineNo\":"+lineNumBer+",\"message\":\"Error to write:\\u0026{"+s);
        //System.out.print("{\"@timestamp\":");
        //logger.error(",\"level\":\"info\",\"message\":\"Import file\"   \n" +
        //        "user:"+user+"}");
    }
}
