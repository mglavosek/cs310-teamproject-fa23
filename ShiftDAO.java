package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Shift;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftDAO {

    private final Connection conn;

    public ShiftDAO(DAOFactory daoFactory) {
    this.conn = daoFactory.getConnection();
}


    public Shift find(int id) {
        try {
            String query = "SELECT * FROM shift WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String description = result.getString("description");
                int interval = result.getInt("interval");
                int gracePeriod = result.getInt("graceperiod");
                int dock = result.getInt("dock");
                int lunchDeduct = result.getInt("lunchdeduct");

                return new Shift(id, description, interval, gracePeriod, dock, lunchDeduct);
            } else{
              System.out.println("Shift not found with id: ");
            return null;}
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Shift find(Badge badge) {
        try {
            String query = "SELECT s.id, s.description, s.interval, s.graceperiod, s.dock, s.lunchdeduct " +
                "FROM shift s " +
                "JOIN employee e ON s.id = e.shiftid " +
                "WHERE e.badgeid = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, badge.getId());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int id = result.getInt("id");
                String description = result.getString("description");
                int interval = result.getInt("interval");
                int gracePeriod = result.getInt("graceperiod");
                int dock = result.getInt("dock");
                int lunchDeduct = result.getInt("lunchdeduct");

                return new Shift(id, description, interval, gracePeriod, dock, lunchDeduct);
            } else{
              System.out.println("Shift not found with id: ");
            return null;}
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
