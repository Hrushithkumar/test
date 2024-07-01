package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.Gallery;
import org.nitya.software.RealEstate.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    public List<Gallery> getAllImages() {
        return galleryRepository.findAll();
    }

    public Optional<Gallery> getImageById(Long id) {
        return galleryRepository.findById(id);
    }

    public Gallery addImage(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    public Gallery updateImage(Long id, Gallery galleryDetails) {
        return galleryRepository.findById(id).map(gallery -> {
            gallery.setName(galleryDetails.getName());
            gallery.setCategory(galleryDetails.getCategory());
            return galleryRepository.save(gallery);
        }).orElseGet(() -> {
            galleryDetails.setId(id);
            return galleryRepository.save(galleryDetails);
        });
    }

    public void deleteImage(Long id) {
        galleryRepository.deleteById(id);
    }
}
