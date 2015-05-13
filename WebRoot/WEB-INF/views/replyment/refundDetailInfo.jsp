<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<script type="text/javascript"><!--
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
    		
			var cid = ''; //客户资源
			var lid = ''; //贷款明细ID
			var fid = '';	//得到首页传过来的id
	
	    // --------------- grid store	签单成功Store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/loan/loadSuccessClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:''
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'clientId' },
	            { name: 'clientAdd' },
	            { name: 'clientName' },
	            { name: 'clientStatus' },
	            { name: 'contactTel' },
	            { name: 'emp_Name' },
	            { name: 'loanAmount' },
	            { name: 'oppType' },
	            { name: 'signPossible' },
	            { name: 'spareTel1' },
	            { name: 'spareTel2' },
	            { name: 'remark' }
	        ]
	    });
	    
	    //贷款明细Store
	    var loanStore = new Ext.data.JsonStore({
	        url: '<%=path%>/loan/findByClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'type' },
	            { name: 'sum' },
	            { name: 'rcount' },
	            { name: 'remark' }
	        ]
	    });
	    
	    //还款期数明细store
	    var refundStore = new Ext.data.JsonStore({
	        url: '<%=path%>/refund/findByLoanD.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'number' },
	            { name: 'rSum' },
	            { name: 'rTime' },
	            { name: 'factRTime' },
	            { name: 'status' }
	        ]
	    });
	    
	     	var searchButton = {
				xtype:'button',
				iconCls: 'check',
				text:'查 询',
				handler:function(){
					var _name=Ext.getCmp('_name').getValue();
					var _tel=Ext.getCmp('_tel').getValue();
						Ext.apply(masterStore.baseParams,{
							conditions:'{_name:"'+_name+'",_tel:"'+_tel+'"}'
					});
						masterStore.load({
				            params:{
				                start:0, 
				                limit:20,
				                fid:''
				            },
				            waitTitle:'提示',waitMsg: '数据加载请稍后...',
				            failure: function() {
				                Ext.Msg.alert('提示', '读取数据失败！');    

                         
				            }
				        });
				}
			}
			
			var myGridtijiaoAction = new Ext.Action({
	        text: '客户还款',
	        iconCls: 'check',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	        if (refundGrid.getSelectionModel().hasSelection()) {
	            	 var record = refundGrid.getSelectionModel().getSelected();
	            	 if(record.get('factRTime') != ''){
	            	 	Ext.MessageBox.alert('提示', '该期已还款！');
	            	 	return false;
	            	 }
	            	 }
                   	form3Window.show();
	        }
	    });
	    
	    	var sureAction = new Ext.Action({
	        text: '确定',
	        iconCls: 'check',
	        scale: 'small',
	        handler: function(){
	            if (refundGrid.getSelectionModel().hasSelection()) {
	            	 var record = refundGrid.getSelectionModel().getSelected();
	                        var id = ''
	                        id = record.id;
	                        var _factRTime = Ext.getCmp('factRTime').getValue();
	                        if(_factRTime != ''){
	                        	_factRTime = Ext.getCmp('factRTime').getValue().format('Y-m-d');
	                        }
	                        Ext.Ajax.request({
	                            url: '<%=path%>/refund/updateRefund.do',
	                            params: {
	                                id: id,
	                                _factRTime:_factRTime
	                            },
	                            success: function(aResponse, aOptions){
	                            	form3Window.hide();
								    refundLoadAction.execute();
	                                var result = Ext.decode(aResponse.responseText);
				                    Ext.MessageBox.alert('提示', result.msg);
	                            },
	                            failure: function(aResponse, aOptions){
								    refundLoadAction.execute();
	                                var result = Ext.decode(aResponse.responseText);
				                    Ext.MessageBox.alert('提示', result.msg);
	                            }
	                        })
	            }
	        }
	    });
	    
	    var seeAction = new Ext.Action({
	        text: '查看还款详情',
	        iconCls: 'check',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	        if(loanGrid.getSelectionModel().hasSelection()){
		            var record = loanGrid.getSelectionModel().getSelected(); 
				   		 form2Window.show();
				} 
	        }
	    });
	    
	    	  //----------返回到首页
		  var returnToIndex = new Ext.Action({
		        text: '返回首页',
		        hidden :true,
		        iconCls: 'returns', 
		        handler: function(){
		           document.location.href ='<%=path%>/to_welcome.do';
		        }
		    });
			
			var oneTbar=new Ext.Toolbar({
		  		items:[
				 '客户名字：',
				 {
		     	 	xtype:'textfield',
		     	 	width:100,
		     	 	id:'_name' 
		     	 },
				 '客户电话：',
				 {
		     	 	xtype:'textfield',
		     	 	width:100,
		     	 	id:'_tel' 
		     	 },
					 searchButton,
					 returnToIndex
			 ] });
			 
	        var myGridLoadAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: true,
	        handler: function(aMasterId){
	        fid = '${param.fid}';
	        if(fid != ''){
	        	returnToIndex.show();
	        }
	         var record = myGrid.getSelectionModel().getSelected();
	        	if(record != null){
	        		cid = record.get('clientId');
	        	}
	            masterStore.setBaseParam('masterId', aMasterId);
	            masterStore.load({
	                params:{
	                    start:0, 
	                    limit:20,
	                    fid:fid
	                },
	                waitTitle:'提示',waitMsg: '数据加载请稍后...',
	                failure: function() {
	                    Ext.Msg.alert('提示', '读取数据失败！');                        

     
	                }
	            });
	        }
	    }); 
	      
	        var loanLoadAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: true,
	        handler: function(aMasterId){
	            loanStore.setBaseParam('masterId', aMasterId);
	            loanStore.load({
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
	             	 var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	                cid = record.get('clientId');
	                 loanStore.load({
							params:{
								cid:cid,
								start:0, 
	                    		limit:20
						}
					});
	               }
	            }
	    };  
	    
       function loanUpdateAction (){
	           	if(loanGrid.getSelectionModel().hasSelection()){
	             	 var record = loanGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	                lid = record.id;
	                 refundStore.load({
							params:{
								lid:lid
						}
					});
					seeAction.enable();
	               }
	            }
	    }; 
	    
	       var refundLoadAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: true,
	        handler: function(aMasterId){
	            refundStore.setBaseParam('masterId', aMasterId);
	            refundStore.load({
	                params:{
	                    start:0, 
	                    limit:10,
	                    lid:lid
	                },
	                waitTitle:'提示',waitMsg: '数据加载请稍后...',
	                failure: function() {
	                    Ext.Msg.alert('提示', '读取数据失败！');                        

     
	                }
	            });
	        }
	    });   
	    function refundUpdateAction(){
	    if(refundGrid.getSelectionModel().hasSelection()){
	    	var record = refundGrid.getSelectionModel().getSelected();
	    	if(record != null){
	    		myGridtijiaoAction.enable();
	    	}
	    }
	    }; 
	    
		    // --------------- grid --------------------
	    var myGrid = new Ext.grid.GridPanel({
	        id: 'myGrid',
	        store: masterStore,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        width:320,
	        autoScroll: true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	                header: '客户名称',
	                sortable: true,
	                dataIndex: 'clientName'
	            },
	            {
	                header: '客户联系方式',
	                sortable: true,
	                dataIndex: 'contactTel'
	            },
	            {
	                header: '贷款金额',
	                sortable: true,
	                dataIndex: 'loanAmount'
	            },
	            {
	                header: '客户地址',
	                sortable: true,
	                dataIndex: 'clientAdd'
	            },
	            {
	                header: '商机类型',
	                sortable: true,
	                dataIndex: 'oppType'
	            },
	            {
	                header: '备用电话1',
	                sortable: true,
	                dataIndex: 'spareTel1'
	            },
	            {
	                header: '备用电话2',
	                sortable: true,
	                dataIndex: 'spareTel2'
	            },
	            {
	                header: '客户状态',
	                sortable: true,
	                dataIndex: 'clientStatus'
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'remark'
	            }
	        ],
	        tbar: [
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
	    
	    //loanGrid
	    var loanGrid = new Ext.grid.GridPanel({
	        id: 'loanGrid',
	        store: loanStore,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        autoScroll: true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	                header: '类型',
	                sortable: true,
	                dataIndex: 'type'
	            },
	            {
	                header: '金额',
	                sortable: true,
	                dataIndex: 'sum'
	            },
	            {
	                header: '还款期数',
	                sortable: true,
	                dataIndex: 'rcount'
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'remark'
	            }
	        ],
	        tbar:[
	        	seeAction
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store: loanStore,
	            displayInfo: true,
	             plugins: new Ext.ux.ProgressBarPager(),
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	    
	    var refundGrid = new Ext.grid.GridPanel({
	        id: 'refundGrid',
	        store: refundStore,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	                header: '期数',
	                sortable: true,
	                dataIndex: 'number'
	            },
	            {
	                header: '还款时间',
	                sortable: true,
	                dataIndex: 'rTime'
	            },
	            {
	                header: '实际还款时间',
	                sortable: true,
	                dataIndex: 'factRTime'
	            },
	            {
	                header: '还款金额',
	                sortable: true,
	                dataIndex: 'rSum'
	            },
	            {
	                header: '状态',
	                sortable: true,
	                dataIndex: 'status'
	            }
	        ],
	        tbar:[
	        	myGridtijiaoAction
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 10,
	            store: refundStore,
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
	    
	    loanGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        loanUpdateAction
	    );
	    
	    refundGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        refundUpdateAction
	    );
	    
	     var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls:'saves',
	        handler: function(){
	            if(form1.getForm().isValid()){
	                form1.getForm().submit({
	                    url: '<%=path%>/loan/saveOrUpdateLoanD.do',
	                    params: {
	                    	cid:cid
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
	    });
	    
	     var form1ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls:'returns',
	        handler: function(){
	           form1Window.hide();
	        }
	    });
	    
	     var form2ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls:'returns',
	        handler: function(){
	           form2Window.hide();
	        }
	    });
	    
	     var form3ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls:'returns',
	        handler: function(){
	           form3Window.hide();
	        }
	    });
	    
	    // ------------------ form -------------------------    
	    var form1 = new Ext.form.FormPanel({
	        id: 'form1',
	        bodyStyle: 'padding:5px',
	        title:'贷款明细',
	        frame: true,
	        items: [{
	            layout: 'column',
	            items: [
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
	                        {
								inputType: 'hidden',
	                            fieldLabel: '*编号',
	                            id: 'id'
	                        },
	                        {
								fieldLabel: '*类型',
	                            allowBlank: false,
	                            id: 'type'
	                        },
	                        {
								fieldLabel: '*金额',
	                            allowBlank: false,
	                            id: 'sum'
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
		                    	fieldLabel: '*还款期数',
		                    	allowBlank: false,
		                    	id: 'rcount'
		                    },
	                        {
	                            fieldLabel: '*备注',
	                            allowBlank: true,
	                            id: 'remark'
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
	            layout: 'column',
	            items: [
	                {
	                    columnWidth: .8,
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
								xtype: 'datefield',
								format:'Y-m-d',
	                            fieldLabel: '*还款时间',
	                            id: 'factRTime'
	                        }
	                    ]
	                }
	            ]
	        }]
	    });
	    
	     ///点击查看重要通知时的双击事件
	    	 loanGrid.addListener('rowdblclick', rowdblclickFn); 
				function rowdblclickFn(loanGrid,rowindex,e){ 
				if(loanGrid.getSelectionModel().hasSelection()){
		            var record = loanGrid.getSelectionModel().getSelected(); 
				   		 form2Window.show();
					} 
				}
				
	      var form1Window = new Ext.Window({
	        width: 700,
	        height: 190,
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
	        width: '60%',
	        title:'还款明细',
	        height: 420,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: refundGrid,
	        closeAction: 'hide',
	        buttons: [
	                form2ReturnAction
	            ]
	    });
	    
	      var form3Window = new Ext.Window({
	        width: '40%',
	        height: 120,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: form2,
	        closeAction: 'hide',
	        buttons: [
	                sureAction,
	                form3ReturnAction
	            ]
	    });
	    
	    // --------------- viewport --------------------
	     var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[
	        {
		            xtype:'panel',
		            region:'center',
		            layout:'column',
		            autoScroll:true,
		            items:[{
		                columnWidth:.55,
		                autoScroll:true,
		                style:'padding:10px 0 10px 10px',
		                items:[{
		                    title: '签单成功信息',
		                    layout:'fit',
		                    height:420,
		                    items:myGrid
		                }]
		            },{
		                columnWidth:.45,
		                autoScroll:true,
		                style:'padding:10px 10px 10px 10px',
		                items:[{
		                    title: '贷款明细',
		                    layout:'fit',
		                    height:420,
		                    items:loanGrid
		                }]
		            }
		            ] 
	        }
	        ]
	    });
	
	    // --------------- load data -------------------
	    myGridLoadAction.execute();
		});
		</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
