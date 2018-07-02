var CometIM = {
	getAuthTicket: function() {
		var authTicket = window.location.hash.substring(1);
		// window.location.hash = "";

		return authTicket;
	},

	isAuthenticated: false
};
