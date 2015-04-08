/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','advertService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,advertService,util){
  var link_info = parent.app.getI18nMessage('common.select.type.link');
  var code_info = parent.app.getI18nMessage('common.select.type.code');
  var sys_info = parent.app.getI18nMessage('common.sys.info');
  var sys_error = parent.app.getI18nMessage('common.sys.error');
  var submit_error = parent.app.getI18nMessage('common.sys.submit.fail');
  var yes = parent.app.getI18nMessage('common.select.type.on');
  var no = parent.app.getI18nMessage('common.select.type.off');
  var code_label = parent.app.getI18nMessage('advert.label.code');
  var desc_label = parent.app.getI18nMessage('advert.label.desc');
  var title_label = parent.app.getI18nMessage('advert.label.title');
  var atlease_tip = parent.app.getI18nMessage('common.sys.select.atleast.one');
  var AdvertListViewModel = function() {
    var ep = this;
    this.sel_text = ko.observable('-Select-');
    this.title = ko.observable('');
    this.type = ko.observable('');
    this.typeOptions = ko.observableArray([]);
    this.start_dateStr = ko.observable('');
    this.end_dateStr = ko.observable('');
    this.params = new Array();
    this.paramAdd = 'addadvert';
    this.paramDel = 'deladvert';
    this.delIds = ko.observable('');
    this.paramModify = 'modifyadvert';
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
        ep.loadTypeList();
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
    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };
    this.customer = ko.observable('');
    this.status = ko.observable();
    this.statusOptions = ko.observableArray([]);
    this.loadStatusList = function(){
          ep.statusOptions.push(new SelectModel(yes,1));
          ep.statusOptions.push(new SelectModel(no,0));
    };
    this.loadTypeList = function(){
        ep.typeOptions.push(new SelectModel(link_info,'L'));
        ep.typeOptions.push(new SelectModel(code_info,'C'));
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
                util.adjustIframeHeight();
                return;
            }
            $.when(advertService.getDetailById(id)).done(function (response) {
                if(response.state.code==200000) {
                    console.log('操作成功');
                    $(self).prop('tag','-');
                    var html = '<table><tr><td>'+title_label+response.result.title + '</td></tr>';
                    if(response.result.type == 'L'){
                        html += '<tr><td><img src="'+All580.imgBaseUrl+response.result.pic_path+'"></td></tr>';
                    }else{
                        html += '<tr><td>'+code_label+response.result.event+'</td></tr>';
                    }
                    html += '<tr><td>'+desc_label+response.result.description+'</td></tr></table>'
                    $(self).parent().parent().after('<tr style="display:none;" id="expand_'+id+'"><td colspan="6">'+html+'</td></tr>');
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

    this.datatable = ko.observable();

    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/advert|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "customer", "value": ep.params['customer']});
        aoData.push({ "name": "title", "value": ep.params['title']});
        aoData.push({ "name": "type", "value": ep.params['type']});
        aoData.push({ "name": "status", "value": ep.params['status']});
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
        {//略缩标题
            'sDefaultContent': '',
            'fnRender': function (obj) {
                var short_title = obj.aData.title.length > 15?obj.aData.title.substr(0,12)+'...':obj.aData.title;
                return '<span tag="+" class="label" style="margin:10px;cursor: pointer;background-color: #65CEA7;color: #ffffff;" onclick="eventHandle(\'' + obj.aData.id + '\',this);" >'+short_title+'</span>';
            }
        },  //点击可查看反馈意见
        { 	//类型
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.type == 'L'){
                    return '<span class="label label-info">'+'链接'+'</span>';
                }else{
                    return '<span class="label label-info">'+'代码'+'</span>';
                }
            }
        },
        { 	//状态
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.status == 1){
                    return '<span class="label label-success">'+yes+'</span>';
                }else{
                    return '<span class="label label-danger">'+no+'</span>';
                }
            }
        },
        { 'mData': 'customer', 'sDefaultContent': ''}, //客户
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
          ep.params['customer']=ep.customer();
          ep.params['title']=ep.title();
          ep.params['type']=ep.type();
          ep.params['status']=ep.status();
          ep.params['from']=ep.start_dateStr();
          ep.params['to']=ep.end_dateStr();
          dataTable.fnDraw();
      });

    };
    this.delParam = function (obj) {
        var deferred = advertService.deleteAdvert(obj);
        $.when(deferred).done(function (response) {
            if(response.state.code==200000) {
                console.log('删除成功');
                ep.params['customer']=ep.customer();
                ep.params['title']=ep.title();
                ep.params['type']=ep.type();
                ep.params['status']=ep.status();
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
  var epList = new AdvertListViewModel();
  epList.init();
  ko.applyBindings(epList);

  window.operateEvent = function(id,self){
      return epList.loadRef(self,id);
  }

  $(document).ready(function(){
	  epList.setUpTable();

      $('#add').click(function(){
          window.location.href = 'edit.html';
      });

      $('#sure_btn').click(function(){
          $('#myModal').modal('toggle');
          epList.delParam(epList.delIds());
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
