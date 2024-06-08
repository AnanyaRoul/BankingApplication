package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class Controller {
	
	@Autowired
	private DocService docservice;
	
	@Autowired
	private FileFunction fileFunc;
	
	@Autowired
	private Document doc;
	
	@Autowired
	private UserInfoService service;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@PostMapping("/uploadDocument")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')") 
	public ResponseEntity<String> UploadDocument(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		
		if(file.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
		
		file.transferTo(new File(fileFunc.UploadFile(file.getOriginalFilename())));
		Document d=docservice.addDocument(doc,file.getOriginalFilename());
	
		return ResponseEntity.ok("Working...");
		
	}
	
	
	@GetMapping("/getAllDocuments")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	public ResponseEntity<List<FileInfo>> GetAllDocuments(){
		
		List<FileInfo> list=docservice.getAllDocuments();
		
		if (list.size()==0)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
		return ResponseEntity.of(Optional.of(list));
		
	}
	
	
	@DeleteMapping("/deleteDocument")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')") 
	public ResponseEntity<String> DeleteDocument(@RequestParam (name="id", required =false) Integer id,
	@RequestParam (name="name", required =false) String name){
		
		if(id==null && name==null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide the Document ID/Name");
		
		Document document=docservice.modifyContents(id,name);
		
		if(document==null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document ID/Name is invalid");
		
		boolean b=fileFunc.DeleteFile(document.getDocumentName());
		
		if(!b)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File could not be deleted");
		
		return ResponseEntity.ok("Deleted...");
	}
	
	
	@PostMapping("/register")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }
	
	

}
