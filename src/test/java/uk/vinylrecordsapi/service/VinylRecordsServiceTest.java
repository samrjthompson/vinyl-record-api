package uk.vinylrecordsapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import uk.vinylrecordsapi.exception.BadRequestException;
import uk.vinylrecordsapi.exception.ConflictException;
import uk.vinylrecordsapi.exception.ServiceUnavailableException;
import uk.vinylrecordsapi.mapper.VinylRecordRequestMapper;
import uk.vinylrecordsapi.mapper.VinylRecordsResponseMapper;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class VinylRecordsServiceTest {

    private static final String RECORD_ID = "1234";

    @Mock
    private VinylRecordsResponseMapper vinylRecordsResponseMapper;
    @Mock
    private VinylRecordRequestMapper vinylRecordRequestMapper;
    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private VinylRecordsService service;

    @Test
    void successfullyGetAllRecords() {
        // given
        List<VinylRecordDocument> documents =
                List.of(new VinylRecordDocument()
                                .setArtist("The Beatles")
                                .setAlbum("Revolver"),
                        new VinylRecordDocument()
                                .setArtist("The Rolling Stones")
                                .setAlbum("Let It Bleed"));

        List<VinylRecordResponse> responseList =
                List.of(new VinylRecordResponse()
                                .setArtist("The Beatles")
                                .setAlbum("Revolver"),
                        new VinylRecordResponse()
                                .setArtist("The Rolling Stones")
                                .setAlbum("Let It Bleed"));

        doReturn(documents)
                .when(mongoTemplate).findAll(any());
        doReturn(responseList)
                .when(vinylRecordsResponseMapper).map(anyList());

        // when
        List<VinylRecordResponse> actual = service.getAllVinylRecords();

        // then
        assertEquals(responseList, actual);
        verify(mongoTemplate).findAll(VinylRecordDocument.class);
        verify(vinylRecordsResponseMapper).map(documents);
    }

    @Test
    void successfullyGetEmptyListWhenNoDocumentsInDb() {
        // given
        List<VinylRecordResponse> responseList = new ArrayList<>();

        doReturn(new ArrayList<>())
                .when(mongoTemplate).findAll(any());
        doReturn(new ArrayList<>())
                .when(vinylRecordsResponseMapper).map(anyList());

        // when
        List<VinylRecordResponse> actual = service.getAllVinylRecords();

        // then
        assertEquals(responseList, actual);
        verify(mongoTemplate).findAll(VinylRecordDocument.class);
        verify(vinylRecordsResponseMapper).map(new ArrayList<>());
    }

    @Test
    void getAllClientProfilesThrowsServiceUnavailable() {
        // given
        doThrow(new DataAccessException("..."){})
                .when(mongoTemplate).findAll(any());

        // when
        Executable executable = () -> service.getAllVinylRecords();;

        // then
        assertThrows(ServiceUnavailableException.class, executable);
        verify(mongoTemplate).findAll(VinylRecordDocument.class);
        verifyNoInteractions(vinylRecordsResponseMapper);
    }

    @Test
    void successfullyInsertNewVinylRecordDocument() {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist("The Beatles")
                        .setAlbum("Revolver")
                        .setYear("1966");

        // when
        service.insertNewVinylRecord(request);

        // then
        verify(mongoTemplate).save(vinylRecordRequestMapper.map(request));
    }

    @Test
    void insertNewVinylRecordDocumentThrowsBadRequestExceptionWithEmptyField() {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist("")
                        .setAlbum("Revolver")
                        .setYear("1966");

        // when
        Executable executable = () -> service.insertNewVinylRecord(request);

        // then
        assertThrows(BadRequestException.class, executable);
    }

    @Test
    void insertNewVinylRecordDocumentThrowsBadRequestExceptionWithNullField() {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist(null)
                        .setAlbum("Revolver")
                        .setYear("1966");

        // when
        Executable executable = () -> service.insertNewVinylRecord(request);

        // then
        assertThrows(BadRequestException.class, executable);
    }

    @Test
    void insertNewVinylRecordDocumentThrowsServiceUnavailableException() {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist("The Beatles")
                        .setAlbum("Revolver")
                        .setYear("1966");

        doThrow(new DataAccessException("..."){})
                .when(mongoTemplate).save(any());

        // when
        Executable executable = () -> service.insertNewVinylRecord(request);

        // then
        assertThrows(ServiceUnavailableException.class, executable);
    }

    @Test
    void successfullyDeleteVinylRecord() {
        // given
        Query query = new Query()
                .addCriteria(Criteria.where("_id").is(RECORD_ID));
        VinylRecordDocument document = new VinylRecordDocument();
        doReturn(document)
                .when(mongoTemplate).findAndRemove(any(), any());

        // when
        Executable executable = () -> service.deleteVinylRecord(RECORD_ID);

        // then
        assertDoesNotThrow(executable);
        verify(mongoTemplate).findAndRemove(query, VinylRecordDocument.class);
    }

    @Test
    void deleteVinylRecordThrowsConflictException() {
        // given
        doReturn(null)
                .when(mongoTemplate).findAndRemove(any(), any());

        // when
        Executable executable = () -> service.deleteVinylRecord(RECORD_ID);

        // then
        assertThrows(ConflictException.class, executable);
    }

    @Test
    void deleteVinylRecordThrowsServiceUnavailableException() {
        // given
        doThrow(new DataAccessException("..."){})
                .when(mongoTemplate).findAndRemove(any(), any());

        // when
        Executable executable = () -> service.deleteVinylRecord(RECORD_ID);

        // then
        assertThrows(ServiceUnavailableException.class, executable);
    }
}
