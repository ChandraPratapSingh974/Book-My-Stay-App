import java.io.*;
import java.util.*;

// ================= RESERVATION =================
class Reservation implements Serializable {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public String toString() {
        return guestName + " (" + roomType + ")";
    }
}

// ================= INVENTORY =================
class RoomInventory implements Serializable {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 2);
        data.put("Double Room", 1);
    }

    public void bookRoom(String type) {
        int available = data.getOrDefault(type, 0);
        if (available > 0) {
            data.put(type, available - 1);
        }
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }

    public void display() {
        System.out.println("Inventory: " + data);
    }
}

// ================= BOOKING HISTORY =================
class BookingHistory implements Serializable {
    private List<Reservation> bookings = new ArrayList<>();

    public void add(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getBookings() {
        return bookings;
    }

    public void display() {
        System.out.println("Booking History: " + bookings);
    }
}

// ================= PERSISTENCE SERVICE =================
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // SAVE STATE
    public static void save(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(inventory);
            out.writeObject(history);

            System.out.println("✅ Data saved successfully!");

        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    // LOAD STATE
    public static Object[] load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();

            System.out.println("✅ Data loaded successfully!");

            return new Object[]{inventory, history};

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No saved data found. Starting fresh...");
        } catch (Exception e) {
            System.out.println("❌ Corrupted data. Starting fresh...");
        }

        return null;
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        RoomInventory inventory;
        BookingHistory history;

        // 🔥 LOAD EXISTING DATA
        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        // Simulate booking
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");

        inventory.bookRoom(r1.getRoomType());
        history.add(r1);

        inventory.bookRoom(r2.getRoomType());
        history.add(r2);

        // Display current state
        inventory.display();
        history.display();

        // 🔥 SAVE STATE BEFORE EXIT
        PersistenceService.save(inventory, history);

        System.out.println("\nSystem state persisted. Restart to test recovery.");
    }
}




