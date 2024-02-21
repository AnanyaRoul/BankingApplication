package com.example.demo;

public class FileInfo {

	private String fileName;
	private String url;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "FileInfo [fileName=" + fileName + ", url=" + url + "]";
	}
}
