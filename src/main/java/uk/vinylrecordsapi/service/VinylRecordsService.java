package uk.vinylrecordsapi.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import uk.vinylrecordsapi.exception.NotFoundException;
import uk.vinylrecordsapi.exception.ServiceUnavailableException;
import uk.vinylrecordsapi.mapper.VinylRecordRequestMapper;
import uk.vinylrecordsapi.mapper.VinylRecordsResponseMapper;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;

import java.util.List;
import java.util.Optional;

@Service
public class VinylRecordsService {

    private final MongoTemplate mongoTemplate;
    private final VinylRecordsResponseMapper vinylRecordsResponseMapper;
    private final VinylRecordRequestMapper vinylRecordRequestMapper;

    public VinylRecordsService(MongoTemplate mongoTemplate, VinylRecordsResponseMapper vinylRecordsResponseMapper, VinylRecordRequestMapper vinylRecordRequestMapper) {
        this.mongoTemplate = mongoTemplate;
        this.vinylRecordsResponseMapper = vinylRecordsResponseMapper;
        this.vinylRecordRequestMapper = vinylRecordRequestMapper;
    }

    public List<VinylRecordResponse> getAllVinylRecords() {
        Optional<List<VinylRecordDocument>> documents;
        try {
            documents = Optional.of(mongoTemplate.findAll(VinylRecordDocument.class));
        } catch (DataAccessException ex) {
            throw new ServiceUnavailableException("MongoDB unavailable.");
        }
        return vinylRecordsResponseMapper.map(
                documents.orElseThrow(
                        () -> new NotFoundException("Could not find any records")));
    }

    public void insertNewVinylRecord(VinylRecordRequest request) {
        try {
            mongoTemplate.save(vinylRecordRequestMapper.map(request));
        } catch (DataAccessException ex) {
            throw new ServiceUnavailableException("MongoDB unavailable.");
        }
    }
}
