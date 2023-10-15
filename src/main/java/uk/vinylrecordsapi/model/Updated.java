package uk.vinylrecordsapi.model;

import java.util.Objects;

public class Updated {

    private String at;
    private String by;

    public String getAt() {
        return at;
    }

    public Updated setAt(String at) {
        this.at = at;
        return this;
    }

    public String getBy() {
        return by;
    }

    public Updated setBy(String by) {
        this.by = by;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Updated updated = (Updated) o;

        if (!Objects.equals(at, updated.at)) {
            return false;
        }
        return Objects.equals(by, updated.by);
    }

    @Override
    public int hashCode() {
        int result = at != null ? at.hashCode() : 0;
        result = 31 * result + (by != null ? by.hashCode() : 0);
        return result;
    }
}
