import java.util.*;

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

// ================= SHARED INVENTORY =================
class RoomInventory {
    private Map<String, Integer> data = new HashMap<>();

    public RoomInventory() {
        data.put("Single Room", 2);
    }

    // 🔥 CRITICAL SECTION (synchronized)
    public synchronized boolean bookRoom(String type) {

        int available = data.getOrDefault(type, 0);

        if (available > 0) {
            // simulate delay (race condition possibility)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            data.put(type, available - 1);
            return true;
        }

        return false;
    }

    public void display() {
        System.out.println("\nFinal Inventory: " + data);
    }
}

// ================= SHARED QUEUE =================
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized add
    public synchronized void add(Reservation r) {
        queue.offer(r);
    }

    // synchronized remove
    public synchronized Reservation get() {
        return queue.poll();
    }
}

// ================= THREAD PROCESSOR =================
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation r;

            // 🔥 synchronized access to queue
            synchronized (queue) {
                r = queue.get();
            }

            if (r == null) break;

            boolean success = inventory.bookRoom(r.getRoomType());

            if (success) {
                System.out.println(getName() + " → Booking SUCCESS for " + r.getGuestName());
            } else {
                System.out.println(getName() + " → Booking FAILED for " + r.getGuestName());
            }
        }
    }
}

// ================= MAIN =================
public class HotelApp {
    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests (same time)
        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Single Room"));
        queue.add(new Reservation("Charlie", "Single Room"));
        queue.add(new Reservation("David", "Single Room"));

        // Multiple threads (concurrent users)
        Thread t1 = new BookingProcessor("Thread-1", queue, inventory);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        inventory.display();

        System.out.println("\nSystem handled concurrent requests safely.");
    }
}




