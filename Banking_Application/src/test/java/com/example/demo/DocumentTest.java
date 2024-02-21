package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document();
        assertNotNull(document);
    }

    @Test
    public void testDocumentGettersAndSetters() {
        Document document = new Document();
        document.setDocumentId(1);
        document.setDocumentName("TestDocument");
        LocalDate creationDate = LocalDate.of(2024, 2, 21);
        document.setCreationDate(creationDate);
        document.setCreatedBy("User1");
        LocalDate modificationDate = LocalDate.of(2024, 2, 22);
        document.setModificationDate(modificationDate);
        document.setModifiedBy("User2");

        assertEquals(1, document.getDocumentId());
        assertEquals("TestDocument", document.getDocumentName());
        assertEquals(creationDate, document.getCreationDate());
        assertEquals("User1", document.getCreatedBy());
        assertEquals(modificationDate, document.getModificationDate());
        assertEquals("User2", document.getModifiedBy());
    }

    @Test
    public void testDocumentToString() {
        Document document = new Document();
        document.setDocumentId(1);
        document.setDocumentName("TestDocument");
        LocalDate creationDate = LocalDate.of(2024, 2, 21);
        document.setCreationDate(creationDate);
        document.setCreatedBy("User1");
        LocalDate modificationDate = LocalDate.of(2024, 2, 22);
        document.setModificationDate(modificationDate);
        document.setModifiedBy("User2");

        String expectedString = "Document [documentId=1, documentName=TestDocument, creationDate=2024-02-21, createdBy=User1, modificationDate=2024-02-22, modifiedBy=User2]";
        assertEquals(expectedString, document.toString());
    }
}