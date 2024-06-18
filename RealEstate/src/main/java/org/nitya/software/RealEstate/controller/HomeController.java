package org.nitya.software.RealEstate.controller;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.nitya.software.RealEstate.dto.HomeDto;
import org.nitya.software.RealEstate.model.Home;
import org.nitya.software.RealEstate.model.enums.Category;
import org.nitya.software.RealEstate.model.enums.HomeCategory;
import org.nitya.software.RealEstate.repository.HomeRepository;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/homes")
public class HomeController {

    private static final String UPLOAD_DIR = "test/RealEstate/src/main/resources/uploads/";

    private final HomeRepository homeRepository;

    public HomeController(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
        // Create upload directory if it doesn't exist
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get all images
     * @param filename
     * @return
     */
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new PathResource(imagePath);

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Method to upload project
     * @param category
     * @param title
     * @param description
     * @param image
     * @return
     */
    @PostMapping("/upload")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public ResponseEntity<?> uploadHome(
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam("price") float price) {

        try {
            // Save image to the file system
            String imageName = image.getOriginalFilename();
            if (imageName == null || imageName.trim().isEmpty()) {
                imageName = UUID.randomUUID() + ".jpg"; // You can change the extension as needed
            }

            // Sanitize the filename to remove any path traversal characters
            imageName = imageName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(imageName);
            //Files.write(imagePath, image.getBytes());

            try {
                Thumbnails.of(image.getInputStream())
                        .size(300, 200)
                        .toFile(imagePath.toFile());
            } catch (UnsupportedFormatException e) {
                return ResponseEntity.badRequest().body("Unsupported image format.");
            }

            HomeCategory homeCategory = Arrays.stream(HomeCategory.values())
                    .filter(cat -> cat.getCategoryName().equalsIgnoreCase(category))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + category));


            // Save project details to the database
            Home home = new Home();
            home.setCategory(homeCategory);
            home.setTitle(title);
            home.setDescription(description);
            home.setImage(imageName);
            home.setCreatedOn(LocalDate.now());
            home.setPrice(price);

            homeRepository.save(home);

            Map<String, Object> response = new HashMap<>();
            response.put("success", Boolean.TRUE);
            response.put("filename", imageName);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload project.");
        }
    }

    /**
     * Method to view all projects
     * @return
     */
    @GetMapping("/view")
    public ResponseEntity<List<HomeDto>> getAllHomes(){
        List<Home> homeList = homeRepository.findAll();

        List<HomeDto> homeDtos = new ArrayList<>();

        for (Home home : homeList) {
            HomeDto homeDto = new HomeDto();
            homeDto.setId(home.getId());
            homeDto.setCategory(home.getCategory().getCategoryName());
            homeDto.setTitle(home.getTitle());
            homeDto.setDescription(home.getDescription());
            homeDto.setImage(home.getImage());
            homeDtos.add(homeDto);
        }

        return ResponseEntity.ok(homeDtos);
    }

    /**
     * Method to get projects by ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Home> getHomeById(@PathVariable Long id){
        Optional<Home> home =  homeRepository.findById(id);
        return home.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Method to get all the categories
     * @return
     */
    @GetMapping("/categories")
    public List<String> getHomeCategories() {
        return Arrays.stream(HomeCategory.values())
                .map(HomeCategory::getCategoryName)
                .collect(Collectors.toList());
    }

    /**
     * Method to delete project by id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public ResponseEntity<?> deleteHome(@PathVariable Long id) {
        try {
            // Find the project by ID
            Home home = homeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Home not found with ID: " + id));

            // Delete the image file from the file system
            String imageName = home.getImage();
            Path imagePath = Paths.get(UPLOAD_DIR + imageName);
            Files.deleteIfExists(imagePath);

            // Delete the project from the database
            homeRepository.delete(home);

            return ResponseEntity.ok("Home deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete project.");
        }
    }
}

