package com.example.shoprunner_be.controllers;
import com.example.shoprunner_be.entitys.Category;
import com.example.shoprunner_be.services.Category.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/healthcheck")
@AllArgsConstructor
public class HealthCheckController {
    private final CategoryService categoryService;
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() throws Exception{
        List<Category> categories = categoryService.getAllCategory();
        String computerName = InetAddress.getLocalHost().getHostName();
        return ResponseEntity.ok().body(computerName);
    }
}