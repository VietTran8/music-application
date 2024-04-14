function deleteUser(){

}

function updateRoleCheckboxes(clickedElement, userId) {
    const rolesText = clickedElement.innerText;

    document.getElementById('roleUser').checked = false;
    document.getElementById('roleAdmin').checked = false;
    document.getElementById('roleArtist').checked = false;

    if (rolesText.includes('Người dùng')) {
        document.getElementById('roleUser').checked = true;
    }
    if (rolesText.includes('Quản trị viên')) {
        document.getElementById('roleAdmin').checked = true;
    }
    if (rolesText.includes('Nghệ sĩ')) {
        document.getElementById('roleArtist').checked = true;
    }

    document.getElementById('userId').value = userId
}

function disableUser(element, userId) {
    swal({
        title: element.classList.contains("btn-danger") ? "Bạn có chắc là muốn khóa tài khoản?" : "Bạn có chắc là muốn mở khóa tài khoản?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                const url = `http://localhost:8080/api/user/disable/${userId}`;

                const options = {
                    method: "POST"
                };

                fetch(url, options)
                    .then(response => response.json())
                    .then(data => {
                        if (data.code === 200) {
                            swal(
                                'Đã cập nhật!',
                                'Trạng thái đã được được cập nhật.',
                                'success'
                            ).then(accept => {
                                window.location.reload();
                            })
                        }else{
                            showToast(data.message, "", false)
                        }
                    })
                    .catch(error => console.error('Error disabling user:', error));
            }
        });
}

function saveRoleChanges() {
    swal({
        title: "Bạn có chắc là muốn cập nhật vai trò người dùng?",
        buttons: true
    }).then(async confirm => {
        if (confirm) {
            const userId = document.getElementById('userId').value;

            const roles = [];
            if (document.getElementById('roleUser').checked) roles.push('ROLE_USER');
            if (document.getElementById('roleAdmin').checked) roles.push('ROLE_ADMIN');
            if (document.getElementById('roleArtist').checked) roles.push('ROLE_ARTIST');

            if (roles.length === 0) {
                alert('Vui lòng chọn ít nhất một vai trò.');
                return;
            }

            const requestBody = {
                userId,
                roles
            };

            try {
                const response = await fetch('/api/user/role/update', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(requestBody)
                });
                const data = await response.json();

                if (data.status) {
                    swal(
                        'Đã cập nhật!',
                        'Vai trò đã được cập nhật.',
                        'success'
                    ).then(accept => {
                        window.location.reload();
                    })
                } else {
                    showToast('Có lỗi xảy ra: ' + data.message, "", false);
                }
            } catch (error) {
                showToast('Có lỗi xảy ra khi gửi yêu cầu: ' + error, "", false);
            }
        }
    })
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

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('saveRoleChangeBtn').addEventListener('click', saveRoleChanges);
});