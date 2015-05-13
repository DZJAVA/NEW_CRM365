	var showMsgStore = new Ext.data.JsonStore({
        url: path+'/statements/loadShowClientMsg.do',
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
        listeners: {
        	'dblclick': function(){
				conditions = _assigns + ':' + _ends + ':' + _times + ':' + _manaDes + ':' + _manaEmp + ':' + _client_source
					+ ':' + signPoss;
        		var record = showGrid.getSelectionModel().getSelected(); 
        		jump_from_flag = 5;//报表客户跳转
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
				reportWindow.hide();
        	}
        },
        bbar: new Ext.PagingToolbar({
        	id: 'showMsgPage',
            pageSize: 20,
            store: showMsgStore,
            displayInfo: true,
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    });
    var reportWindow = new Ext.Window({
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
            {
            	text: '返回',
				iconCls: 'returns',
				handler: function(){
					reportWindow.hide();			
				}
            }
        ]
    });