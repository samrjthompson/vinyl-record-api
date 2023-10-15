package uk.vinylrecordsapi.service;

import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uk.vinylrecordsapi.exception.BadRequestException;
import uk.vinylrecordsapi.exception.ConflictException;
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
        if (checkForBadRequest(request)) {
            throw new BadRequestException("Request had an empty or null field.");
        }

        try {
            mongoTemplate.save(vinylRecordRequestMapper.map(request));
        } catch (DataAccessException ex) {
            throw new ServiceUnavailableException("MongoDB unavailable.");
        }
    }

    protected boolean checkForBadRequest(VinylRecordRequest request) {
        return StringUtils.isBlank(request.getArtist()) ||
                StringUtils.isBlank(request.getAlbum()) ||
                StringUtils.isBlank(request.getYear());
    }

    public void deleteVinylRecord(String recordId) {
        Query query = new Query()
                .addCriteria(Criteria.where("_id").is(recordId));
        try {
            Optional<VinylRecordDocument> document =
                    Optional.ofNullable(mongoTemplate.findAndRemove(query, VinylRecordDocument.class));
            if (document.isEmpty()) {
                throw new ConflictException("Document was either not found or already deleted.");
            }
        } catch (DataAccessException ex) {
            throw new ServiceUnavailableException("MongoDB was unavailable.");
        }
    }

    public void updateVinylRecord(VinylRecordRequest request, String recordId) {
        Query query = new Query()
                .addCriteria(Criteria.where("_id").is(recordId));
        UpdateResult updateResult;
        try {
            updateResult = mongoTemplate.updateFirst(query, vinylRecordRequestMapper.mapUpdate(request), VinylRecordDocument.class);
        } catch (DataAccessException ex) {
            throw new ServiceUnavailableException("MongoDB is unavailable.");
        }
        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException(String.format("Record could not be found with the id: [%s]", recordId));
        }
    }
}
