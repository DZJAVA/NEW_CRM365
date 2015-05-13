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
	
	Ext.apply(Ext.form.VTypes, {
	    idCard : function(pId){  
	            var arrVerifyCode = [1,0,"x",9,8,7,6,5,4,3,2];  
	            var Wi = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];  
	            var Checker = [1,9,8,7,6,5,4,3,2,1,1];  
	            if(pId.length != 15 && pId.length != 18){  
	                return false;  
	            }  
	            var Ai=pId.length==18 ?  pId.substring(0,17)   :   pId.slice(0,6)+"19"+pId.slice(6,16);  
	            if (!/^\d+$/.test(Ai)){  
	                return false;  
	            }  
	            var yyyy=Ai.slice(6,10) ,  mm=Ai.slice(10,12)-1  ,  dd=Ai.slice(12,14);  
	            var d=new Date(yyyy,mm,dd) ,  now=new Date();  
	             var year=d.getFullYear() ,  mon=d.getMonth() , day=d.getDate();  
	            if (year!=yyyy || mon!=mm || day!=dd || d>now || year<1940){  
	                return false;  
	            }  
	            for(var i=0,ret=0;i<17;i++)  ret+=Ai.charAt(i)*Wi[i];      
	            Ai+=arrVerifyCode[ret %=11];       
	            return pId.length ==18 && pId != Ai?false:true;      
	    },  
	    idCardText : '身份证格式错误'
	});  
		
	var forgotPasswordFrom = new Ext.form.FormPanel({
		xtype:'fieldset',
		monitorValid : true,// 把有formBind:true的按钮和验证绑定  
        width:435,
        frame:true,  
        bodyStyle:'padding: 5px',
        items:[{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
	            xtype: 'fieldset',
	            height:162,
	            labelWidth: 60,
            	items: [
            {
                xtype     : 'textfield',
                name      : 'loginId',
                allowBlank : false,
                fieldLabel: '用户名',
                anchor    : '-20'
            },
            {
                xtype     : 'textfield',
                allowBlank: false,
                fieldLabel: '身份证',
                vtype: 'idCard',  
        		name: 'idCard',  
        		id: 'idCard',  
                anchor    : '-20'
            },
            {
                xtype     : 'textfield',
                name      : 'idCarddd',
                allowBlank: false,
                height:23,
                fieldLabel: '邮箱',
                inputType : 'email',
                anchor    : '-20'
            }],
          	buttons : [{  
		            text : '确定',  
		            formBind : true,//绑定表单验证  
		            handler:forgotPassword,
		            type : 'submit' 
        		},{  
		            text : '重置',  
		            handler : function() { 
		            	forgotPasswordFrom.getForm().reset();
        			}
        	}]
        	}]
    	}]
    });
		
	var loginForm = new Ext.form.FormPanel({
		id:'loginForm',
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
			                anchor    : '-20'
            		},
            		{
			                xtype     : 'textfield',
			                name      : 'password',
			                allowBlank: false,
			                fieldLabel: '密码1',
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
			        				allowBlank : false,
			  						height:30,
			    					html:"<a href='javascript:scode()'><img src='<%=path%>/loginyanzhengma.do' id='imgcode' /></a>"
			    				}
                			]
            		}
            		],
              		buttons : [{  
				            text : '登录',  
				            handler: surely,
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
        },{
            title: '找回密码',
            height:210, 
            items:[forgotPasswordFrom]
        }]
    });
    
	function surely(){
    	if(loginForm.getForm().isValid()){
    		loginForm.getForm().submit({
            	url: 'login.do',
            	waitTitle: '请等待',
                waitMsg: '正在登录...',
                timeout: 5,
                success: function(aForm, aAction){
					this.document.location.href='<%=path%>/to_main';
                },
                failure: function(aForm, aAction){
                	var result = Ext.decode(aAction.response.responseText);
                    Ext.Msg.alert('提示', result.msg);  
                	Ext.getCmp('password').reset();
                	Ext.getCmp('remark').reset();
            		scode();
                }
            });
		}
 	}
 	
 	function forgotPassword(){
 		if(forgotPasswordFrom.getForm().isValid()){
    		forgotPasswordFrom.getForm().submit({
            	url: 'login.do',
            	waitTitle: '请等待',
                waitMsg: '正在登录...',
                timeout: 5,
                success: function(aForm, aAction){
                },
                failure: function(aForm, aAction){
                	
                }
            });
		}
 	}
	
	var loginWindow = new Ext.Window({ 
			id: 'loginWindow', 
			Title: '登录', 
			width: 450, 
			height: 280, 
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