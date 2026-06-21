package com.shop.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(length = 180)
    private String title;

    @Column(name = "shoot_date")
    private LocalDate shootDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public Album() {}

    public Album(int id, Client client, Owner owner, String title,
                 LocalDate shootDate, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.owner = owner;
        this.title = title;
        this.shootDate = shootDate;
        this.createdAt = createdAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                  { return id; }
    public Client getClient()           { return client; }
    public Owner getOwner()             { return owner; }
    public String getTitle()            { return title; }
    public LocalDate getShootDate()     { return shootDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                        { this.id = id; }
    public void setClient(Client client)             { this.client = client; }
    public void setOwner(Owner owner)                { this.owner = owner; }
    public void setTitle(String title)               { this.title = title; }
    public void setShootDate(LocalDate shootDate)    { this.shootDate = shootDate; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Album a = new Album();
        public Builder client(Client v)       { a.client = v; return this; }
        public Builder owner(Owner v)         { a.owner = v; return this; }
        public Builder title(String v)        { a.title = v; return this; }
        public Builder shootDate(LocalDate v) { a.shootDate = v; return this; }
        public Album build()                  { return a; }
    }
}