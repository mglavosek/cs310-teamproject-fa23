package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.EventType;
import java.time.LocalDateTime;
import java.sql.*;


public class PunchDAO {
    
   private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";

   private final DAOFactory daoFactory;
   
   PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
   
    public Punch find(int id) {

        Punch punch = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

    try {

        Connection conn = daoFactory.getConnection();
        
        if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

        if (hasresults) {

                    rs = ps.getResultSet();

        while (rs.next()) {

                    LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                    int terminalId = rs.getInt("terminalid");
                    Badge badge = new Badge(rs.getString("badgeid"),null);

                     EventType event = null;
         
        switch (rs.getInt("eventtypeid")) {
                         
                            case 0:
                                event = EventType.CLOCK_OUT;
                                break;
                            case 1:
                                event = EventType.CLOCK_IN;
                                break;
                            case 2:
                                event = EventType.TIME_OUT;
                                break;
                        }
}
