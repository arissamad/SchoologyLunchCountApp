function EditAccountDialog(account) {
	this.account = account;
	this.render();
}

EditAccountDialog.prototype.setRefreshHandler = function(action) {
	this.refreshAction = action;
};

EditAccountDialog.prototype.render = function() {
	this.dialog = new FullPage("manage", "Edit Account" + this.account.name, "/accounts/" + this.account.id + "/edit");
	
	new PageHeaderWidget("Edit Account: " + this.account.name);
	
	var fw = new FormWidget({rightWidget: 500});
	
	fw.label();
	new LabelWidget("Account Name");
	
	fw.value();
	fw.link("name", new InputWidget());
	
	fw.setValues(this.account);
	fw.finish();
	
	new LW(2);
	
	var updateButton = new ButtonWidget("Update Account", $A(this, function() {
		var parameters = {
			name: fw.getValue("name")
		};
		
		updateButton.loading();
		Rest.post("/api/accounts/" + this.account.id, parameters, $A(this, function(result) {
			updateButton.doneLoading();
			this.dialog.close();
			
			if(this.refreshAction != null) this.refreshAction.call();
		}));
	}));
	new SW();
	new ButtonWidget("Cancel", $A(this, function() {
		this.dialog.close();
	}));
};