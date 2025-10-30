package tinder.tindermascotas.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tinder.tindermascotas.entities.Photo;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.repositories.PhotoRepository;

import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public Photo save(MultipartFile file) throws ErrorService {
        if (file != null && !file.isEmpty()) {
            try {
                Photo photo = new Photo();
                photo.setMime(file.getContentType());
                photo.setName(file.getOriginalFilename());
                photo.setContent(file.getBytes());

                return photoRepository.save(photo);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }

    @Transactional
    public Photo update(String idPhoto, MultipartFile file) throws ErrorService {
        if (file != null) {
            try {
                Photo photo = new Photo();

                if (idPhoto != null) {
                    Optional<Photo> response = photoRepository.findById(idPhoto);
                    if (response.isPresent()) {
                        photo = response.get();
                    }
                }
                photo.setMime(file.getContentType());
                photo.setName(file.getOriginalFilename());
                photo.setContent(file.getBytes());

                return photoRepository.save(photo);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }
}
