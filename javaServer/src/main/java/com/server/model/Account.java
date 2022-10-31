package com.server.model;

import java.math.BigDecimal;

public class Account {
    private String account;
    private BigDecimal balance;

    public Account(String account, BigDecimal balance){
        this.account = account;
        this.balance = balance;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return  '{' + account + ',' + balance + '}';
    }
}
