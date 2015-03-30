/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','commentService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,commentService,util){
  var yes = parent.parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.parent.app.getI18nMessage('common.sys.label.no');
  var normal = parent.parent.app.getI18nMessage('common.sys.label.normal');
  var delete_status = parent.parent.app.getI18nMessage('common.sys.label.delStatus');
  var anonymous = parent.parent.app.getI18nMessage('common.select.anonymous');
  var registCustomer = parent.parent.app.getI18nMessage('common.select.customer');
  var CommentListViewModel = function() {
    var ep = this;
    this.sel_text = ko.observable('-Select-');
    this.viewTitle = ko.observable();
    this.name = ko.observable('');
    this.title = ko.observable('');
    this.type = ko.observable('');
    this.typeOptions = ko.observableArray([]);
    this.start_dateStr = ko.observable('');
    this.end_dateStr = ko.observable('');
    this.params = new Array();
    this.delIds = ko.observable('');
    this.isUpdate = ko.observable(false);
    this.seti18nText = function(){
        $('title,label,span,div,th').each(function(){
            var attr = $(this).attr('i18n');
            if(attr !=null && attr != ''){
                $(this).text(parent.parent.app.getI18nMessage(attr));
            }
        });
        $('h4,button').each(function(){
            var attr = $(this).attr('i18n');
            if(attr !=null && attr != ''){
                $(this).html(parent.parent.app.getI18nMessage(attr));
            }
        });
    };
    this.id = ko.observable();
    this.getIDFromUrl = function(){
        var id = util.getQueryString('id');
        if(id != null){
            ep.id(id);
        }
        var type = util.getQueryString('type');
        if(type != null && type == 'customer'){
            ep.viewTitle(true);
        }else if(type != null && type == 'preferential'){
            ep.viewTitle(false);
        }
        ep.loadTypeList();
    };
    this.init = function () {
        ep.sel_text(parent.parent.app.getI18nMessage("common.sys.select.text"));
        ep.seti18nText();
//        ep.initAuth();
        ep.getIDFromUrl();
//        ep.loadTypeList();
        ep.datePickerInit();
    };
    //初始化Date Picker
    this.datePickerInit = function(){
        //start
        var checkin = $('.dpd1').datepicker().on('changeDate', function (ev) {
            var date = new Date(ev.date);
            var date_str =  All580.DPGlobal.formatDateTime(date, 'yyyy-MM-dd');
            ep.start_dateStr(date_str);
            if (ev.date.valueOf() > checkout.date.valueOf()) {
                date.setDate(date.getDate() + 1);
                checkout.setValue(date);
            }
            checkin.hide();
            $('.dpd2')[0].focus();
        }).data('datepicker');
        //end
        var checkout = $('.dpd2').datepicker().on('changeDate', function (ev) {
            var date = new Date(ev.date);
            var date_str =  All580.DPGlobal.formatDateTime(date, 'yyyy-MM-dd');
            ep.end_dateStr(date_str);
            checkout.hide();
        }).data('datepicker');
    };
    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };
    this.loadTypeList = function(){
        ep.typeOptions.push(new SelectModel(registCustomer,1));
        if(!ep.viewTitle())
            ep.typeOptions.push(new SelectModel(anonymous,0));
    };

    this.datatable = ko.observable();

    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/comment|common';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "param", "value": ep.viewTitle()?ep.params['title']:ep.params['name']});
        aoData.push({ "name": "call", "value": ep.viewTitle()?'c':'p'});
        aoData.push({ "name": "callId", "value": ep.id()});
        aoData.push({ "name": "type", "value": ep.params['type']});
        aoData.push({ "name": "from", "value": ep.params['from']});
        aoData.push({ "name": "to", "value": ep.params['to']});
      };
      settings['fnDrawCallback'] = function(){
    	  util.adjustIframeHeight();
      };
      settings['aoColumns'] = [
        { 'mData': 'preferentialTitle','sDefaultContent': ''}, //优惠标题
        { 	//状态
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if (obj.aData.status == 0 ){
                    return '<span class="label label-danger">'+delete_status+'</span>';
                }else{
                    return '<span class="label label-success">'+normal+'</span>';
                }
            }
        },
        { 	//匿名
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(typeof obj.aData.anonymous_user == 'undefined'){
                    return '<span class="label label-info">'+no+'</span>';
                }else{
                    return '<span class="label label-info">'+yes+'</span>';
                }
            }
        },
        { 'mData': 'content', 'sDefaultContent': ''}, //评论内容
        { 	//评论人
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(typeof obj.aData.anonymous_user == 'undefined'){
                    return obj.aData.commentor;
                }else{
                    return obj.aData.anonymous_user;
                }
            }
        },
		{ 	//创建时间
        	'sDefaultContent': '',
        	'fnRender': function (obj) {
        		return All580.DPGlobal.formatDateTime(obj.aData.create_time, 'yyyy-MM-dd HH:mm:ss');
        	}
		}
      ];
      var dataTable = $('#dynamic-table').dataTable(settings);
        ep.datatable(dataTable);
      $('#search').click(function() {
          ep.params['param']=ep.viewTitle()?ep.title():ep.name();
          ep.params['type']=ep.type();
          ep.params['from']=ep.start_dateStr();
          ep.params['to']=ep.end_dateStr();
          dataTable.fnDraw();
      });
    };
  };
  var epList = new CommentListViewModel();
  epList.init();
  ko.applyBindings(epList);

  $(document).ready(function(){
	  epList.setUpTable();
  });
});

