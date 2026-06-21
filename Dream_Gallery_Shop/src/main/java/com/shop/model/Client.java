package com.shop.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(name = "couple_name", nullable = false, length = 180)
    private String coupleName;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String email;

    @Column(name = "wedding_date")
    private LocalDate weddingDate;

    @Column(name = "access_token", unique = true, nullable = false, length = 64)
    private String accessToken;

    @Column(name = "access_password")
    private String accessPassword;

    @Column(length = 30)
    private String status = "pending";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public Client() {}

    public Client(int id, Owner owner, String coupleName, String phone,
                  String email, LocalDate weddingDate, String accessToken,
                  String accessPassword, String status, String notes,
                  LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.coupleName = coupleName;
        this.phone = phone;
        this.email = email;
        this.weddingDate = weddingDate;
        this.accessToken = accessToken;
        this.accessPassword = accessPassword;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                   { return id; }
    public Owner getOwner()              { return owner; }
    public String getCoupleName()        { return coupleName; }
    public String getPhone()             { return phone; }
    public String getEmail()             { return email; }
    public LocalDate getWeddingDate()    { return weddingDate; }
    public String getAccessToken()       { return accessToken; }
    public String getAccessPassword()    { return accessPassword; }
    public String getStatus()            { return status; }
    public String getNotes()             { return notes; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                          { this.id = id; }
    public void setOwner(Owner owner)                  { this.owner = owner; }
    public void setCoupleName(String coupleName)       { this.coupleName = coupleName; }
    public void setPhone(String phone)                 { this.phone = phone; }
    public void setEmail(String email)                 { this.email = email; }
    public void setWeddingDate(LocalDate weddingDate)  { this.weddingDate = weddingDate; }
    public void setAccessToken(String accessToken)     { this.accessToken = accessToken; }
    public void setAccessPassword(String accessPassword){ this.accessPassword = accessPassword; }
    public void setStatus(String status)               { this.status = status; }
    public void setNotes(String notes)                 { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Client c = new Client();
        public Builder owner(Owner v)              { c.owner = v; return this; }
        public Builder coupleName(String v)        { c.coupleName = v; return this; }
        public Builder phone(String v)             { c.phone = v; return this; }
        public Builder email(String v)             { c.email = v; return this; }
        public Builder weddingDate(LocalDate v)    { c.weddingDate = v; return this; }
        public Builder accessToken(String v)       { c.accessToken = v; return this; }
        public Builder accessPassword(String v)    { c.accessPassword = v; return this; }
        public Builder status(String v)            { c.status = v; return this; }
        public Builder notes(String v)             { c.notes = v; return this; }
        public Client build()                      { return c; }
    }
}