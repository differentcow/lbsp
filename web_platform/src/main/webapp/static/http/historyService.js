/**
 * Created by Barry.
 */
define([ 'jquery' ], function($) {
	return {
		service : All580.serverName + '/service/',
        getDataById : function(id){
            var url = this.service+'core/history|esl|'+id;
            return $.getJSON(url);
        }
	};
});