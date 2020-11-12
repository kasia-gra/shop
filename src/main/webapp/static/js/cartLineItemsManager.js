import {dataHandler} from "./dataHandler.js";
import {cookiesHandler} from "./cookiesHandler.js";

const increaseQtyButtons = document.querySelectorAll(".increase-qty");
const decreaseQtyButtons = document.querySelectorAll(".decrease-qty");
const chooseQtyInputs = document.querySelectorAll(".choose-qty");


export let cartLineItemsManager = {
    init: function () {
        increaseQtyButtons.forEach(button => button.addEventListener("click", this.increaseQty));
        decreaseQtyButtons.forEach(button => button.addEventListener("click", this.decreaseQty));
        chooseQtyInputs.forEach(button => button.addEventListener("change", function (event) {
            updateQty(event.target);
        }));
    },

    increaseQty: function (event) {
        let currentQtyField = event.target.parentElement.querySelector(".choose-qty");
        let currentQty = parseInt(currentQtyField.value);
        let productId = event.target.parentElement.id;
        let lineItemContainer = event.target.parentElement.parentElement.parentElement;
        changeLineItemQty(productId, currentQtyField, 1, lineItemContainer);
    },

    decreaseQty: function (event) {
        let currentQtyField = event.target.parentElement.querySelector(".choose-qty");
        let currentQty = parseInt(currentQtyField.value);
        let productId = event.target.parentElement.id;
        let lineItemContainer = event.target.parentElement.parentElement.parentElement;
        if (currentQty === 1) {
            deleteLineItem(productId, lineItemContainer);
        }
        else {
            changeLineItemQty(productId, currentQtyField,  - 1, lineItemContainer);
        }
    },

}

const updateQty =  function (currentQtyField) {
    const currentQty = parseInt(currentQtyField.value);
    let productId = currentQtyField.parentElement.id;
    let lineItemContainer = currentQtyField.parentElement.parentElement.parentElement;
    if (currentQty < 0 || !Number.isInteger(currentQty)) {
        alert("Please enter an integer");
        currentQtyField.value = currentQtyField.dataset.qty;
    }
    else if (currentQty === 0) {
        currentQtyField.dataset.qty = currentQty;
        deleteLineItem(productId, lineItemContainer);
    }
    else {
        let addedQty = currentQty - currentQtyField.dataset.qty
        changeLineItemQty(productId, currentQtyField, addedQty, lineItemContainer);

    }
}

const changeLineItemQty =  function (productId, currentQtyField, updatedQty, lineItemContainer) {
    let dataDict = {};
    dataDict.productId = productId;
    dataDict.qty = updatedQty;
    dataDict.sessionId = cookiesHandler.getCookie("sessionId");
    dataHandler._api_post(`/cart`, dataDict, json_response => {
        updateQtyValueField (currentQtyField, updatedQty)
        updateLineItemPrice(json_response, lineItemContainer);
        updateCart(json_response, lineItemContainer);
    });
}

const updateQtyValueField = function(currentQtyField, updatedQty) {
    let currentQty = currentQtyField.dataset.qty
    currentQtyField.value = parseInt(currentQty) + updatedQty;
    currentQtyField.dataset.qty = parseInt(currentQty) + updatedQty;
}

const deleteLineItem = function(productId, lineItemContainer) {
    let dataDict = {};
    dataDict.productId = productId;
    dataDict.sessionId = cookiesHandler.getCookie("sessionId");
    dataHandler._api_delete(`/cart`, dataDict, json_response => {
        removeLineItemFromCart(lineItemContainer)
        updateCart(json_response, lineItemContainer);
        disableCheckoutIfAllItemsRemoved(cookiesHandler.checkCookie("sessionId"));
    });
}

const removeLineItemFromCart = function (lineItemDOMElement) {
    lineItemDOMElement.remove();

}

const updateLineItemPrice = function (json_response, lineItemContainer) {
    console.log("UPDATING PRICE TO " + parseInt(json_response.linePrice))
    lineItemContainer.querySelector(".total-line-price").innerText = parseInt(json_response.linePrice).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');;
}

const updateCart = function(json_response, lineItemContainer) {
    document.getElementById("total-to-pay").innerText = (parseInt(json_response.totalCartValue)).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
    document.querySelector(".text-success").innerHTML = json_response.itemsNumber;}

const formatPrice = function(price) {
    price.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
}

const disableCheckoutIfAllItemsRemoved = function (isCookiePresent){
    console.log("COOKIE NAME " + cookiesHandler.getCookie());
    if (!isCookiePresent) {
        document.querySelector("#emptyCard").classList.remove("hidden");
        document.querySelector("#notEmptyCard").classList.add("hidden");
        // document.getElementById("checkout-button").disabled = true;
    }
}




