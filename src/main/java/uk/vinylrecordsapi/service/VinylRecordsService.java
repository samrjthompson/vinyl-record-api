package uk.vinylrecordsapi.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import uk.vinylrecordsapi.exception.NotFoundException;
import uk.vinylrecordsapi.exception.ServiceUnavailableException;
import uk.vinylrecordsapi.mapper.VinylRecordsResponseMapper;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;

import java.util.List;
import java.util.Optional;

@Service
public class VinylRecordsService {

    private final MongoTemplate mongoTemplate;
    private final VinylRecordsResponseMapper mapper;

    public VinylRecordsService(MongoTemplate mongoTemplate, VinylRecordsResponseMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    public List<VinylRecordResponse> getAllVinylRecords() {
        Optional<List<VinylRecordDocument>> documents;
        try {
            documents = Optional.of(mongoTemplate.findAll(VinylRecordDocument.class));
        } catch (DataAccessException ex) {
            throw new ServiceUnavailableException("MongoDB unavailable.");
        }
        return mapper.map(
                documents.orElseThrow(
                        () -> new NotFoundException("Could not find any records")));
    }
}
