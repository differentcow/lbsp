/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
//  var $ = require('jquery');
  return {
    service:All580.serverName+'/service/',
      getDetailById : function(id){
          var url = this.service + 'core/category|'+id;
          return $.getJSON(url);
      },
      operate : function(obj,type){
          var url = this.service+'core/category|'+type;
          if(type == 'add') {
              return $.ajax({
                  url: url,
                  type: "POST",
                  data: obj,
                  contentType: 'application/json'
              });
          }
          if(type == 'del') {
              return $.ajax({
                  url: url,
                  type: "POST",
                  data: obj,
                  contentType: 'application/json'
              });
          }
          if(type == 'upt') {
              return $.ajax({
                  url: url,
                  type: "PUT",
                  data: obj,
                  contentType: 'application/json'
              });
          }
      }
  };
});