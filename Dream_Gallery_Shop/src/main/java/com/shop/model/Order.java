package com.shop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package packageEntity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency = "INR";

    @Column(length = 30)
    private String status = "pending";

    @Column(name = "payment_mode", length = 50)
    private String paymentMode = "cash";

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public Order() {}

    public Order(int id, Client client, Package packageEntity,
                 BigDecimal amount, String currency, String status,
                 String paymentMode, LocalDateTime paidAt,
                 String notes, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.packageEntity = packageEntity;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentMode = paymentMode;
        this.paidAt = paidAt;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                   { return id; }
    public Client getClient()            { return client; }
    public Package getPackageEntity()    { return packageEntity; }
    public BigDecimal getAmount()        { return amount; }
    public String getCurrency()          { return currency; }
    public String getStatus()            { return status; }
    public String getPaymentMode()       { return paymentMode; }
    public LocalDateTime getPaidAt()     { return paidAt; }
    public String getNotes()             { return notes; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                          { this.id = id; }
    public void setClient(Client client)               { this.client = client; }
    public void setPackageEntity(Package packageEntity){ this.packageEntity = packageEntity; }
    public void setAmount(BigDecimal amount)           { this.amount = amount; }
    public void setCurrency(String currency)           { this.currency = currency; }
    public void setStatus(String status)               { this.status = status; }
    public void setPaymentMode(String paymentMode)     { this.paymentMode = paymentMode; }
    public void setPaidAt(LocalDateTime paidAt)        { this.paidAt = paidAt; }
    public void setNotes(String notes)                 { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Order o = new Order();
        public Builder client(Client v)           { o.client = v; return this; }
        public Builder packageEntity(Package v)   { o.packageEntity = v; return this; }
        public Builder amount(BigDecimal v)        { o.amount = v; return this; }
        public Builder currency(String v)          { o.currency = v; return this; }
        public Builder status(String v)            { o.status = v; return this; }
        public Builder paymentMode(String v)       { o.paymentMode = v; return this; }
        public Builder paidAt(LocalDateTime v)     { o.paidAt = v; return this; }
        public Builder notes(String v)             { o.notes = v; return this; }
        public Order build()                       { return o; }
    }
}