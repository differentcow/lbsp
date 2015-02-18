/**
 * Created by Barry.
 */
define([ 'jquery' ], function($) {
	return {
		service : All580.serverName + '/service/',
		getParams : function(type) {
			var url = this.service + 'core/param|types';
            if(type != null && type != '')
                url = url + '?type='+type;
			return $.getJSON(url);
		},
        getParamByType : function(type){
            var url = this.service + 'core/param|paramType|'+type;
            return $.getJSON(url);
        },
        getParamById : function(id){
            var url = this.service + 'core/param|'+id;
            return $.getJSON(url);
        },
        modifyParam : function(obj,id){
            var url = this.service+'core/param|upt?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        addParam : function(obj){
            var url = this.service+'core/param|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: obj,
                contentType : 'application/json'
            });
        },
        delParam : function(obj){
            var url = this.service+'core/param|del?idAry='+obj;
            return $.ajax({
                url: url,
                type: "DELETE",
                contentType : 'application/json'
            });
        }
	};
});