package ru.sinitsyn.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Operation() {}

    public Operation(BigDecimal amount, OperationType type, Account account){
        this.account = account;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
        this.type = type;
    }

    public Long getId() {return id;}
    public BigDecimal getAmount() {return amount;}
    public OperationType getType() {return type;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public Account getAccount() {return account;}
    public void setAccount(Account account){
        this.account = account;
    }
}
