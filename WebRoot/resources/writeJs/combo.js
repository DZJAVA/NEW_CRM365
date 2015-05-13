		//--------------跟踪记录部门下拉列表-----------------	  
		var resouComboBox = new Ext.form.ComboBox({
		      id: 'resouComboBox',
			  width:80,
		      emptyText : '部门...',
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
		      lazyRender: true,
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'departId',
		      displayField: 'departName',
		      listeners:{
		      	select:function(){
		      		resouEmpComboBOx.reset();
		      		employeeData.proxy = new Ext.data.HttpProxy({
		      			url: path+'/client/loadEmployees.do?eid=' + resouComboBox.getValue()
		      		});
		      		employeeData.load();
		      	}
		      }
		  });
		 var resouEmpComboBOx = new Ext.form.ComboBox({
	     	   id: 'resouEmpComboBOx',
		       width:80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
			   mode: 'remote',
		       store: employeeData,
		       emptyText : '员工...',
		       valueField: 'eId',
		       displayField: 'eName'
		  });
		//--------------部门下拉列表-----------------	  
		var clientDepComboBox = new Ext.form.ComboBox({
		      id: 'clientDepComboBox',
			  width:80,
		      emptyText : '部门...',
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'departId',
		      displayField: 'departName',
		      listeners:{
		      	select:function(){
		      		clientEmpComboBOx.reset();
		      		employeeData.proxy = new Ext.data.HttpProxy({
		      			url: path+'/client/loadEmployees.do?eid=' + clientDepComboBox.getValue()
		      		});
		      		employeeData.load();
		      	}
		      }
		  });
			  
		 var clientEmpComboBOx = new Ext.form.ComboBox({
	     	   id: 'clientEmpComboBOx',
		       width:80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
			   mode: 'remote',
		       store: employeeData,
		       emptyText : '员工...',
		       valueField: 'eId',
		       displayField: 'eName'
		  });
		//--------------已签单商机部门下拉列表-----------------	  
		var signedDepComboBox = new Ext.form.ComboBox({
		      id: 'signedDepComboBox',
			  width:80,
			  emptyText : '部门...',
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'departId',
		      displayField: 'departName',
		      listeners:{
		      	select:function(){
		      		signedEmpComboBOx.reset();
		      		employeeData.proxy = new Ext.data.HttpProxy({
		      			url: path+'/client/loadEmployees.do?eid=' + signedDepComboBox.getValue()
		      		});
		      		employeeData.load();
		      	}
		      }
		  });
		 var signedEmpComboBOx = new Ext.form.ComboBox({
	     	   id: 'signedEmpComboBOx',
		       width:80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
			   mode: 'remote',
		       store: employeeData,
		       emptyText : '员工...',
		       valueField: 'eId',
		       displayField: 'eName'
		  });
		//--------------还款部门下拉列表-----------------	  
		var refundDepComboBox = new Ext.form.ComboBox({
		      id: 'refundDepComboBox',
			  width:80,
			  emptyText : '部门...',
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'departId',
		      displayField: 'departName',
		      listeners:{
		      	select:function(){
		      		refundEmpComboBOx.reset();
		      		employeeData.proxy = new Ext.data.HttpProxy({
		      			url: path+'/client/loadEmployees.do?eid=' + refundDepComboBox.getValue()
		      		});
		      		employeeData.load();
		      	}
		      }
		  });
		//还款员工数据源  
		 var refundEmpComboBOx = new Ext.form.ComboBox({
	     	   id: 'refundEmpComboBOx',
		       width:80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
			   mode: 'remote',
		       store: employeeData,
		       emptyText : '员工...',
		       valueField: 'eId',
		       displayField: 'eName'
		  });
//--------查询商机类型下拉列表-------------------
		var oppTypeSelCombox = new Ext.form.ComboBox({
		      id : 'oppTypeSelCombox',
		      store : oppTypeData,
		      width: 70,
		      editable: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '商机...', // 默认值   selectOnFocus : true,
		      hiddenName : 'oppType'  
	    });
		var oppTypeSelCombox1 = new Ext.form.ComboBox({
		      id : 'oppTypeSelCombox1',
		      store : oppTypeData,
		      width: 70,
		      editable: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '商机...', // 默认值   selectOnFocus : true,
		      hiddenName : 'oppType'  
	    });
		var oppTypeSelCombox2 = new Ext.form.ComboBox({
		      id : 'oppTypeSelCombox2',
		      store : oppTypeData,
		      width: 70,
		      editable: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '商机...', // 默认值   selectOnFocus : true,
		      hiddenName : 'oppType'  
	    });
		var oppTypeSelCombox3 = new Ext.form.ComboBox({
		      id : 'oppTypeSelCombox3',
		      store : oppTypeData,
		      width: 70,
		      editable: false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '商机...', // 默认值   selectOnFocus : true,
		      hiddenName : 'oppType'  
	    });
	    var departSelComboBox = new Ext.form.ComboBox({
		      id: 'departSelComboBox',
		      emptyText : '请选择跟踪部门...',
		      width: 120,
		      typeAhead: true,
		      editable: false,
		      triggerAction: 'all',
		      lazyRender: true,
			  mode: 'remote',
		      store: departmentStore,
		      valueField: 'departId',
		      displayField: 'departName',
		      listeners:{
		      	select:function(){
		      		employeeData.reset();
		      		employeeData.proxy = new Ext.data.HttpProxy({
		      			url: path+'/client/loadEmployees.do?eid=' + departSelComboBox.getValue()
		      		});
		      		employeeData.load();
		      	}
		      }
		  });
		 var employeeSelComboBOx = new Ext.form.ComboBox({
	     	   id: 'employeeSelComboBOx',
		       width: 120,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: employeeData,
		       emptyText : '请选择跟踪人...',
		       valueField: 'eId',
		       displayField: 'eName'
		});
		 var clientSelComboBOx1 = new Ext.form.ComboBox({
		 	   fieldLabel : '客户来源',
	     	   id: 'clientSelComboBOx1',
		       width: 120,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: clientSelData,
		       emptyText : '请选择客户来源...',
		       valueField: 'client_id',
		       displayField: 'client_name'
		});
		var clientSelComboBOx2 = new Ext.form.ComboBox({
		 	   fieldLabel : '客户来源',
	     	   id: 'clientSelComboBOx2',
		       allowBlank: true,
		       width: 80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: clientSelData,
		       emptyText : '客户来源...',
		       valueField: 'client_id',
		       displayField: 'client_name'
		});
		 var clientSelComboBOx3 = new Ext.form.ComboBox({
		 	   fieldLabel : '客户来源',
	     	   id: 'clientSelComboBOx3',
		       allowBlank: true,
		       width: 80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: clientSelData,
		       emptyText : '客户来源...',
		       valueField: 'client_id',
		       displayField: 'client_name'
		});
		 var clientSelComboBOx4 = new Ext.form.ComboBox({
		 	   fieldLabel : '客户来源',
	     	   id: 'clientSelComboBOx4',
		       allowBlank: true,
		       width: 80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: clientSelData,
		       emptyText : '客户来源...',
		       valueField: 'client_id',
		       displayField: 'client_name'
		});
		 var clientSelComboBOx5 = new Ext.form.ComboBox({
		 	   fieldLabel : '客户来源',
	     	   id: 'clientSelComboBOx5',
		       allowBlank: true,
		       width: 80,
	           typeAhead: true,
		       editable: false,
		       triggerAction: 'all',
		       lazyRender: true,
			   mode: 'remote',
		       store: clientSelData,
		       emptyText : '客户来源...',
		       valueField: 'client_id',
		       displayField: 'client_name'
		});
		 //--------------省-------市
	  var cityCombox1 = new Ext.form.ComboBox({
	      id : 'cityCombox1',
	      store : cityData,
	      width: 100,
	      editable:false,
	      displayField : 'value',
	      valueField : 'key',
	      name: 'city',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '市区...',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'expand': function(){
	      		var val = provinceCombox1.getValue();
	      		if(val != '' && val != null){
	      			cityData.filterBy(function(rec, id){
	      				return rec.get('pro') == val;
		      		});
	      		}
	      	}
	      }
    });
	var provinceCombox1 = new Ext.form.ComboBox({
	      id : 'provinceCombox1',
	      store : provinceData,
	      width: 90,
	      displayField : 'value',
	       editable:false,
	      valueField : 'key',
	      name: 'province',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '省份...',
	      listeners: {
	      	'select': function(){
	      		cityCombox1.reset();
	      	}
	      }
    });
 	    //--------------省-------市
	   var cityCombox2 = new Ext.form.ComboBox({
	       id : 'cityCombox2',
	       store : cityData,
	        width: 80,
	      editable:false,
	      displayField : 'value',
	      valueField : 'key',
	      name: 'city',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '市区',
	      listeners: {
	      	'expand': function(){
	      		var val = provinceCombox2.getValue();
	      		if(val != '' && val != null){
	      			cityData.filterBy(function(rec, id){
	      				return rec.get('pro') == val;
		      		});
	      		}
	      	}
	      }
    });
	var provinceCombox2 = new Ext.form.ComboBox({
	      id : 'provinceCombox2',
	      store : provinceData,
	        width: 80,
	      displayField : 'value',
	       editable:false,
	      valueField : 'key',
	      name: 'province',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '省份',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'select': function(){
	      		cityCombox2.reset();
	      	}
	      }
    });
   	    //--------------省-------市
	   var cityCombox3 = new Ext.form.ComboBox({
	      id : 'cityCombox3',
	      store : cityData,
	        width: 80,
	      editable:false,
	      displayField : 'value',
	      valueField : 'key',
	      name: 'city',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '市区',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'expand': function(){
	      		var val = provinceCombox3.getValue();
	      		if(val != '' && val != null){
	      			cityData.filterBy(function(rec, id){
	      				return rec.get('pro') == val;
		      		});
	      		}
	      	}
	      }
    });
	var provinceCombox3 = new Ext.form.ComboBox({
	      id : 'provinceCombox3',
	      store : provinceData,
	      width: 80,
	      displayField : 'value',
	      editable:false,
	      valueField : 'key',
	      name: 'province',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '省份',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'select': function(){
	      		cityCombox3.reset();
	      	}
	      }
    });
    // 选择查询条件的重置
	     var reset1 = new Ext.Action({
	        text: '重置',
	        iconCls:'btn_del',
	        scale:'small',
	        disabled: false,
	        handler: function(aMasterId){
        		  Ext.getCmp('resouComboBox').setValue('');
				  Ext.getCmp('resouEmpComboBOx').setValue('');
				  Ext.getCmp('oppTypeSelCombox').setValue('');
				  Ext.getCmp('_plantime').setValue('');
				  Ext.getCmp('clientSelComboBOx2').setValue('');
	        }
	    });  
	     var reset2 = new Ext.Action({
	        text: '重置',
	        iconCls:'btn_del',
	        handler: function(aMasterId){
		        Ext.getCmp('clientDepComboBox').setValue('');
		        Ext.getCmp('clientEmpComboBOx').setValue('');
		        Ext.getCmp('newTime').setValue('');
		        Ext.getCmp('oppTypeSelCombox1').setValue('');
	         	Ext.getCmp('clientSelComboBOx3').setValue('');
	         	 Ext.getCmp('provinceCombox1').setValue('');
	         	Ext.getCmp('cityCombox1').setValue('')
	        }
	    });  
	     var reset3 = new Ext.Action({
	        text: '重置',
	        iconCls:'btn_del',
	        handler: function(aMasterId){
        		  Ext.getCmp('refundDepComboBox').setValue('');
				  Ext.getCmp('refundEmpComboBOx').setValue('');
				  Ext.getCmp('oppTypeSelCombox3').setValue('');
				  Ext.getCmp('clientSelComboBOx5').setValue('');
				  Ext.getCmp('provinceCombox2').setValue('');
	         	 Ext.getCmp('cityCombox2').setValue('')
	        }
	    });  
	    var reset4 = new Ext.Action({
	        text: '重置',
	        iconCls:'btn_del',
	        handler: function(aMasterId){
     			  Ext.getCmp('signedDepComboBox').setValue('');
				  Ext.getCmp('signedEmpComboBOx').setValue('');
				  Ext.getCmp('qdtime').setValue('');
				  Ext.getCmp('oppTypeSelCombox2').setValue();
				  Ext.getCmp('clientSelComboBOx4').setValue();
				  Ext.getCmp('provinceCombox3').setValue('');
	         	  Ext.getCmp('cityCombox3').setValue('')
	        }
	    });  