package com.airtribe.library.entity;

public class Branch {
    private final String branchId;
    private String name;


    public Branch(String branchId, String name) {
        this.branchId = branchId;
        this.name = name;
    }

    public String getBranchId() { return branchId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("Branch ID: %s | Name: %s", branchId, name);
    }
}