/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
//  var $ = require('jquery');
  return {
    service:All580.serverName+'/service/',
    deleteComment:function(ids){
      var url = this.service+'core/comment|del?ids='+ids;
      return $.ajax({
          url: url,
          type: "DELETE",
          contentType : 'application/json'
      });
    },
    updateStatus:function(data){
        var url = this.service+'core/comment|upt';
        return $.ajax({
            url: url,
            type: "PUT",
            data:data,
            contentType : 'application/json'
        });
    }
  };
});