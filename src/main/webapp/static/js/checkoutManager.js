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
          }
          form.classList.add('was-validated');
        }, false);
      })
    }, false);
  },
  toggleShippingAddressVisibility: function() {
    let sameAddressCheckbox = document.querySelector("#same-address");
    sameAddressCheckbox.addEventListener("change", checkoutManager.toggleShippingAddressVisibilityHandler);
  },
  toggleShippingAddressVisibilityHandler: function() {
    let shippingAddressContainer = document.querySelector("#shippingAddressContainer");
    shippingAddressContainer.classList.toggle("hidden");
    shippingAddressContainer.disabled = !shippingAddressContainer.disabled;
  }
}

checkoutManager.init();