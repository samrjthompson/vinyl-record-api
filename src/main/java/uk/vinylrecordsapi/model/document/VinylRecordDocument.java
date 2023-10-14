package uk.vinylrecordsapi.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.vinylrecordsapi.model.Created;
import uk.vinylrecordsapi.model.Updated;

@Document(collection = "records")
public class VinylRecordDocument {

    @Id
    private String id;
    @JsonProperty
    private String artist;
    @JsonProperty
    private String album;
    @JsonProperty
    private String year;
    @JsonProperty
    private Updated updated;
    @JsonProperty
    private Created created;

    public String getId() {
        return id;
    }

    public VinylRecordDocument setId(String id) {
        this.id = id;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public VinylRecordDocument setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public VinylRecordDocument setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getYear() {
        return year;
    }

    public VinylRecordDocument setYear(String year) {
        this.year = year;
        return this;
    }

    public Updated getUpdated() {
        return updated;
    }

    public VinylRecordDocument setUpdated(Updated updated) {
        this.updated = updated;
        return this;
    }

    public Created getCreated() {
        return created;
    }

    public VinylRecordDocument setCreated(Created created) {
        this.created = created;
        return this;
    }
}
