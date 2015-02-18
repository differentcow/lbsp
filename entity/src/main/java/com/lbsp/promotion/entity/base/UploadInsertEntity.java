package com.lbsp.promotion.entity.base;

public class UploadInsertEntity extends BaseEntity {
	private String filePath;
	private String fileName;
    private long fileSize;
   
	
    
    
	public UploadInsertEntity(String table,long fileSize,String filePath,String fileName) {
		super(table);
		this.fileName=fileName;
		this.filePath=filePath;
		this.fileSize=fileSize;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
         
}
