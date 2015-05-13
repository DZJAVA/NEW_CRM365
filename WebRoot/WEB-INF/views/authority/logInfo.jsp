<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
Ext.onReady(function(){
	Ext.QuickTips.init();// 浮动信息提示
  	Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
   var masterStore = new Ext.data.JsonStore({
       url: '<%=path%>/log/loadLog.do',
       root: 'data',
       totalProperty: 'totalCount',
       idProperty: 'id',
       autoDestroy: true,
       baseParams: {
       },
       fields: [
           { name: 'id' },
           { name: 'logTime' },
           { name: 'ip' },
           { name: 'loginId' },
           { name: 'userName' },
           { name: 'remark'}
       ]
   });
   
   var myGridDeleteAction = new Ext.Action({
       text: '删除',
       iconCls: 'smt-logDelete',
       scale: 'small',
       disabled: true,
       handler: function(){
           if (myGrid.getSelectionModel().hasSelection()) {
               Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
                   if (aButton == 'yes'){
                       var id = ''
                       var records = myGrid.getSelectionModel().getSelections();
                       for(var i = 0, len = records.length; i < len; i ++) {
                         id = id + records[i].id + ',';
                       }
                       Ext.Ajax.request({
                           url: '<%=path%>/log/deleteLog.do',
                            params: {
                                id: id
                            },
                            success: function(aResponse, aOptions){
                                masterStore.reload({callback: myGridUpdateAction});
                                Ext.MessageBox.alert('提示', '删除成功！');
                            },
                            failure: function(aResponse, aOptions){
                                var result = Ext.decode(aOptions.response.responseText);
                                Ext.MessageBox.alert('提示', result.msg);
                            }
                        })
                    }
                });
            }
        }
    });
    
    var myGridLoadAction = new Ext.Action({
        text: '刷新',
        iconCls:'btn_del',
        scale:'small',
        disabled: true,
        handler: function(aMasterId){
            masterStore.setBaseParam('masterId', aMasterId);
            masterStore.load({
                params:{
                    start:0, 
                    limit:20
                },
                waitTitle:'提示',waitMsg: '数据加载请稍后...',
                failure: function() {
                    Ext.Msg.alert('提示', '读取数据失败！');                             
                }
            });
        }
    });   
    
    function myGridUpdateAction (){
      if(myGrid.getSelectionModel().hasSelection()){
          myGridDeleteAction.enable();
      }else{
          myGridDeleteAction.disable();
      }
    };
    

    var myGrid = new Ext.grid.GridPanel({
        id: 'myGrid',
        store: masterStore,
        sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {   hidden:true,
                header: '编号',
                sortable: true,
                dataIndex: 'id'
            },
            {
            	header: '登录时间',
                sortable: true,
                width:200,
                dataIndex: 'logTime'
            },
            {
	            
            	header: '登录IP',
                sortable: true,
                  width:200,
                dataIndex: 'ip'
            },
            {
	            
            	header: '登录名',
                sortable: true,
                dataIndex: 'userName'
            }
        ],
        tbar: [
            myGridDeleteAction
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: masterStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    
    myGrid.getSelectionModel().addListener(
        'selectionchange', 
        myGridUpdateAction
    );

   var viewport = new Ext.Viewport({
       layout: 'fit',
       border: false,
       items: [
           myGrid
       ]
   });

   myGridLoadAction.execute();
});
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
