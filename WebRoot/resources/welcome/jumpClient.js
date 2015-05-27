	//---------------资源列表-----
	    var clientStore = new Ext.data.JsonStore({
	        url: path+'/client/loadClient.do',
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
                 { name: 'id' },{ name: 'clientName' },{ name: 'contactTel' },{ name: 'loanAmount' },
	             { name: 'clientAdd' },{ name: 'oppType' },{ name: 'spareTel1' },{ name: 'spareTel2' },
	             { name: 'clientStatus' },{ name: 'signPossible' },{ name: 'assignDate' },{ name: 'assignTime' },
	             { name: 'remark' },{ name: 'emp_name' }, { name: 'userId'},{ name: 'khxinx'},
	             { name: 'clientSourse'},{ name: 'clientSourseId'},{ name: 'workPlanNewTime'},{ name: 'eliTime'},
	             { name: 'proCity'},{ name: 'city'},{ name: 'province'}
	        ]
	    });
	    //---------------资源跟踪列表-----
	    var _gridStore = new Ext.data.JsonStore({
	        url: path+'/ResourcesTrack/findByTrackrecord.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions:'',
	        	_cpid: ''
	        },
	        fields: [
	            { name: 'rtid' },{ name: 'resourcescontent' },{ name: 'resourcestime' },
	            { name: 'resourcespeople_name' },{ name: 'intoasinglerate' },{ name: 'workplan' },
	            { name: 'remark' },{ name: 'plantime'},{ name: 'calltime' },{ name: 'types' }
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
			                    url: path+'/client/saveOrUpdateClient.do',
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
			                    		clientStore.setBaseParam('importId', '');
				                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
				                    	clientStore.load({ params: {start:0, limit:20} });
			                    	}else{
			                    		Ext.MessageBox.alert('提示', aAction.result.msg); 
				                        clientStore.reload();
			                    	}
			                    },
			                    failure: function(aForm, aAction) {
			                    	var result = aAction.result;
			                    	if(roleCode == '201206'){
			                    		if(result.id != '' && result.id != null){
			                    			Ext.Msg.confirm('显示确认', result.msg, function(btn){
			                    				if(btn == 'yes'){
			                    					formClientWindow.hide();
						                    		clientStore.setBaseParam('importId', result.id);
							                    	clientStore.load({ params: {start:0, limit:20} });
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
	            Ext.getCmp('assignDate').setValue(new Date().format('Y-m-d'));
	            formClientWindow.show();
	        }
	    });
	       //----------淘汰资源列表
	  	var myGridEliminateAction = new Ext.Action({
	        text: '查看已淘汰资源',
	        iconCls: 'vcard',
	        handler: function(){
	        	judgeJs('view_eliClientJs', 'resources/client/viewEliClient.js');
			    _gridEliminateLoadAction.execute();
			    formWindowEliminate.show();
	        }
	    });
	    var onInformation = new Ext.Action({
		      text:'上一个',
		      iconCls:'lasts',
		      handler:function(){
	      		var rec = clientStore.getAt(0);
		      	if(rec != null){
			      	client_id = rec.id;
		      	}
		      	if(jump_from_flag == 1){
		      		clientStore.baseParams._flag = '0' + ',' + client_id;
		      	}else if(jump_from_flag == 4){
		      		clientStore.baseParams._flag = '2' + ',' + client_id + ',' + sign_time;
		      	}else if(jump_from_flag == 2){
		      		clientStore.baseParams._flag = '4' + ',' + plan_id + ',' + plan_time;
		      	}else if(jump_from_flag == 5){
		      		clientStore.baseParams._flag = '6' + ',' + client_id + ',' + conditions;
		      	}
		      	clientLoadAction.execute();
		      }
	    });
	    
	    var nextClientMsg = new Ext.Action({
		      text:'下一个',
		      iconCls:'nexts',
		      handler:function(){
		      	if(clientStore.getAt(0) != null){
			      	var _id = clientStore.getAt(0).id;
			      	if(jump_from_flag == 1){
			      		clientStore.baseParams._flag = '1' + ',' + _id;
			      	}else if(jump_from_flag == 4){
			      		clientStore.baseParams._flag = '3' + ',' + _id + ',' + sign_time;
			      	}else if(jump_from_flag == 2){
			      		clientStore.baseParams._flag = '5' + ',' + plan_id + ',' + plan_time;
			      	}else if(jump_from_flag == 5){
			      		clientStore.baseParams._flag = '7' + ',' + _id + ',' + conditions;
			      	}
			      	clientLoadAction.execute();
		      	}else{
		      		Ext.MessageBox.alert('提示', '没有相应的客户信息!');   
		      	}
		      }
	    });
	    function eliEvent(btn){
	    	if(!btn.hasListener('click')){
	    		btn.addListener('click', function(){
    				var cid = clientStore.getAt(0).id;
	    			var eliRemark = Ext.getCmp('eliRemark').getValue();
		            Ext.Ajax.request({
		                url: path+'/ResourcesTrack/saveFailureResource.do',
		                params: {
		                	cid: cid,
		                	eli: eliRemark
		                },
	                    success: function(aResponse, aOptions){
	                   		eliWindow.hide();
	                   		var _id = clientStore.getAt(0).id;
	                   		if(roleCode != '201201'){
		                   		if(jump_from_flag == 1){
			                   		clientStore.baseParams._flag = '1' + ',' + _id;
			                        clientLoadAction.execute();
		                   		}
		                   		if(jump_from_flag == 2){
		                   			clientStore.baseParams._flag = '5' + ',' + plan_id + ',' + plan_time;
		                   			clientLoadAction.execute();
		                   		}
		                   		if(jump_from_flag == 5){
						      		clientStore.baseParams._flag = '7' + ',' + _id + ',' + conditions;
						      	}
	                   		}else{
	                   			clientStore.load();
	                   		}
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
	        text: '淘汰资源',
	        iconCls: 'btn_del',
	        handler: function(){
	        	if(clientStore.getAt(0) != null){
	            	var _s = clientStore.getAt(0).get('clientStatus');
	           	 	if(_s =='1'){
	            	 	Ext.MessageBox.alert('提示', "已签单成功资源不能进行淘汰！");
	           	 	}else{
			        	judgeJs('eli_clientJs', 'resources/client/eliClient.js');
			        	var btn = Ext.getCmp('eliSure');
			        	eliEvent(btn);
	            	  	Ext.getCmp('eliForm').getForm().reset();
	            		eliWindow.show();
	           		}
	        	}else{
	        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');
	        	}
       	 	}
    	});
        //----------首页双击进入对淘汰资源的直接认领--------------
	    var renlingAction = new Ext.Action({
	        text: '认领资源',
	        iconCls: 'btn_add',
	        handler: function(){
	        	if(clientStore.getAt(0) != null){
		        	addFlag = 2;
		        	var val = clientStore.getAt(0).get('clientStatus');
		        	if(val == '3'){
		        		addForm.getForm().reset();
		            	addWindow.show();
		        	}else{
	        			Ext.MessageBox.alert('提示', '只有淘汰资源能够认领！');     
	        		}
	        	}else{
	        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');
	        	}
	        }
	    });
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
			            var	cid = clientStore.getAt(0).id;
		                trackForm.getForm().submit({
		                    url: path+'/ResourcesTrack/saveOrUpdateResourcesTrack.do',
		                    params: {
			                    _cpid: cid,gen:gen,_cs:_cs
		                    },
		                    waitTitle: '请等待',
		                    waitMsg: '正在努力的保存数据...',
		                    timeout: 20,
		                    success: function(aForm, aAction){
                            	trackWindow.hide();
							  	_gridStore.reload({callback: myGridUpdateAction2});
							  	clientStore.reload();
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
	        handler: function(){
	        	var val = '';
	        	if(clientStore.getAt(0) != null){
		        	judgeJs('save_trackJs', 'resources/client/addTrack.js');
		        	var btn = Ext.getCmp('saveTrack');
		        	saveTrackEvents(btn);
	        		val = clientStore.getAt(0).get('clientStatus');
	        		setAllowBlank(val);
		            trackForm.getForm().reset();
		            trackWindow.show();
	        	}else{
	        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');   
	        	}
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
		            	var record = clientStore.getAt(0);
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
		                    	clientStore.reload();
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
       		handler: function(){
            	if(clientStore.getAt(0) != null){
        			judgeJs('sign_client', 'resources/client/add_sign.js');
	            	var btn = Ext.getCmp('saveSign');
		        	signEvent(btn);
		            signForm.getForm().reset();
		            signWindow.show();
		            statusCombox.setValue('');
		            signWindow.setTitle('签单');
	        	}else{
	        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');
	        	}
	        }
	    });
	    //---------编辑客户信息
      	var clientEditAction = new Ext.Action({
	        text: '编辑客户',
	        iconCls: 'vcard_edit',
	        handler: function(){
	        	if(clientStore.getAt(0) != null){
		            var record = clientStore.getAt(0)
		        	judgeJs('add_clientJs', 'resources/client/addClient.js');
		        	var btn = Ext.getCmp('saveClient');
		        	clientSaveEvents(btn);
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
	        	}else{
	        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');
	        	}
	        }
	    });
	     	        //--------删除客户信息
	  	    var clientDeleteAction = new Ext.Action({
		        text: '删除客户信息',
		        iconCls: 'vcard_delete',
		        handler: function(){
		        	if(clientStore.getAt(0) != null){
		                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
		                    if (aButton == 'yes'){
		                        var id = clientStore.getAt(0).id;
	                        	Ext.Ajax.request({
		                            url: path+'/client/deleteclientAction.do',
		                            params: {
		                                id: id
		                            },
		                            success: function(aResponse, aOptions){
		                            	_gridStore.setBaseParam('_cpid', '');
	                            	    _gridloanLoadAction.execute();
		                            	clientStore.reload();
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
		        	}else{
		        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');
		        	}
		        }
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
                            url: path+'/client/assignClient.do',
                            params: {
                                _emp: _emp,
                                num: num,
                                ids: ids.length?ids.join(','):''
                            },
                            success: function(aResponse, aOptions){
                            	assignWindow.hide();
                            	clientStore.reload();
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
	        iconCls: 'shoudong',
	        handler: function(){
	        	if(clientStore.getAt(0) != null){
		        	judgeJs('assign_clientJs', 'resources/client/assignClient.js');
		        	var btn = Ext.getCmp('assignClientAction');
		        	assignEvent(btn);
		        	employeeComboBOx.reset();
		        	departComboBox.reset();
		        	employeeData.load();
		            assignWindow.show();
	        	}else{
	        		Ext.MessageBox.alert('提示', '没有相应的客户信息!');
	        	}
	        }
	    });
	     //-----刷新 资源列表---------------
	    var clientLoadAction = new Ext.Action({
	        handler: function(){
				if(roleCode == '201202'){
					assignAction.show();
					clientDeleteAction.hide();
				}
				if(roleCode == '201203'){
				    clientDeleteAction.hide();
		    		assignAction.hide();
				}
				if(jump_from_flag == 3 | jump_from_flag == 7){
					onInformation.hide();
					nextClientMsg.hide();
				}else{
					onInformation.show();
					nextClientMsg.show();
				}
	            clientStore.load({
	                callback: function(records, options, success){
	                	var receive = clientStore.reader.jsonData.nextId;
                		var record = clientStore.getAt(0);
                		if(receive != '' && receive != null){
	                		var receives = receive.split(',')
                			if(jump_from_flag == 2){
                				plan_id = receives[0];
                				plan_time = receives[1];
                			}
                			if(jump_from_flag == 4){
                				sign_time = receives[1];
                			}
                		}
                		if(record != null){
                			if(roleCode == '201201' || roleCode == '201208'){
                				_gridStore.baseParams._cpid = record.id;
	                			_gridloanLoadAction.execute();
                			}else{
                				if(jump_from_flag == 1 | jump_from_flag == 2){
		                			if(record.get('clientStatus') == '3'){
		                				client_id = record.id;
		                				clientStore.removeAll();
		                				_gridStore.removeAll();
		                				Ext.MessageBox.alert('提示', '最后一个客户被淘汰,没有下一个客户了!');   
		                			}else{
			                			_gridStore.baseParams._cpid = record.id;
			                			_gridloanLoadAction.execute();
		                			}
                				}else{
                					_gridStore.baseParams._cpid = record.id;
		                			_gridloanLoadAction.execute();
                				}
                			}
                		}else{
                			_gridStore.baseParams._cpid = '';
                			_gridloanLoadAction.execute();
                		}
                		if(seeFlag == 0){
                			seeFlag++;
                			Ext.Ajax.request({
	                            url: path+'/client/saveClickClient.do',
	                            params: {
	                                name: record.get('clientName'),
	                                opp: record.get('oppType'),
	                                cid: record.id
	                            },
	                            success: function(aResponse, aOptions){
	                                var result = Ext.decode(aResponse.responseText);
	                                fillSeeRec(result.data);
	                            },
	                            failure: function(aResponse, aOptions){
	                                var result = Ext.decode(aResponse.responseText);
	                                Ext.MessageBox.alert('提示', result.msg);
	                            }
	                        });
                		}
	                }
	            });
	        }
	    }); 
	    function fillSeeRec(btns){
	    	var btn = null;
	    	seeBtns.length = 0;
	    	for(var i = 0; i < btns.length; i++){
	    		btn = new Ext.Action({
	    			id: btns[i].id,
	    			text: btns[i].name,
	    			iconCls: 'smt-systemLog',
	    			handler: function(){
	    				jump_from_flag = 7;//点击记忆按钮跳转
	    				seeFlag++;
	    				mainPanels.hide();
	    				clientPanel.show();
	    				clientStore.setBaseParam('_cpid', this.id);
						clientStore.setBaseParam('_flag', '');
						clientLoadAction.execute();
	    			}
	    		}); 
	    		seeBtns.push(btn);
	    	}
	    }
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
  	    //---------------资源跟踪-----
      	function myGridUpdateAction2 (){
            if(_grid.getSelectionModel().hasSelection()){
	            var record = clientStore.getAt(0); 
	            if(record != null){
		           	if(record.get('clientStatus') == '3'){
	            	  _gridStoreEditAction.disable();
		           	}else{
		              _gridStoreEditAction.enable();
	                }
	            }else{
	            	_gridStoreEditAction.enable();
	            }
            }

  	  	}; 
       	//----------返回到首页
	  	var returnToIndex = new Ext.Action({
	        text: '返回首页',
	        iconCls: 'returns', 
	        id: 'returnPage'
	    });
	   	// --------------- grid  资料列表 --------------------
	    var myGrid = new Ext.grid.GridPanel({
	        id: 'myGrid',
	        store: clientStore,
	        sm: new Ext.grid.RowSelectionModel(),
	        region: 'north',
	        height: 180,
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {header: '客户信息',width:120,sortable: true,dataIndex: 'khxinx'},
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
	            {header: '客户状态',width:90,sortable: true,dataIndex: 'clientStatus',
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
	            {header: '成单率',width:90,sortable: true,dataIndex: 'signPossible'},
	            {header: '管理人',width:90,sortable: true,dataIndex: 'emp_name'},
	            {header: '创建日期',width:90,sortable: true,dataIndex: 'assignDate'},
	            {header: '最新工作计划时间',sortable: true,width: 130,dataIndex: 'workPlanNewTime'},
	            {header: '客户来源',width:110,sortable: true,dataIndex: 'clientSourse'},
	            {header: '签单可能性',width:90,sortable: true,dataIndex: 'signPossible'},
	            {header: '备注',sortable: true,width:150,dataIndex: 'remark',
	             renderer: function(value, metadata, record, rowIndex, columnIndex, store){
                	metadata.attr = 'ext:qtip="' + value +'"';  
                	return value;
               	 }
	            }
	        ],
	        tbar: [
	     	 	returnToIndex, onInformation, nextClientMsg, myGridNewAction, clientEditAction,
		        clientDeleteAction, myGridEliminateAction, myGridFailureResourceAction,
			    renlingAction, assignAction
	        ]
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
		        _gridStoreNewAction, _gridStoreEditAction, updateStatusAction
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
	    _grid.getSelectionModel().addListener(
	        'selectionchange', 
	        myGridUpdateAction2
	    );
		//-----------认领资源按钮 -- 弹出 层 填写添加备注信息
	    var addForm = new Ext.form.FormPanel({
	        bodyStyle: 'padding:5px',
	        frame: true,
	        labelWidth: 70,
	        layout: 'form',
	        items:[
				{
                	fieldLabel: '添加备注',
                  	xtype:'textarea',
                  	anchor: '95%',
                  	id:'addRemark'
	            }	        
	        ]
	    });
	    var addSaveAction = new Ext.Action({
	        text: '确定',
	        iconCls: 'saves',
	        handler: function(){
	        	var rec = clientStore.getAt(0);
	        	var addRemark = Ext.getCmp('addRemark').getValue();
	        	if(rec != null){
	        		var id = rec.id;
	        		var eliTime = rec.get('eliTime');
		            Ext.Ajax.request({
		                url: path+'/ResourcesTrack/saveAddResource.do',
		                params: {
		                	id: id,
		                	eli: eliTime,
		                	add: addRemark
		                },
	                    success: function(aResponse, aOptions){
	                   		addWindow.hide();
	                        clientStore.reload();
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
	    var addWindow = new Ext.Window({
	        width: 420,
	        height: 180,
	        title:'添加资源',
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: addForm,
	        closeAction: 'hide',
	        buttons: [
                {
                	text: '返回',
			        iconCls: 'returns',
			        handler: function(){
			           addWindow.hide();
			        }
                },
                addSaveAction
            ]
	    });
		clientPanel = new Ext.Panel({
	        autoScroll:true,
	        layout:'form',
	        monitorResize: true, 
	        items:[
	        	myGrid,
	       	    _grid,
	       	    _grid1
	        ]
	    });