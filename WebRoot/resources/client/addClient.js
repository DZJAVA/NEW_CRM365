	
	//---------------商机类型下拉列表--------------------------
	var oppTypeCombox = new Ext.form.ComboBox({
	      fieldLabel : '商机类型',
	      id : 'oppTypeCombox',
	      store : oppTypeData,
	      editable:false,
	      allowBlank: true,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...', // 默认值   selectOnFocus : true,
	      hiddenName : 'oppType'  
    });
    var provinceCombox = new Ext.form.ComboBox({
	      id : 'provinceCombox',
	      store : provinceData,
	      fieldLabel: '省份',
	      displayField : 'value',
	      editable:false,
	      allowBlank: false,
	      valueField : 'key',
	      name: 'province',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '请选择省份...',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'select': function(){
	      		cityCombox.reset();
	      	}
	      }
    });
    var cityCombox = new Ext.form.ComboBox({
	      id : 'cityCombox',
	      store : cityData,
	      fieldLabel: '市区',
	      allowBlank: false,
	      editable:false,
	      displayField : 'value',
	      valueField : 'key',
	      name: 'city',
	      mode : 'local',
	      triggerAction : 'all',
	      emptyText : '请选择市区...',// 默认值   selectOnFocus : true,
	      listeners: {
	      	'expand': function(){
	      		var val = provinceCombox.getValue();
	      		if(val !== '' && val !== null){
	      			cityData.filterBy(function(rec, id){
	      				return rec.get('pro') == val;
		      		});
	      		}
	      	}
	      }
    });
    //---------------签单可能性下拉列表--------------------------
	var signPossibleCombox = new Ext.form.ComboBox({
	      fieldLabel : '*成单率',
	      id : 'signPossibleCombox',
	      store : signPossibleData,
	      editable:false,
	      allowBlank: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      emptyText : '请选择...',
	      hiddenName : 'signPossible'  
    });
    var clientSelComboBOx = new Ext.form.ComboBox({
		  id: 'clientSelComboBOx',
	      hiddenName: 'clientSourseId',
	      fieldLabel: '*客户来源',
	      typeAhead: true,
	      allowBlank: false,
	      editable: false,
	      triggerAction: 'all',
	      lazyRender: true,
		  mode: 'remote',
	      store: clientSelData,
	      valueField: 'client_id',
	      displayField: 'client_name'
	});
	// ------------------ clientForm -------------------------    
    var clientForm = new Ext.form.FormPanel({
        id: 'clientForm',
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
                            fieldLabel: '*编号',
                            id: 'id'
                        },
                        {
                        	xtype: 'datetimefield',
                        	format: 'Y-m-d H:i',
                            fieldLabel: '创建日期',
                            editable: false,
                            id: 'assignDate'
                        },
                        oppTypeCombox,
                        provinceCombox,
                        cityCombox,
                        {
                            fieldLabel: '*客户联系方式',
                            allowBlank: false,
                            id: 'contactTel',
                            regex:/^[0-9]*[1-9][0-9]*$/,
                            enableKeyEvents: true,
                            listeners: {
                            	'keyup': function(){
                            		var _con = Ext.getCmp('contactTel');
                            		var _value = _con.getValue();
                            		if(_value != ''){
                            			_value = _value.substring(0,1);
                            			if(_value == 1){
                            				_con.regex = /^\d{11}$/;
                            			}else{
                            				_con.regex = /^[0-9]*[1-9][0-9]*$/;
                            			}
                            		}
                            		
                            	}
                            }
                        },
                        signPossibleCombox
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
                            fieldLabel: '客户名称',
                            id: 'clientName'
                        },
                        {
                            fieldLabel: '贷款金额(万)',
                            id: 'loanAmount',
                            regex:/^[0-9].*$/
                        },
                        {
                            fieldLabel: '客户地址',
                            id: 'clientAdd'
                        },
                        {
                            fieldLabel: '备用电话1',
                            id: 'spareTel1',
                            enableKeyEvents: true,
                            regex:/^[0-9]*[1-9][0-9]*$/,
                            listeners: {
                            	'keyup': function(){
                            		var _con = Ext.getCmp('spareTel1');
                            		var _value = _con.getValue();
                            		if(_value != ''){
                            			_value = _value.substring(0,1);
                            			if(_value == 1){
                            				_con.regex = /^\d{11}$/;
                            			}else{
                            				_con.regex = /^[0-9]*[1-9][0-9]*$/;
                            			}
                            		}
                            		
                            	}
                            }
                        },
                        {
                            fieldLabel: '备用电话2',
                            id: 'spareTel2',
                            enableKeyEvents: true,
                            regex:/^[0-9]*[1-9][0-9]*$/,
                            listeners: {
                            	'keyup': function(){
                            		var _con = Ext.getCmp('spareTel2');
                            		var _value = _con.getValue();
                            		if(_value != ''){
                            			_value = _value.substring(0,1);
                            			if(_value == 1){
                            				_con.regex = /^\d{11}$/;
                            			}else{
                            				_con.regex = /^[0-9]*[1-9][0-9]*$/;
                            			}
                            		}
                            	}
                            }
                        },
                        clientSelComboBOx
                    ]
                }
            ]
        },
        {
           	xtype: 'textarea',
            fieldLabel: '备注',
            anchor: '98%',
            id: 'remarkcs',
            name:'remark'
         }
       ]
    });
	var formClientWindow = new Ext.Window({
    	title: '新增客户信息',
        width: 650,
        height: 350,
        modal: true,
        layout: 'fit',
        plain: true,
        bodyStyle: 'padding:5px;',
        buttonAlign: 'center',
        items: clientForm,
        closable: true,
        closeAction: 'hide',
        listeners:{
        	"hide" : function(){
        		cityData.clearFilter();
        	}
        },
        buttons: [
            {
            	id: 'saveClient',
            	text: '保存',
        		iconCls:'saves'
            },
        	{
           		text: '返回',
	        	iconCls:'returns',
	       	 	handler: function(){
	           		formClientWindow.hide();
	        	}
            }
       	]
    });