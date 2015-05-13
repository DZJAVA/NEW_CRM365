<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript" src="resources/writeJs/provinceCity.js"></script>
<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
    		
			var cid = '';	//客户资源	
			var lid = '';	//贷款明细ID
			var coun = '';	//还款期数
			var thissum = '';	//选中该条记录时的金额总数
			var _fid = '${param._fid}';	//得到首页传过来的id
	
	    // --------------- grid store	签单成功Store -------------------
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/loan/loadSuccessClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:'',
                _fid:_fid
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'clientAdd' },
	            { name: 'clientName' },
	            { name: 'clientStatus' },
	            { name: 'contactTel' },
	            { name: 'assignDate' },
	            { name: 'emp_Name' },
	            { name: 'loanAmount' },
	            { name: 'oppType' },
	            { name: 'signPossible' },
	            { name: 'spareTel1' },
	            { name: 'spareTel2' },
	            { name: 'assignTime' },
	            { name: 'companyName' },
	            { name: 'loanAmount' },
	            { name: 'remark' },
	            { name: 'khxinx'},
	             { name: 'proCity2'},
	             { name: 'city2'},
	             { name: 'province2'}
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
	        		cid:cid
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'type' },
	            { name: 'sum' },
	            { name: 'rcount' },
	            { name: 'refundTotal' },
	            { name: 'monthMoney' },
	            { name: 'rremark' },
	            { name: 'hkuanTime'}
	        ]
	    });
	       //-------------监听事件
	     loanStore.on('beforeload',function(store,records,options){
		     	store.baseParams = {
					cid:cid
				};
	     });   
	    
	      //还款期数明细store
	    var refundStore = new Ext.data.JsonStore({
	        url: '<%=path%>/loan/findByLoanD.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	lid:lid
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
	    
	     refundStore.on('beforeload',function(store,records,options){
	     	store.baseParams = {
				lid:lid
			};
	     });
	    
	    var myGridDeleteAction = new Ext.Action({
	        text: '删除贷款明细',
	        iconCls: 'calculator_delete',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            if (loanGrid.getSelectionModel().hasSelection()) {
	                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
	                    if (aButton == 'yes'){
	                        var id = ''
	                       	var record = loanGrid.getSelectionModel().getSelected();
	                       	if(record != null){
	                          id = record.id;
	                          }
	                        Ext.Ajax.request({
	                            url: '<%=path%>/loan/deleteLoanDetail.do',
	                            params: {
	                                id: id
	                            },
	                            success: function(aResponse, aOptions){
	                            	myGridLoadAction.execute();
	                            	loanLoadAction.execute();
	                                var result = Ext.decode(aResponse.responseText);
	                                loanStore.reload({callback: loanGridUpdateAction});
				                    Ext.MessageBox.alert('提示', result.msg);
	                            },
	                            failure: function(aResponse, aOptions){
	                            	myGridLoadAction.execute();
	                            	loanLoadAction.execute();
	                                var result = Ext.decode(aResponse.responseText);
				                    Ext.MessageBox.alert('提示', result.msg);
	                            }
	                        })
	                    }
	                });
	            }
	        }
	    });
	    
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
	                        var id = '';
	                        id = record.id;
	                        var _factRTime = Ext.getCmp('factRTime').getValue();
	                        if(_factRTime != ''){
	                        	_factRTime = Ext.getCmp('factRTime').getValue().format('Y-m-d');
	                        }
	                        Ext.Ajax.request({
	                            url: '<%=path%>/loan/updateRefund.do',
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
	    
        var myGridLoadAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: true,
	        handler: function(aMasterId){
		        if(_fid != ''){
		        	returnToIndex.show();
		        }
	         	var record = myGrid.getSelectionModel().getSelected();
	        	if(record != null){
	        		cid = record.get('id');
	        	}
	            masterStore.load({
	                params:{
	                    start:0, 
	                    limit:20
	                }
	            });
	        }
	    });
	    
    	var sendSure = new Ext.Action({
	        text: '确定',
	        iconCls: 'check',
	        scale: 'small',
	        handler: function(){
	            if (myGrid.getSelectionModel().hasSelection()) {
	            	 var record = myGrid.getSelectionModel().getSelected();
	                        var id = ''
	                        id = record.get('id');
	                        var _sendNumber = record.get('contactTel');
	                        var _sendContent = Ext.getCmp('sendContent').getValue();
	                        var _clientName = record.get('clientName');
	                        Ext.Ajax.request({
	                            url: '<%=path%>/loan/sendSure.do',
	                            params: {
	                                id: id,
	                                _sendContent:_sendContent,
	                                _sendNumber:_sendNumber,
	                                _clientName:_clientName
	                            },
	                            success: function(aResponse, aOptions){
	                            	form6Window.hide();
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
	    
	     	  //----------返回到首页
		  	var returnToIndex = new Ext.Action({
		        text: '返回首页',
		        hidden :true,
		        iconCls: 'returns', 
		        handler: function(){
		           document.location.href ='<%=path%>/to_main.do';
		        }
		    });
		    
	     	var searchButton = {
				xtype:'button',
				iconCls: 'check',
				text:'查 询',
				handler:function(){
					var _name=Ext.getCmp('_name').getValue();
					var _tel=Ext.getCmp('_tel').getValue();
					var _lstartTime = Ext.getCmp('_lstartTime').getValue();
					if(_lstartTime != ''){
						_lstartTime = Ext.getCmp('_lstartTime').getValue().format('Y-m-d');
					}
					var _lendTime = Ext.getCmp('_lendTime').getValue();
					if(_lendTime != ''){
						_lendTime = Ext.getCmp('_lendTime').getValue().format('Y-m-d');
					}
					var _oType = Ext.getCmp('oppTypeCombox1').getValue();
					var _res = Ext.getCmp('_res').getValue();
						Ext.apply(masterStore.baseParams,{
							conditions:'{_name:"'+_name+'",_tel:"'+_tel+'",_lstartTime:"'+_lstartTime+'",_lendTime:"'+_lendTime+'",_oType:"'+_oType+'",_res:"'+_res+'"}'
					});
						masterStore.load({
				            params:{
				                start:0, 
				                limit:20
				            }
				        });
				}
			}
			
			  //---------------商机类型下拉列表--------------------------
	    var oppTypeData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '1', '房贷'],
			    [ '2', '信贷'],
			    [ '3', '短借'],
			    [ '4', '企贷']
		      ]
		});
		var oppTypeCombox = new Ext.form.ComboBox({
		      fieldLabel : '*商机类型',
		      id : 'oppTypeCombox',
		      store : oppTypeData,
		      editable:false,
		      allowBlank: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...', // 默认值   selectOnFocus : true,
		      hiddenName : 'oppType'  
	    });
		    
		      //---------------签单可能性下拉列表--------------------------
	    var signPossibleData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '1', '100%'],
			    [ '2', '80%'],
			    [ '3', '50%'],
			    [ '4', '10%'],
			    [ '5', '0%']
		      ]
		});
		var signPossibleCombox = new Ext.form.ComboBox({
		      fieldLabel : '*签单可能性',
		      id : 'signPossibleCombox',
		      store : signPossibleData,
		      editable:false,
		      allowBlank: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...', // 默认值   selectOnFocus : true,
		      hiddenName : 'signPossible'  
	    });
	    
		  //---------------商机类型下拉列表--------------------------
	    var oppTypeData1 = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '1', '房贷'],
			    [ '2', '信贷'],
			    [ '3', '短借'],
			    [ '4', '企贷']
		      ]
		});
		var oppTypeCombox1 = new Ext.form.ComboBox({
		      fieldLabel : '*商机类型',
		      id : 'oppTypeCombox1',
		      store : oppTypeData1,
		      width:80,
		      editable:true,
		      allowBlank: true,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...' // 默认值   selectOnFocus : true,
	    });
			
	 		var oneTbar=new Ext.Toolbar({
		  		items:[
				 '客户电话：',
				 {
		     	 	xtype:'textfield',
		     	 	width:100,
		     	 	id:'_tel' 
		     	 },
				 '管理人：',
				 {
		     	 	xtype:'textfield',
		     	 	width:100,
		     	 	id:'_res' 
		     	 },
					 searchButton
			 ] });
			 
       function myGridUpdateAction (){
	           	if(myGrid.getSelectionModel().hasSelection()){
	             	 var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	            	myGridNewAction.enable();
	            	cid = '';
	                cid = record.get('id');
	                thissum = record.get('loanAmount');
	                loanLoadAction.execute();
	               }else{
	               	myGridNewAction.disable();
	               }
	            }
	    };  
	    
	    function loanGridUpdateAction(){
	    	if(loanGrid.getSelectionModel().hasSelection()){
	    		var record = loanGrid.getSelectionModel().getSelected();
	    		if(record != null){
	    			lid = '';
	    			lid = record.id;
		         	coun = record.get('rcount');
	    			myGridEditAction.enable();
	                myGridDeleteAction.enable();
	                seeAction.enable();
	    		}else{
	    			myGridEditAction.disable();
	                myGridDeleteAction.disable();
	                seeAction.disable();
	    		}
	    	}
	    }
	    
	      var seeAction = new Ext.Action({
	        text: '查看还款详情',
	        iconCls: 'check',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	        if(loanGrid.getSelectionModel().hasSelection()){
		            var record = loanGrid.getSelectionModel().getSelected();
		            	 refundLoadAction.execute(); 
				   		 form2Window.show();
				} 
	        }
	    });
	    
	      var refundLoadAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: true,
	        handler: function(){
	            refundStore.load({
	                params:{
	                    start:0, 
	                    limit:15
	                }
	            });
	        }
	    }); 
	    
	    var myGridNewAction = new Ext.Action({
	        text: '新增还贷款明细',
	        iconCls: 'calculator_add',
	        scale: 'small',
	        disabled:true,
	        handler: function(){
       			 	form1.getForm().reset();
			  		form1Window.show();
	        }
	    });
	    
	     var myGridEditAction = new Ext.Action({
	        text: '编辑贷款明细',
	        iconCls: 'calculator_edit',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = loanGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	            var loid = record.id;
	            if(record != null){
	              Ext.Ajax.request({
	                            url: '<%=path%>/loan/findByTime.do',
	                            params: {
	                                loid : loid
	                            },
	                            success: function(aResponse, aOptions){
       								  	form1Window.show();
      									form1.getForm().loadRecord(record);
	                            },
	                            failure: function(){
				                    Ext.MessageBox.alert('提示', '已有还款记录，不能进行修改！');
	                            }
	                        })
       				 }
       			 }
	        }
	    });
	    
	    var form4SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls:'saves',
	        handler: function(){
	        	if(myGrid.getSelectionModel().hasSelection()){
             	 var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
	            	var clid = record.get('id');
	            }
	            }
	        	var _opp = Ext.getCmp('oppTypeCombox').getValue(); 
	        	var _sign = Ext.getCmp('signPossibleCombox').getValue();
	        	var _spare1 = Ext.getCmp('spareTel1').getValue();
	        	var _spare2 = Ext.getCmp('spareTel2').getValue();
	        	var _contact = Ext.getCmp('contactTel').getValue();
	        	Ext.getCmp('contactTel').setDisabled(false);
            	Ext.getCmp('spareTel1').setDisabled(false);
            	Ext.getCmp('spareTel2').setDisabled(false);
            	var pro = provinceCombox2.getValue();
	        	var city = cityCombox2.getValue();
	            if(clientForm.getForm().isValid()){
	            	if(_spare1 == '' && _spare2 == '' && _contact == ''){
	            		Ext.MessageBox.alert('提示', '必须填写一个联系电话！');
	            	}else if((_spare1 == _spare2 && _spare1 != '' && _spare2 != '') | (_spare1 == _contact && _spare1 != '' && _contact != '') | (_spare2 == _contact && _spare2 != '' && _contact != '')){
	            		Ext.MessageBox.alert('提示', '联系电话不能相同！');
	            	}else{
	            		clientForm.getForm().submit({
		                    url: '<%=path%>/loan/updateClient.do',
		                    params: {
		                        _opp: _opp,
		                        _sign: _sign,
		                        clid:clid,
		                        pro:pro,
		                        city:city
		                    },
		                    success: function(aForm, aAction){
		                    cityData.clearFilter();
		                    	form4Window.hide();
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
	        }
	    });
	    
	    var form4ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls:'returns',
	        handler: function(){
	           form4Window.hide();
	        }
	    });
	    
	    var _id = '';
        var loanLoadAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: true,
	        handler: function(aMasterId){
	        	flag = 0;
	        	flag1 = 0;
	        	flag2 = 0;
	        	flag3 = 0;
	        	flag4 = 0;
	        	flag5 = 0;
		        var record = loanGrid.getSelectionModel().getSelected();
		        if(record != null){
		         	coun = record.rcount;
		        }
		        Ext.Ajax.request({
                    url: '<%=path%>/loan/findNoRepay.do',
                    params: {
                       id : cid
                    },
                    success: function(aResponse, aOptions){
                    	var result = Ext.decode(aResponse.responseText);
                    	_id = result.ids;
			            loanStore.load({
			                params:{
				                start:0, 
			               		limit:20
			                }
			            });
                    }
                });
	        }
	    });
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
	                header: '客户信息',
	                sortable: true,
	                width:150,
	                dataIndex: 'khxinx'
	            },
	            {
	            	header: '省市',
	            	sortable: true,
	            	dataIndex: 'proCity2',
	            	renderer: function(value, metadata, record, rowIndex, columnIndex, store){
	            		var vals = value.split(',');
	            		var pro = '';
	            		var city = '';
	            		if(vals[0] != '' && vals[0] != null && vals[0] != 'null'){
	            			var len = provinceData.getTotalCount();
	            			for(var i = 0; i < len; i++){
	            				if(vals[0] == provinceData.getAt(i).get('key')){
	            					pro = provinceData.getAt(i).get('value');
	            					break;
	            				}
	            			}
	            		}
	            		if(vals[1] != '' && vals[1] != null && vals[1] != 'null'){
	            			cityData.each(function(rec){
	            				if(vals[1] == rec.get('key')){
	            					city = rec.get('value');
	            				}
	            			});
	            		}
	            		if(pro == ''){
	            			return city;
	            		}else if(city == ''){
	            			return pro;
	            		}else{
	            			return pro + ',' + city;
	            		}
		            }
	            },
	            {
	                header: '客户名称',
	                hidden:true,
	                sortable: true,
	                dataIndex: 'clientName'
	            },
	            {
	                header: '客户联系方式',
	                sortable: true,
	                width:90,
	                dataIndex: 'contactTel'
	            },
	            {
	                header: '贷款金额(万)',
	                sortable: true,
	                hidden:true,
	                dataIndex: 'loanAmount'
	            },
	            {
	                header: '商机类型',
	                 hidden:true,
	                sortable: true,
	                dataIndex: 'oppType'
	            },
	            {
	                header: '备用电话1',
	                width:90,
	                sortable: true,
	                dataIndex: 'spareTel1'
	            },
	            {
	                header: '备用电话2',
	                width:90,
	                sortable: true,
	                dataIndex: 'spareTel2'
	            },
	             {
	                header: '客户地址',
	                width:90,
	                sortable: true,
	                dataIndex: 'clientAdd'
	            },
	            {
	                header: '签单可能性',
	                width:70,
	                sortable: true,
	                dataIndex: 'signPossible'
	            },
	            {
	                header: '客户状态',
	                width:70,
	                sortable: true,
	                dataIndex: 'clientStatus'
	            },
	            {
	                header: '分配时间',
	                sortable: true,
	                dataIndex: 'assignTime'
	            },
	            {
	                header: '管理人',
	                sortable: true,
	                dataIndex: 'emp_Name'
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'remark'
	            }
	        ],
	        tbar: [
	        returnToIndex,
	        '贷款时间：',
		  		{
		  			xtype:'datefield',
		  			width:95,
		  			format:'Y-m-d',
		  			id:'_lstartTime'
		  		},
		  		'至',
		  		{
		  			xtype:'datefield',
		  			format:'Y-m-d',
		  			width:95,
		  			id:'_lendTime'
		  		},
		  		'贷款类型：',
		  		oppTypeCombox1,
				 '客户名字：',
				 {
		     	 	xtype:'textfield',
		     	 	width:70,
		     	 	id:'_name' 
	     	 	}
	        ],
	         listeners : {
			 	'render' : function(){
				 	oneTbar.render(this.tbar); //add one tbar
			 },
			  'rowdblclick' : function(){
			     var record = myGrid.getSelectionModel().getSelected(); 
			     if(record != null){
			    	form4Window.show();
			    	clientForm.getForm().loadRecord(record);
			    	if(record.get('contactTel') != ''){
	            		Ext.getCmp('contactTel').setDisabled(true);
	            	}else{
	            		Ext.getCmp('contactTel').setDisabled(false);
	            	}
	            	if(record.get('spareTel2') != ''){
	            		Ext.getCmp('spareTel2').setDisabled(true);
	            	}else{
	            		Ext.getCmp('spareTel2').setDisabled(false);
	            	}
	            	if(record.get('spareTel1') != ''){
	            		Ext.getCmp('spareTel1').setDisabled(true);
	            	}else{
	            		Ext.getCmp('spareTel1').setDisabled(false);
	            	}
			     }
			 },
			 'bodyresize':function() { 
				Ext.getCmp('myGrid').setHeight(document.body.offsetHeight); 
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
	    var flag = 0;//标识loanGrid显示数据的位置
	    var flag1 = 0
	    var flag2 = 0
	    var flag3 = 0
	    var flag4 = 0
	    var flag5 = 0
	    //loanGrid
	    var loanGrid = new Ext.grid.GridPanel({
	        id: 'loanGrid',
	        store: loanStore,
	        autoScroll:true,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	                header: '类型',
	                width:60,
	                sortable: true,
	                dataIndex: 'type',
	                renderer: function(value){
	                	if(_ids != null | _ids != ''){
	                		var _ids = _id.split(',');
	                		if(flag1 > loanStore.data.length | flag1 == loanStore.data.length){
	            				flag1 = 0;
	            			}
	                		var record = loanStore.getAt(flag1);
	                		var id = record.id;
	                		var size = Ext.getCmp('loanPage').pageSize - 1;
	            			if(flag1 == size){
	            				flag1 = 0;
	            			}else{
	            				flag1++;
	            			}
	                		for(var i = 0; i < _ids.length; i++){
	                			if(id == _ids[i]){
	                				return '<font style="color:red;">'+value+'</font>'
	                			}
	                		}
	                	}
						return value;	                	
	                }
	            },
	            {
	                header: '总贷款金额(万)',
	                width:100,
	                sortable: true,
	                dataIndex: 'sum',
	                renderer: function(value){
	                	if(_ids != null | _ids != ''){
	                		var _ids = _id.split(',');
	                		if(flag > loanStore.data.length | flag == loanStore.data.length){
	            				flag = 0;
	            			}
	                		var record = loanStore.getAt(flag);
	                		var id = record.id;
	                		var size = Ext.getCmp('loanPage').pageSize - 1;
	            			if(flag == size){
	            				flag = 0;
	            			}else{
	            				flag++;
	            			}
	                		for(var i = 0; i < _ids.length; i++){
	                			if(id == _ids[i]){
	                				return '<font style="color:red;">'+value+'</font>'
	                			}
	                		}
	                	}
						return value;	                	
	                }
	            },
	            {
	                header: '还款期数',
	                width:70,
	                sortable: true,
	                dataIndex: 'rcount',
	                renderer: function(value){
	                	if(_ids != null | _ids != ''){
	                		var _ids = _id.split(',');
	                		if(flag2 > loanStore.data.length | flag2 == loanStore.data.length){
	            				flag2 = 0;
	            			}
	                		var record = loanStore.getAt(flag2);
	                		var id = record.id;
	                		var size = Ext.getCmp('loanPage').pageSize - 1;
	            			if(flag2 == size){
	            				flag2 = 0;
	            			}else{
	            				flag2++;
	            			}
	                		for(var i = 0; i < _ids.length; i++){
	                			if(id == _ids[i]){
	                				return '<font style="color:red;">'+value+'</font>'
	                			}
	                		}
	                	}
						return value;	                	
	                }
	            },
	            {
	                header: '总还款金额(万)',
	                width:100,
	                sortable: true,
	                dataIndex: 'refundTotal',
	                renderer: function(value){
	                	if(_ids != null | _ids != ''){
	                		var _ids = _id.split(',');
	                		if(flag3 > loanStore.data.length | flag3 == loanStore.data.length){
	            				flag3 = 0;
	            			}
	                		var record = loanStore.getAt(flag3);
	                		var id = record.id;
	                		var size = Ext.getCmp('loanPage').pageSize - 1;
	            			if(flag3 == size){
	            				flag3 = 0;
	            			}else{
	            				flag3++;
	            			}
	                		for(var i = 0; i < _ids.length; i++){
	                			if(id == _ids[i]){
	                				return '<font style="color:red;">'+value+'</font>'
	                			}
	                		}
	                	}
						return value;	                	
	                }
	            },
	            {
	                header: '每月还款金额(万)',
	                width:100,
	                sortable: true,
	                dataIndex: 'monthMoney',
	                renderer: function(value){
	                	if(_ids != null | _ids != ''){
	                		var _ids = _id.split(',');
	                		if(flag4 > loanStore.data.length | flag4 == loanStore.data.length){
	            				flag4 = 0;
	            			}
	                		var record = loanStore.getAt(flag4);
	                		var id = record.id;
	                		var size = Ext.getCmp('loanPage').pageSize - 1;
	            			if(flag4 == size){
	            				flag4 = 0;
	            			}else{
	            				flag4++;
	            			}
	                		for(var i = 0; i < _ids.length; i++){
	                			if(id == _ids[i]){
	                				return '<font style="color:red;">'+value+'</font>'
	                			}
	                		}
	                	}
						return value;	                	
	                }
	            },
	            {
	                header: '备注',
	                sortable: true,
	                dataIndex: 'rremark',
	                renderer: function(value){
	                	if(_ids != null | _ids != ''){
	                		var _ids = _id.split(',');
	                		if(flag5 > loanStore.data.length | flag5 == loanStore.data.length){
	            				flag5 = 0;
	            			}
	                		var record = loanStore.getAt(flag5);
	                		var id = record.id;
	                		var size = Ext.getCmp('loanPage').pageSize - 1;
	            			if(flag5 == size){
	            				flag5 = 0;
	            			}else{
	            				flag5++;
	            			}
	                		for(var i = 0; i < _ids.length; i++){
	                			if(id == _ids[i]){
	                				return '<font style="color:red;">'+value+'</font>'
	                			}
	                		}
	                	}
						return value;	                	
	                }
	            }
	        ],
	        tbar :[
	       		myGridNewAction,
	        	myGridEditAction,
	        	myGridDeleteAction,
	        	seeAction
	        ],
	        listeners :{
	        	'bodyresize':function() { 
					Ext.getCmp('loanGrid').setHeight(document.body.offsetHeight); 
			 	}
			},
	        bbar: new Ext.PagingToolbar({
	        	id: 'loanPage',
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
	         viewConfig : {
				getRowClass:function(record,index,p,ds) {
					var cls = 'x-grid-record-red';
					switch (record.data['status']) {
						case '未还款' : cls = 'x-grid-record-red'; break;
						case '已还款' : cls = 'x-grid-record-black'; break;
					}
					return cls;
				}
			},
	        columns: [
	            {
	                header: '期数',
	                sortable: true,
	                dataIndex: 'number'
	            },
	            {
	                header: '还款时间',
	                width:200,
	                sortable: true,
	                dataIndex: 'rTime'
	                },
	            {
	                header: '还款金额(万)',
	                sortable: true,
	                 width:200,
	                dataIndex: 'rSum'
	            }
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 15,
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
	        loanGridUpdateAction
	    );
	    
	     var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls:'saves',
	        handler: function(){
	            if(form1.getForm().isValid()){
	            var _payTime = Ext.get('hkuanTime').getValue();//---还款时间
	            var _lTotal = parseFloat(Ext.get('sum').getValue());
	            var _rTotal = parseFloat(Ext.get('refundTotal').getValue());
	            if(_lTotal > _rTotal){
	            	Ext.MessageBox.alert('提示', '还款总金额小于贷款总金额！');
	            	return false;
	            }
	                form1.getForm().submit({
	                   url: '<%=path%>/loan/saveOrUpdateLoanD.do',
	                    params: {
	                    	cid:cid,
	                    	thissum : thissum,
	                    	_payTime : _payTime
	                    },
	                  success: function(aForm, aAction){
	                        form1Window.hide();
	                        loanStore.reload({callback: loanGridUpdateAction});
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
	      var form6ReturnAction = new Ext.Action({
	        text: '返回',
	        iconCls:'returns',
	        handler: function(){
	           form6Window.hide();
	        }
	    });
	    
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
								fieldLabel: '*贷款总金额(万)',
	                            allowBlank: false,
	                            id: 'sum'
	                        },
	                         {
		                    	fieldLabel: '*还款期数',
		                    	allowBlank: false,
		                    	id: 'rcount'
		                    },
		                    {
								fieldLabel: '*还款总金额(万)',
	                            allowBlank: false,
	                            id: 'refundTotal'
	                        }
	                    ]
	                },
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 120,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 180,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
								fieldLabel: '*每月还款金额(万)',
	                            allowBlank: false,
	                            id: 'monthMoney'
	                        },
	                        {
	                        	xtype: 'datefield',
	                        	format: 'Y-m-d',
	                            fieldLabel: '*还款日期',
	                            allowBlank: false,
	                            editable: false,
	                            id: 'hkuanTime',
	                            name: 'hkuanTime'
	                        },
	                        {
	                            fieldLabel: '备注',
	                            xtype:'textarea',
	                            width:180,
	                            height:55,
	                            id: 'rremark'
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
	    
	       var sendForm = new Ext.form.FormPanel({
	        id: 'sendForm',
	        bodyStyle: 'padding:5px',
	        frame: true,
	        items: [{
	            layout: 'column',
	            items: [
	                {
	                    columnWidth: .8,
	                    layout: 'form',
	                    labelWidth: 60,
	                    defaults: {
	                        width: 220,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
	                            fieldLabel: '*内容',
	                            xtype:'textarea',
	                            id: 'sendContent'
	                        }
	                    ]
	                }
	            ]
	        }]
	    });
	    
	
	         //--------------省-------市
	    
	   var cityCombox2 = new Ext.form.ComboBox({
	      id : 'cityCombox2',
	      store : cityData,
	      fieldLabel: '市区',
	      allowBlank: false,
	      editable:false,
	      displayField : 'value',
	      valueField : 'key',
	      name: 'city2',
	      id: 'city2',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '请选择市区...',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'expand': function(){
	      		var val = provinceCombox2.getValue();
	      		if(val != '' && val != null){
	      			cityData.filterBy(function(rec, id){
	      				return rec.get('pro') == val;
		      		});
	      		}
	      	}
	      }
    });
    
  
	
	var provinceCombox2 = new Ext.form.ComboBox({
	      id : 'provinceCombox2',
	      store : provinceData,
	      fieldLabel: '省份',
	      displayField : 'value',
	       editable:false,
	      allowBlank: false,
	      valueField : 'key',
	      name: 'province2',
	      id: 'province2',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '请选择省份...',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'select': function(){
	      		cityCombox2.reset();
	      	}
	      }
    });
	    
	    
	    	    // ------------------ clientForm -------------------------    
	    var clientForm = new Ext.form.FormPanel({
	        id: 'clientForm',
	        bodyStyle: 'padding:5px',
	        labelAlign: 'right',
	        labelWidth: 80,
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
	                        anchor: '95%'
	                    },
	                    items: [
	                        {
								inputType: 'hidden',
	                            fieldLabel: '*编号',
	                            id: 'id'
	                        },
	                        {
	                        	xtype: 'datefield',
	                        	format: 'Y-m-d',
	                            fieldLabel: '*录入日期',
	                            allowBlank: false,
	                            editable: false,
	                            id: 'assignDate'
	                        },
	                        oppTypeCombox,
	                        provinceCombox2,
	                        cityCombox2,
	                        {
	                            fieldLabel: '客户联系方式',
	                            allowBlank: false,
	                            id: 'contactTel',
	                            regex:/^[0-9]*[1-9][0-9]*$/,
	                            enableKeyEvents: true,
	                            listeners: {
	                            	'keyup': function(){
	                            		var _con = Ext.getCmp('contactTel');
	                            		var _value = _con.getValue();
	                            		if(_value != ''){
	                            			_value = _value.substring(0,1);
	                            			if(_value == 1){
	                            				_con.regex = /^\d{11}$/;
	                            			}else{
	                            				_con.regex = /^[0-9]*[1-9][0-9]*$/;
	                            			}
	                            		}
	                            		
	                            	}
	                            }
	                        },
	                        
	                        signPossibleCombox
	                    ]
	                },
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 80,
	                    defaultType: 'textfield',
	                    defaults: {
	                        anchor: '95%'
	                    },
	                    items: [
	                    	{
	                            fieldLabel: '*客户名称',
	                            allowBlank: false,
	                            id: 'clientName'
	                        },
	                        {
	                            fieldLabel: '*贷款金额(万)',
	                            allowBlank: false,
	                            id: 'loanAmount',
	                            enableKeyEvents: true,
	                            regex:/^\d+$/,
	                            listeners: {
	                            	'keyup': function(){
	                            		var _con = Ext.getCmp('loanAmount');
	                            		var _value = _con.getValue();
	                            		for(var i = 0; i < _value.length; i++){
	                            			if(_value.substring(i,i+1) == '.'){
	                            				_con.regex = /^\d+(\.\d+)?$/;
	                            				break;
	                            			}else{
	                            				_con.regex = /^\d+$/;
	                            			}
	                            		}
	                            	}
	                            }
	                        },
	                        {
	                            fieldLabel: '*客户地址',
	                            allowBlank: true,
	                            id: 'clientAdd'
	                        },
	                        {
	                            fieldLabel: '备用电话1',
	                            allowBlank: true,
	                            id: 'spareTel1'
	                        },
	                        {
	                            fieldLabel: '备用电话2',
	                            allowBlank: true,
	                            id: 'spareTel2',
	                            enableKeyEvents: true,
	                            regex:/^[0-9]*[1-9][0-9]*$/,
	                            listeners: {
	                            	'keyup': function(){
	                            		var _con = Ext.getCmp('spareTel2');
	                            		var _value = _con.getValue();
	                            		if(_value != ''){
	                            			_value = _value.substring(0,1);
	                            			if(_value == 1){
	                            				_con.regex = /^\d{11}$/;
	                            			}else{
	                            				_con.regex = /^[0-9]*[1-9][0-9]*$/;
	                            			}
	                            		}
	                            	}
	                            }
	                        }
	                    ]
	                }
	            ]
	        },
            {
            	xtype: 'textarea',
                fieldLabel: '*备注',
                anchor: '98%',
                allowBlank: true,
                id: 'remark'
            }]
	    });
	    
	     ///点击查看重要通知时的双击事件
	    	 loanGrid.addListener('rowdblclick', rowdblclickFn); 
				function rowdblclickFn(loanGrid,rowindex,e){ 
				if(loanGrid.getSelectionModel().hasSelection()){
		            var record = loanGrid.getSelectionModel().getSelected(); 
	             		 refundLoadAction.execute(); 
				   		 form2Window.show();
					} 
				}
				
	      var form1Window = new Ext.Window({
	        width: 700,
	        title:'贷款明细',
	        height: 210,
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
	    
	      var form4Window = new Ext.Window({
	    	title: '修改客户信息',
	        width: 650,
	        height: 340,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: clientForm,
	        closable: true,
	        closeAction: 'hide',
	        buttons: [
	                form4SaveAction,
	                form4ReturnAction
	            ]
	    });
	    
	      var form6Window = new Ext.Window({
	    	title: '发送短信',
	        width: 400,
	        height: 200,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: sendForm,
	        closable: true,
	        closeAction: 'hide',
	        buttons: [
	        		sendSure,
	                form6ReturnAction
	            ]
	    });
	    
	    // --------------- viewport --------------------
	     var mainPanel = new Ext.Panel({
	        autoScroll:true,
	        items:[
	        {
		            xtype:'panel',
		            region:'center',
		            layout:'column',
		            monitorResize: true,  
		            items:[{
		                columnWidth:.57,
	                 	 autoScroll:true,
		                 monitorResize: true, 
		                 autoHeight:true,
		                items:[{
		                    title: '签单成功信息',
		                    layout:'fit',
		                    items:myGrid
		                }] 
		            },{
		                columnWidth:.43,
		                autoScroll:true,
		                 monitorResize: true, 
		                 autoHeight:true,
		                items:[{
		                    title: '贷款明细',
		                    layout:'fit',
		                    items:loanGrid
		                }]
		            }
		            ] 
	        }
	        ]
	    });
	
	 // --------------- viewport --------------------
	     var viewport = new Ext.Viewport({
	       layout:'fit',
	        items:[
	        	mainPanel
	        ]
	    });
	    // --------------- load data -------------------
	    myGridLoadAction.execute();
		});
		</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
