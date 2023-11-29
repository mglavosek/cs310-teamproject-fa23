package edu.jsu.mcis.cs310.tas_fa23;
import java.util.zip.CRC32;

public class Badge {

    private final String id, description;

    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    //Constructor 2
     public Badge(String description) {
        this.description = description;
        this.id = generateBadgeID(description);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    private String generateBadgeID(String description) {
        CRC32 newID = new CRC32();
        newID.update(description.getBytes());
        long checksum = newID.getValue();

        return String.format("%08X", checksum);
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');

        return s.toString();

    }

}
