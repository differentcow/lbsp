/**
 * Created by Zale on 6/16/2014.
 */
define(['jquery','knockout','amplify','jquery-i18n'],function($,ko){

  var CommonUtil = function() {
    this.trim = function(str){
        return str.replace(/(^\s*)|(\s*$)/g, "");
    };
    this.adjustIframeHeight = function (h) {
      if (parent != null && typeof parent != 'undefined') {
        //console.log(parent.$('div.active iframe').height());
        //console.log($(document).height());
        var _h = h==null?632:h;
        if($('body').height()<_h){
          var height = parent.window.innerHeight-166<_h?_h:parent.window.innerHeight-166;
          parent.$('div.active iframe').height(height);
        }else{
          parent.$('div.active iframe').height($('body').height());
        }

      }
    };
    this.getQueryString = function(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return decodeURIComponent(r[2]); return null;
    };
    this.combinProdName = function(prodName, prodSubName, salePlanName) {
		return prodName
				+ (!prodSubName ? '' : ' - ' + prodSubName)
				+ (!salePlanName ? '' : '(' + salePlanName + ')');
	};
    this.sysLocal = function(){
        return $.i18n.browserLang().substring(0, 2);
    };

    this.loadI18n = function(locale,func){
        var url = All580.serverName+'/static/i18n/';
        if(document.location.pathname.indexOf("login") != -1){
            url = '../static/i18n/';
        }
        $.i18n.properties({// 加载资浏览器语言对应的资源文件
            name:'messages', // 资源文件名称
            language:locale==null?'':locale,//配置语言
            path:url, // 资源文件路径
            mode:'map', // 用 Map 的方式使用资源文件中的值
            callback: typeof func == 'function'?func:null// 加载成功后设置显示内容
        });
    };
    this.registerInputSelect = function(inputElementId,loadOptions){
        $('#'+inputElementId+' input').keydown(function(event){
          if(event.keyCode!=40&&event.keyCode!=38) {
            var options = loadOptions;
            if (options.length > 0) {
              $('#' + inputElementId + ' ul').html('');
              $.each(options, function (index, option) {
                var li = '<li>' + option + '</li>';
                $('#' + inputElementId + ' ul').append(li);
                $('#'+inputElementId+' .input-select-list').show();
              });
              $('#'+inputElementId+' ul li').mouseover(function(){
                $(this).addClass('selected');
              });

              $('#'+inputElementId+' ul li').mouseout(function(){
                $(this).removeClass('selected');
              });
              $('#'+inputElementId+' ul li').click(function(){
                var val = $(this).html();
                $('#'+inputElementId+' input').val(val);
                $('#'+inputElementId+' .input-select-list').hide();
              });
            }
          }

        });

        $('#'+inputElementId).keydown(function(event){
          var currObj = $('#'+inputElementId+' ul li.selected');
          if(event.keyCode==40){
            if(typeof currObj.html() == 'undefined'){
              currObj = $('#'+inputElementId+' ul li').first().addClass('selected');
            }else {

              if(typeof currObj.next().html() != 'undefined') {
                currObj.removeClass('selected');
                currObj.next().addClass('selected');

                var val = currObj.next().html();
                $('#'+inputElementId+' input').val(val);
              }
            }
          }else if(event.keyCode == 38){
            if(typeof currObj.html() != 'undefined'){
              if(typeof currObj.prev().html() != 'undefined') {
                currObj.removeClass('selected');
                currObj.prev().addClass('selected');
                var val = currObj.prev().html();
                $('#'+inputElementId+' input').val(val);

              }
            }
          }

        });
      $('body').click(function(){
        $('#'+inputElementId+' .input-select-list').hide();
      });
    }
  };
  var commonUtil = new CommonUtil();
  $(document).ready(function(){
    commonUtil.adjustIframeHeight();
    $(window).resize(function(){
      commonUtil.adjustIframeHeight();
    });
    amplify.subscribe('iframe.resize',function(){
      commonUtil.adjustIframeHeight();
    })
    var url = window.location.pathname;
    //console.log(url);
    if(url.indexOf('login')!=-1&&window.frameElement!=null){
      parent.window.location=window.location;
    }

    $(document).click(function(){
      if(parent&&parent.$('.dropdown-toggle').length>0) {
        parent.$('.dropdown-toggle').dropdown('close');
      }
    });
  });

  return commonUtil;
});