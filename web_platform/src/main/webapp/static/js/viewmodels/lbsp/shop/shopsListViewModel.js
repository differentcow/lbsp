/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','shopService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,shopService,util){
  var sys_info = parent.app.getI18nMessage('common.sys.info');
  var sys_error = parent.app.getI18nMessage('common.sys.error');
  var yes = parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.app.getI18nMessage('common.sys.label.no');
  var normal = parent.app.getI18nMessage('common.sys.label.normal');
  var delete_status = parent.app.getI18nMessage('common.sys.label.delStatus');
  var submit_error = parent.app.getI18nMessage('common.sys.submit.fail');
  var anonymous = parent.app.getI18nMessage('common.select.anonymous');
  var atlease_tip = parent.app.getI18nMessage('common.sys.select.atleast.one');
  var deny = parent.app.getI18nMessage('shop.select.status.deny');
  var open = parent.app.getI18nMessage('shop.select.status.open');
  var wait = parent.app.getI18nMessage('shop.select.status.wait');
  var longitude = parent.app.getI18nMessage('shop.longitude');
  var latitude = parent.app.getI18nMessage('shop.latitude');
  var areaCode = parent.app.getI18nMessage('shop.areaCode');
  var customerName = parent.app.getI18nMessage('shop.customerName');
  var sellPic = parent.app.getI18nMessage('shop.sellPic');
  var desc = parent.app.getI18nMessage('shop.desc');
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
    this.initAuth = function () {
      if (parent.app.checkAuth(ep.paramAdd)) {
          ep.canAdd = true;
          ep.viewAdd(true);
      }
      if (parent.app.checkAuth(ep.paramDel)) {
          ep.canDel = true;
          ep.viewDel(true);
      }
      if (parent.app.checkAuth(ep.paramModify)) {
          ep.canModify = true;
          ep.viewModify(true);
      }
    };
      this.seti18nText = function(){
          $('title,label,span,div,th').each(function(){
              var attr = $(this).attr('i18n');
              if(attr !=null && attr != ''){
                  $(this).text(parent.app.getI18nMessage(attr));
              }
          });
          $('h4,button').each(function(){
              var attr = $(this).attr('i18n');
              if(attr !=null && attr != ''){
                  $(this).html(parent.app.getI18nMessage(attr));
              }
          });
      };
    this.init = function () {
        ep.sel_text(parent.app.getI18nMessage("common.sys.select.text"));
        ep.seti18nText();
        ep.initAuth();
        ep.loadStatusList();
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

      this.buildHtml = function(data){
        var html =  '<table>' +
                     '<tr><td><img src="'+All580.imgBaseUrl+data.pic_path+'"></td></tr>' +
                     '<tr><td>'+longitude+data.longitude+'&nbsp;&nbsp;'+latitude+data.latitude+'</td></tr>' +
                     '<tr><td>'+customerName+data.customerName+'&nbsp;&nbsp;'+areaCode+data.area_code+'</td></tr>' +
                     '<tr><td>'+desc+(typeof data.description == 'undefined'?'':data.description)+'</td></tr>' +
                     '<tr><td><a href="javascript:void(0)" onclick="showSellImg(\''+data.verify_pic_path+'\')">'+sellPic+'</a></td></tr></table>';
          return html;
      };
      this.loadRef = function(self,id){
          if($(self).prop('tag') == '-'){
              $('#expand_'+id).hide();
              $(self).prop('tag','+');
              util.adjustIframeHeight();
          }else{
              var tmp = document.getElementById('expand_'+id);
              if(typeof  tmp != 'undefined' && tmp != null){
                  $('#expand_'+id).show();
                  $(self).prop('tag','-');
                  return;
              }
              $.when(shopService.getDetailById(id)).done(function (response) {
                  if(response.state.code==200000) {
                      console.log('获取成功');
                      $(self).prop('tag','-');
                      var html = ep.buildHtml(response.result);
                      $(self).parent().parent().after('<tr style="display:none;" id="expand_'+id+'"><td colspan="9"><p>' + html + '</p></td></tr>');
                      $('#expand_'+id).slideToggle('slow');
                      util.adjustIframeHeight();
                  }else{
                      parent.amplify.publish('status.alerts',sys_info,submit_error);
                      console.log(response.msg);
                  }
              }).fail(function (error) {
                  parent.amplify.publish('status.alerts',sys_info,sys_error);
                  console.log(error);
              });
          }
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

    this.datatable = ko.observable();

    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/shop|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "name", "value": ep.params['name']});
        aoData.push({ "name": "status", "value": ep.params['status']});
        aoData.push({ "name": "user", "value": ep.params['user']});
        aoData.push({ "name": "address", "value": ep.params['address']});
        aoData.push({ "name": "sell", "value": ep.params['sell']});
        aoData.push({ "name": "from", "value": ep.params['from']});
        aoData.push({ "name": "to", "value": ep.params['to']});
      };
      settings['fnDrawCallback'] = function(){
    	  util.adjustIframeHeight();
      };
      settings['aoColumns'] = [
        {   //checkbox
        	'sDefaultContent': '',
        	'fnRender': function (obj) {
               return '<input style=\'margin-left:10px;cursor:pointer;\' name=\'foreach_chk_data\' type=\'checkbox\'id=\''+obj.aData.id+'\' >';
        	}
        },
        {//商铺名称
            'sDefaultContent': '',
            'fnRender': function (obj) {
                return '<span tag="+" class="label" style="margin:10px;cursor: pointer;background-color: #65CEA7;color: #ffffff;" onclick="eventHandle(\'' + obj.aData.id + '\',this);" >'+obj.aData.shop_name+'</span>';
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
		},
        { 	//操作
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(ep.canModify)
                    return '<a href="edit.html?id='+obj.aData.id+'">编辑</a>';
                else
                    return '不可编辑';
            }
        }

      ];
      var dataTable = $('#dynamic-table').dataTable(settings);
        ep.datatable(dataTable);
      $('#search').click(function() {
          ep.params['name']=ep.name();
          ep.params['status']=ep.status();
          ep.params['sell']=ep.sell();
          ep.params['address']=ep.address();
          ep.params['user']=ep.user();
          ep.params['from']=ep.start_dateStr();
          ep.params['to']=ep.end_dateStr();
          dataTable.fnDraw();
      });

    };
    this.delParam = function (obj) {
        var deferred = shopService.deleteShop(obj);
        $.when(deferred).done(function (response) {
            if(response.state.code==200000) {
                console.log('删除成功');
                ep.params['name']=ep.name();
                ep.params['status']=ep.status();
                ep.params['sell']=ep.sell();
                ep.params['address']=ep.address();
                ep.params['user']=ep.user();
                ep.params['from']=ep.start_dateStr();
                ep.params['to']=ep.end_dateStr();
                ep.datatable().fnDraw();
            }else{
                parent.amplify.publish('status.alerts',sys_info,submit_error);
                console.log(response.msg);
            }
        }).fail(function (error) {
            parent.amplify.publish('status.alerts',sys_info,sys_error);
            console.log(error);
        });
    };
  };
  var epList = new ShopListViewModel();
  epList.init();
  ko.applyBindings(epList);

 window.operateEvent = function(id,self){
     return epList.loadRef(self,id);
 }

  $(document).ready(function(){
	  epList.setUpTable();
      $('#sure_btn').click(function(){
          $('#myModal').modal('toggle');
          epList.delParam(epList.delIds());
      });

      $('#add').click(function(){
          window.location.href='edit.html';
      });

      //删除事件
      $('#delete').click(function(e){
          var ary = [];
          $('input[name=foreach_chk_data]').each(function(){
              if($(this).prop('checked')){
                  ary.push($(this).prop('id'));
              }
          });
          if(ary.length <= 0){
              parent.amplify.publish('status.alerts',sys_info,atlease_tip);
              return;
          }
          epList.delIds(ary.toString());
          $('#myModal').modal('toggle');
      });
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

function showSellImg(path){
    $('#img_sell').attr('src',All580.imgBaseUrl + path);
    $('#sellModal').modal('toggle');
}