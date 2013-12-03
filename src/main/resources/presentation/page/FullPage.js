function FullPage(initialMetaId, title, resourcePath) {
	this.initialMetaId = initialMetaId;
	
	if(initialMetaId != null) {
		
		if(gv.menuWidget == null) {
			Rest.get("/api/menus", [], $A(this, function(menus) {
				gv.menuWidget = new MenuWidget(menus);
				gv.menuWidget.highlight(initialMetaId);
			}));
		} else {
			gv.menuWidget.highlight(initialMetaId);
		}
	}

	current = $(".app-content");
	
	PageStack.push();
	History.pushState({}, title, resourcePath);
}

FullPage.prototype.close = function() {
	var popped = PageStack.pop();
	
	if(!popped) {
		gv.menuWidget.select(this.initialMetaId);
	}
};