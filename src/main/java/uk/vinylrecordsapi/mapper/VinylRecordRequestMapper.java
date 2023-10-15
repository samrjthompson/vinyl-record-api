package uk.vinylrecordsapi.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.vinylrecordsapi.model.Created;
import uk.vinylrecordsapi.model.Updated;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;

import java.time.Instant;

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

    public Update mapUpdate(VinylRecordRequest request) {
        boolean requestIsEmpty = true;
        Update update = new Update();

        if (!StringUtils.isBlank(request.getArtist())) {
            update.set("artist", request.getArtist());
            requestIsEmpty = false;
        }
        if (!StringUtils.isBlank(request.getAlbum())) {
            update.set("album", request.getAlbum());
            requestIsEmpty = false;
        }
        if(!StringUtils.isBlank(request.getYear())) {
            update.set("year", request.getYear());
            requestIsEmpty = false;
        }

        if (!requestIsEmpty) {
            update.set("updated",
                    new Updated()
                            .setAt(Instant.now().toString())
                            .setBy("user"));
        }

        return update;
    }
}
