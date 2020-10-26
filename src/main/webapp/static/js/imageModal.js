export {imageModal}

const modal = document.getElementById("imageModal");
const modalImg = document.getElementById("img01");
const captionText = document.getElementById("caption");

let imageModal = {
    init: function () {
        const images = document.querySelectorAll(".card-img-top");
        images.forEach(image => image.addEventListener("click", this.showModal));

        const span = document.getElementsByClassName("modal-close")[0];
        span.addEventListener("click", this.hideModal);
    },
    showModal: function() {
        modal.style.display = "block";
        modalImg.src = this.src;
        captionText.innerHTML = this.alt;
    },
    hideModal: function () {
        modal.style.display = "none";
    }
}
