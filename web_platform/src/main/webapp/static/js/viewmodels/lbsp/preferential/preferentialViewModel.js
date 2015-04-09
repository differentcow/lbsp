/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'preferentialService','fileUploadService', 'commonUtil','bootstrap-datetimepicker',
    'amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers','additional-methods','bootstrap-fileupload'],
    function ($, ko, mapping, preferentialService, fileUploadService, util) {
    var sys_info = parent.app.getI18nMessage('common.sys.info');
    var sys_error = parent.app.getI18nMessage('common.sys.submit.fail');
    var add_text = parent.app.getI18nMessage("common.sys.add.text");
    var upt_text = parent.app.getI18nMessage("common.sys.upt.text");
    var ok_text = parent.app.getI18nMessage("common.sys.success.text");
    var active_pre = parent.app.getI18nMessage('common.sys.select.active.preferential');
    var cut_off = parent.app.getI18nMessage('common.sys.select.cut.off');
    var over_tip = parent.app.getI18nMessage('common.sys.select.over');
    var active_tip = parent.app.getI18nMessage('common.sys.select.active');
    var upload_error = parent.app.getI18nMessage('common.sys.upload.pic.error');
    var choose_upload = parent.app.getI18nMessage('common.sys.upload.pic.select');
    var PreferentialViewModel = function () {
        var me = this;
        this.isEdit = ko.observable();
        this.pView = new PView();
        this.viewEdit = ko.observable(false);
        this.viewSelect = ko.observable(true);
        this.viewTextEdit = ko.observable(false);
        this.editParamId = ko.observable();
        this.statusOptions = ko.observableArray([]);
        this.wayOptions = ko.observableArray([]);
        this.addAuth = 'addpreferential';
        this.editAuth = 'modifypreferential';
        this.canAdd = ko.observable(false);
        this.canEdit = ko.observable(false);
        this.initAuth = function () {
          if (parent.app.checkAuth(me.addAuth)) {
            me.canAdd(true);
          }
          if (parent.app.checkAuth(me.editAuth)) {
            me.canEdit(true);
          }
        };
        this.seti18nText = function(){
            $('title,legend,label,span').each(function(){
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
        this.typeOptions = ko.observableArray([]);
        this.loadTypeList = function(){
            me.typeOptions.push(new SelectModel(cut_off,'O'));
            me.typeOptions.push(new SelectModel(active_pre,'A'));
        };
        //初始化Date Picker
        this.datePickerInit = function(){
            //start
            var checkin = $('.dpd1').datetimepicker({showMeridian: true,autoclose: true,todayBtn: true}).on('changeDate', function (ev) {
                var date = new Date(ev.date);
                var date_str =  All580.DPGlobal.formatDateTime(date, 'yyyy-MM-dd hh:mm:ss');
                me.pView.start_time_s(date_str);
                $('.dpd2')[0].focus();
            }).data('datepicker');
            //end
            var checkout = $('.dpd2').datetimepicker({showMeridian: true,autoclose: true,todayBtn: true}).on('changeDate', function (ev) {
                var date = new Date(ev.date);
                var date_str =  All580.DPGlobal.formatDateTime(date, 'yyyy-MM-dd hh:mm:ss');
                me.pView.end_time_s(date_str);
//                checkout.hide();
//                $('.dpd2').datetimepicker('hide');
            }).data('datepicker');
        };
        this.init = function () {
          me.seti18nText();
          me.initAuth();
          me.loadTypeList();
          me.datePickerInit();
          me.getIDFromUrl();
          if(typeof me.editParamId() != 'undefined'&& me.editParamId() != ''){
            me.isEdit(true);
            me.loadParam(me.editParamId());
          }else{
            me.pView.setEmpty();
            me.isEdit(false);
          }
        };
        this.getIDFromUrl = function(){
          var id = util.getQueryString('id');
          if(id != null){
            me.editParamId(id);
          }
        };
        this.loadParam = function(id){
          var deferred = preferentialService.getDetailById(id);
          $.when(deferred).done(function (response) {
            if (response.state.code == 200000 &&typeof response.result!='undefined') {
                me.pView.setData(response.result);
                me.store_pic(response.result.pic_path);
              util.adjustIframeHeight();
            } else {
              console.log('load pView failed');
              util.adjustIframeHeight();
            }
          }).fail(function (error) {
            console.log(error);
          });
        };
        this.viewActive = function(){
          if(me.pView.type() == 'A'){
              return true;
          }
          return false;
        };
//        this.store_upload = {logo:'Y',sell:'Y'};
        this.uploadFile = function(){
            if($('#uploadFile').val()!='') {
                me.fileIndex++;
                $("#edit").attr("disabled","disabled");
                $("#add").attr("disabled","disabled");
                fileUploadService.upload('uploadFile','preferential/upload',{},me.uploadSuccess, me.uploadFailed);
            }else{
                parent.amplify.publish('status.alerts',choose_upload,sys_info);
            }
        };
        this.showShop = function(){
            var iframe_src = '../../common/commonShop.html';
            $('#shop_iframe').prop('src',iframe_src);
            $('#myModal').modal('toggle');
        };
        this.showCategory = function(){
            var iframe_src = '../../common/commonCategory.html';
            $('#category_iframe').prop('src',iframe_src);
            $('#categoryModal').modal('toggle');
        };
        this.changeUpload = function(){
            me.pView.pic_path('');
        };
        this.store_pic = ko.observable('');
        this.uploadSuccess = function(data,satus){
            if(data.state.code==200000) {
                me.store_pic(data.result);
                me.submitParam();
            }else{
                parent.amplify.publish('status.alerts',upload_error,data.msg);
                console.log('upload failed code '+data.state.code);
                $("#edit").removeAttr("disabled","disabled");
                $("#add").removeAttr("disabled","disabled");
            }
        };
        this.uploadFailed = function(data,status, e){
            parent.amplify.publish('status.alerts',upload_error,sys_error);
            $("#edit").removeAttr("disabled","disabled");
            $("#add").removeAttr("disabled","disabled");
        };
        this.backToList = function(){
            self.location = 'list.html';
        };
      this.submitParam = function () {
        var param_obj = {};
        $('input[tag=data],select[tag=data],textarea[tag=data]').each(function(){
            param_obj[$(this).attr('name')] = $(this).val();
        });
        param_obj.pic_path = me.store_pic();
        param_obj.id  = me.editParamId();
        param_obj.start_time  = new Date(($('input[name=start_date]').val()+':00').replace(/-/g,"/")).getTime();
        param_obj.end_time  = new Date(($('input[name=end_date]').val()+':00').replace(/-/g,"/")).getTime();
        var msg = add_text;
        param_obj = mapping.toJSON(param_obj);
        var deferred = null;
        if(me.isEdit()){
            console.log(me.editParamId()+":id");
            msg = upt_text;
            deferred = preferentialService.modifyPreferential(param_obj);
        }else{
            deferred = preferentialService.addPreferential(param_obj);
        }
        $.when(deferred).done(function (response) {
          if(response.state.code==200000) {
            console.log(msg + ok_text);
            me.backToList();
          }else{
            parent.amplify.publish('status.alerts',sys_info,sys_error);
            console.log(response.msg);
          }
            $("#edit").removeAttr("disabled","disabled");
            $("#add").removeAttr("disabled","disabled");
        }).fail(function (error) {
          parent.amplify.publish('status.alerts',sys_info,msg + sys_error);
          console.log(error);
            $("#edit").removeAttr("disabled","disabled");
            $("#add").removeAttr("disabled","disabled");
        });
      };
    };

    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };

    var PView = function () {
        var me = this;
        this.title = ko.observable('');
        this.type = ko.observable();
        this.was_price = ko.observable();
        this.id = ko.observable();
        this.org_id = ko.observable();
        this.org_category_id = ko.observable();
        this.now_price = ko.observable('');
        this.off = ko.observable('');
        this.status = ko.observable();
        this.pic_path = ko.observable('');
        this.mark = ko.observable('');
        this.start_time = ko.observable();
        this.start_time_s = ko.observable('');
        this.end_time = ko.observable();
        this.end_time_s = ko.observable('');
        this.shopName = ko.observable('');
        this.shop_id = ko.observable();
        this.description = ko.observable('');
        this.statusStr = ko.observable('');
        this.statusView = ko.observable(false);
        this.category_name = ko.observable('');
        this.category_id = ko.observable();
        this.setEmpty = function(){
            me.title('');
            me.type('');
            me.was_price('');
            me.id();
            me.org_id();
            me.org_category_id();
            me.now_price('');
            me.description('');
            me.off('');
            me.pic_path('');
            me.status();
            me.mark('');
            me.category_name(parent.app.getI18nMessage("common.sys.select.text"));
            me.category_id();
            me.start_time();
            me.end_time();
            me.start_time_s('');
            me.end_time_s('');
            me.shopName(parent.app.getI18nMessage("common.sys.select.text"));
            me.shop_id();
            me.statusView(false);
            me.statusStr(over_tip);
        };
        this.setData = function(data) {
          if (typeof data != 'undefined') {
            if (typeof data.id != 'undefined') {
              me.id(data.id);
            }
            if (typeof data.org_id != 'undefined') {
                me.org_id(data.org_id);
            }
            if (typeof data.org_category_id != 'undefined') {
                me.org_category_id(data.org_category_id);
            }
            if (typeof data.title != 'undefined') {
              me.title(data.title);
            }
            if (typeof data.type != 'undefined') {
              me.type(data.type);
            }
            if (typeof data.was_price != 'undefined') {
              me.was_price(data.was_price);
            }
            if (typeof data.off != 'undefined') {
              me.off(data.off);
            }
            if (typeof data.description != 'undefined') {
              me.description(data.description);
            }
            if (typeof data.status != 'undefined') {
              me.status(data.status);
            }
              if (typeof data.category_name != 'undefined') {
                  me.category_name(data.category_name);
              }else{
                  me.category_name(parent.app.getI18nMessage("common.sys.select.text"));
              }
              if (typeof data.category_id != 'undefined') {
                  me.category_id(data.category_id);
              }
              if (typeof data.pic_path != 'undefined') {
                  me.pic_path(All580.imgBaseUrl+data.pic_path);
              }
              if (typeof data.now_price != 'undefined') {
                  me.now_price(data.now_price);
              }
              if (typeof data.mark != 'undefined') {
                  me.mark(data.mark);
              }
              if (typeof data.start_time != 'undefined') {
                  me.start_time(data.start_time);
                  me.start_time_s(All580.DPGlobal.formatDateTime(data.start_time, 'yyyy-MM-dd HH:mm'));
              }
              if (typeof data.end_time != 'undefined') {
                  me.end_time(data.end_time);
                  if(data.end_time >= new Date().getTime()){
                      me.statusStr(active_tip);
                      me.statusView(true);
                  }else{
                      me.statusStr(over_tip);
                      me.statusView(false);
                  }
                  me.end_time_s(All580.DPGlobal.formatDateTime(data.end_time, 'yyyy-MM-dd HH:mm'));
              }
              if (typeof data.shopName != 'undefined') {
                  me.shopName(data.shopName);
              }else{
                 me.shopName(parent.app.getI18nMessage("common.sys.select.text"));
              }
              if (typeof data.shop_id != 'undefined') {
                  me.shop_id(data.shop_id);
              }
          }
        }
    };
    var desc_tip_error = parent.app.getI18nMessage('preferential.error.tip.desc');
    var was_tip_error = parent.app.getI18nMessage('preferential.error.tip.was');
    var now_tip_error = parent.app.getI18nMessage('preferential.error.tip.now');
    $.validator.addMethod("descriptionCheck",function(value,element){
        if(model.pView.type() == 'A' && (value == null || value == '')){
            return false;
        }
        return true;
    },desc_tip_error);
    $.validator.addMethod("wasPriceCheck",function(value,element){
        if(model.pView.type() == 'O' && (value == null || value == '')){
            return false;
        }
        return true;
    },was_tip_error);
    $.validator.addMethod("nowPriceCheck",function(value,element){
        if(model.pView.type() == 'O' && (value == null || value == '')){
            return false;
        }
        return true;
    },now_tip_error);
    var model = new PreferentialViewModel();
    model.init();
    ko.applyBindings(model);
      //validate
      $().ready(function () {
        $("#editParam").validate({
          rules: {
              title: {required:true},
              type: {required:true},
              description:{descriptionCheck:true},
              was_price:{wasPriceCheck:true},
              now_price:{nowPriceCheck:true},
              uploadFile : {
                  extension: function(){return 'png|jpg';}
              }
          },
          showErrors:function(errorMap,errorList) {
              this.defaultShowErrors();
              util.adjustIframeHeight();
      	  },
          submitHandler: function (form) {
              var tmp = model.pView.shop_id();
              if(tmp == null || tmp == ''){
                  $('label[for=shop_name]').css('display','inline-block');
                  return;
              }
              var _category = model.pView.category_id();
              if(_category == null || _category == ''){
                  $('label[for=category_name]').css('display','inline-block');
                  return;
              }
              var s_t = model.pView.start_time_s();
              var e_t = model.pView.end_time_s();
              if(s_t == null || s_t == '' || e_t == null || e_t == ''){
                  $('#p_time').css('display','inline-block');
                  return;
              }
              var pic = model.pView.pic_path();
              if(pic != null && pic != ''){
                  model.submitParam();
                  return;
              }
              $('#sell_error').hide();
              $('#p_time').hide();
              model.uploadFile();
          }
        }
      );
      $('#uploadFile').change(function(){
          model.changeUpload();
      });
      $('#category_iframe_btn').click(function(){
          var _return = window.frames['category_iframe'].returnVal;
          if(typeof _return == 'undefined'){
              parent.amplify.publish('status.alerts',sys_info,parent.app.getI18nMessage('common.sys.select.atleast.one'));
              return ;
          }
          model.pView.category_id(_return.id);
          model.pView.category_name(_return.name);
          $('#categoryModal').modal('toggle');
      });
      $('#sure_btn').click(function(){
          var _return = window.frames['shop_iframe'].returnVal;
          if(typeof _return == 'undefined'){
              parent.amplify.publish('status.alerts',sys_info,parent.app.getI18nMessage('common.sys.select.atleast.one'));
              return ;
          }
          model.pView.shop_id(_return.id);
          model.pView.shopName(_return.shop_name);
          $('#myModal').modal('toggle');
      });
      $('#goback').click(function () {
    	  self.location = 'list.html';
      });
    });
});
