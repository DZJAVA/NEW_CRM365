	//--------查询商机类型下拉列表-------------------
	var signOppType = new Ext.form.ComboBox({
	      fieldLabel : '*商机类型',
	      id : 'signOppType',
	      store : oppTypeData,
	      width: 90,
	      editable: false,
	      allowBlank: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...', // 默认值   selectOnFocus : true,
	      hiddenName : 'oppType'  
    });
	var signForm = new Ext.form.FormPanel({
        id: 'signForm',
        bodyStyle: 'padding:5px',
        frame: true,
        layout: 'form',
        defaults: {
        	anchor: '95%'
        },
        items: [
			signOppType,
 			{
  				fieldLabel: '*贷款金额(万)',
   				allowBlank: false,
   				xtype: 'textfield',
  				id: 'loanAmount11'
  			},
  			{
	  			fieldLabel: '签单备注',
				xtype: 'textarea',
				id: 'signRemark'
			}
        ]
    });
	var signWindow = new Ext.Window({
    	title: '签单',
        width: 300,
        height: 230,
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
   				text: '签单',
	        	iconCls:'saves',
	        	id: 'signAction'
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