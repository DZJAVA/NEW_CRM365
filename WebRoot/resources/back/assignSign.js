	var departmentAssignStore = new Ext.data.Store({
          proxy: new Ext.data.HttpProxy({
              url: path+'/client/loadDepartment.do?flag='+2
          }),
          reader: new Ext.data.JsonReader({
                  root: 'data',
                  id: 'departId'
              }, 
            ['departId', 'departName']
          )
	});
	//--------------部门下拉列表-----------------	  
  	var departComboBox = new Ext.form.ComboBox({
      	id: 'departComboBox',
	    fieldLabel: '部门',
	    emptyText : '请选择部门...',
	    typeAhead: true,
	    editable: false,
	    triggerAction: 'all',
		mode: 'remote',
	    store: departmentAssignStore,
	    valueField: 'departId',
	    displayField: 'departName',
	    listeners:{
	     	select: function(){
	      		employeeComboBOx.reset();
	      		employeeData.proxy = new Ext.data.HttpProxy({
	      			url: path+'/client/loadEmployees.do?eid=' + this.getValue()
	      		});
	      		employeeData.load();
	      	}
	    }
 	});
	 //--------员工下拉列表 
	var employeeData = new Ext.data.Store({
   		proxy: new Ext.data.HttpProxy({
	          url: path+'/client/loadEmployee.do?flag='+2
	    }),
	    reader: new Ext.data.JsonReader({
	           root: 'data',
	           id: 'eId'
    		}, 
          ['eId', 'eName']
	    )
 	});
 	var employeeComboBOx = new Ext.form.ComboBox({
     	   id: 'employeeComboBOx',
	       allowBlank: false,
	       fieldLabel: '*员工',
           typeAhead: true,
	       editable: false,
	       triggerAction: 'all',
		   mode: 'remote',
	       store: employeeData,
	       emptyText : '请选择员工...',
	       valueField: 'eId',
	       displayField: 'eName'
  	});
	//---------------手动分配form---------------
	var assignForm = new Ext.form.FormPanel({
		id: 'assignForm',
        bodyStyle: 'padding:5px',
        frame: true,
        items:[
        	{
        		layout: 'column',
        		items:[
        			{
        				columnWidth: .5,
        				layout: 'form',
        				labelWidth: 60,
        				defaults: {
        					anchor: '95%',
	                        msgTarget: 'side'
        				},
        				items:[
        					departComboBox
        				]
        			},
        			{
        				columnWidth: .5,
        				layout: 'form',
        				labelWidth: 75,
        				defaults: {
        					anchor: '95%',
        					msgTarget: 'side'
        				},
        				items:[
        					employeeComboBOx
        				]
        			}
        		]
        	}
        ]
	});
	//-------------手动分配客户信息window----------
    var assignWindow = new Ext.Window({
    	title: '手动分配',
        width: 500,
        height: 160,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: assignForm,
        closable: false,
        closeAction: 'hide',
        buttons: [
        	{
        		text: '分配',
	        	iconCls:'saves',
	        	id: 'assignAction'
        	},
            {
            	text: '返回',
		        iconCls:'returns',
		        handler: function(){
		            assignWindow.hide();
		        }
            }
        ]
    });