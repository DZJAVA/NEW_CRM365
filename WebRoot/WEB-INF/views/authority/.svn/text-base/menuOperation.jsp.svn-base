<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
Ext.onReady(function(){
	Ext.QuickTips.init();// 浮动信息提示
  	Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
			
    var masterStore = new Ext.data.JsonStore({
        url: '<%=path%>/menuOperation/loadMenuOperation.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions:''
        },
        fields: [
            { name: 'id' },
            { name: 'menu_id'},
            { name: 'menu_id_operationName'},
            { name: 'operationName'},
            { name: 'displayOrder'},
            { name: 'imageName'},
            { name: 'operationRemark'}
        ]
    });
    
    var myGridNewAction = new Ext.Action({
        text: '新增操作',
        iconCls: 'smt-deparmentAdd',
        scale: 'small',
        handler: function(){
            addOperationForm.getForm().reset();
            addOperationWindow.show();
        }
    });
	      
    var myGridEditAction = new Ext.Action({
        text: '编辑操作',
        iconCls: 'smt-deparmentEdit',
        scale: 'small',
        disabled: true,
        handler: function(){
            var record = myGrid.getSelectionModel().getSelected(); 
            if(record != null){
                addOperationWindow.show();
                Ext.getCmp('find_pmenu').setValue(record.get('menu_id'));
                Ext.getCmp('find_pmenu').setRawValue(record.get('menu_id_operationName'));  
                if(record.operationName = '新增'){
                	Ext.getCmp('operationType').setValue('add');
                	Ext.getCmp('operationType').setRawValue(record.get('operationName'));
                }else if(record.operationName = '编辑'){
                	Ext.getCmp('operationType').setValue('edit');
                	Ext.getCmp('operationType').setRawValue(record.get('operationName'));
                }else if(record.operationName = '删除'){
                	Ext.getCmp('operationType').setValue('delete');
                	Ext.getCmp('operationType').setRawValue(record.get('operationName'));
                }else if(record.operationName = '导入'){
                	Ext.getCmp('operationType').setValue('import');
                	Ext.getCmp('operationType').setRawValue(record.get('operationName'));
                }else if(record.operationName = '导出'){
                	Ext.getCmp('operationType').setValue('derived');
                	Ext.getCmp('operationType').setRawValue(record.get('operationName'));
                }
                addOperationForm.getForm().loadRecord(record);
            }
        }
    });
	            
    var myGridDeleteAction = new Ext.Action({
        text: '删除操作',
        iconCls: 'smt-deparmentDelete',
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
                            url: '<%=path%>/menuOperation/deletemenuoperation.do',
                            params: {
                                id: id
                            },
                            success: function(aResponse, aOptions){
                            	myGridLoadAction.execute();
                            	masterStore.reload({callback: myGridUpdateAction});
                                var result = Ext.decode(aResponse.responseText);
			                    Ext.MessageBox.alert('提示', result.msg);
                            },
                            failure: function(aResponse, aOptions){
                            	myGridLoadAction.execute();
                                var result = Ext.decode(aResponse.responseText);
			                    Ext.MessageBox.alert('提示', result.msg);
                            }
                        })
                    }
                });
            }
        }
    });
	    
       var menuStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: '<%=path%>/menu/loadPMenu.do'
            }),
            reader: new Ext.data.JsonReader({
                    root: 'data',
                    id: 'id'
                }, 
                ['id', 'menu_name']
            )
    });
	    
   	var find_pmenu = new Ext.form.ComboBox({
        id: 'find_pmenu',
        hiddenName: 'systemMenu_id',
		allowBlank:false,
        fieldLabel: '*菜单名称',
        typeAhead: true,
        editable: false,
        triggerAction: 'all',
        lazyRender: true,
		mode: 'remote',
        store:menuStore,
        valueField: 'id',
        displayField: 'menu_name'
    });
    	menuStore.load();
  
	var operationTypeStore = new Ext.data.SimpleStore({
	      fields:['key', 'value'],
	      data:[
	      		[ 'add','新增'],
			    [ 'edit','编辑'],
			    [ 'delete','删除'],
			    [ 'import','导入'],
			    [ 'derived','导出']
	     ]
	});
		     
	   // 下拉列表框控件
	 var operationType = new Ext.form.ComboBox({
	      fieldLabel : '*操作名称',
	      id : 'operationType',
	      width:100,
	      store:operationTypeStore,
	      editable:false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...'
     });
			  
	var searchButton = {
		xtype:'button',
		iconCls: 'check',
		text:'查 询',
		handler:function(){
			var menuName=Ext.getCmp('find_pmenu').getValue();
			alert(menuName);
				Ext.apply(masterStore.baseParams,{
					conditions:'{menuName:"'+menuName+'"}'
			});
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
	}
			
	var oneTbar=new Ext.Toolbar({
	  	items:[
	  		'菜单名称：',
			 find_pmenu,
			 searchButton
		 ] 
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
            	header: '操作编号',
            	hidden:true,
                sortable: true,
                dataIndex: 'id'
            },
            {
            	header: '菜单编号',
            	hidden:true,
                sortable: true,
                dataIndex: 'menu_id'
            },
            {
            	header: '菜单名称',
                sortable: true,
                dataIndex: 'menu_id_operationName'
            },
            {
            	header: '操作名称',
                sortable: true,
                dataIndex: 'operationName'
            },
            {
            	header: '操作顺序',
                sortable: true,
                dataIndex: 'displayOrder'
            },
            {
            	header: '图片名称',
                sortable: true,
                dataIndex: 'imageName'
            },
            {
            	header: '操作备注',
                sortable: true,
                dataIndex: 'operationRemark'
            }
        ],
        tbar: [
            myGridNewAction,
            myGridEditAction,           
            myGridDeleteAction
        ],
         listeners : {
		 'render' : function(){
			 oneTbar.render(this.tbar); //add one tbar
		 }
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: masterStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    
    var addOperationReturnAction = new Ext.Action({
        text: '返回',
        iconCls: 'returns',
        handler: function(){
           addOperationWindow.hide();
        }
    });
	    
    myGrid.getSelectionModel().addListener(
        'selectionchange', 
        myGridUpdateAction
    );

    var addOperationSaveAction = new Ext.Action({
        text: '保存',
        iconCls: 'saves',
        handler: function(){
        var operationType_value =Ext.getCmp('operationType').getValue();
        var find_pmenu =Ext.getCmp('find_pmenu').getValue();
            if(addOperationForm.getForm().isValid()){
                addOperationForm.getForm().submit({
                    url: '<%=path%>/menuOperation/saveOrUpdatemenuoperation.do',
                    params: {
                    	 operationType:operationType_value,
                       	 find_pmenu:find_pmenu
                    },
                    waitTitle: '请等待',
                    waitMsg: '正在处理数据...',
                    timeout: 10,
                    success: function(aForm, aAction){
                        addOperationWindow.hide();
                        masterStore.reload({callback: myGridUpdateAction});
                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
                    },
                    failure: function(aForm, aAction) {
                       Ext.MessageBox.alert('提示', aAction.result.msg);                           
                    }
                });
            }
        }
    });
    
   var addOperationForm = new Ext.form.FormPanel({
       id: 'addOperationForm',
       bodyStyle: 'padding:5px',
       frame: true,
       items: [{
           layout: 'column',
           items: [
               {
                   columnWidth:1,
                   layout: 'form',
                   labelWidth: 60,
                   defaultType: 'textfield',
                   defaults: {
                       width: 120,
                       msgTarget: 'side'
                   },
                   items: [
                       {
						   inputType: 'hidden',
                           fieldLabel: '*编号',
                           id: 'id'
                       },
                       find_pmenu,
                       operationType,
                       {
                           fieldLabel: '图片名称',
                           id: 'imageName'
                       },
                        {
                           fieldLabel: '操作备注',
                           id: 'operationRemark'
                        }]
               }
            ]
        }]
    });
	    
    var addOperationWindow = new Ext.Window({
        width: 250,
        height: 200,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: addOperationForm,
        closeAction: 'hide',
        buttons: [
                addOperationSaveAction,
                addOperationReturnAction
            ]
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