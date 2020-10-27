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
        let productId = {"productId": parentElement.dataset.productId};
        dataHandler._api_post("/cart", productId, cartManager.changeCartItemsNumber);
    },
    changeCartItemsNumber: function (json_response) {
        document.querySelector("#cart").querySelector(".text-success").innerHTML = json_response.itemsNumber;
    }

}