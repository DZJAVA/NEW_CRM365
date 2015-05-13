	//-------------成单率------
   	var comboxStatecdl = new Ext.form.ComboBox({
        id: 'comboxStatecdl',
       	hiddenName: 'intoasinglerate',
        fieldLabel: '*成单率',
        triggerAction: 'all',
        allowBlank: false,
        editable: false,
        mode: 'local',
        store: signPossibleData,
 		valueField: 'key',
        displayField: 'value'
    });
    //-------------客户签约------
    var comboxType1 = new Ext.form.ComboBox({
       	id: 'comboxType1',
        name: 'types',
       	hiddenName: 'types',
        fieldLabel: '*上门状态',
        typeAhead: true,
        editable: false,
        allowBlank: false,
        triggerAction: 'all',
        lazyRender: true,
        mode: 'local',
        store: new Ext.data.ArrayStore({
            id: 'key',
            fields: [ 'key', 'value'],
            data: [
				[ '1', '已上门'],
           		[ '2', '未上门']
            ]
        }),
 		valueField: 'key',
        displayField: 'value',
        listeners:{
	      select:function(){
	      	var a = this.getValue();
			if(a==2){
			   Ext.getCmp('calltime').allowBlank = true;
			}else{
			   Ext.getCmp('calltime').allowBlank = false;
			}
		  }
		 }
    });
	// ------------------ form   新增跟踪记录 form -------------------------    
    var trackForm = new Ext.form.FormPanel({
        bodyStyle: 'padding:5px',
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
                        anchor: '90%',
                        msgTarget: 'side'
                    },
                    items: [
                        {
							inputType: 'hidden',
                            fieldLabel: '*编号',
                            id: 'rtid'
                        },
                        {
                            fieldLabel: '*跟踪内容',
                            allowBlank: false,
                            xtype:'textarea',
                            id: 'resourcescontent'
                        },
                        {
                        	xtype: 'datetimefield',
                        	format: 'Y-m-d H:i',
                            fieldLabel: '*计划时间',
                            allowBlank: false,
                            editable: false,
                            minValue:new Date(),
                            id: 'plantime'
                        },
                        {
                            fieldLabel: '*工作计划',
                            allowBlank: false,
                            xtype:'textarea',
                            id: 'workplan'
                        }
                       
                    ]
                },
                {
                    columnWidth: .5,
                    layout: 'form',
                    labelWidth: 80,
                    defaultType: 'textfield',
                    defaults: {
                        anchor: '90%',
                        msgTarget: 'side'
                    },
                    items: [
                    	comboxStatecdl,
                        comboxType1,
                        {
                        	xtype: 'datefield',
                        	format: 'Y-m-d',
                            fieldLabel: '*上门时间',
                            editable: false,
                            id: 'calltime'
                        },
                        {
                            fieldLabel: '备注',
                            xtype:'textarea',
                            allowBlank: true,
                            id: 'remark'
                        }
                    ]
                }
            ]
        }]
    });
	var trackWindow = new Ext.Window({
        width: 720,
        height: 300,
        title:'跟踪记录',
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: trackForm,
        closeAction: 'hide',
        buttons: [
            {
            	id: 'saveTrack',
            	text: '保存',
	        	iconCls: 'saves'
            },
            {
            	text: '返回',
		        iconCls: 'returns',
		        handler: function(){
		           trackWindow.hide();
		        }
            }
        ]
    });