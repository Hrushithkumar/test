package org.nitya.software.RealEstate.controller;

import net.coobird.thumbnailator.Thumbnails;
import org.nitya.software.RealEstate.dto.ProjectDto;
import org.nitya.software.RealEstate.model.Project;
import org.nitya.software.RealEstate.model.enums.Category;
import org.nitya.software.RealEstate.repository.ProjectRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private static final String UPLOAD_DIR = "test/RealEstate/src/main/resources/uploads/";

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
    @GetMapping("/images/{filename.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            //Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new ClassPathResource("uploads/" + filename);

            if (resource.exists()) {
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
    public ResponseEntity<?> uploadProject(
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {

        try {
            // Save image to the file system
            String imageName = image.getOriginalFilename();
            Path imagePath = Paths.get(UPLOAD_DIR + imageName);
            //Files.write(imagePath, image.getBytes());

            Thumbnails.of(image.getInputStream())
                    .size(300, 200)
                    .toFile(imagePath.toFile());

            Category projectCategory = Arrays.stream(Category.values())
                    .filter(cat -> cat.getCategoryName().equalsIgnoreCase(category))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + category));


            // Save project details to the database
            Project project = new Project();
            project.setCategory(projectCategory);
            project.setTitle(title);
            project.setDescription(description);
            project.setImage(imageName);

            projectRepository.save(project);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
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
    public ResponseEntity<List<ProjectDto>> getAllProjects(){
        List<Project> projectList = projectRepository.findAll();

        List<ProjectDto> projectDtos = new ArrayList<>();

        for (Project project : projectList) {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setCategory(project.getCategory().getCategoryName());
            projectDto.setTitle(project.getTitle());
            projectDto.setDescription(project.getDescription());
            projectDto.setImage(project.getImage());
            projectDtos.add(projectDto);
        }

        return ResponseEntity.ok(projectDtos);
    }

    /**
     * Method to get projects by ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id){
        Optional<Project> project =  projectRepository.findById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Method to get all the categories
     * @return
     */
    @GetMapping("/categories")
    public List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }

    /**
     * Method to delete project by id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            // Find the project by ID
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));

            // Delete the image file from the file system
            String imageName = project.getImage();
            Path imagePath = Paths.get(UPLOAD_DIR + imageName);
            Files.deleteIfExists(imagePath);

            // Delete the project from the database
            projectRepository.delete(project);

            return ResponseEntity.ok("Project deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete project.");
        }
    }
}

