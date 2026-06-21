package com.shop.service;
import com.shop.model.*;
import com.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    public Client getClientRef(int clientId) {
        return clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Photo uploadPhoto(MultipartFile file,
            Album album, Owner owner) throws IOException {
        Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir)
                               .toAbsolutePath();
        Files.createDirectories(uploadPath);
        System.out.println("=== UPLOAD PATH: " + uploadPath.toString());

        String original = file.getOriginalFilename()
            .replaceAll("[^a-zA-Z0-9._-]", "_");
        String filename = System.currentTimeMillis() + "_" + original;
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath,
            StandardCopyOption.REPLACE_EXISTING);
        System.out.println("=== FILE SAVED: " + filePath.toString());
        System.out.println("=== FILE EXISTS: " + Files.exists(filePath));

        Photo photo = new Photo();
        photo.setAlbum(album);
        photo.setOwner(owner);
        photo.setFilename(filename);
        photo.setFilePath(uploadDir + "/" + filename);  // ✅ fixed: uploads/photos/filename.jpg
        photo.setFileSize(file.getSize());
        photo.setIsDeleted(false);
        return photoRepository.save(photo);
    }

    public List<Photo> getPhotosByAlbum(int albumId) {
        return photoRepository.findByAlbumIdAndIsDeletedFalse(albumId);
    }

    public List<Photo> findAllByClientId(int clientId) {
        return photoRepository.findByAlbum_Client_IdAndIsDeletedFalse(clientId);
    }

    public long countByOwner(int ownerId) {
        return photoRepository.countByOwnerIdAndIsDeletedFalse(ownerId);
    }

    public void deletePhoto(int photoId) {
        photoRepository.findById(photoId).ifPresent(p -> {
            p.setIsDeleted(true);
            photoRepository.save(p);
        });
    }

    public Optional<Photo> getById(int id) {
        return photoRepository.findById(id);
    }

    public void restorePhoto(int photoId) {
        photoRepository.findById(photoId).ifPresent(p -> {
            p.setIsDeleted(false);
            photoRepository.save(p);
        });
    }
}