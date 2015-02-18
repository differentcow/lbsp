/**
 * Created by Barry on 6/18/2014.
 */
define(['jquery','ajaxfileupload'],function($){
  return {
    service:All580.serverName+'/service/',
    upload : function(id,type,filename, code,img, successMethod,failedMethod){
      var me = this;
      this.url=this.service+'core/uploadPic?type='+type+'&filename='+filename+'&code='+code+'&id='+img
      $.ajaxFileUpload({
          url: me.url,
          secureuri:false,
          fileElementId:id,
          dataType: 'json',
          success: function(data,status){
            successMethod(data, status)
          },
          error: function(data, status, e){
            failedMethod(data, status, e)
          }
      });

  }

  };
});