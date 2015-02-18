/**
 * Created by Barry.
 */
define([ 'jquery' ], function($) {
	return {
		service : All580.serverName + '/service/',
        //--------------------接口注册Service----------------------
        getRegistInterById : function(id){
            var url = this.service + 'core/interRegist|'+id;
            return $.getJSON(url);
        },
        modifyRegistInter : function(obj,id){
            var url = this.service+'core/interRegist|upt?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        addRegistInter : function(obj){
            var url = this.service+'core/interRegist|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: obj,
                contentType : 'application/json'
            });
        },
        delRegistInter : function(obj){
            var url = this.service+'core/interRegist|del?idAry='+obj;
            return $.ajax({
                url: url,
                type: "DELETE",
                contentType : 'application/json'
            });
        },

        //-----------------外接应用注册Service-------------------------------
        getAppInterById : function(id){
            var url = this.service + 'core/interApp|'+id;
            return $.getJSON(url);
        },
        getAll:function(){
            var url = this.service + 'core/interApp|all';
            return $.getJSON(url);
        },
        modifyAppInter : function(obj,id){
            var url = this.service+'core/interApp|upt?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        addAppInter : function(obj){
            var url = this.service+'core/interApp|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: obj,
                contentType : 'application/json'
            });
        },
        delAppInter : function(obj){
            var url = this.service+'core/interApp|del?idAry='+obj;
            return $.ajax({
                url: url,
                type: "DELETE",
                contentType : 'application/json'
            });
        },

        //-----------------接口开放Service-------------------------------
        getLinkInterById : function(id){
            var url = this.service + 'core/interLink|'+id;
            return $.getJSON(url);
        },
        delLinkInter : function(obj){
            var url = this.service+'core/interLink|del?idAry='+obj;
            return $.ajax({
                url: url,
                type: "DELETE",
                contentType : 'application/json'
            });
        },
        allInterByAppId : function(id,mark){
            var url = this.service + 'core/interLink|all?appId='+id+'&mark='+mark;
            return $.getJSON(url);
        },
        modifyLinkInter : function(obj){
            var url = this.service+'core/interLink|upt';
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        addLinkInter : function(obj){
            var url = this.service+'core/interLink|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: obj,
                contentType : 'application/json'
            });
        }
	};
});