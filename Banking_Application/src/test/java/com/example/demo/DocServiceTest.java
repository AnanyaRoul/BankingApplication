package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DocServiceTest {

	@Mock
	private DocRepository docRepository;
	
	@InjectMocks
	private DocService docService;
    
    @BeforeEach
    public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        Field field = DocService.class.getDeclaredField("mdfByDefault");
        field.setAccessible(true);
        field.set(docService, "NA");
        
        Field field1 = DocService.class.getDeclaredField("mdfBy");
        field1.setAccessible(true);
        field1.set(docService, "Ananya-Deleted");
        
        Field field2 = DocService.class.getDeclaredField("folderName");
        field2.setAccessible(true);
        field2.set(docService, "/PDF/");
    }
	
	@Test
    public void testAddDocument() {
      
        Document document = new Document();
        String fileName = "test_document.txt";

        when(docRepository.save(document)).thenReturn(document);

        Document savedDocument = docService.addDocument(document, fileName);

        verify(docRepository, times(1)).save(document);

        assert(savedDocument.getDocumentName()).equals(fileName);
    }
	
	
	 @Test
	    public void testGetAllDocuments() {
	       
		 Document doc1=new Document();
		 Document doc2=new Document();
		 doc1.setDocumentName("document1.pdf");
		 doc2.setDocumentName("document2.pdf");
		 
	        List<Document> documents = new ArrayList<>();
	        documents.add(doc1);
	        documents.add(doc2);
	     
	        when(docRepository.findByModifiedBy(anyString())).thenReturn(documents);

	        List<FileInfo> result = docService.getAllDocuments();

	        verify(docRepository).findByModifiedBy(anyString());

	        assertEquals(2, result.size());
	        assertEquals("document1.pdf", result.get(0).getFileName());
	        assertEquals("document2.pdf", result.get(1).getFileName());
	    }
	 

	    @Test
	    public void testModifyContents_WithExistingIdAndNotModified() {
	    
	        Document mockDocument = new Document();
	        mockDocument.setModifiedBy("NA");
	        
	        when(docRepository.findById(anyInt())).thenReturn(Optional.of(mockDocument));
	        when(docRepository.save(mockDocument)).thenReturn(mockDocument);

	        Document result = docService.modifyContents(1, null);

	        assertNotNull(result);
	        assertEquals("Ananya-Deleted", result.getModifiedBy());
	        assertEquals(LocalDate.now(), result.getModificationDate());
	        
	        verify(docRepository, times(1)).save(any(Document.class));
	    }
	    
	    @Test
	    public void testModifyContents_WithExistingIdAndModified() {
	      
	        Document mockDocument = new Document();
	        mockDocument.setModifiedBy("Ananya-Deleted"); 
	        when(docRepository.findById(anyInt())).thenReturn(Optional.of(mockDocument));

	        Document result = docService.modifyContents(1, null);
	      
	        assertNotNull(result);
	        assertEquals("Ananya-Deleted", result.getModifiedBy());
	        assertEquals(null, result.getModificationDate());
	        
	        verify(docRepository, never()).save(any(Document.class));
	    }
	   
	    @Test
	    public void testModifyContents_WithNonExistingId() {
	       
	        when(docRepository.findById(anyInt())).thenReturn(Optional.empty());

	        Document result = docService.modifyContents(1, null);

	        assertNull(result);
	       
	        verify(docRepository, never()).save(any(Document.class));
	    }


	    @Test
	    public void testModifyContents_WithNonExistingName() {
	        
	    	when(docRepository.findByDocumentName(anyString())).thenReturn(List.of());

	        Document result = docService.modifyContents(null, "someName");

	        assertNull(result);
	       
	        verify(docRepository, never()).save(any(Document.class));
	    }
	  
	    

	
}
