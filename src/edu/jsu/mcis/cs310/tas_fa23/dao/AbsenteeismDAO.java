
package edu.jsu.mcis.cs310.tas_fa23.dao;


import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import edu.jsu.mcis.cs310.tas_fa23.Absenteeism;
 
public class AbsenteeismDAO {
    private Connection conn;
    
    public AbsenteeismDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Find Method to get data from database 
    public Absenteeism find(Employee employee, LocalDate payPeriodStartDate) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Absenteeism am = null;
        int id;
        BigDecimal percentage;
        try {
            String sql = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiodstartdate = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employee.getId());
            pstmt.setDate(2, Date.valueOf(payPeriodStartDate));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                id = rs.getInt("id");
                percentage = rs.getBigDecimal("percentage");
                am = new Absenteeism(id, employee, payPeriodStartDate, percentage);
            }
            rs.close();
            pstmt.close();
        }
        catch (SQLException e) { e.printStackTrace(); }
        return am;
    }
    
    // Create Method to add new record to Database
    public void create(Absenteeism am) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO absenteeism (id, employeeid, payperiodstartdate, percentage) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, am.getId());
            pstmt.setInt(2, am.getEmployee().getId());
            pstmt.setDate(3, Date.valueOf(am.getPayPeriodStartDate()));
            pstmt.setBigDecimal(4, am.getAbsenteeismPercentage());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Udpate Method for updating the Database
   public void update(Absenteeism a) {
    PreparedStatement pstmt = null;
    try {
        String sql = "UPDATE absenteeism SET percentage = ? WHERE employeeid = ? AND payperiodstartdate = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setBigDecimal(1, a.getPercentage());
        pstmt.setInt(2, a.getEmployee().getId());
        pstmt.setDate(3, Date.valueOf(a.getPayPeriodStartDate()));
        pstmt.executeUpdate();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


}