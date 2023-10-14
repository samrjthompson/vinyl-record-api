package uk.vinylrecordsapi.mapper;

import org.springframework.stereotype.Component;
import uk.vinylrecordsapi.model.Created;
import uk.vinylrecordsapi.model.Updated;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;

@Component
public class VinylRecordRequestMapper {

    public VinylRecordDocument map(VinylRecordRequest request) {
        return new VinylRecordDocument()
                .setArtist(request.getArtist())
                .setAlbum(request.getAlbum())
                .setYear(request.getYear())
                .setCreated(new Created().setAt("time").setBy("user"))
                .setUpdated(new Updated().setAt("time").setBy("user"));
    }
}
