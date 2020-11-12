let registrationManager = {
    init: function() {
        registrationManager.toggleShippingAddressVisibility();
    },

    toggleShippingAddressVisibility: function() {
        let sameAddressCheckbox = document.querySelector("#same-address");
        // sameAddressCheckbox.checked = false;
        sameAddressCheckbox.addEventListener("change", event => this.toggleShippingAddressVisibilityHandler(event));
    },
    toggleShippingAddressVisibilityHandler: function(event) {
        let shippingAddressContainer = document.querySelector("#shippingAddressContainer");
        console.log(event.currentTarget.checked)
        if (event.currentTarget.checked === true) {
            shippingAddressContainer.classList.add("hidden");
            shippingAddressContainer.disabled = true;
        } else {
            shippingAddressContainer.classList.remove("hidden");
            shippingAddressContainer.disabled = false;
        }
    }
}

registrationManager.init();