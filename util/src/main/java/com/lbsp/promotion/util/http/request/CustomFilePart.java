/**
 * 
 */
package com.lbsp.promotion.util.http.request;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.util.EncodingUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Zale
 *
 */
public class CustomFilePart extends FilePart{
	        
	/**
	 * @param name
	 * @param partSource
	 * @param contentType
	 * @param charset
	 */
	public CustomFilePart(String name, PartSource partSource,
			String contentType, String charset) {
		super(name, partSource, contentType, charset);
	}

	@Override
	protected void sendDispositionHeader(OutputStream out) throws IOException {

        super.sendDispositionHeader(out);
        String filename = getSource().getFileName();
        if (filename != null) {
            out.write(EncodingUtil.getBytes(FILE_NAME,getCharSet()));
            out.write(QUOTE_BYTES);
            out.write(EncodingUtil.getBytes(filename,getCharSet()));
            out.write(QUOTE_BYTES);
        }
    
	}
	
	

}
