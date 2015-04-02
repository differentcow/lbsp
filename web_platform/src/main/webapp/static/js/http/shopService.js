/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
//  var $ = require('jquery');
  return {
    service:All580.serverName+'/service/',
    deleteShop:function(ids){
      var url = this.service+'core/shop|del?ids='+ids;
      return $.ajax({
          url: url,
          type: "DELETE",
          contentType : 'application/json'
      });
    },
      getDetailById : function(id){
          var url = this.service + 'core/shop|'+id;
          return $.getJSON(url);
      },
      modifyShop : function(obj,id){
          var url = this.service+'core/shop|upt';
          return $.ajax({
              url: url,
              type: "PUT",
              data: obj,
              contentType : 'application/json'
          });
      },
      addShop : function(obj){
          var url = this.service+'core/shop|add';
          return $.ajax({
              url: url,
              type: "POST",
              data: obj,
              contentType : 'application/json'
          });
      }
  };
});