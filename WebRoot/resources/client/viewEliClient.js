	
	var track_id = '';
	    //---------------资源列表  淘汰资源 列表-----
    var _gridEliminate = new Ext.data.JsonStore({
        url: path+'/ResourcesTrack/loadEliminateClient.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions:''
        },
        fields: [
             { name: 'id' },
             { name: 'clientName' },
             { name: 'contactTel' },
             { name: 'eliTime' },
             { name: 'clientAdd' },
             { name: 'spareTel1' },
             { name: 'spareTel2' },
             { name: 'clientStatus' },
             { name: 'signPossible' },
             { name: 'assignDate' },
             { name: 'assignTime' },
             { name: 'remark' },
             { name: 'clientSource' },
             { name: 'emp_name' },
             { name: 'khxinx'},
             { name: 'proCity1'},
             { name: 'city1'},
             { name: 'province1'}
        ]
    });
     //-----刷新 淘汰资源列表---------------
   	var _gridEliminateLoadAction = new Ext.Action({
        handler: function(){
           _gridEliminate.load({
               params:{
                   start:0, 
                   limit:20
               }
           });
       	}
   	});
	//---------------资源跟踪列表-----
    var _gzgridStore = new Ext.data.JsonStore({
        url: path+'/ResourcesTrack/findTrackrecorAction.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions:'',
        	tcid: track_id
        },
        fields: [
            { name: 'gzid' },
            { name: 'gzresourcescontent' },
            { name: 'gzresourcestime' },
            { name: 'gzresourcespeople_name' },
            { name: 'gztypes' },
            { name: 'gzcalltime' }
        ]
    });
	//------------------------查看淘汰客户的跟踪信息
    var formTrackrecors = new Ext.grid.GridPanel({
        id: 'formTrackrecors',
        store: _gzgridStore,
        sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {
                hidden:true,
            	header: 'ID',
                sortable: false,
                dataIndex: 'gzid'
            },
            {
            	header: '跟踪内容',
                sortable: true,
                width:500,
                dataIndex: 'gzresourcescontent'
            },
           	{
            	header: '跟踪时间',
            	width:120,
                sortable: true,
                dataIndex: 'gzresourcestime'
            },
            {
            	header: '管理人',
            	width:80,
                sortable: true,
                dataIndex: 'gzresourcespeople_name'
            },
            {
            	header: '客户预约',
                sortable: true,
                width:80,
                dataIndex: 'gztypes'
            },
            {
            	header: '上门时间',
                sortable: true,
                width:100,
                dataIndex: 'gzcalltime'
            }
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: _gzgridStore ,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
	var eliTrackReturn = new Ext.Action({
        text: '返回',
        iconCls: 'returns', 
        handler: function(){
           eliTrackWindow.hide();
        }
    });
    var eliTrackWindow = new Ext.Window({
        width: 950,
        height: 420,
        modal: true,
        layout: 'fit',
        plain: true,
        title:'客户跟踪详细信息',
        bodyStyle: 'padding:5px;',
        buttonAlign: 'right',
        items: formTrackrecors,
        closeAction: 'hide',
        buttons: [
            eliTrackReturn
        ]
    });
	var eliAddWindow = new Ext.Window({
        width: 420,
        height: 180,
        title:'添加资源',
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: [
        	{
        		xtype: 'form',
        		id: 'eliAddForm',
        		bodyStyle: 'padding:5px',
		        frame: true,
		        labelWidth: 70,
		        layout: 'form',
		        items:[
					{
	                	fieldLabel: '添加备注',
	                  	xtype:'textarea',
	                  	anchor: '95%',
	                  	id:'eliAddRemark'
		            }	        
		        ]
        	}
        ],
        closeAction: 'hide',
        buttons: [
             {
             	text: '确定',
		        iconCls: 'saves',
		        handler: function(){
		        	var rec = myGridEliminate.getSelectionModel().getSelected();
		        	var addRemark = Ext.getCmp('eliAddRemark').getValue();
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
		                   		eliAddWindow.hide();
		                   		_gridEliminate.reload({callback: eliGridUpdate});
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
             },
             {
               	text: '返回',
		        iconCls: 'returns',
		        handler: function(){
		           eliAddWindow.hide();
	        	}
           	 }
        ]
    });
	var addEliClient = new Ext.Action({
        text: '认领资源',
        iconCls: 'btn_add',
        disabled: true,
        handler: function(){
            Ext.getCmp('eliAddForm').getForm().reset();
            eliAddWindow.show();
        }
    });
    var formup = new Ext.form.FormPanel({
        bodyStyle: 'padding:5px',
        labelAlign: 'right',
        labelWidth: 80,
        frame: true,
        items: [{
            layout: 'column',
            items: [
                {
                    columnWidth: 0.5,
                    layout: 'form',
                    labelWidth: 80,
                    defaultType: 'textfield',
                    defaults: {
                        anchor: '95%'
                    },
                    items: [
                        {
                            fieldLabel: '创建日期',
                            disabled: true,
                            name:'assignDate'
                        },
                        {
                            fieldLabel: '客户联系方式',
                            name: 'contactTel',
                            disabled: true
                        },
                        {
                        	fieldLabel: '客户来源',
                            name: 'clientSource',
                            disabled: true
                        },
                        {
                            fieldLabel: '备用电话1',
                            disabled: true,
                            name: 'spareTel1'
                        }
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
                            fieldLabel: '客户名称',
                            disabled: true,
                            name: 'clientName'
                        },
                        {
                            fieldLabel: '贷款金额(万)',
                            disabled: true,
                            name: 'loanAmount'
                        },
                        {
                            fieldLabel: '客户地址',
                            disabled: true,
                            name: 'clientAdd'
                        },
                        {
                            fieldLabel: '备用电话2',
                           	disabled: true,
                            name: 'spareTel2'
                        }
                         
                    ]
                }
            ]
        },
        {
       		xtype: 'textarea',
            fieldLabel: '备注',
            anchor: '98%',
            disabled: true,
            name: 'remark'
        }
       ]
    });
    
    var clientInfoWindow = new Ext.Window({
        width: 650,
        height: 300,
        modal: true,
		title: '详细客户信息',
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'right',
        items: formup,
        closeAction: 'hide',
        buttons: [
            {
            	text: '返回',
		        iconCls: 'returns', 
		        handler: function(){
		            clientInfoWindow.hide();
		        }
            }
        ]
    });
    //----------淘汰资源列表  --修改资源-----查看客户详细信息
    var viewClientInfo = new Ext.Action({
        text: '查看客户详细信息',
        iconCls: 'vcard',
        disabled: true,
       	handler: function(){
         	var record = myGridEliminate.getSelectionModel().getSelected();
            if(record !== null){
            	formup.getForm().reset();
                clientInfoWindow.show();
                formup.getForm().loadRecord(record);
            }
        }
    });
    //----------------------刷新 淘汰资源列表的点击查看跟踪记录
  	var  eliTrackLoad = new Ext.Action({
         handler: function(){
         	_gzgridStore.setBaseParam('tcid', track_id);
            _gzgridStore.load({
                params:{
                    start:0, 
                    limit:20
                }
            });
        }
    });
    //----------淘汰资源查看跟踪记录
    var trackrecordAction = new Ext.Action({
	    text:'查看跟踪记录',
		iconCls: 'check',
	 	disabled: true,
	   	handler: function(){
		    if (myGridEliminate.getSelectionModel().hasSelection()) {
            	 var record = myGridEliminate.getSelectionModel().getSelected();
            	 if(record !== null){
               	   	eliTrackWindow.show();
               	   	eliTrackLoad.execute();
            	 }
           	 }
        }
    });
    var cityEliCombox = new Ext.form.ComboBox({
	    id : 'cityEliCombox',
	    store : cityData,
	    width: 90,
	    editable:false,
	    displayField : 'value',
	    valueField : 'key',
	    name: 'city',
	    mode : 'local',
	    triggerAction : 'all',
	    emptyText : '请选择...',// 默认值   selectOnFocus : true,
	    listeners: {
	    	'expand': function(){
	      		var val = provinceEliCombox.getValue();
	      		if(val !== '' && val !== null){
	      			cityData.filterBy(function(rec, id){
	      				return rec.get('pro') == val;
		      		});
	      		}
	      	}
      	}
   });
   var provinceEliCombox = new Ext.form.ComboBox({
       id : 'provinceEliCombox',
       store : provinceData,
       width: 70,
       displayField : 'value',
	   editable:false,
	   valueField : 'key',
	   name: 'province',
	   mode : 'local',
	   triggerAction : 'all',
	   emptyText : '请选择...',// 默认值   selectOnFocus : true,
	   listeners: {
	   	'select': function(){
	   		cityEliCombox.reset();
	   	}
	   }
    });
    var departSelComboBox = new Ext.form.ComboBox({
	      id: 'departSelComboBox',
		  allowBlank: true,
	      emptyText : '请选择部门...',
	      width: 120,
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
	      		employeeSelComboBOx.reset();
	      		employeeSelData.proxy = new Ext.data.HttpProxy({
	      			url: path+'/client/loadEmployees.do?eid=' + this.getValue()
	      		});
	      		employeeSelData.load();
	      	}
	      }
  	});
  	var employeeSelComboBOx = new Ext.form.ComboBox({
   	   	id: 'employeeSelComboBOx',
      	allowBlank: true,
       	width: 120,
        typeAhead: true,
       	editable: false,
       	triggerAction: 'all',
       	lazyRender: true,
	   	mode: 'remote',
       	store: employeeData,
       	emptyText : '请选择员工...',
       	valueField: 'eId',
       	displayField: 'eName',
       	listeners: {
   			'expand': function(){
	       		var _dept = departSelComboBox.getValue();
	       		if(_dept === '' | _dept === null){
	       			this.reset();
		      		employeeSelData.proxy = new Ext.data.HttpProxy({
		      			url: path+'/client/loadEmployee.do'
		      		});
		      		employeeSelData.load();
	       		}
	       	}
       	}
	});
	var oppTypeSelCombox = new Ext.form.ComboBox({
	      id : 'oppTypeSelCombox',
	      store : oppTypeData,
	      width: 90,
	      editable: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...',
	      hiddenName : 'oppType'  
   	});
   	var signPossibleSelCombox = new Ext.form.ComboBox({
	      id : 'signPossibleSelCombox',
	      store : signPossibleData,
	      width: 90,
	      editable: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...', 
	      hiddenName : 'signPossible'  
    });
	var createDateCombox = new Ext.form.ComboBox({
	      id : 'createDateCombox',
	      store : createDateData,
	      width: 80,
	      editable: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...',
	      listeners:{
	      	'select': function(){
	      		Ext.getCmp('createStart').setDisabled(true);
	      		Ext.getCmp('createEnd').setDisabled(true);
	      	}
	      }
    });
    var searchButton = {
    	xtype:'button',
		iconCls: 'check',
		text:'查 询',
		handler:function(){
			Ext.getCmp('createStart').setDisabled(false);
	      	Ext.getCmp('createEnd').setDisabled(false);
			var _createStart = Ext.getCmp('createStart');
			var _createEnd = Ext.getCmp('createEnd');
			var _lastTimeStart = Ext.getCmp('lastTimeStart');
			var _lastTimeEnd = Ext.getCmp('lastTimeEnd');
			_createStart = transferDate(_createStart);
			_createEnd = transferDate(_createEnd);
			var createFlag = judgeTime(Ext.getCmp('createStart'), Ext.getCmp('createEnd'));
			var _createDate = Ext.getCmp('createDateCombox').getValue();
			var _oppType = oppTypeSelCombox.getValue();
			var _signPos = signPossibleSelCombox.getValue();
			var _startLoan = Ext.getCmp('startLoan').getValue();
			var _endLoan = Ext.getCmp('endLoan').getValue();
			var _dept = departSelComboBox.getValue();
			var _emp = employeeSelComboBOx.getValue();
			var _createStartttzytime = Ext.getCmp('createStartttzytime').getValue();
			var _createEndttzytime = Ext.getCmp('createEndttzytime').getValue();
			var nameOrNum = Ext.getCmp('nameOrNum').getValue();
			var _provinces1 = Ext.getCmp('provinceEliCombox').getValue();//--------省份
			var _citys1 = Ext.getCmp('cityEliCombox').getValue();//--------城市
			
			if(_createStartttzytime!=""){
				_createStartttzytime = Ext.getCmp('createStartttzytime').getValue().format('Y-m-d');
			}
			if(_createEndttzytime!=""){
				_createEndttzytime = Ext.getCmp('createEndttzytime').getValue().format('Y-m-d');
			}
			if(createFlag){
				if((_startLoan != '' & _endLoan == '') | (_endLoan != '' & _startLoan == '')){
					Ext.MessageBox.alert('提示', '贷款金额条件不完整!');
				}else{
					if(_startLoan > _endLoan){
						Ext.MessageBox.alert('提示', '结束金额必须大于开始金额!');
					}else{
						Ext.apply(_gridEliminate.baseParams,{
							conditions:'{_createStart:"'+_createStart+'",_createEnd:"'+_createEnd+'",_createDate:"'+_createDate+'",_dept:"'+
								_dept+'",_emp:"'+_emp+'",_oppType:"'+_oppType+'",_signPos:"'+_signPos+'",_startLoan:"'+_startLoan+'",_endLoan:"'+
									_endLoan+'",_createStartttzytime:"'+_createStartttzytime+'",_createEndttzytime:"'+_createEndttzytime+'",nameOrNum:"'+nameOrNum+'",_provinces1:"'+_provinces1+'",_citys1:"'+_citys1+'"}'
						});
						_gridEliminate.load({
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
				}
			}else{
				Ext.MessageBox.alert('提示', '结束时间必须大于开始时间!');
			}
			Ext.getCmp('oppTypeSelCombox').setValue('');
			Ext.getCmp('signPossibleSelCombox').setValue('');
			Ext.getCmp('startLoan').setValue('');
			Ext.getCmp('endLoan').setValue('');
			Ext.getCmp('createStart').setValue('');
			Ext.getCmp('createEnd').setValue('');
			createDateCombox.setValue('');
			departSelComboBox.setValue('');
			employeeSelComboBOx.setValue('');
			Ext.getCmp('createStartttzytime').setValue('');
			Ext.getCmp('createEndttzytime').setValue('');
			Ext.getCmp('nameOrNum').setValue('');
			provinceEliCombox.setValue('');
		    cityEliCombox.setValue('');
		}
    }
    var ones=new Ext.Toolbar({
    	id:'ones',
		items:[
			'创建日期：',
			createDateCombox,
			'创建起止日期：',
		 	{
	     	 	xtype:'datefield',
	     	 	width:90,
	     	 	format:'Y-m-d',
	     	 	editable: false,
	     	 	id:'createStart'
     	 	},
     	 	'至',
     	 	{
	     	 	xtype:'datefield',
	     	 	width:90,
	     	 	format:'Y-m-d',
	     	 	editable: false,
	     	 	id:'createEnd'
     	 	},
	     	'淘汰起止日期：',
			{
	     	 	xtype:'datefield',
	     	 	width:90,
	     	 	format:'Y-m-d',
	     	 	editable: false,
	     	 	id:'createStartttzytime'
	     	},
	     	'至',
	     	{
	     	 	xtype:'datefield',
	     	 	width:90,
	     	 	format:'Y-m-d',
	     	 	editable: false,
	     	 	id:'createEndttzytime'
	     	},
     	  	'省份：',
			provinceEliCombox,
			'城市：',
			cityEliCombox,
	     	searchButton	     	 
		] 
	});
    var oneTbar=new Ext.Toolbar({
    	id:'two',
		items:[
			'客户名字：',
			{
				xtype: 'textfield',
				width: 100,
				id: 'nameOrNum'
			},
			'&nbsp;&nbsp;部门：',
			departSelComboBox,
			'管理人：',
			employeeSelComboBOx,
	     	'商机类型：',
			oppTypeSelCombox,
	     	'签单可能性：',
			signPossibleSelCombox,
			'贷款金额：',
			{
				xtype: 'textfield',
				width: 80,
				id: 'startLoan',
				regex:/^[0-9].*$/
			},
			'至',
			{
				xtype: 'textfield',
				width: 80,
				id: 'endLoan',
				regex:/^[0-9].*$/
			}
		] 
	}); 
    //------------------淘汰资源列表----------
    var myGridEliminate = new Ext.grid.GridPanel({
        id: 'myGridEliminate',
        store: _gridEliminate,
        sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {
                hidden:true,
            	header: '编号',
                sortable: false,
                dataIndex: 'id'
            },
            {
            	header: '客户信息',
            	width:160,
                sortable: true,
                dataIndex: 'khxinx'
            },
            {
            	header: '省市',
            	sortable: true,
            	dataIndex: 'proCity1',
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
            	header: '客户名字',
            	hidden:true,
                sortable: true,
                dataIndex: 'clientName'
            },
            {
            	header: '客户联系方式',
                sortable: true,
                width: 100,
                dataIndex: 'contactTel'
            },
            {
            	header: '备用电话1',
            	width: 100,
                sortable: true,
                dataIndex: 'spareTel1'
            },
            {
            	header: '客户状态',
               	sortable: true,
               	width: 80,
                dataIndex: 'clientStatus',
                renderer:function(v){
                	if(v == '1'){
                		return '已签单';
                	}
                	if(v == '2'){
                		return '未签单';
                	}
                	if(v == '3'){
                		return '淘汰';
                	}
                	if(v == '4'){
                		return '退单';
                	}
                }
            },
            {
            	header: '客户地址',
            	width: 100,
                sortable: true,
                dataIndex: 'clientAdd'
            },
            {
            	header: '管理人',
                sortable: true,
                dataIndex: 'emp_name'
            },
            {
            	header: '淘汰日期',
            	width: 120,
                sortable: true,
                dataIndex: 'eliTime'
            },
       	  	{
            	header: '客户来源',
            	width: 80,
                sortable: true,
                dataIndex: 'clientSource'
          	},
            {
            	header: '签单可能性',
                sortable: true,
                width: 80,
                dataIndex: 'signPossible'
            },
            {
            	header: '备注',
                sortable: true,
                dataIndex: 'remark',
                renderer:function(value, metadata, record, rowIndex, columnIndex, store){
                	metadata.attr = 'ext:qtip="' + value +'"';  
                	return value;
                }
     		 }
             
        ],
        tbar: [
	       addEliClient,
	       viewClientInfo,
	       trackrecordAction
        ],
        listeners : {
		 'render': function(){
        		oneTbar.render(this.tbar);
        		ones.render(this.tbar);
        	}  
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: _gridEliminate,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    myGridEliminate.getSelectionModel().addListener(
        'selectionchange', 
        eliGridUpdate
    );
     //-----------------提出资源
    function eliGridUpdate(){
    	var record = myGridEliminate.getSelectionModel().getSelected(); 
        if(record != null){
   	 		addEliClient.enable();
       	 	viewClientInfo.enable();
          	trackrecordAction.enable();
            track_id = record.get('id');//----选中淘汰资源的ID
        }else{
       	 	addEliClient.disable();
       		viewClientInfo.disable();
   	 	 	trackrecordAction.disable();
        }
  	};
    //---------------淘汰资源列表  弹出 层
	var formWindowEliminate = new Ext.Window({
        width: 1200,
        height: 530,
        modal: true,
		title:'查看淘汰资源信息',
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'right',
        items: myGridEliminate,
        closeAction: 'hide',
        buttons: [
        	{
        		text: '返回',
		        iconCls: 'returns', 
		        handler: function(){
		           formWindowEliminate.hide();
		        }
        	}
      	]
    });