/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23.dao;



import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Department;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import edu.jsu.mcis.cs310.tas_fa23.EmployeeType;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Timestamp;

/**
 *
 * @author Daythyn
 */
public class EmployeeDAO {
    
    private static final String QUERY_FIND_ID = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND_BADGE = "SELECT * FROM employee WHERE badgeid = ?";

    private final DAOFactory daoFactory;

    EmployeeDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    
    public Employee find(Badge badgeId){
        Employee employee = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND_BADGE);
                ps.setString(1, badgeId.getId());
                boolean hasresults = ps.execute();
                if (hasresults) {
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        
                        int id = rs.getInt("id");
                        employee = find(id);  
                        
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
        return employee;
    }

    public Employee find(int id) {
        Employee employee = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND_ID);
                ps.setInt(1, id);
                boolean hasresults = ps.execute();
                if (hasresults) {
                    rs = ps.getResultSet();
                    while (rs.next()) {

                        String firstname = rs.getString("firstname");
                        String middlename = rs.getString("middlename");
                        String lastname = rs.getString("lastname");
                        
                        
                        int employeeTypeId = rs.getInt("employeetypeid");
                        EmployeeType eType = null;
                        switch(employeeTypeId){
                            case 0:
                                eType = EmployeeType.PART_TIME;
                                break;
                            case 1:
                                eType = EmployeeType.FULL_TIME;
                        }
                        
                        
                        BadgeDAO badgeDao = daoFactory.getBadgeDAO();
                        Badge badge = badgeDao.find(rs.getString("badgeid"));
                        
                        DepartmentDAO departmentDao = daoFactory.getDepartmentDAO();
                        Department department = departmentDao.find(rs.getInt("departmentid"));
                        
                        ShiftDAO shiftDao = daoFactory.getShiftDAO();
                        Shift shift = shiftDao.find(rs.getInt("shiftid"));
                        
                        
                        Timestamp sqlTime = rs.getTimestamp("active");
                        LocalDateTime active = sqlTime.toLocalDateTime();
                        active = active.withSecond(0).withNano(0);
                        
                        employee = new Employee(id, badge, firstname, middlename, lastname, eType, department, shift, active);
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
        return employee;
    }
}
