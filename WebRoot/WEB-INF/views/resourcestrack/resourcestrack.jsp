<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet" href="resources/css/fileuploadfield.css" type="text/css"></link>
<script type="text/javascript" src="resources/js/all.js"></script>
<script type="text/javascript" src="resources/writeJs/provinceCity.js"></script>
<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
		    var cid = '';	//客户资源	
	        var roleCode = '${userSession.role.roleCode}';
	   //---------------资源列表-----
	    var masterStore = new Ext.data.JsonStore({
	        url: '<%=path%>/client/loadClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:'',
	        	_cpid: '',
	            _flag: '',
	            importId: ''
	        },
	        fields: [
                 { name: 'id' },
	             { name: 'clientName' },
	             { name: 'contactTel' },
	             { name: 'loanAmount' },
	             { name: 'clientAdd' },
	             { name: 'oppType' },
	             { name: 'spareTel1' },
	             { name: 'spareTel2' },
	             { name: 'clientStatus' },
	             { name: 'signPossible' },
	             { name: 'assignDate' },
	             { name: 'assignTime' },
	             { name: 'remark' },
	             { name: 'emp_name' },
	             { name: 'userId'},
	             { name: 'khxinx'},
	             { name: 'clientSourse'},
	             { name: 'clientSourseId'},
	             { name: 'workPlanNewTime'},
	             { name: 'eliTime'},
	             { name: 'proCity'},
	             { name: 'city'},
	             { name: 'province'},
	             { name: 'assignId'},
	             { name: 'assignName'}
	        ]
	    });
	     //---------------资源跟踪列表-----
	    var _gridStore = new Ext.data.JsonStore({
	        url: '<%=path%>/ResourcesTrack/findByTrackrecord.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:'',
	        	_cpid:''
	        },
	        fields: [
	            { name: 'rtid' },
	            { name: 'resourcescontent' },
	            { name: 'resourcestime' },
	            { name: 'resourcespeople_name' },
	            { name: 'intoasinglerate' },
	            { name: 'workplan' },
	            { name: 'remark' },
	            { name: 'plantime'},
	            { name: 'calltime' },
	            { name: 'types' }
	        ]
	    });
	    function clientSaveEvents(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
	    			Ext.getCmp('contactTel').setDisabled(false);
		           	Ext.getCmp('spareTel1').setDisabled(false);
		           	Ext.getCmp('spareTel2').setDisabled(false);
		           	Ext.getCmp('clientSelComboBOx').setDisabled(false);
		        	var _opp = Ext.getCmp('oppTypeCombox').getValue(); 
		        	var _sign = Ext.getCmp('signPossibleCombox').getValue();
		        	var _client = Ext.getCmp('clientSelComboBOx').getValue();
		        	var _spare1 = Ext.getCmp('spareTel1').getValue();
		        	var _spare2 = Ext.getCmp('spareTel2').getValue();
		        	var _contact = Ext.getCmp('contactTel').getValue();
		        	var pro = provinceCombox.getValue();
		        	var city = cityCombox.getValue();
		            if(clientForm.getForm().isValid()){
		            	if(_spare1 == '' && _spare2 == '' && _contact == ''){
		            		Ext.MessageBox.alert('提示', '必须填写一个联系电话！');
		            	}else if((_spare1 == _spare2 && _spare1 != '' && _spare2 != '') | (_spare1 == _contact && _spare1 != '' && _contact != '') | (_spare2 == _contact && _spare2 != '' && _contact != '')){
		            		Ext.MessageBox.alert('提示', '联系电话不能相同！');
		            	}else{
		            		clientForm.getForm().submit({
			                    url: '<%=path%>/client/saveOrUpdateClient.do',
			                    params: {
			                        _opp: _opp,_sign: _sign,_client: _client,pro:pro,city:city
			                    },
			                    waitTitle: '请等待',
			                    waitMsg: '正在努力的保存数据...',
			                    timeout: 20,
			                    success: function(aForm, aAction){
			                    	cityData.clearFilter();
			                    	formClientWindow.hide();
			                    	if(roleCode == '201206'){
			                    		masterStore.setBaseParam('importId', '');
				                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
				                    	masterStore.load({ params: {start:0, limit:20} });
			                    	}else{
			                    		Ext.MessageBox.alert('提示', aAction.result.msg); 
				                        masterStore.reload({callback: myGridUpdateAction1});
			                    	}
			                    },
			                    failure: function(aForm, aAction) {
			                    	var result = aAction.result;
			                    	if(roleCode == '201206'){
			                    		if(result.id != '' && result.id != null){
			                    			Ext.Msg.confirm('显示确认', result.msg, function(btn){
			                    				if(btn == 'yes'){
			                    					formClientWindow.hide();
						                    		masterStore.setBaseParam('importId', result.id);
							                    	masterStore.load({ params: {start:0, limit:20} });
			                    				}
			                    			});
			                    		}
			                    	}else{
				                        Ext.MessageBox.alert('提示', result.msg);                           
			                    	}
			                    }
		                	});
		            	}
		            }
	    		});
	    	}
	    }
	     // --------------- grid actions -----------------
	    var myGridNewAction = new Ext.Action({
	        text: '新增客户',
	        iconCls: 'vcard_add',
	        scale: 'small',
	        handler: function(){
	        	judgeJs('add_clientJs', 'resources/client/addClient.js');
	        	var btn = Ext.getCmp('saveClient');
	        	clientSaveEvents(btn);
	            clientForm.getForm().reset();
	            Ext.getCmp('contactTel').setDisabled(false);
            	Ext.getCmp('spareTel1').setDisabled(false);
            	Ext.getCmp('spareTel2').setDisabled(false);
            	Ext.getCmp('clientSelComboBOx').setDisabled(false);
	            Ext.getCmp('assignDate').setValue(new Date().format('Y-m-d H:i'));
	            formClientWindow.show();
	        }
	    });
	       //----------淘汰资源列表
	  	var myGridEliminateAction = new Ext.Action({
	        text: '查看淘汰资源',
	        iconCls: 'vcard',
	        scale: 'small',
	        handler: function(){
	        	judgeJs('view_eliClientJs', 'resources/client/viewEliClient.js');
			    _gridEliminateLoadAction.execute();
			    formWindowEliminate.show();
	        }
	    });
	    function eliEvent(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
	    			var eliRemark = Ext.getCmp('eliRemark').getValue();
		            Ext.Ajax.request({
		                url: '<%=path%>/ResourcesTrack/saveFailureResource.do',
		                params: {
		                	cid: cid,
		                	eli: eliRemark
		                },
	                    success: function(aResponse, aOptions){
	                   		eliWindow.hide();
	                        masterStore.reload({callback: myGridUpdateAction1});
	                        _gridStore.baseParams._cpid = '';
							_gridloanLoadAction.execute();
	                        var result = Ext.decode(aResponse.responseText);
	               			Ext.MessageBox.alert('提示', result.msg);
	                    },
	                    failure: function(aResponse, aOptions){
	                    	var result = Ext.decode(aResponse.responseText);
							Ext.MessageBox.alert('提示', result.msg);     
	                    }
	                });
	    		});
	    	}
	    }
	     //----------淘汰资源按钮-------
  	    var myGridFailureResourceAction = new Ext.Action({
	        text: '淘汰',
	        iconCls: 'btn_del',
	        disabled: true,
	        scale: 'small',
	        handler: function(){
		        if (myGrid.getSelectionModel().hasSelection()) {
	            	var record = myGrid.getSelectionModel().getSelected();
	            	var _s = record.get('clientStatus');
            	 	if(_s =='1'){
	            	 	Ext.MessageBox.alert('提示', "已签单成功资源不能进行淘汰！");
            	 	}else{
			        	judgeJs('eli_clientJs', 'resources/client/eliClient.js');
			        	var btn = Ext.getCmp('eliSure');
			        	eliEvent(btn);
	            	  	Ext.getCmp('eliForm').getForm().reset();
	            		eliWindow.show();
            		}
           	 	}
       	 	}
    	});
	    function exitEvent(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
	    			if (myGrid.getSelectionModel().hasSelection()) {
	            		var record = myGrid.getSelectionModel().getSelected();
	            	    var tuid_id = record.id;
                        var eremark = Ext.getCmp('exitRemark').getValue();
                        Ext.Ajax.request({
                            url: '<%=path%>/loan/exitSure.do',
                            params: {
                                tuid_id: tuid_id,
                                eremark: eremark
                            },
                            success: function(aResponse, aOptions){
                            	exitWindow.hide();
							    masterStore.reload({callback: myGridUpdateAction1});
                                var result = Ext.decode(aResponse.responseText);
			                    Ext.MessageBox.alert('提示', result.msg);
                            },
                            failure: function(aResponse, aOptions){
                                var result = Ext.decode(aResponse.responseText);
			                    Ext.MessageBox.alert('提示', result.msg);
                            }
                        });
	            	}
	    		});
	    	}
	    }
	    var exitAction = new Ext.Action({
	        text: '退单',
	        iconCls: 'drop-no',
	        disabled: true,
	        handler: function(){
	        	if (myGrid.getSelectionModel().hasSelection()) {
           	    	var record = myGrid.getSelectionModel().getSelected();
            	 	if(record != null){
            	 		judgeJs('exit_clientJs', 'resources/client/exitClient.js');
            	 		var btn = Ext.getCmp('exitClient');
            	 		exitEvent(btn);
             			exitWindow.show();
            	 	}
           	 	}
	        }
	    });
        //----------首页双击进入对淘汰资源的直接认领--------------
	    function setAllowBlank(val){
	    	if(val == '1'){
       			Ext.getCmp('workplan').allowBlank = true;
       			Ext.getCmp('plantime').allowBlank = true;
       			Ext.getCmp('comboxStatecdl').allowBlank = true;
       			Ext.getCmp('comboxType1').allowBlank = true;
       		}else{
       			Ext.getCmp('workplan').allowBlank = false;
       			Ext.getCmp('plantime').allowBlank = false;
       			Ext.getCmp('comboxStatecdl').allowBlank = false;
       			Ext.getCmp('comboxType1').allowBlank = false;
       		}
	    }  
	    function saveTrackEvents(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
	    			if(trackForm.getForm().isValid()){
			            var gen = Ext.getCmp('comboxType1').getValue();
			            var _cs = Ext.getCmp('comboxStatecdl').getValue();
		                trackForm.getForm().submit({
		                    url: '<%=path%>/ResourcesTrack/saveOrUpdateResourcesTrack.do',
		                    params: {
			                    _cpid: cid,gen:gen,_cs:_cs
		                    },
		                    waitTitle: '请等待',
		                    waitMsg: '正在努力的保存数据...',
		                    timeout: 20000,
		                    success: function(aForm, aAction){
                            	trackWindow.hide();
							  	_gridStore.reload({callback: myGridUpdateAction2});
							  	masterStore.reload({callback: myGridUpdateAction1});
	                          	var result = Ext.decode(aAction.response.responseText);
	               			  	Ext.MessageBox.alert('提示', result.msg); 
		                    },
		                    failure: function(aForm, aAction) {
					    		var result = Ext.decode(aAction.response.responseText);
							 	Ext.MessageBox.alert('提示', result.msg);                            
						    }
		                });
		            }
	    		});
	    	}
	    }
	      //---------------新增跟踪记录------
	    var _gridStoreNewAction = new Ext.Action({
	        text: '新增跟踪记录',
	        iconCls: 'book_add',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	        	var rec = myGrid.getSelectionModel().getSelected();
	        	var val = '';
	        	judgeJs('save_trackJs', 'resources/client/addTrack.js');
	        	var btn = Ext.getCmp('saveTrack');
	        	saveTrackEvents(btn);
	        	if(rec != null){
	        		val = rec.get('clientStatus');
	        		setAllowBlank(val);
	        	}
	            trackForm.getForm().reset();
	            trackWindow.show();
	        }
	    });
	      
	      //---------------编辑跟踪记录------
	       var _gridStoreEditAction = new Ext.Action({
		        text: '编辑跟踪记录',
		        iconCls: 'book_edit',
		        scale: 'small',
		        disabled: true,
		        handler: function(){
		         	var record = _grid.getSelectionModel().getSelected();
		            if(record != null){
			        	judgeJs('save_trackJs', 'resources/client/addTrack.js');
			        	var btn = Ext.getCmp('saveTrack');
			        	saveTrackEvents(btn);
		            	trackWindow.show();
		                trackForm.getForm().loadRecord(record);
		            }
		        }
	    });
	    function signEvent(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
		            if(signForm.getForm().isValid()){
		            	var record = myGrid.getSelectionModel().getSelected();
	           			signForm.getForm().submit({
	                    	url: path+'/sign_client/saveOrUpdateSign.do',
		                    params: {
		                        cid: record.id
		                    },
		                    waitTitle: '请等待',
		                    waitMsg: '正在努力的保存数据...',
		                    timeout: 20,
		                    success: function(aForm, aAction){
		                    	signWindow.hide();
		                    	masterStore.reload({callback: myGridUpdateAction1});
	                    		Ext.MessageBox.alert('提示', aAction.result.msg); 
		                    },
		                    failure: function(aForm, aAction) {
		                    	var result = aAction.result;
		                        Ext.MessageBox.alert('提示', result.msg);                           
		                    }
	               		});
		            }
	   			});
	    	}
	   }
       //-------------签单------------------
       var updateStatusAction = new Ext.Action({
	        text: '签单',
	        id: 'update',
	        iconCls: 'drop-yes',
	        scale: 'small',
	        disabled: true,
       		handler: function(){
	         	var record = myGrid.getSelectionModel().getSelected();
	            if(record){
	            	judgeJs('sign_client', 'resources/client/add_sign.js');
	            	var btn = Ext.getCmp('saveSign');
		        	signEvent(btn);
		            signForm.getForm().reset();
		            signWindow.show();
		            statusCombox.setValue('');
		            signWindow.setTitle('签单');
            	}
	        }
	    });
		 var clientSelComboBOx1 = new Ext.form.ComboBox({
		 	   fieldLabel : '客户来源',
	     	   id: 'clientSelComboBOx1',
		       width: 80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
			   mode: 'remote',
		       store: clientSelData,
		       emptyText : '请选择...',
		       valueField: 'client_id',
		       displayField: 'client_name'
		});
	    //---------分配状态查询下拉列表
	    var signStatusData1 = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
	      		[ '1', '已签单'],
			    [ '2', '未签单'],
			    [ '3', '淘汰'],
			    [ '4', '退单']
		      ]
		});
		var signStatusCombox1 = new Ext.form.ComboBox({
		      id : 'signStatusCombox1',
		      store : signStatusData1,
		      width: 70,
		      editable: false,
		      allowBlank: true,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...'// 默认值   selectOnFocus : true,
	    });
		 
	    //---------编辑客户信息
      	var clientEditAction = new Ext.Action({
	        text: '编辑客户',
	        iconCls: 'vcard_edit',
	        scale: 'small',
	        disabled: true,
	        handler: function(){
	            var record = myGrid.getSelectionModel().getSelected(); 
	        	judgeJs('add_clientJs', 'resources/client/addClient.js');
	        	var btn = Ext.getCmp('saveClient');
	        	clientSaveEvents(btn);
	            if(record != null){
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
            		formClientWindow.show();
                	clientForm.getForm().loadRecord(record);
            		clientSelComboBOx.setValue(record.get('clientSourseId'));
	                clientSelComboBOx.setRawValue(record.get('clientSourse'));
	                if(record.get('clientSourse') != ''){
	                	Ext.getCmp('clientSelComboBOx').setDisabled(true);
	                }else{
	                	Ext.getCmp('clientSelComboBOx').setDisabled(false);
	                }
	            }
	        }
	    });
	    //-------------管理人录入人------
        var manage_source = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '1', '管理人'],
	                [ '2', '录入人']
		           ]
		     });
		     
		   // 管理人录入人
	   var manageCom = new Ext.form.ComboBox({
		      id : 'manageCom',
		      width:80,
		      store : manage_source,
		      editable:false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      allowBlank: true,
		      emptyText : '选择管理人录入人...'
	     });
   	     //-------------签单 已签单必须填写备注------
        var notAssign = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '1', '已分配'],
	                [ '2', '未分配']
		           ]
		     });
		     
		   // 下拉列表框控件
	   var notAssignCom = new Ext.form.ComboBox({
		      id : 'notAssignCom',
		      width:80,
		      store : notAssign,
		      editable:false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      allowBlank: true,
		      emptyText : '是否分配...'
	     });
	     	        //--------删除客户信息
	  	    var clientDeleteAction = new Ext.Action({
		        text: '删除客户',
		        iconCls: 'vcard_delete',
		        scale: 'small',
		        disabled: true,
		        handler: function(){
		            if (myGrid.getSelectionModel().hasSelection()) {
		                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
		                    if (aButton == 'yes'){
		                        var id = '';
		                        var flag = 0;
		                        var records = myGrid.getSelectionModel().getSelections();
		                        for(var i = 0, len = records.length; i < len; i ++) {
		                          id = id + records[i].id + ',';
		                        }
	                        	Ext.Ajax.request({
		                            url: '<%=path%>/client/deleteclientAction.do',
		                            params: {
		                                id: id
		                            },
		                            success: function(aResponse, aOptions){
		                            	_gridStore.setBaseParam('_cpid', '');
	                            	    _gridloanLoadAction.execute();
		                            	masterStore.reload({callback: myGridUpdateAction1});
		                                var result = Ext.decode(aResponse.responseText);
					                    Ext.MessageBox.alert('提示', result.msg);
		                            },
		                            failure: function(aResponse, aOptions){
		                                var result = Ext.decode(aOptions.response.responseText);
		                                Ext.MessageBox.alert('提示', result.msg);
		                            }
		                        });
		                    }
		                });
		            }
		        }
	    	});
	     //------------自动分配客户信息--------------
	    var autoAssignAction = new Ext.Action({
	        text: '自动分配',
	        scale: 'small',
	        iconCls: 'zidong',
	        handler: function(){
	        	auto_window.show();
	        }
	    });
	    var auto_window = new Ext.Window({
	        width: 300,
	        height: 120,
	        title:'自动分配',
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: [
	        	{
	        		xtype: 'form',
			        bodyStyle: 'padding:5px',
			        id: 'auto_form',
			        frame: true,
			        layout: 'form',
			        labelWidth: 70,
		            items: [
			           {
			               fieldLabel: '*分配数量',
			               xtype:'textfield',
			               id:'assign_num',
			               anchor: '95%',
			               allowBlank: false,
			               regex:/^[1-9]\d*$/
			           }
		            ]
	        	}
	        ],
	        closeAction: 'hide',
	        buttons: [
	       		{
	              	text: '确定',
		        	iconCls: 'saves',
		        	id: 'eliSure',
		        	handler: function(){
		        		var num = Ext.getCmp('assign_num').getValue();
		        		Ext.getCmp('auto_form').getForm().submit({
                            url: '<%=path%>/client/autoAssignClient.do',
                            params: {
                                num: num
                            },
                            success: function(aResponse, aOptions){
                            	auto_window.hide();
							    masterStore.reload({callback: myGridUpdateAction1});
                                Ext.MessageBox.alert('提示', aOptions.result.msg); 
                            },
                            failure: function(aResponse, aOptions){
                                Ext.MessageBox.alert('提示', aOptions.result.msg); 
                            }
                        });
		        	}
	            },
	            {
	             	text: '返回',
			        iconCls: 'returns',
			        handler: function(){
	           			auto_window.hide();
	        		}
	            }
	       	]
	    });
	    function assignEvent(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
	    			var _emp = employeeComboBOx.getValue();
	    			var num = Ext.getCmp('manual_num').getValue();
	    			if(!num) num = 0;
	    			var records = myGrid.getSelectionModel().getSelections();
	    			var ids = [];
	    			if(!num && !records.length) return;
	    			if(records && records.length){
	                    for(var i = 0, len = records.length; i < len; i ++) {
		    				ids.push(records[i].id);
	                    }
	    			}
	    			if(_emp){
	    				Ext.Ajax.request({
                            url: '<%=path%>/client/assignClient.do',
                            params: {
                                _emp: _emp,
                                num: num,
                                ids: ids.length?ids.join(','):''
                            },
                            success: function(aResponse, aOptions){
                            	assignWindow.hide();
                            	masterStore.reload({callback: myGridUpdateAction1});
                                var result = Ext.decode(aResponse.responseText);
			                    Ext.MessageBox.alert('提示', result.msg);
                            },
                            failure: function(aResponse, aOptions){
                            	assignWindow.hide();
                                Ext.MessageBox.alert('提示', result.msg);
                            }
                        });
	    			}
	    		});
	    	}
	    }
	    //------------手动分配客户信息--------------
	    var assignAction = new Ext.Action({
	        text: '手动分配',
	        scale: 'small', 
	        iconCls: 'shoudong',
	        handler: function(){
	        	judgeJs('assign_clientJs', 'resources/client/assignClient.js');
	        	var btn = Ext.getCmp('assignClientAction');
	        	assignEvent(btn);
	        	employeeComboBOx.reset();
	        	departComboBox.reset();
	        	Ext.getCmp('manual_num').reset();
	        	employeeData.load();
	            assignWindow.show();
	        }
	    });
	     //-----刷新 资源列表---------------
	    var myGridLoadAction = new Ext.Action({
	        handler: function(aMasterId){
				if(roleCode == '201202'){
					assignAction.show();
					departSelComboBox1.hide();
					manageCom.hide();
					clientDeleteAction.hide();
					exportData.hide();
					autoAssignAction.hide();
				}
				if(roleCode == '201203'){
				    clientDeleteAction.hide();
					departSelComboBox1.hide();
					employeeSelComboBOx1.hide();
					manageCom.hide();
					autoAssignAction.hide();
		    		assignAction.hide();
		    		importAction.hide();
		    		exportData.hide();
			    	downloadImportAction.hide();
			    	exportWrongAction.hide();
				}
				if(roleCode == '201208'){
		    		importAction.hide();
		    		exportData.hide();
			    	downloadImportAction.hide();
			    	exportWrongAction.hide();
				}
	            masterStore.load({
	                params:{
	                    start:0,
	                    limit:20
	                }
	            });
	        }
	    }); 
	      //-----刷新 资源列表--资料跟踪-------------
        var _gridloanLoadAction = new Ext.Action({
	        handler: function(){
	            _gridStore.load({
	                params:{
	                    start:0, 
	                    limit:20
	                }
	            });
	        }
	    });   
	      
     //---------------资源列表  -----
      	function myGridUpdateAction1 (){
            var record = myGrid.getSelectionModel().getSelected(); 
            if(record != null){
	            if(record.get('clientStatus') == 1){
	 		    	exitAction.enable();
	       	    }else{
		            exitAction.disable();
	       	    }
	      	    if( record.get('clientStatus') == 3){
	           	    clientEditAction.disable();
	           	    myGridFailureResourceAction.disable();
	           	    _gridStoreNewAction.disable();
	           	    updateStatusAction.disable();
	       	    }else{
	            	clientEditAction.enable();
	           	    myGridFailureResourceAction.enable();
	           	    _gridStoreNewAction.enable();
	           	    updateStatusAction.enable();
	   	     	}
            	clientDeleteAction.enable();
            	autoAssignAction.enable();
           		cid = record.get('id');
           		_gridStore.setBaseParam('_cpid', cid);
               	_gridloanLoadAction.execute();
            }else{
               	clientEditAction.disable();
               	clientDeleteAction.disable();
           		updateStatusAction.disable();
           		_gridStoreNewAction.disable();
               	myGridFailureResourceAction.disable();
            }
  	  	};  
  	    //---------------资源跟踪-----
      	function myGridUpdateAction2 (){
            if(_grid.getSelectionModel().hasSelection()){
	            var record = myGrid.getSelectionModel().getSelected(); 
	            if(record != null){
		           	if(record.get('clientStatus') == '3'){
	            	  _gridStoreEditAction.disable();
		           	}else{
		              _gridStoreEditAction.enable();
	                }
	            }
            }

  	  	}; 
	    //---------------导入的分配按钮------------
	    function importEvent(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
	    			var _value = Ext.getCmp('uploadFilePath').getValue();
		        	if(_value == '' | _value == null){
		        		Ext.MessageBox.alert('提示', '请选择需要导入的Excel文件!');
		        	}else{
		        		var _values = _value.split('\.');
		        		if(_values[_values.length - 1].substring(0, 2) == 'xl'){
		        			Ext.getCmp('importForm').getForm().submit({
			                    url: '<%=path%>/client/importClient.do',
			                    waitTitle: '请等待',
			                    waitMsg: '正在处理数据...',
			                    timeout: 20,
			                   	success: function(aForm, aAction){
			                        masterStore.reload({callback: myGridUpdateAction1});
	                            	importWindow.hide();
	                                Ext.MessageBox.alert('提示', aAction.result.msg); 
			                    },
			                    failure: function(aForm, aAction) {
			                        var result = Ext.decode(aAction.response.responseText);
	                                Ext.MessageBox.alert('提示', result.msg);
			                    }
			                });
		        		}else{
		        			Ext.MessageBox.alert('提示', '只能导入Excel文件, 请重新选择!');
		        		}
		        	}
	    		});
	    	}
	    }
	    //------------------------导入Excel文件--------------------
	    var importAction = new Ext.Action({
	        text: '导入Excel',
	        iconCls: 'export',
	        scale: 'small',
	        handler: function(){
	        	judgeJs('import_clientJs', 'resources/client/importClient.js');
	        	var btn = Ext.getCmp('importAction');
	        	importEvent(btn);
	        	Ext.getCmp('importForm').getForm().reset();
	        	importWindow.show();
	        }
	    });
	    var downloadImportAction = new Ext.Action({
	        text: '下载导入Excel模板',
	        iconCls: 'xzfj',
	        scale: 'small',
	        handler: function(){
	        	location.href='<%=path%>/client/downloadTemplet.do';
	        }
	    });
	    var exportWrongAction = new Ext.Action({
	        text: '下载导入错误的客户资源',
	        iconCls: 'export',
	        scale: 'small',
	        handler: function(){
	        	location.href='<%=path%>/client/exportWrong.do';
	        }
	    });
	     //--------------部门下拉列表-----------------	  
		var departSelComboBox1 = new Ext.form.ComboBox({
		      id: 'departSelComboBox1',
		      emptyText : '请选择部门...',
		      width: 90,
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
		      lazyRender: true,
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'departId',
		      displayField: 'departName',
		      listeners:{
		      	select:function(){
		      		employeeSelComboBOx1.reset();
		      		employeeData.proxy = new Ext.data.HttpProxy({
		      			url: '<%=path%>/client/loadEmployees.do?eid=' + departSelComboBox1.getValue()
		      		});
		      		employeeData.load();
		      	}
		      }
		  });
		   var employeeSelComboBOx1 = new Ext.form.ComboBox({
	     	   id: 'employeeSelComboBOx1',
		       width: 90,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: employeeData,
		       emptyText : '请选择员工...',
		       valueField: 'eId',
		       displayField: 'eName'
		  });
		  var createDateCombox1 = new Ext.form.ComboBox({
		      id : 'createDateCombox1',
		      store : createDateData,
		      width: 60,
		      editable: false,
		      allowBlank: true,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...',// 默认值   selectOnFocus : true,
		      listeners:{
		      	'select': function(){
		      		Ext.getCmp('createStart1').setDisabled(true);
		      		Ext.getCmp('createEnd1').setDisabled(true);
		      	}
		      }
	      });
	      //--------查询商机类型下拉列表-------------------
		  var oppTypeSelCombox1 = new Ext.form.ComboBox({
		      fieldLabel : '*商机类型',
		      id : 'oppTypeSelCombox1',
		      store : oppTypeData,
		      width: 60,
		      editable: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...'
	      });
	      //---------------查询签单可能性下拉列表--------------------------
		  var signPossibleSelCombox1 = new Ext.form.ComboBox({
		      id : 'signPossibleSelCombox1',
		      store : signPossibleData,
		      width: 60,
		      editable: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...'  
	      });
         //--------------省-------市
		  var cityCombox3 = new Ext.form.ComboBox({
		      id : 'cityCombox3',
		      store : cityData,
		      width: 90,
		      editable:false,
		      displayField : 'value',
		      valueField : 'key',
		      name: 'city',
		      mode : 'local',
		      triggerAction : 'all',
		      emptyText : '请选择...',
		      listeners: {
		      	'expand': function(){
		      		var val = provinceCombox3.getValue();
		      		if(val != '' && val != null){
		      			cityData.filterBy(function(rec, id){
		      				return rec.get('pro') == val;
			      		});
		      		}
		      	}
		      }
	      });
		  var provinceCombox3 = new Ext.form.ComboBox({
		      id : 'provinceCombox3',
		      store : provinceData,
		      width: 70,
		      displayField : 'value',
		      editable:false,
		      valueField : 'key',
		      name: 'province',
		      mode : 'local',
		      triggerAction : 'all',
		      emptyText : '请选择...',
		      listeners: {
		      	'select': function(){
		      		cityCombox3.reset();
		      	}
		      }
	      });
	      
	       var searchButton1 = {
		    	xtype:'button',
				iconCls: 'check',
				text:'查询',
				handler:function(){
					if(roleCode == '201206'){
                   		masterStore.setBaseParam('importId', '');
                   	}
					Ext.getCmp('createStart1').setDisabled(false);
			      	Ext.getCmp('createEnd1').setDisabled(false);
			      	Ext.getCmp('signStart').setDisabled(false);
			      	Ext.getCmp('signEnd').setDisabled(false);
					var _createStart = Ext.getCmp('createStart1');
					var _createEnd = Ext.getCmp('createEnd1');
					_createStart = transferDate(_createStart);
					_createEnd = transferDate(_createEnd);
					var createFlag = judgeTime(Ext.getCmp('createStart1'), Ext.getCmp('createEnd1'));
					var _createDate = Ext.getCmp('createDateCombox1').getValue();
					var _oppType=Ext.getCmp('oppTypeSelCombox1').getValue();
					var _assignStatus = Ext.getCmp('signStatusCombox1').getValue();
					var _signPos=Ext.getCmp('signPossibleSelCombox1').getValue();
					var _startLoan = Ext.getCmp('startLoan1').getValue();
					var _endLoan = Ext.getCmp('endLoan1').getValue();
					var _dept = Ext.getCmp('departSelComboBox1').getValue();
					var dept_is_super = 0;
					if(_dept){
						var record = departmentStore.getById(_dept);
						dept_is_super = record.get("superId");
						if(dept_is_super != _dept) dept_is_super = 0;
					}
					var _emp = Ext.getCmp('employeeSelComboBOx1').getValue();
					var _notAssignCom = Ext.getCmp('notAssignCom').getValue();
					var _clientsel = Ext.getCmp('clientSelComboBOx1').getValue();
					var _assignTime = Ext.getCmp('signTimeSelComboBOx');
					_assignTime = transferDate(_assignTime);
					var signTime = Ext.getCmp('signTimeCombo').getValue();
					var signStart = transferDate(Ext.getCmp('signStart'));
					var signEnd = transferDate(Ext.getCmp('signEnd'));
					var _jihuaTime = Ext.getCmp('jihuaTimeSelComboBOx');//----工作计划时间
					_jihuaTime = transferDate(_jihuaTime);
					var _provinces=Ext.getCmp('provinceCombox3').getValue();//--------省份
					var _citys=Ext.getCmp('cityCombox3').getValue();//--------城市
					var phoneNum = Ext.getCmp('phoneNum').getValue();
					var manage = manageCom.getValue();
					if(createFlag){
						if((_startLoan != '' & _endLoan == '') | (_endLoan != '' & _startLoan == '')){
							Ext.MessageBox.alert('提示', '贷款金额条件不完整!');
						}else{
							if(_startLoan > _endLoan){
								Ext.MessageBox.alert('提示', '结束金额必须大于开始金额!');
							}else{
								Ext.apply(masterStore.baseParams,{
									conditions:'{_createStart:"'+_createStart+'",_createEnd:"'+_createEnd+'",_createDate:"'+_createDate+'",_dept:"'+_dept+'",_emp:"'+_emp+'",_oppType:"'+_oppType+'",manage:"'+manage+'",'
										+'_signPos:"'+_signPos+'",_startLoan:"'+_startLoan+'",_endLoan:"'+_endLoan+'",_assignStatus:"'+_assignStatus+'",_notAssignCom:"'+_notAssignCom+'",phoneNum:"'+phoneNum+'",'
											+'_clientsel:"'+_clientsel+'",_assignTime:"'+_assignTime+'",signTime:"'+signTime+'",signStart:"'+signStart+'",signEnd:"'+signEnd+'",_jihuaTime:"'+_jihuaTime+'",_provinces:"'+_provinces+'",_citys:"'+_citys+'",dept_is_super:"'+dept_is_super+'"}'
								});
								masterStore.load({
						            params:{
						                start:0, 
						                limit:20
						            }
							    });
							}
						}
					}else{
						Ext.MessageBox.alert('提示', '结束时间必须大于开始时间!');
					}
					Ext.getCmp('oppTypeSelCombox1').setValue('');
					Ext.getCmp('signPossibleSelCombox1').setValue('');
					Ext.getCmp('startLoan1').setValue('');
					Ext.getCmp('endLoan1').setValue('');
					Ext.getCmp('signStatusCombox1').setValue('');
					Ext.getCmp('createStart1').setValue('');
					Ext.getCmp('createEnd1').setValue('');
					Ext.getCmp('createDateCombox1').setValue('');
					Ext.getCmp('departSelComboBox1').setValue('');
					Ext.getCmp('notAssignCom').setValue('');
					Ext.getCmp('employeeSelComboBOx1').setValue('');
					Ext.getCmp('clientSelComboBOx1').setValue('');
					Ext.getCmp('signTimeSelComboBOx').setValue('');
					Ext.getCmp('signTimeCombo').setValue('');
					Ext.getCmp('signStart').setValue('');
					Ext.getCmp('signEnd').setValue('');
					Ext.getCmp('jihuaTimeSelComboBOx').setValue('');
					Ext.getCmp('provinceCombox3').setValue('');
					Ext.getCmp('cityCombox3').setValue('');
					Ext.getCmp('phoneNum').setValue('');
					manageCom.setValue('');
				}
	    }
	    
	    var clientBar2 = new Ext.Toolbar({
	    	items:[
	    		notAssignCom,
		     	'商机类型：',
				oppTypeSelCombox1,
		     	'成单率：',
				signPossibleSelCombox1,
				'签单状态：',
				signStatusCombox1,
				'贷款金额(万)：',
				{
					xtype: 'textfield',
					width: 70,
					id: 'startLoan1',
					regex:/^[0-9].*$/
				},
				'至',
				{xtype: 'textfield',width: 70,id: 'endLoan1',regex:/^[0-9].*$/},
				'创建日期：',
				createDateCombox1,
				'创建起止日期：',
				 {xtype:'datefield',width:90,format:'Y-m-d',editable: false,id:'createStart1'},
		     	 '至',
		     	 {xtype:'datefield',width:90,format:'Y-m-d',editable: false,id:'createEnd1'},
		     	 '客户来源：',
	    		 clientSelComboBOx1
	    	]
	    });
	    
	     var exportData = new Ext.Action({
		        text: '导出数据',
		        iconCls: 'export',
				handler: function(){
					Ext.getCmp('createStart1').setDisabled(false);
			      	Ext.getCmp('createEnd1').setDisabled(false);
					var _createStart = Ext.getCmp('createStart1');
					var _createEnd = Ext.getCmp('createEnd1');
					_createStart = transferDate(_createStart);
					_createEnd = transferDate(_createEnd);
					var createFlag = judgeTime(Ext.getCmp('createStart1'), Ext.getCmp('createEnd1'));
					var _createDate = Ext.getCmp('createDateCombox1').getValue();
					var _oppType=Ext.getCmp('oppTypeSelCombox1').getValue();
					var _assignStatus = Ext.getCmp('signStatusCombox1').getValue();
					var _signPos=Ext.getCmp('signPossibleSelCombox1').getValue();
					var _startLoan = Ext.getCmp('startLoan1').getValue();
					var _endLoan = Ext.getCmp('endLoan1').getValue();
					var _dept = Ext.getCmp('departSelComboBox1').getValue();
					var _emp = Ext.getCmp('employeeSelComboBOx1').getValue();
					var _notAssignCom = Ext.getCmp('notAssignCom').getValue();
					var _clientsel = Ext.getCmp('clientSelComboBOx1').getValue();
					var _assignTime = Ext.getCmp('signTimeSelComboBOx');
					var pro = provinceCombox3.getValue();
					var city = cityCombox3.getValue();
					var planTime = transferDate(Ext.getCmp('jihuaTimeSelComboBOx'));	
					_assignTime = transferDate(_assignTime);	
					var thisForm = document.createElement('form');
					thisForm.method = 'post';
					thisForm.action = '<%=path%>/client/exportDate.do'
					
					var assignEle = document.createElement('input');
					assignEle.type = 'hidden';
					assignEle.name = '_assignTime';
					assignEle.value = _assignTime;
					
					var a = document.createElement('input');
					a.type = 'hidden';
					a.name = '_createStart';
					a.value = _createStart;
					
					var b = document.createElement('input');
					b.type = 'hidden';
					b.name = '_createEnd';
					b.value = _createEnd;
					
					var c = document.createElement('input');
					c.type = 'hidden';
					c.name = '_createDate';
					c.value = _createDate;
					
					var d = document.createElement('input');
					d.type = 'hidden';
					d.name = '_dept';
					d.value = _dept;
					
					var e = document.createElement('input');
					e.type = 'hidden';
					e.name = '_emp';
					e.value = _emp;
					
					var f = document.createElement('input');
					f.type = 'hidden';
					f.name = '_oppType';
					f.value = _oppType;
					
					var g = document.createElement('input');
					g.type = 'hidden';
					g.name = '_signPos';
					g.value = _signPos;
					
					var h = document.createElement('input');
					h.type = 'hidden';
					h.name = '_startLoan';
					h.value = _startLoan;
					
					var i = document.createElement('input');
					i.type = 'hidden';
					i.name = '_endLoan';
					i.value = _endLoan;
					
					var j = document.createElement('input');
					j.type = 'hidden';
					j.name = '_assignStatus';
					j.value = _assignStatus;
					
					var k = document.createElement('input');
					k.type = 'hidden';
					k.name = '_notAssignCom';
					k.value = _notAssignCom;
					
					var l = document.createElement('input');
					l.type = 'hidden';
					l.name = '_clientsel';
					l.value = _clientsel;
					
					var _pro = document.createElement('input');
					_pro.type = 'hidden';
					_pro.name = 'pro';
					_pro.value = pro;
					
					var _city = document.createElement('input');
					_city.type = 'hidden';
					_city.name = 'city';
					_city.value = city;
					
					var plan = document.createElement('input');
					plan.type = 'hidden';
					plan.name = 'planTime';
					plan.value = planTime;
					
					thisForm.appendChild(a);
					thisForm.appendChild(b);
					thisForm.appendChild(c);
					thisForm.appendChild(d);
					thisForm.appendChild(e);
					thisForm.appendChild(f);
					thisForm.appendChild(g);
					thisForm.appendChild(h);
					thisForm.appendChild(i);
					thisForm.appendChild(j);
					thisForm.appendChild(k);
					thisForm.appendChild(l);
					thisForm.appendChild(assignEle);
					thisForm.appendChild(_pro);
					thisForm.appendChild(_city);
					thisForm.appendChild(plan);
					
					document.body.appendChild(thisForm);
					thisForm.submit();
					
					Ext.getCmp('oppTypeSelCombox1').setValue('');
					Ext.getCmp('signPossibleSelCombox1').setValue('');
					Ext.getCmp('startLoan1').setValue('');
					Ext.getCmp('endLoan1').setValue('');
					Ext.getCmp('signStatusCombox1').setValue('');
					Ext.getCmp('createStart1').setValue('');
					Ext.getCmp('createEnd1').setValue('');
					Ext.getCmp('createDateCombox1').setValue('');
					Ext.getCmp('departSelComboBox1').setValue('');
					Ext.getCmp('notAssignCom').setValue('');
					Ext.getCmp('employeeSelComboBOx1').setValue('');
					Ext.getCmp('clientSelComboBOx1').setValue('');
					Ext.getCmp('signTimeSelComboBOx').setValue('');
					provinceCombox3.setValue('');
					cityCombox3.setValue('');
					Ext.getCmp('jihuaTimeSelComboBOx').setValue('');
				}
		    });
	    var clientBar3 = new Ext.Toolbar({
	    	items:[
	    		 '分配日期：',
				 {xtype:'datefield',width:90,format:'Y-m-d',editable: false,id:'signTimeSelComboBOx'},
		     	 '签单时间:',
		     	 {
		     	 	xtype: 'combo',
		     	 	id : 'signTimeCombo',
			      	store : createDateData,
			      	width: 70,
			      	editable: false,
			      	displayField : 'value', 
			      	valueField : 'key', 
			      	mode : 'local', 
			      	triggerAction : 'all', 
			      	emptyText : '请选择...',// 默认值   selectOnFocus : true,
			      	listeners:{
			      		'select': function(){
			      			Ext.getCmp('signStart').setValue('');
			      			Ext.getCmp('signEnd').setValue('');
			      			Ext.getCmp('signStart').setDisabled(true);
			      			Ext.getCmp('signEnd').setDisabled(true);
			      		}
			      	}
		     	 },
		     	 '签单起止日期:',
		     	 {xtype:'datefield',width:90,format:'Y-m-d',editable: false,id:'signStart'},
		     	 '至',
		     	 {xtype:'datefield',width:90,format:'Y-m-d',editable: false,id:'signEnd'}, 
		     	 '工作计划日期：',
				 {xtype:'datefield',width:90,format:'Y-m-d',editable: false,id:'jihuaTimeSelComboBOx'},
		     	 '省份：',
				provinceCombox3,
				'城市：',
				cityCombox3,
		     	searchButton1,
		     	exportData
	    	]
	    });
		   // --------------- grid  资料列表 --------------------
	    var myGrid = new Ext.grid.GridPanel({
	        id: 'myGrid',
	        store: masterStore,
	        sm: new Ext.grid.RowSelectionModel(),
	        region: 'north',
	        height: 350,
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {header: '客户信息',width:100,sortable: true,dataIndex: 'khxinx'},
	            {
	            	header: '省市',
	            	sortable: true,
	            	dataIndex: 'proCity',
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
	            {header: '客户名字',width:90,hidden:true,sortable: true,dataIndex: 'clientName'},
	            {header: '商机类型',hidden:true,width:80,sortable: true,dataIndex: 'oppType'},
	            {header: '贷款金额(万)',hidden:true,width:80,sortable: true,dataIndex: 'loanAmount'},
	            {header: '客户联系方式',width:100,sortable: true,dataIndex: 'contactTel'},
	            {header: '备用电话1',width:100,sortable: true,dataIndex: 'spareTel1'},
	            {header: '客户状态',width:70,sortable: true,dataIndex: 'clientStatus',
	             renderer: function(val){
	                	if(val == '1'){
	                		return '已签单';
	                	}else if(val == '3'){
	                		return '淘汰';
	                	}else if(val == '2'){
	                		return '未签单';
	                	}else if(val == '4'){
	                		return '退单';
	                	}
	                }
	            },
	            {header: '成单率',width:60,sortable: true,dataIndex: 'signPossible'},
	            {header: '管理人',width:80,sortable: true,dataIndex: 'emp_name'},
	            {header: '录入人',width:80,sortable: true,dataIndex: 'assignName'},
	            {header: '创建日期',width:110,sortable: true,dataIndex: 'assignDate'},
	            {header: '最新工作计划时间',sortable: true,width: 130,dataIndex: 'workPlanNewTime'},
	            {header: '客户来源',width:110,sortable: true,dataIndex: 'clientSourse'},
	            {header: '备注',sortable: true,width:150,dataIndex: 'remark',
	             renderer: function(value, metadata, record, rowIndex, columnIndex, store){
                	metadata.attr = 'ext:qtip="' + value +'"';  
                	return value;
               	 }
	            }
	        ],
	        tbar: [
	     	 	myGridNewAction, clientEditAction,
		        clientDeleteAction, myGridEliminateAction, myGridFailureResourceAction,
			    importAction, downloadImportAction, exportWrongAction,
	    		autoAssignAction, assignAction, departSelComboBox1, employeeSelComboBOx1,manageCom,
	    		'&nbsp;&nbsp;',
	    		{
					xtype: 'textfield',
					width: 120,
					hidden: true,
					emptyText: '客户电话号码...',
					id: 'phoneNum',
					regex:/^[0-9].*$/
				}
	        ],
	        listeners : {
				 'render' : function(e){
					 clientBar2.render(this.tbar);
					 clientBar3.render(this.tbar);
				 },
			  	 'rowdblclick' : function(){
				  	var record = myGrid.getSelectionModel().getSelected();
		        		if(record != null){
		        			Ext.getCmp('remarkMsg').setValue(record.get('remark'));
		        		}
		        		_window.show();
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
	    //-----------------资料追踪-----------------
      	var _grid = new Ext.grid.GridPanel({
	        store:_gridStore,
	        region: 'center',
	        height: 250,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {hidden:true,header: 'ID',sortable: false,dataIndex: 'rtid'},
	            {header: '跟踪内容',width:400,sortable: true,dataIndex: 'resourcescontent',
	                renderer:function(value, metadata, record, rowIndex, columnIndex, store){
	                	metadata.attr = 'ext:qtip="' + value +'"';  
	                	return value;
	                }
	            },
	            {header: '管理人',width:80,sortable: true,dataIndex: 'resourcespeople_name'},
	            {header: '跟单时间',width:150,sortable: true,dataIndex: 'resourcestime'},
	            {header: '工作计划',width:300,sortable: true,dataIndex: 'workplan',
	                renderer:function(value, metadata, record, rowIndex, columnIndex, store){
	                	metadata.attr = 'ext:qtip="' + value +'"';  
	                	return '<font style="color:red;">'+value+'</font>'
	                }
	            },
	            {header:'计划时间',sortable:true,width:130,dataIndex:'plantime'}
	        ],
	        tbar: [
		        _gridStoreNewAction, _gridStoreEditAction, updateStatusAction, exitAction
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store:_gridStore,
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager(),
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	    //-------------------客户签约----------------------
	    var _grid1 = new Ext.grid.GridPanel({
	        store: _gridStore,
	        region: 'south',
	        height: 240,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {header: '上门状态',sortable: true,dataIndex: 'types'},
	            {header: '上门时间',sortable: true,dataIndex: 'calltime'}
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store:_gridStore,
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager(),
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	    myGrid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction1
	    );
	    _grid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction2
	    );
	    var _window = new Ext.Window({
	    	title: '备注信息',
	        width: 380,
	        height: 180,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: [
	        	{
	        		xtype: 'textarea',
	        		id: 'remarkMsg',
	        		name:'remark'
	        	}
	        ],
	        closable: true,
	        closeAction: 'hide',
	        buttons: [
	        	{
	        		text: '返回',
			        iconCls:'returns',
			        handler: function(){
			           _window.hide();
			        }
	        	}
	        ]
	    });
	    //---------------退单------------------------------
	    if(roleCode == '201206'){
	    	Ext.getCmp('phoneNum').show();
	    	clientEditAction.hide();
	    	myGridEliminateAction.hide();
	    	departSelComboBox1.hide();
	    	employeeSelComboBOx1.hide();
	    	manageCom.hide();
	    	myGridFailureResourceAction.hide();
	    	exportData.hide();
	    	assignAction.hide();
		    var viewport = new Ext.Viewport({
			      layout:'fit',
			      border: false,
			      items:[
			     	   myGrid
			      ]
		    });
	    }else{
	    	var mainPanel = new Ext.Panel({
		        autoScroll:true,
		        layout:'form',
		        monitorResize: true, 
		        items:[
		        	myGrid,
		       	    _grid,
		       	    _grid1
		        ]
		     });
		     var viewport = new Ext.Viewport({
			       layout:'fit',
			       border: false,
			       autoScroll:true,
			       items:[
			       	   mainPanel
			       ]
		     });
	    }
	    // --------------- load data -------------------
   		myGridLoadAction.execute();
	});
	</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
