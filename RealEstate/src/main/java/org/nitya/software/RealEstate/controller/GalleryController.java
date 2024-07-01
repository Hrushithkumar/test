package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.Gallery;
import org.nitya.software.RealEstate.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping
    public List<Gallery> getAllImages() {
        return galleryService.getAllImages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gallery> getImageById(@PathVariable Long id) {
        return galleryService.getImageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Gallery addImage(@RequestBody Gallery gallery) {
        return galleryService.addImage(gallery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gallery> updateImage(@PathVariable Long id, @RequestBody Gallery galleryDetails) {
        return ResponseEntity.ok(galleryService.updateImage(id, galleryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        galleryService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
