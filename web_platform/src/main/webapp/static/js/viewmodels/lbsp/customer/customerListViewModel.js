/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','customerService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,customerService,util){
  var sys_info = parent.app.getI18nMessage('common.sys.info');
  var sys_error = parent.app.getI18nMessage('common.sys.error');
  var yes = parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.app.getI18nMessage('common.sys.label.no');
  var normal = parent.app.getI18nMessage('common.sys.label.normal');
  var submit_error = parent.app.getI18nMessage('common.sys.submit.fail');
  var shop_type = parent.app.getI18nMessage('common.select.type.shop');
  var customer_type = parent.app.getI18nMessage('common.select.type.customer');
  var male_type = parent.app.getI18nMessage('common.select.type.male');
  var female_type = parent.app.getI18nMessage('common.select.type.female');
  var off_type = parent.app.getI18nMessage('common.select.type.off');
  var on_type = parent.app.getI18nMessage('common.select.type.on');
  var atlease_tip = parent.app.getI18nMessage('common.sys.select.atleast.one');
  var account_tip = parent.app.getI18nMessage('customer.account');
  var type_tip = parent.app.getI18nMessage('customer.type');
  var gender_tip = parent.app.getI18nMessage('customer.gender');
  var name_tip = parent.app.getI18nMessage('customer.name');
  var status_tip = parent.app.getI18nMessage('customer.status');
  var mobile_tip = parent.app.getI18nMessage('customer.mobile');
  var email_tip = parent.app.getI18nMessage('customer.email');
  var imei_tip = parent.app.getI18nMessage('customer.imei');
  var collection_tip = parent.app.getI18nMessage('customer.view.collection');
  var comment_tip = parent.app.getI18nMessage('customer.view.comment');
  var pwd_tip = parent.app.getI18nMessage('customer.rest.pwd');
  var edit_tip = parent.app.getI18nMessage('common.sys.edit.text');
  var add_tip = parent.app.getI18nMessage('common.sys.add.text');
  var reset_tip = parent.app.getI18nMessage('customer.reset.pwd.success');
  var pic_tip = parent.app.getI18nMessage('common.sys.user.pic');
  var require_tip = parent.app.getI18nMessage('common.sys.user.require');
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
    this.start_dateStr = ko.observable('');
    this.end_dateStr = ko.observable('');
    this.params = new Array();
    this.paramAdd = 'addcustomer';
    this.paramDel = 'delcustomer';
    this.delIds = ko.observable('');
    this.paramModify = 'modifycustomer';
    this.viewDel = ko.observable(false);
    this.viewAdd = ko.observable(false);
    this.canAdd = false;
    this.canDel = ko.observable('');
    this.canModify = false;
    this.viewModify = ko.observable(false);
    this.isUpdate = ko.observable(false);
    this.store = {};
    this.buildExpand = function(obj){
        var id='new',name='',account='',gender=-1,stauts=-1,type='',key='',email='',mobile='',src='',srcPath='',imgId='';
        var flag = false;
        if(obj != null){
            flag = true;
            id = obj.aData.id;name = obj.aData.name;account=obj.aData.account;gender=obj.aData.gender;status=obj.aData.status;type=obj.aData.type;
            key=obj.aData.key;email =obj.aData.email;mobile=obj.aData.mobile;src=All580.imgBaseUrl+obj.aData.path;srcPath=obj.aData.path;imgId = obj.aData.id;
        }
        var html = '<table style="width:100%;" class="table table-striped">'+
            '<tr>'+
            '<td rowspan="6" width="20%">'+
                '<table style="width: 100%">'+
                    '<tr>'+
                        '<td><img id="'+id+'_img" src="'+src+'"><span style="display: none;" template="'+id+'_result">'+pic_tip+'</span></td>'+
                            '</tr>'+
                        '<tr>'+
                            '<td align="center"><a href="javascript:void(0);" onclick="uploadPic(\''+imgId+'\')">'+edit_tip+'</a></td>'+
                            '</tr>'+
                        '</table>'+
                    '</td>'+
                '<td><span template="'+id+'_account">'+account_tip+'</span><input type="hidden" id="'+id+'_result" value="'+srcPath+'"></td>'+
                '<td><input type="text" template="'+id+'_account" disabled="disabled" value="'+account+'"/></td>'+
                '<td><span template="'+id+'_name">'+name_tip+'</span></td>'+
                '<td><input type="text" disabled="disabled" template="'+id+'_name" value="'+name+'"/></td>'+
        '</tr>'+ '<tr>'+'<td><span template="'+id+'_gender">'+gender_tip+'</span></td>'+'<td>'+
        '<select template="'+id+'_gender" disabled="disabled">'+
        '<option value="1" '+(gender==1?'selected':'')+' ><span>'+male_type+'</span></option>'+
        '<option value="0" '+(gender==0?'selected':'')+'><span>'+female_type+'</span></option>'+
        '</select>'+'</td>'+'<td><span template="'+id+'_status">'+status_tip+'</span></td>'+'<td>'+
        '<select template="'+id+'_status" disabled="disabled">'+
        '<option value="1" '+(status==1?'selected':'')+'><span>'+on_type+'</span></option>'+
        '<option value="0" '+(status==0?'selected':'')+'><span>'+off_type+'</span></option>'+
        '</select>'+ '</td>'+'</tr>'+'<tr>'+'<td><span template="'+id+'_type">'+type_tip+'</span></td>'+'<td>'+
        '<select template="'+id+'_type" disabled="disabled">'+
        '<option value="S" '+(type=='S'?'selected':'')+'><span>'+shop_type+'</span></option>'+
        '<option value="N" '+(type=='N'?'selected':'')+'><span>'+customer_type+'</span></option>'+
        '</select>'+'</td>'+'<td><span template="'+id+'_mobile">'+mobile_tip+'</span></td>'+
        '<td><input type="text" template="'+id+'_mobile" disabled="disabled" value="'+mobile+'"/></td>'+
        '</tr>'+'<tr>'+ '<td><span template="'+id+'_email">'+email_tip+'</span></td>'+
        '<td><input type="text" template="'+id+'_email" disabled="disabled" value="'+email+'"/></td>'+
        '<td><span>'+imei_tip+'</span></td>'+
        '<td><span template="'+id+'_imei">'+key+'</span></td>'+
        '</tr>'+ '<tr>'+'<td colspan="4">&nbsp;</td>'+
        '</tr>'+'<tr>'+'<td colspan="4" align="right" style="padding-right: 30px;">';
        if(flag){
            html += '<a href="javascript:void(0);" align="right">'+comment_tip+'</a>&nbsp;&nbsp;'+
                '<a href="javascript:void(0);" align="right">'+collection_tip+'</a>&nbsp;&nbsp;'+
                '<a href="javascript:void(0);" align="right" onclick="editDetail(3,\''+id+'\')" >'+pwd_tip+'</a>&nbsp;&nbsp;'+
                '<a href="javascript:void(0);" id="'+id+'_edit" onclick="editDetail(1,\''+id+'\')" >'+edit_tip+'</a>'+ '</td>'+'</tr>'+'</table>';
        }else{
            html += '<a href="javascript:void(0);" onclick="addDetail(2,\'new\')" >'+add_tip+'</a>'+ '</td>'+'</tr>'+'</table>';
        }
        return html;
    };
    this.check = function(id){
        $('select[template^='+id+'],input[template^='+id+']').each(function(){
            if($(this).val() == null || $(this).val() == ''){
                parent.amplify.publish('status.alerts',sys_info,$('span[template='+$(this).attr('template')+']').text()+require_tip);
                return false;
            }
        });
        return true;
    };
    this.editClick = function(id){
        var text = $('#'+id+'_edit').text();
        if(text == edit_tip){
            $('select[template^='+id+'],input[template^='+id+']').each(function(){
                $(this).removeAttr('disabled');
            });
            $('#'+id+'_edit').text(add_tip);
        }else{
            var flag = ep.check(id);
            if(flag)
                ep.submitServer(id);
        }
    };
    this.submitServer = function(id){
        var obj = {};
        if(id != null){
            obj.id = id;
        }else{
            id = 'new';
        }
        $('select[template^='+id+'],input[template^='+id+']').each(function(){
            if($(this).attr('template').split('_')[1] == 'result'){
                obj['path'] = $(this).val();
            }else{
                obj[$(this).attr('template').split('_')[1]] = $(this).val();
            }
        });
        var deferred = null;
        if(id != null){
            deferred = customerService.updateCustomer(obj);
        }else{
            deferred = customerService.addCustomer(obj);
        }
        $.when(deferred).done(function (response) {
            if(response.state.code==200000) {
                console.log('操作成功');
                ep.params['name']=ep.name();
                ep.params['account']=ep.account();
                ep.params['type']=ep.type();
                ep.params['status']=ep.status();
                ep.params['gender']=ep.gender();
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
    }
    this.addClick = function(){
        var flag = ep.check('new');
        if(flag){
            ep.submitServer(null);
        }
    };
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
        ep.loadGenderList();
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
    this.loadGenderList = function(){
        ep.genderOptions.push(new SelectModel(male_type,1));
        ep.genderOptions.push(new SelectModel(female_type,0));
    };
    this.loadTypeList = function(){
        ep.typeOptions.push(new SelectModel(shop_type,'S'));
        ep.typeOptions.push(new SelectModel(customer_type,'N'));
    };
    this.expandTable = function(self,id){
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
            var data = ep.store[id];
            var html = ep.buildExpand(data);
            $(self).parent().parent().after('<tr style="display:none;" id="expand_'+id+'"><td colspan="7">'+html+'</td></tr>');
            $('#expand_'+id).show();
            util.adjustIframeHeight();
        }
    };
    this.resetPwd = function(id){
        var deferred = customerService.resetCustomerPwd({id:id});
        $.when(deferred).done(function (response) {
            if(response.state.code==200000) {
                console.log('重置密码成功');
                parent.amplify.publish('status.alerts',sys_info,reset_tip);
            }else{
                parent.amplify.publish('status.alerts',sys_info,submit_error);
                console.log(response.msg);
            }
        }).fail(function (error) {
            parent.amplify.publish('status.alerts',sys_info,sys_error);
            console.log(error);
        });
    };

    this.datatable = ko.observable();

    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/customer|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "name", "value": ep.params['name']});
        aoData.push({ "name": "account", "value": ep.params['account']});
        aoData.push({ "name": "type", "value": ep.params['type']});
        aoData.push({ "name": "status", "value": ep.params['status']});
        aoData.push({ "name": "gender", "value": ep.params['gender']});
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
        {//账号
            'sDefaultContent': '',
            'fnRender': function (obj) {
                ep.store[obj.aData.id] = obj;
                return '<span tag="+" class="label" style="margin:10px;cursor: pointer;background-color: #65CEA7;color: #ffffff;" onclick="expand(this,\''+obj.aData.id+'\')" >'+obj.aData.account+'</span>';
            }
        },
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
          ep.params['type']=ep.type();
          ep.params['status']=ep.status();
          ep.params['gender']=ep.gender();
          ep.params['from']=ep.start_dateStr();
          ep.params['to']=ep.end_dateStr();
          dataTable.fnDraw();
      });

    };

    this.delParam = function (obj) {
        var deferred = customerService.deleteCustomer(obj);
        $.when(deferred).done(function (response) {
            if(response.state.code==200000) {
                console.log('删除成功');
                ep.params['name']=ep.name();
                ep.params['account']=ep.account();
                ep.params['type']=ep.type();
                ep.params['status']=ep.status();
                ep.params['gender']=ep.gender();
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
  var epList = new CustomerListViewModel();
  epList.init();
  ko.applyBindings(epList);
  window.operateEvent = function(self,id){
    return epList.expandTable(self,id);
  }
  window.adjustParentHeight = function(){
    return epList.adjusHeight();
  }

  window.detailClick = function(type,id){
      if(type == 1){
          return epList.editClick(id);
      }else if(type == 3){
          return epList.resetPwd(id);
      }else{
          return epList.addClick();
      }
  }
  $(document).ready(function(){
	  epList.setUpTable();
      $('#sure_btn').click(function(){
          $('#myModal').modal('toggle');
          epList.delParam(epList.delIds());
      });

      //添加事件
      $('#add').click(function(e){
          var tbody = $('#dynamic-table').find('tbody:first');
          var html = epList.buildExpand(null);
          $('<tr id="expand_new"><td colspan="7">'+html+'</td></tr>').prependTo(tbody);
          $('select[template^=new],input[template^=new]').each(function(){
                $(this).removeAttr('disabled');
          });
          epList.adjusHeight();
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
function editDetail(type,id){
    window.detailClick(type,id);
}
function addDetail(type,id){
    window.detailClick(2,id);
}
function expand(self,id){
    window.operateEvent(self,id);
}
function uploadPic(id){
    var orgId = id;
    if(id == null || id == ''){
        id = 'new';
        orgId= '';
    }
    var iframe_src = '../../common/fileupload.html?forward=customer/upload&ext=png|jpg' +
        '&winId=close_times&resultId='+id+'_result&id='+orgId+'&resultSrc='+id+'_img';
    $('#upload_iframe').prop('src',iframe_src);
    $('#uploadModal').modal('toggle');
}

