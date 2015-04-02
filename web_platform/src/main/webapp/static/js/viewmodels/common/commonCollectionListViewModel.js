/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','collectionService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,collectionService,util){
  var sys_info = parent.parent.app.getI18nMessage('common.sys.info');
  var sys_error = parent.parent.app.getI18nMessage('common.sys.error');
  var submit_error = parent.parent.app.getI18nMessage('common.sys.submit.fail');
  var yes = parent.parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.parent.app.getI18nMessage('common.sys.label.no');
  var anonymous = parent.parent.app.getI18nMessage('common.select.anonymous');
  var atlease_tip = parent.parent.app.getI18nMessage('common.sys.select.atleast.one');
  var shop_label = parent.parent.app.getI18nMessage('collection.shop.type.label');
  var promotion_label = parent.parent.app.getI18nMessage('collection.promotion.type.label');
  var CollectionListViewModel = function() {
    var ep = this;
    this.sel_text = ko.observable('-Select-');
    this.name = ko.observable('');
    this.title = ko.observable('');
    this.type = ko.observable('');
    this.typeOptions = ko.observableArray([]);
    this.start_dateStr = ko.observable('');
    this.end_dateStr = ko.observable('');
    this.params = new Array();
    this.delIds = ko.observable('');
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
        ep.getIDFromUrl();
//        ep.initAuth();
        ep.loadTypeList();
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
    this.id = ko.observable();
    this.getIDFromUrl = function(){
        var id = util.getQueryString('id');
        if(id != null){
            ep.id(id);
        }
    };
    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };
    this.loadTypeList = function(){
        ep.typeOptions.push(new SelectModel(shop_label,'S'));
        ep.typeOptions.push(new SelectModel(promotion_label,'G'));
    };

    this.datatable = ko.observable();

    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/collection|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "customerId", "value": ep.id()});
        aoData.push({ "name": "type", "value": ep.params['type']});
        aoData.push({ "name": "from", "value": ep.params['from']});
        aoData.push({ "name": "to", "value": ep.params['to']});
      };
      settings['iListLength'] = 3;
      settings['iDisplayLength'] = 10;
      settings['fnDrawCallback'] = function(){
    	  util.adjustIframeHeight();
      };
      settings['aoColumns'] = [
        { 'mData': 'customerName','sDefaultContent': ''}, //所属人
        {//收藏类型
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(typeof obj.aData.type == 'S'){
                    return '<span class="label label-info">'+shop_label+'</span>';
                }else{
                    return '<span class="label label-info">'+promotion_label+'</span>';
                }
            }
        },
        {//收藏内容
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(typeof obj.aData.type == 'S'){
                    return obj.aData.shopName;
                }else{
                    return obj.aData.title;
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
          ep.params['customerId']=ep.id();
          ep.params['type']=ep.type();
          ep.params['from']=ep.start_dateStr();
          ep.params['to']=ep.end_dateStr();
          dataTable.fnDraw();
      });

    };
  };
  var epList = new CollectionListViewModel();
  epList.init();
  ko.applyBindings(epList);

  window.operateEvent = function(id,self){
      return epList.loadRef(self,id);
  }

  $(document).ready(function(){
	  epList.setUpTable();
  });
});

function eachCheckBox(_self){
    var _chk = $(_self).prop('checked');
    $('input[name=foreach_chk_data]').each(function(){
        $(this).prop('checked',_chk);
    });
}

function eventHandle(id,self){
    window.operateEvent(id,self);
}
