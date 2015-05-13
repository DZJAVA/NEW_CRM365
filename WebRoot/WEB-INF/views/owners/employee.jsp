<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
	<script type="text/javascript"><!--
		Ext.onReady(function(){
		Ext.QuickTips.init();// 浮动信息提示
    	Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
    		// --------------- grid store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/employee/loadEmployee.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:''
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'name' },
	            { name: 'dId'},
	            { name: 'dName'},
	            { name: 'sex'},
	            { name: 'age' },
	            { name: 'IDcard'},
	            { name: 'mailbox'},
	            { name: 'idCardNum'},
	            { name: 'address'},
	            { name: 'counts'},
	            { name: 'signStatus'},
	            { name: 'birthday'},
                { name: 'marriage'},
                { name: 'marriageName'},
                { name: 'state'},
                { name: 'stateName'},
	            { name: 'remark'}
	        ]
	    });
	    
	   var myGridNewAction = new Ext.Action({
	        text: '新增员工信息',
	        iconCls: 'smt-userAdd',
	        scale: 'small',
	        handler: function(){
	            form1.getForm().reset();
	            Ext.getCmp('formWindow').setTitle('新增员工信息');
	            form1Window.show();
	        }
	    });
	    //-------修改接单状态---------
	    function changeStatus(flag){
	    	if (myGrid.getSelectionModel().hasSelection()) {
                Ext.Msg.confirm('确定修改', '是否修改选择的记录?', function(aButton){
                    if (aButton == 'yes'){
                        var id = ''
                        var records = myGrid.getSelectionModel().getSelections();
                        for(var i = 0, len = records.length; i < len; i ++) {
                          id = id + records[i].id + ',';
                        }
                        Ext.Ajax.request({
                            url: '<%=path%>/employee/change.do',
                            params: {
                                id: id,
                                flag: flag
                            },
                            success: function(aResponse, aOptions){
                                var result = Ext.decode(aResponse.responseText);
                                masterStore.reload({callback: myGridUpdateAction});
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
	    
	    //-----接单按钮-----
	    var receiveAction = new Ext.Action({
	        text: '接单',
	        iconCls: 'drop-yes',
	        scale: 'small',
	        handler: function(){
	        	var flag = '1';
	        	changeStatus(flag);
	        }
	    });
	    //-----------不接单按钮--------
	    var notReceiveAction = new Ext.Action({
	        text: '不接单',
	        iconCls: 'drop-no',
	        scale: 'small',
	        handler: function(){
	        	var flag = '0';
	        	changeStatus(flag);
	        }
	    });
	    
	    //---------------签单状态下拉列表--------------------------
	    var signStatusData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '0', '不接单'],
			    [ '1', '接单']
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
		      emptyText : '请选择...', // 默认值   selectOnFocus : true,
		      hiddenName : 'signStatus'  
	    });
	   var marriageData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '1', '已婚'],
			    [ '2', '未婚']
		      ]
		});
		var marriageCombox = new Ext.form.ComboBox({
		      fieldLabel : '&nbsp;&nbsp;&nbsp婚姻状态',
		      id : 'marriageCombox',
		      store : marriageData,
		      editable:false,
		      allowBlank: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...' // 默认值   selectOnFocus : true,
	    });
	     var stateData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '1', '实习'],
			    [ '2', '见习'],
			    [ '3', '正式']
		      ]
		});
		var stateCombox = new Ext.form.ComboBox({
		      fieldLabel : '*在职状态',
		      id : 'stateCombox',
		      store : stateData,
		      editable:false,
		      allowBlank: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...' // 默认值   selectOnFocus : true,
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
			      hiddenName: 'dId',
				  allowBlank: false,
			      fieldLabel: '*所属部门',
			      typeAhead: true,
			      allowBlank: false,
			      editable: false,
			      triggerAction: 'all',
			      lazyRender: true,
				  mode: 'remote',
			      store:departmentStore,
			      valueField: 'departId',
			      displayField: 'departName'
			  });
			  
	    //-------------------员工信息表查询
	       var departmentStore1 = new Ext.data.Store({
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
			  
			  var departComboBox1 = new Ext.form.ComboBox({
			      id: 'departComboBox1',
			      fieldLabel: '*所属部门',
			      typeAhead: true,
			      width:100,
			      editable: false,
			      triggerAction: 'all',
			      lazyRender: true,
				  mode: 'remote',
			      store:departmentStore1,
			      valueField: 'departId',
			      displayField: 'departName'
			  });
			  
			   var positionStore1 = new Ext.data.Store({
			          proxy: new Ext.data.HttpProxy({
			              url: '<%=path%>/employee/loadPosition.do'
			          }),
			          reader: new Ext.data.JsonReader({
			                  root: 'data',
			                  id: 'pId'
			              }, 
			              ['pId', 'pName']
			          )
			  });
			  
			  var posiComboBox1 = new Ext.form.ComboBox({
			      id: 'posiComboBox1',
			      fieldLabel: '*员工职位',
			      typeAhead: true,
			      width:100,
			      editable: false,
			      triggerAction: 'all',
			      lazyRender: true,
				  mode: 'remote',
			      store:positionStore1,
			      valueField: 'pId',
			      displayField: 'pName'
			  });
			  
			  
			  var comboxData1 = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '1', '在职'],
				    [ '2', '离职'],
				    [ '3', '休假']
		           ]
		     });
		     
		   // 下拉列表框控件
		   var comboxType1 = new Ext.form.ComboBox({
		      fieldLabel : '*员工状态',
		      id : 'comboxType1',
		      store : comboxData1,
		      width:80,
		      editable:false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...' // 默认值   selectOnFocus : true,
	     });
	     
	      var sexcomboxData1 = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '1', '男'],
				    [ '2', '女']
		           ]
		     });
		     
		   // 下拉列表框控件
		   var sexcomboxType1 = new Ext.form.ComboBox({
		      fieldLabel : '&nbsp;&nbsp;&nbsp*员工性别',
		      id : 'sexcomboxType1',
		      store : sexcomboxData1,
		      allowBlank:false,
		      editable:false,
		      displayField : 'value', 
		      width:60,
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...', // 默认值   selectOnFocus : true,
		       hiddenName : 'sex'
	     });
	     
	var exportData = new Ext.Action({
        text: '导出数据',
        scale: 'small',
        id:'exportData',
        iconCls:'export',
        xtype:'button',
		handler: function(){
			var thisForm = document.createElement('form');
			var _name = Ext.getCmp('_name').getValue();
			var _departComboBox1 = Ext.getCmp('departComboBox1').getValue();
			thisForm.method = 'post';
			thisForm.action = '<%=path%>/employee/EmpPortDate.do'
			
			var namebox = document.createElement('input');
			namebox.type = 'hidden';
			namebox.name = '_name';
			namebox.value = _name;
			
			var depbox = document.createElement('input');
			depbox.type = 'hidden';
			depbox.name = '_departComboBox1';
			depbox.value = _departComboBox1;
			
			thisForm.appendChild(namebox);
			thisForm.appendChild(depbox);
			document.body.appendChild(thisForm);
			thisForm.submit();
		}
    });	  
			  var searchButton = {
				xtype:'button',
				iconCls: 'check',
				text:'查 询',
				handler:function(){
					var dcb=Ext.getCmp('departComboBox1').getValue();
					//var pc=Ext.getCmp('posiComboBox1').getValue();
					//var ct=Ext.getCmp('comboxType1').getValue();
					//var st=Ext.getCmp('sexcomboxType1').getValue();
					var _empName=Ext.getCmp('_name').getValue();
						Ext.apply(masterStore.baseParams,{
							conditions:'{dcb:"'+dcb+'",_empName:"'+_empName+'"}'
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
						//Ext.getCmp('posiComboBox1').setValue('');
						//Ext.getCmp('comboxType1').setValue('');
						//Ext.getCmp('sexcomboxType1').setValue('');
						Ext.getCmp('name').setValue('');
				}
			}
			
			var oneTbar=new Ext.Toolbar({
		  	items:[
		  		'员工名字：',
				 {
		     	 	xtype:'textfield',
		     	 	width:80,
		     	 	id:'_name'
		     	 },
		     	 '所属部门：',
				 departComboBox1,
				 searchButton,
				 exportData
			 ] });
			
	      function myGridUpdateAction (){
	            if(myGrid.getSelectionModel().hasSelection()){
	                myGridEditAction.enable();
	                myGridDeleteAction.enable();
	            }else{
	                myGridEditAction.disable();
	                myGridDeleteAction.disable();
	            }
	  	  };   
	    
		      var myGridEditAction = new Ext.Action({
		        text: '编辑员工信息',
		        iconCls: 'smt-userEdit',
		        scale: 'small',
		        disabled: true,
		        handler: function(){
		            var record = myGrid.getSelectionModel().getSelected(); 
		            if(record != null){
		                Ext.getCmp('formWindow').setTitle('编辑员工信息');
		                form1Window.show();
		                form1.getForm().loadRecord(record);
		                marriageCombox.setValue(record.get('marriage'));
		                marriageCombox.setRawValue(record.get('marriageName'));
		                stateCombox.setValue(record.get('state'));
		                stateCombox.setRawValue(record.get('stateName'));
		                departComboBox.setValue(record.get('dId'));
		                departComboBox.setRawValue(record.get('dName'));
		            }
		        }
		    });
	    
	    
	    var myGridDeleteAction = new Ext.Action({
	        text: '删除员工信息',
	        iconCls: 'smt-userDelete',
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
	                            url: '<%=path%>/employee/deleteEmployee.do',
	                            params: {
	                                id: id
	                            },
	                            success: function(aResponse, aOptions){
	                                var result = Ext.decode(aResponse.responseText);
	                                masterStore.reload({callback: myGridUpdateAction});
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
    		
    		   // --------------- grid --------------------
	    var myGrid = new Ext.grid.GridPanel({
	        id: 'myGrid',
	        store: masterStore,
	        sm: new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	        	new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
	            {
	            	header: '员工名字',
	                sortable: true,
	                dataIndex: 'name'
	            },
	            {
	            	header: '所属部门',
	                sortable: true,
	                dataIndex: 'dName'
	            },
	            {
	            	header: '性别',
	                sortable: true,
	                dataIndex: 'sex'
	            },
	            {
	            	header: '年龄',
	                sortable: true,
	                dataIndex: 'age'
	            },
    	        {
	            	header: '出生日期',
	                sortable: true,
	                dataIndex: 'birthday'
	            },
	            {
	            	header: '婚姻状况',
	                sortable: true,
	                dataIndex: 'marriageName'
	            },
	            {
	            	header: '在职状态',
	                sortable: true,
	                dataIndex: 'stateName'
	            },
	           {
	            	header: '手机号码',
	                sortable: true,
	                dataIndex: 'IDcard'
	            },
	            {
	            	header: '邮箱',
	                sortable: true,
	                dataIndex: 'mailbox'
	            },
	            {
	            	header: '身份证号码',
	                sortable: true,
	                dataIndex: 'idCardNum'
	            },
	            {
	            	header: '地址',
	                sortable: true,
	                dataIndex: 'address'
	            },
	             {
	            	header: '分配系数',
	                sortable: true,
	                dataIndex: 'counts'
	            },
	            {
	            	header: '接单状态',
	                sortable: true,
	                dataIndex: 'signStatus'
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
	            receiveAction,
	            notReceiveAction
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
	    
	     myGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction
	    );
	    
	      var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	        	var dComboBox =Ext.getCmp('departComboBox').getValue(); 
	        	var _sign = Ext.getCmp('signStatusCombox').getValue(); 
	        	var _sexcomboxType1=Ext.getCmp('sexcomboxType1').getValue();
	        	var _marriageCombox=Ext.getCmp('marriageCombox').getValue();
	        	var _stateCombox=Ext.getCmp('stateCombox').getValue();
	        	var gridStore = myGrid.getStore();
	        	var formId = Ext.getCmp('id').getValue();
	        	var falg = 0;
	        	for(var i=0;i<gridStore.data.length;i++){
	        	  if(formId==undefined){
	        	      if(gridStore.getAt(i).get('name')==Ext.getCmp('name').getValue()){
		        	      falg=1;
		        	      break;
	        	       }
	        	  }else if(formId==gridStore.getAt(i).id){
	        	     if(gridStore.getAt(i).get('name')==Ext.getCmp('name').getValue()){
		        	      falg=2;
		        	      break;
	        	       }
	        	  }else if(gridStore.getAt(i).get('name')==Ext.getCmp('name').getValue()){
		        	      falg=1;
		        	      break;
	        	  }
	        	}
	        	if(falg==1){
	        	  Ext.MessageBox.alert('提示','该员工名称已存在');
	        	}else{
	        	  if(form1.getForm().isValid()){
	                form1.getForm().submit({
	                    url: '<%=path%>/employee/saveOrUpdateEmployee.do',
	                    params: {
	                        depart:dComboBox,
	                        _sign: _sign,
	                        _sexcomboxType1:_sexcomboxType1,
	                        _marriageCombox:_marriageCombox,
	                        _stateCombox:_stateCombox
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                   success: function(aForm, aAction){
	                        form1Window.hide();
	                        masterStore.reload({callback: myGridUpdateAction});
	                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
	                    },
	                    failure: function(aForm, aAction) {
	                       Ext.MessageBox.alert('提示', aAction.result.msg);                           
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
	    
		    var form1 = new Ext.form.FormPanel({
		        id: 'form1',
		        bodyStyle: 'padding:5px',
		        labelAlign:'right',
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
									fieldLabel: '*员工名字',
		                            allowBlank: false,
		                            id: 'name'
		                        },
		                        sexcomboxType1,
		                        marriageCombox,
		                         {
									fieldLabel: '出生日期',
		                            xtype:'datefield',
		                            format:'Y-m-d',
		                            id: 'birthday'
		                        },
		                         {
									fieldLabel: '身份证号码',
		                            regex:/^[0-9]{18}$/,
		                            id: 'idCardNum'
		                        },
		                        {
									fieldLabel: '手机号码',
		                            regex:/^[0-9]{11}$/,
		                            id: 'IDcard'
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
		                        departComboBox,
		                        stateCombox,
	                            signStatusCombox,
		                        {
									fieldLabel: '年龄',
		                            regex:/^[0-9]{1,3}$/,
		                            id: 'age'
		                        },
		                        {
									fieldLabel: '*分配系数',
		                            allowBlank: false,
		                            regex:/^[0-9]{1,2}$/,
		                            id: 'counts'
		                        },
		                        {
								fieldLabel: '邮箱',
								xtype:'textfield',
	                            regex:/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/,
	                            id: 'mailbox'
	                       		 }
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
									fieldLabel: '地址',
		                            xtype:'textfield',
		                            width:468,
		                            id: 'address'
	                        },
			                {
			                   fieldLabel:'备注',
			                   xtype:'textarea',
			                   width:468,
			                   height:80,
			                   id:'remark'
			                }
			             ]
			           }
		           ]
		         }
		        ]
		    });
	
		      var form1Window = new Ext.Window({
		        id:'formWindow',
		        title:'新增员工信息',
		        width: 620,
		        height: 390,
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
