var cityData = new Ext.data.SimpleStore({
	      fields:['value', 'key', 'pro'],
	      data:[
     		['东城','001','01'],['西城','002','01'],['崇文','003','01'],['宣武','004','01'],
     		['朝阳','005','01'],['丰台','006','01'],['石景山','007','01'],['海淀','008','01'],['门头沟','009','01'],
     		['房山','010','01'],['通州','011','01'],['顺义','012','01'],['昌平','013','01'],['大兴','014','01'],
     		['怀柔','015','01'],['平谷','016','01'],['密云','017','01'],['延庆','018','01'],
     		['崇明县','019','02'],['黄浦','020','02'],['卢湾','021','02'],['徐汇','022','02'],['长宁','023','02'],
     		['静安','024','02'],['普陀','025','02'],['闸北','026','02'],['虹口','027','02'],['杨浦','028','02'],
     		['闵行','029','02'],['宝山','030','02'],['嘉定','031','02'],['浦东','032','02'],['金山','033','02'],
     		['松江','034','02'],['青浦','035','02'],['南汇','036','02'],['奉贤','037','02'],['朱家角','038','02'],
     		['万州','039','03'],['涪陵','040','03'],['渝中','041','03'],['大渡口','042','03'],['江北','043','03'],['沙坪坝','044','03'],
     		['九龙坡','045','03'],['南岸','046','03'],['北碚','047','03'],['万盛','048','03'],['双桥','049','03'],
     		['渝北','050','03'],['巴南','051','03'],['黔江','052','03'],['长寿','053','03'],['綦江','054','03'],
     		['潼南','055','03'],['铜梁','056','03'],['大足','057','03'],['荣昌','058','03'],['璧山','059','03'],
     		['梁平','060','03'],['丰都','061','03'],['垫江','062','03'],['武隆','063','03'],['忠县','064','03'],
     		['开县','065','03'],['云阳','066','03'],['奉节','067','03'],['巫山','068','03'],['巫溪','069','03'],
     		['石柱','070','03'],['秀山','071','03'],['酉阳','072','03'],['彭水','073','03'],['江津','074','03'],
     		['合川','075','03'],['永川','076','03'],['南川','077','03'],
     		['和平','078','04'],['河东','079','04'],['河西','080','04'],['南开','081','04'],['河北','082','04'],['红桥','083','04'],['塘沽','084','04'],
     		['汉沽','085','04'],['大港','086','04'],['东丽','087','04'],['西青','088','04'],['津南','089','04'],
     		['北辰','090','04'],['武清','091','04'],['宝坻','092','04'],['宁河','093','04'],['静海','094','04'],
     		['蓟县','095','04'],
     		['合肥','096','05'],['芜湖','097','05'],['蚌埠','098','05'],['淮南','099','05'],
     		['马鞍山','100','05'],['淮北','101','05'],['铜陵','102','05'],['安庆','103','05'],['黄山','104','05'],
     		['滁州','105','05'],['阜阳','106','05'],['宿州','107','05'],['巢湖','108','05'],['六安','109','05'],
     		['毫州','110','05'],['池州','111','05'],['宣城','112','05'],
     		['福州','113','06'],['厦门','114','06'],['莆田','115','06'],['三明','116','06'],['泉州','117','06'],['漳州','118','06'],['南平','119','06'],
     		['龙岩','120','06'],['宁德','121','06'],
     		['兰州','122','07'],['嘉峪关','123','07'],['金昌','124','07'],
     		['天水','125','07'],['武威','126','07'],['张掖','127','07'],['平凉','128','07'],['酒泉','129','07'],
     		['庆阳','130','07'],['定西','131','07'],['陇南','132','07'],['临夏','133','07'],['甘南','134','07'],
     		['广州','135','08'],['韵关','136','08'],['深圳','137','08'],['珠海','138','08'],['汕头','139','08'],
     		['佛山','140','08'],['江门','141','08'],['湛江','142','08'],['茂名','143','08'],['肇庆','144','08'],
     		['惠州','145','08'],['梅州','146','08'],['汕尾','147','08'],['河源','148','08'],['阳江','149','08'],
     		['清远','150','08'],['东莞','151','08'],['中山','152','08'],['潮州','153','08'],['揭阳','154','08'],
     		['云浮','155','08'],
     		['南宁','156','09'],['柳州','157','09'],['桂林','158','09'],['梧州','159','09'],
     		['北海','160','09'],['防城港','161','09'],['钦州','162','09'],['贵港','163','09'],['玉林','164','09'],
     		['百色','165','09'],['贺州','166','09'],['河池','167','09'],['来宾','168','09'],['崇左','169','09'],
     		['贵阳','170','10'],['六盘山','171','10'],['遵义','172','10'],['安顺','173','10'],['铜仁','174','10'],
     		['毕节','175','10'],['黔西南','176','10'],['黔东南','177','10'],['黔南','178','10'],
     		['海口','179','11'],['三亚','180','11'],['儋州','181','11'],['万宁','182','11'],['文昌','183','11'],['五指山','184','11'],
     		['琼海','185','11'],['东方','186','11'],
     		['石家庄','187','12'],['唐山','188','12'],['秦皇岛','189','12'],
     		['邯郸','190','12'],['邢台','191','12'],['保定','192','12'],['张家口','193','12'],['承德','194','12'],
     		['沧州','195','12'],['廊坊','196','12'],['衡水','197','12'],
     		['哈尔滨','198','13'],['齐齐哈尔','199','13'],['鸡西','200','13'],['鹤岗','201','13'],['双鸭山','202','13'],['大庆','203','13'],['伊春','204','13'],
     		['佳木斯','205','13'],['七台河','206','13'],['杜江丹','207','13'],['黑河','208','13'],['绥化','209','13'],
     		['大兴安岭','210','13'],
     		['郑州','211','14'],['开封','212','14'],['洛阳','213','14'],['平顶山','214','14'],
     		['安阳','215','14'],['鹤壁','216','14'],['新乡','217','14'],['焦作','218','14'],['濮阳','219','14'],
     		['许昌','220','14'],['漯河','221','14'],['三门峡','222','14'],['南阳','223','14'],['商丘','224','14'],
     		['信阳','225','14'],['周口','226','14'],['驻马店','227','14'],['济源','228','14'],
     		['武汉','229','15'],['黄石','230','15'],['十堰','231','15'],['宜昌','232','15'],['襄樊','233','15'],['鄂州','234','15'],
     		['荆门','235','15'],['孝感','236','15'],['黄冈','237','15'],['咸宁','238','15'],['随州','239','15'],
     		['恩施','240','15'],['仙桃','241','15'],['潜江','242','15'],['天门','243','15'],['神农架','244','15'],
     		['长沙','245','16'],['株洲','246','16'],['湘潭','247','16'],['衡阳','248','16'],['邵阳','249','16'],
     		['岳阳','250','16'],['常德','251','16'],['张家界','252','16'],['益阳','253','16'],['郴州','254','16'],
     		['永州','255','16'],['怀化','256','16'],['娄底','257','16'],['湘西','258','16'],
     		['南京','259','17'],['无锡','260','17'],['徐州','261','17'],['常州','262','17'],['苏州','263','17'],['南通','264','17'],
     		['连云港','265','17'],['淮安','266','17'],['盐城','267','17'],['扬州','268','17'],['镇江','269','17'],
     		['秦州','270','17'],['宿迁','271','17'],
     		['南昌','272','18'],['景德镇','273','18'],['萍乡','274','18'],
     		['九江','275','18'],['新余','276','18'],['鹰潭','277','18'],['赣州','278','18'],['吉安','279','18'],
     		['宜春','280','18'],['抚州','281','18'],['上饶','282','18'],
     		['长春','283','19'],['吉林','284','19'],['四平','285','19'],['辽源','286','19'],['通化','287','19'],['白山','288','19'],['松原','289','19'],
     		['延边','290','19'],
     		['沈阳','291','20'],['大连','292','20'],['鞍山','293','20'],['抚顺','294','20'],
     		['本溪','295','20'],['丹东','296','20'],['锦州','297','20'],['营口','298','20'],['阜新','299','20'],
     		['辽阳','300','20'],['盘锦','301','20'],['铁岭','302','20'],['朝阳','303','20'],['葫芦岛','304','20'],
     		['呼和浩特','305','21'],['包头','306','21'],['乌海','307','21'],['赤峰','308','21'],['通辽','309','21'],
     		['鄂尔多斯','310','21'],['呼伦贝尔','311','21'],['巴彦淖尔','312','21'],['乌兰察布','313','21'],['兴安盟','314','21'],
     		['锡林郭勒盟','315','21'],['阿拉善盟','316','21'],
     		['银川','317','22'],['石嘴山','318','22'],['吴忠','319','22'],['固原','320','22'],['中卫','321','22'],
     		['西宁','322','23'],['海东','323','23'],['海北','324','23'],
     		['黄南','325','23'],['海南','326','23'],['果洛','327','23'],['玉树','328','23'],['海西','329','23'],
     		['济南','330','24'],['青岛','331','24'],['淄博','332','24'],['枣庄','333','24'],['东营','334','24'],
     		['烟台','335','24'],['潍坊','336','24'],['济宁','337','24'],['泰安','338','24'],['威海','339','24'],
     		['日照','340','24'],['莱芜','341','24'],['临沂','342','24'],['德州','343','24'],['聊城','344','24'],
     		['滨州','345','24'],['菏泽','346','24'],['太原','347','25'],['大同','348','25'],['阳泉','349','25'],['长治','350','25'],['晋城','351','25'],
     		['朔州','352','25'],['晋中','353','25'],['运城','354','25'],['忻州','355','25'],['临汾','356','25'],
     		['吕梁','357','25'],['西安','358','26'],['铜川','359','26'],['宝鸡','360','26'],['咸阳','361','26'],
     		['渭南','362','26'],['延安','363','26'],['汉中','364','26'],['榆林','365','26'],['安康','366','26'],
     		['商洛','367','26'],['成都','368','27'],['自贡','369','27'],['攀枝花','370','27'],['泸州','371','27'],
     		['德阳','372','27'],['绵阳','373','27'],['广元','374','27'],['遂宁','375','27'],['内江','376','27'],
     		['乐山','377','27'],['南充','378','27'],['眉山','379','27'],['宜宾','380','27'],['广安','381','27'],
     		['达州','382','27'],['雅安','383','27'],['巴中','384','27'],['资阳','385','27'],['阿坝','386','27'],
     		['甘孜','387','27'],['凉山','388','27'],['乌鲁木齐','389','28'],['克拉玛依','390','28'],['吐鲁番','391','28'],
     		['哈密','392','28'],['昌吉','393','28'],['博尔塔拉','394','28'],['巴音郭楞','395','28'],['阿克苏','396','28'],
     		['克孜勒','397','28'],['喀什','398','28'],['和田','399','28'],['伊犁','400','28'],['塔城','401','28'],
     		['阿勒泰','402','28'],['石河子','403','28'],['阿拉尔','404','28'],['图木舒克','405','28'],['五家渠','406','28'],
     		['拉萨','407','29'],['昌都','408','29'],['山南','409','29'],['日喀则','410','29'],['那曲','411','29'],
     		['阿里','412','29'],['林芝','413','29'],['昆明','414','30'],['曲靖','415','30'],['玉溪','416','30'],
     		['保山','417','30'],['昭通','418','30'],['丽江','419','30'],['思茅','420','30'],['临沧','421','30'],
     		['楚雄','422','30'],['红河','423','30'],['文山','424','30'],['西双版纳','425','30'],
     		['大理','426','30'],['德宏','427','30'],['怒江','428','30'],['迪庆','429','30'],['普洱','430','30'],
     		['杭州','431','31'],['宁波','432','31'],['温州','433','31'],['嘉兴','434','31'],['湖州','435','31'],
     		['绍兴','436','31'],['金华','437','31'],['衡州','438','31'],['舟山','439','31'],['台州','440','31'],
     		['丽水','441','31'],['香港','442','32'],['九龙','443','32'],['新界','444','32'],['澳门','445','33'],
     		['台北','446','34'],['基隆','447','34'],['台南','448','34'],['高雄','449','34'],['屏东','450','34'],
     		['南投','451','34'],['云林','452','34'],['新竹','453','34'],['彰化','454','34'],['苗栗','455','34'],
     		['嘉义','456','34'],['花莲','457','34'],['桃园','458','34'],['宜兰','459','34'],['台东','460','34'],
     		['金门','461','34'],['马祖','462','34'],['澎湖','463','34']
	      ]
	});
	//省份
    var provinceData = new Ext.data.SimpleStore({
	      fields:['value', 'key'],
	      data:[
     		[ '四川','27'],[ '北京','01'],[ '上海','02'],[ '重庆','03'],[ '天津','04'],
    		[ '安徽','05'],[ '福建','06'],[ '甘肃','07'],[ '广东','08'],
    		[ '广西','09'],[ '贵州','10'],[ '海南','11'],[ '河北','12'],
    		[ '黑龙江','13'],[ '河南','14'],[ '湖北','15'],[ '湖南','16'],
    		[ '江苏','17'],[ '江西','18'],[ '吉林','19'],[ '辽宁','20'],
    		[ '内蒙古','21'],[ '宁夏','22'],[ '青海','23'],[ '山东','24'],
    		[ '山西','25'],[ '陕西','26'],[ '新疆','28'],[ '西藏','29'],
    		[ '云南','30'],[ '浙江','31'],[ '香港','32'],
    		[ '澳门','33'],[ '台湾','34']
	      ]
	});
	var path = '/CRM';
	//--------------部门下拉列表-----------------	  
    var departmentStore = new Ext.data.Store({
          proxy: new Ext.data.HttpProxy({
              url: path+'/client/loadDepartment.do'
          }),
          reader: new Ext.data.JsonReader({
                  root: 'data',
                  id: 'departId'
              }, 
            ['departId', 'departName']
          )
	});
	//--------员工下拉列表 
	var employeeData = new Ext.data.Store({
   		proxy: new Ext.data.HttpProxy({
	          url: path+'/client/loadEmployee.do'
	    }),
	    reader: new Ext.data.JsonReader({
	           root: 'data',
	           id: 'eId'
	    	}, 
          ['eId', 'eName']
	    )
	 });
	 var clientSelData = new Ext.data.Store({
		 proxy: new Ext.data.HttpProxy({
	          url: path+'/clientsource/loadclients.do'
	     }),
	     reader: new Ext.data.JsonReader({
	           root: 'data',
	           id: 'client_id'
	    	}, 
          ['client_id', 'client_name']
	    )
	 });
	 var oppTypeData = new Ext.data.SimpleStore({
	      fields:['key', 'value'],
	      data:[
      		[ '1', '房贷'],
		    [ '2', '信贷'],
		    [ '3', '短借'],
		    [ '4', '企贷']
	      ]
	 });
	 var createDateData = new Ext.data.SimpleStore({
      	fields:['key', 'value'],
      	data:[
      		[ '1', '今天'],
		    [ '2', '本周'],
		    [ '3', '本月'],
		    [ '4', '本年']
      	]
	 });
	 var signPossibleData = new Ext.data.SimpleStore({
	      fields:['key', 'value'],
	      data:[
      		[ '1', '100%'],
		    [ '2', '80%'],
		    [ '3', '50%'],
		    [ '4', '10%'],
		    [ '5', '0%']
	      ]
	});
	 function GetHttpRequest(){ 
	    if ( window.XMLHttpRequest ) // Gecko 
	        return new XMLHttpRequest() ; 
	    else if ( window.ActiveXObject ) // IE 
	        return new ActiveXObject("MsXml2.XmlHttp") ; 
	} 
    
   	function AjaxPage(sId, url){ 
   		var oXmlHttp = GetHttpRequest() ; 
   		oXmlHttp.onreadystatechange = function() { 
	        if ( oXmlHttp.readyState == 4 ) { 
	            if ( oXmlHttp.status == 200 || oXmlHttp.status == 304 ) { 
	                IncludeJS( sId, url, oXmlHttp.responseText ); 
	            }else { 
	                alert( 'XML request error: ' + oXmlHttp.statusText + ' (' + oXmlHttp.status + ')' ) ; 
	            } 
	        } 
	    } 
	    oXmlHttp.open('GET', url, false); 
	    oXmlHttp.send(null); 
	} 

	function IncludeJS(sId, fileUrl, source){ 
	    if (( source != null ) && ( !document.getElementById( sId ) ) ){ 
	        var oHead = document.getElementsByTagName('HEAD').item(0); 
	        var oScript = document.createElement( "script" ); 
	        oScript.language = "javascript"; 
	        oScript.type = "text/javascript"; 
	        oScript.id = sId; 
	        oScript.defer = true; 
	        oScript.text = source; 
	        oHead.appendChild( oScript ); 
	    } 
	} 
	function transferDate(date){
	   	date = date.getValue();
	   	if(date != ''){
	   		date = date.format('Y-m-d');
	   	}
	   	return date;
    }
    function judgeTime(start, end){
   		if(start.getValue() != '' & end.getValue() != ''){
	   		start = start.getValue().format('Ymd');
	   		end = end.getValue().format('Ymd');
	   		if(end > start | end == start){
	   			return true;
	   		}else{
	   			return false;
	   		}
	   	}else{
	   		return true;
	   	}
    }
    //判断js是否加载
    function judgeJs(id, src){
    	var el = Ext.get(id);
    	if(!el){
    		AjaxPage(id, src);
    	}
    }
    function loadData(_store){
    	_store.load({
             params:{
                 start:0, 
                 limit:20
             }
       	});  
    }
    var _assigns = '';var _ends = '';var _times = '';var _manaDes = '';var _manaEmp = '';var _client_source = '';
    var signPoss = ''; var conditions = '';//报表跳转的查询条件
    var idss = '';
    var seeFlag = 0;
    var clientPanel = '';//跳转显示的内容
    var client_id = ''; var plan_id = ''; var plan_time = ''; var sign_time = '';
    var jump_from_flag = 0;//从哪里点击跳转（1为今日新增2为今日工作计划3为签单客户）
    var seeBtns = []; var searchTbar = ''; var mainPanels = '';
    var mainPanels = '';//主页显示的内容
    var todayAddStore = ''; var todayWorkPlanStore = ''; var signedStore = ''; var masterStore = '';
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