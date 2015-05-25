	// ------------------ clientForm -------------------------    
    var signForm = new Ext.form.FormPanel({
        id: 'signForm',
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
                        	xtype: 'datefield',
                        	format: 'Y-m-d',
                            fieldLabel: '*报单日期',
                            allowBlank: false,
                            editable: false,
                            name: 'signDate'
                        },
                        {
                            fieldLabel: '贷款品种',
                            name: 'loanType'
                        },
                        {
                        	fieldLabel: '来源渠道',
                        	name: 'loanSource'
                        }
                    ]
                },
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
                            fieldLabel: '委托编码',
                            name: 'clientCode'
                        },
                        {
                            fieldLabel: '*贷款金额(万)',
                            allowBlank: false,
                            name: 'loanAmount'
                        }
                    ]
                }
            ]
        },
        {
           	xtype: 'textarea',
            fieldLabel: '报单银行',
            anchor: '98%',
            name:'loanBank'
       	}
	]
    });
	var signWindow = new Ext.Window({
    	title: '签单信息',
        width: 650,
        height: 300,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: signForm,
        closable: true,
        closeAction: 'hide',
        buttons: [
            {
            	id: 'saveSign',
            	text: '保存',
        		iconCls:'saves'
            },
        	{
           		text: '返回',
	        	iconCls:'returns',
	       	 	handler: function(){
	           		signWindow.hide();
	        	}
            }
       	]
    });