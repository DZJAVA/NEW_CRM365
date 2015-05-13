<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" href="resources/css/ext-all.css"
			type="text/css"></link>
		<link rel="stylesheet" href="resources/css/ext-smt.css" type="text/css"></link>
		<link rel="stylesheet" href="resources/css/costmon.css" type="text/css">
		<script type="text/javascript" src="resources/js/ext-base.js"></script>
		<script type="text/javascript" src="resources/js/ext-all.js"></script>
		<script type="text/javascript" src="resources/js/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="resources/js/ProgressBarPager.js"></script>
		
		<style type="text/css">
			.title_a{
				height: 20px;
				font-size: 13px;
				width: 60px;
			}
			.title_b{
				height: 20px;	
				width: 60px;
			}
			#btn_div{
				height: 20px;
				width: 140px;
				margin-left:85%;
				margin-top: 40px;
				position: absolute;
			}
			#user{
				float:right;
				font-size: 14px;
				color: black;
				margin-top: 10px;
				margin-left:74%;
				position: absolute;
			}
			
		</style>
		<title>CRM</title>
	</head>
	<body class="icon-male">