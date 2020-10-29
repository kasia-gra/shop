import {imageModal} from "./imageModal.js";
import {cartManager} from "./cartManager.js";
import {cartLineItemsManager} from "./cartLineItemsManager.js";

init();

function init() {
    imageModal.init();
    cartManager.init();
    cartLineItemsManager.init();
}