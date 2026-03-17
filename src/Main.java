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

// ================= INVENTORY (NOT USED HERE FOR UPDATE) =================
class RoomInventory {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 5);
        data.put("Double Room", 3);
        data.put("Suite Room", 2);
    }

    public int getAvailability(String type) {
        return data.getOrDefault(type, 0);
    }
}

// ================= RESERVATION (REQUEST OBJECT) =================
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// ================= BOOKING QUEUE =================
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Request Added → " + r.getGuestName());
    }

    // View all queued requests
    public void showQueue() {
        System.out.println("\n=== BOOKING REQUEST QUEUE ===");

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Get next request (for future processing)
    public Reservation getNextRequest() {
        return queue.peek(); // only view, not remove
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        // Create Queue System
        BookingQueue bookingQueue = new BookingQueue();

        // Guest Requests (Arrival Order)
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double Room"));
        bookingQueue.addRequest(new Reservation("David", "Single Room"));

        // Display Queue (FIFO Order)
        bookingQueue.showQueue();

        // Peek next request (no removal)
        Reservation next = bookingQueue.getNextRequest();
        System.out.println("\nNext to be processed (FIFO):");
        if (next != null) {
            next.display();
        }

        System.out.println("\nNo inventory updated. Requests only queued.");
    }
}