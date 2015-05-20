	var interestData = new Ext.data.SimpleStore({
      	fields:['key', 'value'],
      	data:[
      		[ 1, '等额本息'],
		    [ 2, '先息后本']
      	]
	});
	var interestCombox = new Ext.form.ComboBox({
	      fieldLabel : '*利息类型',
	      store : interestData,
	      editable: false,
	      allowBlank: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      hiddenName : 'interestType'
    });
	// ------------------ clientForm -------------------------    
    var makeloanForm = new Ext.form.FormPanel({
        id: 'makeloanForm',
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
                        	xtype: 'datefield',
                        	format: 'Y-m-d',
                            fieldLabel: '*放款时间',
                            allowBlank: false,
                            editable: false,
                            name: 'loanDate'
                        },
                        {
                            fieldLabel: '*服务费',
                            allowBlank: false,
                            name: 'serviceFee'
                        },
                        {
                        	fieldLabel: '*放款利息',
                        	allowBlank: false,
                        	name: 'loanInterest'
                        },
                        {
                        	fieldLabel: '收款金额',
                        	name: 'receiveAmount'
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
                            fieldLabel: '*放款年限',
                            allowBlank: false,
                            name: 'loanYear'
                        },
                        {
                            fieldLabel: '*贷款金额(万)',
                            allowBlank: false,
                            name: 'loanAmount'
                        },
                        interestCombox
                    ]
                }
            ]
        }																			
	]
    });
	var makeloanWindow = new Ext.Window({
    	title: '放款',
        width: 600,
        height: 300,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: makeloanForm,
        closable: true,
        closeAction: 'hide',
        buttons: [
            {
            	id: 'makeloanBtn',
            	text: '保存',
        		iconCls:'saves'
            },
        	{
           		text: '返回',
	        	iconCls:'returns',
	       	 	handler: function(){
	           		makeloanWindow.hide();
	        	}
            }
       	]
    });