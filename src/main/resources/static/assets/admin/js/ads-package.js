const spinner = document.getElementById("spinner");

document.addEventListener('DOMContentLoaded', function() {
    const btnAdd = document.getElementById('btn-add');
    const editButtons = document.querySelectorAll('.btn-edit');

    document.querySelectorAll('.btn-approve, .btn-reject').forEach(button => {
        button.addEventListener('click', function () {
            const requestId = this.getAttribute('data-request-id');
            const isApproval = this.classList.contains('btn-approve');
            const action = isApproval ? 'Phê duyệt' : 'Từ chối';

            swal({
                title: `Bạn có chắc muốn ${action.toLowerCase()} yêu cầu này?`,
                text: "Nhập lý do (nếu có):",
                content: "input",
                button: {
                    text: action,
                    closeModal: false,
                },
            })
                .then(reason => {
                    if (!reason) throw null;

                    const status = isApproval ? 'APPROVED' : 'NOT_APPROVED';
                    return fetch(`/api/ad-package/approval`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            adId: requestId,
                            reason: reason,
                            status: status
                        })
                    });
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(json => {
                    swal(`${action} thành công!`, {
                        icon: "success",
                    }).then(result => {
                        location.reload();
                    });
                })
                .catch(err => {
                    if (err) {
                        swal("Lỗi", "Có lỗi xảy ra, vui lòng thử lại!", "error");
                    } else {
                        swal.stopLoading();
                        swal.close();
                    }
                });
        });
    });

    let requestInfos = document.querySelectorAll('.request-info');
    requestInfos.forEach(modal => {
        modal.addEventListener('click', function (event) {
            let requestId = this.getAttribute('data-request-id');
            let apiUrl = '/api/ad-package/ad/' + requestId;

            fetch(apiUrl)
                .then(response => response.json())
                .then(data => {
                    if (data && data.status && data.data) {
                        let adData = data.data;
                        document.getElementById('requestId').textContent = adData.id;
                        document.getElementById('adBoughtDate').textContent = new Date(adData.boughtDate).toLocaleString();
                        document.getElementById('adExpirationDate').textContent = new Date(adData.expirationDate).toLocaleString();
                        document.getElementById('paymentMethod').textContent = adData.paymentMethod;
                        document.getElementById('amount').textContent = new Intl.NumberFormat('vi-VN', {
                            style: 'currency',
                            currency: 'VND'
                        }).format(adData.amount);
                        document.getElementById('status').textContent = adData.stringStatus;
                        document.getElementById('productName').textContent = adData.productName;
                        document.getElementById('productImage').src = adData.imageUrl;
                        document.getElementById('fullName').textContent = adData.contactInfo.fullName;
                        document.getElementById('birthDate').textContent = adData.contactInfo.birthDay;
                        document.getElementById('position').textContent = adData.contactInfo.position;
                        document.getElementById('phoneNumber').textContent = adData.contactInfo.phoneNumber;
                        document.getElementById('email').textContent = adData.contactInfo.email;
                        document.getElementById('legalDocId').textContent = adData.contactInfo.legalDocId;
                        document.getElementById('issuedBy').textContent = adData.contactInfo.issuedBy;
                        document.getElementById('dated').textContent = adData.contactInfo.dated;
                        document.getElementById('address').textContent = adData.contactInfo.address;
                        document.getElementById('enterpriseName').textContent = adData.enterpriseInfo.name;
                        document.getElementById('enterpriseAddress').textContent = adData.enterpriseInfo.address;
                        document.getElementById('enterprisePhoneNumber').textContent = adData.enterpriseInfo.phoneNumber;
                        document.getElementById('enterpriseEmail').textContent = adData.enterpriseInfo.email;
                        document.getElementById('enterpriseWebsite').href = adData.enterpriseInfo.websiteLink;
                        document.getElementById('enterpriseWebsite').textContent = adData.enterpriseInfo.websiteLink;
                    }
                })
                .catch(error => console.error('Error fetching ad data:', error));
        });
    });

    document.getElementById('btn-edit').addEventListener('click', function() {
        // Validate dữ liệu từ form ở đây...
        const name = document.getElementById('editPackageName').value.trim();
        const description = document.getElementById('editPackageDescription').value.trim();
        const price = parseFloat(document.getElementById('editPackagePrice').value);
        const discount = parseInt(document.getElementById('editPackageDiscount').value);
        const specialFeatures = document.getElementById('editSpecialFeatures').value.trim();
        const type = document.getElementById('editSelectPackageType').value;
        const unit = document.getElementById('editSelectPackageUnit').value;
        const packageId = document.getElementById('package-id').value;

        let errorMessage = '';
        if (!name) {
            errorMessage += 'Tên gói không được để trống.\n';
        }
        if (!description) {
            errorMessage += 'Mô tả gói không được để trống.\n';
        }
        if (!price || isNaN(price) || price <= 0) {
            errorMessage += 'Giá tiền phải là một số dương.\n';
        }
        if (!discount || isNaN(discount) || discount < 0 || discount > 100) {
            errorMessage += 'Giảm giá phải là một số từ 0 đến 100.\n';
        }
        if (!specialFeatures) {
            errorMessage += 'Tính năng đặc biệt không được để trống.\n';
        }
        if (!unit) {
            errorMessage += 'Thời gian không được để trống.\n';
        }
        if (!type) {
            errorMessage += 'Vui lòng nhập loại gói.\n';
        }

        // Nếu có lỗi, hiển thị thông báo và không tiếp tục
        if (errorMessage) {
            swal("Lỗi!", errorMessage, "error");
            return;
        }

        swal({
            title: "Bạn có chắc không?",
            text: "Bạn sẽ không thể hoàn nguyên điều này!",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
            .then((willUpdate) => {
                if (willUpdate) {
                    const updatedPackage = {
                        name: document.getElementById('editPackageName').value,
                        description: document.getElementById('editPackageDescription').value,
                        price: parseFloat(document.getElementById('editPackagePrice').value),
                        discount: parseFloat(document.getElementById('editPackageDiscount').value),
                        specialFeatures: document.getElementById('editSpecialFeatures').value,
                        type: document.getElementById('editSelectPackageType').value,
                        unit: unit,
                    };

                    fetch(`/api/ad-package/update/${packageId}`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(updatedPackage),
                    })
                        .then(response => response.json())
                        .then(data => {
                            swal("Cập nhật!", "Gói dịch vụ đã được cập nhật thành công.", "success")
                                .then(result => {
                                    location.reload();
                                });
                        })
                        .catch((error) => {
                            console.error('Error:', error);
                            swal("Lỗi!", "Có lỗi xảy ra khi cập nhật gói dịch vụ.", "error");
                        });
                } else {
                    swal("Thao tác đã bị hủy!");
                }
            });
    });

    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', function() {
            const packageId = this.getAttribute('data-pack-id');

            swal({
                title: "Bạn có chắc chắn?",
                text: "Khi đã xóa, bạn sẽ không thể khôi phục lại thông tin!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        fetch(`/api/ad-package/delete/${packageId}`, {
                            method: 'POST', // Hoặc 'DELETE', tùy thuộc vào API của bạn
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.json();
                            })
                            .then(data => {
                                swal("Đã xóa!", "Gói dịch vụ đã được xóa thành công.", "success")
                                    .then(result => {
                                        location.reload();
                                    });
                            })
                            .catch((error) => {
                                console.error('Error:', error);
                                swal("Lỗi!", "Có lỗi xảy ra, không thể xóa gói dịch vụ.", "error");
                            });
                    } else {
                        swal("Thao tác đã bị hủy!");
                    }
                });
        });
    });

    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const packageId = this.getAttribute('data-pack-id');
            fetch(`/api/ad-package/${packageId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    data = data.data
                    document.getElementById('package-id').value = data.id;
                    document.getElementById('editPackageName').value = data.name;
                    document.getElementById('editPackageDescription').value = data.description;
                    document.getElementById('editPackagePrice').value = data.price;
                    document.getElementById('editPackageDiscount').value = data.discount;
                    document.getElementById('editSpecialFeatures').value = data.specialFeatures;
                    document.getElementById('editSelectPackageType').value = data.type;
                    document.getElementById('editSelectPackageUnit').value = data.unit;

                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("Có lỗi xảy ra khi tải thông tin gói dịch vụ.");
                });
        });
    });

    btnAdd.addEventListener('click', function() {
        // Validate dữ liệu trước khi submit
        const packageName = document.getElementById('packageName').value;
        const packageDescription = document.getElementById('packageDescription').value;
        const packagePrice = document.getElementById('packagePrice').value;
        const packageDiscount = document.getElementById('packageDiscount').value;
        const specialFeatures = document.getElementById('specialFeatures').value;
        const selectPackageType = document.getElementById('selectPackageType').value;
        const unit = document.getElementById('selectPackageUnit').value;

        if (!packageName || !packageDescription || !packagePrice || !specialFeatures || !selectPackageType || !unit) {
            alert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if(parseInt(packageDiscount) > 100 || parseInt(packageDiscount) < 0){
            alert("Thông tin giảm giá không hợp lệ");
            return;
        }

        showSpinner();
        const dataToSend = {
            name: packageName,
            description: packageDescription,
            price: parseFloat(packagePrice),
            discount: parseInt(packageDiscount),
            specialFeatures: specialFeatures,
            type: selectPackageType,
            unit: unit
        };

        fetch('/api/ad-package/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dataToSend)
        })
            .then(response => {
                if (!response.ok) {
                    dismissSpinner();
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                dismissSpinner();
                swal(
                    'Đã thêm!',
                    'Gói đã được thêm',
                    'success'
                ).then(accept => {
                    window.location.reload();
                })
            })
            .catch((error) => {
                dismissSpinner();
                alert("Có lỗi xảy ra khi thêm gói.");
            });
    });
});

function showSpinner(){
    spinner.classList.add('show');
    spinner.style.opacity = "0.5";
}

function dismissSpinner(){
    spinner.classList.remove('show');
    spinner.style.opacity = "0";
}