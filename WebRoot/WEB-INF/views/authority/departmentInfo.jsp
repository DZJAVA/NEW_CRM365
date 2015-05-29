<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
	    // --------------- grid store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/tt/loadDepartment.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:''
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'depaName'},
	            { name: 'depaNotes'},
	            { name: 'orderStatus'},
	            { name: 'superId'},
	            { name: 'superName'},
	            { name: 'isFront'},
	            { name: 'super_id'},
	            { name: 'remark'}
	        ]
	    });
	          // --------------- grid actions -----------------
	    var myGridNewAction = new Ext.Action({
	        text: '新增部门信息',
	        iconCls: 'smt-deparmentAdd',
	        scale: 'small',
	        handler: function(){
	            form1.getForm().reset();
	            Ext.getCmp('formWindow').setTitle('新增部门信息');
	            form1Window.show();
	            SUPERID = 0;
	        }
	    });
	    var SUPERID = 0;
	    var myGridEditAction = new Ext.Action({
	        text: '编辑部门信息',
	        iconCls: 'smt-deparmentEdit',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected();  
	            if(record != null){
	                Ext.getCmp('formWindow').setTitle('编辑部门信息');
	                form1Window.show();
	                form1.getForm().loadRecord(record);
	                if(record.get("superId")){
	                	departSelComboBox.setValue(record.get("superId"));
	                	departSelComboBox.setRawValue(record.get("superName"));
	                }else{
	                	departSelComboBox.setValue("");
	                	departSelComboBox.setRawValue("");
	                }
	                SUPERID = record.get("super_id");
	                console.log(SUPERID);
	            }
	        }
	    });
	            
	    var myGridDeleteAction = new Ext.Action({
	        text: '删除部门信息',
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
	                            url: '<%=path%>/tt/deleteDepartment.do',
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
	                                masterStore.reload({callback: myGridUpdateAction});
				                    Ext.MessageBox.alert('提示', result.msg);
	                            }
	                        })
	                    }
	                });
	            }
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
		      hiddenName : 'orderStatus'  
	    });
	    
	    var isFrontData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ 1, '前台'],
			    [ 2, '后台']
		      ]
		});
		var isFrontCombox = new Ext.form.ComboBox({
		      fieldLabel : '前后台标识',
		      id : 'isFrontCombox',
		      store : isFrontData,
		      editable:false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...', 
		      hiddenName : 'isFront'  
	    });
		var searchButton = {
			xtype:'button',
			iconCls: 'check',
			text:'查 询',
			handler:function(){
				var dn=Ext.getCmp('depName').getValue();
					Ext.apply(masterStore.baseParams,{
						conditions:'{dn:"'+dn+'"}'
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
				Ext.getCmp('depName').setValue('');
			}
		}
			
		var oneTbar=new Ext.Toolbar({
		  	items:[
		  		'部门名称：',
				 {
		     	 	xtype:'textfield',
		     	 	width:80,
		     	 	id:'depName'
		     	 },
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
	                    Ext.Msg.alert('提示','读取数据失败！');                             
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
	            	header: '部门名称',
	                sortable: true,
	                width:200,
	                dataIndex: 'depaName'
	            },
	            {
	            	header: '部门说明',
	            	width:200,
	                sortable: true,
	                dataIndex: 'depaNotes'
	            },
	            {
	            	header: '上级部门',
	            	width:200,
	                sortable: true,
	                dataIndex: 'superName'
	            },
	            {
	            	header: '接单状态',
	            	width:200,
	                sortable: true,
	                dataIndex: 'orderStatus'
	            },
	            {
	            	header: '前后台标识',
	            	width:200,
	                sortable: true,
	                dataIndex: 'isFront', renderer: function(val, meda, record){
		            	if(val === 0){
		            		record.data.isFront = null;
		            	}
		             	switch(val){
		             		case 1: return '前台';
		             		case 2: return '后台';
		             		default: return '';
		             	}
		            }
	            },
	            {
	            	header: '备注',
	            	width:200,
	                sortable: true,
	                dataIndex: 'remark'
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
	    
	    var form1ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls: 'returns',
	        handler: function(){
	           form1Window.hide();
	        }
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
	        	if(form1.getForm().isValid()){
	        		var status = Ext.getCmp('signStatusCombox').getValue();
	        		if(status == '0' | status == '不接单'){
	        			status = '0';
	        		}else if(status == '1' | status == '接单'){
	        			status = '1'
	        		}
	        		var superId = departSelComboBox.getValue();
	        		var super_id = 0;
	        		if(superId){
	        			var record = departmentStore.getById(superId);
	        			super_id = record.get("superId");
	        		}else{
	        			superId = 0;
	        		}
	        		if(SUPERID) super_id = SUPERID;
	        		console.log(SUPERID);
	                form1.getForm().submit({
	                    url: '<%=path%>/tt/qq.do',
	                    params: {
	                    	status: status,
	                    	superId: superId,
	                    	super_Id: super_id
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                        form1Window.hide();
	                        masterStore.reload({callback: myGridUpdateAction});
	                        departmentStore.load();
	                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
	                    },
	                    failure: function(aForm, aAction) {
	                       Ext.MessageBox.alert('提示', aAction.result.msg);                           
	                    }
	                });
	            }
	        }
	    });
	    var departmentStore = new Ext.data.Store({
	          proxy: new Ext.data.HttpProxy({
	              url: '<%=path%>/tt/loadDepartments.do'
	          }),
	          reader: new Ext.data.JsonReader({
	                  root: 'data',
	                  id: 'id'
	              }, 
	            ['id', 'depaName', 'superId']
	          )
		});
		departmentStore.load();
		var departSelComboBox = new Ext.form.ComboBox({
		      id: 'departSelComboBox',
		      fieldLabel : '上级部门',
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
		      lazyRender: true,
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'id',
		      displayField: 'depaName'
		  });
	    var form1 = new Ext.form.FormPanel({
	        id: 'form1',
	        bodyStyle: 'padding:5px',
	        labelAlign:'right',
	        layout: 'form',
	        defaultType: 'textfield',
	        frame: true,
	        labelWidth:70,
	        defaults:{
	          width:190,
	          msgTarget:'size'
	        },
	        items: [
                      {
					      inputType: 'hidden',
                          fieldLabel: '*编号',
                          id: 'id'
                      },
                       {
                          fieldLabel: '*部门名字',
                          allowBlank: false,
                          id: 'depaName'
                      },
                       {
                          fieldLabel: '*部门说明',
                          allowBlank: false,
                          id: 'depaNotes'
                      },
                      departSelComboBox,
                      isFrontCombox,
                      signStatusCombox,
                      {
                          fieldLabel: '备注',
                          xtype:'textarea',
                          height:70,
                          allowBlank: true,
                          id: 'remark'
                      }
	        ]
	    });
	    
	    var form1Window = new Ext.Window({
	        id:'formWindow',
	        width: 350,
	        height: 300,
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
	     // bind event
	    myGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction
	    );
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
