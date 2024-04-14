import * as fileService from "./file-service.js";

const spinner = document.getElementById("spinner");
spinner.classList.remove('show');
spinner.style.opacity = "0";
document.getElementById('imageUrl').addEventListener('change', function() {
    if (this.files && this.files[0]) {
        var imgPreview = document.getElementById('image-preview');
        imgPreview.innerHTML = '';

        var reader = new FileReader();
        reader.onload = function(e) {
            var imgElement = document.createElement('img');
            imgElement.src = e.target.result;
            imgElement.style.maxWidth = '100%';
            imgElement.style.height = 'auto';
            imgPreview.appendChild(imgElement);
        }
        reader.readAsDataURL(this.files[0]);
    }
});

document.querySelectorAll('.contact-btn').forEach(button => {
    button.addEventListener('click', function() {
        const packId = this.getAttribute('data-pack-id');
        const packName = this.getAttribute('data-pack-name');
        const packAmount = this.getAttribute('data-pack-amount');
        const packUnit = this.getAttribute('data-pack-unit');

        document.getElementById('packageId').value = packId;
        document.getElementById('packageName').value = packName;
        document.getElementById('packageAmount').innerText = packAmount;
        document.getElementById('packageUnit').innerText = packUnit;
    });
});

document.getElementById('send-request').addEventListener('click', function() {
    spinner.classList.add('show');
    spinner.style.opacity = "0.5";

    let isValid = true;
    let errorMessage = '';

    // Lấy giá trị các trường
    let enterpriseName = document.getElementById('enterpriseName').value;
    let productName = document.getElementById('productName').value;
    let enterpriseAddress = document.getElementById('enterpriseAddress').value;
    let enterprisePhoneNumber = document.getElementById('enterprisePhoneNumber').value;
    let enterpriseEmail = document.getElementById('enterpriseEmail').value;
    let contactPhoneNumber = document.getElementById('contactPhoneNumber').value;
    let contactEmail = document.getElementById('contactEmail').value;
    let legalDocId = document.getElementById('legalDocId').value.trim();
    let issuedBy = document.getElementById('issuedBy').value.trim();
    let dated = document.getElementById('dated').value.trim();
    let birthDay = document.getElementById('birthDate').value.trim();
    let address = document.getElementById('address').value.trim();
    let fileInput = document.getElementById('imageUrl');
    let filePath = fileInput.value;
    let allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;

    document.querySelectorAll('.text-danger').forEach(function(span) {
        span.textContent = '';
    });

    if (!filePath) {
        document.getElementById('errorImage').textContent = 'Bạn chưa chọn file ảnh cho banner.';
        isValid = false;
    } else if (!allowedExtensions.exec(filePath)) {
        document.getElementById('errorImage').textContent = 'Chỉ chấp nhận file ảnh định dạng .jpg, .jpeg hoặc .png.';
        fileInput.value = '';
        isValid = false;
    }

    if (!enterpriseName.trim()) {
        document.getElementById('errorEnterpriseName').textContent = 'Tên doanh nghiệp không được để trống.';
        isValid = false;
    }

    if (!productName.trim()) {
        document.getElementById('errorProductName').textContent = 'Tên sản phẩm không được để trống.';
        isValid = false;
    }

    if (!enterpriseAddress.trim()) {
        document.getElementById('errorEnterpriseAddress').textContent = 'Địa chỉ doanh nghiệp không được để trống.';
        isValid = false;
    }

    if (!birthDay.trim()) {
        document.getElementById('errorBirthDate').textContent = 'Ngày sinh không được để trống.';
        isValid = false;
    }

    if (!address.trim()) {
        document.getElementById('errorAddress').textContent = 'Địa chỉ không được để trống.';
        isValid = false;
    }

    if (!enterprisePhoneNumber.trim()) {
        document.getElementById('errorEnterprisePhoneNumber').textContent = 'Số điện thoại không được để trống.';
        isValid = false;
    }

    if (!/^\d{10,11}$/.test(enterprisePhoneNumber)) {
        document.getElementById('errorEnterprisePhoneNumber').textContent = 'Số điện thoại không hợp lệ.';
        isValid = false;
    }

    if (!/\S+@\S+\.\S+/.test(enterpriseEmail)) {
        document.getElementById('errorEnterpriseEmail').textContent = 'Email không hợp lệ.';
        isValid = false;
    }

    if (!document.getElementById('fullName').value.trim()) {
        document.getElementById('errorFullName').textContent = 'Họ và tên không được để trống.';
        isValid = false;
    }

    // Validation for position
    if (!document.getElementById('position').value.trim()) {
        document.getElementById('errorPosition').textContent = 'Chức vụ không được để trống.';
        isValid = false;
    }

    // Validation for contactPhoneNumber
    if (!contactPhoneNumber) {
        document.getElementById('errorContactNumber').textContent = 'Số điện thoại không được để trống.';
        isValid = false;
    } else if (!/^\d+$/.test(contactPhoneNumber)) { // Simple check for numeric value
        document.getElementById('errorContactNumber').textContent = 'Số điện thoại chỉ chứa số.';
        isValid = false;
    }

    // Validation for contactEmail
    if (!contactEmail) {
        document.getElementById('errorContactEmail').textContent = 'Email không được để trống.';
        isValid = false;
    } else if (!/\S+@\S+\.\S+/.test(contactEmail)) { // Simple email format check
        document.getElementById('errorContactEmail').textContent = 'Email không hợp lệ.';
        isValid = false;
    }

    if (!legalDocId) {
        document.getElementById('errorLegalDocId').textContent = 'Số CCCD/Hộ chiếu không được để trống.';
        isValid = false;
    } else if (!/^\d{9,12}$/.test(legalDocId)) { // Simple check for 9 to 12 digits
        document.getElementById('errorLegalDocId').textContent = 'Số CCCD/Hộ chiếu phải từ 9 đến 12 số.';
        isValid = false;
    }

    // Validation for issuedBy
    if (!issuedBy) {
        document.getElementById('errorIssuedBy').textContent = 'Thông tin cấp bởi không được để trống.';
        isValid = false;
    }

    // Validation for dated
    if (!dated) {
        document.getElementById('errorDated').textContent = 'Ngày cấp không được để trống.';
        isValid = false;
    }

    if (!isValid) {
        spinner.classList.remove('show');
        spinner.style.opacity = "0";
        showToast('Vui lòng nhập đủ thông tin!', "", false);
    } else {
        console.log(fileInput.value)
        let formData = new FormData();
        formData.append("file", fileInput.files[0])

        fileService.uploadFile(formData, "img")
            .then(result => {
                submitForm(result.data);
            });
    }
});

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

function collectFormData(imageUrl) {
    const packageId = document.getElementById('packageId').value;

    let amountDisplayed = document.getElementById('packageAmount').innerText;
    amountDisplayed = amountDisplayed.split('.')[0];
    const amount = parseInt(amountDisplayed, 10);

    return {
        packageId: packageId,
        imageUrl: imageUrl,
        amount: amount,
        productName: document.getElementById('productName').value,
        contactInfo: {
            fullName: document.getElementById('fullName').value,
            position: document.getElementById('position').value,
            phoneNumber: document.getElementById('contactPhoneNumber').value,
            email: document.getElementById('contactEmail').value,
            legalDocId: document.getElementById('legalDocId').value,
            issuedBy: document.getElementById('issuedBy').value,
            dated: document.getElementById('dated').value,
            address: document.getElementById('address').value,
            birthDay: document.getElementById('birthDate').value
        },
        enterpriseInfo: {
            name: document.getElementById('enterpriseName').value,
            address: document.getElementById('enterpriseAddress').value,
            phoneNumber: document.getElementById('enterprisePhoneNumber').value,
            email: document.getElementById('enterpriseEmail').value,
            websiteLink: document.getElementById('websiteLink').value,
        }
    };
}

function submitForm(imageUrl) {
    const formData = collectFormData(imageUrl);

    fetch('/api/ad-package/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
    .then(response => response.json())
    .then(data => {
        spinner.classList.remove('show');
        spinner.style.opacity = "0";
        showToast(data.message, '', false);
    })
    .catch((error) => {
        spinner.classList.remove('show');
        spinner.style.opacity = "0";
        console.error('Error:', error);
        showToast(error.message, '', false);
    });
}