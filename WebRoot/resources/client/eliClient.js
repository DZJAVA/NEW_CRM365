	var eliWindow = new Ext.Window({
        width: 400,
        height: 160,
        title:'淘汰资源',
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: [
        	{
        		xtype: 'form',
        		id: 'eliForm',
		        bodyStyle: 'padding:5px',
		        frame: true,
		        layout: 'form',
		        labelWidth: 70,
	            items: [
		           {
		               fieldLabel: '淘汰备注',
		               xtype:'textarea',
		               anchor: '95%',
		               id: 'eliRemark'
		           }
	            ]
        	}
        ],
        closeAction: 'hide',
        buttons: [
       		{
              	text: '确定',
	        	iconCls: 'saves',
	        	id: 'eliSure'
            },
            {
             	text: '返回',
		        iconCls: 'returns',
		        handler: function(){
           			eliWindow.hide();
        		}
            }
       	]
    });