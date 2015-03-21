package com.lbsp.promotion.coreplatform.editor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.PropertiesEditor;

import java.text.SimpleDateFormat;

/**
 * Created by HLJ on 2015/3/21.
 */
public class LongDataEditor extends PropertiesEditor {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Long data = null;
        if(StringUtils.isBlank(text)){
            data = null;
        }else{
            try {
                if(text.trim().indexOf("-") != -1){
                    if (text.trim().indexOf(" ") != -1 && text.trim().indexOf(":") != -1){
                        data = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(text).getTime();
                    }else{
                        data = new SimpleDateFormat("yyyy-MM-dd").parse(text).getTime();
                    }
                }else{
                    data = Long.parseLong(text);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        setValue(data);
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}
