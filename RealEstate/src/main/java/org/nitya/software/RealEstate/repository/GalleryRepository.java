package org.nitya.software.RealEstate.repository;

import org.nitya.software.RealEstate.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  
}
