import {dataHandler} from "./dataHandler.js";
import {cookiesHandler} from "./cookiesHandler.js";

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
        if (!cookiesHandler.checkCookie("sessionId")) {
            data.productId = productId;
            dataHandler._api_put("/cart", data, json_response => {
                    cartManager.changeCartItemsNumber(json_response);
                    cookiesHandler.setCookie("sessionId", json_response.sessionId, 30);
                });
        } else {
            data.qty = 1;
            data.productId = productId;
            data.sessionId = cookiesHandler.getCookie("sessionId");
            dataHandler._api_post("/cart", data, cartManager.changeCartItemsNumber);
        }
    },
    changeCartItemsNumber: function (json_response) {
        document.querySelector("#cart").querySelector(".text-success").innerHTML = json_response.itemsNumber;
    }
}