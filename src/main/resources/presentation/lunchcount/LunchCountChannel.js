function LunchCountChannel() {
	new PageHeaderWidget("Lunch Count");
	
	this.currDate = new Date();
		
	new DateSelector(this.currDate, $A(this, function() {
		this.render();
	}));
	
	this.markerWidget = new MarkerWidget();
	new QSFooter();
	
	this.render();
}

LunchCountChannel.prototype.render = function() {

	this.markerWidget.activate();

	new LW(2);
	
	new TextWidget("Total lunch count: ");
	var lw = new LoadingWidget({css: {display: "inline-block"}});
	
	var lunchCountText = new TextWidget("");
	
	var refreshTotal = $IA(this, function() {
		Rest.get("/api/lunchcounts/" + DateUtil.toPlainDate(this.currDate) + "/total", {}, $A(this, function(total) {
			lunchCountText.setValue(total);
			lw.remove();
		}));
	});
	refreshTotal.call();
	

	new LW(2);
	
	new ButtonWidget("Add Lunch Count", $A(this, function() {
		new AddLunchCount(this.currDate).setRefreshHandler($A(this, function() {
			tw.refresh();
			refreshTotal.call();
		}));
	}));
	new LW(2);
	
	var tw = new TableWidget(this, {css: {width: "100%"}});
	
	tw.addHeader("Teacher");
	tw.addColumn("teacherName");
	
	tw.addHeader("Lunch Count");
	tw.addColumn("count");
	
	tw.addHeader("Notes");
	tw.addColumn("notes");
	
	tw.addHeader("Edit");
	tw.addColumn(function(lunchCount) {
		var td = current;

		new LinkWidget("Edit", $A(this, function() {
			new AddLunchCount(this.currDate, lunchCount).setRefreshHandler($A(this, function(fullRefresh) {
				if (fullRefresh == true) {
					tw.refresh.call();
				} else {
					tw.refreshRow(lunchCount.id, td);
				}
				refreshTotal();
			}));
		}));
	});
	
	tw.turnOnPaging();

	tw.setLoaderResource("/api/lunchcounts", {date: DateUtil.toPlainDate(this.currDate)});
	tw.render();
};