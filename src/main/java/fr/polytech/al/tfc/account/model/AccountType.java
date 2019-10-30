package fr.polytech.al.tfc.account.model;

public enum AccountType {
    SAVINGSACCOUNT("savingsAccount"), CHECKACCOUNT("checkAccount");

    String name;
    AccountType(String name) {
        this.name = name;
    }
}
