/**
 * Created by Barry on 6/16/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'userinfoService', 'commonUtil',
        'amplify', 'jquery-validate','bootstrap', 'validateMsg','knockout-amd-helpers'],
    function ($, ko, mapping, userinfoService, util) {
    var old_tip = parent.app.getI18nMessage('password.old.not.null');
    var new_tip = parent.app.getI18nMessage('password.new.not.null');
    var code_tip = parent.app.getI18nMessage('password.code.not.null');
    var old_new_not_same = parent.app.getI18nMessage('password.new.old.not.same');
    var old_label = parent.app.getI18nMessage('password.old.label');
    var new_label = parent.app.getI18nMessage('password.new.label');
    var again_tip = parent.app.getI18nMessage('password.again.tip');
    var sys_info = parent.app.getI18nMessage('common.sys.info');
    var sys_error = parent.app.getI18nMessage('common.sys.error');
    var submit_error = parent.app.getI18nMessage('common.sys.submit.fail');
    var code_error = parent.app.getI18nMessage('password.auth.code.error');
    var correct_label = parent.app.getI18nMessage('common.right.label');
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
                if(response.state.code==200000 && typeof response.result != 'undefined') {
                    me.step(step==2?1:(step+1));
                    if(step == 2){
                        me.oldpwd('');
                        $('#myModal').modal('toggle');
                    }
                    console.log(me.msgCobine(step)+ correct_label);
                }else{
                    parent.amplify.publish('status.alerts',sys_info,submit_error);
                    console.log(response.msg);
                }
            }).fail(function (error) {
                parent.amplify.publish('status.alerts',sys_info,sys_error);
                console.log(error);
            });
        }
        this.seti18nText = function(){
            $('title,label,a,span,legend').each(function(){
                var attr = $(this).attr('i18n');
                if(attr !=null && attr != ''){
                    $(this).text(parent.app.getI18nMessage('password.'+attr));
                }
            });
            $('#myModalLabel').text(parent.app.getI18nMessage('password.modify.success'));
            $('#next').html(parent.app.getI18nMessage('common.btn.nextBtn'));
            $('#sure').html(parent.app.getI18nMessage('common.btn.sureBtn'));
            $('button[i18n=nospaceclose]').html(parent.app.getI18nMessage('common.btn.nospace.closeBtn'));
        };
        //初始化
        this.init = function(){
            me.seti18nText();
            $.when(userinfoService.isAuthing()).done(function (response) {
                if(response.state.code==200000 && typeof response.result != 'undefined') {
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
                    parent.amplify.publish('status.alerts',sys_info,submit_error);
                    console.log(response.msg);
                }
            }).fail(function (error) {
                parent.amplify.publish('status.alerts',sys_info,sys_error)
                console.log(error);
            });
        };

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
                return old_label;
            }
            if(step == 2){
                return new_label;
            }
        }

        //事件处理
        this.eventHandler = function(deferred,step){
            $.when(deferred).done(function (response) {
                if(response.state.code==200000 && typeof response.result != 'undefined') {
                    me.step(step==2?1:(step+1));
                    if(step == 2){
                        me.oldpwd('');
                        parent.app.refreshHome('pwd',null);
//                        $('#myModal').modal('toggle');
                    }
                    console.log(me.msgCobine(step)+ correct_label);
                }else{
                    parent.amplify.publish('status.alerts',sys_info,submit_error);
                    console.log(response.msg);
                }
            }).fail(function (error) {
                parent.amplify.publish('status.alerts',sys_info,sys_error)
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
    },old_tip);

    $.validator.addMethod("codeChk",function(value,element){
        if(pwdViewModel.step() == 2){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },code_tip);

    $.validator.addMethod("newPwd",function(value,element){
        if(pwdViewModel.step() == 2){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },new_tip);

    $.validator.addMethod("newPwdSame",function(value,element){
        if(pwdViewModel.step() == 2){
            if(pwdViewModel.newpwd() != pwdViewModel.repwd()){
                return false;
            }
        }
        return true;
    },old_new_not_same);

    $.validator.addMethod("rePwd",function(value,element){
        if(pwdViewModel.step() == 2){
            if(value == null || value == ''){
                return false;
            }
        }
        return true;
    },again_tip);
    var check_min = parent.app.getI18nMessage("common.check.min",[3]);
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
             code:{remote:code_error},
             newpwd:{min:check_min},
             repwd:{min:check_min}
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
