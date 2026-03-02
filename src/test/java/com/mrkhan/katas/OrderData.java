package com.mrkhan.katas;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Production-realistic test data for all katas.
 * Contains Orders, Users, Transactions and Products.
 */
public class OrderData {

    // ─────────────────────────── ORDER ───────────────────────────

    public static class Order {
        private final int id;
        private final String customerName;
        private final String status;
        private final double amount;
        private final String category;
        private final LocalDate orderDate;
        private final List<String> tags;

        public Order(int id, String customerName, String status,
                     double amount, String category, LocalDate orderDate, List<String> tags) {
            this.id = id;
            this.customerName = customerName;
            this.status = status;
            this.amount = amount;
            this.category = category;
            this.orderDate = orderDate;
            this.tags = tags;
        }

        public int getId()               { return id; }
        public String getCustomerName()  { return customerName; }
        public String getStatus()        { return status; }
        public double getAmount()        { return amount; }
        public String getCategory()      { return category; }
        public LocalDate getOrderDate()  { return orderDate; }
        public List<String> getTags()    { return tags; }

        @Override
        public String toString() {
            return "Order{id=" + id + ", customer='" + customerName + "', status='" + status + "', amount=" + amount + "}";
        }
    }

    public static List<Order> sampleOrders() {
        return Arrays.asList(
            new Order(1, "John",    "PAID",    250.00, "Electronics", LocalDate.of(2024, 1, 5),  Arrays.asList("new", "vip")),
            new Order(2, "Alice",   "PENDING", 120.50, "Books",       LocalDate.of(2024, 1, 10), Arrays.asList("new")),
            new Order(3, "Bob",     "PAID",    890.00, "Electronics", LocalDate.of(2024, 2, 3),  Arrays.asList("vip", "repeat")),
            new Order(4, "Charlie", "CANCELED",  45.00, "Books",      LocalDate.of(2024, 2, 14), Arrays.asList("new")),
            new Order(5, "Diana",   "PAID",    310.75, "Clothing",    LocalDate.of(2024, 3, 1),  Arrays.asList("repeat")),
            new Order(6, "Eve",     "PENDING", 670.00, "Electronics", LocalDate.of(2024, 3, 20), Arrays.asList("vip")),
            new Order(7, "Frank",   "PAID",     55.00, "Books",       LocalDate.of(2024, 4, 2),  Arrays.asList("new")),
            new Order(8, "Grace",   "SHIPPED",  430.00, "Clothing",   LocalDate.of(2024, 4, 18), Arrays.asList("repeat", "vip")),
            new Order(9, "Hank",    "PAID",    1200.00, "Electronics",LocalDate.of(2024, 5, 7),  Arrays.asList("vip")),
            new Order(10,"Ivy",     "CANCELED",  90.00, "Books",      LocalDate.of(2024, 5, 22), Arrays.asList("new"))
        );
    }

    public static List<Order> ordersWithDuplicateAmounts() {
        return Arrays.asList(
            new Order(1, "A", "PAID",   100.0, "Books",       LocalDate.now(), Arrays.asList("x")),
            new Order(2, "B", "PAID",   100.0, "Electronics", LocalDate.now(), Arrays.asList("x")),
            new Order(3, "C", "PAID",   200.0, "Clothing",    LocalDate.now(), Arrays.asList("x")),
            new Order(4, "D", "PENDING",200.0, "Books",       LocalDate.now(), Arrays.asList("x")),
            new Order(5, "E", "PAID",   300.0, "Electronics", LocalDate.now(), Arrays.asList("x"))
        );
    }

    // ─────────────────────────── USER ───────────────────────────

    public static class User {
        private final int id;
        private final String name;
        private final String email;
        private final String department;
        private final int age;
        private final boolean active;
        private final List<String> roles;

        public User(int id, String name, String email, String department,
                    int age, boolean active, List<String> roles) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.department = department;
            this.age = age;
            this.active = active;
            this.roles = roles;
        }

        public int getId()           { return id; }
        public String getName()      { return name; }
        public String getEmail()     { return email; }
        public String getDepartment(){ return department; }
        public int getAge()          { return age; }
        public boolean isActive()    { return active; }
        public List<String> getRoles(){ return roles; }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "', dept='" + department + "'}";
        }
    }

    public static List<User> sampleUsers() {
        return Arrays.asList(
            new User(1,  "Alice",   "alice@corp.com",   "Engineering", 28, true,  Arrays.asList("ADMIN", "DEV")),
            new User(2,  "Bob",     "bob@corp.com",     "Engineering", 34, true,  Arrays.asList("DEV")),
            new User(3,  "Charlie", "charlie@corp.com", "Marketing",   25, false, Arrays.asList("VIEWER")),
            new User(4,  "Diana",   "diana@corp.com",   "Marketing",   41, true,  Arrays.asList("MANAGER", "VIEWER")),
            new User(5,  "Eve",     "eve@corp.com",     "HR",          30, true,  Arrays.asList("HR_ADMIN")),
            new User(6,  "Frank",   "frank@corp.com",   "Engineering", 22, true,  Arrays.asList("DEV")),
            new User(7,  "Grace",   "grace@corp.com",   "HR",          38, false, Arrays.asList("HR_ADMIN", "VIEWER")),
            new User(8,  "Hank",    "hank@corp.com",    "Finance",     45, true,  Arrays.asList("FINANCE_MANAGER")),
            new User(9,  "Ivy",     "ivy@corp.com",     "Engineering", 27, true,  Arrays.asList("DEV", "VIEWER")),
            new User(10, "Jack",    "jack@corp.com",    "Finance",     33, true,  Arrays.asList("VIEWER"))
        );
    }

    // ─────────────────────────── TRANSACTION ───────────────────────────

    public static class Transaction {
        private final String id;
        private final String type;
        private final double amount;
        private final String currency;
        private final boolean successful;

        public Transaction(String id, String type, double amount, String currency, boolean successful) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.currency = currency;
            this.successful = successful;
        }

        public String getId()          { return id; }
        public String getType()        { return type; }
        public double getAmount()      { return amount; }
        public String getCurrency()    { return currency; }
        public boolean isSuccessful()  { return successful; }

        @Override
        public String toString() {
            return "Transaction{id='" + id + "', type='" + type + "', amount=" + amount + "}";
        }
    }

    public static List<Transaction> sampleTransactions() {
        return Arrays.asList(
            new Transaction("T001", "CREDIT",  500.00, "USD", true),
            new Transaction("T002", "DEBIT",   200.00, "USD", true),
            new Transaction("T003", "CREDIT", 1500.00, "EUR", false),
            new Transaction("T004", "DEBIT",    75.50, "USD", true),
            new Transaction("T005", "CREDIT",  300.00, "GBP", true),
            new Transaction("T006", "DEBIT",   450.00, "EUR", false),
            new Transaction("T007", "CREDIT",  800.00, "USD", true),
            new Transaction("T008", "DEBIT",   120.00, "GBP", true),
            new Transaction("T009", "CREDIT",  250.00, "USD", false),
            new Transaction("T010", "DEBIT",   680.00, "EUR", true)
        );
    }

    // ─────────────────────────── PRODUCT ───────────────────────────

    public static class Product {
        private final String name;
        private final String category;
        private final double price;
        private final int stock;

        public Product(String name, String category, double price, int stock) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.stock = stock;
        }

        public String getName()     { return name; }
        public String getCategory() { return category; }
        public double getPrice()    { return price; }
        public int getStock()       { return stock; }

        @Override
        public String toString() {
            return "Product{name='" + name + "', category='" + category + "', price=" + price + "}";
        }
    }

    public static List<Product> sampleProducts() {
        return Arrays.asList(
            new Product("Laptop",     "Electronics",  999.99, 15),
            new Product("Phone",      "Electronics",  699.99, 30),
            new Product("Headphones", "Electronics",  149.99,  5),
            new Product("Java Book",  "Books",         49.99, 50),
            new Product("Clean Code", "Books",         39.99, 45),
            new Product("T-Shirt",    "Clothing",      19.99, 100),
            new Product("Jeans",      "Clothing",      59.99, 80),
            new Product("Keyboard",   "Electronics",   89.99, 20),
            new Product("Mouse",      "Electronics",   35.99, 60),
            new Product("Notebook",   "Books",          9.99, 200)
        );
    }

    // ─────────────────────────── NESTED DATA ───────────────────────────

    public static class Department {
        private final String name;
        private final List<User> employees;

        public Department(String name, List<User> employees) {
            this.name = name;
            this.employees = employees;
        }

        public String getName()           { return name; }
        public List<User> getEmployees()  { return employees; }
    }

    public static List<Department> sampleDepartments() {
        List<User> users = sampleUsers();
        return Arrays.asList(
            new Department("Engineering", Arrays.asList(users.get(0), users.get(1), users.get(5), users.get(8))),
            new Department("Marketing",   Arrays.asList(users.get(2), users.get(3))),
            new Department("HR",          Arrays.asList(users.get(4), users.get(6))),
            new Department("Finance",     Arrays.asList(users.get(7), users.get(9)))
        );
    }
}
