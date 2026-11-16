package com.bank.entity;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "branch")
    private String branch;

    @Column(name = "balance")
    private double balance;

    public Account() {}

    public Account(String name, String branch, double balance) {
        this.name = name;
        this.branch = branch;
        this.balance = balance;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "Account [ID=" + id + ", Name='" + name + "', Branch='" + branch +
               "', Balance=₹" + balance + "]";
    }
}
