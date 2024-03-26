import * as packageService from "./package-service.js";

const payBtns = document.querySelectorAll(".btn-pay");

payBtns.forEach(btn => {
    btn.addEventListener("click", function () {
        let packageId = this.getAttribute("data-package-id");
        let stringNumber = this.getAttribute("data-amount");

        let amount = parseFloat(stringNumber.replace(/,/g, ''));

        packageService.createPayment(packageId, amount)
            .then(result => {
                if(result.status){
                    console.log(result.data.url)
                    window.location.href = result.data.url
                }
                else
                    showToast(result.message, "", "")
            })
    })
})

function showToast(msg, url, redirect){
    Toastify({
        text: msg,
        duration: 3000,
        close: true,
        destination: url,
        newWindow: redirect,
        gravity: "bottom", // `top` or `bottom`
        position: "right", // `left`, `center` or `right`
        stopOnFocus: true, // Prevents dismissing of toast on hover
        offset: {
            x: 10,
            y: 60
        },
        style: {
            background: "linear-gradient(to right, #FF416C, #f6728e)",
            fontWeight: "bold"
        },
        onClick: function(){} // Callback after click
    }).showToast();
}