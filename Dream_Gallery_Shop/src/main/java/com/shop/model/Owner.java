package com.shop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "studio_name", length = 180)
    private String studioName;

    @Column(length = 20)
    private String phone;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public Owner() {}

    public Owner(int id, String name, String email, String passwordHash,
                 String studioName, String phone, String logoUrl,
                 LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.studioName = studioName;
        this.phone = phone;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                  { return id; }
    public String getName()             { return name; }
    public String getEmail()            { return email; }
    public String getPasswordHash()     { return passwordHash; }
    public String getStudioName()       { return studioName; }
    public String getPhone()            { return phone; }
    public String getLogoUrl()          { return logoUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                        { this.id = id; }
    public void setName(String name)                 { this.name = name; }
    public void setEmail(String email)               { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setStudioName(String studioName)     { this.studioName = studioName; }
    public void setPhone(String phone)               { this.phone = phone; }
    public void setLogoUrl(String logoUrl)           { this.logoUrl = logoUrl; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Owner o = new Owner();
        public Builder name(String v)         { o.name = v; return this; }
        public Builder email(String v)        { o.email = v; return this; }
        public Builder passwordHash(String v) { o.passwordHash = v; return this; }
        public Builder studioName(String v)   { o.studioName = v; return this; }
        public Builder phone(String v)        { o.phone = v; return this; }
        public Builder logoUrl(String v)      { o.logoUrl = v; return this; }
        public Owner build()                  { return o; }
    }
}