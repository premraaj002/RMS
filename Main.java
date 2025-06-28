import java.util.*;

class MenuItem {
    protected String name;
    protected double price;
    protected int quantity;

    public MenuItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void displayItem() {
        System.out.println(name + " - ₹" + price + " [" + quantity + " left]");
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int count) {
        if (count <= quantity) {
            quantity -= count;
        }
    }
}

class FoodItem extends MenuItem {
    public FoodItem(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class DrinkItem extends MenuItem {
    public DrinkItem(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class Order {
    private List<MenuItem> items;
    private List<Integer> counts;

    public Order() {
        items = new ArrayList<>();
        counts = new ArrayList<>();
    }

    public void addItem(MenuItem item, int count) {
        items.add(item);
        counts.add(count);
    }

    public void displayBill() {
        double total = 0;
        System.out.println("\n--- Bill ---");
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            int qty = counts.get(i);
            System.out.println(item.getName() + " x " + qty + " = ₹" + (item.getPrice() * qty));
            total += item.getPrice() * qty;
        }
        System.out.println("Total: ₹" + total);
    }
}

public class Main {
    private static List<MenuItem> menu = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Prem's Restaurant!");
        boolean running = true;

        while (running) {
            System.out.println("\n1. Add Food Item");
            System.out.println("2. Add Drink Item");
            System.out.println("3. View Menu");
            System.out.println("4. Take Order");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    addItem("food");
                    break;
                case 2:
                    addItem("drink");
                    break;
                case 3:
                    displayMenu();
                    break;
                case 4:
                    takeOrder();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you! Visit again.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void addItem(String type) {
        System.out.print("Enter item name: ");
        String name = sc.nextLine();
        System.out.print("Enter item price: ");
        double price = sc.nextDouble();
        System.out.print("Enter item quantity: ");
        int qty = sc.nextInt();
        sc.nextLine(); 

        MenuItem item;
        if (type.equals("food")) {
            item = new FoodItem(name, price, qty);
        } else {
            item = new DrinkItem(name, price, qty);
        }

        menu.add(item);
        System.out.println(type + " item added successfully.");
    }

    public static void displayMenu() {
        if (menu.isEmpty()) {
            System.out.println("Menu is empty.");
            return;
        }

        List<MenuItem> foodItems = new ArrayList<>();
        List<MenuItem> drinkItems = new ArrayList<>();

        for (MenuItem item : menu) {
            if (item instanceof FoodItem) {
                foodItems.add(item);
            } else if (item instanceof DrinkItem) {
                drinkItems.add(item);
            }
        }

        System.out.printf("\n%-35s | %-35s\n", "FOOD ITEMS", "DRINK ITEMS");
        System.out.println("-------------------------------------------------------------------------");

        int max = Math.max(foodItems.size(), drinkItems.size());
        for (int i = 0; i < max; i++) {
            String foodStr = i < foodItems.size()
                    ? String.format("%-2d. %-20s ₹%-6.2f [%2d left]", i + 1, foodItems.get(i).getName(), foodItems.get(i).getPrice(), foodItems.get(i).getQuantity())
                    : "";
            String drinkStr = i < drinkItems.size()
                    ? String.format("%-2d. %-20s ₹%-6.2f [%2d left]", i + 1, drinkItems.get(i).getName(), drinkItems.get(i).getPrice(), drinkItems.get(i).getQuantity())
                    : "";
            System.out.printf("%-35s | %-35s\n", foodStr, drinkStr);
        }
    }

    public static void takeOrder() {
        if (menu.isEmpty()) {
            System.out.println("Menu is empty. Add items first.");
            return;
        }

        Order order = new Order();
        while (true) {
            displayMenu();
            System.out.print("Select item number to add (0 to finish): ");
            int choice = sc.nextInt();
            if (choice == 0) break;

            if (choice > 0 && choice <= menu.size()) {
                MenuItem item = menu.get(choice - 1);
                if (item.getQuantity() == 0) {
                    System.out.println("Sorry, " + item.getName() + " is out of stock.");
                    continue;
                }

                System.out.print("Enter quantity (Available: " + item.getQuantity() + "): ");
                int qty = sc.nextInt();
                if (qty <= 0 || qty > item.getQuantity()) {
                    System.out.println("Invalid quantity. Try again.");
                    continue;
                }

                item.reduceQuantity(qty);
                order.addItem(item, qty);
                System.out.println("Added to order.");
            } else {
                System.out.println("Invalid item number.");
            }
        }

        order.displayBill();
    }
}
