/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','preferentialService','commonUtil','amplify','DT-bootstrap','bootstrap','bootstrap-datepicker'],
		function($,ko,preferentialService,util){
  var sys_info = parent.app.getI18nMessage('common.sys.info');
  var sys_error = parent.app.getI18nMessage('common.sys.error');
  var submit_error = parent.app.getI18nMessage('common.sys.submit.fail');
  var yes = parent.app.getI18nMessage('common.sys.label.yes');
  var no = parent.app.getI18nMessage('common.sys.label.no');
  var title_label = parent.app.getI18nMessage('preferential.label.title');
  var active_pre = parent.app.getI18nMessage('common.sys.select.active.preferential');
  var cut_off = parent.app.getI18nMessage('common.sys.select.cut.off');
  var over_tip = parent.app.getI18nMessage('common.sys.select.over');
  var active_tip = parent.app.getI18nMessage('common.sys.select.active');
  var atlease_tip = parent.app.getI18nMessage('common.sys.select.atleast.one');
  var was_price_label = parent.app.getI18nMessage('preferential.label.was.price');
  var now_price_label = parent.app.getI18nMessage('preferential.label.now.price');
  var desc_label = parent.app.getI18nMessage('preferential.label.desc');
  var comment_tip = parent.app.getI18nMessage('customer.view.comment');
  var mark_tip = parent.app.getI18nMessage('common.sys.label.mark');
  var off_tip = parent.app.getI18nMessage('preferential.label.off');
  var PreferentialListViewModel = function() {
    var ep = this;
    this.sel_text = ko.observable('-Select-');
    this.name = ko.observable('');
    this.title = ko.observable('');
    this.type = ko.observable('');
    this.typeOptions = ko.observableArray([]);
    this.start_dateStr = ko.observable('');
    this.end_dateStr = ko.observable('');
    this.params = new Array();
    this.paramAdd = 'addpreferential';
    this.paramDel = 'delpreferential';
    this.delIds = ko.observable('');
    this.paramModify = 'modifypreferential';
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
    this.status = ko.observable();
    this.shop = ko.observable();
    this.statusOptions = ko.observableArray([]);
    this.loadStatusList = function(){
        ep.statusOptions.push(new SelectModel(over_tip,0));
        ep.statusOptions.push(new SelectModel(active_tip,1));
    };
    this.adjustPic = function(){
        util.adjustIframeHeight();
    };
    this.loadTypeList = function(){
        ep.typeOptions.push(new SelectModel(cut_off,'O'));
        ep.typeOptions.push(new SelectModel(active_pre,'A'));
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
            $.when(preferentialService.getDetailById(id)).done(function (response) {
                if(response.state.code==200000) {
                    console.log('操作成功');
                    $(self).prop('tag','-');
                    var _type = $(self).attr('type');
                    var html = '<table style="margin-left: 10px;"><tr><td><img src="'+All580.imgBaseUrl+response.result.pic_path+'"  onload="loadPic()"/></td></tr>';
                    html += '<tr><td>'+title_label + response.result.title + '</td></tr>';
                    if(_type == 'O'){
                        html += '<tr><td>' + was_price_label + response.result.was_price + '</td></tr>';
                        html += '<tr><td>' + now_price_label + response.result.now_price + '</td></tr>';
                        html += '<tr><td>' + off_tip + (response.result.off * 100) + '%</td></tr>'
                    }else if(_type == 'A'){
                        html += '<tr><td>' + desc_label + response.result.description + '</td></tr>';
                    }
                    html += '<tr><td>' + mark_tip+response.result.mark + '</td></tr>'
                    html += '<tr><td><a href="javascript:void(0)" onclick="viewComment(\''+response.result.id+'\')" >'+comment_tip + '</a></td></tr></table>';
                    $(self).parent().parent().after('<tr style="display:none;" id="expand_'+id+'"><td colspan="9">'+html+'</td></tr>');
                    $('#expand_'+id).slideToggle('slow');
//                    util.adjustIframeHeight();
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
      settings['sAjaxSource'] = All580.serverName + '/service/core/preferential|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "status", "value": ep.params['status']});
        aoData.push({ "name": "title", "value": ep.params['title']});
        aoData.push({ "name": "shop", "value": ep.params['shop']});
        aoData.push({ "name": "type", "value": ep.params['type']});
        aoData.push({ "name": "start", "value": ep.params['start']});
        aoData.push({ "name": "end", "value": ep.params['end']});
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
                return '<span tag="+" class="label" type="'+obj.aData.type+'" style="margin:10px;cursor: pointer;background-color: #65CEA7;color: #ffffff;" onclick="eventHandle(\'' + obj.aData.id + '\',this);" >'+short_title+'</span>';
            }
        },  //点击可查看详细信息
        { 'mData': 'shopName', 'sDefaultContent': ''}, //店铺
        { 	//类型
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.type == 'A'){
                    return '<span class="label label-info">'+active_pre+'</span>';
                }else{
                    return '<span class="label label-info">'+cut_off+'</span>';
                }
            }
        },
        { 	//状态
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.end_time > new Date().getTime()){
                    return '<span class="label label-success">'+active_tip+'</span>';
                }else{
                    return '<span class="label label-danger">'+over_tip+'</span>';
                }
            }
        },
        { 'mData': 'category_name', 'sDefaultContent': ''}, //分类
        { 	//开始时间
            'sDefaultContent': '',
            'fnRender': function (obj) {
                return All580.DPGlobal.formatDateTime(obj.aData.start_time, 'yyyy-MM-dd HH:mm:ss');
            }
        },
		{ 	//结束时间
        	'sDefaultContent': '',
        	'fnRender': function (obj) {
        		return All580.DPGlobal.formatDateTime(obj.aData.end_time, 'yyyy-MM-dd HH:mm:ss');
        	}
		},
        { 	//操作
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(ep.canModify)
                    return "<a href='edit.html?id="+obj.aData.id+"'>编辑</a>";
                else
                    return "不可编辑";
            }
        }
      ];
      var dataTable = $('#dynamic-table').dataTable(settings);
      ep.datatable(dataTable);
      $('#search').click(function() {
          ep.params['shop']=ep.shop();
          ep.params['title']=ep.title();
          ep.params['type']=ep.type();
          ep.params['status']=ep.status();
          ep.params['start']=ep.start_dateStr();
          ep.params['end']=ep.end_dateStr();
          dataTable.fnDraw();
      });

    };
    this.delParam = function (obj) {
        var deferred = preferentialService.deletePreferential(obj);
        $.when(deferred).done(function (response) {
            if(response.state.code==200000) {
                console.log('删除成功');
                ep.params['shop']=ep.shop();
                ep.params['title']=ep.title();
                ep.params['type']=ep.type();
                ep.params['status']=ep.status();
                ep.params['start']=ep.start_dateStr();
                ep.params['end']=ep.end_dateStr();
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
  var epList = new PreferentialListViewModel();
  epList.init();
  ko.applyBindings(epList);

window.operateEvent = function(id,self){
    return epList.loadRef(self,id);
}
window.loadPicture = function(){
    return epList.adjustPic();
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
function viewComment(id){
    var iframe_src = '../../common/commonComment.html?type=p&id=' + id;
    $('#comment_iframe').prop('src',iframe_src);
    $('#commentModal').modal('toggle');
}
function eventHandle(id,self){
    window.operateEvent(id,self);
}

function loadPic(){
    window.loadPicture();
}