<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript" src="resources/js/all.js"></script>
<script type="text/javascript" src="resources/js/index.js"></script>
<script type="text/javascript">
	var roleCode = '${userSession.role.roleCode}';
	function scode(id){
		Ext.Ajax.request({
          	url: '<%=path%>/index/findWarnClient.do?id='+id,
            success: function(aResponse, aOptions){
            	jump_from_flag = 6;//分配提醒跳转
				mainPanels.hide();
				if(clientPanel == ''){
					judgeJs('jump_clientJs', 'resources/welcome/jumpClient.js');
					Ext.getCmp('btnAlert').add(clientPanel);
					Ext.getCmp('btnAlert').doLayout();
					var btn = Ext.getCmp('returnPage');
					returnEvent(btn);
				}else{
					clientPanel.show();
				}
				clientStore.setBaseParam('_flag', 'have');
				clientLoadAction.execute();
			}
       	});
	}
	function returnEvent(btn){
		if(!btn.hasListener('click')){
			btn.addListener('click', function(){
				seeFlag = 0;
				clientPanel.hide();
				if(jump_from_flag == 1 | jump_from_flag == 2 | jump_from_flag == 6){
	            	todayWorkPlanStore.reload();
	            	todayAddStore.reload();
	            	signedStore.reload();
				}
				if(jump_from_flag != 6){
					for(var i = 0; i < 5; i++){
						var obj = searchTbar.getComponent(0);
						if(obj.id == 'cusTelOrName'){
							break;
						}else{
							searchTbar.remove(obj);
						}
					}
					searchTbar.insertButton(0, seeBtns);
					searchTbar.doLayout();
				}
				clientStore.removeAll();
				mainPanels.show();
			});
		}
	}
</script>
<script type="text/javascript">  
   var qfpwd=1; 
   function resetpassword(){
		Ext.getCmp('pwd').setValue('');
		Ext.getCmp('mailbox').setValue('');
		Ext.getCmp('idCardNum').setValue('');
		Ext.getCmp('pwd2').setValue('');
		Ext.getCmp('password').setValue('');
		qfpwd=2;
		Ext.getCmp('loginId').setValue('${userSession.loginId}');
		Ext.getCmp('form1Window').show();
	}
Ext.onReady(function() {
	Ext.QuickTips.init();// 浮动信息提示
    Ext.BLANK_IMAGE_URL = 'resources/images/default/s.gif';// 替换图片文件地址为本地
	var rootNodeList=[];
	var sdf=${upPwd};
	var sd='count';
	if(sdf==3){
    	sd='month';
    }
   
    var updateForm = new Ext.form.FormPanel({
        id: 'updateForm',
        bodyStyle: 'padding:5px',
        labelAlign:'right',
        frame: true,
        labelWidth: 70,
        defaultType: 'textfield',
        layout: 'form',
        defaults:{
       		anchor: '95%'
        },
        items: [
            {
             inputType:'hidden',
	         id: 'loginId'
            },
            {
	          fieldLabel: '*原始密码',
	          allowBlank: false,
	          inputType : 'password',
	          id: 'pwd'
	       },
	       {
       		  fieldLabel: '*身份证号',
	          allowBlank: false,
	          id: 'idCardNum'
	       },
	       {
	          fieldLabel: '*邮箱',
	          allowBlank: false,
	          id: 'mailbox'
	       },
	        {
	          fieldLabel: '*修改密码',
	          allowBlank: false,
	          inputType : 'password',
	          regex:/^[a-zA-Z0-9]{6,8}$/,
	          id: 'pwd2'
	       },
             {
             fieldLabel: '*确认密码',
             inputType : 'password',
	         allowBlank: false,
	         regex:/^[a-zA-Z0-9]{6,8}$/,
	         id: 'password'
	         }
        ]
    });

	 var form1SaveAction = new Ext.Action({
	        text: '保存',
	        iconCls: 'saves',
	        handler: function(){
	            if(updateForm.getForm().isValid()){
	            var _pwd = Ext.getCmp('pwd').getValue();
	            var _pwd2 = Ext.getCmp('pwd2').getValue();
	            var _pwd3 = Ext.getCmp('password').getValue();
	            var _idCardNum = Ext.getCmp('idCardNum').getValue();
	            var _mailbox = Ext.getCmp('mailbox').getValue();
	            if(_pwd2 != _pwd3){
	            	Ext.MessageBox.alert('提示', '输入的新密码不一致，请重新输入！'); 
	            	return false;
	            }
	                updateForm.getForm().submit({
	                    url: '<%=path%>/updataPwd.do',
	                    params: {
	                    _pwd: _pwd,
	                    _pwd2:_pwd2,
	                    _mailbox:_mailbox,
	                    _idCardNum:_idCardNum,
	                    _qfpwd: qfpwd,
	                     sd:sd
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在处理数据...',
	                    timeout: 10,
	                    success: function(aForm, aAction){
	                        form1Window.hide();
	                    	Ext.MessageBox.alert('提示', aAction.result.msg); 
	                    },
	                    failure: function(aForm, aAction) {
	                       Ext.MessageBox.alert('提示', aAction.result.msg);                           
	                    }
	                });
	            }
	        }
	    });
       var form1Window = new Ext.Window({
	       id:'form1Window',
            title:'修改密码',
	        width: 350,
	        height: 250,
	        modal: true,
	        layout: 'fit',
	        plain: true,
	        bodyStyle: 'padding:5px;',
	        buttonAlign: 'center',
	        items: updateForm,
	        closeAction: 'hide',
	        buttons: [
	                form1SaveAction
	            ]
	    });

	var rootNodeRequest = function(url) {
	    var conn = GetHttpRequest();
	    conn.open("POST", url, false);
	    conn.send(null);
	    if (conn.responseText != '') {
	        return Ext.decode(conn.responseText);
	    } else {
	        return null;
	    }
	};
	
	var rootNode = rootNodeRequest('<%=path%>/menu/rootNode.do');
	
	var childNodeRequest = function(url) {
	    var conn = GetHttpRequest();
	    conn.open("POST", url, false);
	    conn.send(null);
	    if (conn.responseText != '') {
	        return Ext.decode(conn.responseText);
	    } else {
	        return null;
	    }
	};
	for(var i=0;i<rootNode.length;i++){
	   var rootNodeStore='';
		   rootNodeStore= new Ext.tree.TreePanel({
	           id:rootNode[i].id,
	           title:rootNode[i].title,
	           iconCls:rootNode[i].iconCls,
	           loader: new Ext.tree.TreeLoader(),
	           width:100,
	           rootVisible:false,
	           lines:false,
	           autoScroll:true,
	           root:childNodeRequest('<%=path%>/menu/childNode.do?id='+rootNode[i].id),
	           listeners: {
		    		click: function(n) {
		    			createTabpanelItems(n.attributes.url, n.text,n.id,n.attributes.iconCls);
					}
			  }
      });
      rootNodeList.push(rootNodeStore);
	}
	
	var northTitle = new Ext.Panel({
		region: 'north',
        margins:'8 8 8 8',
        border: false,
        height:50,
		bodyStyle:'font-size:12px;background-color:#B7DBFD;background-image:url(/CRM/image/top_bg6.jpg);background-repeat:repeat-x;',
		html: '<div id="user">用户姓名：<span style="color:red">${userSession.loginId}</span>&nbsp;&nbsp;<a class="title_a" href="javascript:resetpassword();">[密码修改]</a>&nbsp;&nbsp;<a class="title_a" href="<%=path%>/to_login">[退出登陆]</a></div>'
	});
	
	var tree = new Ext.Panel({
        id:'tree',
		xtype:'panel',
        region: 'west',
		iconCls: 'menu_panel',
        width: 130,
        collapsible: true,
        margins:'0 0 0 0',
    	animCollapse:false,
    	animFloat:false,
        split:true,
		layout:'accordion',
		items:[
			rootNodeList
		]
	});
	if(roleCode == '201202'){
		resouComboBox.hide();
		clientDepComboBox.hide();
		signedDepComboBox.hide();
		refundDepComboBox.hide();
	}
	if(roleCode == '201203'){
		resouComboBox.hide();
		clientDepComboBox.hide();
		signedDepComboBox.hide();
		refundDepComboBox.hide();
		refundEmpComboBOx.hide();
		signedEmpComboBOx.hide();
		clientEmpComboBOx.hide();
		resouEmpComboBOx.hide();
	}
	//------------今日新增商机
    todayAddStore = new Ext.data.JsonStore({
        url: '<%=path%>/index/todayAddClient.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions : ''
        },
        fields: [
            { name: 'id' },{ name: 'clientName' },{ name: 'loanCount' },
            { name: 'emp_departName' },{ name: 'oppType' },{ name: 'userName' },{ name: 'contactTel' },
	        { name: 'signPossible' },{ name: 'assignDate'},{ name: 'clientSourse'},{ name: 'remark'},
	        { name: 'jrproCity'},{ name: 'editTime'}
        ]
    });
    //------今日新增商机刷新----------
    var todayAddLoadAction = new Ext.Action({
        handler: function(){
            todayWorkPlanLoadAction.execute();
            todayAddStore.load({
            	params:{
	                 start:0, 
	                 limit:20
             	},
             	callback: function(){
             		SignedLoadAction.execute();
             	}
            })
        }
    });
    var clientLoad = new Ext.Action({
		  text:'查询',
		  scale: 'small',
		  iconCls: 'check',
		  handler:function(){
	      	var clientDep = Ext.getCmp('clientDepComboBox').getValue();
	      	var clientEmp = Ext.getCmp('clientEmpComboBOx').getValue();
	      	var opp = Ext.getCmp('oppTypeSelCombox1').getValue();
	      	var newTime = Ext.getCmp('newTime').getValue();
	      	var client_source = Ext.getCmp('clientSelComboBOx3').getValue();
	      	var _provinces=Ext.getCmp('provinceCombox1').getValue();//--------省份
		    var _citys=Ext.getCmp('cityCombox1').getValue();//--------城市
	      	if(newTime!=''){
	      	   newTime=newTime.format('Y-m-d');
	      	}
	   		checks = '{clientDep:"'+clientDep+'",clientEmp:"'+clientEmp+'",newTime:"'+newTime+'",opp:"'+opp+'",client_source:"'+client_source+'",_provinces:"'+_provinces+'",_citys:"'+_citys+'"}';
	      	Ext.apply(todayAddStore.baseParams,{
		   		conditions:'{clientDep:"'+clientDep+'",clientEmp:"'+clientEmp+'",newTime:"'+newTime+'",opp:"'+opp+'",client_source:"'+client_source+'",_provinces:"'+_provinces+'",_citys:"'+_citys+'"}'
		    });
	      	todayAddStore.load({
	      	   params:{
	      	     start:0,
	      	     limit:20
	      	   }
	      	});  
		  }
	})
	var todayBar = new Ext.Toolbar({
    	items:[
			clientDepComboBox,
			'&nbsp',
			clientEmpComboBOx
    	]
    });
    var jumpFlag = 0;//跳转标识（第一次为动态加载js）
	var todayAddGrid = new Ext.grid.GridPanel({	
        id: 'todayAddGrid',
        store: todayAddStore,
       	sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        height: 400,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {header: '创建日期', width: 110, sortable:true, dataIndex:'assignDate'},
            {header: '客户信息', width: 90, sortable:true, dataIndex:'clientName',
                renderer:function(value,data,row){
	            	var edit = row.data.editTime;
	            	if(edit){
	            		return row.data.clientName;
	            	}else{
	            		return '<font style="color:red;">'+row.data.clientName+'</font>';
	            	}
	             }
            },
            {header: '省市', width:90, sortable: true, dataIndex: 'jrproCity',
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
            {header: '客户电话', sortable:true, width: 90, dataIndex:'contactTel' },
            {header:'签单可能性', hidden: true, sortable:true, width: 60, dataIndex:'signPossible'},
            {header:'客户来源', width: 60, sortable: true, dataIndex:'clientSourse'},
            {header:'管理人', width: 75, sortable:true, dataIndex:'userName'},
            {header:'备注', width:100, sortable:true, dataIndex:'remark'}
        ],
        tbar:[
			{
			  xtype:'datefield',
			  width:80,
			  format:'Y-m-d',
			  emptyText: '创建时间...',
			  id:'newTime'
			},
			'&nbsp',
			oppTypeSelCombox1,
			'&nbsp',
			clientSelComboBOx3,
				'&nbsp',
    		provinceCombox1,
    		'&nbsp',
			cityCombox1,
			clientLoad,
			reset2
		],
        listeners : {
		   'rowdblclick' : function(){
				jump_from_flag = 1;//今日新增跳转
			    var record = todayAddGrid.getSelectionModel().getSelected(); 
			   	client_id = record.id;
				mainPanels.hide();
				if(clientPanel == ''){
					judgeJs('jump_clientJs', 'resources/welcome/jumpClient.js');
					Ext.getCmp('btnAlert').add(clientPanel);
					Ext.getCmp('btnAlert').doLayout();
					var btn = Ext.getCmp('returnPage');
					returnEvent(btn);
				}else{
					clientPanel.show();
				}
				clientStore.setBaseParam('_cpid', client_id);
				clientStore.setBaseParam('_flag', '');
				clientLoadAction.execute();
		 	},
		 	'render': function(){
		 		todayBar.render(this.tbar);
		 	}
		},
        bbar: new Ext.PagingToolbar({
            id:'todayAddPage',
            pageSize: 20,
            store: todayAddStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    }); 	
    //--------今日工作计划
    todayWorkPlanStore = new Ext.data.JsonStore({
        url: '<%=path%>/index/findTodayWorkPlan.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'rtid',
        autoDestroy: true,
        baseParams: {
        	conditions: ''
        },
        fields: [
            { name: 'id' },{ name: 'rtid' },{ name: 'clientName' },
	        { name: 'resouRcescontent' },{ name: 'workPlan' },
	        { name: 'resourcespeople' },{ name: 'telephone' },
	        { name: 'plantime'}, { name: 'editTime'}
        ]
    });
    //-------------------今日需完成商机
    var todayWorkPlanLoadAction = new Ext.Action({
        handler: function(){
			todayWorkPlanStore.load({
				params:{
	                 start:0, 
	                 limit:20
             	},
             	callback:function(){
             		refundLoadAction.execute();
             	}
			});     
        }
    });  
    var resourcesAction = new Ext.Action({
		   text:'查询',
		   iconCls: 'check',
		   handler:function(){
			   var resoudep = Ext.getCmp('resouComboBox').getValue();
			   var plan = Ext.getCmp('_plantime').getValue();
			   var resouemp = Ext.getCmp('resouEmpComboBOx').getValue();
			   var opp = Ext.getCmp('oppTypeSelCombox').getValue();
			   var client_source = Ext.getCmp('clientSelComboBOx2').getValue();
			   if(plan != ''){
			   	plan = plan.format('Y-m-d');
			   }
			   checks1 =  '{resoudep:"'+resoudep+'",resouemp:"'+resouemp+'",opp:"'+opp+'",plan:"'+plan+'",client_source:"'+client_source+'"}';
			   Ext.apply(todayWorkPlanStore.baseParams,{
				conditions:'{resoudep:"'+resoudep+'",resouemp:"'+resouemp+'",opp:"'+opp+'",plan:"'+plan+'",client_source:"'+client_source+'"}'
			   });
			   todayWorkPlanStore.load({
			      params:{
			        start:0,
			        limit:20
			      }
			   });
		   }
  	});
  	var now_time = new Date();
    //------------今日工作计划(需完成商机)
    var todayWorkPlanGrid = new Ext.grid.GridPanel({
        id: 'todayWorkPlanGrid',
        store: todayWorkPlanStore,
       	sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        height: 260,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {header: '计划时间', sortable: true, width:120, dataIndex: 'plantime'},
            {header: '工作计划', width:150,
                renderer:function(value, metadata, record, rowIndex, columnIndex, store){
                	metadata.attr = 'ext:qtip="' + value +'"';  
                	var y = now_time.getFullYear();
                	var m = now_time.getMonth()+1;
                	if(m<10)m='0'+m;
                	var d = now_time.getDate();
                	if(d<10)d='0'+d;
                	var edit = record.data.editTime;
                	if(edit == (y+'-'+m+'-'+d)){
                		return value;
                	}else{
	                	return '<font style="color:red;">'+value+'</font>'
                	}
                },
            	sortable:true, dataIndex:'workPlan'
            },
            {header:'客户信息', width:100, sortable:true,dataIndex:'clientName'},
            {header:'电话号码', sortable:true, width: 90, dataIndex:'telephone'},
            {header:'管理人', sortable:true, dataIndex:'resourcespeople'}
        ],
        tbar:[
	        resouComboBox,
	        '&nbsp',
	        resouEmpComboBOx,
	        '&nbsp',
	        oppTypeSelCombox,
	        '&nbsp',
	        {
	        	xtype: 'datefield',
	        	width:90,
	        	id: '_plantime',
	        	format: 'Y-m-d',
	        	emptyText: '计划时间'
	        },
	        '&nbsp',
	        clientSelComboBOx2,
	        resourcesAction,
	        reset1
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            id: 'workPageTool',
            store: todayWorkPlanStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    }); 
    //双击跳转到工作计划的详细页面
    todayWorkPlanGrid.addListener('rowdblclick', rowdblclickFn); 
	function rowdblclickFn(todayWorkPlanGrid,rowindex,e){ 
		if(todayWorkPlanGrid.getSelectionModel().hasSelection()){
            var record = todayWorkPlanGrid.getSelectionModel().getSelected();
            jump_from_flag = 2;//今日工作计划跳转
		   	client_id = record.get('id');
		   	plan_id = record.get('rtid');
		   	plan_time = record.get('plantime');
			mainPanels.hide();
			if(clientPanel == ''){
				judgeJs('jump_clientJs', 'resources/welcome/jumpClient.js');
				Ext.getCmp('btnAlert').add(clientPanel);
				Ext.getCmp('btnAlert').doLayout();
				var btn = Ext.getCmp('returnPage');
				returnEvent(btn);
			}else{
				clientPanel.show();
			}
			clientStore.setBaseParam('_cpid', client_id);
			clientStore.setBaseParam('_flag', '');
			clientLoadAction.execute();
		} 
	}
    //--------已签单商机-------------
    signedStore = new Ext.data.JsonStore({
        url: '<%=path%>/index/signedClient.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions: ''
        },
        fields: [
            { name: 'id' },{ name: 'loanCount' },{ name: 'userName' },
            { name: 'clientName' },{ name: 'user_depaName' },{ name: 'contactTel' },
            { name: 'oppType' },{ name: 'signingtime'},{ name: 'yqdproCity'}
        ]
    });
    //------------签单商机---------- 
    var SignedLoadAction = new Ext.Action({
        handler: function(){
            loadData(signedStore);
        }
    }); 
    var signedAction = new Ext.Action({
	       text:'查询',
		   scale: 'small',
		   iconCls: 'check',
		   handler:function(){
		   	   var signedDep = Ext.getCmp('signedDepComboBox').getValue();
			   var signedEmp = Ext.getCmp('signedEmpComboBOx').getValue();
			   var opp = Ext.getCmp('oppTypeSelCombox2').getValue();
			   var qdtime = Ext.getCmp('qdtime').getValue();
			   var client_source = Ext.getCmp('clientSelComboBOx4').getValue();
			   var _provinces3=Ext.getCmp('provinceCombox3').getValue();//--------省份
			   var _citys3=Ext.getCmp('cityCombox3').getValue();//--------城市
			   if(qdtime!=''){
			   	qdtime=qdtime.format('Y-m-d');
			   }
			 checks2 = '{signedDep:"'+signedDep+'",signedEmp:"'+signedEmp+'",qdtime:"'+qdtime+'",opp:"'+opp+'",client_source:"'+client_source+'",_provinces3:"'+_provinces3+'",_citys3:"'+_citys3+'"}';
			 Ext.apply(signedStore.baseParams,{
		   		conditions:'{signedDep:"'+signedDep+'",signedEmp:"'+signedEmp+'",qdtime:"'+qdtime+'",opp:"'+opp+'",client_source:"'+client_source+'",_provinces3:"'+_provinces3+'",_citys3:"'+_citys3+'"}'
		     });
			 signedStore.load({
			      params:{
			        start:0, limit:20, signedDep:signedDep, signedEmp:signedEmp,
			        qdtime:qdtime, opp: opp, _provinces3:_provinces3, _citys3:_citys3
			      }
			  });
		   }
  	});
  	var todayBar2 = new Ext.Toolbar({
    	items:[
	        signedDepComboBox,'&nbsp',signedEmpComboBOx
    	]
    });
    //--------已签单商机---------
    var signedGrid = new Ext.grid.GridPanel({
        id: 'signedGrid',
        store: signedStore,
        region: 'center',
       	sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        height: 260,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {header: '签单时间',sortable:true,width:140,dataIndex:'signingtime'},
            {header: '客户信息',width:150,sortable:true,dataIndex:'clientName'},
            {header: '省市',sortable: true, dataIndex: 'yqdproCity',
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
            {header: '客户电话', sortable:true, dataIndex:'contactTel'},
            {header:'管理人', sortable:true, dataIndex:'userName'}
        ],
        tbar:[
	        {
		        xtype:'datefield',
		        width:90,
		        format:'Y-m-d',
		        emptyText: '签单时间...',
		        id:'qdtime'
	        },
	        '&nbsp',
	        oppTypeSelCombox2,
	        '&nbsp',
	        clientSelComboBOx4,
	        provinceCombox3,
	        cityCombox3,
	        signedAction,
	        reset4
        ],
        listeners : {
		   'rowdblclick' : function(){
		   		if(roleCode != '201205'){
		   			if(signedGrid.getSelectionModel().hasSelection()){
			            var record = signedGrid.getSelectionModel().getSelected();
			            jump_from_flag = 4;//签单客户跳转
					   	client_id = record.get('id');
					   	sign_time = record.get('signingtime');
						mainPanels.hide();
						if(clientPanel == ''){
							judgeJs('jump_clientJs', 'resources/welcome/jumpClient.js');
							Ext.getCmp('btnAlert').add(clientPanel);
							Ext.getCmp('btnAlert').doLayout();
							var btn = Ext.getCmp('returnPage');
							returnEvent(btn);
						}else{
							clientPanel.show();
						}
						clientStore.setBaseParam('_cpid', client_id);
						clientStore.setBaseParam('_flag', '');
						clientLoadAction.execute();
					} 
		   		}
		   },
		 	'render': function(){
		 		todayBar2.render(this.tbar);
		 	}
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: signedStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    }); 
     //------还款管理--------------
    var refundStore = new Ext.data.JsonStore({
        url: '<%=path%>/index/loadSuccessClient.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions: ''
        },
        fields: [
            { name: 'id' },{ name: 'loanCount' },{ name: 'userName' },
            { name: 'clientName' },{ name: 'user_depaName' },{ name: 'contactTel' },
            { name: 'oppType' },{ name: 'signingtime'},{ name: 'hkproCity'}
        ]
    });
    //-----------还款管理
    var refundLoadAction = new Ext.Action({
        handler: function(){
            loadData(refundStore);
        }
    });   
    var refundAction = new Ext.Action({
		   text:'查询',
		   scale: 'small',
		   iconCls: 'check',
		   handler:function(){
			   var refundDep = Ext.getCmp('refundDepComboBox').getValue();
			   var refundEmp = Ext.getCmp('refundEmpComboBOx').getValue();
			   var opp = Ext.getCmp('oppTypeSelCombox3').getValue();
			   var client_source = Ext.getCmp('clientSelComboBOx5').getValue();
		      	var _provinces2=Ext.getCmp('provinceCombox2').getValue();//--------省份
			    var _citys2=Ext.getCmp('cityCombox2').getValue();//--------城市
			   Ext.apply(refundStore.baseParams,{
		   		conditions:'{refundDep:"'+refundDep+'",refundEmp:"'+refundEmp+'",opp:"'+opp+'",client_source:"'+client_source+'",_provinces2:"'+_provinces2+'",_citys2:"'+_citys2+'"}'
		       });
			   refundStore.load({
			      params:{
			        start:0,
			        limit:20,
			        refundDep:refundDep,
			        refundEmp:refundEmp,
			        opp: opp,
			        _provinces2:_provinces2,
			        _citys2:_citys2
			      }
			   });
		   }
	  });
    //--------还款管理
    var refundGrid = new Ext.grid.GridPanel({
        id: 'refundGrid',
        store: refundStore,
        sm: new Ext.grid.RowSelectionModel(),
        frame:true,
        width:320,
        autoScroll: true,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {header: '签单时间',sortable:true,width:140,dataIndex:'signingtime'},
            {header: '客户信息',width:150,sortable:true,dataIndex:'clientName'},
            {header: '省市',sortable: true,dataIndex: 'hkproCity',
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
            {header: '客户电话',sortable:true,dataIndex:'contactTel'},
            {header:'管理人',sortable:true,dataIndex:'userName'}
        ],
        tbar:[
	        refundDepComboBox,
	        '&nbsp',
	        refundEmpComboBOx,
	        '&nbsp',
	        oppTypeSelCombox3,
	        '&nbsp',
	        clientSelComboBOx5,
	        provinceCombox2,
	        cityCombox2,
	        refundAction,
	        reset3
        ],
        listeners : {
		   'rowdblclick' : function(){
			    var record = refundGrid.getSelectionModel().getSelected(); 
		   		var _fid = record.get('id');
           		document.location.href='<%=path%>/index/findByRefund.do?_fid='+_fid;
		 }
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: refundStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
	searchTbar = new Ext.Toolbar({
    	items:[
    		{
            	id: 'cusTelOrName',
                width: 250,
                height: 25,
                xtype: 'textfield',
                style:'margin-top:5px;',
                emptyText:'请输入客户名称或电话'
            },
	        {
               	 xtype:'button',
       			 iconCls:'check',
               	 text: '查询',
               	 style:'margin-top:5px;',
               	 handler:function(){
               	 	var cusMes = Ext.getCmp('cusTelOrName').getValue();
               	 	judgeJs('search_clientJs', 'resources/welcome/search.js');
               	 	Ext.apply(searchStore.baseParams,{
			   			conditions:'{cusMes:"'+cusMes+'"}'
			        });
				    searchStore.load({
				      params:{
				        start:0,
				        limit:20,
				        cusMes: cusMes
				      }
				    });
           	 		Ext.getCmp('cusTelOrName').setValue('');
                   	searchWindow.show();
           	 	}
            }
    	]
    });
    function indexFillSeeRec(btns){
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
    				if(clientPanel == ''){
						judgeJs('jump_clientJs', 'resources/welcome/jumpClient.js');
						Ext.getCmp('btnAlert').add(clientPanel);
						Ext.getCmp('btnAlert').doLayout();
						var btn = Ext.getCmp('returnPage');
						returnEvent(btn);
					}else{
						clientPanel.show();
					}
    				clientStore.setBaseParam('_cpid', this.id);
					clientStore.setBaseParam('_flag', '');
					clientLoadAction.execute();
    			}
    		}); 
    		seeBtns.push(btn);
    	}
    }
    Ext.Ajax.request({
        url: '<%=path%>/client/showSeeClient.do',
        success: function(aResponse, aOptions){
            var result = Ext.decode(aResponse.responseText);
            indexFillSeeRec(result.data);
            searchTbar.insertButton(0, seeBtns);
			searchTbar.doLayout();
        },
        failure: function(aResponse, aOptions){
            var result = Ext.decode(aResponse.responseText);
            Ext.MessageBox.alert('提示', result.msg);
        }
    });
	mainPanels = new Ext.form.FormPanel({
		layout:'form',
		frame:true,
		autoScroll:true,
		items:[
			searchTbar,
			{
				layout:'column',
	            items:[
		         	{
		                columnWidth:.5,
		                autoScroll:true,
		                style:'padding:5px 0px 5px 5px',
		                items:[
		                	{
		                    	title: '今日新增商机',
		                    	layout:'fit',
		                    	height:400,
		                    	items: todayAddGrid
		                	}
		                ]
		            },
	               	{
	                	columnWidth:.5,
	                	autoScroll:true,
	                	style:'padding:5px 0 5px 5px',
	                	items:[{
	                    	title: '今日需完成商机',
	                    	layout:'fit',
	                    	height:400,
	                    	items: todayWorkPlanGrid
	                	}]
	            	}
	            ]
			},
			{
	            layout:'column',
		            items:[
		              {
		                columnWidth:.5,
		                autoScroll:true,
		                style:'padding:5px 0px 5px 5px',
		                items:[{
		                    title: '已签单商机',
		                    layout:'fit',
		                    height:350,
		                    items: signedGrid
		                }]
		            },
		            {
		                columnWidth:.5,
		                autoScroll:true,
		                style:'padding:5px 0 5px 5px',
		                items:[{
		                    title: '还款管理',
		                    layout:'fit',
		                    height:350,
		                    items: refundGrid
		                }]
		            }
	            ]
	        }
        ]
	});
	var tab = new Ext.TabPanel({
        region:'center',
        margins:'0 0 0 0',
		id:'tab_view',
	 	activeTab:0,
		tabWidth:130,
		enableTabScroll:true,
		defaults:{
			xtype:'panel',
		 	closable:true
		},
		items:[
			{
				iconCls:'smt-welcome',
				title:'桌面',
				id:'btnAlert',
				closable: false,
				autoScroll:true
			}
		]
	});
	var a=0;
	var b='';	
	function createTabpanelItems(href,tit,id,iconCls){
			var tabMainView = Ext.getCmp('tab_view');
			if(a==0){
				a++;
				var p = tabMainView.add({
	                title:tit,
		            id:id+"",
		            iconCls:iconCls,
					html: '<iframe name="centerF" frameborder="0" width="100%" height="100%" src="<%=path%>'+href+'"/>',    
		            closable:true
	            });
	            b=id;
	            tabMainView.setActiveTab(p);
			}else{
				a--;
				tabMainView.remove(b);
				var p = tabMainView.add({
	                title:tit,
		            id:id+"",
		            iconCls:iconCls,
					html: '<iframe name="centerF" frameborder="0" width="100%" height="100%" src="<%=path%>'+href+'"/>',    
		            closable:true
	            });
	            a++;
	            b=id;
	            tabMainView.setActiveTab(p);
			}
	}
	if(roleCode == '201206' | roleCode == '201207'){
		Ext.getCmp('btnAlert').html = '<iframe name="centerF" scrolling="auto" frameborder="0" width="100%" height="100%" src="<%=path%>/to_welcome"/>';
		var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[
				northTitle,
	    		tab
	        ]
	    });
	}else if(roleCode == '201205'){
		var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[
				northTitle,
	    		signedGrid
	        ]
	    });
	    SignedLoadAction.execute();
	}else{
		if(roleCode == '201204'){
			Ext.getCmp('btnAlert').html = '<iframe name="centerF" scrolling="auto" frameborder="0" width="100%" height="100%" src="<%=path%>/to_welcome"/>';
		}else{
			Ext.getCmp('btnAlert').add(mainPanels);
			Ext.getCmp('btnAlert').doLayout();
		}
		var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[	
				northTitle,
	        	tree,
	    		tab
	        ]
	    });
		todayAddLoadAction.execute();
	}
	var warnLoadAction = new Ext.Action({
        handler : function() {
       		Ext.Ajax.request({
	          	url: '<%=path%>/user/processexaminerecords.do',
	            success: function(aResponse, aOptions){
                 	var result = Ext.decode(aResponse.responseText);
                 	var msg=result.message;   
					if(msg == '1'){
						var id = result.id;
						new Ext.ux.ToastWindow({ 
					  		title: '温馨提示', 
					  		html: '</br></br></br><div align="center"><span>有新的客户分配给你了！</span></div>' +
					  			'<div align="center"><a class="title_a" href="javascript:scode('+id+');"">点击查看详情</a></div>'
						}).show(document);
					}
				}
				
          	});
	    }
	});
	var task = {
	  	run: function(){
	    	warnLoadAction.execute();
	  	},
	  	interval: 200000 //1 second
	}
	var runner = new Ext.util.TaskRunner();
	if(roleCode == '201202' | roleCode == '201203'){
		runner.start(task);
	}
});
Ext.namespace("Ext.ux");  
Ext.ux.ToastWindowMgr = {  
    positions: []   
};  
Ext.ux.ToastWindow = Ext.extend(Ext.Window, {  
    initComponent: function(){  
          Ext.apply(this, {  
            iconCls: this.iconCls || 'information',  
            width: 250,  
            height: 180,  
            autoScroll: true,  
            autoDestroy: true,  
            plain: false,  
            shadow:false  
          });  
        this.task = new Ext.util.DelayedTask(this.hide, this);  
        Ext.ux.ToastWindow.superclass.initComponent.call(this);  
    },  
    setMessage: function(msg){  
        this.body.update(msg);  
    },  
    setTitle: function(title, iconCls){  
        Ext.ux.ToastWindow.superclass.setTitle.call(this, title, iconCls||this.iconCls);  
    },  
    onRender:function(ct, position) {  
        Ext.ux.ToastWindow.superclass.onRender.call(this, ct, position);  
    },  
    onDestroy: function(){  
        Ext.ux.ToastWindowMgr.positions.remove(this.pos);  
        Ext.ux.ToastWindow.superclass.onDestroy.call(this);  
    },  
    afterShow: function(){  
        Ext.ux.ToastWindow.superclass.afterShow.call(this);  
        this.on('move', function(){  
               Ext.ux.ToastWindowMgr.positions.remove(this.pos);  
            this.task.cancel();}  
        , this);  
        this.task.delay(5000);  
    },  
    animShow: function(){  
        this.pos = 0;  
        while(Ext.ux.ToastWindowMgr.positions.indexOf(this.pos)>-1)  
            this.pos++;  
        Ext.ux.ToastWindowMgr.positions.push(this.pos);  
        this.setSize(250,180);  
        this.el.alignTo(document, "br-br", [ -20, -((this.getSize().height+10)*this.pos) ]);  
        this.el.slideIn('b', {  
            duration: 2,  
            callback: this.afterShow,  
            scope: this  
        });      
    },  
    animHide: function(){  
           Ext.ux.ToastWindowMgr.positions.remove(this.pos);  
        this.el.ghost("b", {  
            duration: 5,  
            remove: true,  
         scope: this,  
         callback: this.destroy  
        });      
    } 
});
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>