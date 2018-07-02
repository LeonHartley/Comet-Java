var Packet = {
	AUTH_REQUEST: "messengerAuthRequest"
};

var PacketHandler = {
	handlers: [],

	handlePacket: function(socket, data) {
		if(PacketHandler.handlers[data.id]) {
			PacketHandler.handlers[data.id](socket, data);
		}
	}
};

PacketHandler.handlers["authok"] = function(socket, data) {
	CometIM.isAuthenticated = true;	
};