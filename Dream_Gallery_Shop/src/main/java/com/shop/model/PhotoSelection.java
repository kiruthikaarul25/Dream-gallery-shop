package com.shop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo_selections",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"client_id", "photo_id"}))
public class PhotoSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @Column(name = "selected_at")
    private LocalDateTime selectedAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public PhotoSelection() {}

    public PhotoSelection(int id, Client client, Photo photo,
                          LocalDateTime selectedAt) {
        this.id = id;
        this.client = client;
        this.photo = photo;
        this.selectedAt = selectedAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                    { return id; }
    public Client getClient()             { return client; }
    public Photo getPhoto()               { return photo; }
    public LocalDateTime getSelectedAt()  { return selectedAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                           { this.id = id; }
    public void setClient(Client client)                { this.client = client; }
    public void setPhoto(Photo photo)                   { this.photo = photo; }
    public void setSelectedAt(LocalDateTime selectedAt) { this.selectedAt = selectedAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final PhotoSelection ps = new PhotoSelection();
        public Builder client(Client v)          { ps.client = v; return this; }
        public Builder photo(Photo v)            { ps.photo = v; return this; }
        public Builder selectedAt(LocalDateTime v){ ps.selectedAt = v; return this; }
        public PhotoSelection build()            { return ps; }
    }
}