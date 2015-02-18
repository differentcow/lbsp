/**
 * Created by Barry.
 */
requirejs(['jquery','knockout','paramsService','taskService','commonUtil', 'knockout-mapping','knockout-amd-helpers','amplify','DT-bootstrap','bootstrap'],
		function($,ko,paramsService,taskService,util,mapping){
  var TaskListViewModel = function() {

    var ep = this;
    this.task_name = ko.observable('');
    this.task_status = ko.observable('');
    this.statusOptions = ko.observableArray([]);
    this.params = new Array();
    this.paramAdd = 'addtask';
    this.paramDel = 'deltask';
    this.paramModify = 'modifytask';
    this.canAdd = ko.observable(false);
    this.canModify = ko.observable(false);
    this.canDel = ko.observable(false);
    this.isEdit = ko.observable(false);
    this.isAdd = ko.observable(false);

    this.handlerServer = function(deferred,func){
        $.when(deferred).done(function (response) {
            if (response.state.code == 200 && typeof response.result != 'undefined') {
                if(typeof func == 'function'){
                    func(response.result);
                }
            } else {
                console.log('load failed');
            }
        }).fail(function (error) {
            console.log(error);
        });
    }

    //Start Crone Express
    this.pointShow = ko.observable(true);
    this.periodShow = ko.observable(false);
    this.customerShow = ko.observable(false);
    this.unit_choose = ko.observable('');
    this.hour_choose = ko.observable('');
    this.min_choose = ko.observable('');
    this.sec_choose = ko.observable('');
    this.cron_choose = ko.observable('');
    this.cronOptions = ko.observableArray([]);
    this.hourOptions = ko.observableArray([]);
    this.minOptions = ko.observableArray([]);
    this.secOptions = ko.observableArray([]);
    this.unitOptions = ko.observableArray([]);
    this.emptyCron = function(){
        $('input[name=date_choose]').each(function(){
           $(this).prop('checked',false);
        });
        ep.task_class('');
        ep.add_task_name('');
        ep.hour_choose('');
        ep.min_choose('');
        ep.sec_choose('');
        ep.unit_choose('');
        ep.cron_choose('point');
        ep.pointShow(true);
        ep.periodShow(false);
        ep.customerShow(false);
    };
    this.initCronExpress = function(){
        ep.hourOptions([]);
        ep.minOptions([]);
        ep.secOptions([]);
        ep.unitOptions([{code:'d',name:'天'},{code:'h',name:'时'},{code:'m',name:'分'},{code:'s',name:'秒'}]);
        ep.cronOptions([{code:'point',name:'按时间点'},{code:'period',name:'按时间段'},{code:'customer',name:'自定义'}]);
        ep.cron_choose('point');
        for(var i = 0;i < 60;i++){
            if(i < 24){
                ep.hourOptions.push({code:i,name:i<10?'0'+i:i});
            }
            if(i < 60){
                ep.minOptions.push({code:i,name:i<10?'0'+i:i});
                ep.secOptions.push({code:i,name:i<10?'0'+i:i});
            }
        }
    };
      this.chooseCron = function(){
          var val = $('#cron_choose').val();
          ep.cron_choose(val);
          if(val == 'point'){
              ep.pointShow(true);
              ep.periodShow(false);
              ep.customerShow(false);
          }else if(val == 'customer'){
              ep.pointShow(false);
              ep.periodShow(false);
              ep.customerShow(true);
          }else if(val == 'period'){
              ep.pointShow(false);
              ep.periodShow(true);
              ep.customerShow(false);
          }
      };
    //End Crone Express
    this.initAuth = function () {
      if (parent.app.checkAuth(ep.paramAdd)) {
          ep.canAdd(true);
      }
      if (parent.app.checkAuth(ep.paramDel)) {
          ep.canDel(true);
      }
      if (parent.app.checkAuth(ep.paramModify)) {
          ep.canModify(true);
      }
    };

    this.init = function () {
        ep.initAuth();
        ep.loadStatusList();
        ep.initTasks();
        ep.initCronExpress();
    };

    this.loadStatusList = function(){
        ep.handlerServer(paramsService.getParamByType('TASK_STATUS'),function(data){
            ep.statusOptions([]);
            ep.statusOptions(data);
        });
    };

    this.datatable = ko.observable();

    this.setUpTable = function () {
      var settings = All580.dataTable;
      settings['sAjaxSource'] = All580.serverName + '/service/core/task|lst';
      settings['fnServerParams'] = function (aoData) {
        aoData.push({ "name": "task_name", "value": ep.params['task_name']});
        aoData.push({ "name": "task_status", "value": ep.params['task_status']});
      };
      settings['fnDrawCallback'] = function(){
    	  util.adjustIframeHeight();
      };
      settings['aoColumns'] = [
        { 'mData': 'task_name','sDefaultContent': ''}, //任务名称
        { 'mData': 'task_content', 'sDefaultContent': ''}, //工作内容
        { 	//状态
            'sDefaultContent': '',
            'fnRender': function (obj) {
                if(obj.aData.task_status == 'NORMAL'){
                    return '<span class="label label-info">正常</span>';
                }
                if(obj.aData.task_status == 'PAUSE'){
                    return '<span class="label label-warning">暂停</span>';
                }
                if(obj.aData.task_status == 'RUNNING'){
                    return '<span class="label label-success">运行中</span>';
                }
            }
        },
		{ 	//策略
        	'sDefaultContent': '',
        	'fnRender': function (obj) {
                if(typeof obj.aData.cron_text == 'undefined'){
                    return obj.aData.cron_expression;
                }else{
                    return obj.aData.cron_text;
                }
        	}
		},
		{ 	//操作
        	'sDefaultContent': '',
        	'fnRender': function (obj) {
                if(obj.aData.task_status == 'RUNNING'){
                    return "正在运行中";
                }
                var html = '';
                if(ep.canDel()){
                    html += '&nbsp;&nbsp;<a href="#" onclick="handlerTaskEvent(\'del\',\''+obj.aData.id+'\');" >删除</a>';
                }
                if(ep.canModify()) {
                    if (obj.aData.task_status == 'NORMAL') {
                        html += '&nbsp;&nbsp;<a href="#" onclick="handlerTaskEvent(\'pause\',\''+obj.aData.id+'\');" >暂停</a>';
                    }else if(obj.aData.task_status == 'PAUSE'){
                        html += '&nbsp;&nbsp;<a href="#" onclick="handlerTaskEvent(\'restore\',\''+obj.aData.id+'\');" >恢复</a>';
                    }
                    html += '&nbsp;&nbsp;<a href="#" onclick="handlerTaskEvent(\'upt\',\''+obj.aData.id+'\');" >修改策略</a>';
                    html += '&nbsp;&nbsp;<a href="#" onclick="handlerTaskEvent(\'run\',\''+obj.aData.id+'\');" >只运行一次</a>';
                }
                return html;
            }
		}
      ];
      var dataTable = $('#dynamic-table').dataTable(settings);
      ep.datatable(dataTable);
      $('#search').click(function() {
    	  ep.params['task_name']=ep.task_name();
    	  ep.params['task_status']=ep.task_status();
          dataTable.fnDraw();
      });

    };
    this.del = function (type,id) {
        if(type == 0){
            $('#warnModal').css('display','block');
            $('#editModal').css('display','none');
            $('#warnModalLabel').css('display','block');
            $('#editModalLabel').css('display','none');
            $('#myModal').modal('toggle');
        }else{
            ep.handlerServer(taskService.del(id),function(){
                console.log('删除成功');
                ep.params['task_name']=ep.task_name();
                ep.params['task_status']=ep.task_status();
                ep.datatable().fnDraw();
            });
        }
    };
      this.add = function () {
          if(ep.add_task_name() == null || ep.add_task_name() == ''){
              parent.amplify.publish('status.alerts','系统信息','工作名称不能为空.');
              return;
          }
          var chkObj = ep.checkCron();
          if(chkObj.ok == 0)return;
          if(chkObj.cron != ''){
              ep.cron_expression(chkObj.cron);
          }
          var param = {
              task_name:ep.add_task_name(),
              cron_expression:ep.cron_expression(),
              cron_text:chkObj.text,
              task_class:ep.task_class()
          };
          param = mapping.toJSON(param);
          ep.handlerServer(taskService.add(param),function(data){
              if(data){
                  ep.params['task_name']=ep.task_name();
                  ep.params['task_status']=ep.task_status();
                  ep.datatable().fnDraw();
                  $('#myModal').modal('toggle');
              }else{
                  parent.amplify.publish('status.alerts','系统信息','已存在相同的工作任务.');
                  return;
              }
              console.log('操作成功');
          });
      };
      this.pause = function (id) {
          ep.handlerServer(taskService.pause(id),function(){
              console.log('操作成功');
              ep.params['task_name']=ep.task_name();
              ep.params['task_status']=ep.task_status();
              ep.datatable().fnDraw();
          });
      };
      this.restore = function (id) {
          ep.handlerServer(taskService.restore(id),function(){
              console.log('操作成功');
              ep.params['task_name']=ep.task_name();
              ep.params['task_status']=ep.task_status();
              ep.datatable().fnDraw();
          });
      };

      this.initTasks = function(){
          ep.handlerServer(taskService.getTasks(),function(data){
              console.log('操作成功');
              ep.contextOptions([]);
              ep.contextOptions(data);
          });
      }
      this.add_task_name = ko.observable('');
      this.contextOptions = ko.observableArray([]);
      this.task_content = ko.observable('');
      this.task_class = ko.observable('');
      this.cron_expression = ko.observable('');
      this.period_time = ko.observable('');
      this.modify = function (type,id) {
          if(type == 0){
              ep.handlerServer(taskService.getTaskQueueById(id),function(data){
                  console.log('操作成功');
                  ep.add_task_name(data.task_name);
                  ep.task_class(data.task_class);
                  ep.cron_expression(data.cron_expression);
                  ep.task_content(data.task_content);
                  ep.isAdd(false);
                  ep.isEdit(true);
                  if(typeof data.cron_text != 'undefined'){
                      var ary = data.cron_text.split('-');
                      ep.cron_choose('point');
                      ep.pointShow(true);
                      ep.periodShow(false);
                      ep.customerShow(false);
                      if(ary.length >= 2){
                          var express = data.cron_expression.split(' ');
                          var week = express[5].split(',');
                          ep.hour_choose(express[2]);
                          ep.min_choose(express[1]);
                          ep.sec_choose(express[0]);
                          $('input[name=date_choose]').each(function(){
                              for(var i = 0; i < week.length; i++){
                                  if(week[i] == $(this).prop('value')){
                                      $(this).prop('checked',true);
                                  }
                              }
                          });
                      }else{
                          ep.cron_choose('period');
                          ep.pointShow(false);
                          ep.periodShow(true);
                          ep.customerShow(false);
                          var express = data.cron_expression.split(' ');
                          var tmp = {ok:0};
                          for(var i= 0;i < express.length;i++){
                              if(express[i].indexOf('/') != -1){
                                  tmp.key = i;
                                  var cnt = express[i].indexOf('/');
                                  tmp.val = express[i].substr(cnt+1,1);
                                  tmp.ok = 1;
                                  break;
                              }
                          }
                          if(tmp.ok == 1){
                              ep.unit_choose(tmp.key==0?'s':tmp.key==1?'m':tmp.key==2?'h':'d');
                              ep.period_time(tmp.val);
                          }else{
                              ep.unit_choose('');
                              ep.period_time('');
                          }
                      }
                  }else{
                      ep.cron_choose('customer');
                      ep.pointShow(false);
                      ep.periodShow(false);
                      ep.customerShow(true);
                  }
                  $('#warnModal').css('display','none');
                  $('#editModal').css('display','block');
                  $('#warnModalLabel').css('display','none');
                  $('#editModalLabel').css('display','block');
                  $('#myModal').modal('toggle');
              });
          }else{
              var chkObj = ep.checkCron();
              if(chkObj.ok == 0)return;
              if(chkObj.cron != ''){
                  ep.cron_expression(chkObj.cron);
              }
              var param = {
                  task_name:ep.add_task_name(),
                  cron_text:chkObj.text,
                  task_class:ep.task_class(),
                  cron_expression : ep.cron_expression()
              };
              param = mapping.toJSON(param);
              ep.handlerServer(taskService.modify(param,id),function(data){
                  console.log('操作成功');
                  ep.params['task_name']=ep.task_name();
                  ep.params['task_status']=ep.task_status();
                  ep.datatable().fnDraw();
                  $('#myModal').modal('toggle');
              });
          }

      };
      this.checkCron = function(){
          var cron_epress = '',cron_text='';
          if(ep.cron_choose() == 'point'){
              var flag = false;
              var week = '';
              $('input[name=date_choose]').each(function(){
                  if($(this).prop('checked')){
                      flag = true;
                      week += ','+$(this).prop('value');
                      var id = $(this).attr('text');
                      cron_text += ','+$('#'+id).text();
                  }
              });
              if(flag){
                  ep.sec_choose($('#sec').val());
                  ep.min_choose($('#min').val());
                  ep.hour_choose($('#hour').val());
                  cron_epress = ep.sec_choose()+' '+ep.min_choose()+' '+ep.hour_choose();
                  cron_epress += ' ? * ' + week.substring(1);
                  cron_text = cron_text.substring(1) + '-' + (parseInt(ep.hour_choose())<10?'0' + ep.hour_choose():ep.hour_choose()) + '时'
                            + (parseInt(ep.min_choose())<10?'0'+ep.min_choose():ep.min_choose()) + '分'
                            + (parseInt(ep.sec_choose())<10?'0'+ep.sec_choose():ep.sec_choose()) + '秒';
              }else{
                  parent.amplify.publish('status.alerts','系统信息','请选择周时间.');
                  return {ok:0};
              }
          }
          if(ep.cron_choose() == 'period'){
              if(ep.period_time() == null || $.trim(ep.period_time()) == ''){
                  parent.amplify.publish('status.alerts','系统信息','时间间隔不能为空.');
                  return {ok:0};
              }
              ep.unit_choose($('#unit').val());
              if(ep.unit_choose() == 'h' && !/^([1-9]|1[0-9]|2[0-3])$/.test(ep.period_time())){
                  parent.amplify.publish('status.alerts','系统信息','小时数不能超过23.');
                  return {ok:0};
              }
              if(ep.unit_choose() == 'm' && !/^([1-9]|[1-5][0-9])$/.test(ep.period_time())){
                  parent.amplify.publish('status.alerts','系统信息','分钟数不能超过59.');
                  return {ok:0};
              }
              if(ep.unit_choose() == 's' && !/^([1-9]|[1-5][0-9])$/.test(ep.period_time())){
                  parent.amplify.publish('status.alerts','系统信息','秒数不能超过59.');
                  return {ok:0};
              }
              var tmp = ep.unit_choose();
              var val = ep.period_time();
              cron_epress = (tmp=='s'?'0/'+val:'0')+' '+(tmp=='m'?'0/'+val:'0')+' '+(tmp=='h'?'0/'+val:'0') + ' '+(tmp=='d'?'0/'+val:'?')+' * *';
              cron_text = '时间间隔:'+val+(tmp=='s'?'秒':tmp=='m'?'分':tmp=='h'?'小时':'天');
          }
          return {cron:cron_epress,text:cron_text};
      };
      this.cmdType = ko.observable('');
      this.taskId = ko.observable('');
      this.run = function (id) {
          ep.handlerServer(taskService.run(id),function(){
              console.log('操作成功');
              ep.params['task_name']=ep.task_name();
              ep.params['task_status']=ep.task_status();
              ep.datatable().fnDraw();
          });
      };
  };

  var epList = new TaskListViewModel();
  epList.init();
  ko.applyBindings(epList);

  window.handlerTaskEvent = function(type,id){
      epList.cmdType(type);
      epList.taskId(id);
      if(type == 'del') {
          return epList.del(0, id);
      }
      if(type == 'upt') {
          epList.emptyCron();
          return epList.modify(0, id);
      }
      if(type == 'run') {
          return epList.run(id);
      }
      if(type == 'restore') {
          return epList.restore(id);
      }
      if(type == 'pause') {
          return epList.pause(id);
      }
  };

  $(document).ready(function(){
	  epList.setUpTable();
      $('#sure_btn').click(function() {

          if (epList.cmdType() == 'del'){
              epList.del(1,epList.taskId());
          }else if(epList.cmdType() == 'upt') {
              epList.modify(1,epList.taskId());
          }else{
              epList.add(1,epList.taskId());
          }
      });

      //新增事件
      $('#add').click(function(e){
          epList.isAdd(true);
          epList.isEdit(false);
          epList.cmdType('add');
          epList.emptyCron();
          $('#warnModal').css('display','none');
          $('#editModal').css('display','block');
          $('#warnModalLabel').css('display','none');
          $('#editModalLabel').css('display','block');
          $('#myModal').modal('toggle');
      });
  });
});

function handlerTaskEvent(type,id){
    window.handlerTaskEvent(type,id);
}

