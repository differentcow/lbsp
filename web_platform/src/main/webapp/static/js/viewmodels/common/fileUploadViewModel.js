/**
 * Created by Barry on 6/16/2014.
 */
requirejs([
        'jquery','knockout','knockout-mapping','fileUploadService','commonUtil','jquery-validate',
        'bootstrap','validateMsg','additional-methods','bootstrap-fileupload','amplify'],
    function ($, ko, mapping, fileUploadService,util) {
        var FileUploadViewModel = function () {
            var me = this;
            this.default_pic = ko.observable('');
            this.init = function(){
                $('#dismiss').trigger('click.fileupload');
                me.getIDFromUrl();
            };
            this.ext = ko.observable('');
            this.resultSrc = ko.observable('');
            this.winId = ko.observable();
            this.resultId = ko.observable();
            this.dataObject = ko.observable();
            this.forward = ko.observable();
            this.getIDFromUrl = function(){
                var url = window.location.search;
                var index = url.indexOf('?');
                if(index != -1){
                    var param = url.substring(index+1,url.length);
                    var ary = param.split("&");
                    var obj = {};
                    for(var i = 0;i < ary.length;i++){
                        if(ary[i].indexOf('forward=') != -1){
                            me.forward(ary[i].split('=')[1]);
                        }else if(ary[i].indexOf('winId=') != -1){
                            me.winId(ary[i].split('=')[1]);
                        }else if(ary[i].indexOf('resultId=') != -1){
                            me.resultId(ary[i].split('=')[1]);
                        }else if(ary[i].indexOf('ext=') != -1){
                            me.ext(ary[i].split('=')[1]);
                        }else if(ary[i].indexOf('resultSrc=') != -1){
                            me.resultSrc(ary[i].split('=')[1]);
                        }else if(ary[i].indexOf('=') != -1){
                            var keyVal = ary[i].split('=');
                            if(keyVal.length == 2){
                                obj[keyVal[0]] = keyVal[1];
                            }
                        }
                    }
                    me.dataObject(obj);
                }
            };

            this.uploadFile = function(){
              if($('#wxpic').val()!='') {
            	$("#sure_btn").attr("disabled","disabled");
                fileUploadService.upload('wxpic',me.forward(),me.dataObject(),me.uploadSuccess, me.uploadFailed);
              }else{
                  parent.amplify.publish('status.alerts','请选择要上传的图片','系统信息');
              }
            };
            this.uploadSuccess = function(data,satus){
              if(data.state.code==200000) {
                  var resultSrc = parent.document.getElementById(me.resultSrc());
                  if(typeof resultSrc != 'undefined'){
                      $(resultSrc).attr('src',All580.imgBaseUrl+data.result);
                  }
                  var result = parent.document.getElementById(me.resultId());
                  if(typeof result != 'undefined'){
                      $(result).val(data.result);
                  }
                  var win = parent.document.getElementById(me.winId());
                  if(typeof win != 'undefined'){
                      $(win).trigger('click');
                  }
//                  parent.window.adjustParentHeight();
                  util.adjustIframeParentHeight();
              }else{
                parent.amplify.publish('status.alerts','上传图片失败',data.msg);
                console.log('upload failed code '+data.code);
                $("#sure_btn").removeAttr("disabled");
              }
            };

            this.uploadFailed = function(data,status, e){
              parent.amplify.publish('status.alerts','上传图片失败','系统错误');
            };
        };

        var model = new FileUploadViewModel();
        model.init();
        var ext = model.ext();
        ko.applyBindings(model);

        //validate
        $(document).ready(function () {

            $('#close_btn').click(function(){
                var win = parent.document.getElementById(model.winId());
                if(typeof win != 'undefined'){
                    $(win).trigger('click');
                }
            });

//            $('#close_btn').click(function(){$('#myModal').modal('toggle');});

            $("#theForm").validate({
                rules: {
                  uploadFile : {
                      extension: function(){return model.ext();}
                  }
                },
                submitHandler: function (form) {
                    model.uploadFile();
                },
                showErrors:function(errorMap,errorList) {
                    this.defaultShowErrors();
                    util.adjustIframeHeight();
            	}
            });
        });
    });