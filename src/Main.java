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

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ================= ADD-ON SERVICE =================
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// ================= SERVICE MANAGER =================
class AddOnServiceManager {

    // Map → Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println(service.getServiceName() + " added to " + reservationId);
    }

    // Show services for a reservation
    public void showServices(String reservationId) {

        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.getServiceName() + " (₹" + s.getCost() + ")");
        }
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) {

        // Existing reservation (already booked earlier)
        Reservation r1 = new Reservation("R101", "Alice", "Single Room");

        // Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService("R101", new AddOnService("Breakfast", 200));
        manager.addService("R101", new AddOnService("Airport Pickup", 500));
        manager.addService("R101", new AddOnService("Extra Bed", 300));

        // Display services
        manager.showServices("R101");

        // Calculate cost
        double total = manager.calculateTotalCost("R101");

        System.out.println("\nTotal Add-On Cost: ₹" + total);

        System.out.println("\nCore booking & inventory unchanged.");
    }
}