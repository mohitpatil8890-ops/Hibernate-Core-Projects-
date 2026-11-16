package com.hospital.main;

import com.hospital.entity.Doctor;
import com.hospital.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class HospitalApp {

    public static void main(String[] args) {
        System.out.println("=== HOSPITAL MANAGEMENT SYSTEM (PLAIN HIBERNATE - 20 TASKS) ===\n");

        task1_InsertSingleDoctor();
        task6_InsertTenDoctorsSingleTransaction();
        task2_FetchAllDoctorsHQL();
        task3_FetchDoctorById(1);
        task4_UpdateDoctorSalaryUsingGet(1, 95000.0);
        task7_UpdateDoctorDepartmentUsingUpdate(2, "Neurology");
        task5_DeleteDoctorUsingRemove(11);
        task8_DeleteAdminDepartmentUsingHQL();

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
        task20_HQLBulkIncreaseCardiologySalaryBy10Percent();

        HibernateUtil.shutdown();
        System.out.println("\n=== ALL 20 HIBERNATE TASKS COMPLETED SUCCESSFULLY ===");
    }

    public static void task1_InsertSingleDoctor() {
        System.out.println("--- Task 1: Insert single doctor using session.persist() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Doctor d1 = new Doctor("Dr. Aarav Sharma", "Cardiology", 85000.0);
            session.persist(d1);
            tx.commit();
            System.out.println("Inserted: " + d1);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task2_FetchAllDoctorsHQL() {
        System.out.println("\n--- Task 2: Fetch all doctors using HQL createQuery() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery("FROM Doctor", Doctor.class);
            List<Doctor> doctors = query.list();
            doctors.forEach(System.out::println);
        }
    }

    public static void task3_FetchDoctorById(int id) {
        System.out.println("\n--- Task 3: Fetch doctor by ID (" + id + ") using session.get() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Doctor doctor = session.get(Doctor.class, id);
            System.out.println("Fetched: " + doctor);
        }
    }

    public static void task4_UpdateDoctorSalaryUsingGet(int id, double newSalary) {
        System.out.println("\n--- Task 4: Update doctor salary using session.get() & commit() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            if (doctor != null) {
                doctor.setSalary(newSalary);
            }
            tx.commit();
            System.out.println("Updated Salary: " + doctor);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task5_DeleteDoctorUsingRemove(int id) {
        System.out.println("\n--- Task 5: Delete doctor ID (" + id + ") using session.remove() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            if (doctor != null) {
                session.remove(doctor);
                System.out.println("Removed doctor: " + doctor.getName());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task6_InsertTenDoctorsSingleTransaction() {
        System.out.println("\n--- Task 6: Insert 10 doctors in a single transaction ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Doctor> doctorList = Arrays.asList(
                new Doctor("Dr. Ananya Verma", "Cardiology", 75000.0),
                new Doctor("Dr. Rohan Mehta", "Orthopedics", 62000.0),
                new Doctor("Dr. Aditi Nair", "Pediatrics", 54000.0),
                new Doctor("Dr. Vikram Singh", "Neurology", 90000.0),
                new Doctor("Dr. Arjun Kapoor", "Cardiology", 80000.0),
                new Doctor("Dr. Neha Joshi", "Pediatrics", 45000.0),
                new Doctor("Dr. Kabir Das", "Orthopedics", 68000.0),
                new Doctor("Dr. Aakash Gupta", "Neurology", 72000.0),
                new Doctor("Suresh Kumar", "HR", 38000.0),
                new Doctor("Pooja Rani", "HR", 40000.0)
            );
            for (Doctor d : doctorList) {
                session.persist(d);
            }
            tx.commit();
            System.out.println("Successfully inserted 10 doctors in one transaction.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task7_UpdateDoctorDepartmentUsingUpdate(int id, String newDepartment) {
        System.out.println("\n--- Task 7: Update doctor department using session.update() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            if (doctor != null) {
                doctor.setDepartment(newDepartment);
                session.update(doctor);
            }
            tx.commit();
            System.out.println("Updated Department: " + doctor);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task8_DeleteAdminDepartmentUsingHQL() {
        System.out.println("\n--- Task 8: Delete all 'HR' department records using HQL ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM Doctor WHERE department = :dept");
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
        System.out.println("\n--- Task 9 (Native SQL): Doctors with salary > ₹50,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Doctor> query = session.createNativeQuery(
                "SELECT * FROM doctors WHERE salary > :minSalary", Doctor.class);
            query.setParameter("minSalary", 50000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task10_NativeOrderBySalaryDesc() {
        System.out.println("\n--- Task 10 (Native SQL): Doctors ordered by salary DESC ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Doctor> query = session.createNativeQuery(
                "SELECT * FROM doctors ORDER BY salary DESC", Doctor.class);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task11_NativeHighestSalary() {
        System.out.println("\n--- Task 11 (Native SQL): Highest salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object maxSalary = session.createNativeQuery("SELECT MAX(salary) FROM doctors").getSingleResult();
            System.out.println("Highest Salary: ₹" + maxSalary);
        }
    }

    public static void task12_NativeTotalSalarySum() {
        System.out.println("\n--- Task 12 (Native SQL): Total salary of all doctors ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object totalSalary = session.createNativeQuery("SELECT SUM(salary) FROM doctors").getSingleResult();
            System.out.println("Total Hospital Payroll: ₹" + totalSalary);
        }
    }

    public static void task13_NativeDepartmentWiseCount() {
        System.out.println("\n--- Task 13 (Native SQL): Department-wise doctor count ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createNativeQuery(
                "SELECT department, COUNT(*) FROM doctors GROUP BY department").getResultList();
            for (Object[] row : results) {
                System.out.println("Department: " + row[0] + " | Count: " + row[1]);
            }
        }
    }

    public static void task14_NativeSalaryBetween40000And80000() {
        System.out.println("\n--- Task 14 (Native SQL): Doctors with salary between ₹40,000 and ₹80,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Doctor> query = session.createNativeQuery(
                "SELECT * FROM doctors WHERE salary BETWEEN :low AND :high", Doctor.class);
            query.setParameter("low", 40000.0);
            query.setParameter("high", 80000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task15_HQLNamesStartingWithA() {
        System.out.println("\n--- Task 15 (HQL): Doctors whose names start with 'Dr. A' or 'A' ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor WHERE name LIKE :prefix1 OR name LIKE :prefix2", Doctor.class);
            query.setParameter("prefix1", "A%");
            query.setParameter("prefix2", "Dr. A%");
            query.list().forEach(System.out::println);
        }
    }

    public static void task16_HQLSalaryGreaterThanAverage() {
        System.out.println("\n--- Task 16 (HQL): Doctors with salary > average salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor WHERE salary > (SELECT AVG(salary) FROM Doctor)", Doctor.class);
            query.list().forEach(System.out::println);
        }
    }

    public static void task17_HQLSecondHighestSalary() {
        System.out.println("\n--- Task 17 (HQL): Second-highest salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                "SELECT MAX(salary) FROM Doctor WHERE salary < (SELECT MAX(salary) FROM Doctor)", Double.class);
            Double secondHighest = query.uniqueResult();
            System.out.println("Second Highest Salary: ₹" + secondHighest);
        }
    }

    public static void task18_HQLDepartmentsUsingInOperator() {
        System.out.println("\n--- Task 18 (HQL): Doctors in ('Cardiology', 'Neurology', 'Orthopedics') using IN ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor WHERE department IN (:depts)", Doctor.class);
            query.setParameterList("depts", Arrays.asList("Cardiology", "Neurology", "Orthopedics"));
            query.list().forEach(System.out::println);
        }
    }

    public static void task19_HQLDepartmentWiseAvgSalaryGroupBy() {
        System.out.println("\n--- Task 19 (HQL): Department-wise average salary using GROUP BY ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT department, AVG(salary) FROM Doctor GROUP BY department HAVING COUNT(id) >= 1", Object[].class);
            for (Object[] row : query.list()) {
                System.out.printf("Department: %-14s | Avg Salary: ₹%.2f%n", row[0], (Double) row[1]);
            }
        }
    }

    public static void task20_HQLBulkIncreaseCardiologySalaryBy10Percent() {
        System.out.println("\n--- Task 20 (HQL): Increase 'Cardiology' department salary by 10% ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery(
                "UPDATE Doctor SET salary = salary * 1.10 WHERE department = :dept");
            query.setParameter("dept", "Cardiology");
            int updatedRows = query.executeUpdate();
            tx.commit();
            System.out.println("Updated salary for " + updatedRows + " Cardiology doctors.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
