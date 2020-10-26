console.log("js working");

const getAPI = function (url, callback) {
    fetch(url, {
        method: "GET",
        credentials: "same-origin",
    })
        .then(response => {
            if (response.ok) {
                return response.json()}
            else {
                throw new Error('There is no next page to display');
            }
        })
        .then(json_response => callback(json_response))
        .catch((error) => {
            alert(error);
        });
}

const getData = function (supplierName, callback) {
    getAPI(`/?supplier=${supplierName}`, response => {
        callback(response);
    })
}

const getChosenSupplier = function () {
    const suppliersList = document.querySelectorAll(".chosen-supplier");
    const chosenSupplier = document.getElementById("supplier-filter");
    const cardsContainer = document.getElementsByClassName("container");
    for (let supplier of suppliersList) {
        supplier.addEventListener(
            'click', (e) => {
                chosenSupplier.innerText = e.target.innerText;
                getData(e.target.innerText, function (products) {
                    for (let product of products) {
                        console.log(product);
                    }
                })
            })
    }
}


getChosenSupplier();