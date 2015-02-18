/**
 * Created by Zale on 6/15/2014.
 */
define(['jquery'],function($){
    return {
        service:All580.serverName+'/service/',
        getLoginUserInfo:function(){
            var url = this.service+'user/info';
            return $.getJSON(url);
        },
        changeLocale:function(locale){
            var url = this.service+'locale/'+locale;
            return $.getJSON(url);
        },
        getLocale:function(locale){
            var url = this.service+'locale/one';
            return $.getJSON(url);
        },
        getUserProfile:function(id){
          var url = this.service+'core/user|profile|view';
          return $.getJSON(url);
        },
        updateUserProfile:function(payload){
        var url = this.service+'core/user|profile|upt'
        return $.ajax({
          url: url,
          type: "PUT",
          data: payload,
          contentType : 'application/json'
        });
      },
        getUser:function(id){
            var url = this.service+'core/user|view';
            if (id) url += '?id=' + id;
            return $.getJSON(url);
        },

        updateUser:function(payload){
            var url = this.service+'core/user|upt';
            return $.ajax({
                url: url,
                type: "PUT",
                data: payload,
                contentType : 'application/json'
            });
        },
        getRoleList:function(userId){
        	var url = this.service+'core/role|assignable';
        	if (userId) url += '?uid=' + userId;
        	return $.getJSON(url);
        },
        addUser:function(user){
        	var url = this.service+'core/user|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: user,
                contentType : 'application/json'
            });
        },
        rstpwd:function(userId){
        	var url = this.service+'core/user|rstpwd';
            if (userId) url += '?userId=' + userId;
            return $.getJSON(url);
        },
        getListByEpId:function(payload){
          var url = this.service+'core/user|lst-ep';
          return $.getJSON(url,payload);
        },
        getWorkScheduleInfo:function(payload){
        	var url = this.service+'core/work|schema';
            return $.getJSON(url,payload);
        }
    };
});