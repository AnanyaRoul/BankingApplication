package com.example.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends CrudRepository<Document,Integer>{

	public List<Document> findByModifiedBy(String mfdBy);
	public List<Document> findByDocumentName(String name);
}
