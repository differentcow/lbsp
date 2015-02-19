/**
 * Created by Summer.
 */
define([ 'jquery' ], function($) {
	return {
		service : All580.serverName + '/service/',
        checkPassword:function(obj){
            var url = this.service+'core/userInfo|old';
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        isAuthing:function(){
            var url = this.service + 'core/userInfo|isAuthing';
            return $.getJSON(url);
        },
        updatePassword:function(obj){
            var url = this.service+'core/userInfo|new';
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        repeatAuth:function (){
            var url = this.service + 'core/userInfo|repeatAuth';
            return $.getJSON(url);
        },
        me : function(id){
            var url = this.service + 'core/userInfo|me';
            return $.getJSON(url);
        },
        getUserById : function(id){
            var url = this.service + 'core/userInfo|'+id;
            return $.getJSON(url);
        },
        getUserSizeByAccountOrName : function(tag,value,id){
            var url = this.service + 'core/userInfo|ifRepeat?tag='+tag+'&value='+value+'&id='+id;
            //return $.getJSON(url);
            return $.ajax({
                url: url,
                type: "GET",
                async: false//同步
            });
        },
        modifyUser : function(obj,id){
            var url = this.service+'core/userInfo|upt?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        modifyMe : function(obj){
            var url = this.service+'core/userInfo|upt|me';
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        addUser : function(obj){
            var url = this.service+'core/userInfo|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: obj,
                contentType : 'application/json'
            });
        },
        delUser : function(obj){
            var url = this.service+'core/userInfo|del?idAry='+obj;
            return $.ajax({
                url: url,
                type: "DELETE",
                contentType : 'application/json'
            });
        },
        getRolesByUserId:function(userid){
        	var url=this.service+'core/userInfo|findRolesOfUser?id='+userid;
        	return $.getJSON(url);
        },
        getRemainByUserId:function(userid){
        	var url=this.service+'core/userInfo|findRolesOfRemain?id='+userid;
        	return $.getJSON(url);
        },
        getOperByRoleId:function(roleid){
        	var url=this.service+'core/userInfo|findOperatesByRoleId?id='+roleid;
        	return $.getJSON(url);
        },
        addUserRole:function(userid,roleids){
        	var url=this.service+'core/userInfo|addUserRole?userid='+userid+'&roleids='+roleids;
        	return $.getJSON(url);
        },
        delUserRole:function(userid,roleids){
        	var url=this.service+'core/userInfo|delUserRole?userid='+userid+'&roleids='+roleids;
        	return $.getJSON(url);
        },
        
        editRole:function(id){
        	var url=this.service+'core/userInfo|editRole?id='+id;
        	return $.getJSON(url);
        },
        savaEditRole:function(obj){
        	var url=this.service+'core/userInfo|saveEditRole?idAry='+obj;
        	return $.ajax({
                url: url,
                type: "POST",
                contentType : 'application/json'
            });
        }
	};
});