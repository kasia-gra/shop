import {dataHandler} from "./dataHandler.js";

export let cartManager = {
    init: function () {
        const cards = document.querySelectorAll("[data-product-id]");
        cards.forEach(card => card.querySelector(".cart-button a").addEventListener("click", this.addToCartHandler))
    },
    addToCartHandler: function (event) {
        let parentElement = event.currentTarget.parentElement;
        while (!parentElement.hasAttribute("data-product-id")) {
            parentElement = parentElement.parentElement;
        }
        let productId = parentElement.dataset.productId;
        let data = {};
        if (!cartManager.checkCookie("userId")) {
            data.productId = productId;
            dataHandler._api_put("/cart", data, json_response => {
                    cartManager.changeCartItemsNumber(json_response);
                    cartManager.setCookie("userId", json_response.userId, 30);
                });
        } else {
            data.productId = productId;
            data.userId = cartManager.getCookie("userId");
            dataHandler._api_post("/cart", data, cartManager.changeCartItemsNumber);
        }
    },
    changeCartItemsNumber: function (json_response) {
        document.querySelector("#cart").querySelector(".text-success").innerHTML = json_response.itemsNumber;
    },
    setCookie: function(name, value, expireInDays) {
        let date = new Date();
        date.setTime(date.getTime() + (expireInDays*24*60*60*1000));
        let expires = "expires="+ date.toUTCString();
        document.cookie = name + "=" + value + ";" + expires + ";";
    },
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
        return cartManager.getCookie(name) !== "";
    }
}