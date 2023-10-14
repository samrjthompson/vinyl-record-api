package uk.vinylrecordsapi.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class VinylRecordResponse {

    @Id
    private String id;
    @JsonProperty
    private String artist;
    @JsonProperty
    private String album;
    @JsonProperty
    private String year;

    public String getId() {
        return id;
    }

    public VinylRecordResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public VinylRecordResponse setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public VinylRecordResponse setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getYear() {
        return year;
    }

    public VinylRecordResponse setYear(String year) {
        this.year = year;
        return this;
    }
}
