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
    }

}