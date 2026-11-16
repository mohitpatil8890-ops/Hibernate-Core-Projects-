package com.employee.main;

import com.employee.entity.Employee;
import com.employee.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class EmployeeApp {

    public static void main(String[] args) {
        System.out.println("=== EMPLOYEE MANAGEMENT SYSTEM (PLAIN HIBERNATE - 20 TASKS) ===\n");

        task1_InsertSingleEmployee();
        task6_InsertTenEmployeesSingleTransaction();
        task2_FetchAllEmployeesHQL();
        task3_FetchEmployeeById(1);
        task4_UpdateEmployeeSalaryUsingGet(1, 75000.0);
        task7_UpdateEmployeeDepartmentUsingUpdate(2, "Finance");
        task5_DeleteEmployeeUsingRemove(11);
        task8_DeleteHRDepartmentUsingHQL();

        task9_NativeSalaryGreaterThan50000();
        task10_NativeOrderBySalaryDesc();
        task11_NativeHighestSalary();
        task12_NativeTotalSalarySum();
        task13_NativeDepartmentWiseCount();
        task14_NativeSalaryBetween40000And80000();

        task15_HQLNamesStartingWithA();
        task16_HQLSalaryGreaterThanAverage();
        task17_HQLSecondHighestSalary();
        task18_HQLDepartmentsUsingInOperator();
        task19_HQLDepartmentWiseAvgSalaryGroupBy();
        task20_HQLBulkIncreaseITSalaryBy10Percent();

        HibernateUtil.shutdown();
        System.out.println("\n=== ALL 20 HIBERNATE TASKS COMPLETED SUCCESSFULLY ===");
    }

    public static void task1_InsertSingleEmployee() {
        System.out.println("--- Task 1: Insert single employee using session.persist() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee e1 = new Employee("Aarav Sharma", "IT", 65000.0);
            session.persist(e1);
            tx.commit();
            System.out.println("Inserted: " + e1);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task2_FetchAllEmployeesHQL() {
        System.out.println("\n--- Task 2: Fetch all employees using HQL createQuery() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            List<Employee> employees = query.list();
            employees.forEach(System.out::println);
        }
    }

    public static void task3_FetchEmployeeById(int id) {
        System.out.println("\n--- Task 3: Fetch employee by ID (" + id + ") using session.get() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            System.out.println("Fetched: " + employee);
        }
    }

    public static void task4_UpdateEmployeeSalaryUsingGet(int id, double newSalary) {
        System.out.println("\n--- Task 4: Update employee salary using session.get() & commit() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setSalary(newSalary);
            }
            tx.commit();
            System.out.println("Updated Salary: " + employee);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task5_DeleteEmployeeUsingRemove(int id) {
        System.out.println("\n--- Task 5: Delete employee ID (" + id + ") using session.remove() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                System.out.println("Removed employee: " + employee.getName());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task6_InsertTenEmployeesSingleTransaction() {
        System.out.println("\n--- Task 6: Insert 10 employees in a single transaction ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Employee> employeeList = Arrays.asList(
                new Employee("Ananya Verma", "IT", 70000.0),
                new Employee("Rohan Mehta", "Finance", 55000.0),
                new Employee("Aditi Nair", "Marketing", 48000.0),
                new Employee("Vikram Singh", "IT", 90000.0),
                new Employee("Arjun Kapoor", "Finance", 78000.0),
                new Employee("Neha Joshi", "Sales", 42000.0),
                new Employee("Kabir Das", "IT", 62000.0),
                new Employee("Aakash Gupta", "Finance", 68000.0),
                new Employee("Suresh Kumar", "HR", 38000.0),
                new Employee("Pooja Rani", "HR", 41000.0)
            );
            for (Employee emp : employeeList) {
                session.persist(emp);
            }
            tx.commit();
            System.out.println("Successfully inserted 10 employees in one transaction.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task7_UpdateEmployeeDepartmentUsingUpdate(int id, String newDepartment) {
        System.out.println("\n--- Task 7: Update employee department using session.update() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setDepartment(newDepartment);
                session.update(employee);
            }
            tx.commit();
            System.out.println("Updated Department: " + employee);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task8_DeleteHRDepartmentUsingHQL() {
        System.out.println("\n--- Task 8: Delete all 'HR' department records using HQL ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM Employee WHERE department = :dept");
            query.setParameter("dept", "HR");
            int deletedCount = query.executeUpdate();
            tx.commit();
            System.out.println("Deleted " + deletedCount + " HR department records.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task9_NativeSalaryGreaterThan50000() {
        System.out.println("\n--- Task 9 (Native SQL): Employees with salary > ₹50,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Employee> query = session.createNativeQuery(
                "SELECT * FROM employees WHERE salary > :minSalary", Employee.class);
            query.setParameter("minSalary", 50000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task10_NativeOrderBySalaryDesc() {
        System.out.println("\n--- Task 10 (Native SQL): Employees ordered by salary DESC ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Employee> query = session.createNativeQuery(
                "SELECT * FROM employees ORDER BY salary DESC", Employee.class);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task11_NativeHighestSalary() {
        System.out.println("\n--- Task 11 (Native SQL): Highest salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object maxSalary = session.createNativeQuery("SELECT MAX(salary) FROM employees").getSingleResult();
            System.out.println("Highest Salary: ₹" + maxSalary);
        }
    }

    public static void task12_NativeTotalSalarySum() {
        System.out.println("\n--- Task 12 (Native SQL): Total salary of all employees ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object totalSalary = session.createNativeQuery("SELECT SUM(salary) FROM employees").getSingleResult();
            System.out.println("Total Employee Payroll: ₹" + totalSalary);
        }
    }

    public static void task13_NativeDepartmentWiseCount() {
        System.out.println("\n--- Task 13 (Native SQL): Department-wise employee count ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createNativeQuery(
                "SELECT department, COUNT(*) FROM employees GROUP BY department").getResultList();
            for (Object[] row : results) {
                System.out.println("Department: " + row[0] + " | Count: " + row[1]);
            }
        }
    }

    public static void task14_NativeSalaryBetween40000And80000() {
        System.out.println("\n--- Task 14 (Native SQL): Employees with salary between ₹40,000 and ₹80,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Employee> query = session.createNativeQuery(
                "SELECT * FROM employees WHERE salary BETWEEN :low AND :high", Employee.class);
            query.setParameter("low", 40000.0);
            query.setParameter("high", 80000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task15_HQLNamesStartingWithA() {
        System.out.println("\n--- Task 15 (HQL): Employees whose names start with 'A' ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery(
                "FROM Employee WHERE name LIKE :prefix", Employee.class);
            query.setParameter("prefix", "A%");
            query.list().forEach(System.out::println);
        }
    }

    public static void task16_HQLSalaryGreaterThanAverage() {
        System.out.println("\n--- Task 16 (HQL): Employees with salary > average salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery(
                "FROM Employee WHERE salary > (SELECT AVG(salary) FROM Employee)", Employee.class);
            query.list().forEach(System.out::println);
        }
    }

    public static void task17_HQLSecondHighestSalary() {
        System.out.println("\n--- Task 17 (HQL): Second-highest salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                "SELECT MAX(salary) FROM Employee WHERE salary < (SELECT MAX(salary) FROM Employee)", Double.class);
            Double secondHighest = query.uniqueResult();
            System.out.println("Second Highest Salary: ₹" + secondHighest);
        }
    }

    public static void task18_HQLDepartmentsUsingInOperator() {
        System.out.println("\n--- Task 18 (HQL): Employees in ('IT', 'HR', 'Finance') using IN ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery(
                "FROM Employee WHERE department IN (:depts)", Employee.class);
            query.setParameterList("depts", Arrays.asList("IT", "HR", "Finance"));
            query.list().forEach(System.out::println);
        }
    }

    public static void task19_HQLDepartmentWiseAvgSalaryGroupBy() {
        System.out.println("\n--- Task 19 (HQL): Department-wise average salary using GROUP BY ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT department, AVG(salary) FROM Employee GROUP BY department HAVING COUNT(id) >= 1", Object[].class);
            for (Object[] row : query.list()) {
                System.out.printf("Department: %-12s | Avg Salary: ₹%.2f%n", row[0], (Double) row[1]);
            }
        }
    }

    public static void task20_HQLBulkIncreaseITSalaryBy10Percent() {
        System.out.println("\n--- Task 20 (HQL): Increase 'IT' department salary by 10% ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery(
                "UPDATE Employee SET salary = salary * 1.10 WHERE department = :dept");
            query.setParameter("dept", "IT");
            int updatedRows = query.executeUpdate();
            tx.commit();
            System.out.println("Updated salary for " + updatedRows + " IT employees.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
