import java.util.*;

// ================= DOMAIN MODEL =================
abstract class Room {
    private int beds;
    private double price;

    public Room(int beds, double price) {
        this.beds = beds;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();

    public void display() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 1500);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 2500);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 5000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 5);
        data.put("Double Room", 0); // unavailable
        data.put("Suite Room", 2);
    }

    // READ ONLY METHOD
    public int getAvailability(String type) {
        return data.getOrDefault(type, 0);
    }
}

// ================= SEARCH SERVICE =================
class SearchService {

    // Read-only search
    public void search(List<Room> rooms, RoomInventory inventory) {

        System.out.println("=== AVAILABLE ROOMS ===\n");

        for (Room r : rooms) {

            int available = inventory.getAvailability(r.getRoomType());

            // Validation (Defensive Programming)
            if (available > 0) {
                r.display();
                System.out.println("Available: " + available);
                System.out.println("----------------------");
            }
        }
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        // Room objects (Domain)
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Inventory (State)
        RoomInventory inventory = new RoomInventory();

        // Search (Read-only)
        SearchService service = new SearchService();

        // Guest action
        service.search(rooms, inventory);

        System.out.println("\nSystem state unchanged.");
    }
}