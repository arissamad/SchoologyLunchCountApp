function ViewUsersDialog(account) {
	this.account = account;
	this.render();
}

ViewUsersDialog.prototype.setRefreshHandler = function(action) {
	this.refreshAction = action;
};

ViewUsersDialog.prototype.render = function() {
	this.dialog = new FullPage("manage", "Users for account \"" + this.account.name + "\"", "/accounts/" + this.account.id + "/users");
	
	new PageHeaderWidget("Users for account: " + this.account.name);
	
	new ButtonWidget("Add User", $A(this, function() {
		new EditUserDialog(this.account).setRefreshHandler($A(this, function() {
			tw.refresh();
		}));
	}));
	new SW();
	new ButtonWidget("Close", $A(this, function() {
		this.dialog.close();
	}));
	
	new LW(2);
	
	var tw = new TableWidget(this, {css: {width: "100%"}});
	
	tw.addHeader("Name");
	tw.addColumn("name");
	
	tw.addHeader("Email");
	tw.addColumn("email");
	
	tw.addHeader("Role");
	tw.addColumn("roleMetaId");
	
	tw.addHeader("Edit");
	tw.addColumn(function(user) {
		var td = current;
		
		new LinkWidget("Edit", $A(this, function() {
			new EditUserDialog(this.account, user).setRefreshHandler($A(this, function(fullRefresh) {
				if(fullRefresh == true) tw.refresh();
				else tw.refreshRow(user.id, td);
			}));
		}));
	});
	
	tw.turnOnPaging();
	tw.turnOnSearching();
	
	tw.setLoaderResource("/api/users", {accountId: this.account.id});
	
	tw.render();
};