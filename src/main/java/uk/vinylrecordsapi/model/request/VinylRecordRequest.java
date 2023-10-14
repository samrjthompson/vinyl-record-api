package uk.vinylrecordsapi.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VinylRecordRequest {

    @JsonProperty(required = true)
    private String artist;
    @JsonProperty(required = true)
    private String album;
    @JsonProperty(required = true)
    private String year;

    public String getArtist() {
        return artist;
    }

    public VinylRecordRequest setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public VinylRecordRequest setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getYear() {
        return year;
    }

    public VinylRecordRequest setYear(String year) {
        this.year = year;
        return this;
    }
}
