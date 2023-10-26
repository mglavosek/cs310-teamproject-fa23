package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.time.*;
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
            
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", String.valueOf(punch.getBadge()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("punchtype", String.valueOf(punch.getPunchType()));
            punchData.put("adjustmenttype", String.valueOf(punch.getAdjustmentType()));
            punchData.put("originaltimestamp", String.valueOf(punch.printOriginal()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.printAdjusted()));
            
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
        
        while(iter < dailyPunchList.size()){
            punchIn = dailyPunchList.get(iter);
            iter++;
            
            if(punchIn.getPunchType() == CLOCK_IN){
                punchOut = punchIn = dailyPunchList.get(iter);
                iter++;
                
                if (punchOut.getPunchType() == CLOCK_OUT){
                    total += ChronoUnit.MINUTES.between(punchIn.getAdjustedTimeStamp(), punchOut.getAdjustedTimeStamp());
                }
                
            }
            
        }
        System.out.println(total);
        return total;
    }
}