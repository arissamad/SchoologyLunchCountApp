function DateSelector(date, onChangeAction) {
	ClassUtil.mixin(DateSelector, this, Widget);
	Widget.call(this, "DateSelector");
	
	this.onChangeAction = onChangeAction;
	
	this.widget.disableSelection();
	
	this.date = date;
	this.update(true);
	
	this.widget.find(".prev").click($IA(this, function() {
		var newDate = moment(this.date).add("days", -1);
		this.date.setTime(newDate.valueOf());
		this.update();
	}));
	
	this.widget.find(".next").click($IA(this, function() {
		var newDate = moment(this.date).add("days", 1);
		this.date.setTime(newDate.valueOf());
		this.update();
	}));
}

DateSelector.prototype.update = function(firstTime) {
	var m = moment(this.date);
	this.widget.find(".month").text(m.format("MMM"));
	this.widget.find(".date").text(m.format("DD"));
	this.widget.find(".day").text(m.format("ddd"));
	
	if(firstTime != true) {
		clearTimeout(this.timeoutId);
		this.timeoutId = setTimeout($IA(this, function() {
			log("Boom");
			this.onChangeAction.call();
		}), 500);
	}
};