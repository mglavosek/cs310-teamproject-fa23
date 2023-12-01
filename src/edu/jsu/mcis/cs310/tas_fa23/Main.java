package edu.jsu.mcis.cs310.tas_fa23;

import edu.jsu.mcis.cs310.tas_fa23.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;

public class Main {

    //Daythyn Wuz Here

    public static void main(String[] args) {
        
        // test database connectivity; get DAOs

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        // find badge

        Badge b = badgeDAO.find("31A25435");
        
        // output should be "Test Badge: #31A25435 (Munday, Paul J)"
        
        System.err.println("Test Badge: " + b.toString());

    }
//permissions check
}
