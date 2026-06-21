package com.shop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(name = "file_path", nullable = false, columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // ─── Constructors ───────────────────────────────
    public Photo() {}

    public Photo(int id, Album album, Owner owner, String filename,
                 String filePath, String thumbnailUrl, Long fileSize,
                 Boolean isDeleted, LocalDateTime uploadedAt) {
        this.id = id;
        this.album = album;
        this.owner = owner;
        this.filename = filename;
        this.filePath = filePath;
        this.thumbnailUrl = thumbnailUrl;
        this.fileSize = fileSize;
        this.isDeleted = isDeleted;
        this.uploadedAt = uploadedAt;
    }

    // ─── Getters ────────────────────────────────────
    public int getId()                   { return id; }
    public Album getAlbum()              { return album; }
    public Owner getOwner()              { return owner; }
    public String getFilename()          { return filename; }
    public String getFilePath()          { return filePath; }
    public String getThumbnailUrl()      { return thumbnailUrl; }
    public Long getFileSize()            { return fileSize; }
    public Boolean getIsDeleted()        { return isDeleted; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    // ─── Setters ────────────────────────────────────
    public void setId(int id)                          { this.id = id; }
    public void setAlbum(Album album)                  { this.album = album; }
    public void setOwner(Owner owner)                  { this.owner = owner; }
    public void setFilename(String filename)           { this.filename = filename; }
    public void setFilePath(String filePath)           { this.filePath = filePath; }
    public void setThumbnailUrl(String thumbnailUrl)   { this.thumbnailUrl = thumbnailUrl; }
    public void setFileSize(Long fileSize)             { this.fileSize = fileSize; }
    public void setIsDeleted(Boolean isDeleted)        { this.isDeleted = isDeleted; }
    public void setUploadedAt(LocalDateTime uploadedAt){ this.uploadedAt = uploadedAt; }

    // ─── Builder ────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Photo p = new Photo();
        public Builder album(Album v)            { p.album = v; return this; }
        public Builder owner(Owner v)            { p.owner = v; return this; }
        public Builder filename(String v)        { p.filename = v; return this; }
        public Builder filePath(String v)        { p.filePath = v; return this; }
        public Builder thumbnailUrl(String v)    { p.thumbnailUrl = v; return this; }
        public Builder fileSize(Long v)          { p.fileSize = v; return this; }
        public Builder isDeleted(Boolean v)      { p.isDeleted = v; return this; }
        public Photo build()                     { return p; }
    }
}