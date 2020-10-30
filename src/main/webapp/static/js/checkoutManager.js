import {logManager} from "./logManager.js";

let checkoutManager = {
  init: function() {
    checkoutManager.initValidation();
    checkoutManager.toggleShippingAddressVisibility();
  },
  initValidation: function() {
    window.addEventListener('load', function () {
      // Fetch all the forms we want to apply custom Bootstrap validation styles to
      let forms = document.querySelectorAll(".needs-validation");
      // Loop over them and prevent submission
      Array.prototype.filter.call(forms, function (form) {
        form.addEventListener('submit', function (event) {
          if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
            logManager.sendLog("CHECKOUT", false);
          } else {
            logManager.sendLog("CHECKOUT", true);
          }
          form.classList.add('was-validated');
        }, false);
      })
    }, false);
  },
  toggleShippingAddressVisibility: function() {
    let sameAddressCheckbox = document.querySelector("#same-address");
    // sameAddressCheckbox.checked = false;
    sameAddressCheckbox.addEventListener("change", event => checkoutManager.toggleShippingAddressVisibilityHandler(event));
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

checkoutManager.init();