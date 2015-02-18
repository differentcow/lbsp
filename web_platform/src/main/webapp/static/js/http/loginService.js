/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
//  var $ = require('jquery');
  return {
    service:All580.serverName+'/service/',
    getValidationCode : function(){
      var url = this.service+'validateCode/generate';
      return $.getJSON(url);
    },
    login:function(payLoad){
      var url = this.service+'j_spring_security_check'
      return $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(payLoad),
        contentType : 'application/json'
      });
    }
  };
});