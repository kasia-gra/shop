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
            .then(response => {
            if (response.ok) {
                return response.json()}
            else {
                throw new Error('Ooops something went wrong');
            }
        })
            .then(response => response.json())
            .then(json_response => callback(json_response))

    },
    _api_put: function(url, data, callback) {
        fetch(url, {
            method: "PUT",
            credentials: "same-origin" ,
            body: JSON.stringify(data),
            cache: "no-cache",
            headers: new Headers({
                "content-type": "application/json"
            })
        })
            .then(response => {
                if (response.ok) {
                    return response.json()}
                else {
                    throw new Error('Ooops something went wrong');
                }
            })
            .then(json_response => callback(json_response))
            .catch((error) => {
                alert(error);
            });
        },

    _api_delete: function (url, dataDict, callback) {
        fetch(url, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(dataDict)
        })
            .then(response => response.json(), error => alert(error))
            .then(data => {
                callback(data)
            })
    }
}