import java.util.*;

// ================= RESERVATION =================
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId; // assigned during booking

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 1);
        data.put("Double Room", 1);
        data.put("Suite Room", 0);
    }

    public void increase(String type) {
        data.put(type, data.getOrDefault(type, 0) + 1);
    }

    public void display() {
        System.out.println("\n=== INVENTORY ===");
        for (String key : data.keySet()) {
            System.out.println(key + " → " + data.get(key));
        }
    }
}

// ================= BOOKING HISTORY =================
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void add(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation get(String id) {
        return history.get(id);
    }

    public void remove(String id) {
        history.remove(id);
    }

    public void display() {
        System.out.println("\n=== ACTIVE BOOKINGS ===");
        for (Reservation r : history.values()) {
            System.out.println(r.getReservationId() + " | " + r.getGuestName()
                    + " | " + r.getRoomType() + " | RoomID: " + r.getRoomId());
        }
    }
}

// ================= CANCELLATION SERVICE =================
class CancellationService {

    private BookingHistory history;
    private RoomInventory inventory;

    // Stack for rollback tracking (LIFO)
    private Stack<String> releasedRoomIds = new Stack<>();

    public CancellationService(BookingHistory history, RoomInventory inventory) {
        this.history = history;
        this.inventory = inventory;
    }

    public void cancel(String reservationId) {

        System.out.println("\nProcessing cancellation: " + reservationId);

        // Validate existence
        Reservation r = history.get(reservationId);

        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation not found");
            return;
        }

        // Step 1: Push room ID to stack (rollback tracking)
        releasedRoomIds.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.increase(r.getRoomType());

        // Step 3: Remove booking
        history.remove(reservationId);

        // Confirmation
        System.out.println("Cancellation SUCCESS for " + r.getGuestName());
        System.out.println("Released Room ID: " + r.getRoomId());
    }

    // Show rollback stack
    public void showRollbackStack() {
        System.out.println("\n=== ROLLBACK STACK (LIFO) ===");
        System.out.println(releasedRoomIds);
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Booking history (simulate confirmed bookings)
        BookingHistory history = new BookingHistory();
        history.add(new Reservation("R101", "Alice", "Single Room", "SI-101"));
        history.add(new Reservation("R102", "Bob", "Double Room", "DO-201"));

        // Show current state
        history.display();
        inventory.display();

        // Cancellation service
        CancellationService service = new CancellationService(history, inventory);

        // Perform cancellations
        service.cancel("R101"); // valid
        service.cancel("R999"); // invalid
        service.cancel("R102"); // valid

        // Final state
        history.display();
        inventory.display();
        service.showRollbackStack();
    }
}