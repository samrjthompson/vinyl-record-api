package uk.vinylrecordsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;
import uk.vinylrecordsapi.service.VinylRecordsService;

import java.util.List;

@CrossOrigin
@RestController
public class VinylRecordsController {

    private final VinylRecordsService service;

    public VinylRecordsController(VinylRecordsService service) {
        this.service = service;
    }

    @GetMapping("/vinyl_records")
    public ResponseEntity<List<VinylRecordResponse>> getAllClientProfiles() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllVinylRecords());
    }
}
