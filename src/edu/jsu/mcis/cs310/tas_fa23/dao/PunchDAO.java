package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.*;
import java.time.LocalDateTime;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class PunchDAO {
    
   private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
   private static final String QUERY_FIND_LIST = "SELECT * FROM event WHERE badgeid = ? AND (timestamp LIKE ? OR timestamp LIKE ?) ORDER BY timestamp";
   private static final String QUERY_CREATE = "INSERT INTO event (terminalid, badgeid, timestamp, eventtypeid) VALUES (?, ?, ?, ?)";

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
                    
                    BadgeDAO badgeDao = daoFactory.getBadgeDAO();
                    Badge badge = badgeDao.find(rs.getString("badgeid"));

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
             punch = new Punch(id, terminalId, badge, timestamp, event);
        }
       }
      }
        
        } catch (SQLException e) {

          throw new DAOException(e.getMessage());

        } finally {

          if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
          
          if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
              }
            }
    
          return punch;
    }  
    
    
    public ArrayList<Punch> list(Badge badgeQ, LocalDate date){
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Punch> punchList = new ArrayList();
        
        try {

            Connection conn = daoFactory.getConnection();
        
            if (conn.isValid(0)) {
                
                String date1 = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
                String date2 = date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
                
                ps = conn.prepareStatement(QUERY_FIND_LIST);
                ps.setString(1, badgeQ.getId());
                ps.setString(2,date1);
                ps.setString(3,date2);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();
                    
                    while (rs.next()) {
                        
                            
                            //if it isn't a clock out on the next day, stop. 
                            if((rs.getInt("eventtypeid") == 1) && (date.getDayOfMonth() != rs.getTimestamp("timestamp").toLocalDateTime().getDayOfMonth())){
                                break;
                            }
                            
                            int id = rs.getInt("id");
                            Punch punch = (find(id));
                            
                            punchList.add(punch);

                    }
                }
            }
        } catch (SQLException e) {
          throw new DAOException(e.getMessage());
        } finally {
          if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
          if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return punchList;
    }
    
    
    
    
    public ArrayList<Punch> list(Badge badgeQ, LocalDate dateStart, LocalDate dateEnd){
        
        ArrayList<Punch> punchList = new ArrayList();
        
        
        try {
            
            //Quickly handles accidental single day Queries
            if(dateStart.isEqual(dateEnd)){
                punchList.addAll(list(badgeQ,dateStart));
                return punchList;
            }
            
            if(dateStart.isAfter(dateEnd)){
                LocalDate tempDate = dateStart;
                dateStart = dateEnd;
                dateEnd = tempDate;
                tempDate = null;
            }
            
            
            int days = (int) ChronoUnit.DAYS.between(dateStart, dateEnd);
            LocalDate tempDate = dateStart;
            
            while(!tempDate.isAfter(dateEnd)){
                punchList.addAll(list(badgeQ,tempDate));
                tempDate = tempDate.plusDays(1);
            }
            
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return punchList;
    }
    
    public int create(Punch punch)
    {
            
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        int key = 0, result = 0;
        int termID = punch.getTerminalId();
        String badgeID = punch.getBadge().getId();
        LocalDateTime ots = punch.getOriginalTimeStamp();
        ots = ots.withNano(0);
        EventType punchtype = punch.getPunchType();
        
        String timeString = ots.toString();
        int punchType = punchtype.ordinal();    
        
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
        
        Employee employee = employeeDAO.find(punch.getBadge());
        Department department = departmentDAO.find(employee.getId());
        
        int departmentid = department.getId();
        
        //STILL NEEDS VERIFICATION
        try 
        {
    
            Connection conn = daoFactory.getConnection();
                     
            if (conn.isValid(0)) 
            {
                ps = conn.prepareStatement(QUERY_CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, termID);
                ps.setString(2, badgeID);
                ps.setString(3, timeString);
                ps.setInt(4, punchType);
  
                //boolean hasresults = ps.execute();
                result = ps.executeUpdate();

                if (result == 1)
                {
                    rs = ps.getGeneratedKeys();
                    if (rs.next())
                    {
                        key = rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());
        } finally {
          if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
          if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return key;   //fix return value to return new punch ID if verified or 0 if otherwise.
    
    }
    
    
}
