/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','customerService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,customerService,util){
  var yes = parent.parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.parent.app.getI18nMessage('common.sys.label.no');
  var normal = parent.parent.app.getI18nMessage('common.sys.label.normal');
  var shop_type = parent.parent.app.getI18nMessage('common.select.type.shop');
  var customer_type = parent.parent.app.getI18nMessage('common.select.type.customer');
  var male_type = parent.parent.app.getI18nMessage('common.select.type.male');
  var female_type = parent.parent.app.getI18nMessage('common.select.type.female');
  var off_type = parent.parent.app.getI18nMessage('common.select.type.off');
  var on_type = parent.parent.app.getI18nMessage('common.select.type.on');
  var CustomerListViewModel = function() {
    var ep = this;
    this.sel_text = ko.observable('-Select-');
    this.name = ko.observable('');
    this.account = ko.observable('');
    this.type = ko.observable('');
    this.typeOptions = ko.observableArray([]);
    this.statusOptions = ko.observableArray([]);
    this.genderOptions = ko.observableArray([]);
    this.gender = ko.observable();
    this.status = ko.observable();
    this.params = new Array();
    this.delIds = ko.observable('');
    this.store = {};
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

    this.init = function () {
        ep.sel_text(parent.parent.app.getI18nMessage("common.sys.select.text"));
        ep.seti18nText();
        ep.loadStatusList();
    };
    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };
    this.adjusHeight = function(){
        util.adjustIframeHeight();
    };
    this.loadStatusList = function(){
        ep.statusOptions.push(new SelectModel(off_type,0));
        ep.statusOptions.push(new SelectModel(on_type,1));
    };

    this.datatable = ko.observable();
    this.store_el = ko.observable();
    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/customer|lst';
      settings['iDisplayLength'] = 10;
      settings['iListLength'] = 3;
      settings['fnClickTr'] = function(element,row,index){
          if(ep.store_el()){
              ep.store_el().removeClass('clickTr');
          }
          $(element).addClass('clickTr');
          ep.store_el($(element));
          window.returnVal = row;
      };
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "name", "value": ep.params['name']});
        aoData.push({ "name": "account", "value": ep.params['account']});
        aoData.push({ "name": "status", "value": ep.params['status']});
      };
      settings['fnDrawCallback'] = function(){
    	  util.adjustIframeHeight();
      };
      settings['aoColumns'] = [
        { 'mData': 'account','sDefaultContent': ''}, //账号
        { 'mData': 'name','sDefaultContent': ''}, //昵称
        { 	//性别
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if (obj.aData.gender == 1 ){
                    return '<span class="label label-info">'+male_type+'</span>';
                }else{
                    return '<span class="label label-info">'+female_type+'</span>';
                }
            }
        },
        { 	//类型
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.type == 'S'){
                    return '<span class="label label-info">'+shop_type+'</span>';
                }else{
                    return '<span class="label label-info">'+customer_type+'</span>';
                }
            }
        },
        { 	//状态
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.status == 1){
                    return '<span class="label label-success">'+on_type+'</span>';
                }else{
                    return '<span class="label label-danger">'+off_type+'</span>';
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
          ep.params['name']=ep.name();
          ep.params['account']=ep.account();
          ep.params['status']=ep.status();
          dataTable.fnDraw();
      });

    };
  };
  var epList = new CustomerListViewModel();
  epList.init();
  ko.applyBindings(epList);
  $(document).ready(function(){
	  epList.setUpTable();

  });
});