package com.school.main;

import com.school.entity.Teacher;
import com.school.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class SchoolApp {

    public static void main(String[] args) {
        System.out.println("=== SCHOOL MANAGEMENT SYSTEM (PLAIN HIBERNATE - 20 TASKS) ===\n");

        task1_InsertSingleTeacher();
        task6_InsertTenTeachersSingleTransaction();
        task2_FetchAllTeachersHQL();
        task3_FetchTeacherById(1);
        task4_UpdateTeacherSalaryUsingGet(1, 65000.0);
        task7_UpdateTeacherDepartmentUsingUpdate(2, "Mathematics");
        task5_DeleteTeacherUsingRemove(11);
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
        task20_HQLBulkIncreaseScienceSalaryBy10Percent();

        HibernateUtil.shutdown();
        System.out.println("\n=== ALL 20 HIBERNATE TASKS COMPLETED SUCCESSFULLY ===");
    }

    public static void task1_InsertSingleTeacher() {
        System.out.println("--- Task 1: Insert single teacher using session.persist() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Teacher t1 = new Teacher("Amitabh Verma", "Science", 58000.0);
            session.persist(t1);
            tx.commit();
            System.out.println("Inserted: " + t1);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task2_FetchAllTeachersHQL() {
        System.out.println("\n--- Task 2: Fetch all teachers using HQL createQuery() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Teacher> query = session.createQuery("FROM Teacher", Teacher.class);
            List<Teacher> teachers = query.list();
            teachers.forEach(System.out::println);
        }
    }

    public static void task3_FetchTeacherById(int id) {
        System.out.println("\n--- Task 3: Fetch teacher by ID (" + id + ") using session.get() ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Teacher teacher = session.get(Teacher.class, id);
            System.out.println("Fetched: " + teacher);
        }
    }

    public static void task4_UpdateTeacherSalaryUsingGet(int id, double newSalary) {
        System.out.println("\n--- Task 4: Update teacher salary using session.get() & commit() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Teacher teacher = session.get(Teacher.class, id);
            if (teacher != null) {
                teacher.setSalary(newSalary);
            }
            tx.commit();
            System.out.println("Updated Salary: " + teacher);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task5_DeleteTeacherUsingRemove(int id) {
        System.out.println("\n--- Task 5: Delete teacher ID (" + id + ") using session.remove() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Teacher teacher = session.get(Teacher.class, id);
            if (teacher != null) {
                session.remove(teacher);
                System.out.println("Removed teacher: " + teacher.getName());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task6_InsertTenTeachersSingleTransaction() {
        System.out.println("\n--- Task 6: Insert 10 teachers in a single transaction ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Teacher> teacherList = Arrays.asList(
                new Teacher("Anjali Deshmukh", "Science", 62000.0),
                new Teacher("Rajesh Kulkarni", "English", 48000.0),
                new Teacher("Aruna Joshi", "Mathematics", 71000.0),
                new Teacher("Manish Tiwari", "Science", 84000.0),
                new Teacher("Alok Nath", "History", 45000.0),
                new Teacher("Sunita Rao", "Mathematics", 68000.0),
                new Teacher("Deepak Chavan", "English", 52000.0),
                new Teacher("Ashwini Patil", "Science", 59000.0),
                new Teacher("Ramesh Pawar", "Admin", 35000.0),
                new Teacher("Kavita More", "Admin", 38000.0)
            );
            for (Teacher t : teacherList) {
                session.persist(t);
            }
            tx.commit();
            System.out.println("Successfully inserted 10 teachers in one transaction.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task7_UpdateTeacherDepartmentUsingUpdate(int id, String newDepartment) {
        System.out.println("\n--- Task 7: Update teacher department using session.update() ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Teacher teacher = session.get(Teacher.class, id);
            if (teacher != null) {
                teacher.setDepartment(newDepartment);
                session.update(teacher);
            }
            tx.commit();
            System.out.println("Updated Department: " + teacher);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task8_DeleteAdminDepartmentUsingHQL() {
        System.out.println("\n--- Task 8: Delete all 'Admin' department records using HQL ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM Teacher WHERE department = :dept");
            query.setParameter("dept", "Admin");
            int deletedCount = query.executeUpdate();
            tx.commit();
            System.out.println("Deleted " + deletedCount + " Admin department records.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void task9_NativeSalaryGreaterThan50000() {
        System.out.println("\n--- Task 9 (Native SQL): Teachers with salary > ₹50,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Teacher> query = session.createNativeQuery(
                "SELECT * FROM teachers WHERE salary > :minSalary", Teacher.class);
            query.setParameter("minSalary", 50000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task10_NativeOrderBySalaryDesc() {
        System.out.println("\n--- Task 10 (Native SQL): Teachers ordered by salary DESC ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Teacher> query = session.createNativeQuery(
                "SELECT * FROM teachers ORDER BY salary DESC", Teacher.class);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task11_NativeHighestSalary() {
        System.out.println("\n--- Task 11 (Native SQL): Highest salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object maxSalary = session.createNativeQuery("SELECT MAX(salary) FROM teachers").getSingleResult();
            System.out.println("Highest Salary: ₹" + maxSalary);
        }
    }

    public static void task12_NativeTotalSalarySum() {
        System.out.println("\n--- Task 12 (Native SQL): Total salary of all teachers ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object totalSalary = session.createNativeQuery("SELECT SUM(salary) FROM teachers").getSingleResult();
            System.out.println("Total School Payroll: ₹" + totalSalary);
        }
    }

    public static void task13_NativeDepartmentWiseCount() {
        System.out.println("\n--- Task 13 (Native SQL): Department-wise teacher count ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createNativeQuery(
                "SELECT department, COUNT(*) FROM teachers GROUP BY department").getResultList();
            for (Object[] row : results) {
                System.out.println("Department: " + row[0] + " | Count: " + row[1]);
            }
        }
    }

    public static void task14_NativeSalaryBetween40000And80000() {
        System.out.println("\n--- Task 14 (Native SQL): Teachers with salary between ₹40,000 and ₹80,000 ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Teacher> query = session.createNativeQuery(
                "SELECT * FROM teachers WHERE salary BETWEEN :low AND :high", Teacher.class);
            query.setParameter("low", 40000.0);
            query.setParameter("high", 80000.0);
            query.getResultList().forEach(System.out::println);
        }
    }

    public static void task15_HQLNamesStartingWithA() {
        System.out.println("\n--- Task 15 (HQL): Teachers whose names start with 'A' ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Teacher> query = session.createQuery(
                "FROM Teacher WHERE name LIKE :prefix", Teacher.class);
            query.setParameter("prefix", "A%");
            query.list().forEach(System.out::println);
        }
    }

    public static void task16_HQLSalaryGreaterThanAverage() {
        System.out.println("\n--- Task 16 (HQL): Teachers with salary > average salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Teacher> query = session.createQuery(
                "FROM Teacher WHERE salary > (SELECT AVG(salary) FROM Teacher)", Teacher.class);
            query.list().forEach(System.out::println);
        }
    }

    public static void task17_HQLSecondHighestSalary() {
        System.out.println("\n--- Task 17 (HQL): Second-highest salary ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                "SELECT MAX(salary) FROM Teacher WHERE salary < (SELECT MAX(salary) FROM Teacher)", Double.class);
            Double secondHighest = query.uniqueResult();
            System.out.println("Second Highest Salary: ₹" + secondHighest);
        }
    }

    public static void task18_HQLDepartmentsUsingInOperator() {
        System.out.println("\n--- Task 18 (HQL): Teachers in ('Science', 'Mathematics', 'English') using IN ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Teacher> query = session.createQuery(
                "FROM Teacher WHERE department IN (:depts)", Teacher.class);
            query.setParameterList("depts", Arrays.asList("Science", "Mathematics", "English"));
            query.list().forEach(System.out::println);
        }
    }

    public static void task19_HQLDepartmentWiseAvgSalaryGroupBy() {
        System.out.println("\n--- Task 19 (HQL): Department-wise average salary using GROUP BY ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT department, AVG(salary) FROM Teacher GROUP BY department HAVING COUNT(id) >= 1", Object[].class);
            for (Object[] row : query.list()) {
                System.out.printf("Department: %-12s | Avg Salary: ₹%.2f%n", row[0], (Double) row[1]);
            }
        }
    }

    public static void task20_HQLBulkIncreaseScienceSalaryBy10Percent() {
        System.out.println("\n--- Task 20 (HQL): Increase 'Science' department salary by 10% ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery(
                "UPDATE Teacher SET salary = salary * 1.10 WHERE department = :dept");
            query.setParameter("dept", "Science");
            int updatedRows = query.executeUpdate();
            tx.commit();
            System.out.println("Updated salary for " + updatedRows + " Science teachers.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
