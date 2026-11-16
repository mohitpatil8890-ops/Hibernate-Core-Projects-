package com.library.main;

import com.library.entity.Book;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class LibraryApp {

    public static void main(String[] args) {
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM (PLAIN HIBERNATE - 20 TASKS) ===\n");

        task1_InsertSingleBook();
        task6_InsertTenBooksSingleTransaction();
        task2_FetchAllBooksHQL();
        task3_FetchBookById(1);
        task4_UpdateBookPriceUsingGet(1, 650.0);
        task7_UpdateBookCategoryUsingUpdate(2, "Reference");
        task5_DeleteBookUsingRemove(11);
        task8_DeleteArchivedCategoryUsingHQL();

        task9_NativePriceGreaterThan500();
        task10_NativeOrderByPriceDesc();
        task11_NativeHighestPrice();
        task12_NativeTotalPriceSum();
        task13_NativeCategoryWiseCount();
        task14_NativePriceBetween400And800();

        task15_HQLTitlesStartingWithA();
        task16_HQLPriceGreaterThanAverage();
        task17_HQLSecondHighestPrice();
        task18_HQLCategoriesUsingInOperator();
        task19_HQLCategoryWiseAvgPriceGroupBy();
        task20_HQLBulkIncreaseITBookPricesBy10Percent();

        HibernateUtil.shutdown();
        System.out.println("\n=== ALL 20 HIBERNATE TASKS COMPLETED SUCCESSFULLY ===");
    }

    public static void task1_InsertSingleBook() {
        System.out.println("--- Task 1: Insert single book using session.persist() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Book b1 = new Book("Effective Java", "Joshua Bloch", "IT", 750.0);
            session.persist(b1);
            tx.commit();
            System.out.println("Inserted: " + b1);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task2_FetchAllBooksHQL() {
        System.out.println("\n--- Task 2: Fetch all books using HQL createQuery() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book", Book.class);
            List<Book> books = query.list();
            books.forEach(System.out::println);
        }
    }

    public static void task3_FetchBookById(int id) {
        System.out.println("\n--- Task 3: Fetch book by ID (" + id + ") using session.get() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Book book = session.get(Book.class, id);
            System.out.println("Fetched: " + book);
        }
    }

    public static void task4_UpdateBookPriceUsingGet(int id, double newPrice) {
        System.out.println("\n--- Task 4: Update book price using session.get() & commit() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                book.setPrice(newPrice);
            }
            tx.commit();
            System.out.println("Updated Price: " + book);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task5_DeleteBookUsingRemove(int id) {
        System.out.println("\n--- Task 5: Delete book ID (" + id + ") using session.remove() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.remove(book);
                System.out.println("Removed book: " + book.getTitle());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task6_InsertTenBooksSingleTransaction() {
        System.out.println("\n--- Task 6: Insert 10 books in a single transaction ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Book> bookList = Arrays.asList(
                new Book("Algorithms Unlocked", "Thomas Cormen", "IT", 550.0),
                new Book("Clean Code", "Robert C. Martin", "IT", 820.0),
                new Book("A Brief History of Time", "Stephen Hawking", "Science", 480.0),
                new Book("Atomic Habits", "James Clear", "Self-Help", 420.0),
                new Book("Artificial Intelligence", "Stuart Russell", "IT", 950.0),
                new Book("The Alchemist", "Paulo Coelho", "Fiction", 350.0),
                new Book("Database System Concepts", "Abraham Silberschatz", "IT", 780.0),
                new Book("Astrophysics for People in a Hurry", "Neil deGrasse Tyson", "Science", 510.0),
                new Book("Old Magazine Vol 1", "Editorial", "Archive", 150.0),
                new Book("Temporary Catalog", "Admin", "Archive", 100.0)
            );
            for (Book b : bookList) {
                session.persist(b);
            }
            tx.commit();
            System.out.println("Successfully inserted 10 books in one transaction.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task7_UpdateBookCategoryUsingUpdate(int id, String newCategory) {
        System.out.println("\n--- Task 7: Update book category using session.update() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                book.setCategory(newCategory);
                session.update(book);
            }
            tx.commit();
            System.out.println("Updated Category: " + book);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task8_DeleteArchivedCategoryUsingHQL() {
        System.out.println("\n--- Task 8: Delete all 'Archive' category books using HQL ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM Book WHERE category = :cat");
            query.setParameter("cat", "Archive");
            int deletedCount = query.executeUpdate();
            tx.commit();
            System.out.println("Deleted " + deletedCount + " archived books.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task9_NativePriceGreaterThan500() {
        System.out.println("\n--- Task 9 (Native SQL): Books with price > ₹500 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Book> query = session.createNativeQuery(
                "SELECT * FROM books WHERE price > :minPrice", Book.class);
            query.setParameter("minPrice", 500.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task10_NativeOrderByPriceDesc() {
        System.out.println("\n--- Task 10 (Native SQL): Books ordered by price DESC ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Book> query = session.createNativeQuery(
                "SELECT * FROM books ORDER BY price DESC", Book.class);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task11_NativeHighestPrice() {
        System.out.println("\n--- Task 11 (Native SQL): Highest book price ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object maxPrice = session.createNativeQuery("SELECT MAX(price) FROM books").getSingleResult();
            System.out.println("Highest Book Price: ₹" + maxPrice);
        }
    }

    public static void task12_NativeTotalPriceSum() {
        System.out.println("\n--- Task 12 (Native SQL): Total price of all books ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object totalPrice = session.createNativeQuery("SELECT SUM(price) FROM books").getSingleResult();
            System.out.println("Total Library Valuation: ₹" + totalPrice);
        }
    }

    public static void task13_NativeCategoryWiseCount() {
        System.out.println("\n--- Task 13 (Native SQL): Category-wise book count ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createNativeQuery(
                "SELECT category, COUNT(*) FROM books GROUP BY category").getResultList();
            for (Object[] row : results) {
                System.out.println("Category: " + row[0] + " | Count: " + row[1]);
            }
        }
    }

    public static void task14_NativePriceBetween400And800() {
        System.out.println("\n--- Task 14 (Native SQL): Books with price between ₹400 and ₹800 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Book> query = session.createNativeQuery(
                "SELECT * FROM books WHERE price BETWEEN :low AND :high", Book.class);
            query.setParameter("low", 400.0);
            query.setParameter("high", 800.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task15_HQLTitlesStartingWithA() {
        System.out.println("\n--- Task 15 (HQL): Books starting with 'A' ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book WHERE title LIKE :prefix", Book.class);
            query.setParameter("prefix", "A%");
            query.list().forEach(System.out::println);
        }
    }

    public static void task16_HQLPriceGreaterThanAverage() {
        System.out.println("\n--- Task 16 (HQL): Books with price > average price ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery(
                "FROM Book WHERE price > (SELECT AVG(price) FROM Book)", Book.class);
            query.list().forEach(System.out::println);
        }
    }

    public static void task17_HQLSecondHighestPrice() {
        System.out.println("\n--- Task 17 (HQL): Second-highest book price ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                "SELECT MAX(price) FROM Book WHERE price < (SELECT MAX(price) FROM Book)", Double.class);
            Double secondHighest = query.uniqueResult();
            System.out.println("Second Highest Book Price: ₹" + secondHighest);
        }
    }

    public static void task18_HQLCategoriesUsingInOperator() {
        System.out.println("\n--- Task 18 (HQL): Books in ('IT', 'Science', 'Fiction') using IN ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery(
                "FROM Book WHERE category IN (:cats)", Book.class);
            query.setParameterList("cats", Arrays.asList("IT", "Science", "Fiction"));
            query.list().forEach(System.out::println);
        }
    }

    public static void task19_HQLCategoryWiseAvgPriceGroupBy() {
        System.out.println("\n--- Task 19 (HQL): Category-wise average price using GROUP BY ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT category, AVG(price) FROM Book GROUP BY category HAVING COUNT(id) >= 1", Object[].class);
            for (Object[] row : query.list()) {
                System.out.printf("Category: %-12s | Avg Price: ₹%.2f%n", row[0], (Double) row[1]);
            }
        }
    }

    public static void task20_HQLBulkIncreaseITBookPricesBy10Percent() {
        System.out.println("\n--- Task 20 (HQL): Increase 'IT' category book prices by 10% ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery(
                "UPDATE Book SET price = price * 1.10 WHERE category = :cat");
            query.setParameter("cat", "IT");
            int updatedRows = query.executeUpdate();
            tx.commit();
            System.out.println("Updated prices for " + updatedRows + " IT books.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
