<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
		Ext.onReady(function(){
			Ext.QuickTips.init();// 浮动信息提示
    		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
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
     	//-----刷新 资源列表---------------
	    var myGridLoadAction = new Ext.Action({
	        handler: function(aMasterId){
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
            	autoAssignAction.enable();
           		cid = record.get('id');
           		_gridStore.setBaseParam('_cpid', cid);
               	_gridloanLoadAction.execute();
            }else{
               	clientEditAction.disable();
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
	            {header: '客户名字',width:90,hidden:true,sortable: true,dataIndex: 'clientName'},
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
	        ],
	        listeners : {
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
	            {header: '管理人',width:80,sortable: true,dataIndex: 'resourcespeople_name'},
	            {header: '跟单时间',width:150,sortable: true,dataIndex: 'resourcestime'},
	            {header:'计划时间',sortable:true,width:130,dataIndex:'plantime'}
	        ],
	        tbar: [
	        ],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store:_gridStore,
	            displayInfo: true,
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
	    //---------------退单------------------------------
	    if(roleCode == '201206'){
	    	Ext.getCmp('phoneNum').show();
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
