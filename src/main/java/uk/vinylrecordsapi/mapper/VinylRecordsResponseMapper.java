package uk.vinylrecordsapi.mapper;

import org.springframework.stereotype.Component;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class VinylRecordsResponseMapper {

    public VinylRecordResponse map(VinylRecordDocument document) {
        return new VinylRecordResponse()
                .setId(document.getId())
                .setArtist(document.getArtist())
                .setAlbum(document.getAlbum())
                .setYear(document.getYear());
    }

    public List<VinylRecordResponse> map(List<VinylRecordDocument> documents) {
        List<VinylRecordResponse> allDocuments = new ArrayList<>();
        for (var document : documents) {
            allDocuments.add(map(document));
        }
        return allDocuments;
    }
}
