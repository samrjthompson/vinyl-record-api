package uk.vinylrecordsapi.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class VinylRecordRequestMapperTest {

    @InjectMocks
    private VinylRecordRequestMapper mapper;

    @Test
    void mapRequestToDocument() {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist("The Beatles")
                        .setAlbum("Revolver")
                        .setYear("1966");

        // when
        VinylRecordDocument actualDocument = mapper.map(request);

        // then
        assertEquals("The Beatles", actualDocument.getArtist());
        assertEquals("Revolver", actualDocument.getAlbum());
        assertEquals("1966", actualDocument.getYear());
    }
}
