abstract class Room {
    private int numberOfBeds;
    private double size;
    private double price;

    // Constructor
    public Room(int numberOfBeds, double size, double price) {
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    // Getters (Encapsulation)
    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method
    public abstract String getRoomType();

    // Common method
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 1500);
    }

    @Override
    public String getRoomType() {
        return "Single Room";
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 2500);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 5000);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}

// Main Class
public class HotelApp {
    public static void main(String[] args) {

        // Polymorphism
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("=== HOTEL ROOM DETAILS ===\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println("--------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println("--------------------------");

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailable);
        System.out.println("--------------------------");

        System.out.println("\nApplication Terminated.");
    }
}