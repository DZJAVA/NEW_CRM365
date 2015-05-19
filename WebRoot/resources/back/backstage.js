var path = '/CRM';
var SIGN = {sign:{store: null, grid:null}, source:{store: null, grid:null}, log:{store: null, grid:null}};
Ext.onReady(function(){
	Ext.QuickTips.init();// 浮动信息提示
	Ext.BLANK_IMAGE_URL = path+'/resources/images/default/s.gif';// 替换图片文件地址为本地
    
    SIGN.role = '${userSession.role.roleCode}';
    
   	initSignGrid();
   	initSourceGrid();
   	initLogGrid();
   	
    initLayout();//布局
    
	SIGN.sign.store.load({
    	params:{start:0, limit:20}
	});
});
//签单列表
var initSignGrid = function(){
	SIGN.sign.store = new Ext.data.JsonStore({
        url: path+'/sign_client/loadSignClient.do',
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
    var saveSignEvents = function(btn){
    	if(!btn.hasListener('click')){
    		btn.addListener('click', function(){
	            if(signForm.getForm().isValid()){
           			signForm.getForm().submit({
                    	url: path+'/sign_client/saveOrUpdateSign.do',
	                    params: {
	                        cid: 0
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在努力的保存数据...',
	                    timeout: 20,
	                    success: function(aForm, aAction){
	                    	signWindow.hide();
                    		Ext.MessageBox.alert('提示', aAction.result.msg); 
	                        SIGN.sign.store.reload();
	                    },
	                    failure: function(aForm, aAction) {
	                    	var result = aAction.result;
	                        Ext.MessageBox.alert('提示', result.msg);                           
	                    }
               		});
	            }
   			});
    	}
    };
    var editAction = new Ext.Action({
        text: '编辑信息',
        iconCls: 'vcard_add',
        handler: function(){
        	var record = SIGN.sign.grid.getSelectionModel().getSelected();
            if(record){
	        	judgeJs('edit_sign', 'resources/back/edit_sign.js');
	        	var btn = Ext.getCmp('saveSign');
	        	saveSignEvents(btn);
	            signForm.getForm().reset();
	            signWindow.show();
                signForm.getForm().loadRecord(record);
            }
        }
    });
   	// --------------- grid  资料列表 --------------------
    SIGN.sign.grid = new Ext.grid.GridPanel({
        store: SIGN.sign.store,
        sm: new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
        region: 'north',
        height: 350,
        frame:true,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
        	new Ext.grid.CheckboxSelectionModel ({singleSelect : false}),
            {header: '报单日期', width:90, sortable: true,dataIndex: 'signDate'},
            {header: '委托编码', width:100, dataIndex: 'clientCode'},
            {header: '报单人', width:100, dataIndex: 'frontUser', renderer:function(value,data,row){
           		return row.data.frontDept+':'+row.data.frontUser;
             }},
            {header: '客户信息', width:80, dataIndex: 'clientName'},
            {header: '联系电话', width:90, dataIndex: 'contactTel'},
            {header: '贷款金额', width:80, dataIndex: 'loanAmount'},
            {header: '贷款品种', width:100, dataIndex: 'loanType'},
            {header: '保单银行', width:150, dataIndex: 'loanBank',renderer: function(value, metadata){
               	metadata.attr = 'ext:qtip="' + value +'"';  
               	return value;
             }},
            {header: '来源渠道', width:100, dataIndex: 'loanSource'},
            {header: '接单时间', width:90, dataIndex: 'followDate'},
            {header: '接单人', width:90, dataIndex: 'userName'},
            {header: '前后台接单情况', width:120, dataIndex: 'followInfo'},
            {header: '状态', width:90, dataIndex: 'status', renderer: function(val){
             	switch(val){
             		case 0: return '资料未齐';
             		case 1: return '进件';
             		case 2: return '退单';
             		default: return '资料未齐';
             	}
              	}},
            {header: '退单时间', width:90,dataIndex: 'backDate'}
        ],
        tbar: [
        	editAction
        ],
        listeners: {
        	'rowdblclick': function(grid){
        		var record = grid.getSelectionModel().getSelected();
        		judgeJs('edit_sign', 'resources/back/edit_sign.js');
	        	var btn = Ext.getCmp('saveSign');
	        	saveSignEvents(btn);
	            signForm.getForm().reset();
	            signWindow.show();
                signForm.getForm().loadRecord(record);
        	}
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: SIGN.sign.store,
            displayInfo: true,
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    SIGN.sign.grid.getSelectionModel().addListener('selectionchange', function(){
    	var record = SIGN.sign.grid.getSelectionModel().getSelected(); 
        if(record){
       		cid = record.get('id');
  			SIGN.source.store.setBaseParam('signId', record.get('id'));
       		SIGN.source.store.load({
       			params: {start:0, limit:20}
         	});
         	SIGN.source.add.enable();
        }else{
        	SIGN.source.add.disable();
        	SIGN.source.store.removeAll();
        }
    });
};
//渠道列表
var initSourceGrid = function(){
	SIGN.source.store = new Ext.data.JsonStore({
        url: path+'/sign_client/loadSourceBySign.do',
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
    var saveSourceEvents = function(btn){
    	if(!btn.hasListener('click')){
    		btn.addListener('click', function(){
    			var record = SIGN.sign.grid.getSelectionModel().getSelected();
    			var signId = record.get('id');
	            if(sourceForm.getForm().isValid()){
           			sourceForm.getForm().submit({
                    	url: path+'/sign_client/saveOrUpdateSource.do',
	                    params: {
	                        signId: signId
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在努力的保存数据...',
	                    timeout: 20,
	                    success: function(aForm, aAction){
	                    	sourceWindow.hide();
                    		Ext.MessageBox.alert('提示', aAction.result.msg); 
	                        SIGN.source.store.reload();
	                    },
	                    failure: function(aForm, aAction) {
	                    	var result = aAction.result;
	                        Ext.MessageBox.alert('提示', result.msg);                           
	                    }
               		});
	            }
   			});
    	}
    };
    SIGN.source.add = new Ext.Action({
        text: '新增渠道',
        iconCls: 'vcard_add',
        disabled: true,
        handler: function(){
        	judgeJs('add_source', 'resources/back/addSource.js');
        	var btn = Ext.getCmp('saveSource');
        	saveSourceEvents(btn);
            sourceForm.getForm().reset();
            sourceWindow.show();
        }
    });
    
    var editAction = new Ext.Action({
        text: '编辑渠道',
        iconCls: 'book_edit',
        disabled: true,
        handler: function(){
        	var record = SIGN.source.grid.getSelectionModel().getSelected();
        	if(record){
	        	judgeJs('add_source', 'resources/back/addSource.js');
	        	var btn = Ext.getCmp('saveSource');
	        	saveSourceEvents(btn);
	            sourceForm.getForm().reset();
	            sourceWindow.show();
	            sourceForm.getForm().loadRecord(record);
        	}
        }
    });
    
    var delAction = new Ext.Action({
        text: '删除渠道',
        iconCls: 'vcard_delete',
        disabled: true,
        handler: function(){
            if (SIGN.source.grid.getSelectionModel().hasSelection()) {
                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
                    if (aButton == 'yes'){
                        var record = SIGN.source.grid.getSelectionModel().getSelected();
                       	Ext.Ajax.request({
                            url: path+'/sign_client/deleteSource.do',
                            params: {
                                id: record.id
                            },
                            success: function(aResponse, aOptions){
                            	SIGN.source.store.reload();
                            	SIGN.log.store.removeAll();
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
    //-----------------资料追踪-----------------
	SIGN.source.grid = new Ext.grid.GridPanel({
        store: SIGN.source.store,
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
        	SIGN.source.add, editAction, delAction
        ],
        listeners : {
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: SIGN.source.store,
            displayInfo: true,
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    SIGN.source.grid.getSelectionModel().addListener('selectionchange', function(){
    	var record = SIGN.source.grid.getSelectionModel().getSelected(); 
       	if(record){
       		editAction.enable();
       		delAction.enable();
       		SIGN.log.add.enable();
       		SIGN.log.store.setBaseParam('sourceId', record.get('id'));
        	SIGN.log.store.load({
             	params: {start:0, limit:20}
         	});
       	}else{
       		editAction.disable();
       		delAction.disable();
       		SIGN.log.add.disable();
       		SIGN.source.store.removeAll();
       	}
    });
};
//跟踪日志列表
var initLogGrid = function(){
    SIGN.log.store = new Ext.data.JsonStore({
        url: path+'/sign_client/loadLogBySource.do',
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
    
    var saveSourceLogEvents = function(btn){
    	if(!btn.hasListener('click')){
    		btn.addListener('click', function(){
    			var record = SIGN.source.grid.getSelectionModel().getSelected();
    			var sourceId = record.get('id');
	            if(logForm.getForm().isValid()){
           			logForm.getForm().submit({
                    	url: path+'/sign_client/saveOrUpdateLog.do',
	                    params: {
	                        sourceId: sourceId
	                    },
	                    waitTitle: '请等待',
	                    waitMsg: '正在努力的保存数据...',
	                    timeout: 20,
	                    success: function(aForm, aAction){
	                    	logWindow.hide();
                    		Ext.MessageBox.alert('提示', aAction.result.msg); 
	                        SIGN.log.store.reload();
	                    },
	                    failure: function(aForm, aAction) {
	                    	var result = aAction.result;
	                        Ext.MessageBox.alert('提示', result.msg);                           
	                    }
               		});
	            }
   			});
    	}
    };
    
    SIGN.log.add = new Ext.Action({
        text: '新增渠道日志',
        iconCls: 'vcard_add',
        disabled: true,
        handler: function(){
        	judgeJs('add_Log', 'resources/back/addLog.js');
        	var btn = Ext.getCmp('saveSourceLog');
        	saveSourceLogEvents(btn);
            logForm.getForm().reset();
            logWindow.show();
        }
    });
    
    var editAction = new Ext.Action({
        text: '编辑渠道日志',
        iconCls: 'book_edit',
        disabled: true,
        handler: function(){
        	var record = SIGN.log.grid.getSelectionModel().getSelected();
        	if(record){
	        	judgeJs('add_Log', 'resources/back/addLog.js');
	        	var btn = Ext.getCmp('saveSourceLog');
	        	saveSourceLogEvents(btn);
	            logForm.getForm().reset();
	            logWindow.show();
	            logForm.getForm().loadRecord(record);
        	}
        }
    }); 
    
    var delAction = new Ext.Action({
        text: '删除渠道日志',
        iconCls: 'vcard_delete',
        disabled: true,
        handler: function(){
            if (SIGN.log.grid.getSelectionModel().hasSelection()) {
                Ext.Msg.confirm('删除确认', '是否删除选择的记录?', function(aButton){
                    if (aButton == 'yes'){
                        var record = SIGN.log.grid.getSelectionModel().getSelected();
                       	Ext.Ajax.request({
                            url: path+'/sign_client/deleteSourceLog.do',
                            params: {
                                id: record.id
                            },
                            success: function(aResponse, aOptions){
                            	SIGN.log.store.reload();
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
    
    SIGN.log.grid = new Ext.grid.GridPanel({
        store: SIGN.log.store,
        region: 'south',
        height: 240,
        sm: new Ext.grid.RowSelectionModel(),
        frame: true,
        trackMouseOver: false,
        disableSelection: false,
        loadMask: true,
        columns: [
            {header: '跟踪信息', width: 400, dataIndex: 'logInfo', renderer: function(value, metadata){
               	metadata.attr = 'ext:qtip="' + value +'"';  
               	return value;
           	}},
            {header: '跟踪时间', width: 150, dataIndex: 'logDate'}
        ],
        tbar: [
        	SIGN.log.add, editAction, delAction
        ],
        listeners : {
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: SIGN.log.store,
            displayInfo: true,
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    SIGN.log.grid.getSelectionModel().addListener('selectionchange', function(){
    	var record = SIGN.log.grid.getSelectionModel().getSelected(); 
       	if(record){
       		editAction.enable();
       		delAction.enable();
       	}else{
       		editAction.disable();
       		delAction.disable();
       	}
    });
};
//布局
var initLayout = function(){
	var viewport = null;
	if(SIGN.role == '201206'){
	    viewport = new Ext.Viewport({
		      layout:'fit',
		      border: false,
		      items:[
		     	   SIGN.sign.grid
		      ]
	    });
    }else{
    	var mainPanel = new Ext.Panel({
	        autoScroll:true,
	        layout:'form',
	        monitorResize: true, 
	        items:[
	        	SIGN.sign.grid,
	       	    SIGN.source.grid,
	       	    SIGN.log.grid
	        ]
	     });
	     viewport = new Ext.Viewport({
		       layout:'fit',
		       border: false,
		       autoScroll:true,
		       items:[
		       	   mainPanel
		       ]
	     });
    }
};
//判断js是否加载
function judgeJs(id, src){
	var el = Ext.get(id);
	if(!el){
		AjaxPage(id, src);
	}
}

function AjaxPage(sId, url){ 
	var oXmlHttp = GetHttpRequest() ; 
	oXmlHttp.onreadystatechange = function() { 
	    if ( oXmlHttp.readyState == 4 ) { 
	        if ( oXmlHttp.status == 200 || oXmlHttp.status == 304 ) { 
	            IncludeJS( sId, url, oXmlHttp.responseText ); 
	        }else { 
	            alert( 'XML request error: ' + oXmlHttp.statusText + ' (' + oXmlHttp.status + ')' ) ; 
	        } 
	    }
    }
    oXmlHttp.open('GET', url, false); 
    oXmlHttp.send(null); 
}

function IncludeJS(sId, fileUrl, source){
    if (( source != null ) && ( !document.getElementById( sId ) ) ){ 
        var oHead = document.getElementsByTagName('HEAD').item(0); 
        var oScript = document.createElement( "script" ); 
        oScript.language = "javascript"; 
        oScript.type = "text/javascript"; 
        oScript.id = sId; 
        oScript.defer = true; 
        oScript.text = source; 
        oHead.appendChild( oScript ); 
    }
}

function GetHttpRequest(){ 
    if (window.XMLHttpRequest) // Gecko 
        return new XMLHttpRequest() ; 
    else if (window.ActiveXObject) // IE 
        return new ActiveXObject("MsXml2.XmlHttp") ; 
} 