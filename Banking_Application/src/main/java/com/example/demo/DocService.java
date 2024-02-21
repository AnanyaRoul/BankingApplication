package com.example.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class DocService {

	@Autowired
	private DocRepository docRepository;
	@Value("${service.modifiedBy:Ananya-Deleted}")
	private String mdfBy;
	
	private LocalDate mdfDate=LocalDate.now();
	@Value("${doc.modifiedBy:NA}")
	private String mdfByDefault;
	@Value("${file.folder:/PDF/}")
	private String folderName;
	
	public Document addDocument(Document d,String fileName) {
		
		d.setDocumentName(fileName);
		Document doc=docRepository.save(d);
		
		return doc;
	}
	
	public List<FileInfo> getAllDocuments(){
	
		List<Document> list=docRepository.findByModifiedBy(mdfByDefault);
		
		List<String> l=list.stream().map(doc -> doc.getDocumentName()).toList();
		
		List<FileInfo> listFile=l.stream().map(docName ->{
			
			FileInfo f=new FileInfo();
			f.setFileName(docName);
			f.setUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path(folderName).path(docName).toUriString());
		
			return f;
		}).toList();
		
		return listFile;
	}
	
	public Document modifyContents(Integer id,String name) {
		
		Document doc=new Document();
		
		if(id!=null)
		     doc=docRepository.findById(id).orElse(null);
		
		else {
			Optional<Document> optionalDocument = docRepository.findByDocumentName(name).stream().findFirst();
			doc = optionalDocument.orElse(null);
			}
		
		
		if(doc!=null && doc.getModifiedBy().equalsIgnoreCase(mdfByDefault)){
			
			doc.setModificationDate(mdfDate);
		    doc.setModifiedBy(mdfBy);
		    
		    return docRepository.save(doc);
		}
		
		return doc;
		
	}

}
