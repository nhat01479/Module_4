package com.cg.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Transactional
public class Transfer extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName = "id", nullable = false)
    private Customer sender;
    @ManyToOne
    @JoinColumn(name="recipient_id", referencedColumnName = "id", nullable = false)
    private Customer recipient;
    @Column(name="transfer_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transferAmount;
    private Long fees = 10L;
    @Column(name="fees_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal feesAmount;
    @Column(name="transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public Transfer() {
    }

    public Transfer(Long id, Customer sender, Customer recipient, BigDecimal transferAmount, BigDecimal feesAmount, BigDecimal transactionAmount) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.transferAmount = transferAmount;
        this.feesAmount = feesAmount;
        this.transactionAmount = transactionAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public Customer getRecipient() {
        return recipient;
    }

    public void setRecipient(Customer recipient) {
        this.recipient = recipient;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public BigDecimal getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(BigDecimal feesAmount) {
        this.feesAmount = feesAmount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
