<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
			
	
	    // --------------- grid store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/role/loadRole.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'roleName' },
	            { name: 'createTime'},
	            { name: 'createUser'},
	            { name: 'roleCode'},
	            { name: 'roleCodeName'},
	            { name: 'remark'},
	            { name: 'remind'}
	        ]
	    });
	    
	    var menuRoleId='';
	    	    //--------用户类型
        var comboxData1 = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '201201', '管理员'],
				    [ '201202', '部门经理'],
				    [ '201203', '员工'],
				    [ '201204', '行政经理'],
				    [ '201205', '统计员'],
				    [ '201206', '客户导入员'],
				    [ '201207', '贷后管理员'],
				    [ '201208', '市场调研员']
		           ]
		     });
		var comboxType1 = new Ext.form.ComboBox({
		      fieldLabel : '*查看权限',
		      id : 'comboxType1',
		      store : comboxData1,
		      hiddenName : 'roleCode',
		      editable:false,
		      allowBlank: false,
		      width:180,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...' // 默认值   selectOnFocus : true,
	    });
	    
	    //----------分配提醒
	      var remindData1 = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '1', '提醒'],
				    [ '2', '不提醒']
		           ]
		     });
		var remindType1 = new Ext.form.ComboBox({
		      fieldLabel : '*分配提醒',
		      id : 'remindType1',
		      store : remindData1,
		      hiddenName : 'remind',
		      editable:false,
		      allowBlank: false,
		      width:180,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...' // 默认值   selectOnFocus : true,
	    });
	    
	    
	    
	    var menuStore = new Ext.data.JsonStore({
			url: '<%=path%>/menu/loadRoleWithMenu.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	_roleId:menuRoleId
	        },
	        fields: [
	            { name: 'mId' },
	            { name: 'mPName'},
	            { name: 'mURL'},
	            { name: 'mIndex'},
	            { name: 'mCaption'},
	            { name: 'mIcon'},
	            { name: 'mHint'}
	        ]
	    });
	    
	    menuStore.on("beforeload", function(thiz, options) {
	        //debugger
	        thiz.baseParams["_roleId"] = menuRoleId;
	    });
	    
		var user_store = new Ext.data.JsonStore({
			url: '<%=path%>/user/loadRoleWithUser.do',
			root: 'data',
			totalProperty: 'totalCount',
			idProperty: 'id',
			autoDestroy: true,
			baseParams: {
				_sel_name:'',
				_roleId:menuRoleId,
				_storeType:'2'
			},
			fields: [
	            { name: 'id' },
	            { name: 'loginId' },
	            { name: 'userName'},
	            { name: 'sex'},
	            { name: 'customerName'},
				{ name: 'userType'},
				{ name: 'pmtName'},
				{ name: 'groupName'},
	            { name: 'remark'}
	        ]
		});
		
		user_store.on("beforeload", function(thiz, options) {
	        //debugger
	        thiz.baseParams["_roleId"] = menuRoleId;
	        thiz.baseParams["_storeType"] = '2';
	    });
		
		var user_left_store = new Ext.data.JsonStore({
			url: '<%=path%>/user/loadRoleWithUser.do',
			root: 'data',
			totalProperty: 'totalCount',
			idProperty: 'id',
			autoDestroy: true,
			baseParams: {
				_sel_name:'',
				_roleId:menuRoleId,
				_storeType:'1'
			},
			fields: [
	            { name: 'id' },
	            { name: 'loginId' },
	            { name: 'userName'},
	            { name: 'sex'},
	            { name: 'customerName'},
				{ name: 'userType'},
				{ name: 'pmtName'},
				{ name: 'groupName'},
	            { name: 'remark'}
	        ]
		});
	    
	    user_left_store.on("beforeload", function(thiz, options) {
	        //debugger
	        thiz.baseParams["_roleId"] = menuRoleId;
	        thiz.baseParams["_storeType"] = '1';
	    });
	    
	    var cgrtColumnAction = new Ext.Action({
	        text: '配置菜单标题',
	        iconCls: 'list_users',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	            	menuRoleId = record.get('id');
	            	Ext.getCmp('ids').setValue(record.get('id'));
	            	Ext.getCmp('role_name').setValue(record.get('roleName'));
	            	Ext.getCmp('role_remark').setValue(record.get('remark'));
	            	menuStore.load({
				    	params:{
			                start:0, 
			                limit:10,
			                _roleId:menuRoleId
			            },
			            waitTitle:'提示',waitMsg: '数据加载请稍后...',
			            
			            failure: function() {
			                Ext.Msg.alert('提示', '读取数据失败！');                             
			            }
				    });
				    ds.load({
				    	params:{
			                start:0, 
			                limit:13,
			                _roleId:menuRoleId
			            },
			            waitTitle:'提示',waitMsg: '数据加载请稍后...',
			            failure: function() {
			                Ext.Msg.alert('提示', '读取数据失败！');                             
			            }
				    });
	                form2Window.show();
	                form2.getForm().loadRecord(record);
	            }
	        }
	    });


		var cgrtUserAction = new Ext.Action({
	        text: '配置用户',
	        iconCls: 'list_users',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	            	menuRoleId = record.get('id');
	            	Ext.getCmp('u_id').setValue(record.get('id'));
	            	Ext.getCmp('u_role_name').setValue(record.get('roleName'));
	            	Ext.getCmp('u_role_remark').setValue(record.get('remark'));
				    user_left_store.load({
				    	params:{
			                start:0, 
			                limit:10,
			                _roleId:menuRoleId,
			                _storeType:'1'
			            },
			            waitTitle:'提示',waitMsg: '数据加载请稍后...',
			            failure: function() {
			                Ext.Msg.alert('提示', '读取数据失败！');                             
			            }
				    });
				    
				    user_store.load({
				    	params:{
			                start:0, 
			                limit:10,
			                _roleId:menuRoleId,
			                _storeType:'2'
			            },
			            waitTitle:'提示',waitMsg: '数据加载请稍后...',
			            failure: function() {
			                Ext.Msg.alert('提示', '读取数据失败！');                             
			            }
					});
	                form3Window.show();
	                form3.getForm().loadRecord(record);
	            }
	        }
	    });
	    
	    // --------------- grid actions -----------------
	    var myGridNewAction = new Ext.Action({
	        text: '新增角色',
	        iconCls: 'smt-roleAdd',
	        scale: 'small',
	        handler: function(){
	            form1.getForm().reset();
	            Ext.getCmp('formWindow').setTitle('新增角色信息');
	            form1Window.show();
	        }
	    });
	    
	    var myGridEditAction = new Ext.Action({
	        text: '编辑角色',
	        iconCls: 'smt-roleEdit',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	            	comboxType1.setValue(record.get('roleCode'));
	            	comboxType1.setRawValue(record.get('roleCodeName'));
	            	Ext.getCmp('formWindow').setTitle('编辑角色信息');
	                form1Window.show();
	                form1.getForm().loadRecord(record);
	            }
	        }
	    });
	            
	    var myGridDeleteAction = new Ext.Action({
	        text: '删除角色',
	        iconCls: 'smt-roleDelete',
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
	                            url: '<%=path%>/role/deleteRole.do',
	                            params: {
	                                id: id
	                            },
	                            success: function(aResponse, aOptions){
	                                masterStore.reload({callback: myGridUpdateAction});
	                                Ext.MessageBox.alert('提示','删除成功！');
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
	                cgrtColumnAction.enable();
	                cgrtUserAction.enable();
	            }else{
	                myGridEditAction.disable();
	                myGridDeleteAction.disable();
	                cgrtColumnAction.disable();
	                cgrtUserAction.disable();
	            }
	    };
	    
	     var checkType = function(userType){
	       if(userType == '1'){
	         return '员工';
	       }else if(userType == '2'){
	       	 return '客户';
	       }else{
		   	return '其他';
		   }
	    };
	    var checkState = function(userState){
	       if(userState=='0'){
	         return '正常';
	       }else if(userState == '1'){
	         return '禁用';
	       }else{
	         return '删除';
	       }
	    };
	<!--  性别start  -->
	 var checkSex = function(userState){
	       if(userState=='0'){
	         return '男';
	       }else if(userState == '1'){
	         return '女';
	       }
	    };
	<!--性别 end-->
	    var checkCertificate = function(userCertificateType){
	       if(userCertificateType=='1'){
	         return '身份证';
	       }else if(userCertificateType=='2'){
	         return '学生证';
	       }else if(userCertificateType=='3'){
	         return '军官证';
	       }else if(userCertificateType=='4'){
	         return '驾驶证';
	       }else{
	         return '其它';
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
	                hidden:true,
	                header: '编号',
	                sortable: true,
	                dataIndex: 'id'
	            },
	            {
	            
	            	header: '角色名',
	                sortable: true,
	                dataIndex: 'roleName'
	            },
	             {
	            
	            	header: '创建时间',
	                sortable: true,
	                dataIndex: 'createTime'
	            },
	             {
	            
	            	header: '查看权限',
	                sortable: true,
	                dataIndex: 'roleCodeName'
	            },
	            {
	               header: '分配提醒',
	                sortable: true,
	                dataIndex: 'remind'
	            },
	             {
	            
	            	header: '创建人',
	                sortable: true,
	                dataIndex: 'createUser'
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'remark'
	            }
	        ],
	        tbar: [
	            myGridNewAction,
	            myGridEditAction,           
	            myGridDeleteAction,
<%--				cgrtUserAction,--%>
	            cgrtColumnAction
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
	
	    var form2SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	        	var ids = ''
	            var records = Ext.getCmp('menuGrid').getSelectionModel().getSelections();
	            var roleId = Ext.getCmp('ids').getValue();
	            if(records != ''){
		            for(var i = 0, len = records.length; i < len; i ++) {
		               ids = ids + records[i].id + ',';
		            }
	        	}else{
	        		Ext.Msg.alert('提示', '请选择要配置的菜单项！');              
	        		return false;
	        	}
	            if(form2.getForm().isValid()){
	                form2.getForm().submit({
	                    url: '<%=path%>/role/saveMenuWithRole.do',
	                    params: {
	                        _ids:ids,
	                        _roleId:roleId
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                    	Ext.Msg.alert('提示', '保存成功！'); 
	                    	menuStore.load({
						    	params:{
					                start:0, 
					                limit:10,
					                _roleId:menuRoleId
					            },
					            waitTitle:'提示',waitMsg: '数据加载请稍后...',
					            failure: function() {
					                Ext.Msg.alert('提示', '读取数据失败！');                             
					            }
						    });
						    ds.load({
						    	params:{
					                start:0, 
					                limit:13,
					                _roleId:menuRoleId
					            },
					            waitTitle:'提示',waitMsg: '数据加载请稍后...',
					            failure: function() {
					                Ext.Msg.alert('提示', '读取数据失败！');                             
					            }
						    });
	                    },
	                    failure: function(aForm, aAction) {
	                    	var result = Ext.decode(aAction.response.responseText);
	                        Ext.Msg.alert('提示', result.msg);                             
	                    }
	                });
	            }
	        }
	    });
	    
	    
	    var form3SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	        	var ids = ''
	            var records = Ext.getCmp('userGrid').getSelectionModel().getSelections();
	            var roleId = menuRoleId;
	            if(records != ''){
		            for(var i = 0, len = records.length; i < len; i ++) {
		               ids = ids + records[i].get('id') + ',';
		            }
	        	}else{
	        		alert('请选择要配置的菜单项！'); 
	        		return false;
	        	}
	            if(form3.getForm().isValid()){
	                form3.getForm().submit({
	                    url: '<%=path%>/role/saveUserWithRole.do',
	                    params: {
	                        _ids:ids,
	                        _roleId:roleId
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                    	Ext.Msg.alert('提示', '保存成功！'); 
	                    	    user_left_store.load({
							    	params:{
						                start:0, 
						                limit:10,
						                _roleId:menuRoleId,
						                _storeType:'1'
						            },
						            waitTitle:'提示',waitMsg: '数据加载请稍后...',
						            failure: function() {
						                Ext.Msg.alert('提示', '读取数据失败！');                             
						            }
							    });
							    user_store.load({
							    	params:{
						                start:0, 
						                limit:10,
						                _roleId:menuRoleId,
						                _storeType:'2'
						            },
						            waitTitle:'提示',waitMsg: '数据加载请稍后...',
						            failure: function() {
						                Ext.Msg.alert('提示', '读取数据失败！');                             
						            }
								});
	                    },
	                    failure: function(aForm, aAction) {
	                    	var result = Ext.decode(aAction.response.responseText);
	                        Ext.Msg.alert('提示', result.msg);                             
	                    }
	                });
	            }
	        }
	    });
	    
	    var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	            var _comboxType1=Ext.getCmp('comboxType1').getValue();
	            var _remindType1 =Ext.getCmp('remindType1').getValue();
	            var _store = myGrid.getStore();
	            var flag = 0;
	            if(flag == 1){
	            	Ext.MessageBox.alert('提示', '查看权限管理员无法重复添加！');
	            }else{
	            	if(form1.getForm().isValid()){
		                form1.getForm().submit({
		                    url: '<%=path%>/role/saveOrUpdateUser.do',
		                    params: {
		                        _comboxType1:_comboxType1,
		                        _remindType1:_remindType1
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
	    
	    var form3ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls: 'returns',
	        handler: function(){
	           form3Window.hide();
	        }
	    });
	    // ------------------ form -------------------------    
	    var form1 = new Ext.form.FormPanel({
	        id: 'form1',
	        bodyStyle: 'padding:5px',
	        layout: 'form',
	        labelAlign:'right',
	        defaultType: 'textfield',
	        frame: true,
	        items: [
         		   	  {	
						  inputType: 'hidden',
                          fieldLabel: '*编号',
                          id: 'id'
                      },
                      comboxType1,
                      {
                          fieldLabel: '*角色名',
                          allowBlank: false,
                           width:180,
                          id: 'roleName'
                      },
                      remindType1,
                      {
                          fieldLabel: '备注',
                          xtype:'textarea',
                           width:180,
                           height:70,
                          id: 'remark'
                      }
	        		]
	    });
	    
	    
	<%--  ds是右边查询所有子菜单项   --%>
	    var ds = new Ext.data.JsonStore({
			url: '<%=path%>/menu/loadChildMenu.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	_sel_name:'',
	        	_roleId:menuRoleId
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
	    ds.on("beforeload", function(thiz, options) {
	        thiz.baseParams["_roleId"] = menuRoleId;
	    });
	    
	    var mColModel = new Ext.grid.ColumnModel([
          		{
          			hidden:true,
	                header: '编号',
	                sortable: true,
	                dataIndex: 'mId'
	            },
	            {
	            
	            	header: '菜单标题',
	                sortable: true,
	                dataIndex: 'mCaption'
	            },
	            {
	                header: '父级菜单标题',
	                sortable: true,
	                dataIndex: 'mPName'
	            },
	            {
	                header: '资源路径',
	                sortable: true,
	                dataIndex: 'mURL'
	            },
	            {
	                header: '图标',
	                sortable: true,
	                dataIndex: 'mIcon'
	            },
	            {
	                header: '排列顺序',
	                sortable: true,
	                dataIndex: 'mIndex'
	            },
	            {
	                header: '提示',
	                sortable: true,
	                dataIndex: 'mHint'
	            }
      	]);
	    
	    var form2 = new Ext.form.FormPanel({
	        id: 'form2',
	        bodyStyle: 'padding:5px',
	        width:450,
	        frame: true,
	        items: [{
	            layout: 'column',
	            items: [
	                {
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
	                            id: 'ids'
	                        },
	                        {
	                            fieldLabel: '*角色名',
	                            readOnly:true,
	                            id: 'role_name'
	                        }
	                    ]
	                },
	                {
	                    layout: 'form',
	                    labelWidth: 60,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 120,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
	                            fieldLabel: '&nbsp;备注',
	                            readOnly:true,
	                            id: 'role_remark'
	                        }
	                    ]
	                }
	            ]        
	        },{
	        	 columnWidth: 0.60,
	             layout: 'fit',
	             bodyStyle:'margin-top:5px;',
	             items: {
	        	    xtype: 'grid',
		        	id:'selectTypeId',
		        	title:'绑定的菜单信息',
		        	frame:true,
	                ds: menuStore,
	                cm: mColModel,
	                sm: new Ext.grid.RowSelectionModel(),
	                height: 355,
	                width: 440,
	                tbar: [
			            {
			            	text:'删除菜单项',
			            	iconCls: 'btn_del',
	        				scale: 'small',
							handler:function(){
								  if (Ext.getCmp('selectTypeId').getSelectionModel().hasSelection()) {
						                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
						                    if (aButton == 'yes'){
						                        var id = ''
						                        var records = Ext.getCmp('selectTypeId').getSelectionModel().getSelections();
						                        for(var i = 0, len = records.length; i < len; i ++) {
						                          id = id + records[i].get('mId') + ',';
						                        }
						                        Ext.Ajax.request({
						                            url: '<%=path%>/role/deleteRoleWithMenu.do',
						                            params: {
						                                id:id,
						                                _roleId:menuRoleId
						                            },
						                            success: function(aResponse, aOptions){
						                                Ext.MessageBox.alert('提示','删除成功！');
						                                menuStore.load({
															params:{
												                start:0, 
												                limit:10,
												                _roleId:menuRoleId
												            },
												            waitTitle:'提示',waitMsg: '数据加载请稍后...',
												            failure: function() {
												                Ext.Msg.alert('提示', '读取数据失败！');                             
												            }
														});
														ds.load({
															params:{
												                start:0, 
												                limit:13,
												                _roleId:menuRoleId
												            },
												            waitTitle:'提示',waitMsg: '数据加载请稍后...',
												            failure: function() {
												                Ext.Msg.alert('提示', '读取数据失败！');                             
												            }
														});
						                            },
						                            failure: function(aResponse, aOptions){
						                                Ext.MessageBox.alert('提示', "删除失败！");
						                            }
						                        })
						                    }
						                });
	            					}else{
	            						Ext.MessageBox.alert('提示', "请选择要删除的菜单项！");
	            						return false;
	            					}
							}
			            }
			        ],
	                bbar: new Ext.PagingToolbar({
	                	id:'roleMenuToolbar',
			            pageSize: 10,
			            store: menuStore,
			            displayInfo: true,
			            displayMsg: '显示: {0} - {1} / 总数: {2}',
			            emptyMsg: '没有记录'
		        	})
	        	}
	        }]
	    });
	    
		<%--  菜单栏目的colModel和grid  --%>
	    var colModel = new Ext.grid.ColumnModel([
          		{
          			hidden:true,
	                header: '编号',
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
         ]);
	    
	    var menuGrid = new Ext.grid.GridPanel({
	        id: 'menuGrid',
	        store: ds,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        cm: colModel,
	        tbar: [
	            '菜单标题：',
               	{
					id:'sel_name34',
					xtype:'textfield',
					width:100
				},
	            {
	            	text:'查询',
	            	iconCls: 'check',
	        		scale: 'small',
					handler:function(){
						var _sel_name = Ext.getCmp('sel_name34').getValue();
						ds.load({
							params:{
				                start:0, 
				                limit:13,
				                _sel_name:_sel_name,
				                _roleId:menuRoleId
				            },
				            waitTitle:'提示',waitMsg: '数据加载请稍后...',
				            failure: function() {
				                Ext.Msg.alert('提示', '读取数据失败！');                             
				            }
						});
					}
	            },
	            form2SaveAction
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 13,
	            store: ds,
	            displayInfo: true,
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });

		<%--  用户colModel和grid  --%>
		user_colModel = new Ext.grid.ColumnModel([
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
	                header: '用户姓名',
	                sortable: true,
	                dataIndex: 'userName'
	            },
	            {
	                header: '用户类型',
	                sortable: true,
	                dataIndex: 'userType',
	                renderer:checkType
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'remark'
	            }
		]);
		user_left_colModel = new Ext.grid.ColumnModel([
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
	                header: '用户姓名',
	                sortable: true,
	                dataIndex: 'userName'
	            },
	            {
	                header: '用户类型',
	                sortable: true,
	                dataIndex: 'userType',
	                renderer:checkType
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'remark'
	            }
		]);

		var userGrid = new Ext.grid.GridPanel({
	        id: 'userGrid',
	        store: user_store,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        cm: user_colModel,
	        tbar: [
	            '&nbsp;&nbsp;姓名：',
               	{
					id:'user_name',
					xtype:'textfield',
					width:100
				},
	            {
	            	text:'查询',
	            	iconCls: 'check',
	        		scale: 'small',
					handler:function(){
						var _sel_name = Ext.getCmp('user_name').getValue();
						user_store.load({
							params:{
				                start:0, 
				                limit:13,
				                _sel_name:_sel_name,
				                _roleId:menuRoleId,
				                _storeType:'2'
				            },
				            waitTitle:'提示',waitMsg: '数据加载请稍后...',
				            failure: function() {
				                Ext.Msg.alert('提示', '读取数据失败！');                             
				            }
						});
				            Ext.getCmp('user_name').setValue('');
					}
	            },
	            form3SaveAction
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 13,
	            store: user_store,
	            displayInfo: true,
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
		<%--用户form--%>
		var form3 = new Ext.form.FormPanel({
	        id: 'form3',
	        bodyStyle: 'padding:5px',
	        width:450,
	        frame: true,
	        items: [{
	            layout: 'column',
	            items: [
	                {
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
	                            id: 'u_id'
	                        },
	                        {
	                            fieldLabel: '*角色名',
	                            readOnly:true,
	                            id: 'u_role_name'
	                        }
	                    ]
	                },
	                {
	                    layout: 'form',
	                    labelWidth: 60,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 120,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
	                            fieldLabel: '&nbsp;备注',
	                            readOnly:true,
	                            id: 'u_role_remark'
	                        }
	                    ]
	                }
	            ]        
	        },{
	        	 columnWidth: 0.60,
	             layout: 'fit',
	             bodyStyle:'margin-top:5px;',
	             items: {
	        	    xtype: 'grid',
		        	id:'leftUserGrid',
		        	title:'绑定的用户信息',
		        	frame:true,
	                ds: user_left_store,
	                cm: user_left_colModel,
	                sm: new Ext.grid.RowSelectionModel(),
	                height: 355,
	                width: 440,
	                tbar: [
			            {
			            	text:'删除用户',
			            	iconCls:'btn_del',
	        				scale: 'small',
							handler:function(){
								  if (Ext.getCmp('leftUserGrid').getSelectionModel().hasSelection()) {
						                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
						                    if (aButton == 'yes'){
						                        var id = ''
						                        var records = Ext.getCmp('leftUserGrid').getSelectionModel().getSelections();
						                        for(var i = 0, len = records.length; i < len; i ++) {
						                          id = id + records[i].get('id') + ',';
						                        }
						                        Ext.Ajax.request({
						                            url: '<%=path%>/role/deleteRoleWithUser.do',
						                            params: {
						                                id:id,
						                                _roleId:menuRoleId
						                            },
						                            success: function(aResponse, aOptions){
						                                Ext.MessageBox.alert('提示','删除成功！');
						                                user_left_store.load({
															params:{
												                start:0, 
												                limit:10,
												                _roleId:menuRoleId,
												                _storeType:'1'
												            },
												            waitTitle:'提示',waitMsg: '数据加载请稍后...',
												            failure: function() {
												                Ext.Msg.alert('提示', '读取数据失败！');                             
												            }
														});
														 user_store.load({
															params:{
												                start:0, 
												                limit:10,
												                _roleId:menuRoleId,
												                _storeType:'2'
												            },
												            waitTitle:'提示',waitMsg: '数据加载请稍后...',
												            failure: function() {
												                Ext.Msg.alert('提示', '读取数据失败！');                             
												            }
														});
						                            },
						                            failure: function(aResponse, aOptions){
						                                Ext.MessageBox.alert('提示', "删除失败！");
						                            }
						                        })
						                    }
						                });
	            					}else{
	            						Ext.MessageBox.alert('提示', "请选择要删除的菜单项！");
	            						return false;
	            					}
							}
			            }
			        ],
	                bbar: new Ext.PagingToolbar({
			            pageSize: 10,
			            store: user_left_store,
			            displayInfo: true,
			            displayMsg: '显示: {0} - {1} / 总数: {2}',
			            emptyMsg: '没有记录'
		        	})
	        	}
	        }]
	    });
	    
	    var nav = new Ext.Panel({
            region: 'west',
            width: 450,
            margins:'3 0 3 3',
            cmargins:'3 3 3 3',
            items:[
            	form2
            ]
        });
        
        var centerPanel = new Ext.Panel({
            title: '菜单标题',
            region: 'center',
            layout:'fit',
            split: true,
            width: 450,
            margins:'3 0 3 5',
            cmargins:'3 3 3 3',
            items:[
            	menuGrid
            ]
        });

		var user_nav = new Ext.Panel({
            region: 'west',
            width: 450,
            margins:'3 0 3 3',
            cmargins:'3 3 3 3',
            items:[
            	form3
            ]
        });
        
        var user_cPanel = new Ext.Panel({
            title: '用户信息',
            region: 'center',
            layout:'fit',
            split: true,
            width: 450,
            margins:'3 0 3 5',
            cmargins:'3 3 3 3',
            items:[
            	userGrid
            ]
        });
	
	    var form1Window = new Ext.Window({
	       id:'formWindow',
	        width: 390,
	        height: 250,
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
	        width: 1000,
	        height: 485,
	        title:'配置栏目菜单',
	        modal: true,
	        layout: 'border',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: [nav,centerPanel],
	        closeAction: 'hide',
	        buttons: [
	                form2ReturnAction
	            ]
	    });

		var form3Window = new Ext.Window({
	        width: 1000,
	        height: 485,
	        title:'配置用户',
	        modal: true,
	        layout: 'border',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: [user_nav,user_cPanel],
	        closeAction: 'hide',
	        buttons: [
	                form3ReturnAction
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