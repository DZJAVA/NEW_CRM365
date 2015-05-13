<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
scode=function(){ 
	Ext.getDom("imgcode").src="<%=path%>/loginyanzhengma.do?id=" + Math.random(); 
} 
Ext.onReady(function() { 
	//加载提示框 
	Ext.QuickTips.init(); 
	
	var loginLogo = new Ext.Panel({ 
		id: 'loginLogo', 
		height: 60, 
		frame:true, 
		bodyStyle:'padding-top:4px', 
		html:"<img src='<%=path%>/image/top_img.png'/>"
		
	});

	var updateForm = new Ext.form.FormPanel({
	    id:'updateForm',
        width: 434,
        frame:true,  
        monitorValid:true,// 把有formBind:true的按钮和验证绑定  
        bodyStyle:'padding: 5px',
        keys:[{
            key: [10,13],
            fn:surely
        }],
        items:[{
	            xtype: 'container',
	            anchor: '100%',
	            layout: 'hbox',
            	items:[{
		            xtype: 'fieldset',
		            height:162,
		            labelWidth: 60,
            		items: [{
			                xtype     : 'textfield',
			                name      : 'loginId',
			                fieldLabel: '用户名',
			                allowBlank: false,
			                readOnly :true,
			                anchor    : '-20',
			                id        : 'uploginId'
            		},
            		{
			                xtype     : 'textfield',
			                name      : 'pwd',
			                allowBlank: false,
			                fieldLabel: '原始密码',
			                inputType : 'password',
			                id:'passwords',
			                anchor    : '-20',
			                id        :'pwd'
            		},
            		{
			                xtype     : 'textfield',
			                name      : 'password',
			                allowBlank: false,
			                fieldLabel: '修改密码',
			                inputType : 'password',
			                id:'passwords',
			                anchor    : '-20'
            		}],
              		buttons : [{  
				            text : '修该密码',  
				            handler:updateSurely,
				            formBind : true,//绑定表单验证  
				            type : 'submit' 
        			},{  
				            text : '重置',  
				            handler : function() { 
				            	loginForm.getForm().reset();
		        				scode();
        					}
        			}]
        		}]
       	   }]
	}); 	
	var loginForm = new Ext.form.FormPanel({
		id:'loginForm',
        width: 300,
        frame:true,  
        monitorValid:true,// 把有formBind:true的按钮和验证绑定  
        bodyStyle:'padding: 5px',
        keys:[{
            key: [10,13],
            fn: surely
        }],
        items:[{
	            xtype: 'container',
	            anchor: '100%',
	            layout: 'hbox',
            	items:[{
		            xtype: 'fieldset',
		            height:162,
		            labelWidth: 60,
            		items: [{
			                xtype     : 'textfield',
			                name      : 'loginId',
			                fieldLabel: '账号',
			                allowBlank: false,
			                anchor    : '-20',
			                id        : 'loginId'
            		},
            		{
			                xtype     : 'textfield',
			                name      : 'password',
			                allowBlank: false,
			                fieldLabel: '密码',
			                inputType : 'password',
			                id:'password',
			                anchor    : '-20'
            		},
            		{
			                xtype: 'compositefield',
			                fieldLabel: '验证码',
			                allowBlank: false,
			                msgTarget : 'side',
			                anchor    : '-20',
			                defaults: {
			                    flex: 1
			                },
                			items: [
			                    {
			                        xtype: 'textfield',
			                        name : 'remark',
			                        id:'remark',
			                        height:30,
			                        minLength:4,
			                        maxLength:4,
			                        allowBlank : false
			                    },
			                    { 
			        				xtype:"panel",
			        				width:80, 
			        				allowBlank : true,
			  						height:30,
			    					html:"<a href='javascript:scode()'><img src='<%=path%>/loginyanzhengma.do' id='imgcode' /></a>"
			    				}
                			]
            		}
            		],
              		buttons : [{  
				            text : '登录',  
				            iconCls:'logins',
		        			scale:'small',
				            handler:surely,
				            formBind : true,//绑定表单验证  
				            type : 'submit' 
        			}]
        		}]
       	   }]
    });
           
		
	var tabControl =new Ext.TabPanel({
		id:'tabControl',
        region: 'center',
        margins:'3 3 3 0', 
        activeTab: 0,
        defaults:{autoScroll:true},
        items:[{
            title: '登录',
            height:210,
            items:[loginForm]
        }]
    });
   function updateSurely(){
    	if(updateForm.getForm().isValid()){
    	var _pwd = Ext.getCmp('pwd').getValue();
    		updateForm.getForm().submit({
            	url: 'updataPwd.do',
            	waitTitle: '请等待',
                waitMsg: '正在登录...',
                timeout: 20000,
                params:{
                	_pwd: _pwd
                },
                success: function(aForm, aAction){
				   var result = Ext.decode(aAction.response.responseText);
                    Ext.Msg.alert('提示', result.msg);
					Ext.getCmp('tabControl').setActiveTab(0);
                },
                failure: function(aForm, aAction){
                	var result = Ext.decode(aAction.response.responseText);
                    Ext.Msg.alert('提示', result.msg);
                    Ext.getCmp('tabControl').setActiveTab(3); 
                	Ext.getCmp('password').reset();
                	Ext.getCmp('remark').reset();
            		scode();
                }
            });
		}
 	}
	function surely(){
    	if(loginForm.getForm().isValid()){
    		loginForm.getForm().submit({
            	url: 'login.do',
            	waitTitle: '请等待',
                waitMsg: '正在登录...',
                timeout: 5,
                success: function(aForm, aAction){
                  var result = Ext.decode(aAction.response.responseText);
                  if(result.msg!=null&&result.msg==2){
					this.document.location.href='<%=path%>/to_main?upPwd='+result.msg+"&loginName="+Ext.getCmp('loginId').getValue();
				  }else if(result.msg!=null&&result.msg==3){
					this.document.location.href='<%=path%>/to_main?upPwd='+result.msg+"&loginName="+Ext.getCmp('loginId').getValue();
				  }else{
					this.document.location.href='<%=path%>/to_main';
				  }
                },
                failure: function(aForm, aAction){
                	var result = Ext.decode(aAction.response.responseText);
                    Ext.Msg.alert('提示', result.msg);
                    if(result.msg==1){
                     Ext.Msg.confirm('确认', '该用户要修改密码,是否修改密码?', function(aButton){
	                    if (aButton == 'yes'){
			                Ext.getCmp('uploginId').setValue(Ext.getCmp('loginId').getValue());
		                    Ext.getCmp('tabControl').setActiveTab(3); 
	                    }
	                });
                    }
                	Ext.getCmp('password').reset();
                	Ext.getCmp('remark').reset();
            		scode();
                }
            });
		}
 	}
	
	var loginWindow = new Ext.Window({ 
			id: 'loginWindow', 
			Title: '登录', 
			width: 300, 
			height: 300, 
			closable: false, 
			collapsible: false, 
			resizable:false, 
			defaults: { 
				border: false 
			}, 
			layout: 'column', 
			items:[ 
				{ 
					columnWidth:1, 
					items:[loginLogo]
				}, 
				{ 
					columnWidth: 1, 
					items:[tabControl]
				}] 
		});
		
	loginWindow.show(); 
}); 
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>