/**
 * Created by Barry on 6/18/2014.
 */
define(['jquery','ajaxfileupload'],function($){
  return {
    service:All580.serverName+'/service/',
    upload : function(id,forward,data,successMethod,failedMethod){
      var me = this;
      this.url=this.service+'core/upload?forward='+forward;
      for(var k in data){
          this.url += '&'+k+'='+data[k];
      }
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