requirejs(['jquery','knockout','loginService','commonUtil',"jquery-validate",
    "jquery-cookie",'commonUtil'],function($,ko,loginService,util){


  var LoginViewModel = function(){
    var self = this;
    this.error = ko.observable('');
    this.hasError = ko.observable(false);
    this.userName = ko.observable('');
    this.password = ko.observable('');
    this.validateCode = ko.observable('');
    this.rememberPWD = ko.observable(false);
    this.expiryTime={};
    this.check_pwd = '';
    this.check_account = '';
    this.check_min = '';
    this.check_code = '';
    this.text_account = '';
    this.text_pwd = '';
    this.text_code = '';
      /*this.server_account = '';
      this.server_code = '';
      this.server_certify = '';
      this.server_pwd = '';
      this.server_post = '';*/
    this.loadValidateCode = function(){
      $('#validateCodeSrc > img').attr('src','../service/validateCode/generate?jsessionId='+Math.random())
    };

    //加载配置文件
    this.i18n = function(){
        util.loadI18n('',null);
        self.text_account = $.i18n.prop('login.account');
        self.text_pwd = $.i18n.prop('login.password');
        self.text_code = $.i18n.prop('login.auth.code');
        self.check_account = $.i18n.prop('login.check.username');
        self.check_pwd = $.i18n.prop('login.check.password');
        self.check_min = $.i18n.prop('login.check.password.minlength',[3]);
        self.check_code = $.i18n.prop('login.check.validateCode');
        /*self.server_account = $.i18n.prop('login.error.account.null');
        self.server_code = $.i18n.prop('login.error.valid.code');
        self.server_certify = $.i18n.prop('login.error.certify');
        self.server_post = $.i18n.prop('login.error.post');
        self.server_pwd = $.i18n.prop('login.error.password.null');*/
        $('title,h3,span').each(function(){
            var attr = $(this).attr('i18n');
            if(attr !=null && attr != ''){
                $(this).text($.i18n.prop(attr));
            }
        });
        $('#userName').attr('placeholder',self.text_account);
        $('#password').attr('placeholder',self.text_pwd);
        $('#validateCode').attr('placeholder',self.text_code);
    };
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
        util.loadI18n('',null);
        self.error($.i18n.prop(error));
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
        userName:  loginViewModel.check_account,
        password: {
          required: loginViewModel.check_pwd,
          minlength: loginViewModel.check_min
        },
        validateCode: loginViewModel.check_code
      }
    });
  });
});
