/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'shopService','fileUploadService', 'commonUtil',
    'amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers','additional-methods','bootstrap-fileupload'],
    function ($, ko, mapping, shopService, fileUploadService, util) {
    var sys_info = parent.app.getI18nMessage('common.sys.info');
    var sys_error = parent.app.getI18nMessage('common.sys.submit.fail');
    var add_text = parent.app.getI18nMessage("common.sys.add.text");
    var upt_text = parent.app.getI18nMessage("common.sys.upt.text");
    var ok_text = parent.app.getI18nMessage("common.sys.success.text");
    var deny = parent.app.getI18nMessage('shop.select.status.deny');
    var open = parent.app.getI18nMessage('shop.select.status.open');
    var wait = parent.app.getI18nMessage('shop.select.status.wait');
    var upload_error = parent.app.getI18nMessage('common.sys.upload.pic.error');
    var choose_upload = parent.app.getI18nMessage('common.sys.upload.pic.select');
    var ShopViewModel = function () {
        var me = this;
        this.isEdit = ko.observable();
        this.shopView = new ShopView();
        this.viewEdit = ko.observable(false);
        this.viewSelect = ko.observable(true);
        this.viewTextEdit = ko.observable(false);
        this.editParamId = ko.observable();
        this.statusOptions = ko.observableArray([]);
        this.wayOptions = ko.observableArray([]);
        this.addAuth = 'addshop';
        this.editAuth = 'modifyshop';
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
        this.loadStatusList = function(){
            me.statusOptions.push(new SelectModel(deny,0));
            me.statusOptions.push(new SelectModel(open,1));
            me.statusOptions.push(new SelectModel(wait,2));
        };

        this.init = function () {
          me.seti18nText();
          me.initAuth();
          me.loadStatusList();
          me.getIDFromUrl();
          if(typeof me.editParamId() != 'undefined'&& me.editParamId() != ''){
            me.isEdit(true);
            me.loadParam(me.editParamId());
          }else{
            me.shopView.setEmpty();
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
          var deferred = shopService.getDetailById(id);
          $.when(deferred).done(function (response) {
            if (response.state.code == 200000 &&typeof response.result!='undefined') {
                me.shopView.setData(response.result);
                me.sell_path = response.result.verify_pic_path;
                me.logo_path = response.result.pic_path;
              util.adjustIframeHeight();
            } else {
              console.log('load shopView failed');
              util.adjustIframeHeight();
            }
          }).fail(function (error) {
            console.log(error);
          });
        };
        this.changePath = function(type){
            if(type == 1){
                me.shopView.pic_path('');
                me.fileArray.push('logo');
            }else{
                me.shopView.verify_pic_path('');
                me.fileArray.push('sell');
            }
        };
//        this.store_upload = {logo:'Y',sell:'Y'};
        this.uploadFile = function(){
            var id = me.fileArray[me.fileIndex];
            if($('#'+id).val()!='') {
                me.fileIndex++;
                $("#edit").attr("disabled","disabled");
                $("#add").attr("disabled","disabled");
                $('#'+id).attr('name','uploadFile');
                fileUploadService.upload(id,'shop/upload',{type:id},me.uploadSuccess, me.uploadFailed);
            }else{
                parent.amplify.publish('status.alerts',choose_upload,sys_info);
            }
        };
        this.showCustomer = function(){
            var iframe_src = '../../common/commonCustomer.html';
            $('#customer_iframe').prop('src',iframe_src);
            $('#myModal').modal('toggle');
//            $(window.frames['customer_iframe'].document)
        };
        this.logo_path = '';
        this.sell_path = '';
        this.fileArray = [];
        this.fileIndex = 0;
        this.uploadSuccess = function(data,satus){
            if(data.state.code==200000) {
//                me[data.result.key] = data.result.val;
                if(data.result.key == 'logo_path'){
                    $('#logo').attr('name','logo');
                    me.logo_path =  data.result.val;
                }else if(data.result.key == 'sell_path'){
                    $('#sell').attr('name','sell');
                    me.sell_path =  data.result.val;
                }
                if(me.fileArray.length >= me.fileIndex){
                    me.submitParam();
                }else{
                    me.uploadFile();
                }

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
//            parent.amplify.publish('page.redirect','sysFrame',All580.serverName+'/lbsp/show/list.html');
        };
      this.submitParam = function () {
        var param_obj = {};
        $('input[tag=data],select[tag=data],textarea[tag=data]').each(function(){
            param_obj[$(this).attr('name')] = $(this).val();
        });
          for(var i = 0; i < me.fileArray.length;i++){
              if(me.fileArray[i] == 'logo'){
                  param_obj.pic_path = me.logo_path;
              }
              if(me.fileArray[i] == 'sell'){
                  param_obj.verify_pic_path = me.sell_path;
              }
          }
        param_obj.id  = me.editParamId();
        var msg = add_text;
        param_obj = mapping.toJSON(param_obj);
        var deferred = null;
        if(me.isEdit()){
            console.log(me.editParamId()+":id");
            msg = upt_text;
            deferred = shopService.modifyShop(param_obj);
        }else{
            deferred = shopService.addShop(param_obj);
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

    var ShopView = function () {
        var me = this;
        this.shop_name = ko.observable('');
        this.contact_user = ko.observable('');
        this.contact_phone = ko.observable('');
        this.id = ko.observable();
        this.verify_pic_path = ko.observable('');
        this.description = ko.observable('');
        this.status = ko.observable();
        this.pic_path = ko.observable('');
        this.latitude = ko.observable();
        this.longitude = ko.observable();
        this.area_code = ko.observable('');
        this.sell_no = ko.observable('');
        this.address = ko.observable('');
        this.customerName = ko.observable('');
        this.customer_id = ko.observable('');
        this.setEmpty = function(){
            me.shop_name('');
            me.contact_user('');
            me.contact_phone('');
            me.id();
            me.verify_pic_path('');
            me.description('');
            me.status();
            me.pic_path('');
            me.latitude();
            me.longitude();
            me.area_code('');
            me.sell_no('');
            me.address('');
            me.customerName(parent.app.getI18nMessage("common.sys.select.text"));
            me.customer_id();
        };
        this.setData = function(data) {
          if (typeof data != 'undefined') {
            if (typeof data.id != 'undefined') {
              me.id(data.id);
            }
            if (typeof data.shop_name != 'undefined') {
              me.shop_name(data.shop_name);
            }
            if (typeof data.contact_user != 'undefined') {
              me.contact_user(data.contact_user);
            }
            if (typeof data.contact_phone != 'undefined') {
              me.contact_phone(data.contact_phone);
            }
            if (typeof data.verify_pic_path != 'undefined') {
              me.verify_pic_path(All580.imgBaseUrl+data.verify_pic_path);

            }
            if (typeof data.description != 'undefined') {
              me.description(data.description);
            }
            if (typeof data.status != 'undefined') {
              me.status(data.status);
            }
              if (typeof data.pic_path != 'undefined') {
                  me.pic_path(All580.imgBaseUrl+data.pic_path);
                  me.logo_path = data.pic_path;
              }
              if (typeof data.latitude != 'undefined') {
                  me.latitude(data.latitude);
              }
              if (typeof data.longitude != 'undefined') {
                  me.longitude(data.longitude);
              }
              if (typeof data.area_code != 'undefined') {
                  me.area_code(data.area_code);
              }
              if (typeof data.sell_no != 'undefined') {
                  me.sell_no(data.sell_no);
              }
              if (typeof data.address != 'undefined') {
                  me.address(data.address);
              }
              if (typeof data.customerName != 'undefined') {
                  me.customerName(data.customerName);
              }else{
                 me.customerName(parent.app.getI18nMessage("common.sys.select.text"));
              }
              if (typeof data.customer_id != 'undefined') {
                  me.customer_id(data.customer_id);
              }
          }
        }
    };
    var model = new ShopViewModel();
    model.init();
    ko.applyBindings(model);
      //validate
      $().ready(function () {
        $("#editParam").validate({
          rules: {
              shop_name: {required:true},
              contact_user: {required:true},
              contact_phone: {required:true},
              status:{required:true},
              latitude:{required:true},
              longitude:{required:true},
              sell_no:{required:true},
              address:{required:true},
              logo : {
                  extension: function(){return 'png|jpg';}
              },
              sell : {
                  extension: function(){return 'png|jpg';}
              }
          },
          showErrors:function(errorMap,errorList) {
              this.defaultShowErrors();
              util.adjustIframeHeight();
      	  },
          submitHandler: function (form) {
              var tmp = model.shopView.customer_id();
              if(tmp == null || tmp == ''){
                  $('label[for=customer_text]').css('display','inline-block');
                  return;
              }
              var verify = model.shopView.verify_pic_path();
              var pic = model.shopView.pic_path();
              if(verify != null && pic != null && verify != '' && pic != ''){

                  model.submitParam();
                  return;
              }
              if(!($('#logo').val() != '') && (pic == null || pic == '')){
                  $('#logo_error').show();
                  return;
              }
              $('#logo_error').hide();
              if(!($('#sell').val() != '') && (verify == null || verify == '')){
                  $('#sell_error').show();
                  return;
              }
              $('#sell_error').hide();
              model.uploadFile();
          }
        }
      );
      $('#sure_btn').click(function(){
          var _return = window.frames['customer_iframe'].returnVal;
          if(typeof _return == 'undefined'){
              parent.amplify.publish('status.alerts',sys_info,parent.app.getI18nMessage('common.sys.select.atleast.one'));
              return ;
          }
          model.shopView.customer_id(_return.id);
          model.shopView.customerName(_return.name);
          $('#myModal').modal('toggle');
      });
      $('#logo').change(function(){
          model.changePath(1);
      });
      $('#sell').change(function(){
          model.changePath(2);
      });
      $('#goback').click(function () {
    	  self.location = 'list.html';
      });
    });
});
