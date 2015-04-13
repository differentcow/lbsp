/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'advertService','fileUploadService', 'commonUtil','bootstrap-datetimepicker',
    'amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers','additional-methods','bootstrap-fileupload'],
    function ($, ko, mapping, advertService, fileUploadService, util) {
    var sys_info = parent.app.getI18nMessage('common.sys.info');
    var sys_error = parent.app.getI18nMessage('common.sys.submit.fail');
    var add_text = parent.app.getI18nMessage("common.sys.add.text");
    var upt_text = parent.app.getI18nMessage("common.sys.upt.text");
    var yes = parent.app.getI18nMessage('common.select.type.on');
    var no = parent.app.getI18nMessage('common.select.type.off');
    var ok_text = parent.app.getI18nMessage("common.sys.success.text");
    var link_info = parent.app.getI18nMessage('common.select.type.link');
    var code_info = parent.app.getI18nMessage('common.select.type.code');
    var upload_error = parent.app.getI18nMessage('common.sys.upload.pic.error');
    var choose_upload = parent.app.getI18nMessage('common.sys.upload.pic.select');
    var event_required = parent.app.getI18nMessage('advert.sys.event.required');
    var PreferentialViewModel = function () {
        var me = this;
        this.isEdit = ko.observable();
        this.advertView = new AdvertView();
        this.viewEdit = ko.observable(false);
        this.viewSelect = ko.observable(true);
        this.viewTextEdit = ko.observable(false);
        this.editParamId = ko.observable();
        this.statusOptions = ko.observableArray([]);
        this.wayOptions = ko.observableArray([]);
        this.addAuth = 'addadvert';
        this.editAuth = 'modifyadvert';
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
            me.typeOptions.push(new SelectModel(link_info,'L'));
            me.typeOptions.push(new SelectModel(code_info,'C'));
        };
        this.statusOptions = ko.observableArray([]);
        this.loadStatusList = function(){
            me.statusOptions.push(new SelectModel(yes,1));
            me.statusOptions.push(new SelectModel(no,0));
        };
        this.init = function () {
          me.seti18nText();
          me.initAuth();
          me.loadTypeList();
          me.loadStatusList();
          me.getIDFromUrl();
          if(typeof me.editParamId() != 'undefined'&& me.editParamId() != ''){
            me.isEdit(true);
            me.loadParam(me.editParamId());
          }else{
            me.viewLink(true);
            me.advertView.setEmpty();
            me.isEdit(false);
          }
        };
        this.getIDFromUrl = function(){
          var id = util.getQueryString('id');
          if(id != null){
            me.editParamId(id);
          }
        };
        this.chanegType = function(){
            me.viewLink(me.advertView.type() == 'L'?true:false);
            util.adjustIframeHeight();
        };
        this.loadParam = function(id){
          var deferred = advertService.getDetailById(id);
          $.when(deferred).done(function (response) {
            if (response.state.code == 200000 &&typeof response.result!='undefined') {
                me.advertView.setData(response.result);
                me.store_pic(response.result.pic_path);
                me.viewLink(response.result.type == 'L'?true:false);
                util.adjustIframeHeight();
            } else {
              console.log('load advertView failed');
              util.adjustIframeHeight();
            }
          }).fail(function (error) {
            console.log(error);
          });
        };
        this.alertErrorMsg = function(type){
            if(type=='C'){
                parent.amplify.publish('status.alerts',sys_error,event_required);
            }else{
                parent.amplify.publish('status.alerts',sys_error,choose_upload);
            }
        };
        this.viewLink = ko.observable(false);
        this.uploadFile = function(){
            if($('#uploadFile').val()!='') {
                $("#edit").attr("disabled","disabled");
                $("#add").attr("disabled","disabled");
                fileUploadService.upload('uploadFile','advert/upload',{},me.uploadSuccess, me.uploadFailed);
            }else{
                parent.amplify.publish('status.alerts',choose_upload,sys_info);
            }
        };
        this.changeUpload = function(){
            me.advertView.pic_path('');
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
        var msg = add_text;
        param_obj = mapping.toJSON(param_obj);
        var deferred = null;
        if(me.isEdit()){
            console.log(me.editParamId()+":id");
            msg = upt_text;
            deferred = advertService.modifyAdvert(param_obj);
        }else{
            deferred = advertService.addAdvert(param_obj);
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

    var AdvertView = function () {
        var me = this;
        this.title = ko.observable('');
        this.type = ko.observable('');
        this.id = ko.observable();
        this.status = ko.observable();
        this.pic_path = ko.observable('');
        this.event = ko.observable('');
        this.customer = ko.observable('');
        this.description = ko.observable('');
        this.setEmpty = function(){
            me.title('');
            me.type('');
            me.event('');
            me.id();
            me.customer('');
            me.description('');
            me.pic_path('');
            me.status();
        };
        this.setData = function(data) {
          if (typeof data != 'undefined') {
            if (typeof data.id != 'undefined') {
              me.id(data.id);
            }
            if (typeof data.title != 'undefined') {
              me.title(data.title);
            }
            if (typeof data.type != 'undefined') {
              me.type(data.type);
                if(data.type == 'C'){
                    if (typeof data.event != 'undefined') {
                        me.event(data.event);
                        me.pic_path('');
                    }
                }else{
                    if (typeof data.pic_path != 'undefined') {
                        me.event('');
                        me.pic_path(All580.imgBaseUrl+data.pic_path);
                    }
                }
            }
            if (typeof data.customer != 'undefined') {
              me.customer(data.customer);
            }
            if (typeof data.description != 'undefined') {
              me.description(data.description);
            }
            if (typeof data.status != 'undefined') {
              me.status(data.status);
            }
          }
        }
    };
    var model = new PreferentialViewModel();
    model.init();
    ko.applyBindings(model);
      //validate
      $().ready(function () {
        $("#editParam").validate({
          rules: {
              title: {required:true},
              customer:{required:true},
              uploadFile : {
                  extension: function(){return 'png|jpg';}
              }
          },
          showErrors:function(errorMap,errorList) {
              this.defaultShowErrors();
              util.adjustIframeHeight();
      	  },
          submitHandler: function (form) {
              var _type = model.advertView.type();
              if(_type == 'C' && (model.advertView.event() == null || model.advertView.event() == '')){
                  model.alertErrorMsg(_type);
                  return;
              }
              if(_type == 'L'){
                  model.uploadFile();
              }else{
                  model.submitParam();
              }
          }
        }
      );
      $('#uploadFile').change(function(){
          model.changeUpload();
      });
      $('#sure_btn').click(function(){
          var _return = window.frames['shop_iframe'].returnVal;
          if(typeof _return == 'undefined'){
              parent.amplify.publish('status.alerts',sys_info,parent.app.getI18nMessage('common.sys.select.atleast.one'));
              return ;
          }
          model.advertView.shop_id(_return.id);
          model.advertView.shopName(_return.shop_name);
          $('#myModal').modal('toggle');
      });
      $('#goback').click(function () {
    	  self.location = 'list.html';
      });
    });
});
