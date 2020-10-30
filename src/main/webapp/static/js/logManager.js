import {dataHandler} from "./dataHandler.js";

export let logManager = {
    sendLog: function (logType, isSuccessful) {
        let data = {};
        data.date = logManager.createDate();
        data.logType = logType;
        data.isSuccessful = isSuccessful;
        dataHandler._api_post("/log", data, json_response => {
            console.log(json_response);
        });
    },
    createDate: function() {
        let date = new Date();
        return date.toLocaleString();
    }
}