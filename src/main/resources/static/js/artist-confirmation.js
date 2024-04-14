import * as fileService from "./file-service.js"

const ownershipInput = document.getElementById("ownership-input");
const addWorkButton = document.getElementById('add-work');
const spinner = document.getElementById("spinner");
spinner.classList.remove('show');
spinner.style.opacity = "0";

let selectedFiles = [];
const selectedGenres = [];
function displaySelectedImages() {
    let imageInput = document.getElementById('ownership-input');
    let imagePreviewContainer = document.getElementById('image-preview');
    imagePreviewContainer.innerHTML = ''; // xóa các ảnh hiển thị cũ
    selectedFiles = [];

    if (imageInput.files) {
        let files = Array.from(imageInput.files);

        files.forEach(function(file) {
            let reader = new FileReader();

            selectedFiles.push(file);

            reader.onload = function(e) {
                let imgElement = document.createElement('img');
                imgElement.src = e.target.result;
                imgElement.classList.add('ownership', 'col-lg-3', 'col-md-6', 'col-12');
                imagePreviewContainer.appendChild(imgElement);
            };

            reader.readAsDataURL(file);
        });
    }

    console.log(selectedFiles)
}

function addGenreTag(text, value) {
    const tagsContainer = document.querySelector('.tags');
    const tag = document.createElement('div');
    tag.className = 'genre-tag';
    tag.textContent = text + ' ';
    tag.dataset.value = value; // lưu value vào dataset để dễ xử lý khi xóa

    const removeIcon = document.createElement('i');
    removeIcon.className = 'fa-solid fa-xmark remove-tag';
    removeIcon.onclick = function() { removeGenreTag(tag, value); };

    tag.appendChild(removeIcon);
    tagsContainer.appendChild(tag);
    selectedGenres.push(value); // thêm value vào mảng
}

function removeGenreTag(tag, value) {
    tag.remove(); // Xóa thẻ
    const index = selectedGenres.indexOf(value);
    if (index > -1) {
        selectedGenres.splice(index, 1); // xóa value khỏi mảng
    }
}

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

function fetchBanksData(){
    fetch('https://api.vietqr.io/v2/banks')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const selectElement = document.getElementById('bankName');
            selectElement.innerHTML = '';

            if (data && data.data && data.data.length > 0) {
                const defaultOption = document.createElement('option');
                defaultOption.selected = true;
                defaultOption.disabled = true;
                defaultOption.textContent = 'Chọn ngân hàng';
                selectElement.appendChild(defaultOption);

                data.data.forEach((bank, index) => {
                    const option = document.createElement('option');
                    option.value = bank.id;
                    option.textContent = `[${bank.code}] - ${bank.shortName}`;
                    selectElement.appendChild(option);

                    if (index === 0) {
                        option.selected = true;
                    }
                });
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

fetchBanksData();

document.getElementById('genreIds').addEventListener('change', function() {
    const selectedOption = this.options[this.selectedIndex];
    const value = selectedOption.value;
    const text = selectedOption.textContent;

    if (value && selectedGenres.indexOf(value) === -1) { // kiểm tra xem thể loại đã được chọn chưa
        addGenreTag(text, value);
    }

    console.log(selectedGenres);
});

addWorkButton.addEventListener('click', function() {
    // Tìm số lượng các tác phẩm hiện tại để xác định id tiếp theo
    const works = document.querySelectorAll('.each-work');
    const nextId = works.length + 1;

    // Tạo một each-work mới
    const work = document.createElement('div');
    work.classList.add('each-work', 'mb-3');
    work.innerHTML = `
            <div class="work-card-title">
                <b>Tác phẩm ${nextId}</b>
            </div>
            <div class="form-group mt-2">
                <label for="workName${nextId}">Tên tác phẩm</label>
                <input type="text" class="form-control" id="workName${nextId}" name="works[${nextId-1}][name]" required>
            </div>
            <div class="form-group mt-2">
                <label for="releaseDate${nextId}">Ngày phát hành</label>
                <input type="date" class="form-control" id="releaseDate${nextId}" name="works[${nextId-1}][releaseDate]" required>
            </div>
            <div class="form-group mt-2">
                <label for="author${nextId}">Sáng tác bởi</label>
                <input type="text" class="form-control" id="author${nextId}" name="works[${nextId-1}][author]" required>
            </div>
        `;

    // Thêm nút xóa cho mỗi tác phẩm mới ngoại trừ tác phẩm đầu tiên
    if (nextId > 1) {
        const delWorkDiv = document.createElement('div');
        delWorkDiv.classList.add('del-work');
        delWorkDiv.innerHTML = `<i class="fa-solid fa-xmark"></i>`;
        work.querySelector('b').after(delWorkDiv);

        // Thêm sự kiện xóa cho nút xóa
        delWorkDiv.addEventListener('click', function() {
            work.remove();
        });
    }

    document.querySelector('.works').appendChild(work);
});


ownershipInput.addEventListener('change', displaySelectedImages);

document.getElementById('send-request').addEventListener('click', function() {
    spinner.classList.add('show');
    spinner.style.opacity = "0.5";

    const artistName = document.getElementById('artistName').value;
    const genres = selectedGenres;

    const personalInfo = {
        fullName: document.getElementById('fullName').value,
        birthDate: document.getElementById('birthDate').value,
        nation: document.getElementById('nation').value,
        phoneNumber: document.getElementById('phoneNumber').value,
        bankAccountName: document.getElementById('bankName').options[document.getElementById('bankName').selectedIndex].text, // Assuming bank account name is needed as per the structure but it's not in the form
        bankAccountNumber: document.getElementById('bankNumber').value,
        socialNets: Array.from(document.querySelectorAll('[name="personalInfo[socialNets][0][type]"]')).filter(input => input.checked).map(input => ({
            type: input.value,
            link: document.getElementById('socialLink1').value // Assumes a single link for simplicity; might need adjustment for multiple links
        }))
    };

    const legalDocs = [{
        legalDocId: document.getElementById('legalDocId').value,
        issuedBy: document.getElementById('issuedBy').value,
        dated: document.getElementById('dated').value,
        address: document.getElementById('address').value,
    }];

    const works = Array.from(document.querySelectorAll('.each-work')).map(work => ({
        name: work.querySelector('[name^="works["][name$="[name]"]').value,
        releaseDate: work.querySelector('[name^="works["][name$="[releaseDate]"]').value,
        author: work.querySelector('[name^="works["][name$="[author]"]').value,
    }));

    const ownerShips = [];

    const requestBody= {
        artistName,
        genreIds: genres,
        personalInfo,
        legalDocs,
        works,
        ownerShips
    };

    function validateData() {
        if (!artistName.trim()) {
            showToast("Vui lòng nhập tên nhạc sĩ!", "", false);
            return false;
        }

        if (!genres.length) {
            showToast("Vui lòng chọn ít nhất một thể loại sáng tác!", "", false);
            return false;
        }

        if (!personalInfo.fullName.trim() || !personalInfo.birthDate.trim() || !personalInfo.nation.trim() || !personalInfo.phoneNumber.trim()) {
            showToast("Vui lòng điền đủ thông tin cá nhân!", "", false);
            return false;
        }

        const phoneNumberRegex = /^\d{10,11}$/;
        if (!phoneNumberRegex.test(personalInfo.phoneNumber)) {
            showToast("Vui lòng điền đúng số điện thoại!", "", false);
            return false;
        }

        if (personalInfo.bankAccountName && !/^\d+$/.test(personalInfo.bankAccountNumber)) {
            showToast("Vui lòng nhập số tài khoản hợp lệ!", "", false);
            return false;
        }

        if (!legalDocs.every(doc => doc.legalDocId.trim() && doc.issuedBy.trim() && doc.dated.trim() && doc.address.trim())) {
            showToast("Vui lòng nhập đầy đủ tài liệu pháp lý!", "", false);

            return false;
        }

        if (!works.length || works.some(work => !work.name.trim() || !work.releaseDate.trim() || !work.author.trim())) {
            showToast("Vui lòng nhập đầy đủ thông tin tác phẩm!", "", false);

            return false;
        }

        return true;
    }

    async function uploadFilesAndUpdateSubmission() {
        if (!selectedFiles || selectedFiles.length === 0) {
            showToast("Vui lòng chọn file", "", false);
            spinner.classList.remove('show');
            spinner.style.opacity = "0";
            return;
        }

        try {
            const uploadPromises = selectedFiles.map(async (file) => {
                const formData = new FormData();
                formData.append('file', file);
                const uploadResult = await fileService.uploadFile(formData, 'img');
                if (!uploadResult.status) {
                    throw new Error(uploadResult.message);
                }
                return { imageUrl: uploadResult.data };
            });

            requestBody.ownerShips = await Promise.all(uploadPromises);

            const response = await fetch('http://localhost:8080/api/artist-req/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });

            const responseData = await response.json();

            if (response.ok && responseData.status) {
                showToast(responseData.message, "", false);
            } else {
                showToast(responseData.message || 'Something went wrong', "", false);
                throw new Error(responseData.message || 'Something went wrong');
            }

            spinner.classList.remove('show');
            spinner.style.opacity = "0";

        } catch (error) {
            spinner.classList.remove('show');
            spinner.style.opacity = "0";
            showToast("Có lỗi xảy ra!", "", false);
        }
    }

    if(validateData())
        uploadFilesAndUpdateSubmission();
    else{
        spinner.classList.remove('show');
        spinner.style.opacity = "0";
    }
});