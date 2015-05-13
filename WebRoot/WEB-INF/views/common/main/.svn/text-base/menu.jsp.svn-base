<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<script type="text/javascript">
Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = '<c:url value="/js/ext/images/default/s.gif"/>';
	Ext.QuickTips.init();
	var viewport = new Ext.Viewport({
		border: false,
		items: [
			new Ext.tree.TreePanel({
		        id:'meuntree',
				autoScroll : true,
				border : false,
				containerScroll : true,
				rootVisible : false,
				lines : false,
				singleExpand : true,
				useArrows : true,
				root:new Ext.tree.AsyncTreeNode({
					id : '-1'
				}),
				loader:new Ext.tree.TreeLoader({
					url : 'menu.do'
				})
			})
		],
        renderTo: Ext.getBody()
	});
});
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
