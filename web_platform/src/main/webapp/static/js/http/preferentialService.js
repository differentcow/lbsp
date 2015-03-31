/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
  return {
    service:All580.serverName+'/service/',
    deletePreferential:function(ids){
      var url = this.service+'core/preferential|del?ids='+ids;
      return $.ajax({
          url: url,
          type: "DELETE",
          contentType : 'application/json'
      });
    },
    getDetailById : function(id){
        var url = this.service + 'core/preferential|'+id;
        return $.getJSON(url);
    }
  };
});