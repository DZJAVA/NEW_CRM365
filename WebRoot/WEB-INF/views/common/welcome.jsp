<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<script type="text/javascript" src="resources/writeJs/provinceCity.js"></script>
		<script type="text/javascript" src="resources/writeJs/combo.js"></script>
		<script type="text/javascript">
		var roleCode = '${userSession.role.roleCode}';
  	Ext.onReady(function(){
  		Ext.QuickTips.init();// 浮动信息提示
   		Ext.BLANK_IMAGE_URL = 'resources/images/default/s.gif';// 替换图片文件地址为本地
   		var i = 0;
   		var ids = [];
   		var idss = '';
   		var qfpage=0;
  		var fhtheCurrents='${param._theCurrent}';
  		var ss = fhtheCurrents.split(",");
  		var checks = '';	//今日新增商机传的conditions
  		var checks1 = '';	//今日工作计划传的conditions
  		var checks2 = '';	//今日已签单传的conditions
  		var condition = '';
  		var conditions1 = '';	//今日新增商机
  		var conditions2 = '';	//今日工作计划
  		var conditions3 = '';	//今日已签单
  		if(ss.length > 2){
  			for(var i= 2;i<ss.length;i++){
  				condition = condition + ","+ ss[i];
  			}
  			if(ss[1] == 1){
	  			conditions1 = condition.substring(1,condition.length);
	  			checks = conditions1;
  			}if(ss[1] == 2){
  				conditions2 = condition.substring(1,condition.length);
	  			checks1 = conditions2;
  			}if(ss[1] == 4){
  				conditions3 = condition.substring(1,condition.length);
	  			checks2 = conditions3;
  			}
  		}
  		var _cpid = '${param._cpid}';
   		if(roleCode == '201202'){
   			resouComboBox.hide();
   			clientDepComboBox.hide();
   			signedDepComboBox.hide();
   			refundDepComboBox.hide();
   			departSelComboBox.hide();
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
   			departSelComboBox.hide();
       	 	employeeSelComboBOx.hide();
   		}
   		
   		var grid1LoadAction = new Ext.Action({
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
						  			'<div align="center"><a class="title_a" href="<%=path%>/index/findWarnClient.do?flag=have&id='+id+'">点击查看详情</a></div>'
							}).show(document);
						}
					}
		          });
		    }
		});
		var task = {
		  	run: function(){
		    	grid1LoadAction.execute();
		  	},
		  	interval: 200000 //1 second
		}
		var runner = new Ext.util.TaskRunner();
	    //--------今日工作计划
	    var todayWorkPlanStore = new Ext.data.JsonStore({
	        url: '<%=path%>/index/findTodayWorkPlan.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'rtid',
	        autoDestroy: true,
	        baseParams: {
	        	conditions: conditions2
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'rtid' },
	            { name: 'clientName' },
		        { name: 'resouRcescontent' },
		        { name: 'workPlan' },
		        { name: 'resourcespeople' },
		        { name: 'telephone' },
		        { name: 'plantime'}
		        
	        ]
	    });
	    //--------查找五个记录信息
	    var fiveClientStore = new Ext.data.JsonStore({
	        url: '<%=path%>/index/showFiveClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'c_id' },
	            { name: 'c_name' }
	        ]
	    });
	    //------------今日新增商机
	    var todayAddStore = new Ext.data.JsonStore({
	        url: '<%=path%>/index/todayAddClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions : conditions1
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'clientName' },
	            { name: 'loanCount' },
	            { name: 'emp_departName' },
	            { name: 'oppType' },
	            { name: 'userName' },
	            { name: 'contactTel' },
		        { name: 'signPossible' },
		        { name: 'assignDate'},
		        { name: 'clientSourse'},
		        { name: 'remark'},
		        { name: 'jrproCity'}
	        ]
	    });
	    //--------已签单商机-------------
	    var signedStore = new Ext.data.JsonStore({
	        url: '<%=path%>/index/signedClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	conditions: conditions3
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'loanCount' },
	            { name: 'userName' },
	            { name: 'clientName' },
	            { name: 'user_depaName' },
	            { name: 'contactTel' },
	            { name: 'oppType' },
	            { name: 'signingtime'},
	            { name: 'yqdproCity'}
	        ]
	    });
	    //------还款管理--------------
	    var refundStore = new Ext.data.JsonStore({
	        url: '<%=path%>/index/loadSuccessClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	start:0,
	        	limit:20,
	        	conditions: ''
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'loanCount' },
	            { name: 'userName' },
	            { name: 'clientName' },
	            { name: 'user_depaName' },
	            { name: 'contactTel' },
	            { name: 'oppType' },
	            { name: 'signingtime'},
	            { name: 'hkproCity'}
	        ]
	    });
	    //-------------------今日需完成商机
	    var todayWorkPlanLoadAction = new Ext.Action({
	        handler: function(){
	        	workPage = 0;
	            if(fhtheCurrents == ''){
					loadData(todayWorkPlanStore);         
                }else{
                	var pages = fhtheCurrents.split(',');
                	if(pages.length > 1){
                		if(pages[1] == 2){
                			todayWorkPlanStore.load({
				                params:{
				                    start:0+(pages[0]-1)*20, 
				                    limit:20
				                }
				            });
                		}else{
                			loadData(todayWorkPlanStore);
                		}
                	}else{
                		loadData(todayWorkPlanStore);
                	}
                }
	        }
	    });  
	    //------今日新增商机
	    var todayAddLoadAction = new Ext.Action({
	        handler: function(aMasterId){
	        	pages = 0;
	        	Ext.Ajax.request({
                    url: '<%=path%>/client/selectTodayAdd.do',
                    success: function(aResponse, aOptions){
                        var result = Ext.decode(aResponse.responseText);
                        idss = result.msg;
                    },
                    failure: function(aResponse, aOptions){
                        var result = Ext.decode(aOptions.response.responseText);
                        Ext.MessageBox.alert('提示', result.msg);
                    }
                });
                todayWorkPlanLoadAction.execute();
                runner.start(task);
                if(fhtheCurrents == ''){
					loadData(todayAddStore);         
                }else{
                	var _pages = fhtheCurrents.split(',');
                	if(_pages.length > 1){
                		if(_pages[1] == 1){
                			todayAddStore.load({
				                params:{
				                    start:0+(_pages[0]-1)*20, 
				                    limit:20
				                }
				            });
                		}else{
                			loadData(todayAddStore);
                		}
                	}else{
                		loadData(todayAddStore);
                	}
                }
	        }
	    });  
	    
	    //------------已签单商机 
	    var SignedLoadAction = new Ext.Action({
	        handler: function(){
	            signedStore.load({
	                params:{
	                    start:0, 
	                    limit:20
	                }
	            });
	        }
	    });  
	    //五个客户信息的显示
	    var fiveClientAction = new Ext.Action({
	        text: '刷新',
	        iconCls:'btn_del',
	        handler: function(aMasterId){
	            fiveClientStore.load({
	                params:{
	                    start:0, 
	                    limit:20
	                }
	            });
	        }
	    });  
		  var resourcesAction = new Ext.Action({
			   text:'查询',
			   scale: 'small',
			   iconCls: 'check',
			   handler:function(){
			   	   workPage = 0;
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
		var workPage = 0;
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
	            {
	                header: '编号',
	                sortable: true,
	                hidden:true,
	                dataIndex: 'rtid'
	            },
	            {
	                header: '计划时间',
	                sortable: true,
	                width:120,
	                dataIndex: 'plantime'
	            },
	            {
	            	header: '工作计划',
	            	width:150,
	            	renderer:function(value, metadata, record, rowIndex, columnIndex, store){
	                	metadata.attr = 'ext:qtip="' + value +'"';  
	            		if(idss != 'null' && idss != undefined){
	            			var id = idss.split(',');
	            			if(workPage > todayWorkPlanStore.data.length | workPage == todayWorkPlanStore.data.length){
	            				workPage = 0;
	            			}
	            			var tadayId = todayWorkPlanStore.getAt(workPage);
	            			var _id = tadayId.get('id');
	            			var size = Ext.getCmp('workPageTool').pageSize - 1;
	            			if(workPage == size){
	            				workPage = 0;
	            			}else{
	            				workPage++;
	            			}
	            			for(var i = 0; i < id.length; i++){
	            				if(id[i] == _id){
	            					return value;
	            				}
	            			}
	            		}
	                	return '<font style="color:red;">'+value+'</font>'
	                },
	            	sortable:true,
	            	dataIndex:'workPlan'
	            },
	            {
	            	header:'客户信息',
	            	width:100,
	            	sortable:true,
	            	dataIndex:'clientName'
	            },
	            {
	            	header:'电话号码',
	            	sortable:true,
	            	width: 90,
	            	dataIndex:'telephone'
	            },
	            {
	            	header:'管理人',
	            	sortable:true,
	            	dataIndex:'resourcespeople'
	            }
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
	            emptyMsg: '没有记录',
	            listeners: {
	            	'change' : function(){
	            		workPage = 0;
	            	}
	            }
	        })
	    }); 
	    
	    //双击跳转到工作计划的详细页面
	    todayWorkPlanGrid.addListener('rowdblclick', rowdblclickFn); 
		function rowdblclickFn(todayWorkPlanGrid,rowindex,e){ 
			if(todayWorkPlanGrid.getSelectionModel().hasSelection()){
	            var record = todayWorkPlanGrid.getSelectionModel().getSelected();
	            var theCurrent = todayWorkPlanGrid.getBottomToolbar().getPageData().activePage;
	            var pid = record.get('id') + ',' + record.get('rtid') + ',' + record.get('plantime');
	            theCurrent += ',2';
	            theCurrent = theCurrent + "," + checks1;
	           	document.location.href='<%=path%>/index/findByToday.do?tid='+pid+'&theCurrent='+theCurrent;
			} 
		}
	
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
	      	pages = 0;
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
		var pages = 0;
		var todayBar = new Ext.Toolbar({
	    	items:[
	    	   '&nbsp',
				clientDepComboBox,
				'&nbsp',
				clientEmpComboBOx
	    	]
	    });
	    var todayAddGrid = new Ext.grid.GridPanel({	
	    	title: '今日新增商机',
	        id: 'todayAddGrid',
	        store: todayAddStore,
         	sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        height: 400,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	           	    header: '创建日期',
	           	    width: 75,
	            	sortable:true,
	            	dataIndex:'assignDate'
	            },
	            {
	            	header: '客户信息',
	            	width: 90,
	            	sortable:true,
	            	dataIndex:'clientName',
	            	renderer:function(value){
	            		if(idss != 'null' && idss != undefined){
	            			var id = idss.split(',');
	            			if(pages > todayAddStore.data.length | pages == todayAddStore.data.length){
	            				pages = 0;
	            			}
	            			var tadayId = todayAddStore.getAt(pages).id;
	            			var size = Ext.getCmp('todayAddPage').pageSize - 1;
	            			if(pages == size){
	            				pages = 0;
	            			}else{
	            				pages++;
	            			}
	            			for(var i = 0; i < id.length; i++){
	            				if(id[i] == tadayId){
	            					return value;
	            				}
	            			}
	            		}
	            		return '<font style="color:red;">'+value+'</font>';
	                }
	            },
	             {
	            	header: '省市',
	            	width:100,
	            	sortable: true,
	            	dataIndex: 'jrproCity',
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
	            	header: '客户电话',
	            	sortable:true,
	            	width: 75,
	            	dataIndex:'contactTel'
	            },
	            {
	            	header:'签单可能性',
	            	hidden: true,
	            	sortable:true,
	            	width: 70,
	            	dataIndex:'signPossible'
	            },
	            {
	            	header:'客户来源',
	            	width: 80,
	            	sortable: true,
	            	dataIndex:'clientSourse'
	            },
	            {
	            	header:'管理人',
	            	width: 75,
	            	sortable:true,
	            	dataIndex:'userName'
	            },
	            {
		            header:'备注',
		            width:100,
		            sortable:true,
		            dataIndex:'remark'
	            }
	        ],
	         listeners : {
			   'rowdblclick' : function(){
				    var record = todayAddGrid.getSelectionModel().getSelected(); 
				   	var tid = record.get('id');
				   	var theCurrent = todayAddGrid.getBottomToolbar().getPageData().activePage;
				   	theCurrent += ',1,';
				   	theCurrent = theCurrent +checks;
		           	document.location.href= '<%=path%>/index/findByToday.do?tid='+tid+'&theCurrent='+encodeURI(theCurrent);
			 	},
			 	'render': function(){
			 		todayBar.render(this.tbar);
			 	}
			},
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
	        bbar: new Ext.PagingToolbar({
	            id:'todayAddPage',
	            pageSize: 20,
	            store: todayAddStore,
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager(),
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录',
	            listeners: {
	            	'change' : function(){
	            		pages = 0;
	            	}
	            }
	        })
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
				        start:0,
				        limit:20,
				        signedDep:signedDep,
				        signedEmp:signedEmp,
				        qdtime:qdtime,
				        opp: opp,
				        _provinces3:_provinces3,
				        _citys3:_citys3
				      }
				  });
			   }
		  });
	     	var todayBar2 = new Ext.Toolbar({
	    	items:[
		        signedDepComboBox,
		        '&nbsp',
		        signedEmpComboBOx
	    	]
	    });
	    //--------已签单商机
	    var signedGrid = new Ext.grid.GridPanel({
	        id: 'signedGrid',
	        store: signedStore,
         	sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        height: 260,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {
	                header: '编号',
	                sortable: true,
	                hidden:true,
	                dataIndex: 'id'
	            },
	            {
	            	header: '签单时间',
	            	sortable:true,
	            	width:140,
	            	dataIndex:'signingtime'
	            },
	            {
	            	header: '客户信息',
	            	width:150,
	            	sortable:true,
	            	dataIndex:'clientName'
	            },
	            {
	            	header: '省市',
	            	sortable: true,
	            	dataIndex: 'yqdproCity',
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
	            	header: '客户电话',
	            	sortable:true,
	            	dataIndex:'contactTel'
	            },
	            {
	            	header:'管理人',
	            	sortable:true,
	            	dataIndex:'userName'
	            }
	        ],
	        tbar:[
		        '&nbsp',
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
			   			var record = signedGrid.getSelectionModel().getSelected(); 
					   	var sid = record.get('id') + ',' + record.get('signingtime');
					   	var theCurrent = signedGrid.getBottomToolbar().getPageData().activePage;
					   	theCurrent += ',4';
					   	theCurrent = theCurrent + "," + checks2;
			           	document.location.href='<%=path%>/index/findByToday.do?tid='+sid+'&theCurrent='+theCurrent;
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
	            {
	                header: '编号',
	                sortable: true,
	                hidden:true,
	                dataIndex: 'id'
	            },
	            {
	            	header: '签单时间',
	            	sortable:true,
	            	width:140,
	            	dataIndex:'signingtime'
	            },
	            {
	            	header: '客户信息',
	            	width:150,
	            	sortable:true,
	            	dataIndex:'clientName'
	            },
	            {
	            	header: '省市',
	            	sortable: true,
	            	dataIndex: 'hkproCity',
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
	            	header: '客户电话',
	            	sortable:true,
	            	dataIndex:'contactTel'
	            },
	            {
	            	header:'管理人',
	            	sortable:true,
	            	dataIndex:'userName'
	            }
	        ],
	        tbar:[
	        '&nbsp',
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
	    
	    fiveClientStore.load({
	    	callback: function(records, options, success){
		    	if( fiveClientStore.getCount() == 1) {
		    		Ext.getCmp('btnFirst').show();
		    		Ext.getCmp('btnFirst').setText(records[0].get('c_name'));
		    	}if( fiveClientStore.getCount() == 2) {
		    		Ext.getCmp('btnFirst').show();
		    		Ext.getCmp('btnSecond').show();
		    		Ext.getCmp('btnFirst').setText(records[0].get('c_name')); 
		    		Ext.getCmp('btnSecond').setText(records[1].get('c_name'));
		    	}if( fiveClientStore.getCount() == 3) {
		    		Ext.getCmp('btnFirst').show();
		    		Ext.getCmp('btnSecond').show();
		    		Ext.getCmp('btnThird').show();
	    			Ext.getCmp('btnFirst').setText(records[0].get('c_name')); 
	            	Ext.getCmp('btnSecond').setText(records[1].get('c_name')); 
	            	Ext.getCmp('btnThird').setText(records[2].get('c_name'));
		    	}if( fiveClientStore.getCount() == 4) {
		    		Ext.getCmp('btnFirst').show();
		    		Ext.getCmp('btnSecond').show();
		    		Ext.getCmp('btnThird').show();
		    		Ext.getCmp('btnFourth').show();
		    		Ext.getCmp('btnFirst').setText(records[0].get('c_name')); 
	            	Ext.getCmp('btnSecond').setText(records[1].get('c_name')); 
	            	Ext.getCmp('btnThird').setText(records[2].get('c_name')); 
	            	Ext.getCmp('btnFourth').setText(records[3].get('c_name')); 
		    	}if( fiveClientStore.getCount() >= 5) {
		    		Ext.getCmp('btnFirst').show();
		    		Ext.getCmp('btnSecond').show();
		    		Ext.getCmp('btnThird').show();
		    		Ext.getCmp('btnFourth').show();
		    		Ext.getCmp('btnFivth').show();
		    		Ext.getCmp('btnFirst').setText(records[0].get('c_name')); 
	            	Ext.getCmp('btnSecond').setText(records[1].get('c_name')); 
	            	Ext.getCmp('btnThird').setText(records[2].get('c_name')); 
	            	Ext.getCmp('btnFourth').setText(records[3].get('c_name')); 
	            	Ext.getCmp('btnFivth').setText(records[4].get('c_name')); 
		    	}   
            }
	    });
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
							_assigns = _assign; _ends = _endAssignDate; _times = _time;
							_manaDes = _depComboBox; _manaEmp = _empComboBox; _client_source = client_source;
							Ext.apply(masterStore.baseParams,{
								conditions:'{_assign:"'+_assign+'",_endAssignDate:"'+_endAssignDate+'",_time:"'+_time+'",_depComboBox:"'+_depComboBox+'",_empComboBox:"'+_empComboBox+'",client_source:"'+_client_source+'"}'
							});
							masterStore.load();
						}
					}else{
						_assigns = _assign; _ends = _endAssignDate; _times = _time; _manaDes = _depComboBox;
						_manaEmp = _empComboBox; _client_source = client_source;
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
	                       display: 'bottom', padding: 5,  
	                       font: {  
	                          family : 'Tahoma', size: 13  
	                       }  
	                 }  
	            },
		        chartStyle: {
		            padding: 10,
		            animationEnabled: true,//柱状图 弹性显示
		            font: {
		                name: 'Tahoma', color: 0x444444, size: 11
		            },
		            dataTip: {
		                padding: 5,
		                border: {
		                    color: 0x99bbe8, size: 1
		                },
		                background: {
		                    color: 0xDAE7F6, alpha: .9
		                },
		                font: {
		                    name: 'Tahoma', color: 0x15428B, size: 10,bold: true
		                }
		            },
		            xAxis: {
		                color: 0x69aBc8,
		                majorTicks: {
		                    color: 0x69aBc8, length: 4
		                },
		                minorTicks: {
		                    color: 0x69aBc8, length: 2
		                },
		                majorGridLines: {
		                    size: 1, color: 0xeeeeee
		                }
		            },
		            yAxis: {
		                color: 0x69aBc8,
		                majorTicks: {
		                    color: 0x69aBc8, length: 4
		                },
		                minorTicks: {
		                    color: 0x69aBc8, length: 2
		                },
		                majorGridLines: {
		                    size: 1, color: 0xdfe8f6
		                }
		            }
		        },
		        series: [
		        	{
			            type: 'column', displayName: '房贷', yField: 'count1',
			            style: {
			            	marginBottom: '10px', color: '#E82F37'
		           	 	}
		            },
					{
			            type: 'column', displayName: '信贷', yField: 'count2',
			            style: {
			            	marginBottom: '10px', color: '#A8A8A8'
			            }
			       	},
			       	{
			            type: 'column', displayName: '短借', yField: 'count3',
			            style: {
			            	marginBottom: '10px', color: '#A9DBF6'
			            }
			       	},
			       	{
			            type: 'column', displayName: '企贷', yField: 'count4',
			            style: {
			            	marginBottom: '10px', color: '#0x69aBc8'
			            }
			       	},
			       	{
			            type: 'column', displayName: '未定义', yField: 'count5',
			            style: {
			            	marginBottom: '10px', color: '#3307F4'
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
						judgeJs('report_clientJs', 'resources/welcome/report.js');
						Ext.apply(showMsgStore.baseParams,{
							conditions1:'{_assigns:"'+_assigns+'",signPoss:"'+signPoss+'",_ends:"'+_ends+'",_times:"'+_times+'",_manaDes:"'+_manaDes+'",_manaEmp:"'+_manaEmp+'",client_source:"'+_client_source+'"}'
						});
						showMsgStore.load({
				            params:{
				                start:0, 
				                limit:20
				            }
					    });
						reportWindow.show();
					}
				}
	    	}
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
	  	if('201205' == roleCode){
	  		var viewport = new Ext.Viewport({
		    	layout:'fit',
		        items:[
		        	signedGrid
		        ]
	    	});
	  	}else{
	  		var mainPanel = new Ext.Panel({
	    	autoScroll:true,
	    	items:[
		        {
	                    columnWidth: .1,
	                    defaultType: 'textfield',
	                    defaults: {
	                        msgTarget: 'side',
	                        height:20
	                    },
	                    items: [
	                        {
			                	 xtype:'button',
			        			 iconCls:'check',
			                	 width:50,
			                	 text: '查询',
			                	 style:'float:right;margin-right:40px;margin-top:5px;',
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
			                },
			                {
	                            id: 'cusTelOrName',
		                      	style:'float:right;margin-right:5px;margin-top:5px;',
	                            width:'20%',
	                            emptyText:'请输入客户名称或电话'
	                        },
	                        {
	                        	xtype:'button',
	                        	style:'float:right;margin: 5 5 0 5',
	                        	width:'10%',
	                        	hidden:true,
	                        	id:'btnFirst',
	                        	handler:function(){
							   		var seid = fiveClientStore.getAt(0).get('c_id');
				           			document.location.href='<%=path%>/index/findByToday.do?tid='+seid;
	                        	}
	                        },
	                        {
	                        	xtype:'button',
	                        	style:'float:right;margin: 5 5 0 5',
	                        	width:'10%',
	                        	hidden:true,
	                        	id:'btnSecond',
	                        	handler:function(){
							   		var seid = fiveClientStore.getAt(1).get('c_id');
				           			document.location.href='<%=path%>/index/findByToday.do?tid='+seid;
	                        	}
	                        },
	                        {
	                        	xtype:'button',
	                        	style:'float:right;margin: 5 5 0 5',
	                        	width:'10%',
	                        	hidden:true,
	                        	id:'btnThird',
	                        	handler:function(){
							   		var seid = fiveClientStore.getAt(2).get('c_id');
				           			document.location.href='<%=path%>/index/findByToday.do?tid='+seid;
	                        	}
	                        },
	                        {
	                        	xtype:'button',
	                        	style:'float:right;margin: 5 5 0 5',
	                        	width:'10%',
	                        	hidden:true,
	                        	id:'btnFourth',
	                        	handler:function(){
							   		var seid = fiveClientStore.getAt(3).get('c_id');
				           			document.location.href='<%=path%>/index/findByToday.do?tid='+seid;
	                        	}
	                        },
	                        {
	                        	xtype:'button',
	                        	style:'float:right;margin: 5 5 0 5',
	                        	width:'10%',
	                        	hidden:true,
	                        	id:'btnFivth',
	                        	handler:function(){
							   		var seid = fiveClientStore.getAt(4).get('c_id');
				           			document.location.href='<%=path%>/index/findByToday.do?tid='+seid;
	                        	}
	                        }
	                       
	                    ]
	                },
			        {
			            xtype:'panel',
			            layout:'column',
				            items:[
				         	{
				                columnWidth:.5,
				                autoScroll:true,
				                style:'padding:5px 0px 5px 5px',
				                items:[
									todayAddGrid
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
			            xtype:'panel',
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
			        },
		            {
		            xtype:'panel',
		            layout:'column',
			            items:[
			       		 {
			                columnWidth:1.0,
			                width:'100%',
			                style:'padding:5px 0px 5px 5px',
			                items:[{
			                    title: '统计报表',
			                    layout:'fit',
			                    height:500,
			                    items: mainPanels
			                }]
			            }
		            ]
		        }
		    	]
		    });
		    var viewport = new Ext.Viewport({
	    	layout:'fit',
	        items:[
	        	mainPanel
	        ]
	    });
	  	}
	    //-----------还款管理
	    var refundLoadAction = new Ext.Action({
	        handler: function(){
	            refundStore.load({
	                params:{
	                    start:0, 
	                    limit:20
	                }
	            });
	        }
	    });   
        
	    todayAddLoadAction.execute();
	    SignedLoadAction.execute();
	    refundLoadAction.execute();
	    myGridLoadAction.execute();
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
