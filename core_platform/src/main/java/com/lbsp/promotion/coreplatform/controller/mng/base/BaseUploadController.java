package com.lbsp.promotion.coreplatform.controller.mng.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class BaseUploadController extends BaseController {

    protected String upload(MultipartFile file,String resourceRootPath,String resourceRootDir)
            throws Exception {
        return upload(file,resourceRootPath,resourceRootDir,null);
    }

    protected String upload(MultipartFile file,String resourceRootPath,String resourceRootDir,String resourceSrcPath)
			throws Exception {
		// 获取老名称
		String orgName = file.getOriginalFilename();
		String fileExt = "";
		int index = orgName.lastIndexOf(".");
		if (index > -1)
			fileExt = orgName.substring(index, orgName.length());

		// 组成新路径及名称
		String filePath = resourceRootDir
				+ "/"
				+ (new java.text.SimpleDateFormat("yyyy/MM/dd")).format(new Date())
				+ "/";
		String fileName = filePath + orgName.substring(0,index)+"_"+System.currentTimeMillis()+fileExt;
		
        // 保存文件(因为maven目录结构问题，src也需要复制一份)
        if (StringUtils.isNotBlank(resourceSrcPath)){
            File dir = new File(resourceSrcPath+filePath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            saveFileFromInputStream(file.getInputStream(),resourceSrcPath+fileName);
//            saveFile(file, resourceSrcPath+filePath, resourceSrcPath+fileName);
        }
        // 保存文件(target)
        saveFile(file, resourceRootPath+filePath, resourceRootPath+fileName);
//		return this.createBaseResult("上传成功", fileName);
        return fileName;
	}

    public void saveFileFromInputStream(InputStream stream,String filename) throws IOException{
        FileOutputStream fs = new FileOutputStream(filename);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1){
            bytesum+=byteread;
            fs.write(buffer,0,byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }
	
	private void saveFile(MultipartFile multipartFile, String filePath,
			String fileName) throws Exception {
		File file = new File(fileName);
		File dir = new File(filePath);
		if (!dir.exists())
			dir.mkdirs();
		multipartFile.transferTo(file);
	}



	/*
	private void deleteFile(String filePath) throws Exception {
		File file = new File(resourceRootPath + filePath);
		if (!file.exists())
			file.delete();
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.DELETE)
	public @ResponseBody
	Object delete(
			@RequestParam("filePath") List<String> filePaths,
			HttpServletRequest request) throws Exception {
		int result = uploadService.delete(filePaths,);
		if (result == 0) {
			return this.createBaseResult("更新失败", true);
		} else {
			return this.createBaseResult("更新成功", false);
		}
	}
	*/
}
