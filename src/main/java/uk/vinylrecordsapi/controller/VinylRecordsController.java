package uk.vinylrecordsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;
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
    public ResponseEntity<List<VinylRecordResponse>> getAllVinylRecords() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllVinylRecords());
    }

    @PostMapping("/vinyl_records")
    public ResponseEntity<Void> insertNewVinylRecord(
            @RequestBody VinylRecordRequest request) {
        service.insertNewVinylRecord(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/vinyl_records/{record_id}")
    public ResponseEntity<Void> deleteVinylRecord(
            @PathVariable("record_id") String recordId) {
        service.deleteVinylRecord(recordId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
