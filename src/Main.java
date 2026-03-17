import java.util.*;

// ================= DOMAIN =================
abstract class Room {
    private int beds;
    private double price;

    public Room(int beds, double price) {
        this.beds = beds;
        this.price = price;
    }

    public abstract String getRoomType();
}

class SingleRoom extends Room {
    public SingleRoom() { super(1, 1500); }
    public String getRoomType() { return "Single Room"; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super(2, 2500); }
    public String getRoomType() { return "Double Room"; }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 5000); }
    public String getRoomType() { return "Suite Room"; }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 2);
        data.put("Double Room", 1);
        data.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return data.getOrDefault(type, 0);
    }

    public void reduce(String type) {
        int count = data.getOrDefault(type, 0);
        if (count > 0) {
            data.put(type, count - 1);
        }
    }

    public void display() {
        System.out.println("\n=== INVENTORY ===");
        for (String key : data.keySet()) {
            System.out.println(key + " → " + data.get(key));
        }
    }
}

// ================= RESERVATION =================
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// ================= QUEUE =================
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void add(Reservation r) {
        queue.offer(r);
    }

    public Reservation next() {
        return queue.poll(); // remove (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ================= BOOKING SERVICE =================
class BookingService {

    private RoomInventory inventory;

    // Track ALL allocated room IDs (no duplicates)
    private Set<String> allAllocatedIds = new HashSet<>();

    // Map room type → assigned room IDs
    private Map<String, Set<String>> allocatedMap = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String type) {
        String id;
        do {
            id = type.substring(0, 2).toUpperCase() + "-" + (int)(Math.random() * 1000);
        } while (allAllocatedIds.contains(id)); // ensure uniqueness

        return id;
    }

    // Process queue
    public void processQueue(BookingQueue queue) {

        System.out.println("\n=== PROCESSING BOOKINGS ===");

        while (!queue.isEmpty()) {
            Reservation r = queue.next();

            String type = r.getRoomType();
            int available = inventory.getAvailability(type);

            System.out.println("\nProcessing: " + r.getGuestName());

            // Check availability
            if (available > 0) {

                // Generate unique ID
                String roomId = generateRoomId(type);

                // Store in global set
                allAllocatedIds.add(roomId);

                // Store in map (type → IDs)
                allocatedMap.putIfAbsent(type, new HashSet<>());
                allocatedMap.get(type).add(roomId);

                // Update inventory (atomic step)
                inventory.reduce(type);

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Room Type: " + type);
                System.out.println("Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed (No availability)");
            }
        }
    }

    // Display allocated rooms
    public void showAllocations() {
        System.out.println("\n=== ALLOCATED ROOMS ===");

        for (String type : allocatedMap.keySet()) {
            System.out.println(type + " → " + allocatedMap.get(type));
        }
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Queue
        BookingQueue queue = new BookingQueue();
        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Single Room"));
        queue.add(new Reservation("Charlie", "Single Room")); // extra → fail
        queue.add(new Reservation("David", "Suite Room"));

        // Booking Service
        BookingService service = new BookingService(inventory);

        // Process requests
        service.processQueue(queue);

        // Show results
        service.showAllocations();
        inventory.display();
    }
}