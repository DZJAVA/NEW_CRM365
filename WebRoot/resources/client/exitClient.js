	var exitForm = new Ext.form.FormPanel({
        id: 'exitForm',
        bodyStyle: 'padding:5px',
        frame: true,
        layout: 'form',
        items: [
            {
                fieldLabel: '备注',
                anchor: '95%',
                xtype:'textarea',
                id: 'exitRemark'
            }
        ]
    });
	var exitWindow = new Ext.Window({
    	title: '退单',
        width: 400,
        height: 180,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: exitForm,
        closable: true,
        closeAction: 'hide',
        buttons: [
       		{text: '退单',iconCls:'saves',id: 'exitClient'},
               {
               	text: '返回',iconCls:'returns',
		        handler: function(){
		           exitWindow.hide();
        		}
               }
           ]
    });
	    