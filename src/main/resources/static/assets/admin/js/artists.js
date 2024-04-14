import * as fileService from "../../../js/file-service.js";

const spinner = document.getElementById("spinner");
document.addEventListener("DOMContentLoaded", () => {
    let selectedGenres = [];
    let editGenres = [];

    const genreSelect = document.getElementById("genreSelect");
    const editGenreSelect = document.getElementById('editGenreSelect');
    const selectedGenresContainer = document.getElementById("selectedGenres");
    const editSelectedGenresContainer = document.getElementById("editSelectedGenres");
    const btnEditList = document.querySelectorAll('.btn-edit');
    const btnEdit = document.getElementById('btn-edit');
    const editArtistId = document.getElementById('editArtistId');
    const deleteButtons = document.querySelectorAll('.btn-delete');
    const revokeButtons = document.querySelectorAll('.btn-revoke'); // Lấy tất cả các nút có lớp btn-revoke

    revokeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const requestId = this.getAttribute('data-request-id');

            swal({
                title: "Bạn có chắc chắn?",
                text: "Bạn sẽ thu hồi quyền nghệ sĩ cho yêu cầu này!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        fetch(`/api/artist-req/revoke/${requestId}`, {
                            method: 'POST',
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.json();
                            })
                            .then(data => {
                                swal("Quyền nghệ sĩ đã được thu hồi!", {
                                    icon: "success",
                                }).then((confirm) => {
                                    location.reload();
                                });
                            })
                            .catch(error => {
                                swal("Lỗi!", "Có lỗi xảy ra trong quá trình thu hồi.", "error");
                            });
                    }
                });
        });
    });

    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const artistId = this.getAttribute('data-artist-id');

            swal({
                title: "Bạn có chắc là muốn xóa?",
                text: "Một khi đã xóa, bạn sẽ không thể khôi phục lại!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        fetch(`/api/artist-info/delete/${artistId}`, {
                            method: 'POST',
                        })
                            .then(response => {
                                if(response.ok) {
                                    swal(
                                        'Đã xóa!',
                                        'Nghệ sĩ đã được xóa.',
                                        'success'
                                    ).then(accept => {
                                        window.location.reload();
                                    })
                                } else {
                                    swal("Xóa thất bại!", {
                                        icon: "error",
                                    });
                                }
                            })
                            .catch(error => console.error('Error:', error));
                    }
                });
        });
    });

    document.querySelectorAll('.request-info').forEach(function(wrapper) {
        wrapper.addEventListener('click', function() {
            var requestId = this.getAttribute('data-request-id');

            fetch(`/api/artist-req/${requestId}`)
                .then(response => response.json())
                .then(data => {
                    populateModal(data.data);
                })
                .catch(error => console.error('Error fetching details:', error));
        });
    });

    btnEdit.addEventListener('click', async function () {
        const editArtistName = document.getElementById('editArtistName').value.trim();
        const imageFile = document.getElementById('newImage').files[0];
        let imageUrl = document.getElementById('oldImage').value;

        if (!editArtistName || editGenres.length === 0) {
            alert('Vui lòng điền đầy đủ thông tin.');
            return;
        }

        showSpinner();
        if (imageFile) {
            let formData = new FormData();
            formData.append('file', imageFile);

            const response = await fileService.uploadFile(formData, "img");

            imageUrl = response.data;
        }

        const requestBody = {
            artistName: editArtistName,
            image: imageUrl,
            genreIds: editGenres
        };

        fetch(`/api/artist-info/update/${editArtistId.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody),
        })
            .then(response => response.json())
            .then(data => {
                dismissSpinner();
                swal(
                    'Đã cập nhật!',
                    'Nghệ sĩ đã được cập nhật.',
                    'success'
                ).then(accept => {
                    window.location.reload();
                })
            })
            .catch((error) => {
                dismissSpinner();
                alert(error)
            });
    })

    btnEditList.forEach(btn => {
        btn.addEventListener('click', function() {
            editSelectedGenresContainer.innerHTML = '';
            editGenres = [];
            const artistId = this.getAttribute('data-artist-id');
            editArtistId.value = artistId;
            
            fetch(`/api/artist-info/${artistId}`).then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const artistData = data.data;

                document.getElementById('editArtistName').value = artistData.artistName;
                document.getElementById('oldImage').value = artistData.image;

                artistData.genres.forEach(genre => {
                    editGenres.push(parseInt(genre.id));
                    const genreTag = document.createElement("span");
                    genreTag.textContent = genre.name + " ";
                    genreTag.className = "badge text-dark me-2 d-flex align-items-center";
                    genreTag.dataset.id = genre.id;

                    const removeButton = document.createElement("button");
                    removeButton.innerHTML = "&times;";
                    removeButton.className = "btn btn-sm ms-2";
                    removeButton.onclick = function() {
                        editGenres = editGenres.filter(id => id !== parseInt(genreTag.dataset.id));
                        genreTag.remove();
                    };

                    genreTag.appendChild(removeButton);
                    editSelectedGenresContainer.appendChild(genreTag);
                });

                console.log(editGenres);
            })
            .catch(error => console.error('Error:', error));
        })
    })

    selectedGenres = [];

    genreSelect.addEventListener("change", () => {
        const genreId = genreSelect.value;
        const genreName = genreSelect.options[genreSelect.selectedIndex].text;

        if (!selectedGenres.includes(genreId) && genreId) {
            selectedGenres.push(genreId);

            const genreTag = document.createElement("span");
            genreTag.textContent = genreName + " ";
            genreTag.className = "badge text-dark me-2 d-flex align-items-center";
            genreTag.dataset.id = genreId;

            const removeButton = document.createElement("button");
            removeButton.innerHTML = "&times;";
            removeButton.className = "btn btn-sm ms-2";
            removeButton.onclick = function() {
                selectedGenres = selectedGenres.filter(id => id !== genreTag.dataset.id);
                genreTag.remove();
            };

            genreTag.appendChild(removeButton);
            selectedGenresContainer.appendChild(genreTag);
        }
        console.log(selectedGenres);
    });

    editGenreSelect.addEventListener("change", () => {
        const genreId = editGenreSelect.value;
        const genreName = editGenreSelect.options[editGenreSelect.selectedIndex].text;

        if (!editGenres.includes(parseInt(genreId)) && genreId) {
            editGenres.push(parseInt(genreId));

            const genreTag = document.createElement("span");
            genreTag.textContent = genreName + " ";
            genreTag.className = "badge text-dark me-2 d-flex align-items-center";
            genreTag.dataset.id = genreId;

            const removeButton = document.createElement("button");
            removeButton.innerHTML = "&times;";
            removeButton.className = "btn btn-sm ms-2";
            removeButton.onclick = function() {
                editGenres = editGenres.filter(id => id !== parseInt(genreTag.dataset.id));
                genreTag.remove();
            };

            genreTag.appendChild(removeButton);
            editSelectedGenresContainer.appendChild(genreTag);
        }
        console.log(editGenres);
    });

    const approveButtons = document.querySelectorAll('.btn-approve');

    const rejectButtons = document.querySelectorAll('.btn-reject'); // Lấy tất cả các nút có lớp btn-reject

    rejectButtons.forEach(button => {
        button.addEventListener('click', function() {
            const requestId = this.getAttribute('data-request-id');

            swal({
                title: "Bạn có chắc là muốn từ chối tài khoản này?",
                text: "Nhập lý do:",
                content: "input",
                button: {
                    text: "Từ chối",
                    closeModal: false,
                },
            })
                .then((reason) => {
                    if (!reason) throw null;

                    showSpinner();

                    return fetch('/api/artist-req/approval', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            requestId: requestId,
                            reason: reason,
                            status: 'NOT_APPROVED'
                        })
                    });
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
                    swal("Thành công!", {
                        icon: "success",
                    }).then(() => {
                        location.reload();
                    });
                })
                .catch(error => {
                    dismissSpinner();
                    if (error) {
                        swal("Lỗi!", "Có lỗi xảy ra trong quá trình từ chối.", "error");
                    }
                });
        });
    });

    approveButtons.forEach(button => {
        button.addEventListener('click', function() {
            const requestId = this.getAttribute('data-request-id');

            swal({
                title: "Bạn có chắc là muốn phê duyệt tài khoản này trở thành nghệ sĩ?",
                text: "Nhập lý do:",
                content: "input",
                button: {
                    text: "Phê duyệt",
                    closeModal: false,
                },
            })
                .then((reason) => {
                    if (!reason) throw null;

                    showSpinner();
                    return fetch('/api/artist-req/approval', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            requestId: requestId,
                            reason: reason,
                            status: 'APPROVED'
                        })
                    });
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
                    swal("Phê duyệt thành công!", {
                        icon: "success",
                    }).then(() => {
                        location.reload();
                    });
                })
                .catch(error => {
                    if (error) {
                        dismissSpinner();
                        swal("Lỗi!", "Có lỗi xảy ra trong quá trình phê duyệt.", "error");
                    } else {
                        dismissSpinner();
                    }
                });
        });
    });

    document.getElementById('btn-add').addEventListener('click', function() {
        const artistName = document.getElementById('artistName').value.trim();
        const imageFile = document.getElementById('image').files[0];
        const genreBadges = document.querySelectorAll('#selectedGenres .badge');

        if (!artistName || !imageFile || selectedGenres.length === 0) {
            alert('Vui lòng điền đầy đủ thông tin.');
            return;
        }

        showSpinner();
        let formData = new FormData();
        formData.append('file', imageFile);

        fileService.uploadFile(formData, "img").then(response => {
            const imageUrl = response.data;

            const requestBody = {
                artistName: artistName,
                image: imageUrl,
                genreIds: selectedGenres
            };

            fetch('/api/artist-info/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            })
                .then(response => response.json())
                .then(data => {
                    dismissSpinner();
                    swal(
                        'Đã thêm!',
                        'Nhạc sĩ đã được thêm.',
                        'success'
                    ).then(accept => {
                        window.location.reload();
                    })
                })
                .catch((error) => {
                    dismissSpinner();
                    alert(error)
                });
        }).catch(error => {
            dismissSpinner();
            console.log(error);
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

function populateModal(data) {
    document.getElementById('reqStatus').textContent = data.stringStatus || 'N/A';
    document.getElementById('reqDate').textContent = data.requestedDate || 'N/A';
    document.getElementById('reqEmail').textContent = data.user.email || 'N/A';
    document.getElementById('reqUsername').textContent = data.user.username || 'N/A';
    document.getElementById('reqArtistImage').src = data.artistInfo.image || '';

    let worksList = document.getElementById('works');
    worksList.innerHTML = '';
    data.works.forEach(work => {
        let li = document.createElement('li');
        li.textContent = `${work.name} - ${work.releaseDate} - ${work.author}`;
        worksList.appendChild(li);
    });

    let imagePreviewContainer = document.getElementById('image-preview');
    imagePreviewContainer.innerHTML = '';
    data.ownerships.forEach(ownership => {
        let img = document.createElement('img');
        img.src = ownership.imageUrl;
        img.classList.add('img-fluid');
        img.alt = "Ownership Image";
        imagePreviewContainer.appendChild(img);
    });

    const personalInfo = data.artistInfo.personalInfo || {};
    document.getElementById('reqFullname').textContent = personalInfo.fullName || 'N/A';
    document.getElementById('reqBirthDate').textContent = personalInfo.birthDate || 'N/A';
    document.getElementById('reqNation').textContent = personalInfo.nationality || 'N/A';
    document.getElementById('reqPhoneNumber').textContent = personalInfo.phoneNumber || 'N/A';
    document.getElementById('reqBankName').textContent = personalInfo.bankAccountName || 'N/A';
    document.getElementById('reqBankNumber').textContent = personalInfo.bankAccountNumber || 'N/A';
}