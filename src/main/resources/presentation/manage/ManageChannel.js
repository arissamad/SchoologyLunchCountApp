function ManageChannel() {
	new PageHeaderWidget("Lunch Count Administration");
	
	new HeaderWidget("Accounts");
	
	new LW(4);
	
	var tw = new TableWidget(this, {css: {width: "100%"}});
	
	tw.addHeader("Acount Name");
	tw.addColumn("name");
	
	tw.addHeader("Created");
	tw.addColumn(function(account) {
		new TextWidget(DateUtil.toDateAndTime(account.creationDate));
	});
	
	tw.addHeader("View Users");
	tw.addColumn(function(account) {
		new LinkWidget("Users", $A(this, function() {
			new ViewUsersDialog(account);
		}));
	});
	
	tw.addHeader("Edit Account");
	tw.addColumn(function(account) {
		var td = current;

		new LinkWidget("Edit", $A(this, function() {
			new EditAccountDialog(account).setRefreshHandler($A(this, function(
					fullRefresh) {
				if (fullRefresh == true)
					tw.refresh();
				else
					tw.refreshRow(account.id, td);
			}));
		}));
	});
	
	
	tw.turnOnPaging();
	tw.turnOnSearching();
	
	tw.setLoaderResource("/api/accounts", {});
	
	tw.render();
}