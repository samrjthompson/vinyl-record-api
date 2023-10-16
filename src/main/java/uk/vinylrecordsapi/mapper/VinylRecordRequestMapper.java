package uk.vinylrecordsapi.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.vinylrecordsapi.exception.BadRequestException;
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

        final String artist = request.getArtist();
        final String album = request.getAlbum();
        final String year = request.getYear();

        if (!StringUtils.isBlank(artist)) {
            update.set("artist", artist);
            requestIsEmpty = false;
        }
        if (!StringUtils.isBlank(album)) {
            update.set("album", album);
            requestIsEmpty = false;
        }
        if(!StringUtils.isBlank(year)) {
            update.set("year", year);
            requestIsEmpty = false;
        }

        if (!requestIsEmpty) {
            update.set("updated",
                    new Updated()
                            .setAt(Instant.now().toString())
                            .setBy("user"));
        } else {
            throw new BadRequestException("Request contained no populated fields.");
        }
        return update;
    }
}
