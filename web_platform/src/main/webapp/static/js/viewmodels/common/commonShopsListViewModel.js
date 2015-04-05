/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','shopService','commonUtil','amplify','DT-bootstrap','bootstrap'],
		function($,ko,shopService,util){
  var yes = parent.parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.parent.app.getI18nMessage('common.sys.label.no');
  var normal = parent.parent.app.getI18nMessage('common.sys.label.normal');
  var anonymous = parent.parent.app.getI18nMessage('common.select.anonymous');
  var deny = parent.parent.app.getI18nMessage('shop.select.status.deny');
  var open = parent.parent.app.getI18nMessage('shop.select.status.open');
  var wait = parent.parent.app.getI18nMessage('shop.select.status.wait');
  var longitude = parent.parent.app.getI18nMessage('shop.longitude');
  var latitude = parent.parent.app.getI18nMessage('shop.latitude');
  var areaCode = parent.parent.app.getI18nMessage('shop.areaCode');
  var customerName = parent.parent.app.getI18nMessage('shop.customerName');
  var sellPic = parent.parent.app.getI18nMessage('shop.sellPic');
  var desc = parent.parent.app.getI18nMessage('shop.desc');
  var ShopListViewModel = function() {
    var ep = this;
    this.user = ko.observable();
    this.sel_text = ko.observable('-Select-');
    this.name = ko.observable('');
    this.sell = ko.observable('');
    this.status = ko.observable();
    this.sell = ko.observable();
    this.address = ko.observable();
    this.statusOptions = ko.observableArray([]);
    this.start_dateStr = ko.observable('');
    this.end_dateStr = ko.observable('');
    this.params = new Array();
    this.paramAdd = 'addshop';
    this.paramDel = 'delshop';
    this.delIds = ko.observable('');
    this.paramModify = 'modifyshop';
    this.viewDel = ko.observable(false);
    this.viewAdd = ko.observable(false);
    this.canAdd = false;
    this.canDel = ko.observable('');
    this.canModify = false;
    this.viewModify = ko.observable(false);
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
    this.init = function () {
        ep.sel_text(parent.parent.app.getI18nMessage("common.sys.select.text"));
        ep.seti18nText();
        ep.loadStatusList();
    };

    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };
    this.loadStatusList = function(){
        ep.statusOptions.push(new SelectModel(deny,0));
        ep.statusOptions.push(new SelectModel(open,1));
        ep.statusOptions.push(new SelectModel(wait,2));
    };
    this.store_el = ko.observable();
    this.datatable = ko.observable();
    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/shop|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "name", "value": ep.params['name']});
        aoData.push({ "name": "status", "value": ep.params['status']});
      };
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
      settings['fnDrawCallback'] = function(){
    	  util.adjustIframeHeight();
      };
      settings['aoColumns'] = [
        {//商铺名称
            'sDefaultContent': '',
            'fnRender': function (obj) {
                return obj.aData.shop_name;
            }
        },  //点击可查看商铺信息
        { 	//状态
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if (obj.aData.status == 0 ){
                    return '<span class="label label-danger">'+deny+'</span>';
                }else if(obj.aData.status == 1 ){
                    return '<span class="label label-success">'+open+'</span>';
                }else{
                    return '<span class="label label-info">'+wait+'</span>';
                }
            }
        },
        { 'mData': 'contact_user','sDefaultContent': ''}, //联系人
        { 'mData': 'contact_phone','sDefaultContent': ''}, //联系人电话
        { 'mData': 'sell_no','sDefaultContent': ''}, //营业号
        { 'mData': 'address','sDefaultContent': ''}, //地址
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
          ep.params['status']=ep.status();
          dataTable.fnDraw();
      });

    };
  };
  var epList = new ShopListViewModel();
  epList.init();
  ko.applyBindings(epList);

  $(document).ready(function(){
	  epList.setUpTable();
  });
});
