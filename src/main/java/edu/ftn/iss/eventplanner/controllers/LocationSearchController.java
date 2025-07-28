package edu.ftn.iss.eventplanner.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class LocationSearchController {

    @GetMapping("/api/location/search")
    public ResponseEntity<String> searchLocation(@RequestParam String query) {
        String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&addressdetails=1&limit=25";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "EventPlannerApp");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return ResponseEntity.ok(response.getBody());
    }

}

