function EditUserDialog(account, user) {
	this.account = account;
	this.user = user;
	
	this.userId = null;
	if(this.user != null) {
		this.userId = this.user.id;
	}
	
	this.render();
}

EditUserDialog.prototype.setRefreshHandler = function(action) {
	this.refreshAction = action;
};

EditUserDialog.prototype.render = function() {
	
	if(this.user == null) {
		this.dialog = new FullPage("manage", "Add User", "/accounts/" + this.account.id + "/users/add");
		new PageHeaderWidget("Add User");
	} else {
		this.dialog = new FullPage("manage", "Edit user " + this.user.name, "/accounts/" + this.account.id + "/users/" + this.user.id + "/edit");
		new PageHeaderWidget("Edit user: " + this.user.name);
	}
	
	var fw = new FormWidget({rightWidth: 500});
	
	fw.label();
	new LabelWidget("Name");
	
	fw.value();
	fw.link("name", new InputWidget());
	
	fw.label();
	new LabelWidget("Email");
	
	fw.value();
	fw.link("email", new InputWidget());
	
	fw.label();
	new LabelWidget("Role Meta ID");
	
	fw.value();
	fw.link("roleMetaId", new InputWidget());
	
	fw.label();
	new LabelWidget("Password");
	
	fw.value();
	var passwordMarker = new MarkerWidget();
	passwordMarker.activate();
	
	new LW();
	
	var passwordInput = new NullWidget();
	new LinkWidget("Set password", $A(this, function() {
		passwordMarker.activate();
		passwordInput = new PasswordWidget();
		passwordInput.input.focus();
	}));
	
	fw.setValues(this.user);
	fw.finish();
	
	
	new LW(2);
	
	var updateButton = new ButtonWidget("Update User", $A(this, function() {
		var parameters = {
			name: fw.getValue("name"),
			email: fw.getValue("email"),
			roleMetaId: fw.getValue("roleMetaId"),
			password: passwordInput.getValue()
		};

		updateButton.loading();
		Rest.post("/api/users/" + this.userId, parameters, $A(this, function(result) {
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