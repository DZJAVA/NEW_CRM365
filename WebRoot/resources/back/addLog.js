    var logForm = new Ext.form.FormPanel({
        id: 'logForm',
        bodyStyle: 'padding:5px',
        labelAlign: 'right',
        labelWidth: 90,
        frame: true,
        items: [
        	{
        		xtype: 'textfield',
				inputType: 'hidden',
                name: 'id'
            },
	        {
	        	fieldLabel: '*跟踪日志',
                allowBlank: false,
                anchor: '95%',
                xtype: 'textarea',
                name: 'logInfo'
	        },
	        {
	        	fieldLabel: '跟踪时间',
	        	xtype: 'datetimefield',
	        	format: 'Y-m-d H:i',
	        	anchor: '95%',
                allowBlank: false,
                editable: false,
                name: 'logDate'
	        }
       	]
    });
	var logWindow = new Ext.Window({
    	title: '新增渠道日志',
        width: 450,
        height: 200,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: logForm,
        closable: true,
        closeAction: 'hide',
        buttons: [
            {
            	id: 'saveSourceLog',
            	text: '保存',
        		iconCls:'saves'
            },
        	{
           		text: '返回',
	        	iconCls:'returns',
	       	 	handler: function(){
	           		logWindow.hide();
	        	}
            }
       	]
    });