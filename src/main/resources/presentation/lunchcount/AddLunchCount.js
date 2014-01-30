function AddLunchCount(date, lunchCount) {
	this.date = date;
	this.lunchCount = lunchCount;
	this.lunchCountId = null;

	if(this.lunchCount != null) this.lunchCountId = this.lunchCount.id;
	
	var dialog = new DialogWidget("Add Lunch Count", {width: "500px"});
	
	var fw = new FormWidget({
		leftWidth: 100, rightWidth: 300 
	});

	fw.label();
	new LabelWidget("Teacher");
	
	fw.value();
	fw.link("teacherName", new InputWidget(), $V("Teacher", v.notEmpty));
	
	fw.label();
	new LabelWidget("Count");
	
	fw.value();
	fw.link("count", new InputWidget(), $V("Count", v.notEmpty, v.int));
	
	fw.label();
	new LabelWidget("Notes");
	
	fw.value();
	fw.link("notes", new InputWidget());
	
	fw.setValues(this.lunchCount);
	fw.focus();
	fw.submitOnEnter($A(this, function() {
		updateButton.trigger();
	}));
	fw.finish();

	dialog.buttons();
	
	if (this.lunchCountId != null) {
		var bw = new ButtonWidget("Delete", $A(this, function() {
			bw.loading();
			Rest.del("/api/lunchcounts/" + this.lunchCountId, {}, $A(this, function() {
				dialog.close();

				if (this.refreshAction != null)
					this.refreshAction.call(true);
			}));
		}));
		bw.widget.css("float", "right");
	}
	
	var updateButton = new ButtonWidget("Add", $A(this, function() {
		if(!fw.verify()) return;
		
		var parameters = {
			date: DateUtil.toPlainDate(this.date),
			teacherName: fw.getValue("teacherName"),
			count: fw.getValue("count"),
			notes: fw.getValue("notes")
		};

		updateButton.loading();
		Rest.post("/api/lunchcounts/" + this.lunchCountId, parameters, $A(this,
				function(result) {
					updateButton.doneLoading();
					dialog.close();

					if (this.refreshAction != null)
						this.refreshAction.call();
				}));
	}));
	new SW();
	new ButtonWidget("Cancel", $A(this, function() {
		dialog.close();
	}));

	dialog.reposition();
}


AddLunchCount.prototype.setRefreshHandler = function(action) {
	this.refreshAction = action;
};