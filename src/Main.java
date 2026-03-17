import java.util.*;

// ================= CUSTOM EXCEPTION =================
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
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

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 2);
        data.put("Double Room", 1);
        data.put("Suite Room", 0);
    }

    public boolean isValidRoomType(String type) {
        return data.containsKey(type);
    }

    public int getAvailability(String type) {
        return data.getOrDefault(type, 0);
    }

    public void reduce(String type) throws InvalidBookingException {
        int count = getAvailability(type);

        if (count <= 0) {
            throw new InvalidBookingException("No availability for " + type);
        }

        data.put(type, count - 1);
    }
}

// ================= VALIDATOR =================
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Check null/empty
        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Check availability
        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + r.getRoomType());
        }
    }
}

// ================= BOOKING SERVICE =================
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void book(Reservation r) {

        try {
            // 🔥 Fail-Fast Validation
            BookingValidator.validate(r, inventory);

            // If valid → proceed
            inventory.reduce(r.getRoomType());

            System.out.println("Booking SUCCESS for " + r.getGuestName()
                    + " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)
        service.book(new Reservation("Alice", "Single Room"));   // ✅ success
        service.book(new Reservation("", "Double Room"));        // ❌ empty name
        service.book(new Reservation("Bob", "Luxury Room"));     // ❌ invalid type
        service.book(new Reservation("Charlie", "Suite Room"));  // ❌ no availability
        service.book(new Reservation("David", "Single Room"));   // ✅ success
        service.book(new Reservation("Eve", "Single Room"));     // ❌ becomes unavailable

        System.out.println("\nSystem continues running safely.");
    }
}