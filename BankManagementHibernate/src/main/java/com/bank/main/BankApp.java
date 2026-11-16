package com.bank.main;

import com.bank.entity.Account;
import com.bank.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class BankApp {

    public static void main(String[] args) {
        System.out.println("=== BANK MANAGEMENT SYSTEM (PLAIN HIBERNATE - 20 TASKS) ===\n");

        task1_InsertSingleAccount();
        task6_InsertTenAccountsSingleTransaction();
        task2_FetchAllAccountsHQL();
        task3_FetchAccountById(1);
        task4_UpdateAccountBalanceUsingGet(1, 85000.0);
        task7_UpdateAccountBranchUsingUpdate(2, "Savings");
        task5_DeleteAccountUsingRemove(11);
        task8_DeleteLoanBranchUsingHQL();

        task9_NativeBalanceGreaterThan50000();
        task10_NativeOrderByBalanceDesc();
        task11_NativeHighestBalance();
        task12_NativeTotalBalanceSum();
        task13_NativeBranchWiseCount();
        task14_NativeBalanceBetween40000And80000();

        task15_HQLNamesStartingWithA();
        task16_HQLBalanceGreaterThanAverage();
        task17_HQLSecondHighestBalance();
        task18_HQLBranchesUsingInOperator();
        task19_HQLBranchWiseAvgBalanceGroupBy();
        task20_HQLBulkIncreaseSavingsBalanceBy10Percent();

        HibernateUtil.shutdown();
        System.out.println("\n=== ALL 20 HIBERNATE TASKS COMPLETED SUCCESSFULLY ===");
    }

    public static void task1_InsertSingleAccount() {
        System.out.println("--- Task 1: Insert single account using session.persist() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Account a1 = new Account("Aarav Sharma", "Savings", 60000.0);
            session.persist(a1);
            tx.commit();
            System.out.println("Inserted: " + a1);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task2_FetchAllAccountsHQL() {
        System.out.println("\n--- Task 2: Fetch all accounts using HQL createQuery() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery("FROM Account", Account.class);
            List<Account> accounts = query.list();
            accounts.forEach(System.out::println);
        }
    }

    public static void task3_FetchAccountById(int id) {
        System.out.println("\n--- Task 3: Fetch account by ID (" + id + ") using session.get() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Account account = session.get(Account.class, id);
            System.out.println("Fetched: " + account);
        }
    }

    public static void task4_UpdateAccountBalanceUsingGet(int id, double newBalance) {
        System.out.println("\n--- Task 4: Update account balance using session.get() & commit() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Account account = session.get(Account.class, id);
            if (account != null) {
                account.setBalance(newBalance);
            }
            tx.commit();
            System.out.println("Updated Balance: " + account);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task5_DeleteAccountUsingRemove(int id) {
        System.out.println("\n--- Task 5: Delete account ID (" + id + ") using session.remove() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Account account = session.get(Account.class, id);
            if (account != null) {
                session.remove(account);
                System.out.println("Removed account: " + account.getName());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task6_InsertTenAccountsSingleTransaction() {
        System.out.println("\n--- Task 6: Insert 10 accounts in a single transaction ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Account> accountList = Arrays.asList(
                new Account("Ananya Verma", "Savings", 75000.0),
                new Account("Rohan Mehta", "Current", 55000.0),
                new Account("Aditi Nair", "Savings", 48000.0),
                new Account("Vikram Singh", "Current", 90000.0),
                new Account("Arjun Kapoor", "Savings", 78000.0),
                new Account("Neha Joshi", "Current", 42000.0),
                new Account("Kabir Das", "Savings", 62000.0),
                new Account("Aakash Gupta", "Current", 68000.0),
                new Account("Suresh Kumar", "Loan", 38000.0),
                new Account("Pooja Rani", "Loan", 41000.0)
            );
            for (Account acc : accountList) {
                session.persist(acc);
            }
            tx.commit();
            System.out.println("Successfully inserted 10 accounts in one transaction.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task7_UpdateAccountBranchUsingUpdate(int id, String newBranch) {
        System.out.println("\n--- Task 7: Update account branch using session.update() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Account account = session.get(Account.class, id);
            if (account != null) {
                account.setBranch(newBranch);
                session.update(account);
            }
            tx.commit();
            System.out.println("Updated Branch: " + account);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task8_DeleteLoanBranchUsingHQL() {
        System.out.println("\n--- Task 8: Delete all 'Loan' branch records using HQL ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM Account WHERE branch = :br");
            query.setParameter("br", "Loan");
            int deletedCount = query.executeUpdate();
            tx.commit();
            System.out.println("Deleted " + deletedCount + " Loan branch records.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task9_NativeBalanceGreaterThan50000() {
        System.out.println("\n--- Task 9 (Native SQL): Accounts with balance > ₹50,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Account> query = session.createNativeQuery(
                "SELECT * FROM accounts WHERE balance > :minBal", Account.class);
            query.setParameter("minBal", 50000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task10_NativeOrderByBalanceDesc() {
        System.out.println("\n--- Task 10 (Native SQL): Accounts ordered by balance DESC ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Account> query = session.createNativeQuery(
                "SELECT * FROM accounts ORDER BY balance DESC", Account.class);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task11_NativeHighestBalance() {
        System.out.println("\n--- Task 11 (Native SQL): Highest balance ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object maxBal = session.createNativeQuery("SELECT MAX(balance) FROM accounts").getSingleResult();
            System.out.println("Highest Balance: ₹" + maxBal);
        }
    }

    public static void task12_NativeTotalBalanceSum() {
        System.out.println("\n--- Task 12 (Native SQL): Total balance of all accounts ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object totalBal = session.createNativeQuery("SELECT SUM(balance) FROM accounts").getSingleResult();
            System.out.println("Total Bank Deposits: ₹" + totalBal);
        }
    }

    public static void task13_NativeBranchWiseCount() {
        System.out.println("\n--- Task 13 (Native SQL): Branch-wise account count ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createNativeQuery(
                "SELECT branch, COUNT(*) FROM accounts GROUP BY branch").getResultList();
            for (Object[] row : results) {
                System.out.println("Branch: " + row[0] + " | Count: " + row[1]);
            }
        }
    }

    public static void task14_NativeBalanceBetween40000And80000() {
        System.out.println("\n--- Task 14 (Native SQL): Accounts with balance between ₹40,000 and ₹80,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Account> query = session.createNativeQuery(
                "SELECT * FROM accounts WHERE balance BETWEEN :low AND :high", Account.class);
            query.setParameter("low", 40000.0);
            query.setParameter("high", 80000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task15_HQLNamesStartingWithA() {
        System.out.println("\n--- Task 15 (HQL): Accounts whose names start with 'A' ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                "FROM Account WHERE name LIKE :prefix", Account.class);
            query.setParameter("prefix", "A%");
            query.list().forEach(System.out::println);
        }
    }

    public static void task16_HQLBalanceGreaterThanAverage() {
        System.out.println("\n--- Task 16 (HQL): Accounts with balance > average balance ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                "FROM Account WHERE balance > (SELECT AVG(balance) FROM Account)", Account.class);
            query.list().forEach(System.out::println);
        }
    }

    public static void task17_HQLSecondHighestBalance() {
        System.out.println("\n--- Task 17 (HQL): Second-highest balance ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                "SELECT MAX(balance) FROM Account WHERE balance < (SELECT MAX(balance) FROM Account)", Double.class);
            Double secondHighest = query.uniqueResult();
            System.out.println("Second Highest Balance: ₹" + secondHighest);
        }
    }

    public static void task18_HQLBranchesUsingInOperator() {
        System.out.println("\n--- Task 18 (HQL): Accounts in ('Savings', 'Current', 'Loan') using IN ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                "FROM Account WHERE branch IN (:branches)", Account.class);
            query.setParameterList("branches", Arrays.asList("Savings", "Current", "Loan"));
            query.list().forEach(System.out::println);
        }
    }

    public static void task19_HQLBranchWiseAvgBalanceGroupBy() {
        System.out.println("\n--- Task 19 (HQL): Branch-wise average balance using GROUP BY ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT branch, AVG(balance) FROM Account GROUP BY branch HAVING COUNT(id) >= 1", Object[].class);
            for (Object[] row : query.list()) {
                System.out.printf("Branch: %-12s | Avg Balance: ₹%.2f%n", row[0], (Double) row[1]);
            }
        }
    }

    public static void task20_HQLBulkIncreaseSavingsBalanceBy10Percent() {
        System.out.println("\n--- Task 20 (HQL): Increase 'Savings' branch balance by 10% ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery(
                "UPDATE Account SET balance = balance * 1.10 WHERE branch = :br");
            query.setParameter("br", "Savings");
            int updatedRows = query.executeUpdate();
            tx.commit();
            System.out.println("Updated balance for " + updatedRows + " Savings accounts.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
