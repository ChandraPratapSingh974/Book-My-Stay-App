import java.util.HashMap;
import java.util.Map;

// Abstract Room Class
abstract class Room {
    private int numberOfBeds;
    private double size;
    private double price;

    public Room(int numberOfBeds, double size, double price) {
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Room Classes
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 1500);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 2500);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 5000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// 🔥 Centralized Inventory Class
class RoomInventory {
    private Map<String, Integer> inventory;

    // Constructor initializes availability
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Add or initialize room type
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + change;

        if (updated < 0) {
            System.out.println("Cannot reduce below 0 for " + roomType);
        } else {
            inventory.put(roomType, updated);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n=== CURRENT ROOM INVENTORY ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}

// Main Application
public class HotelApp {
    public static void main(String[] args) {

        // Create Room Objects (Domain Model)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize Inventory (Centralized)
        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType(single.getRoomType(), 5);
        inventory.addRoomType(doubleRoom.getRoomType(), 3);
        inventory.addRoomType(suite.getRoomType(), 2);

        // Display Room Details
        System.out.println("=== HOTEL ROOM DETAILS ===\n");

        single.displayDetails();
        System.out.println("--------------------------");

        doubleRoom.displayDetails();
        System.out.println("--------------------------");

        suite.displayDetails();
        System.out.println("--------------------------");

        // Display Inventory
        inventory.displayInventory();

        // Perform Updates
        System.out.println("\nUpdating Inventory...");
        inventory.updateAvailability("Single Room", -2); // booking
        inventory.updateAvailability("Suite Room", +1);  // cancellation

        // Display Updated Inventory
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}