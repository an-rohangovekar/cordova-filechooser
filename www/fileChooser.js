module.exports = {
    open: function (success, failure) {
        cordova.exec(success, failure, "FileChooser", "open", []);
    }
    open: function (success, failure,uri) {
      cordova.exec(success, failure, "FileChooser", "startPlaying", [uri]);
    },
    open: function (success, failure) {
         cordova.exec(success, failure, "FileChooser", "stopPlaying");
    },
};
