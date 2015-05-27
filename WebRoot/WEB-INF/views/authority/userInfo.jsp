<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
			var empOrCus='';//储存用户关联的客户ID或者员工ID
			var empId = '';//员工ID
			var roleCode = '${userSession.role.roleCode}';
	    // --------------- grid store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/user/loadUser.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	_userName:''
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'loginId' },
	            { name: 'password'},
				{ name: 'userName'},
				{ name: 'isOrNotEnable'},
				{ name: 'roleId'},
				{ name: 'roleName'},
				{ name: 'departId'},
				{ name: 'departName'},
				{ name: 'counts'},
				{ name: 'signStatus'},
	            { name: 'remark'}
	        ]
	    });
	    
	    var showRoleByUserId = '';
	    var showRoleStore = new Ext.data.JsonStore({
	        url: '<%=path%>/user/loadUserWithRole.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	_userId:showRoleByUserId
	        },
	        fields: [
	            { name: 'rid' },
	            { name: 'rName'},
	            { name: 'rRemark'}
	        ]
	    });

	     var departmentStore = new Ext.data.Store({
			          proxy: new Ext.data.HttpProxy({
			              url: '<%=path%>/employee/loadDepartment.do'
			          }),
			          reader: new Ext.data.JsonReader({
			                  root: 'data',
			                  id: 'departId'
			              }, 
			              ['departId', 'departName']
			          )
			  });
			  
		var departComboBox = new Ext.form.ComboBox({
		      id: 'departComboBox',
			  allowBlank: false,
			  hiddenName: 'departId',
		      fieldLabel: '*所属部门',
		      emptyText : '请选择...',
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
		      lazyRender: true,
			  mode: 'remote',
		      store:departmentStore,
		      valueField: 'departId',
		      displayField: 'departName'
	  	});
		departmentStore.load();
		//---------------签单状态下拉列表--------------------------
	    var signStatusData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ 0, '不接单'],
			    [ 1, '接单']
		      ]
		});
		var signStatusCombox = new Ext.form.ComboBox({
		      fieldLabel : '*接单状态',
		      id : 'signStatusCombox',
		      store : signStatusData,
		      editable:false,
		      allowBlank: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      hiddenName : 'signStatus'  
	    });
	    
	          // --------------- grid actions -----------------
	    var myGridNewAction = new Ext.Action({
	        text: '新增用户信息',
	        iconCls: 'group_add',
	        scale: 'small',
	        handler: function(){
	        	Ext.getCmp("loginId").enable();
	        	Ext.getCmp('formWindow').setTitle('新增用户信息');
	        	departComboBox.setRawValue('');
	        	find_role.setRawValue('');
	            form1.getForm().reset();
	        	signStatusCombox.setRawValue('');
	            form1Window.show();
	        }
	    });
	    
	    var myGridEditAction = new Ext.Action({
	        text: '编辑用户信息',
	        iconCls: 'group_edit',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
					Ext.getCmp("loginId").disable();
	            	Ext.getCmp('formWindow').setTitle('编辑用户信息');
	                form1Window.show();
	                form1.getForm().loadRecord(record);
	            }
	        }
	    });
	    
	     var myGridDeleteAction1 = new Ext.Action({
	        text: '删除用户信息',
	        iconCls: 'group_delete',
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
	                            url: '<%=path%>/user/deleteUsers.do',
	                            params: {
	                                id: id
	                            },
	                            success: function(aResponse, aOptions){
	                            	masterStore.reload({callback: myGridUpdateAction});
	                                var result = Ext.decode(aResponse.responseText);
				                    Ext.MessageBox.alert('提示', result.msg);
	                            },
	                            failure: function(aResponse, aOptions){
	                                var result = Ext.decode(aResponse.responseText);
				                    Ext.MessageBox.alert('提示', result.msg);
	                            }
	                        })
	                    }
	                });
	            }
	        }
	    });
	    var startAction = new Ext.Action({
	        text: '启用',
	        iconCls: 'drop-yes',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            if (myGrid.getSelectionModel().hasSelection()) {
                    var id = ''
                    var records = myGrid.getSelectionModel().getSelections();
                    for(var i = 0, len = records.length; i < len; i ++) {
                      id = id + records[i].id + ',';
                    }
                    Ext.Ajax.request({
                        url: '<%=path%>/user/userStatus.do',
                        params: {
                            id: id,
                            flag: 0
                        },
                        success: function(aResponse, aOptions){
                        	masterStore.reload({callback: myGridUpdateAction});
                            var result = Ext.decode(aResponse.responseText);
                   			Ext.MessageBox.alert('提示', result.msg);
                        },
                        failure: function(aResponse, aOptions){
                            var result = Ext.decode(aResponse.responseText);
                   			Ext.MessageBox.alert('提示', result.msg);
                        }
                    })
	            }
	        }
	    });
	    var stopAction = new Ext.Action({
	        text: '停用',
	        iconCls: 'drop-no',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            if (myGrid.getSelectionModel().hasSelection()) {
                    var id = ''
                    var records = myGrid.getSelectionModel().getSelections();
                    for(var i = 0, len = records.length; i < len; i ++) {
                      id = id + records[i].id + ',';
                    }
                    Ext.Ajax.request({
                        url: '<%=path%>/user/userStatus.do',
                        params: {
                            id: id,
                            flag: 1
                        },
                        success: function(aResponse, aOptions){
                        	masterStore.reload({callback: myGridUpdateAction});
                            var result = Ext.decode(aResponse.responseText);
                   			Ext.MessageBox.alert('提示', result.msg);
                        },
                        failure: function(aResponse, aOptions){
                            var result = Ext.decode(aResponse.responseText);
                   			Ext.MessageBox.alert('提示', result.msg);
                        }
                    })
	            }
	        }
	    });
        var myGridLoadAction = new Ext.Action({
	        handler: function(){
	            masterStore.load({
	                params:{
	                    start:0, 
	                    limit:20
	                }
	            });
	        }
	    });   
	     var searchAction = new Ext.Action({
	        text: '查询',
	        iconCls:'check',
	        scale:'small',
	        handler: function(){
	            var user_name = Ext.getCmp('user_name').getValue();
	            masterStore.load({
	                params:{
	                    start:0, 
	                    limit:20,
	                    _userName:user_name
	                }
	            });
	             Ext.getCmp('user_name').setValue('');
	        }
	    });
	    
	    function myGridUpdateAction(){
	            if(myGrid.getSelectionModel().hasSelection()){
	                myGridEditAction.enable();
	                myGridDeleteAction1.enable();
	                startAction.enable();
	                stopAction.enable();
	            }else{
	                myGridEditAction.disable();
	                myGridDeleteAction1.disable();
	                startAction.disable();
	                stopAction.disable();
	            }
	    };
	    
	 	var checkIsUser = function(stats){
	       if(stats == '0'){
	         return '是';
	       }else if(stats == '1'){
	         return '否';
	       }
	    };
		// --------------- grid --------------------
	    var myGrid = new Ext.grid.GridPanel({
	        id: 'myGrid',
	        store: masterStore,
	        sm: new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        viewConfig: {
	            forceFit: true
	        },
	        columns: [
	        	new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
	            {
	            	hidden:true,
	                header: '编号',
	                sortable: true,
	                dataIndex: 'id'
	            },
	            {
	            
	            	header: '登录名',
	                sortable: true,
	                dataIndex: 'loginId'
	            },
	            {
	                header: '登录密码',
	                sortable: true,
	                dataIndex: 'password',
	                renderer: function(val){
	                	if(roleCode == '201204'){
	                		return '******';
	                	}else{
	                		return val;
	                	}
	                }
	            },
	            {
	                header: '部门名称',
	                sortable: true,
	                dataIndex: 'departName'
	            },
	            {
	                header: '用户姓名',
	                sortable: true,
	                dataIndex: 'userName'
	            },
	            {
	            	header: '角色名称',
	                sortable: true,
	                dataIndex: 'roleName'
	            },
	            {
	            	header: '分配系数',
	                sortable: true,
	                dataIndex: 'counts'
	            },
	            {
	            	header: '状态接单',
	                sortable: true,
	                dataIndex: 'signStatus',
	                renderer:function(value){
	                	return value == 1 ? '<font style="color:green">接单</font>' : '<font style="color:red">不接单</font>'
	                }
	            },
	            {
	            	header: '是否停用',
	                sortable: true,
	                dataIndex: 'isOrNotEnable',
	                renderer:function(value){
	                	return value == 1 ? '<font style="color:green">停用</font>' : '<font style="color:red">启用</font>'
	                }
	            },
	            {
	                header: '备注',
	                sortable: true,
	                width:150,
	                dataIndex: 'remark'
	            }
	        ],
	        tbar: [
	            myGridNewAction,
	            myGridEditAction,  
	            myGridDeleteAction1,
	            startAction,
	            stopAction,
	            '&nbsp;&nbsp;&nbsp;用户姓名：',
	            {
	            	xtype:'textfield',
	            	width:120,
	            	id:'user_name'
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
	    
	    var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	        	var _find_role=Ext.getCmp('find_role').getValue();
	        	var dept = departComboBox.getValue();
        	  	if(form1.getForm().isValid()){
        	  		var sign = signStatusCombox.getValue();
        	  		if(sign === ''){
        	  			signStatusCombox.setValue(0);
        	  		}
	                form1.getForm().submit({
	                    url: '<%=path%>/user/saveOrUpdateUser.do',
	                    params: {
	                        find_role:_find_role,
	                        dept: dept
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
	                        Ext.Msg.alert('提示', result.message);                             
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
     var enableData = new Ext.data.SimpleStore({
	      fields:['value', 'key'],
	      data:[
	      		['停用', 1],
	        	['启用', 2]
	           ]
	     });
	   // 下拉列表框控件
	   var comboxEnable = new Ext.form.ComboBox({
	      fieldLabel : '*是否停用',
	      id : 'comboxEnable',
	      allowBlank:false,
	      editable:false,
	      store : enableData,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...', // 默认值       selectOnFocus : true,
	      hiddenName : 'isOrNotEnable'  
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
	        hiddenName: 'roleId',
			allowBlank:false,
	        fieldLabel: '*角色名称',
	        typeAhead: true,
	        editable: false,
	        triggerAction: 'all',
	        lazyRender: true,
			mode: 'remote',
	        store:roleStore,
	        emptyText : '请选择...',
	        valueField: 'id',
	        displayField: 'role_name'
	    });
	    roleStore.load();
	    
	    // ------------------ form -------------------------    
	    var form1 = new Ext.form.FormPanel({
	        id: 'form1',
	        bodyStyle: 'padding:5px',
	        labelAlign:'right',
	        frame: true,
	        items: [
	              {
		              layout: 'column',
		              items:[
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
	                            fieldLabel: '*登录名',
	                            allowBlank: false,
	                            id: 'loginId'
	                        },
	                        {
	                            inputType: 'password',
	                            fieldLabel: '*密码',
	                            allowBlank: false,
	                            regex:/^[a-zA-Z0-9]{6,8}$/,
	                            id: 'password'
	                        },
	                        departComboBox,
	                        {
								fieldLabel: '*分配系数',
	                            allowBlank: false,
	                            regex:/^[0-9]{1,2}$/,
	                            id: 'counts'
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
								fieldLabel: '*用户名字',
	                            allowBlank: false,
	                            id: 'userName'
	                        },
	                        comboxEnable,
	                        find_role,
	                        signStatusCombox
	                    ]
	                }
	                ]
	              },
                  {
                    items:[
                      {
                       columnWidth:1,
                       layout:'form',
                       labelWidth:80,
                       items:[
                          {
                             fieldLabel:'备注',
			                 allowBlank:true,
			                 xtype:'textarea',
			                 width:468,
			                 height:80,
			                 id:'remark'
                          }
                        ]
                      }
                    ]
                  },
                  {
                  	xtype: 'label',
                  	text: '(密码必须为6位)',
                  	style: 'color: red'
                  }
	          ]
	    });
	    
	    var form1Window = new Ext.Window({
	        id:'formWindow',
	        width: 620,
	        height: 350,
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
	    myGridLoadAction.execute();
	    
	});
	</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
