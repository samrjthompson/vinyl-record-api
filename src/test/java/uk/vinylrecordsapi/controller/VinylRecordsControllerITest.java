package uk.vinylrecordsapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.vinylrecordsapi.Main;
import uk.vinylrecordsapi.model.document.VinylRecordDocument;
import uk.vinylrecordsapi.model.request.VinylRecordRequest;
import uk.vinylrecordsapi.model.response.VinylRecordResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VinylRecordsControllerITest {

    private static final String VINYL_RECORD_COLLECTION = "records";
    private static final String GET_ALL_RECORDS_ENDPOINT = "/vinyl_records";
    private static final String POST_RECORD_ENDPOINT = "/vinyl_records";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4");

    private static MongoTemplate mongoTemplate;

    @BeforeAll
    static void start() {
        final String replicaSetUrl = mongoDBContainer.getReplicaSetUrl();
        mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(replicaSetUrl));

        System.setProperty("spring.data.mongodb.uri", replicaSetUrl);
        System.setProperty("spring.data.mongodb.database", mongoTemplate.getDb().getName());
    }

    @BeforeEach
    void cleanUp() {
        mongoTemplate.dropCollection(VINYL_RECORD_COLLECTION);
        mongoTemplate.createCollection(VINYL_RECORD_COLLECTION);
        assertTrue(mongoTemplate.findAll(VinylRecordDocument.class).isEmpty());
    }

    @Test
    void successfullyGetAllVinylRecords() throws Exception {
        // given
        List<VinylRecordDocument> documents =
                List.of(new VinylRecordDocument()
                                .setArtist("The Beatles")
                                .setAlbum("Revolver"),
                        new VinylRecordDocument()
                                .setArtist("The Rolling Stones")
                                .setAlbum("Let It Bleed"));

        String rawJson = new String(Files.readAllBytes(Path.of("src/test/resources/vinyl-record-document.json")));
        List<Document> documentsToAdd = new ArrayList<>();
        for (VinylRecordDocument record : documents) {
            documentsToAdd.add(Document.parse(
                    rawJson.replaceAll("<id>", UUID.randomUUID().toString())
                            .replaceAll("<artistName>", record.getArtist())
                            .replaceAll("<albumName>", record.getAlbum())));
        }
        mongoTemplate.insert(documentsToAdd, VINYL_RECORD_COLLECTION);

        // when
        ResultActions result =
                mockMvc.perform(get(GET_ALL_RECORDS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        List<VinylRecordResponse> actual =
                objectMapper.readValue(
                        result.andReturn().getResponse().getContentAsString(),
                        new TypeReference<>() {});

        assertEquals(documents.size(), actual.size());
        assertEquals("The Beatles", actual.get(0).getArtist());
        assertEquals("Revolver", actual.get(0).getAlbum());
        assertEquals("The Rolling Stones", actual.get(1).getArtist());
        assertEquals("Let It Bleed", actual.get(1).getAlbum());
    }

    @Test
    void successfullyInsertVinylRecord() throws Exception {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist("The Beatles")
                        .setAlbum("Revolver")
                        .setYear("1966");

        // when
        ResultActions result =
                mockMvc.perform(post(POST_RECORD_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        List<VinylRecordDocument> documents = mongoTemplate.findAll(VinylRecordDocument.class);
        assertEquals(1, documents.size());
        assertEquals("The Beatles", documents.get(0).getArtist());
        assertEquals("Revolver", documents.get(0).getAlbum());
        assertEquals("1966", documents.get(0).getYear());
    }

    @Test
    void insertVinylRecordThrowsBadRequest() throws Exception {
        // given
        VinylRecordRequest request =
                new VinylRecordRequest()
                        .setArtist("")
                        .setAlbum("Revolver")
                        .setYear("1966");

        // when
        ResultActions result =
                mockMvc.perform(post(POST_RECORD_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        List<VinylRecordDocument> documents = mongoTemplate.findAll(VinylRecordDocument.class);
        assertEquals(0, documents.size());
    }
}
