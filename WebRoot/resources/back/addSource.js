	// ------------------ clientForm -------------------------    
    var sourceForm = new Ext.form.FormPanel({
        id: 'sourceForm',
        bodyStyle: 'padding:5px',
        labelAlign: 'right',
        labelWidth: 90,
        frame: true,
        items: [{
            layout: 'column',
            items: [
                {
                    columnWidth: .5,
                    layout: 'form',
                    labelWidth: 90,
                    defaultType: 'textfield',
                    defaults: {
                        anchor: '95%'
                    },
                    items: [
                        {
							inputType: 'hidden',
                            name: 'id'
                        },
                        {
                            fieldLabel: '*渠道名称',
                            allowBlank: false,
                            name: 'sourceName'
                        },
                    	{
                            fieldLabel: '服务费',
                            name: 'serviceFee'
                        }
                    ]
                },
                {
                    columnWidth: .5,
                    layout: 'form',
                    labelWidth: 80,
                    defaultType: 'textfield',
                    defaults: {
                        anchor: '95%'
                    },
                    items: [
                    	{
                            fieldLabel: '*渠道金额',
                            allowBlank: false,
                            name: 'sourceAmount'
                        },
                    	{
                            fieldLabel: '收款金额',
                            name: 'receiveAmount'
                        }
                    ]
                }
            ]
        }
       ]
    });
	var sourceWindow = new Ext.Window({
    	title: '新增渠道信息',
        width: 450,
        height: 250,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: clientForm,
        closable: true,
        closeAction: 'hide',
        buttons: [
            {
            	id: 'saveSource',
            	text: '保存',
        		iconCls:'saves'
            },
        	{
           		text: '返回',
	        	iconCls:'returns',
	       	 	handler: function(){
	           		sourceWindow.hide();
	        	}
            }
       	]
    });