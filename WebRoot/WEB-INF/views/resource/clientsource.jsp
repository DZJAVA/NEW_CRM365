<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript" src="resources/js/ext-basex.js"></script>
<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
			
	
	    // --------------- grid store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/clientsource/loadClientSource.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'name' },
	            { name: 'remark'}
	        ]
	    });

	          // --------------- grid actions -----------------
	    var myGridNewAction = new Ext.Action({
	        text: '新增客户来源类型',
	        iconCls: 'btn_add',
	        scale: 'small',
	        handler: function(){
	            form1.getForm().reset();
	            form1Window.show();
	        }
	    });
	      
	    var myGridEditAction = new Ext.Action({
	        text: '编辑客户来源类型',
	        iconCls: 'btn_edit',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	                form1Window.show();
	                form1.getForm().loadRecord(record);
	            }
	        }
	    });
	            
	    var myGridDeleteAction = new Ext.Action({
	        text: '删除客户来源类型',
	        iconCls: 'btn_del',
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
	                            url: '<%=path%>/clientsource/deleteClientSource.do',
	                            params: {
	                                id: id
	                            },
	                            success: function(aResponse, aOptions){
	                            	masterStore.reload({callback: myGridUpdateAction});
	                                var result = Ext.decode(aResponse.responseText);
				                    Ext.MessageBox.alert('提示', result.msg);
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
                myGridEditAction.enable();
                myGridDeleteAction.enable();
            }else{
                myGridEditAction.disable();
                myGridDeleteAction.disable();
            }
	    };
		    // --------------- grid --------------------
	    var myGrid = new Ext.grid.GridPanel({
	        id: 'myGrid',
	        store: masterStore,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	            	header: '客户来源名称',
	                sortable: true,
	                width:300,
	                dataIndex: 'name'
	            },
	            {
	            	header: '备注',
	                sortable: true,
	                  width:300,
	                dataIndex: 'remark'
	            }
	        ],
	        tbar: [
	            myGridNewAction,
	            myGridEditAction,           
	            myGridDeleteAction
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store: masterStore,
	            displayInfo: true,
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	     // bind event
	    myGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction
	    );
			   
	    var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls:'saves',
	        handler: function(){
	            if(form1.getForm().isValid()){
	                form1.getForm().submit({
	                    url: '<%=path%>/clientsource/saveOrUpdateClientSource.do',
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                    form1Window.hide();
	                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
	                        masterStore.reload({callback: myGridUpdateAction});
	                    },
	                    failure: function(aForm, aAction) {
	                    	masterStore.reload({callback: myGridUpdateAction});
	                       Ext.MessageBox.alert('提示', aAction.result.msg);                           
	                    }
	                });
	            }
	        }
	    });
	    
	    var form1ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls:'returns',
	        handler: function(){
	           form1Window.hide();
	        }
	    });
	    
	    // ------------------ form1 -------------------------    
	    var form1 = new Ext.form.FormPanel({
	        id: 'form1',
	        bodyStyle: 'padding:5px',
	        frame: true,
	        items: [{
	            layout: 'column',
	            items: [
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 90,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 180,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
								inputType: 'hidden',
	                            fieldLabel: '*编号',
	                            id: 'id'
	                        },
	                         {
	                            fieldLabel: '*客户来源名称',
	                            editable:true,
	                            allowBlank: false,
	                            id: 'name'
	                        }
	                    ]
	                },
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 80,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 180,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
	                            fieldLabel: '备注',
	                            allowBlank: true,
	                            id: 'remark'
	                        }
	                    ]
	                }
	            ]
	        }]
	    });
	
	    var form1Window = new Ext.Window({
	        width: 620,
	        height: 150,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: form1,
	        closeAction: 'hide',
	        buttons: [
	                form1SaveAction,
	                form1ReturnAction
	            ]
	    });
	
	    // --------------- viewport --------------------
	    var viewport = new Ext.Viewport({
	        layout: 'fit',
	        border: false,
	        items: [
	            myGrid
	        ]
	    });
	
	    // --------------- load data -------------------
	    myGridLoadAction.execute();
		});
		</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
