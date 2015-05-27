	Ext.override(Ext.form.CheckboxGroup,{ 
		//在inputValue中找到定义的内容后，设置到items里的各个checkbox中 
		setValue : function(value){
			var flag = true;
			if(!value) flag = false;
			if(!flag){
				this.items.each(function(f){
					f.setValue(false);
				});
			}else{
				var vals = value.split(',');
				var len = vals.length;
				var check = 0;
				this.items.each(function(f){
					check = f.inputValue;
					for(var i = 0; i < len; i++){
						if(vals[i] == check){
							flag = false;
							break;
						}
					}
					if(flag){
						f.setValue(false);
					}else{
						f.setValue(true);
					}
					flag = true;
				});
			}
		},
		//以value1,value2的形式拼接group内的值
		getValue : function(){
			var re = "";
			this.items.each(function(f){
				if(f.getValue()){
					re += f.inputValue + ",";
				}
			});
			return re.substr(0,re.length - 1);
		},
		//在Field类中定义的getName方法不符合CheckBoxGroup中默认的定义，因此需要重写该方法使其可以被BasicForm找到
		getName : function(){
			return this.name;
		}
	});
	var statusData = new Ext.data.SimpleStore({
      	fields:['key', 'value'],
      	data:[
      		[ 0, '资料未齐'],
      		[ 1, '资料已齐'],
		    [ 2, '进件'],
		    [ 3, '退单']
      	]
	});
	var statusCombox = new Ext.form.ComboBox({
		id: 'statusCombox',
	      fieldLabel : '状态',
	      store : statusData,
	      editable: false,
	      displayField : 'value', 
	      valueField : 'key', 
	      mode : 'local', 
	      triggerAction : 'all', 
	      hiddenName : 'status',
	      listeners: {
	      	'select': function(combo, rec){
	      		var val = rec.get('key');
	      		if(val){
	      			signForm.find('name', 'unCommitData')[0].setValue('');
	      		}
	      	}
	      }
    });
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
                        statusCombox,
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
                        },
                        {
                        	fieldLabel: '未提交资料',
                        	name: 'unCommitData'
                        }
                    ]
                }
            ]
        },
        {  
	      	xtype: 'checkboxgroup',
      		name: 'dataList',  
	      	columns: 5,
	      	fieldLabel: '资料清单',
	      	items: [
	          {boxLabel: '身份证', inputValue: 1},  
	          {boxLabel: '结婚证', inputValue: 2},  
	          {boxLabel: '购房合同', inputValue: 3},  
	          {boxLabel: '借款合同', inputValue: 4},
	          {boxLabel: '房产证', inputValue: 5},
	          {boxLabel: '还款流水', inputValue: 6},
	          {boxLabel: '收入证明', inputValue: 7},
	          {boxLabel: '居住证明', inputValue: 8},
	          {boxLabel: '车辆行驶证', inputValue: 9},
	          {boxLabel: '车辆登记证', inputValue: 10},
	          {boxLabel: '交强险', inputValue: 11},
	          {boxLabel: '商业险', inputValue: 12},
	          {boxLabel: '车辆备用钥匙', inputValue: 13},
	          {boxLabel: '营业执照副本', inputValue: 14},
	          {boxLabel: '组织机构代码', inputValue: 15},
	          {boxLabel: '税务登记证', inputValue: 16},
	          {boxLabel: '公司章程', inputValue: 17},
	          {boxLabel: '开户许可证', inputValue: 18},
	          {boxLabel: '购销单据', inputValue: 19},
	          {boxLabel: '个人流水', inputValue: 20},
	          {boxLabel: '对公流水', inputValue: 21}
          	]  
	    },
	    {
           	xtype: 'textfield',
            fieldLabel: '备注资料',
            anchor: '98%',
            name:'dataField'
       	},
        {
           	xtype: 'textarea',
            fieldLabel: '报单银行',
            anchor: '98%',
            name:'loanBank'
       	},
       	{
           	xtype: 'textarea',
            fieldLabel: '前后台接单情况',
            anchor: '98%',
            name:'followInfo'
       	}
	]
    });
	var signWindow = new Ext.Window({
    	title: '编辑签单信息',
        width: 750,
        height: 500,
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