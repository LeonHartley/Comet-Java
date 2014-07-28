/**
 * Set the plugin information
 * @type {{title: string, author: string}}
 */
var plugin = {
    title: "Comet Test",
    author: "Leon"
};

/**
 * Attach a handler to the login event, so we can use our
 * external logging application for this stuff...
 */
Comet.on('login', function(session) {
    log.info("Hey!");
});