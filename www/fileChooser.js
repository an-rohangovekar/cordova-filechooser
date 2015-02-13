cordova.define("com.megster.cordova.FileChooser.FileChooser", function(require, exports, module) 
{ 
    
    fileChooser.prototype.open= function (success, failure) {
        cordova.exec(success, failure, "FileChooser", "open", []);
    };
    fileChooser.prototype.play=function (success, failure,uri) {
   
        cordova.exec(success, failure, "FileChooser", "startPlaying", [uri]);
    };
    fileChooser.prototype.stop=function (success, failure) {
        cordova.exec(success, failure, "FileChooser", "stopPlaying", []);
    };
module.exports = fileChooser;

});
