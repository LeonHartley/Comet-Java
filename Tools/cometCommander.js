const net = require('net');

const StringDecoder = require('string_decoder').StringDecoder;
const decoder = new StringDecoder('utf8');

const client = net.connect(30000, '127.0.0.1', function () {
    console.log('connected to server!');

    //command \/
    client.write("");
});

client.on('data', function (data) {
    console.log(decoder.write(data));
});

client.on('end', function () {
    console.log('disconnected from server');
});
