const spinner = document.getElementById("spinner");

document.addEventListener('DOMContentLoaded', function() {
    const btnAdd = document.getElementById('btn-add');
    const editButtons = document.querySelectorAll('.btn-edit');


    document.getElementById('btn-edit').addEventListener('click', function() {
        // Validate dữ liệu từ form ở đây...
        const name = document.getElementById('editPackageName').value.trim();
        const description = document.getElementById('editPackageDescription').value.trim();
        const price = parseFloat(document.getElementById('editPackagePrice').value);
        const discount = parseInt(document.getElementById('editPackageDiscount').value);
        const specialFeatures = document.getElementById('editSpecialFeatures').value.trim();
        const type = document.getElementById('editSelectPackageType').value;
        const duration = parseInt(document.getElementById('editDuration').value);
        const packageId = document.getElementById('package-id').value;

        console.log(duration);
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
        if (!duration || isNaN(duration) || duration <= 0) {
            errorMessage += 'Thời gian phải là một số dương.\n';
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
                        duration: parseInt(document.getElementById('editDuration').value),
                    };

                    fetch(`/api/package/update/${packageId}`, {
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
            const packageId = this.getAttribute('data-pack-id'); // Lấy ID của gói từ thuộc tính data

            swal({
                title: "Bạn có chắc chắn?",
                text: "Khi đã xóa, bạn sẽ không thể khôi phục lại thông tin!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        fetch(`/api/package/delete/${packageId}`, {
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
            fetch(`/api/package/${packageId}`)
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
                    document.getElementById('editDuration').value = data.duration;

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
        const duration = document.getElementById('duration').value;

        if (!packageName || !packageDescription || !packagePrice || !specialFeatures || !selectPackageType || !duration) {
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
            duration: parseInt(duration)
        };

        fetch('/api/package/add', {
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