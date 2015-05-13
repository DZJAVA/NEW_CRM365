	//--------百度搜索
    var searchStore = new Ext.data.JsonStore({
        url: path+'/index/findByCusNameOrTel.do',
        root: 'data',
        totalProperty: 'totalCount',
        idProperty: 'id',
        autoDestroy: true,
        baseParams: {
        	conditions: ''
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
            { name: 'clientSource' },
            { name: 'managerUser' },
            { name: 'assignTime' },
            { name: 'remark' },
            { name: 'bdshproCity'},
            { name: 'assignId'},
            { name: 'assignName'}
        ]
    });
	//-------------百度搜索结果
    var searchGrid = new Ext.grid.GridPanel({
        id: 'searchGrid',
        store: searchStore,
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
            	header: '客户信息',
            	width:150,
            	sortable:true,
            	dataIndex:'clientName'
            },
             {
            	header: '省市',
            	sortable: true,
            	dataIndex: 'bdshproCity',
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
            	header: '客户联系方式',
            	sortable:true,
            	dataIndex:'contactTel'
            },
             {
            	header:'备用电话1',
            	sortable:true,
            	dataIndex:'spareTel1'
            },
            {
            	header:'备用电话2',
            	sortable:true,
            	dataIndex:'spareTel2'
            },
            {
            	header:'客户状态',
            	sortable:true,
            	dataIndex:'clientStatus'
            },
            {
            	header:'成单率',
            	sortable:true,
            	dataIndex:'signPossible'
            },
            {
            	header:'客户地址',
            	sortable:true,
            	dataIndex:'clientAdd'
            },
            {
            	header:'管理人',
            	sortable:true,
            	dataIndex:'managerUser'
            },
            {
            	header:'录入人',
            	sortable:true,
            	dataIndex:'assignName'
            },
            {
            	header:'创建日期',
            	sortable:true,
            	dataIndex:'assignDate'
            },
            {
            	header:'分配时间',
            	sortable:true,
            	dataIndex:'assignTime'
            },
            {
            	header:'客户来源',
            	sortable:true,
            	dataIndex:'clientSource'
            },
            {
            	header:'签单可能性',
            	sortable:true,
            	dataIndex:'signPossible'
            },
            {
            	header:'备注',
            	sortable:true,
            	dataIndex:'remark'
            }
        ],
        listeners : {
		   'rowdblclick' : function(){
			    var record = searchGrid.getSelectionModel().getSelected(); 
			   	client_id = record.id;
			   	jump_from_flag = 3;//搜索客户跳转
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
				searchWindow.hide();
		 	}
		},
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: searchStore,
            displayInfo: true,
            plugins: new Ext.ux.ProgressBarPager(),
            displayMsg: '显示: {0} - {1} / 总数: {2}',
            emptyMsg: '没有记录'
        })
    }); 
    var searchWindow = new Ext.Window({
        width: '80%',
        height: 400,
        title:'客户信息',
        modal: true,
        layout: 'fit',
        plain: true,
        buttonAlign: 'center',
        items: searchGrid,
        closeAction: 'hide',
        buttons: [
        	{
               	text: '返回',
		        iconCls:'returns',
		        handler: function(){
		           searchWindow.hide();
		        }
             }
         ]
    });