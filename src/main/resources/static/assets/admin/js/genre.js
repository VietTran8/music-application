const spinner = document.getElementById("spinner");

document.getElementById('btn-add').addEventListener('click', function() {
    const name = document.getElementById('name').value.trim();
    const description = document.getElementById('description').value.trim();

    if (!name || !description) {
        alert('Vui lòng nhập đầy đủ thông tin.');
        return;
    }

    showSpinner();

    const requestBody = {
        name: name,
        description: description
    };

    fetch('/api/genre/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if(response.ok) {
                return response.json();
            } else {
                dismissSpinner();
                throw new Error('Something went wrong');
            }
        })
        .then(data => {
            dismissSpinner();
            swal(
                'Đã thêm!',
                'Thể loại đã được thêm.',
                'success'
            ).then(accept => {
                window.location.reload();
            })
        })
        .catch(error => {
            dismissSpinner();
            console.error('Error:', error);
            alert('Có lỗi xảy ra, vui lòng thử lại.');
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

document.addEventListener('DOMContentLoaded', function() {
    const editButtons = document.querySelectorAll('.btn-edit');
    const editButton = document.getElementById('btn-edit');
    const deleteButtons = document.querySelectorAll('.btn-delete');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const genreId = this.getAttribute('data-genre-id');

            swal({
                title: "Bạn có chắc là muốn xóa?",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((result) => {
                if (result) {
                    fetch(`/api/genre/delete/${genreId}`, {
                        method: 'POST',
                    })
                        .then(response => {
                            if (response.ok) {
                                return response.json();
                            } else {
                                throw new Error('Something went wrong');
                            }
                        })
                        .then(data => {
                            swal(
                                'Đã Xóa!',
                                'Thể loại đã được xóa.',
                                'success'
                            ).then(accept => {
                                window.location.reload();
                            })
                        })
                        .catch((error) => {
                            console.error('Error:', error);
                        });
                }
            })
        });
    });

    editButton.addEventListener('click', function() {
        // Validate dữ liệu
        const name = document.getElementById('editName').value.trim();
        const description = document.getElementById('editDescription').value.trim();
        if (!name || !description) {
            alert('Tên thể loại và mô tả không được để trống!');
            return;
        }
        showSpinner();
        const requestBody = {
            name: name,
            description: description
        };

        const genreId = document.getElementById('genreId').value;

        fetch(`/api/genre/update/${genreId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody),
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    dismissSpinner()
                    throw new Error('Something went wrong');
                }
            })
            .then(data => {
                dismissSpinner()
                swal(
                    'Đã cập nhật!',
                    'Thể loại đã được cập nhật.',
                    'success'
                ).then(accept => {
                    window.location.reload();
                })
            })
            .catch((error) => {
                dismissSpinner()
                console.error('Error:', error);
            });
    });

    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const genreId = this.getAttribute('data-genre-id');
            fetch(`/api/genre/${genreId}`)
                .then(response => {
                    if(response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to fetch genre data');
                    }
                })
                .then(data => {
                    const { id, name, description } = data.data;
                    document.getElementById('editName').value = name;
                    document.getElementById('editDescription').value = description;
                    document.getElementById('genreId').value = id;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    });
});