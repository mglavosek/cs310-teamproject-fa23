package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import static edu.jsu.mcis.cs310.tas_fa23.EventType.*;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.Shift;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {

    
    public static String getPunchListAsJSON(ArrayList<Punch> dailyPunchList){
        
        ArrayList<HashMap<String,String>> jsonData = new ArrayList<>();
        
        for(Punch punch : dailyPunchList){
            
            HashMap<String,String> punchData = new HashMap<>();
            //[{\"originaltimestamp\":\"FRI 09\\/07\\/2018 06:50:35\",\"badgeid\":\"28DC3FB8\",\"adjustedtimestamp\":\"FRI 09\\/07\\/2018 07:00:00\",\"adjustmenttype\":\"Shift Start\",\"terminalid\":\"104\",\"id\":\"3634\",\"punchtype\":\"CLOCK IN\"}
            
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginalTimeStamp().format(DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss")).toUpperCase()));
            punchData.put("badgeid", String.valueOf(punch.getBadge().getId()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedTimeStamp().format(DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss")).toUpperCase()));
            punchData.put("adjustmenttype", String.valueOf(punch.getAdjustmentType()));
            punchData.put("terminalid", String.valueOf( punch.getTerminalId()));
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("punchtype", String.valueOf(punch.getPunchType()));
            
            
            
            
            jsonData.add(punchData);
        }
        
        String json = Jsoner.serialize(jsonData);
        return json;
    }
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailyPunchList, Shift shift){
        int total = 0;
        
        Punch punchIn = null;
        Punch punchOut = null;
        int iter = 0;
        
        
        //Rework to deduct for lunch thresh AND don`t do stuff with timeouts
        while(iter < dailyPunchList.size()){
            punchIn = dailyPunchList.get(iter);
            iter++;
            
            if(punchIn.getPunchType() == CLOCK_IN){
                punchOut = dailyPunchList.get(iter);
                iter++;
                
                if (punchOut.getPunchType() == CLOCK_OUT){
                    total += ChronoUnit.MINUTES.between(punchIn.getAdjustedTimeStamp(), punchOut.getAdjustedTimeStamp());
                }
            }
        }
        
        if(dailyPunchList.size() < 4 && total>shift.getLunchThreshhold()){
            total -= ChronoUnit.MINUTES.between(shift.getLunchStart(), shift.getLunchStop());
        }
        return total;
    }
    
 
} 
  