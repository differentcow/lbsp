/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'userinfoService', 'commonUtil','amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers'], function ($, ko, mapping, userinfoService, util) {
    var PwdViewModel = function () {
        var me = this;
        this.oldpwd = ko.observable('');
        this.newpwd = ko.observable('');
        this.repwd = ko.observable('');
        this.code = ko.observable('');
        this.filterCode = ko.observable(false);
        //记录流程步骤
        this.step = ko.observable(1);
        //流程
        this.flow = function(step){
            if(me.step() == 3 && step == me.step()){
                return true;
            }else{
                if(step == 0 && me.step() != 3){
                    return true;
                }else if(step == me.step()){
                    return true;
                }
            }
            return false;
        };

        this.repeatAuth = function(){
            $.when(userinfoService.repeatAuth()).done(function (response) {
                if(response.code==200 && typeof response.result != 'undefined') {
                    me.step(step==2?1:(step+1));
                    if(step == 2){
                        me.oldpwd('');
                        $('#myModal').modal('toggle');
                    }
                    console.log(me.msgCobine(step)+ '正确');
                }else{
                    parent.amplify.publish('status.alerts','系统信息',response.msg);
                    console.log(response.msg);
                }
            }).fail(function (error) {
                parent.amplify.publish('status.alerts','系统信息','网络异常');
                console.log(error);
            });
        }
        //初始化
        this.init = function(){
            $.when(userinfoService.isAuthing()).done(function (response) {
                if(response.code==200 && typeof response.result != 'undefined') {
                    if(response.result == 'filter'){
                        me.filterCode(true);
                        me.step(1);
                    }else{
                        me.filterCode(false);
                        if(response.result){
                            me.step(2);
                        }else{
                            me.step(1);
                        }
                    }
                    console.log('success');
                }else{
                    parent.amplify.publish('status.alerts','系统信息',response.msg);
                    console.log(response.msg);
                }
            }).fail(function (error) {
                parent.amplify.publish('status.alerts','系统信息','网络异常');
                console.log(error);
            });
        }

        //流程事件
        this.nextEvent = function(step){
            var deferred = null;
            var param = {};
            if(step == 1){
                param.oldPassword = me.oldpwd();
                param = mapping.toJSON(param);
                deferred = userinfoService.checkPassword(param);
            }
            if(step == 2){
                param.newPassword = me.newpwd();
                param.surePassword = me.repwd();
                param = mapping.toJSON(param);
                deferred = userinfoService.updatePassword(param);
            }
            if(deferred != null){
                me.eventHandler(deferred,step);
            }
        }

        this.msgCobine = function(step){
            if(step == 1){
                return '旧密码';
            }
            if(step == 2){
                return '新密码';
            }
        }

        //事件处理
        this.eventHandler = function(deferred,step){
            $.when(deferred).done(function (response) {
                if(response.code==200 && typeof response.result != 'undefined') {
                    me.step(step==2?1:(step+1));
                    if(step == 2){
                        me.oldpwd('');
                        parent.app.refreshHome('pwd',null);
//                        $('#myModal').modal('toggle');
                    }
                    console.log(me.msgCobine(step)+ '正确');
                }else{
                    parent.amplify.publish('status.alerts','系统信息',response.msg);
                    console.log(response.msg);
                }
            }).fail(function (error) {
                parent.amplify.publish('status.alerts','系统信息','网络异常');
                console.log(error);
            });
        }

    };

    $.validator.addMethod("oldPwd",function(value,element){
        if(pwdViewModel.step() == 1){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },"旧密码不能为空");

    $.validator.addMethod("codeChk",function(value,element){
        if(pwdViewModel.step() == 2){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },"验证码不能为空");

    $.validator.addMethod("newPwd",function(value,element){
        if(pwdViewModel.step() == 2){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },"新密码不能为空");

    $.validator.addMethod("newPwdSame",function(value,element){
        if(pwdViewModel.step() == 2){
            if(pwdViewModel.newpwd() != pwdViewModel.repwd()){
                return false;
            }
        }
        return true;
    },"新密码与再次填写的密码不一致");

    $.validator.addMethod("rePwd",function(value,element){
        if(pwdViewModel.step() == 2){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },"请再次填写新密码");

    var pwdViewModel = new PwdViewModel();
    pwdViewModel.init();
    ko.applyBindings(pwdViewModel);
      //validate
      $().ready(function () {
        $("#submitSure").validate({
          rules: {
                  oldpwd: {
                      oldPwd:true
                  },
                  code: {
                      codeChk:true,
                      remote:{
                          url: All580.serverName+'/service/remote/validate/userInfo|auth',
                          type: "get",
                          dataType: "json",
                          data:{
                              code: function() {return $("#code").val();}
                          }
                      }
                  },
                  newpwd :{
                      newPwd:true,
                      min:3,
                      newPwdSame:true
                  },
                  repwd:{
                      rePwd:true,
                      min:3,
                      newPwdSame:true
                  }
              },
          messages:{
             code:{remote:'验证码已过期或填写错误.'}
          },
          showErrors:function(errorMap,errorList) {
              this.defaultShowErrors();
              util.adjustIframeHeight();
      	  },
          submitHandler: function (form) {
              pwdViewModel.nextEvent(pwdViewModel.step());
          }
        }
      );
    });
});
