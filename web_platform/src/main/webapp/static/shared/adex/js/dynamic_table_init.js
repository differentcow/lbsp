function dateFormat(date, format){
	if (!format || format == '')
		format = 'yyyy-MM-dd hh:mm:ss';
	if (typeof date === 'string' || typeof date === 'number')
		date = new Date(date);
	var year = date.getFullYear()
	var month = date.getMonth()+1;
	var days = date.getDate();
	var hours = date.getHours();
	var minutes = date.getMinutes();
	var seconds = date.getSeconds();
	var res = format.match(/([yY]+)/);
	if (res) format = format.replace(res[0], year.toString().substr(res[0].length - 4,4-(res[0].length - 4)));
	res = format.match(/(M+)/);
	if (res) format = format.replace(res[0], (res[0].length == 2 && month < 10 ? '0'+month : month));
	res = format.match(/([dD]+)/);
	if (res) format = format.replace(res[0], (res[0].length == 2 && days < 10 ? '0'+days : days));
	res = format.match(/([hH]+)/);
	if (res) format = format.replace(res[0], (res[0].length == 2 && hours < 10 ? '0'+hours : hours));
	res = format.match(/([m]+)/);
	if (res) format = format.replace(res[0], (res[0].length == 2 && minutes < 10 ? '0'+minutes : minutes));
	res = format.match(/([Ss]+)/);
	if (res) format = format.replace(res[0], (res[0].length == 2 && seconds < 10 ? '0'+seconds : seconds));
	return format;
}

$(document).ready(function() {
	//var settings = Global.dataTable;
	var settings = {
    	'bFilter': false, 					//是否使用内置的过滤功能
    	'bLengthChange': false, 	//是否允许自定义每页显示条数.
    	'iDisplayLength': 20, 			//最大显示多少条记录
    	'bProcessing': true, 			//当datatable获取数据时候是否显示正在处理提示信息。
    	'bSort': false,					//是否显示排序
    	'bServerSide': true,
    	'sAjaxDataProp': 'result',
    	'sServerMethod': 'GET',
    	'oLanguage': {
    	    'sProcessing':   	'处理中...',
    	    'sLengthMenu': '_MENU_ 记录/页',
    	    'sZeroRecords': '没有匹配的记录',
    	    'sInfo':         		'显示第 _START_ 至 _END_ 项记录，共 _TOTAL_ 项',
    	    'sInfoEmpty':    '显示第 0 至 0 项记录，共 0 项',
    	    'sInfoFiltered': 	'(由 _MAX_ 项记录过滤)',
    	    'sInfoPostFix':  	'',
    	    'sSearch':       	'过滤:',
    	    'sUrl':          		'',
    	    'oPaginate': {
    	        'sFirst':    		'首页',
    	        'sPrevious': 	'上页',
    	        'sNext':     	'下页',
    	        'sLast':     		'末页'
    	    }
    	}
	};
	settings['sAjaxSource'] = '/sale-platform/service/core/user|lst';
	settings['fnServerParams'] = function (aoData) {
		var params = $('#search').attr('params');
		if (params){
			eval('params='+params);
		    aoData.push({ "name": "mobile", "value": params.mobile});
		    aoData.push({ "name": "nick_name", "value": params.nick_name});
		    aoData.push({ "name": "real_name", "value": params.real_name});
		}
	}
	settings['aoColumns'] = [
		{ 'mData': 'nick_name'}, //昵称
		{ 'mData': 'real_name','sDefaultContent': ''}, //姓名
		{ //手机号
			'mData': 'mobile',
			'fnRender': function (obj) {
				return '<a href="view?id=' + obj.aData.id + '">' + obj.aData.mobile + '</a>';
			}
		},
		{ 'mData': 'email'}, //email
		{ 'mData': 'user_type_name','sDefaultContent': ''}, //用户类型
		{ //状态
			'mData': 'status',
			'fnRender': function (obj) {
				switch (obj.aData.status){
					case 1:
						return '<span class="label label-info">' + obj.aData.status_name + '</span>';
					default:
						return '<span class="label label-default">' + obj.aData.status_name + '</span>';
				}
			}
		},
		{ //最后登录时间
			'mData': 'login_time',
			'fnRender': function (obj) {
				return dateFormat(obj.aData.login_time,'yy-MM-dd hh:mm');
			}
		}];
    var dataTable = $('#dynamic-table').dataTable(settings);
    
    $('#search').click(function(){
    	$(this).attr('params',JSON.stringify({
    			'mobile' : $('#mobile').val(),
    			'nick_name' : $('#nick_name').val(),
    			'real_name' : $('#real_name').val()
    	}));
    	dataTable.fnDraw();
    });
});