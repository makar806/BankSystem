package ru.sinitsyn.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "account")
    private List<Operation> operations = new ArrayList<>();

    public Account() {}

    public Account(BigDecimal balance, User owner){
        this.balance = balance;
        this.owner = owner;
    }

    public Long getId() {return id;}
    public BigDecimal getBalance() {return balance;}
    public User getOwner() {return owner;}
    public List<Operation> getOperations() {return operations;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
}
