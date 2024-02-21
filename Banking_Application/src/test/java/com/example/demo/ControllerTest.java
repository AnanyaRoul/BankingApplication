package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Mock
    private DocService mockDocService;
	
	@Mock
	private FileFunction mockFileFunc;
	
	@Mock
	private Document mockDocument;

    @InjectMocks
    private Controller controller;
    
    @BeforeEach
    public void setup() {
    	
    	mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
    @Test
    public void testUploadFileSuccess() throws Exception {
       
    	byte[] fileContent = "This is a sample file content.".getBytes();
    	MockMultipartFile sampleFile = new MockMultipartFile("file","sample.txt","text/plain",fileContent);
       
       
        when(mockFileFunc.UploadFile(any())).thenReturn("uploaded-file-path");
        when(mockDocService.addDocument(any(Document.class), anyString())).thenReturn(new Document());

        
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadDocument")
                .file(sampleFile))
                .andExpect(status().isOk())
                .andReturn();

        
        String responseBody = mvcResult.getResponse().getContentAsString();
        assert responseBody.equals("Working...");

        
        verify(mockFileFunc, times(1)).UploadFile(any());
        verify(mockDocService, times(1)).addDocument(any(Document.class),anyString());
    }
    

	
	  @Test 
	  public void testUploadFileEmptyFile() throws Exception {
		  
		  MockMultipartFile file = new MockMultipartFile("file", "", "",new byte[0]);
	  
		 
	  mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadDocument").file(file))
	  .andExpect(status().isBadRequest())
	  .andExpect(content().string("File cannot be empty"));
	  
	  
	  verifyNoInteractions(mockFileFunc);
	  verifyNoInteractions(mockDocService); 
	  
	  }
	  
	  @Test
	    public void testGetAllDocumentsFound() throws Exception {
	        
	        List<FileInfo> fileInfoList = new ArrayList<>();
	        fileInfoList.add(new FileInfo());
	        fileInfoList.add(new FileInfo());

	        
	        when(mockDocService.getAllDocuments()).thenReturn(fileInfoList);
	        

	        mockMvc.perform(MockMvcRequestBuilders.get("/getAllDocuments")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(MockMvcResultMatchers.status().isOk());
	        
	        verify(mockDocService, times(1)).getAllDocuments();
	    }

	    @Test
	    public void testGetAllDocumentsNotFound() throws Exception {
	     
	        when(mockDocService.getAllDocuments()).thenReturn(new ArrayList<>());
	        
	        mockMvc.perform(MockMvcRequestBuilders.get("/getAllDocuments")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(MockMvcResultMatchers.status().isNotFound());

	        verify(mockDocService, times(1)).getAllDocuments();
	    }
	    
	    @Test
	    public void testDeleteDocument_DeletedById() throws Exception {
	        int id = 123;
	       
	        when(mockDocService.modifyContents(eq(id), any())).thenReturn(new Document());

	        when(mockFileFunc.DeleteFile(any())).thenReturn(true);
	        
	        mockMvc.perform(delete("/deleteDocument")
	                .param("id", String.valueOf(id))
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Deleted..."));
	    }
	    
	    @Test
	    public void testDeleteDocument_DeletedByName() throws Exception {
	        String name="Ananya";
	       
	        when(mockDocService.modifyContents(any(), eq(name))).thenReturn(new Document());

	        when(mockFileFunc.DeleteFile(any())).thenReturn(true);
	       
	        mockMvc.perform(delete("/deleteDocument")
	                .param("name", String.valueOf(name))
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Deleted..."));
	    }
	    
	    
	    @Test
	    public void testDeleteDocument_WithInvalidId() throws Exception {
	        int id = 123;
	        when(mockDocService.modifyContents(eq(id), anyString())).thenReturn(null);
	       
	        mockMvc.perform(delete("/deleteDocument")
	                .param("id", String.valueOf(id))
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isNotFound())
	                .andExpect(content().string("Document ID/Name is invalid"));
	    }
	    

	    @Test
	    public void testDeleteDocument_InvalidIdOrName() throws Exception {
	        
	        when(mockDocService.modifyContents(null, null)).thenReturn(null);
	        
	       mockMvc.perform(MockMvcRequestBuilders.delete("/deleteDocument")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isBadRequest())
	                .andExpect(content().string("Please provide the Document ID/Name"));

	    
	        verify(mockDocService, never()).modifyContents(any(), any());
	        verify(mockFileFunc, never()).DeleteFile(anyString());
	    }
	    
	    @Test
	    public void testDeleteDocument_FileDeletionFailed() throws Exception {
	        int id = 123;
	        
	        Document doc = new Document();
	        doc.setDocumentId(id);
	        doc.setDocumentName("Ananya123");
	        doc.setCreatedBy("Roul");
	       
	        when(mockDocService.modifyContents(any(), any())).thenReturn(new Document());
	        when(mockFileFunc.DeleteFile(anyString())).thenReturn(false);
	     
	        mockMvc.perform(delete("/deleteDocument")
	                .param("id", String.valueOf(id))
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isNotFound())
	                .andExpect(content().string("File could not be deleted"));
	    }
	    
	    
}

	

