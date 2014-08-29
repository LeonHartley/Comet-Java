var net = require('net');

// Keep track of the chat clients
var clients = [];

var handshake = {
  "name": "hello",
  "message": ""
}

// Start a TCP Server
net.createServer(function (socket) {

  socket.instanceData = {
    "name": socket.remoteAddress + ":" + socket.remotePort,
    "version": "",
    "status": {},
    "logs": []
  };

  clients.push(socket);

  socket.write(JSON.stringify(handshake));

  socket.on('data', function (data) {
    var dataString = data.toString();
    //var obj = JSON.parse(data.toString().slice(0, -4));//

    if(dataString.indexOf("}{") > -1) {
      dataString = dataString.split("}{")[0] + "}";
    }

    var obj = JSON.parse(dataString);

    switch(obj.name) {
      case "hello":
        console.log("Server connected with version: " + obj.message);
        socket.instanceData.version = obj.message;
      break;

      case "status":
        socket.instanceData.status = obj.message;

        console.log(obj.message);
        break;

      case "appendLog":
        socket.instanceData.logs.push(obj.message);
        console.log(obj);
        break;
    }

  });

  socket.on('end', function () {
    clients.splice(clients.indexOf(socket), 1);
  });

}).listen(1337);

exports.getClientByHotelName = function(hotelName) {
  console.log(hotelName + ", " + clients.length);
}
