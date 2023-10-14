package uk.vinylrecordsapi.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class VinylRecordResponseMapperTest {

    @InjectMocks
    private VinylRecordsResponseMapper mapper;

    @Test
    void mapSingleResponse() {
        // given
        VinylRecordDocument document =
                new VinylRecordDocument()
                        .setArtist("The Beatles")
                        .setAlbum("Revolver");

        // when
        VinylRecordResponse response = mapper.map(document);

        // then
        assertEquals("The Beatles", response.getArtist());
        assertEquals("Revolver", response.getAlbum());
    }

    @Test
    void mapMultipleResponses() {
        // given
        List<VinylRecordDocument> documents =
                List.of(new VinylRecordDocument()
                                .setArtist("The Beatles")
                                .setAlbum("Revolver"),
                        new VinylRecordDocument()
                                .setArtist("The Rolling Stones")
                                .setAlbum("Let It Bleed"));

        // when
        List<VinylRecordResponse> response = mapper.map(documents);

        // then
        assertFalse(response.isEmpty());
        assertEquals("The Beatles", response.get(0).getArtist());
        assertEquals("Revolver", response.get(0).getAlbum());
        assertEquals("The Rolling Stones", response.get(1).getArtist());
        assertEquals("Let It Bleed", response.get(1).getAlbum());
    }
}
