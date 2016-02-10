var instances = {};

var start = function(config) {
        var launchString = "java -jar versions/" + config.version + ".jar ";

        var configOverrides = {
            //database overrides
            "comet.db.host": config.db.host,
            "comet.db.username": config.db.username,
            "comet.db.password": config.db.password,
            "comet.db.name": config.db.database,
            "comet.db.pool.max": config.db.pool,

            //network overrides
            "comet.network.host": config.ip,
            "comet.network.port": config.port
        };

        for (key in configOverrides) {
            if (!configOverrides.hasOwnProperty(key)) continue;

            launchString += key + "=" + configOverrides[key] + " ";
        }

        var exec = require('child_process').exec, child;
        var hasErrored = false;

        child = exec(launchString,
            function (error, stdout, stderr) {
                if (error) {
                    hasErrored = true;
                    //send response saying there's an error
                    console.log("Comet instance failed to launch on port: " + configOverrides["comet.network.port"] + " with PID: " + child.pid + ", error: " + error);
                }
            }
        );

        setTimeout(function () {
            if (hasErrored) {
                return;
            }

            // send response saying it worked, including the processid!
            console.log("Comet instance running on port: " + configOverrides["comet.network.port"] + " with PID: " + child.pid);

            instances[config.instanceId] = {
                processId: child.pid,
                config: config
            };
        }, 2000);
};

var restart = function (instanceId) {
    if(!instances[instanceId]) {
        console.log("Failed to find instance: " + instanceId);
    }

    var instance = instances[instanceId];

    var spawn = require('child_process').spawn;
    spawn("taskkill", ["/pid", instance.processId, '/f', '/t']);
    //process.kill(instance.processId, "SIGINT");

    console.log("Restarting instance: " + instanceId);

    setTimeout(function() {
       start(instance.config);
    }, 5000);
};

var config = {
    instanceId: "52dd30a7-9904-4915-8409-50adc108a96b",

    version: "1.1.1-ALPHA1",
    db: {
        host: "localhost",
        username: "root",
        password: "",
        database: "cometsrv",
        pool: 50
    },

    ip: "0.0.0.0",
    port: "30000"
};

start(config);

setTimeout(function() {
    restart(config.instanceId);
}, 30000);
