	//-------------导入客户信息window----------
    var importWindow = new Ext.Window({
    	title: '导入客户信息',
        width: 350,
        height: 130,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: [
        	{
        		xtype: 'form',
        		id: 'importForm',
		        bodyStyle: 'padding:5px',
		        labelWidth: 60,
		        frame: true,
		        layout: 'fit',
		        fileUpload: true,
		        items:[
		        	{
	                    xtype: 'fileuploadfield',
			            name:'uploadFilePath',
			            id: 'uploadFilePath',
			            bodyStyle:'padding:0 0 4px 0',
			            width:250,
			            height: 18,
			            emptyText: '请选择要导入的Excel',
			            buttonText: '选择'
	                }
		        ]
        	}
        ],
        closable: false,
        closeAction: 'hide',
        buttons: [
        	{
        		id: 'importAction',
        		text: '导入',
        		iconCls:'saves'
        	},
            {
            	text: '返回',
		        iconCls:'returns',
		        handler: function(){
		        	importWindow.hide();
		        }
            }
        ]
    });