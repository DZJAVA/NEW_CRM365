<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<script type="text/javascript">
			var roleCode = '${userSession.role.roleCode}';
			Ext.onReady(function(){
				Ext.QuickTips.init();// 浮动信息提示
    			Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
				var _assigns = '';
				var _ends = '';
				var _times = '';
				var _manaDes = '';
				var _manaEmp = '';
				var _client_source = '';
				var signPoss = '';
				var masterStore = new Ext.data.JsonStore({
			        url: '<%=path%>/statements/loadClient.do',
			        root: 'data',
			        baseParams: {
			        	conditions:''
			        },
			        fields: [
			            { name: 'signPossible' },
			            { name: 'count1' },
			            { name: 'count2' },
			            { name: 'count3' },
			            { name: 'count4' },
			            { name: 'count5' }
			        ]
			    });
			    var myGridLoadAction = new Ext.Action({
			        handler: function(){
			            masterStore.load();
			        }
			    });
		    	var showMsgStore = new Ext.data.JsonStore({
			        url: '<%=path%>/statements/loadShowClientMsg.do',
			        root: 'data',
			        totalProperty: 'totalCount',
			        idProperty: 'id',
			        autoDestroy: true,
			        baseParams: {
			        	conditions1: ''
			        },
			        fields: [
			        	{ name: 'id'},
			            { name: 'assignDate'},
			            { name: 'clientName'},
			            { name: 'showOppType'},
			            { name: 'loanAmount'},
			            { name: 'clientStatus'},
			            { name: 'signPossible'},
			            { name: 'assignTime'},
			            { name: 'empName'},
			            { name: 'remark'}
			        ]
			    });
			    //--------------部门下拉列表-----------------	  
			    var departmentSelStore = new Ext.data.Store({
			          proxy: new Ext.data.HttpProxy({
			              url: '<%=path%>/client/loadDepartment.do'
			          }),
			          reader: new Ext.data.JsonReader({
			                  root: 'data',
			                  id: 'departId'
			              }, 
			            ['departId', 'departName']
			          )
				});
				
				var departSelComboBox = new Ext.form.ComboBox({
				      id: 'departSelComboBox',
				      emptyText : '请选择跟踪部门...',
				      width: 120,
				      typeAhead: true,
				      editable: false,
				      triggerAction: 'all',
				      lazyRender: true,
					  mode: 'remote',
				      store: departmentSelStore,
				      valueField: 'departId',
				      displayField: 'departName',
				      listeners:{
				      	select:function(){
				      		employeeSelComboBOx.reset();
				      		employeeSelData.proxy = new Ext.data.HttpProxy({
				      			url: '/CRM/client/loadEmployees.do?eid=' + departSelComboBox.getValue()
				      		});
				      		employeeSelData.load();
				      	}
				      }
				  });
				  
		         var employeeSelData = new Ext.data.Store({
		     		proxy: new Ext.data.HttpProxy({
				          url: '<%=path%>/client/loadEmployee.do'
				    }),
				    reader: new Ext.data.JsonReader({
				           root: 'data',
				           id: 'eId'
				    	}, 
			          ['eId', 'eName']
				    )
				 });
				 var employeeSelComboBOx = new Ext.form.ComboBox({
			     	   id: 'employeeSelComboBOx',
				       width: 120,
			           typeAhead: true,
				       editable: false,
				       triggerAction: 'all',
				       lazyRender: true,
					   mode: 'remote',
				       store: employeeSelData,
				       emptyText : '请选择跟踪人...',
				       valueField: 'eId',
				       displayField: 'eName',
				       listeners: {
				       	'expand': function(){
				       		var _dept = Ext.getCmp('departSelComboBox').getValue();
				       		if(_dept == '' | _dept == null){
				       			employeeSelComboBOx.reset();
					      		employeeSelData.proxy = new Ext.data.HttpProxy({
					      			url: '/CRM/client/loadEmployee.do'
					      		});
					      		employeeSelData.load();
				       		}
				       	}
				       }
				});
				
				var clientSelData1 = new Ext.data.Store({
		     		proxy: new Ext.data.HttpProxy({
		     			 url: '<%=path%>/clientsource/loadclients.do'
				    }),
				    reader: new Ext.data.JsonReader({
				           root: 'data',
				           id: 'client_id'
				    	}, 
			          ['client_id', 'client_name']
				    )
				 });
				 var clientSelComboBOx1 = new Ext.form.ComboBox({
				 	   fieldLabel : '客户来源',
			     	   id: 'clientSelComboBOx1',
				       width: 120,
			           typeAhead: true,
				       editable: false,
				       triggerAction: 'all',
				       lazyRender: true,
					   mode: 'remote',
				       store: clientSelData1,
				       emptyText : '请选择客户来源...',
				       valueField: 'client_id',
				       displayField: 'client_name'
				});
				if(roleCode == '201202'){
					departSelComboBox.hide();
				}
				if(roleCode == '201203'){
		       	 	departSelComboBox.hide();
		       	 	employeeSelComboBOx.hide();
		        }
		        var timeData = new Ext.data.SimpleStore({
				      fields:['key', 'value'],
				      data:[
			      		[ '1', '本周'],
					    [ '2', '本月'],
					    [ '3', '本年']
				      ]
				});
				var timeCombox = new Ext.form.ComboBox({
				      id : 'timeCombox',
				      store : timeData,
				      width: 80,
				      editable: false,
				      allowBlank: true,
				      displayField : 'value', 
				      valueField : 'key', 
				      mode : 'local', 
				      triggerAction : 'all', 
				      emptyText : '请选择...',
				      listeners:{
				        'select':function(){
				            Ext.getCmp('assignDate').setValue('');
				            Ext.getCmp('assignDate').setDisabled(true);
				            Ext.getCmp('endAssignDate').setValue('');
				            Ext.getCmp('endAssignDate').setDisabled(true);
				         }
				      }
			    });
			    
			    var searchButton = {
			    	xtype:'button',
					iconCls: 'check',
					text:'查 询',
					handler:function(){
						var _time = timeCombox.getValue();
						var _assign = Ext.getCmp('assignDate').getValue();
						var _endAssignDate = Ext.getCmp('endAssignDate').getValue();
						var _depComboBox = Ext.getCmp('departSelComboBox').getValue();
						var _empComboBox = Ext.getCmp('employeeSelComboBOx').getValue();
						var client_source = Ext.getCmp('clientSelComboBOx1').getValue();
						if(_assign != ''){
							_assign = _assign.format('Y-m-d');
						}
						if(_endAssignDate != ''){
							_endAssignDate = _endAssignDate.format('Y-m-d');
						}
						if(_time == '1' | _time == '本周'){
							_time = '1';
						}else if(_time == '2' | _time == '本月'){
							_time = '2';
						}else if(_time == '3' | _time == '本年'){
							_time = '3';
						}
						if(_time != '' & (_assign != '' | _endAssignDate != '')){
							Ext.MessageBox.alert('提示', '分配日期和时间不能同时填写！');
						}else{
							if(_assign != '' & _endAssignDate != ''){
								var _start = Ext.getCmp('assignDate').getValue().format('Ymd');
								var end = Ext.getCmp('endAssignDate').getValue().format('Ymd');
								if(_start > end){
									Ext.MessageBox.alert('提示', '结束时间必须大于开始时间');
								}else{
									_assigns = _assign;
									_ends = _endAssignDate;
									_times = _time;
									_manaDes = _depComboBox;
									_manaEmp = _empComboBox;
									_client_source = client_source;
									Ext.apply(masterStore.baseParams,{
										conditions:'{_assign:"'+_assign+'",_endAssignDate:"'+_endAssignDate+'",_time:"'+_time+'",_depComboBox:"'+_depComboBox+'",_empComboBox:"'+_empComboBox+'",client_source:"'+_client_source+'"}'
									});
									masterStore.load();
								}
							}else{
								_assigns = _assign;
								_ends = _endAssignDate;
								_times = _time;
								_manaDes = _depComboBox;
								_manaEmp = _empComboBox;
								_client_source = client_source;
								Ext.apply(masterStore.baseParams,{
									conditions:'{_assign:"'+_assign+'",_endAssignDate:"'+_endAssignDate+'",_time:"'+_time+'",_depComboBox:"'+_depComboBox+'",_empComboBox:"'+_empComboBox+'",client_source:"'+_client_source+'"}'
								});
								masterStore.load();
							}
						}
					}
			    }
			    
			    //重置查询条件
			      var reset5 = new Ext.Action({
				        text: '重置',
				        iconCls:'btn_del',
				        scale:'small',
				        handler: function(aMasterId){
			            	timeCombox.setValue('');
							Ext.getCmp('assignDate').setValue('');
							Ext.getCmp('endAssignDate').setValue('');
							Ext.getCmp('departSelComboBox').setValue('');
							Ext.getCmp('departSelComboBox').setRawValue('');
							Ext.getCmp('employeeSelComboBOx').setValue('');
							Ext.getCmp('clientSelComboBOx1').setValue('');
							Ext.getCmp('employeeSelComboBOx').setRawValue('');
							Ext.getCmp('assignDate').setDisabled(false);
				            Ext.getCmp('endAssignDate').setDisabled(false);
				        }
			    });
			    var oneTbar=new Ext.Toolbar({
					items:[
						'时间：',
				     	timeCombox,
						'日期：',
						{
				     	 	xtype:'datefield',
				     	 	width:100,
				     	 	format:'Y-m-d',
				     	 	editable: true,
				     	 	id:'assignDate'
				     	},
				     	'&nbsp至&nbsp',
				     	{
				     		xtype:'datefield',
				     	 	width:100,
				     	 	format:'Y-m-d',
				     	 	editable: true,
				     	 	id:'endAssignDate'
				     	},
				     	'&nbsp&nbsp',
				     	departSelComboBox,
				     	'&nbsp&nbsp',
						employeeSelComboBOx,
						'&nbsp&nbsp',
						clientSelComboBOx1,
						searchButton,
						reset5
					] 
				}); 
				//----------------------showGrid-------------------
			    var showGrid = new Ext.grid.GridPanel({
			        id: 'showGrid',
			        store: showMsgStore,
			        width: '100%',
			        sm: new Ext.grid.RowSelectionModel(),
			        frame:true,
			        trackMouseOver: false,
			        disableSelection: false,
			        loadMask: true,
			        columns: [
			        	{
			            	header: '录入日期',
			                sortable: true,
			                dataIndex: 'assignDate'                                                  
			            },
			            {
			            	header: '客户名称',
			                sortable: true,
			                dataIndex: 'clientName'
			            },
			            {
			            	header: '商机类型',
			                sortable: true,
			                dataIndex: 'showOppType'
			            },
			            {
			            	header: '贷款金额',
			                sortable: true,
			                dataIndex: 'loanAmount'
			            },
			            {
			            	header: '客户状态',
			                sortable: true,
			                dataIndex: 'clientStatus'
			            },
			            {
			            	header: '签单可能性',
			                sortable: true,
			                dataIndex: 'signPossible'
			            },
			            {
			            	header: '分配时间',
			                sortable: true,
			                dataIndex: 'assignTime'
			            },
			            {
			            	header: '管理人',
			                sortable: true,
			                dataIndex: 'empName'
			            },
			            {
			            	header: '备注',
			            	sortable: true,
			            	dataIndex: 'remark'
			            }
			        ],
			        bbar: new Ext.PagingToolbar({
			        	id: 'showMsgPage',
			            pageSize: 20,
			            store: showMsgStore,
			            displayInfo: true,
			            displayMsg: '显示: {0} - {1} / 总数: {2}',
			            emptyMsg: '没有记录'
			        })
			    });
			    
				    var pan = new Ext.Panel({
						iconCls: 'chart',
						frame:true,
				        height: 400,
				        anchor: '100%',
						layout: 'fit',
					    items: [{
					        xtype: 'linechart',
					        store: masterStore,
					        url: '<%=path%>/resources/charts.swf',
					        xField: 'signPossible',
					        xAxis: new Ext.chart.CategoryAxis({
				                title: '签单可能性'
				            }),
				            yAxis: new Ext.chart.NumericAxis({
				                title: '客户资源量'
				            }),
				            extraStyle: {  
				                 legend: {  
				                       display: 'bottom',  
				                       padding: 5,  
				                       font: {  
				                          family : 'Tahoma',  
				                          size: 13  
				                       }  
				                 }  
				            },
					        chartStyle: {
					            padding: 10,
					            animationEnabled: true,//柱状图 弹性显示
					            font: {
					                name: 'Tahoma',
					                color: 0x444444,
					                size: 11
					            },
					            dataTip: {
					                padding: 5,
					                border: {
					                    color: 0x99bbe8,
					                    size: 1
					                },
					                background: {
					                    color: 0xDAE7F6,
					                    alpha: .9
					                },
					                font: {
					                    name: 'Tahoma',
					                    color: 0x15428B,
					                    size: 10,
					                    bold: true
					                }
					            },
					            xAxis: {
					                color: 0x69aBc8,
					                majorTicks: {
					                    color: 0x69aBc8,
					                    length: 4
					                },
					                minorTicks: {
					                    color: 0x69aBc8,
					                    length: 2
					                },
					                majorGridLines: {
					                    size: 1,
					                    color: 0xeeeeee
					                }
					            },
					            yAxis: {
					                color: 0x69aBc8,
					                majorTicks: {
					                    color: 0x69aBc8,
					                    length: 4
					                },
					                minorTicks: {
					                    color: 0x69aBc8,
					                    length: 2
					                },
					                majorGridLines: {
					                    size: 1,
					                    color: 0xdfe8f6
					                }
					            }
					        },
					        series: [
					        	{
						            type: 'column',
						            displayName: '房贷',
						            yField: 'count1',
						            style: {
						            	marginBottom: '10px',
						                color: '#E82F37'
					           	 	}
					            },
								{
						            type: 'column',
						            displayName: '信贷',
						            yField: 'count2',
						            style: {
						            	marginBottom: '10px',
						                color: '#A8A8A8'
						            }
						       	},
						       	{
						            type: 'column',
						            displayName: '短借',
						            yField: 'count3',
						            style: {
						            	marginBottom: '10px',
						                color: '#A9DBF6'
						            }
						       	},
						       	{
						            type: 'column',
						            displayName: '企贷',
						            yField: 'count4',
						            style: {
						            	marginBottom: '10px',
						                color: '#0x69aBc8'
						            }
						       	},
						       	{
						            type: 'column',
						            displayName: '未定义',
						            yField: 'count5',
						            style: {
						            	marginBottom: '10px',
						                color: '#3307F4'
						            }
						       	}
							],
							listeners: {
								'itemClick': function(e){
									var record = masterStore.getAt(e.index);
									signPoss = record.get('signPossible');
									if(_times == '1' | _times == '本周'){
										_times = '1';
									}else if(_times == '2' | _times == '本月'){
										_times = '2';
									}else if(_times == '3' | _times == '本年'){
										_times = '3';
									}
									if(signPoss == '未知'){
										signPoss = '6';
									}else if(signPoss == '100%'){
										signPoss = 1;
									}else if(signPoss == '80%'){
										signPoss = 2;
									}else if(signPoss == '50%'){
										signPoss = 3;
									}else if(signPoss == '10%'){
										signPoss = 4;
									}else if(signPoss == '0%'){
										signPoss = 5;
									}
									Ext.apply(showMsgStore.baseParams,{
										conditions1:'{_assigns:"'+_assigns+'",signPoss:"'+signPoss+'",_ends:"'+_ends+'",_times:"'+_times+'",_manaDes:"'+_manaDes+'",_manaEmp:"'+_manaEmp+'",client_source:"'+_client_source+'"}'
									});
									showMsgStore.load({
							            params:{
							                start:0, 
							                limit:20
							            }
								    });
									_window.show();
								}
							}
				    	}
					]
				});
				var returnAction = new Ext.Action({
					text: '返回',
					iconCls: 'returns',
					handler: function(){
						_window.hide();			
					}
				});
			    var _window = new Ext.Window({
			    	title: '详细客户信息',
			        width: 850,
			        height: 450,
			        modal: true,
			        layout: 'fit',
			        plain: true,
			        bodyStyle: 'padding:5px;',
			        buttonAlign: 'center',
			        closable: false,
			        closeAction: 'hide',
			        items: showGrid,
			        buttons: [
			            returnAction
			        ]
			    });
			    
			 	var mainPanels = new Ext.form.FormPanel({
					layout:'form',
					border:false,
					frame:true,
					autoScroll:true,
					items:[
						oneTbar,
						pan
			        ]
				});
				var viewport = new Ext.Viewport({
			        border: false,
					layout:'fit',
			        items: [
			        	mainPanels
			        ]
			    });
			    myGridLoadAction.execute();
			});
		</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
