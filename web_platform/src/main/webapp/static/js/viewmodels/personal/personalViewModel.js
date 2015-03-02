/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'userinfoService', 'commonUtil',
    'amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers'],
    function ($, ko, mapping, userinfoService, util) {
    var username_tip = parent.app.getI18nMessage('personal.username.check.tip');
    var sys_info = parent.app.getI18nMessage('common.sys.info');
    var sys_error = parent.app.getI18nMessage('common.sys.error');
    var submit_error = parent.app.getI18nMessage('common.sys.submit.fail');
    var PersonalViewModel = function () {
        var me = this;
        this.userView = new UserView();
       //初始化
        this.init = function () {
            me.seti18nText();
            me.loadInfo();
        };
        this.seti18nText = function(){
            $('title,p,label,span').each(function(){
                var attr = $(this).attr('i18n');
                if(attr !=null && attr != ''){
                    $(this).text(parent.app.getI18nMessage('personal.'+attr));
                }
            });
            $('#add').html(parent.app.getI18nMessage('common.btn.sureBtn'));
        };
        //加载个人信息
        this.loadInfo = function(){
          var deferred = userinfoService.me();
          $.when(deferred).done(function (response) {
            if (response.state.code == 200000 &&typeof response.result!='undefined') {
                me.userView.setData(response.result.user);
              util.adjustIframeHeight();
            } else {
              console.log('load userView failed');
              util.adjustIframeHeight();
            }
          }).fail(function (error) {
            console.log(error);
          });
        };

      //提交更改
      this.submitInfo = function () {
        var param_obj = {
        	username:me.userView.username(),
            email:me.userView.email()
        };
        param_obj = mapping.toJSON(param_obj);
        var deferred = userinfoService.modifyMe(param_obj);
        $.when(deferred).done(function (response) {
          if(response.state.code==200000 && typeof response.result != 'undefined') {
//            parent.amplify.publish('status.alerts','系统信息','更改成功');
            parent.app.refreshHome('account',{username:me.userView.username(),email:me.userView.email()});
            console.log('保存成功');
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

    //用户信息载体
    var UserView = function () {
        var me = this;
        this.id = ko.observable('');
        this.account = ko.observable('');
        this.username = ko.observable('');
        this.email = ko.observable('');
        this.create_date = ko.observable('');
        this.setEmpty = function(){
            me.id = ko.observable('');
            me.username = ko.observable('');
            me.account = ko.observable('');
            me.email = ko.observable('');
        };
        this.setData = function(data) {
          if (typeof data != 'undefined') {
            if (typeof data.id != 'undefined') {
              me.id(data.id);
            }
            if (typeof data.account != 'undefined') {
                me.account(data.account);
              }
              if (typeof data.username != 'undefined') {
                me.username(data.username);
              }
              if (typeof data.email != 'undefined') {
                me.email(data.email);
              }
            if (typeof data.create_time != 'undefined') {
              var date_str =  All580.DPGlobal.formatDateTime(data.create_time, 'yyyy-MM-dd HH:mm:ss');
              me.create_date(date_str);
            }
          }
        };
    };
    var check_required = parent.app.getI18nMessage("common.check.must.field");
    var check_email = parent.app.getI18nMessage("common.check.email");
    //验证用户名称
    $.validator.addMethod("nameCheck",function(value, element) {
            return this.optional(element) || /([a-zA-Z])|(\d)|([_])|([-])+$/.test(value) || /[^\x00-\xff]/.test(value);},
        username_tip);

    var personalViewModel = new PersonalViewModel();
    personalViewModel.init();
    ko.applyBindings(personalViewModel);
      //validate
      $().ready(function () {
        $("#sureUpdate").validate({
          rules: {
        	  username: {
                  required:true,
                  nameCheck:true
              },
              email :{
            	  required : true,
            	  email: true
              }
          },
          messages:{
              username:{required:check_required},
              email:{required:check_required,email:check_email}
          },
          showErrors:function(errorMap,errorList) {
              this.defaultShowErrors();
              util.adjustIframeHeight();
      	  },
          submitHandler: function (form) {
              personalViewModel.submitInfo();
          }
        }
      );
    });
});
