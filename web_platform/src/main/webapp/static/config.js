/**
 * Created by Barry on 9/25/2014.
 */

var All580 = {
	resBaseUrl : 'http://127.0.0.1:8086',
	serverName : '',
    webPath : window.location.protocol+window.location.host,
    contextPath :function() {
        var pathName = document.location.pathname;
        var index = pathName.substr(1).indexOf("/");
        var result = pathName.substr(0,index+1);
        return result;
    } ,
	getResUrl : function(url, defSize){
		if (url){
			return All580.resBaseUrl + url;
		}else if (defSize == 'lg'){
			return All580.serverName + '/static/images/pic_no_256.jpg';
		}else if (defSize == 'md'){
			return All580.serverName + '/static/images/pic_no_128.jpg';
		}else if (defSize == 'mw'){
			return All580.serverName + '/static/images/pic_no_128w.jpg';
		}else{
			return All580.serverName + '/static/images/pic_no_256.jpg';
		}
	}
};
requirejs.config({
  baseUrl: All580.serverName + '/static/',
  paths:{
    'jquery' : 'shared/js/jquery-1.11.1.min',
    'jquery-ui':'shared/js/jquery-ui-1.9.2.custom.min',
    'jquery-nicescroll':'shared/adex/js/jquery.nicescroll',   //滚动条
    'scripts':'shared/adex/js/scripts',
    'jquery.multi-select':'shared/js/jquery-multi-select/js/jquery.multi-select', //多选
    'jquery.quicksearch':'shared/js/jquery-multi-select/js/jquery.quicksearch',//快速搜索
    'jquery-validate': 'shared/js/jquery.validate.min',
    'additional-methods' : 'shared/js/additional-methods.min',//jquery validate addons
    'jquery-cookie': 'shared/js/jquery.cookie',
    'jquery-datatable':'shared/js/advanced-datatable/js/jquery.dataTables',
    'DT-bootstrap':"shared/js/data-tables/DT_bootstrap",     //datatable要用
    'bootstrap': 'shared/js/bootstrap.min',
    'bootstrap-fileupload':'shared/js/bootstrap-fileupload.min',//文件上传组件
    'bootstrap-datepicker':'shared/js/bootstrap-datepicker/js/bootstrap-datepicker',
    'daterangepicker':'shared/js/bootstrap-daterangepicker/daterangepicker',
    'moment.min':'shared/js/bootstrap-daterangepicker/moment.min',
    'bootstrap-wysihtml5':'shared/js/bootstrap-wysihtml5/bootstrap-wysihtml5', //editor
    'wysihtml5':'shared/js/bootstrap-wysihtml5/wysihtml5-0.3.0',
    'ckeditor':'shared/js/ckeditor/ckeditor',
    'jquery-ckeditor':'shared/js/ckeditor/adapters/jquery',
    'jquery-tagsinput':'shared/js/jquery-tags-input/jquery.tagsinput',
    'ajaxfileupload' :'shared/js/ajaxfileupload',//jquery 上传文件
    'knockout' : 'shared/js/knockout-3.1.0',
    'knockout-mapping': 'shared/js/knockout.mapping-latest',
    'knockout-amd-helpers': 'shared/js/knockout-amd-helpers.min',//template用
    'amplify': 'shared/js/amplify.core',  //http://amplifyjs.com/api/pubsub/
    'gritter':'shared/js/gritter/js/jquery.gritter',
    'text':'shared/js/text',
    'dhtmlxtree':'shared/js/dhtmlxtree',
    'DB_gallery':'shared/js/jquery.DB_gallery',
    'moment':'shared/js/moment/moment.min.2.9.0',
    'moment_locale':'shared/js/moment/lang/locale',
    'fullcalendar':'shared/js/fullcalendar/fullcalendar',
    'fullcalendar_lang':'shared/js/fullcalendar/lang-all',
    'morris':'shared/js/morris-chart/morris.min',
    'raphael':'shared/js/morris-chart/raphael-min',
    'jquery-i18n':'shared/js/jquery.i18n.properties',

    //配置http service
    'loginService' :'js/http/loginService',
    'userService' :'js/http/userService',
    'paramsService' :'js/http/paramsService',
    'taskService' :'js/http/taskService',
    'interfaceService' :'js/http/interfaceService',
    'userinfoService':'js/http/userinfoService',
    'fileUploadService':'js/http/fileUploadService',
    'commentService':'js/http/commentService',
    //validate Msg
    'validateMsg': 'js/util/message_cn',//jquery validator 中文message
    //配置Util
    'dialog':'js/util/dialog',
    'commonUtil' :'js/util/commonUtil', //通用工具
    'map':'js/util/map',
    'iosSwitch':'shared/js/ios-switch/switchery'
  },
  shim:{
    'jquery-validate': ['jquery'],
    'jquery-i18n': ['jquery'],
    'additional-methods':['jquery','jquery-validate'],
    'amplify': {
      deps: ['jquery']
      , exports: 'amplify'
    },
    'gritter':['jquery'],
    'jquery-datatable':['jquery'],
    'DT-bootstrap':['jquery','jquery-datatable'],
    'ajaxfileupload': ['jquery'],
    'bootstrap': ['jquery'],
    'bootstrap-fileupload': ['jquery'],
  	'jquery-nicescroll':['jquery'],
  	'scripts':['jquery','jquery-ui','bootstrap'],//'jquery-nicescroll'
  	'jquery-ui':['jquery'],
    'bootstrap-datepicker': ['jquery'],
    'validateMsg':['jquery','jquery-validate'],
    'jquery.multi-select':['jquery'],
    'jquery.quicksearch':['jquery'],
    'bootstrap-wysihtml5':['jquery','wysihtml5'],
    'jquery-tagsinput':['jquery'],
    'jquery-ckeditor':['jquery','ckeditor'],
    'iosSwitch':['jquery'],
    'morris':['jquery','raphael']
  }
});
//datatable配置
All580.dataTable = {
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
//日期工具类
All580.DPGlobal={
    modes: [
      {
        clsName: 'days',
        navFnc: 'Month',
        navStep: 1
      },
      {
        clsName: 'months',
        navFnc: 'FullYear',
        navStep: 1
      },
      {
        clsName: 'years',
        navFnc: 'FullYear',
        navStep: 10
      }],
    dates:{
      days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
      daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
      daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
      months: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
      monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
    },
    isLeapYear: function (year) {
      return (((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0))
    },
    getDaysInMonth: function (year, month) {
      return [31, (All580.DPGlobal.isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month]
    },
    parseFormat: function(format){
      var separator = format.match(/[.\/\-\s].*?/),
        parts = format.split(/\W+/);
      if (!separator || !parts || parts.length === 0){
        throw new Error("Invalid date format.");
      }
      return {separator: separator, parts: parts};
    },
    parseDate: function(date, format) {
      if(typeof format === 'string'){
        format = this.parseFormat(format);
      }
      var parts = date.split(format.separator),
        date = new Date(),
        val;
      date.setHours(0);
      date.setMinutes(0);
      date.setSeconds(0);
      date.setMilliseconds(0);
      if (parts.length === format.parts.length) {
        for (var i=0, cnt = format.parts.length; i < cnt; i++) {
          val = parseInt(parts[i], 10)||1;
          switch(format.parts[i]) {
            case 'dd':
            case 'd':
              date.setDate(val);
              break;
            case 'mm':
            case 'm':
              date.setMonth(val - 1);
              break;
            case 'yy':
              date.setFullYear(2000 + val);
              break;
            case 'yyyy':
              date.setFullYear(val);
              break;
          }
        }
      }
      return date;
    },
    formatDate: function(date, format){
      if(typeof format === 'string'){
        if (!format || format == '') {
          format = 'yyyy-MM-dd hh:mm:ss';
        }
        format = this.parseFormat(format);
      }
      if (typeof date === 'string' || typeof date === 'number') {
        date = new Date(date);
      }

      var val = {
        d: date.getDate(),
        m: date.getMonth() + 1,
        yy: date.getFullYear().toString().substring(2),
        yyyy: date.getFullYear()
      };
      val.dd = (val.d < 10 ? '0' : '') + val.d;
      val.mm = (val.m < 10 ? '0' : '') + val.m;
      var date = [];
      for (var i=0, cnt = format.parts.length; i < cnt; i++) {
        date.push(val[format.parts[i]]);
      }
      return date.join(format.separator);
    },
   formatDateTime:function(date, format){
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
  },
  checkDateTime:function(value){
	  var pattern= /^\d{4}-\d{1,2}-\d{1,2}$/;
	  if(pattern.exec(value)){
    	  return true;
      }
	  return false;
  }
 };
