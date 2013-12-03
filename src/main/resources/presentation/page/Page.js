function Page(initialMetaId) {
	
	Rest.get("/api/menus", [], $A(this, function(menus) {
		gv.menuWidget = new MenuWidget(menus);
		gv.menuWidget.select(initialMetaId);
	}));
}