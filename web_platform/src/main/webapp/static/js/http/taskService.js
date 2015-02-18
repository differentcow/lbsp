/**
 * Created by Barry.
 */
define([ 'jquery' ], function($) {
	return {
		service : All580.serverName + '/service/',
        getTaskQueueById : function(id){
            var url = this.service + 'core/task|'+id;
            return $.getJSON(url);
        },
        getTasks : function(id){
            var url = this.service + 'core/task|tasks';
            return $.getJSON(url);
        },
        run : function(id){
            var url = this.service + 'core/task|status|run?id='+id;
            return $.getJSON(url);
        },
        modify : function(obj,id){
            var url = this.service+'core/task|upt?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: obj,
                contentType : 'application/json'
            });
        },
        pause : function(id){
            var url = this.service+'core/task|status|pause?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: null,
                contentType : 'application/json'
            });
        },
        restore : function(id){
            var url = this.service+'core/task|status|restore?id='+id;
            return $.ajax({
                url: url,
                type: "PUT",
                data: null,
                contentType : 'application/json'
            });
        },
        add : function(obj){
            var url = this.service+'core/task|add';
            return $.ajax({
                url: url,
                type: "POST",
                data: obj,
                contentType : 'application/json'
            });
        },
        del : function(id){
            var url = this.service+'core/task|del?id='+id;
            return $.ajax({
                url: url,
                type: "DELETE",
                contentType : 'application/json'
            });
        }
	};
});