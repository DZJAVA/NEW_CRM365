<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
			
	
	    // --------------- grid store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/menu/loadMenu.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	_sel_name:''
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'systemMenu_id' },
	            { name: 'systemMenu_name'},
	            { name: 'resourceURL'},
	            { name: 'smenIndex'},
	            { name: 'smenCaption'},
	            { name: 'smenIcon'},
	            { name: 'smenHint'}
	        ]
	    });
	          // --------------- grid actions -----------------
	    var myGridNewAction = new Ext.Action({
	        text: '新增菜单',
	        iconCls: 'application_add',
	        scale: 'small',
	        handler: function(){
	            form1.getForm().reset();
	            form1Window.show();
	        }
	    });
	    
	    var addRoleAction = new Ext.Action({
	        text: '配置角色',
	        iconCls: 'list_users',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            form2.getForm().reset();
	            form2Window.show();
	        }
	    });
	      
	    var myGridEditAction = new Ext.Action({
	        text: '编辑菜单',
	        iconCls: 'application_edit',
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
	        text: '删除菜单',
	        iconCls: 'application_delete',
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
	                            url: '<%=path%>/menu/deleteMenu.do',
	                            params: {
	                                id: id
	                            },
	                            success: function(aResponse, aOptions){
	                                masterStore.reload({callback: myGridUpdateAction});
	                                var result = Ext.decode(aResponse.responseText);
	                                Ext.MessageBox.alert('提示', result.message);
	                            },
	                            failure: function(aResponse, aOptions){
	                                masterStore.reload({callback: myGridUpdateAction});
	                                var result = Ext.decode(aResponse.responseText);
	                                Ext.MessageBox.alert('提示', result.message);
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
	    
	   var searchAction = new Ext.Action({
	        text: '查询',
	        iconCls:'check',
	        scale:'small',
	        handler: function(){
	            var sel_name = Ext.getCmp('sel_name').getValue();
	            masterStore.load({
	                params:{
	                    start:0, 
	                    limit:20,
	                    _sel_name:sel_name
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
	                addRoleAction.enable();
	            }else{
	                myGridEditAction.disable();
	                myGridDeleteAction.disable();
	                addRoleAction.disable();
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
	        viewConfig: {
	            forceFit: true
	        },
	        columns: [
	            {
	                header: '编号',
	                hidden:true,
	                sortable: true,
	                dataIndex: 'id'
	            },
	            {
	            
	            	header: '菜单标题',
	                sortable: true,
	                dataIndex: 'smenCaption'
	            },
	            {
	                header: '父级菜单标题',
	                sortable: true,
	                dataIndex: 'systemMenu_name'
	            },
	            {
	                header: '资源路径',
	                sortable: true,
	                dataIndex: 'resourceURL'
	            },
	            {
	                header: '图标',
	                sortable: true,
	                dataIndex: 'smenIcon'
	            },
	            {
	                header: '排列顺序',
	                sortable: true,
	                dataIndex: 'smenIndex'
	            },
	            {
	                header: '提示',
	                sortable: true,
	                dataIndex: 'smenHint'
	            }
	        ],
	        tbar: [
	            myGridNewAction,
	            myGridEditAction,           
	            myGridDeleteAction,
	            addRoleAction,
	            '&nbsp;&nbsp;&nbsp;菜单名称：',
	            {
	            	xtype:'textfield',
	            	width:120,
	            	id:'sel_name'
	            },
	            searchAction
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
	     // bind event
	    myGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction
	    );
			   
	
	    var form1LoadAction = new Ext.Action({
	        text: '刷新',
	        handler: function(aId){
	            var idValue = '<c:out value="${param.id}"/>';
	
	            if (Ext.type(aId) == 'number') {
	                idValue = aId;
	            }
	            form1.getForm().reset();
	            form1.getForm().load({
	                url: '<c:url value="/security/usermanage.do"/>?action=form1_load',
	                method: 'get',
	                params: {
	                    id: idValue
	                },
	                waitTitle:'提示',waitMsg: '数据加载请稍后...',
	                success: function(aResponse, aOptions){
	                    myGridUpdateAction();
				// Ext.getCmp('image_id').el.dom.src =  aOptions.result.data.imageUrl;//更新form中图片控件
	                },
	                failure: function() {
	                    Ext.Msg.alert('提示', '读取数据失败！');                             
	                }
	            });
	        }
	    });
	
	    var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	        	var pMenu = Ext.getCmp('find_pmenu').getValue();
	            if(form1.getForm().isValid()){
	                form1.getForm().submit({
	                    url: '<%=path%>/menu/saveMenu.do',
	                    params: {
	                        pMenuId:pMenu
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                    	Ext.Msg.alert('提示', '保存成功！'); 
	                        form1Window.hide();
	                        masterStore.reload({callback: myGridUpdateAction});
	                    },
	                    failure: function(aForm, aAction) {
	                        var result = Ext.decode(aAction.response.responseText);
	                    	Ext.Msg.alert('提示', result.msg);                            
	                    }
	                });
	            }
	        }
	    });
	    
	    var form2SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	        	var menuId = myGrid.getSelectionModel().getSelected().get('id'); 
	        	var menuUrl = myGrid.getSelectionModel().getSelected().get('resourceURL');
	        	var roleId = Ext.getCmp('find_role').getValue();
	        	if(roleId == ''){
	        		alert('请先选择角色，再进行配置！');
	        		return false;
	        	}
	        	if(menuUrl == ''){
	        		alert('父级菜单项不用配置角色！');
	        		return false;
	        	}
	            if(form2.getForm().isValid()){
	                form2.getForm().submit({
	                    url: '<%=path%>/menu/saveRoleAndMenu.do',
	                    params: {
	                        menuid:menuId,
	                        roleid:roleId
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                    	Ext.Msg.alert('提示', '保存成功！'); 
	                        form2Window.hide();
	                    },
	                    failure: function(aForm, aAction) {	
	                    	var result = Ext.decode(aAction.response.responseText);
	                    	Ext.Msg.alert('提示', result.msg);     
	                    }
	                });
	            }
	        }
	    });
	    
	    var form1ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls: 'returns',
	        handler: function(){
	           form1Window.hide();
	        }
	    });
	    
	    var form2ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls: 'returns',
	        handler: function(){
	           form2Window.hide();
	        }
	    });
	    
	    var roleStore = new Ext.data.Store({
	            proxy: new Ext.data.HttpProxy({
	                url: '<%=path%>/role/findRole.do'
	            }),
	            reader: new Ext.data.JsonReader({
	                    root: 'data',
	                    id: 'id'
	                }, 
	                ['id', 'role_name']
	            )
	    });
	    
	   	var find_role = new Ext.form.ComboBox({
	        id: 'find_role',
	        hiddenName: 'role_id',
			allowBlank:false,
	        fieldLabel: '*角色名称',
	        typeAhead: true,
	        editable: false,
	        triggerAction: 'all',
	        lazyRender: true,
			mode: 'remote',
	        store:roleStore,
	        valueField: 'id',
	        displayField: 'role_name'
	    });
	    roleStore.load();
	    
	    
	    
	    
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
			allowBlank:true,
	        fieldLabel: '*父级栏目名称',
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
	   
	   
	    // ------------------ form -------------------------    
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
	                    labelWidth: 80,
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
	                            fieldLabel: '*栏目名称',
	                            allowBlank: false,
	                            id: 'smenCaption'
	                        },
	                        {
	                            fieldLabel: '*资源路径',
	                            id: 'resourceURL'
	                        },
	                        {
	                            fieldLabel: '*菜单顺序',
	                            id: 'smenIndex'
	                        }
	                    ]
	                },
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 100,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 180,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        find_pmenu,
	                        {
	                            fieldLabel: '*资源图片',
	                            id: 'smenIcon'
	                        },
	                        {
	                            fieldLabel: '*提示',
	                            id: 'smenHint'
	                        }
	                    ]
	                }
	            ]
	        }]
	    });
	    
	     var form2 = new Ext.form.FormPanel({
	        id: 'form2',
	        bodyStyle: 'padding:5px',
	        frame: true,
	        items: [{
	            layout: 'form',
	            items: [
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
	                    	find_role
	                    ]
	                }
	            ]
	        }]
	    });
	
	    var form1Window = new Ext.Window({
	        width: 640,
	        height: 260,
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
	    
	    var form2Window = new Ext.Window({
	        width: 380,
	        height: 150,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: form2,
	        closeAction: 'hide',
	        buttons: [
	                form2SaveAction,
	                form2ReturnAction
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
