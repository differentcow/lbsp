/**
 * Created by Barry.
 */
requirejs([ 'jquery', 'knockout', 'knockout-mapping','categoryService', 'amplify', 'commonUtil', 'dhtmlxtree', 'jquery-validate', 'validateMsg','DT-bootstrap','bootstrap'],
    function ($, ko, mapping, categoryService, amplify, util){
  var CategoryListViewModel = function() {
    this.params = new Array();
    this.tree = null;
    this.firstLoad = ko.observable(false);
    var me = this;
    this.isInsertNewNode = false;
      this.seti18nText = function(){
          $('title,label,span,div,th,a').each(function(){
              var attr = $(this).attr('i18n');
              if(attr !=null && attr != ''){
                  $(this).text(parent.parent.app.getI18nMessage(attr));
              }
          });
          $('h4,button').each(function(){
              var attr = $(this).attr('i18n');
              if(attr !=null && attr != ''){
                  $(this).html(parent.parent.app.getI18nMessage(attr));
              }
          });
      };
    //初始化
    this.init = function () {
        me.seti18nText();
        me.tree = new CategoryTreeOperate({
                        url: All580.serverName + '/service/core/category|tree',
                        elId:'tree',
                        rootId:0,
                        async:true,
                        clickHandler:function(id,data){
                            var _name = data.tree.getUserData(id,'name');
                            window.returnVal = {id:id,name:_name};
                        }
                    });
        me.tree.createTree();
    };

  };

  var CategoryTreeOperate = function(data){
      var me = this;
      this.no = 0;
      this.elId = data.elId;
      this.rootId = data.rootId;
      this.tree = null;
      this.async = data.async == null?false:true;
      this.url = data.url;
      //获取Tree对象
      this.getTreeObj = function(){
          return me.tree;
      };
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
      //节点点击事件
      this.clickHandler = typeof data.clickHandler == 'function'?function(id){return data.clickHandler(id,me);}:function(){};
      //生成树
      this.setUpTree = function (treeItems) {
          me.tree.setSkin('dhx_skyblue');
          me.tree.setImagePath("../static/images/dhxtree_skyblue/");
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

