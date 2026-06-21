package com.shop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "photo_limit")
    private Integer photoLimit;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    private String currency = "INR";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public Package() {}

    public Package(int id, Owner owner, String name, String description,
                   Integer photoLimit, BigDecimal price, String currency,
                   Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.photoLimit = photoLimit;
        this.price = price;
        this.currency = currency;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                  { return id; }
    public Owner getOwner()             { return owner; }
    public String getName()             { return name; }
    public String getDescription()      { return description; }
    public Integer getPhotoLimit()      { return photoLimit; }
    public BigDecimal getPrice()        { return price; }
    public String getCurrency()         { return currency; }
    public Boolean getIsActive()        { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                         { this.id = id; }
    public void setOwner(Owner owner)                 { this.owner = owner; }
    public void setName(String name)                  { this.name = name; }
    public void setDescription(String description)    { this.description = description; }
    public void setPhotoLimit(Integer photoLimit)     { this.photoLimit = photoLimit; }
    public void setPrice(BigDecimal price)            { this.price = price; }
    public void setCurrency(String currency)          { this.currency = currency; }
    public void setIsActive(Boolean isActive)         { this.isActive = isActive; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Package p = new Package();
        public Builder owner(Owner v)             { p.owner = v; return this; }
        public Builder name(String v)             { p.name = v; return this; }
        public Builder description(String v)      { p.description = v; return this; }
        public Builder photoLimit(Integer v)      { p.photoLimit = v; return this; }
        public Builder price(BigDecimal v)        { p.price = v; return this; }
        public Builder currency(String v)         { p.currency = v; return this; }
        public Builder isActive(Boolean v)        { p.isActive = v; return this; }
        public Package build()                    { return p; }
    }
}