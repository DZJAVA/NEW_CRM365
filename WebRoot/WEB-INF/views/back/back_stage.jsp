<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
	Ext.onReady(function(){
		Ext.QuickTips.init();// 浮动信息提示
   		Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
	   //---------------资源列表-----
	    var signStore = new Ext.data.JsonStore({
	        url: '<%=path%>/sign_client/loadSignClient.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	condition:''
	        },
	        fields: [
                 { name: 'id' },
	             { name: 'clientName' },
	             { name: 'contactTel' },
	             { name: 'loanAmount' },
	             { name: 'signDate' },
	             { name: 'clientCode' },
	             { name: 'loanType' },
	             { name: 'loanBank' },
	             { name: 'loanSource' },
	             { name: 'frontUser' },
	             { name: 'frontDept' },
	             { name: 'backDate' },
	             { name: 'status' },
	             { name: 'userName' },
	             { name: 'deptName' },
	             { name: 'followInfo' },
	             { name: 'followDate' }
	        ]
	    });
	    //---------------资源跟踪列表-----
	    var sourceStore = new Ext.data.JsonStore({
	        url: '<%=path%>/sign_client/loadSourceBySign.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	signId: 0
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'loanDate' },
	            { name: 'loanYear' },
	            { name: 'loanInterest' },
	            { name: 'serviceFee' },
	            { name: 'interestType' },
	            { name: 'sourceName' },
	            { name: 'loanAmount' },
	            { name: 'status' },
	            { name: 'receiveAmount' },
	            { name: 'sourceAmount' }
	        ]
	    });
	    //---------------资源跟踪列表-----
	    var logStore = new Ext.data.JsonStore({
	        url: '<%=path%>/sign_client/loadLogBySource.do',
	        root: 'data',
	        totalProperty: 'totalCount',
	        idProperty: 'id',
	        autoDestroy: true,
	        baseParams: {
	        	sourceId: 0
	        },
	        fields: [
	            { name: 'id' },
	            { name: 'logDate' },
	            { name: 'logInfo' }
	        ]
	    });
     	//-----刷新 签单列表---------------
	    var signLoadAction = new Ext.Action({
	        handler: function(){
	            signStore.load({
	                params:{
	                    start:0,
	                    limit:20
	                }
	            });
	        }
	    });
	      
     	//---------------签单select change触发-----
   		function signGridSelectChange(){
            var record = signGrid.getSelectionModel().getSelected(); 
            if(record){
           		cid = record.get('id');
           		sourceStore.setBaseParam('signId', record.get('id'));
           		signStore.load({
           			params: {start:0, limit:20}
	            });
            }else{
            	
            }
  	  	}
	   	// --------------- grid  资料列表 --------------------
	    var signGrid = new Ext.grid.GridPanel({
	        store: signStore,
	        sm: new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
	        region: 'north',
	        height: 350,
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	        	new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
	            {header: '报单日期', width:100, sortable: true,dataIndex: 'signDate'},
	            {header: '委托编码', width:100, dataIndex: 'clientCode'},
	            {header: '报单人', width:100, dataIndex: 'frontUser', renderer:function(value,data,row){
            		return row.data.frontDept+':'+row.data.frontUser;
	             }},
	            {header: '客户信息', width:80, dataIndex: 'clientName'},
	            {header: '联系电话', width:80, dataIndex: 'contactTel'},
	            {header: '贷款金额', width:80, dataIndex: 'loanAmount'},
	            {header: '贷款品种', width:100, dataIndex: 'loanType'},
	            {header: '保单银行', width:100, dataIndex: 'loanBank',renderer: function(value){
                	metadata.attr = 'ext:qtip="' + value +'"';  
                	return value;
               	 }},
	            {header: '来源渠道', width:100, dataIndex: 'loanSource'},
	            {header: '接单时间', width:100, dataIndex: 'followDate'},
	            {header: '接单人', width:100, dataIndex: 'userName'},
	            {header: '前后台接单情况', width:100, dataIndex: 'followInfo'},
	            {header: '状态', width:100, dataIndex: 'status', renderer: function(val){
	             	switch(val){
	             		case 0: return '资料未齐';
	             		case 1: return '进件';
	             		case 2: return '退单';
	             		default: return '资料未齐';
	             	}
               	}},
	            {header: '退单时间', width:60,dataIndex: 'backDate'}
	        ],
	        tbar: [
	        ],
	        listeners : {
	        	'selectionchange': signGridSelectChange
			},
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store: signStore,
	            displayInfo: true,
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	    //---------------渠道select change触发-----
      	function sourceGridSelectChange (){
            var record = sourceGrid.getSelectionModel().getSelected(); 
            if(record){
            	logStore.setBaseParam('sourceId', record.get('id'));
            	logStore.load({
	                params: {start:0, limit:20}
	            });
            }
  	  	};
	    //-----------------资料追踪-----------------
      	var sourceGrid = new Ext.grid.GridPanel({
	        store: sourceStore,
	        region: 'center',
	        height: 250,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame:true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {header: '渠道名称', width:80, dataIndex: 'sourceName'},
	            {header: '渠道金额', width:150, dataIndex: 'sourceAmount'},
	            {header: '服务费用', width:150, dataIndex: 'serviceFee'},
	            {header: '状态', width:150, dataIndex: 'status', renderer: function(val){
	             	switch(val){
	             		case 0: return '未通过';
	             		case 1: return '放款';
	             		default: return '未通过';
	             	}
               	}},
	            {header: '放款时间', width:150, dataIndex: 'loanDate'},
	            {header: '放款金额', width:150, dataIndex: 'loanAmount'},
	            {header: '放款年限', width:150, dataIndex: 'loanYear'},
	            {header: '放款利息', width:150, dataIndex: 'loanInterest'},
	            {header: '利息类型', width:150, dataIndex: 'interestType'},
	            {header:'收款金额', width:130,dataIndex: 'receiveAmount'}
	        ],
	        tbar: [
	        ],
	        listeners : {
	        	'selectionchange': sourceGridSelectChange
			},
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store: sourceStore,
	            displayInfo: true,
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	    //---------------渠道日志select change触发-----
      	function logGridSelectChange (){
            var record = logGrid.getSelectionModel().getSelected(); 
            if(record){
            	
            }
  	  	};
	    //-------------------客户签约----------------------
	    var logGrid = new Ext.grid.GridPanel({
	        store: logStore,
	        region: 'south',
	        height: 240,
	        sm: new Ext.grid.RowSelectionModel(),
	        frame: true,
	        trackMouseOver: false,
	        disableSelection: false,
	        loadMask: true,
	        columns: [
	            {header: '跟踪信息', dataIndex: 'logInfo', renderer: function(value){
                	metadata.attr = 'ext:qtip="' + value +'"';  
                	return value;
               	 }},
	            {header: '跟踪时间', dataIndex: 'logDate'}
	        ],
	        listeners : {
	        	'selectionchange': logGridSelectChange
			},
	        bbar: new Ext.PagingToolbar({
	            pageSize: 20,
	            store: logStore,
	            displayInfo: true,
	            displayMsg: '显示: {0} - {1} / 总数: {2}',
	            emptyMsg: '没有记录'
	        })
	    });
	    //---------------退单------------------------------
	    if(roleCode == '201206'){
		    var viewport = new Ext.Viewport({
			      layout:'fit',
			      border: false,
			      items:[
			     	   signGrid
			      ]
		    });
	    }else{
	    	var mainPanel = new Ext.Panel({
		        autoScroll:true,
		        layout:'form',
		        monitorResize: true, 
		        items:[
		        	signGrid,
		       	    sourceGrid,
		       	    logGrid
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
   		signLoadAction.execute();
	});
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
