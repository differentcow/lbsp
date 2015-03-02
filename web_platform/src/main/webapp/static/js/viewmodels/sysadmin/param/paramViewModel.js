/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'paramsService', 'commonUtil',
    'amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers'],
    function ($, ko, mapping, paramsService, util) {
    var sys_info = parent.app.getI18nMessage('common.sys.info');
    var sys_error = parent.app.getI18nMessage('common.sys.submit.fail');
    var add_text = parent.app.getI18nMessage("common.sys.add.text");
    var upt_text = parent.app.getI18nMessage("common.sys.upt.text");
    var ok_text = parent.app.getI18nMessage("common.sys.success.text");
    var ParamViewModel = function () {
        var me = this;
        this.isEdit = ko.observable();
        this.paramView = new ParamView();
        this.viewEdit = ko.observable(false);
        this.viewSelect = ko.observable(true);
        this.viewTextEdit = ko.observable(false);
        this.editParamId = ko.observable();
        this.typeOptions = ko.observableArray([]);
        this.wayOptions = ko.observableArray([]);
        this.addAuth = 'addparam';
        this.editAuth = 'modifyparam';
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
                    $(this).text(parent.app.getI18nMessage('param.'+attr));
                }
            });
            $('#add').html(parent.app.getI18nMessage('common.btn.addBtn'));
            $('#edit').html(parent.app.getI18nMessage('common.btn.editBtn'));
            $('#goback').html(parent.app.getI18nMessage('common.btn.returnBtn'));
        };
        this.loadTypeList = function(){
            var deferred = paramsService.getParams(null);
            $.when(deferred).done(function (response) {
                if(typeof response =="string") {
                    eval('var result =' + response);
                }else{
                    var result = response;
                }
                if (result.state.code == 200000 && result.result.length > 0) {
                    me.typeOptions([]);
                    $.each(result.result, function (index,status) {
                        me.typeOptions.push(new SelectModel(status.type_meaning,status.type));
                    });
                } else {
                    console.log('load types failed');
                }
            }).fail(function (error) {
                console.log(error);
            });
        };

        this.initTypeWay = function(){
            var et = parent.app.getI18nMessage("param.type.had");
            var ct = parent.app.getI18nMessage("param.type.customer");
            me.wayOptions([]);
            me.wayOptions.push(new SelectModel(et,'had'));
            me.wayOptions.push(new SelectModel(ct,'customer'));
            me.paramView.way('had');
        };

        this.init = function () {
          me.initTypeWay();
          me.initAuth();
          me.loadTypeList();
          me.getIDFromUrl();
          if(typeof me.editParamId() != 'undefined'&& me.editParamId() != ''){
            me.isEdit(true);
            me.loadParam(me.editParamId());
          }else{
            me.paramView.setEmpty();
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
          var deferred = paramsService.getParamById(id);
          $.when(deferred).done(function (response) {
            if (response.state.code == 200000 &&typeof response.result!='undefined') {
                me.paramView.setData(response.result);
              util.adjustIframeHeight();
            } else {
              console.log('load paramView failed');
              util.adjustIframeHeight();
            }
          }).fail(function (error) {
            console.log(error);
          });
        };

        this.backToList = function(){
            parent.amplify.publish('page.redirect','sysFrame',All580.serverName+'/sysadmin/param/list.html');
        }

        this.typeWay = function(way){
            if(way == 'had'){
                me.viewSelect(true);
                me.viewEdit(false);
                me.viewTextEdit(false);
            }
            if(way == 'customer'){
                me.viewSelect(false);
                me.viewEdit(true);
                me.viewTextEdit(true);
            }
        };

        this.autoCompleteCodeText = function(){
            var code = me.paramView.type_code();
            var ary = me.typeOptions();
            var flag = false;
            var tmpVar = '';;
            for(var i = 0;i < ary.length;i++){
                if(ary[i].val == code){
                    flag = true;
                    tmpVar = ary[i].text;
                    break;
                }
            }
            if(flag){
                me.paramView.type_meaning(tmpVar);
            }else{
                me.paramView.type_meaning('');
            }
        };

      this.submitParam = function () {
        var param_obj = {
          name:me.paramView.name(),
          code:me.paramView.code(),
          type:me.paramView.opType(),
          type_meaning:me.paramView.opText()
        };
        var msg = add_text;
        param_obj = mapping.toJSON(param_obj);
        var deferred = null;
        if(me.isEdit()){console.log(me.editParamId()+":id");
            msg = upt_text;
            deferred = paramsService.modifyParam(param_obj, me.editParamId());
        }else{
            deferred = paramsService.addParam(param_obj);
        }
        $.when(deferred).done(function (response) {
          if(response.state.code==200000) {
            console.log(msg + ok_text);
            me.backToList();
          }else{
            parent.amplify.publish('status.alerts',sys_info,sys_error);
            console.log(response.msg);
          }
        }).fail(function (error) {
          parent.amplify.publish('status.alerts',sys_info,msg + sys_error);
          console.log(error);
        });
      };

    };

    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };

    var ParamView = function () {
        var me = this;
        this.way = ko.observable('had');
        this.opText = ko.observable();
        this.opType = ko.observable();
        this.id = ko.observable('');
        this.code = ko.observable('');
        this.name = ko.observable('');
        this.type = ko.observable('');
        this.type_code = ko.observable('');
        this.type_meaning = ko.observable('');
        this.create_date = ko.observable('');
        this.description = ko.observable('');
        this.setEmpty = function(){
            me.opText = ko.observable();
            me.opType = ko.observable();
            me.id = ko.observable('');
            me.code = ko.observable('');
            me.name = ko.observable('');
            me.type = ko.observable('');
            me.type_code = ko.observable('');
            me.type_meaning = ko.observable('');
        };
        this.setData = function(data) {
          if (typeof data != 'undefined') {
            if (typeof data.id != 'undefined') {
              me.id(data.id);
            }
            if (typeof data.type != 'undefined') {
              me.type(data.type);
              me.type_code(data.type);
            }
            if (typeof data.name != 'undefined') {
              me.name(data.name);
            }
            if (typeof data.code != 'undefined') {
              me.code(data.code);
            }
            if (typeof data.type_meaning != 'undefined') {
              me.type_meaning(data.type_meaning);
            }
            if (typeof data.create_time != 'undefined') {
              var date_str =  All580.DPGlobal.formatDateTime(data.create_time, 'yyyy-MM-dd HH:mm:ss');
              me.create_date(date_str);
            }
            if (typeof data.description != 'undefined') {
              me.description(data.description);
            }

          }
        }
    };
    var check_type_name = parent.app.getI18nMessage("param.type.name.not.null");
    var check_type_code = parent.app.getI18nMessage("param.type.code.not.null");
    $.validator.addMethod("typeMeaning",function(value,element){
        if(paramViewModel.paramView.way() == 'customer'){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },check_type_name);

    $.validator.addMethod("typeSelect",function(value,element){
        if(paramViewModel.paramView.way() == 'had'){
            paramViewModel.paramView.opText($(element).find("option:selected").text());
            paramViewModel.paramView.opType(value);
        }
        return true;
    },"");

    $.validator.addMethod("typeCode",function(value,element){
        if(paramViewModel.paramView.way() == 'customer'){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },check_type_code);
    var check_required = parent.app.getI18nMessage("common.check.must.field");
    var paramViewModel = new ParamViewModel();
    paramViewModel.init();
    ko.applyBindings(paramViewModel);
      //validate
      $().ready(function () {
        $("#editParam").validate({
          rules: {
                  name: {
                      required:true
                  },
                  code: {
                      required:true
                  },
                  type :{
                    typeSelect : true
                  },
                  type_code:{
                      typeCode:true
                  },
                  type_meaning:{
                      typeMeaning:true
                  }
          },
          messages:{
            name:{required:check_required},
            code:{required:check_required}
          },
          showErrors:function(errorMap,errorList) {
              this.defaultShowErrors();
              util.adjustIframeHeight();
      	  },
          submitHandler: function (form) {
              if(paramViewModel.paramView.way() == 'customer'){
                  paramViewModel.paramView.opText(paramViewModel.paramView.type_meaning());
                  paramViewModel.paramView.opType(paramViewModel.paramView.type_code());
              }
              paramViewModel.submitParam();
          }
        }
      );
      $('#goback').click(function () {
    	  self.location = 'list.html';
      });
    });
});
