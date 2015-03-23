/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
  return {
    service:All580.serverName+'/service/',
    deleteCollection:function(ids){
      var url = this.service+'core/collection|del?ids='+ids;
      return $.ajax({
          url: url,
          type: "DELETE",
          contentType : 'application/json'
      });
    }
  };
});