<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
	<script type="text/javascript">
	Ext.onReady(function(){
		Ext.QuickTips.init();// 浮动信息提示
    	Ext.BLANK_IMAGE_URL = '<%=path%>/resources/images/default/s.gif';// 替换图片文件地址为本地
		
		 var departmentStore = new Ext.data.Store({
			          proxy: new Ext.data.HttpProxy({
			              url: '<%=path%>/employee/loadDepartment.do'
			          }),
			          reader: new Ext.data.JsonReader({
			                  root: 'data',
			                  id: 'departId'
			              }, 
			              ['departId', 'departName']
			          )
			  });
			  
			  var departComboBox = new Ext.form.ComboBox({
			      id: 'departComboBox',
			      hiddenName: 'dId',
				  allowBlank: false,
			      fieldLabel: '*所属楼盘',
			      typeAhead: true,
			      editable: false,
			      triggerAction: 'all',
			      lazyRender: true,
				  mode: 'remote',
			      store:departmentStore,
			      valueField: 'departId',
			      displayField: 'departName'
			  });
			  
		 var sexcomboxData = new Ext.data.SimpleStore({
		      fields:['key', 'value'],
		      data:[
		      		[ '1', '男'],
				    [ '2', '女']
		           ]
		     });
		     
		   // 下拉列表框控件
		   var sexcomboxType = new Ext.form.ComboBox({
		      fieldLabel : '*员工性别',
		      id : 'sexcomboxType',
		      store : sexcomboxData,
		      editable:false,
		      displayField : 'value', 
		      valueField : 'key', 
		      mode : 'local', 
		      triggerAction : 'all', 
		      emptyText : '请选择...', // 默认值   selectOnFocus : true,
		      hiddenName : 'sex'  
	     });
	     
	       var positionStore = new Ext.data.Store({
			          proxy: new Ext.data.HttpProxy({
			              url: '<%=path%>/employee/loadPosition.do'
			          }),
			          reader: new Ext.data.JsonReader({
			                  root: 'data',
			                  id: 'pId'
			              }, 
			              ['pId', 'pName']
			          )
			  });
			  
			  var posiComboBox = new Ext.form.ComboBox({
			      id: 'posiComboBox',
			      hiddenName: 'pId',
				  allowBlank: false,
			      fieldLabel: '*员工职位',
			      typeAhead: true,
			      editable: false,
			      triggerAction: 'all',
			      lazyRender: true,
				  mode: 'remote',
			      store:positionStore,
			      valueField: 'pId',
			      displayField: 'pName'
			  });
	     
			var form1ReturnAction = new Ext.Action({
		        text: '返回',
		        handler: function(){
		           document.location.href='<%=path%>/index.jsp';
		        }
		    });
		    
		     var myGridLoadAction = new Ext.Action({
		        text: '刷新',
		        iconCls:'btn_del',
		        scale:'small',
		        disabled: true,
		        handler: function(aMasterId){
		            masterStore.setBaseParam('masterId', aMasterId);
		            masterStore.load({
		                params:{
		                    start:0, 
		                    limit:20
		                },
		                waitTitle:'提示',waitMsg: '数据加载请稍后...',
		                failure: function() {
		                    Ext.Msg.alert('提示', '读取数据失败！');                             
		                }
		            });
		        }
		    }); 
		    
	     var form1SaveAction = new Ext.Action({
		        text: '保存',
		        handler: function(){
		        var dComboBox =Ext.getCmp('departComboBox').getValue(); 
		        var pComboBox =Ext.getCmp('posiComboBox').getValue(); 
		        var sComboBox =Ext.getCmp('sexcomboxType').getValue(); 
		        var uName = Ext.getCmp('loginId').getValue() 
		            if(form1.getForm().isValid()){
		                form1.getForm().submit({
		                    url: '<%=path%>/user/saveUser.do',
		                    params: {
			                    depart:dComboBox,
		                        posi:pComboBox,
		                        sex:sComboBox,
		                        usName:uName
		                    },
		                    waitTitle: '请等待',
		                    waitMsg: '正在处理数据...',
		                    timeout: 10,
		                    success: function(aForm, aAction){
		                        form1Window.hide();
	        						 document.location.href='<%=path%>/index.jsp';
	        						 Ext.MessageBox.alert('提示', aAction.result.msg);
		                    },
		                    	failure: function(aForm, aAction) {
	        						 Ext.MessageBox.alert('提示', aAction.result.msg);
		                    }
		                });
		            }
		        }
		    });
		    
		    
		var form1 = new Ext.form.FormPanel({
	        id: 'form1',
	        bodyStyle: 'padding:5px',
	        frame: true,
	        items: [{
	            layout: 'column',
	            items: [
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 80,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 180,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                        {
								inputType: 'hidden',
	                            fieldLabel: '*编号',
	                            id: 'id'
	                        },
	                        {
								fieldLabel: '*登录账号',
	                            allowBlank: false,
	                            id: 'loginId'
	                        },
	                        {
								fieldLabel: '*密码',
	                            allowBlank: false,
	                            id: 'password'
	                        },
	                        
	                        departComboBox
	                    ]
	                },
	                {
	                    columnWidth: .5,
	                    layout: 'form',
	                    labelWidth: 80,
	                    defaultType: 'textfield',
	                    defaults: {
	                        width: 180,
	                        msgTarget: 'side'
	                    },
	                    items: [
	                    	posiComboBox,
	                    	sexcomboxType,
	                    	{
	                            fieldLabel: '*电话',
	                            allowBlank: false,
	                            id: 'telPhone'
	                        }
	                    ]
	                }
	            ]
	        }]
	    });
		    
		    var form1Window = new Ext.Window({
		        title: '成都永发鑫乐鑫物业管理信息系统用户注册',
		        width: 700,
		        height: 250,
		        layout: 'fit',
		        plain:true,
		        bodyStyle:'padding:5px;',
		        buttonAlign:'center',
		        closable:false,
		        items: form1,
		        buttons: [
	                form1SaveAction,
	                form1ReturnAction
	            ]
		    });
		    
		form1Window.show();
		});
	</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>