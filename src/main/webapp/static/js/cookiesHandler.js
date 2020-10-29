export let cookiesHandler = {

    getCookie: function (name) {
        name = name + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let cookiesArray = decodedCookie.split(';');
        for (let i = 0; i <cookiesArray.length; i++) {
            let cookie = cookiesArray[i];
            while (cookie.charAt(0) === ' ') {
                cookie = cookie.substring(1);
            }
            if (cookie.indexOf(name) === 0) {
                return cookie.substring(name.length, cookie.length);
            }
        }
        return "";
    },

    checkCookie: function (name) {
        return this.getCookie(name) !== "";
    },

    setCookie: function(name, value, expireInDays) {
        let date = new Date();
        date.setTime(date.getTime() + (expireInDays*24*60*60*1000));
        let expires = "expires="+ date.toUTCString();
        document.cookie = name + "=" + value + ";" + expires + ";";
    }

}