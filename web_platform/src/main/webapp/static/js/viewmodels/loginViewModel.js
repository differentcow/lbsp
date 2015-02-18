requirejs(['jquery','knockout','loginService','commonUtil',"jquery-validate","jquery-cookie",'commonUtil'],function($,ko,loginService,util){


  var LoginViewModel = function(){
    var self = this;
    this.error = ko.observable('');
    this.hasError = ko.observable(false);
    this.userName = ko.observable('');
    this.password = ko.observable('');
    this.validateCode = ko.observable('');
    this.rememberPWD = ko.observable(false);
    this.expiryTime={};
    this.loadValidateCode = function(){
      $('#validateCodeSrc > img').attr('src','../service/validateCode/generate?jsessionId='+Math.random())
    };
//加载配置文件
      this.i18n = function(){
          util.loadI18n('',null);
      }
    this.setExpiryTime = ko.computed(function() {
      if (self.rememberPWD()) {
        self.expiryTime = {path: '/', expires: 14};
      } else {
        self.expiryTime = {path: '/'};
      }
    });

    this.checkErrorMsg = ko.computed(function() {
      var error = util.getQueryString('error');
      if(error!=null){
        self.error(error);
        self.hasError(true);
      }
    });
    this.checkCookie =  ko.computed(function() {
      var remember = eval($.cookie('remember'));
      if(remember){
        //self.rememberPWD(true);
      }
    });
    self.userName.subscribe(function(){
      var pwd = eval($.cookie(self.userName()));
      if(pwd){
        self.password(pwd);
      }
    });
    self.rememberPWD.subscribe(function(){
      if(!self.rememberPWD()){
        self.removeCookies();
      }
    });
    this.login = function(){

      if(self.validate()) {
        if(self.rememberPWD()){
          $.cookie(self.userName(), self.password(), self.expiryTime);
        }
        $.cookie('remember',self.rememberPWD(),self.expiryTime);
        $('#form-signin').submit();
      }
    };


    this.removeCookies = function() {
      $.cookie(self.userName(), null, self.expiryTime);
    };
    this.validate = function(){
      return true;
    }
 }

 var loginViewModel = new LoginViewModel();
 ko.applyBindings(loginViewModel);
 loginViewModel.i18n();


  //validate
  $().ready(function() {
    $("#form-signin").validate({
      rules: {
        userName: {
          required: true
        },
        password: {
          required: true,
          minlength: 3
        },
        validateCode: {
          required: true
        }
      },
      messages: {
        userName:  "请输入用户名",
        password: {
          required: "请输入密码",
          minlength: "密码最少为3位字符"
        },
        validateCode:  "请输入验证码"
      }
    });
  });
});
