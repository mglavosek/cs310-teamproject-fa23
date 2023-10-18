package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Shift;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftDAO {

    private static final String QUERY_FIND_SHIFT = "SELECT * FROM shift WHERE id = ?";
    
    private final Connection conn;
    private final DAOFactory daoFactory;

    ShiftDAO(DAOFactory daoFactory) 
    {
        this.daoFactory = daoFactory;
    }


    public Shift find(int id) {
        
        Shift shift = null;
        try {
            PreparedStatement statement = conn.prepareStatement(QUERY_FIND_SHIFT);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String description = result.getString("description");
                int interval = result.getInt("interval");
                int gracePeriod = result.getInt("graceperiod");
                int dock = result.getInt("dock");
                int lunchDeduct = result.getInt("lunchdeduct");

                shift = new Shift(id, description, interval, gracePeriod, dock, lunchDeduct);
            } else{
              System.out.println("Shift not found with id: ");
            return null;}
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int find(Badge badge) {
        
        int result;
        try {
            PreparedStatement statement = conn.prepareStatement();
            statement.setString(1, badge.getId());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                
                BadgeDAO badgeDao = daoFactory.getBadgeDAO();
                Badge badge = badgeDao.find(rs.getString("badgeid"));
                
                EmployeeDAO employeeDao = daoFactory.getEmployeeDAO();
                Employee employee = employeeDao.find(badge);
                result = employee.getShiftId();
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
