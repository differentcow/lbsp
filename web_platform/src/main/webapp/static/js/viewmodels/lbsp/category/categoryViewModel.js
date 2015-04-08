/**
 * Created by Barry.
 */
requirejs([ 'jquery', 'knockout', 'knockout-mapping','categoryService', 'amplify', 'commonUtil', 'dhtmlxtree', 'jquery-validate', 'validateMsg','DT-bootstrap','bootstrap'],
    function ($, ko, mapping, categoryService, amplify, util){
  var off_type = parent.app.getI18nMessage('common.select.type.off');
  var on_type = parent.app.getI18nMessage('common.select.type.on');
  var CategoryListViewModel = function() {
    this.name = ko.observable('');
    this.code = ko.observable('');
    this.params = new Array();
    this.paramAdd = 'addcategory';
    this.paramDel = 'delcategory';
    this.paramModify = 'modifycategory';
    this.viewDel = ko.observable(false);
    this.viewAdd = ko.observable(false);
    this.viewEdit = ko.observable(false);
    this.tree = null;
    this.firstLoad = ko.observable(false);
    this.statusOptions = ko.observableArray([]);
    var me = this;

    var SelectModel = function (text, value) {
        this.text = text;
        this.val = value;
    };
    this.loadStatusList = function(){
        me.statusOptions.push(new SelectModel(on_type,1));
        me.statusOptions.push(new SelectModel(off_type,0));
    };
    this.initAuth = function () {
        if (parent.parent.app.checkAuth(me.paramAdd)) {
            me.viewAdd(true);
        }
        if (parent.parent.app.checkAuth(me.paramDel)) {
            me.viewDel(true);
        }
        if (parent.parent.app.checkAuth(me.paramModify)) {
            me.viewEdit(true);
        }
    };
    this.handler = new HandlerServer();
    this.categoryView = new CateoryView();
    this.addTypeView = ko.observable(false);
    this.isInsertNewNode = false;
    this.viewSub = ko.observable(true);
    //提交
    this.apply = function(){
        if(me.tree.saveList.length == 0
            && me.tree.editList.length == 0
            && me.tree.delList.length == 0){
            parent.amplify.publish('status.alerts','系统信息','您还没有进行任何操作！!');
            return;
        }
        var ary = [];
        if(me.tree.saveList.length > 0){
            ary.push({obj:mapping.toJSON({saveOperateList:me.tree.saveList}),type:"add",msg:"新增操作保存成功"});
        }
        if(me.tree.editList.length > 0){
            ary.push({obj:mapping.toJSON({editOperateList:me.tree.editList}),type:"upt",msg:"更新操作保存成功"});
        }
        if(me.tree.delList.length > 0){
            ary.push({obj:mapping.toJSON({deleteOperateList:me.tree.delList}),type:"del",msg:"删除操作保存成功"});
        }
        $('#myModal2').modal('toggle');
        $('#body-content-modal').text('正在保存操作，请稍候.......');
        $('#myModal2').off('click.dismiss.bs.modal');
        me.submitStep(ary,0);
    }
    this.submitStep = function(data,i){
        me.handler.setDeferred(categoryService.operate(data[i].obj,data[i].type));
        me.handler.setResponceFunc(function(msg){
            $('#body-content-modal').text(data[i].msg);
            if((i+1) == data.length){
                console.log('成功');
                $('#myModal2').modal('toggle');
                self.location.href="list.html";
                parent.amplify.publish('status.alerts','系统信息','操作成功!');
            }else{
                me.submitStep(data,++i);
            }
        });
        me.handler.handler();
    }
    //初始化
    this.init = function () {
        me.initAuth();
        me.loadStatusList();
        me.firstLoad(false);
        me.tree = new CategoryTreeOperate({
                        url: All580.serverName + '/service/core/category|tree',
                        elId:'tree',
                        rootId:0,
                        async:true,
                        onOpenEnd: function(_this){
                            if(me.isInsertNewNode){
                                var obj = _this.newNode(2);
                                me.categoryView.setData(obj);
                                util.adjustIframeHeight();
                                me.isInsertNewNode = false;
                            }
                        },
                        clickHandler:function(id,data){
                            me.firstLoad(true);
                            if(data.isNewNode(id)){
                                me.addTypeView(true);
                            }else{
                                me.addTypeView(false);
                            }
                            var obj = data.getNodeUserData(id);
                            if(obj != null){
                                me.categoryView.empty();
                                me.categoryView.setData(obj);
                            }else{
                                me.handler.setDeferred(categoryService.getDetailById(id));
                                me.handler.setResponceFunc(function(data){
                                    me.categoryView.empty();
                                    me.categoryView.setData(data);
                                });
                                me.handler.handler();
                            }
                        }
                    });
        me.tree.createTree();
    };
    this.add = function(type){
        var node = me.tree.getSelectedNode();
        var flag = me.tree.getTreeObj().getOpenState(node.id);
        if(!flag && type == 2 && !me.tree.isNewNode(node.id)){   //未打开节点，并且添加的是子节点
            me.tree.getTreeObj().openItem(node.id);
            me.isInsertNewNode = true;
        }else{  //已经打开节点
            var obj = me.tree.newNode(type);
            me.categoryView.setData(obj);
            util.adjustIframeHeight();
        }
    };
    this.del = function(type){
        if(type == 0){
            me.tree.delNode();
            me.firstLoad(false);
            util.adjustIframeHeight();
        }
        $('#myModal').modal('toggle');
    };
    this.edit = function(){
        //检验
        if(typeof me.categoryView.name() == 'undefined' || me.categoryView.name().replace(/(^\s*)|(\s*$)/g,'') == ''){
            parent.amplify.publish('status.alerts', '系统信息', '名称是必填项.');
            return;
        }
        var param = {name:me.categoryView.name(),status:me.categoryView.status()};
        me.tree.editNode(param);
    };
    this.move = function(type){
        if(type == 1){
            me.tree.moveNode('up_strict');
        }else{
            me.tree.moveNode('down_strict');
        }
    };

  };

  var CateoryView = function(){
      this.id = ko.observable();
      this.name = ko.observable('');
      this.parent_id = ko.observable();
      this.depth = ko.observable();
      this.status = ko.observable();
      this.parentName = ko.observable('');
      this.priority = ko.observable();
      this.query_code = ko.observable('');
      var me = this;
      this.empty = function(){
          me.id();
          me.name('');
          me.parent_id();
          me.depth();
          me.status();
          me.parentName('');
          me.priority();
          me.query_code('');
      };
      this.setData = function(data){
          if(typeof data != 'undefined'){
              if(typeof data.id != 'undefined'){
                  me.id(data.id);
              }
              if(typeof data.query_code != 'undefined'){
                  me.query_code(data.query_code);
              }
              if(typeof data.priority != 'undefined'){
                  me.priority(data.priority);
              }
              if(typeof data.parentName != 'undefined'){
                  me.parentName(data.parentName);
              }
              if(typeof data.depth != 'undefined'){
                  me.depth(data.depth);
              }
              if(typeof data.parent_id != 'undefined'){
                  me.parent_id(data.parent_id);
              }
              if(typeof data.name != 'undefined'){
                  me.name(data.name);
              }
              if(typeof data.status != 'undefined'){
                  me.status(data.status);
              }
          }
      }
  }

  var HandlerServer = function(deferred,func){
      var me = this;
      this.deferred = deferred;

      this.func = (typeof func == 'function'?function(data){return func(data);}:function(data){});

      this.setDeferred = function(defferred){
          me.deferred = defferred;
      }

      this.setResponceFunc = function (func){
          me.func = (typeof func == 'function'?function(data){return func(data);}:function(data){});
      }

      this.handler = function(){
        $.when(me.deferred).done(function (response) {
            if (response.state.code==200000 &&typeof response.result!='undefined') {
                me.func(response.result);
            } else {
                console.log('Server Failed');
                util.adjustIframeHeight();
            }
        }).fail(function (error) {
            console.log(error);
        });
      }
  }

  var CategoryTreeOperate = function(data){
      var me = this;
      this.savePoint = {};
      this.editPoint = {};
      this.delPoint = {};
      this.saveList = [];
      this.editList = [];
      this.delList = [];
      this.structure = [];
      this.no = 0;
      this.elId = data.elId;
      this.rootId = data.rootId;
      this.tree = null;
      this.async = data.async == null?false:true;
      this.url = data.url;
      //获取Tree对象
      this.getTreeObj = function(){
          return me.tree;
      }
      //判断是否新增节点
      this.isNewNode = function(id){
          if(id < 0){
              return true;
          }
          return false;
      }
      this.onOpenEnd = typeof data.onOpenEnd == 'function'?function(){return data.onOpenEnd(me);}:function(){};
      //初始化树
      this.createTree = function(){
          //树对象
          me.tree = new dhtmlXTreeObject(me.elId, "100%", "100%", me.rootId);

          me.tree.attachEvent("onOpenEnd", me.onOpenEnd);

          if(me.async) {
              me.setUpTree(null);
          }else{
              /*new HandlerServer(placeService.tree(null),function(result){
                  me.setUpTree(result);
              }).handler();*/
          }

      };
      this.genId = 0;
      //生成节点ID
      this.genNodeId = function(){
          return (--me.genId);
      };
      //获取选中的节点对象
      this.getSelectedNode = function(){
          var nodeId = me.tree.getSelectedItemId();
          var node = me.tree._globalIdStorageFind(nodeId);
          return node;
      };
      //获取父节点对象
      this.getParentNode = function(id){
          var parentId = me.tree.getParentId(id);
          var node = me.tree._globalIdStorageFind(parentId);
          return node;
      };
      //获取同级别内的最后一个对象
      this.getLastNodeWithLevel = function(id){
          var parentId = me.getParentNode(id).id;
          var ids = me.tree.getSubItems(parentId);
          var ary = ids.split(",");
          var lastId = ary[ary.length - 1];
          var node = me.tree._globalIdStorageFind(lastId);
          return node;
      };
      //获取子级别内的最后一个对象
      this.getLastNodeWithSub = function(parentId){
          if(me.tree.hasChildren(parentId) == 0){
              return null;
          }
          var ids = me.tree.getSubItems(parentId);
          var ary = ids.split(",");
          var lastId = ary[ary.length - 1];
          var node = me.tree._globalIdStorageFind(lastId);
          return node;
      };
      //获取节点自定义属性
      this.getNodeUserData = function(id){
          if(me.savePoint[id] != null){
              var index = me.savePoint[id];
              return me.saveList[index];
          }else if(me.editPoint[id] != null){
              var index = me.editPoint[id];
              return me.editList[index];
          }
          return null;
      };
      //获取ID节点附近节点（around = next：ID节点的下一个节点；around = previous：ID节点的上一个节点）
      this.getArountNodeWithLevel = function(around,id){
          var parentId = me.getParentNode(id).id;
          var ids = me.tree.getSubItems(parentId);
          var ary = ids.split(",");
          var len = around == 'next'?ary.length - 1:0;
          for(var i = 0;i < ary.length; i++){
              if(i != len && ary[i] == id){
                  return around == 'next'?ary[i+1]:ary[i-1];
              }
          }
          return null;
      };
      //新增节点
      this.newNode = function(type){
          var node = me.getSelectedNode();
          var last = type==1?me.getLastNodeWithLevel(node.id):me.getLastNodeWithSub(node.id);
          var id = me.genNodeId();
          var label = 'undefined'+(++me.no);
          var parentId = type==1?me.getParentNode(node.id).id:node.id;
          if(last == null){
              me.tree.insertNewChild(node.id,id,label);
          }else{
              me.tree.insertNewNext(last.id,id,label);
          }
          var obj = me.createNodeObj(id,last,label,parentId);
          me.push(obj,'SAVE');
          me.tree.selectItem(id,false,false);
          return obj;
      };
      //创建新节点对象
      this.createNodeObj = function(id,preNode,label,parentId){
          var obj = {};
          var flag = true;
          if(parentId != null && parentId != 0 ){
              obj['parent_id'] = parentId;
              flag = false;
          }
          obj['id'] = id;
          obj['name'] = label;
          obj['priority'] = preNode== null?1:(parseInt(me.tree.getUserData(preNode.id,'priority'))+1);
          obj['depth'] = flag?1:parseInt(me.tree.getUserData(obj['parent_id'],'depth')) + 1;
          var queryCode = flag?','+obj['id']:me.tree.getUserData(obj['parent_id'],'query_code')+','+obj['id'];
          obj['query_code'] = queryCode;
          obj['parentName'] = me.tree.getUserData(obj['parent_id'],'name');
          obj['status'] = 1;
          for(var k in obj){
              me.tree.setUserData(id,k,obj[k]);
          }
          return obj;
      };
      //复制操作的节点对象
      this.copyItem = function(data){
          var obj = {};
          for(var k in data){
              if(typeof data[k] != 'function'){
                  obj[k.substring(2)] = data[k];
              }
          }
          return obj;
      };
      //存储操作对象
      this.push = function(obj,type){
          var list = null;
          if(type == 'SAVE'){
              me.savePoint[obj.id] = me.saveList.length;
              list = me.saveList;
          }else if(type == 'EDIT'){
              me.editPoint[obj.id] = me.editList.length;
              list = me.editList;
          }else if(type == 'DELETE'){
              me.delPoint[obj.id] = me.delList.length;
              list = me.delList;
          }
          list.push(obj);
      };
      //删除节点
      this.delNode  = function(){
          var node = me.getSelectedNode();
          var obj = {};
          if(me.savePoint[node.id] != null){
              me.saveList.splice(me.savePoint[node.id],1);
          }else if(me.editPoint[node.id] != null){
              obj = me.editList.splice(me.editPoint[node.id],1);
          }else{
              obj = me.copyItem(node.userData);
          }
          if(obj != null){
              me.push(obj,'DELETE');
          }
          me.tree.deleteItem(node.id,false);
      };
      //移动节点
      this.moveNode = function(mode){
          var src = me.getSelectedNode().id;
          var target = null;
          if(mode == 'up_strict'){
            target = me.getArountNodeWithLevel('previous',src);
          }else{
            target = me.getArountNodeWithLevel('next',src);
          }
          if(target == null){
              return;
          }
          //获取
          var src_sort = me.tree.getUserData(src,'priority');
          var target_sort = me.tree.getUserData(target,'priority');
          //顺序交换
          me.editNodeContent(src,{priority:target_sort});
          me.editNodeContent(target,{priority:src_sort});
          //交换节点
          me.tree.moveItem(src,mode,target);
          //选中移动的节点
          me.tree.selectItem(src,false,false);
      };
      //编辑节点
      this.editNode = function(data){
          var id = me.tree.getSelectedItemId();
          me.editNodeContent(id,data);
          me.tree.setItemText(id,data.name);
      };
      //编辑节点内容
      this.editNodeContent = function(id,data){
          var node = me.tree._globalIdStorageFind(id);
          me.changeNodeProp(node,data);
          if(me.savePoint[node.id] != null){
              me.saveList.splice(me.savePoint[node.id],1,me.copyItem(node.userData));
          }else if(me.editPoint[node.id] != null){
              me.editList.splice(me.editPoint[node.id],1,me.copyItem(node.userData));
          }else{
              me.editPoint[id] = me.editList.length;
              me.push(me.copyItem(node.userData),'EDIT');
          }
      };
      //更改节点内部属性
      this.changeNodeProp = function(node,data){
          if(data == null){return;}
          for(var k in data){
              if(typeof node[k] != 'undefined'){
                  node[k] = data[k];
              }
              if(typeof me.tree.getUserData(node.id,k) != 'undefined'){
                  me.tree.setUserData(node.id,k,data[k]);
              }
          }
      };
      //节点点击事件
      this.clickHandler = typeof data.clickHandler == 'function'?function(id){return data.clickHandler(id,me);}:function(){};
      //生成树
      this.setUpTree = function (treeItems) {
          me.tree.setSkin('dhx_skyblue');
          me.tree.setImagePath("../../static/images/dhxtree_skyblue/");
          me.tree.enableCheckBoxes(false);
          me.tree.enableThreeStateCheckboxes(false);
          me.tree.setOnClickHandler(me.clickHandler);
          me.tree.enableHighlighting(1);
          me.tree.setOnOpenEndHandler(function () {
              util.adjustIframeHeight();
          });
          if(me.async){
              me.tree.setXMLAutoLoading(me.url);
              me.tree.setDataMode("json");
              //load first level of tree
              me.tree.loadJSON(me.url);
          }else{
              me.tree.loadJSONObject({id: me.rootId,item: treeItems}, function () {
                  util.adjustIframeHeight();
              });
          }
      };
  }

  var list = new CategoryListViewModel();
  list.init();
  ko.applyBindings(list);

});

