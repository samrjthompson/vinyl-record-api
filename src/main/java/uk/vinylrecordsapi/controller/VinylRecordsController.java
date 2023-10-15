package uk.vinylrecordsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    // TODO: Refactor insert/update to become a PUT upsert combined
    @PatchMapping("/vinyl_records/{record_id}")
    public ResponseEntity<Void> updateVinylRecord(
            @PathVariable("record_id") String recordId,
            @RequestBody VinylRecordRequest request) {
        service.updateVinylRecord(request, recordId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
