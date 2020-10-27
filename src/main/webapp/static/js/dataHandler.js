export let dataHandler = {
    _api_get: function (callback, url) {
        fetch(url, {
            method: "GET",
            credentials: "same-origin"
        })
            .then(response => response.json())
            .then(json_response => callback(json_response))
    },
    _api_post: function(url, data, callback) {
        fetch(url, {
            method: "POST",
            credentials: "same-origin" ,
            body: JSON.stringify(data),
            cache: "no-cache",
            headers: new Headers({
                "content-type": "application/json"
            })
        })
            .then(response => response.json())
            .then(json_response => callback(json_response))

    }
}