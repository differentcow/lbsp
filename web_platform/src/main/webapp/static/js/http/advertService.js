/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
  return {
    service:All580.serverName+'/service/',
    deleteAdvert:function(ids){
      var url = this.service+'core/advert|del?ids='+ids;
      return $.ajax({
          url: url,
          type: "DELETE",
          contentType : 'application/json'
      });
    },
    getDetailById : function(id){
        var url = this.service + 'core/advert|'+id;
        return $.getJSON(url);
    }
  };
});