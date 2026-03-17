import java.util.*;

// ================= RESERVATION =================
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// ================= BOOKING HISTORY =================
class BookingHistory {

    // Ordered storage
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void add(Reservation r) {
        history.add(r);
    }

    // Retrieve all bookings
    public List<Reservation> getAll() {
        return history;
    }
}

// ================= REPORT SERVICE =================
class BookingReportService {

    // Display all bookings
    public void showAllBookings(BookingHistory history) {

        System.out.println("\n=== BOOKING HISTORY ===");

        for (Reservation r : history.getAll()) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(BookingHistory history) {

        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : history.getAll()) {
            String type = r.getRoomType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        System.out.println("\n=== BOOKING SUMMARY REPORT ===");

        for (String type : countMap.keySet()) {
            System.out.println(type + " → " + countMap.get(type) + " bookings");
        }
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        // Booking History (acts like in-memory persistence)
        BookingHistory history = new BookingHistory();

        // Simulating confirmed bookings
        history.add(new Reservation("R101", "Alice", "Single Room"));
        history.add(new Reservation("R102", "Bob", "Suite Room"));
        history.add(new Reservation("R103", "Charlie", "Single Room"));
        history.add(new Reservation("R104", "David", "Double Room"));

        // Admin requests report
        BookingReportService reportService = new BookingReportService();

        // Show all bookings
        reportService.showAllBookings(history);

        // Generate summary
        reportService.generateSummary(history);

        System.out.println("\nHistory stored in order. Reporting is read-only.");
    }
}