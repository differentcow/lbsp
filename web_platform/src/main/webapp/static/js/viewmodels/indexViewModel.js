/**
 * Created by Barry on 6/15/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping','moment','userService',
    'paramsService','commonUtil', 'gritter', 'amplify', 'scripts',
    'moment_locale','morris'],
    function ($, ko, mapping,moment ,userService,paramsService,util) {

  var IndexViewModel = function () {
    var self = this;
    this.sys_info;
    this.pwd_sure;
    this.personal_sure;
    this.userFunctions = ko.observableArray([]);
    this.userMenus = ko.observableArray([]);
    this.iframes = ko.observableArray([]);
    this.user = ko.observable();
    this.currentDate = ko.observable();
    this.loading = ko.observable(true);

    this.init = function () {
      self.i18n();
      //初始化首页显示的数据
      self.loadUserInfo();
      self.initIFrames();
      self.initEvents();
      self.regTabEvent();
    };

    this.refreshHome = function(type,params){
        if(type == 'pwd'){
            amplify.publish('status.alerts',self.sys_info,self.pwd_sure);
            $('a[href=#home]').tab('show');
        }else if(type == 'account'){
            self.user().user.username(params.username);
            self.user().user.email(params.email);
            amplify.publish('status.alerts',self.sys_info,self.personal_sure);
            $('a[href=#home]').tab('show');
        }else{
            location.href = All580.serverName;
        }

    }
    this.checkAuth = function(needAuth){
      if($.isArray(needAuth)){
        var result = true;
        for( var i in needAuth){
          if(self.userFunctions.indexOf(needAuth[i])==-1){
            result = false;
            break;
          }
        }
        return result;
      }else{
        return self.userFunctions.indexOf(needAuth)!=-1;
      }

    };

    this.loadedI18nFinished = ko.observable(false);
    this.seti18nText = function(){
        $('title').html(self.getI18nMessage("app.name"));
        $('.logo a p').text(self.getI18nMessage("app.name"));
        $('#switch_language').text(self.getI18nMessage("app.language"));
        $('#modify_account').text(self.getI18nMessage("personal.information.modify"));
        $('#login_out').text(self.getI18nMessage("app.login.out"));
        $('#my_index').text(self.getI18nMessage('my.index.header'));
        $('#hello').text(self.getI18nMessage('my.index.text.hello'));
        $('#today_is').text(self.getI18nMessage('my.index.text.today.is'));
        $('#right_of').text(self.getI18nMessage('my.index.text.right.of',[new Date().getYear()]));
    };
    //加载配置文件
    this.i18n = function(){
        $.when(userService.getLocale()).done(function (response) {
            if (response.state.code == 200000 &&typeof response.result!='undefined') {
                console.log('success');
                var locale = '';
                if(response.result.length >= 2){
                    locale = response.result.substring(0,2);
                    moment.locale(locale);
                    //format当前日期
                    self.currentDate(moment().format('LL'));
                }
                if(response.result.length >= 5){
                    locale = response.result.substring(0,5);
                }

                util.loadI18n(locale,function(){
                    self.seti18nText();
                    self.sys_info = self.getI18nMessage('common.sys.info');
                    self.pwd_sure = self.getI18nMessage('password.modify.success');
                    self.personal_sure = self.getI18nMessage('personal.modify.success');
                    return self.loadedI18nFinished(true);
                });
            } else {
                console.log('failed');
                util.adjustIframeHeight();
            }
        }).fail(function (error) {
            console.log(error);
        });
    }
    //切换语言
    this.changeLocale = function(locale){
        var deferred = null;
        if(!self.loadedI18nFinished()){
            deferred = userService.changeLocale(locale);
        }else if(locale != $.i18n.getLanguage()){
            deferred = userService.changeLocale(locale);
        }
        if(deferred != null){
            $.when(deferred).done(function (response) {
                if (response.state.code == 200000 &&typeof response.result!='undefined') {
                    console.log('success');
                    document.location.href = All580.serverName;
                    util.adjustIframeHeight();
                } else {
                    console.log('failed');
                    util.adjustIframeHeight();
                }
            }).fail(function (error) {
                console.log(error);
            });
        }

    }
    //获取信息
    this.getI18nMessage = function(key,array){
        if(!self.loadedI18nFinished()){
            self.i18n();
        }
        if($.isArray(array)){
            return $.i18n.prop(key,array);
        }
        return $.i18n.prop(key);
    }

    this.initEvents = function () {
        amplify.subscribe('status.alerts', function (title, content) {
            $.gritter.add({
                // (string | mandatory) the heading of the notification
                title: title,
                // (string | mandatory) the text inside the notification
                text: content,
                class_name: 'error'
            });
        });

        amplify.subscribe('status.success', function (title, content) {
            $.gritter.add({
                // (string | mandatory) the heading of the notification
                title: title,
                // (string | mandatory) the text inside the notification
                text: content,
                class_name: 'success'
            });
        });


      amplify.subscribe('page.redirect', function (currentIframe, url, targetIframe) {

        if (typeof targetIframe == 'undefined') {
          $.each(self.iframes(), function (index, iframe) {
            if (iframe.id() == currentIframe) {
              iframe.src('');
              iframe.src(url);

              return false;
            }
          });
        } else if (targetIframe == 'home') {
          if (currentIframe != 'home') {
            var removed;
            $.each(self.iframes(), function (index, iframe) {
              if (iframe.id() == currentIframe) {
                removed = iframe;
                return false;
              }
            });
            self.iframes.remove(removed);
          }
          $('a[href="#home"]').tab('show');
        } else {
          var currentFrame;
          var isExist = false;
          var ahref = '';
          $.each(self.iframes(), function (index, iframe) {
            if (iframe.id() == currentIframe) {
              currentFrame = iframe;
            }
            if (iframe.id() == targetIframe) {
              iframe.src('');
              iframe.src(url);
              isExist = true;
              iframe.isActive(true);
              ahref = iframe.ahref();
            }
          });
          if (currentFrame != null) {
            self.iframes.remove(currentFrame);
          }
          if (!isExist) {
            var func = self.findFuncByTarget(targetIframe);
            var iframe = new IFrame(func.name(), func.target(), '#' + func.target(), url, func.icon(), true);
            self.iframes.push(iframe);
            ahref = iframe.ahref();
            self.regTabEvent();
          }
          $('a[href=' + ahref + ']').tab('show');
        }

      });
    };
    this.findFuncByTarget = function (tagetName) {
      var result = null;
      $.each(self.userMenus(), function (index, func) {
        if(!$.isArray(func)) {
          if (typeof func.target() != 'undefined' && func.target() == tagetName) {
            result= func;
             return false;
          }
        }
      });
      return result;
    };
    this.initIFrames = function () {
//          self.iframes.push(new IFrame('我的主页','home','#home','user/list.html','fa-home',true));
    };

    this.loadUserInfo = function () {
      var deferred = userService.getLoginUserInfo();
      $.when(deferred).done(function (response) {

        $.each(response.funcs, function (index, func) {

          if (typeof func.type != 'undefined' && func.type == 'menu') {
            self.userMenus.push(new UserFunction(func));
          }
        });
        self.user(new User($.extend(response.userRsp,{isPlatformAdmin:typeof response.isPlatformAdmin!='undefined'?response.isPlatformAdmin:false})));
        self.userFunctions(response.auths);
        $('#loading').css('display','none');
        $('#portlet-content').css('display','block');
      }).fail(function (error) {
        if (error.state() == 'rejected') {
          window.location = 'login';
        }
        console.log(error);
      })
    };
    this.visibleSubMenuClose = function () {
      $('.menu-list').each(function () {
        var t = $(this);
        if (t.hasClass('nav-active')) {
          t.find('> ul').slideUp(200, function () {
            t.removeClass('nav-active');
          });
        }
      });
    };

    this.mainContentHeightAdjust = function () {
      // Adjust main content height
      //var docHeight = jQuery(document).height();
//      var docHeight = $(document).height();
//      console.log('doc height'+docHeight);
//      if (docHeight > $('.main-content').height()) {
//        $('.main-content').height(docHeight);
//      }

    };
    // Toggle Left Menu
    this.menuListClick = function (element) {
      var parent = $(element).parent();
      var sub = parent.find('> ul');

      if (!$('body').hasClass('left-side-collapsed')) {
        if (sub.is(':visible')) {
          sub.slideUp(200, function () {
            parent.removeClass('nav-active');
            $('.main-content').css({height: ''});
            self.mainContentHeightAdjust();
          });
        } else {
          self.visibleSubMenuClose();
          parent.addClass('nav-active');
          sub.slideDown(200, function () {
            self.mainContentHeightAdjust();
          });
        }
      }
      return false;
    };

    this.menuClick = function (parent, data) {

      var isExist = false;
      var ahref = '';
      $.each(self.iframes(), function (index, iframe) {
        //  $('li.active,div.active').removeClass('active');
        if (iframe.id() == parent.target()) {
          iframe.src('');
          iframe.src(data.url());
          iframe.isActive(true);
          ahref = iframe.ahref();
          isExist = true;
          return false;
        }
      });

      if (!isExist) {

        var iframe = new IFrame(parent.name(), parent.target(), '#' + parent.target(), data.url(), parent.icon(), true);
        self.iframes.push(iframe);
        ahref = iframe.ahref();
        self.regTabEvent();
      }
      $('a[href=' + ahref + ']').tab('show');

    };
    this.closeIframe = function (data) {
      if (data.isActive()) {
        var index = self.iframes.indexOf(data);
        if (index - 1 >= 0) {
          $('a[href=' + self.iframes()[index - 1].ahref() + ']').tab('show');
          self.iframes()[index - 1].isActive(true);
        } else {
          $('a[href="#home"]').tab('show');
        }
      }
      self.iframes.remove(data);
    };
    this.regTabEvent = function () {
      $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        $.each(self.iframes(), function (index, iframe) {
          if (iframe.ahref() == $(e.target).attr('href')) {
            iframe.isActive(true);
          } else if (iframe.ahref() == $(e.relatedTarget).attr('href')) {
            iframe.isActive(false);
          }
        });

      })
    };

    this.showProfile = function () {
      var parent = {name: ko.observable('帐户资料修改'), target: ko.observable('personalfileFrame'), icon: ko.observable('fa-pencil')};
      var data = {url: ko.observable('personal/info.html')};
      self.menuClick(parent, data);

    };
  };

  var IFrame = function (name, id, ahref, src, aicon, isActive) {
    this.name = ko.observable(name);
    this.id = ko.observable(id);
    this.src = ko.observable(src);
    this.ahref = ko.observable(ahref);
    this.aicon = ko.observable(aicon);
    this.isActive = ko.observable(isActive)
  };

  var UserFunction = function (data) {
    var mappingUpdate = {
      'url': {
        update: function (options) {
          return (typeof options.data !== "undefined" && options.data != "" ? All580.serverName + options.data : "");
        }
      }
    };

    mapping.fromJS(data, mappingUpdate, this);
  };

  var User = function(data){
    mapping.fromJS(data,{}, this);
  };

    var Detail = function(){
        this.total = ko.observable();
        this.free = ko.observable();
        this.max = ko.observable();
        this.usable = ko.observable();
        this.time = ko.observable();
        this.active = ko.observable();
        this.freeze = ko.observable();
        this.data = ko.observableArray([]);
        var me = this;
        this.init = function(data){
            if(typeof data != 'undefined'){
                if(typeof data.total != 'undefined'){
                    me.total(data.total);
                }
                if(typeof data.active != 'undefined'){
                    me.active(data.active);
                }
                if(typeof data.free != 'undefined'){
                    me.free(data.free);
                }
                if(typeof data.max != 'undefined'){
                    me.max(data.max);
                }
                if(typeof data.usable != 'undefined'){
                    me.usable(data.usable);
                }
                if(typeof data.time != 'undefined'){
                    me.time(data.time);
                }
                if(typeof data.freeze != 'undefined'){
                    me.freeze(data.freeze);
                }
                if(typeof data.data != 'undefined'){
                    me.data(data.data);
                }
            }
        }
    };

  var indexViewModel = new IndexViewModel();
  indexViewModel.init();
  window.app = indexViewModel;
  ko.applyBindings(indexViewModel);
});