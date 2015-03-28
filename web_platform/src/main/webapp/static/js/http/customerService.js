/**
 * Created by Barry on 6/12/2014.
 */
define(['jquery'],function($){
  return {
    service:All580.serverName+'/service/',
    deleteCustomer:function(ids){
      var url = this.service+'core/customer|del?ids='+ids;
      return $.ajax({
          url: url,
          type: "DELETE",
          contentType : 'application/json'
      });
    },
    addCustomer : function(obj){
        var url = this.service+'core/customer|add';
        return $.ajax({
            url: url,
            type: "POST",
            data: JSON.stringify(obj),
            contentType : 'application/json'
        });
    },
    updateCustomer : function(obj){
        var url = this.service+'core/customer|upt';
        return $.ajax({
            url: url,
            type: "PUT",
            data: JSON.stringify(obj),
            contentType : 'application/json'
        });
    },
    resetCustomerPwd : function(obj){
        var url = this.service+'core/customer|reset';
        return $.ajax({
            url: url,
            type: "PUT",
            data: JSON.stringify(obj),
            contentType : 'application/json'
        });
    }
  };
});