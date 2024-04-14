import * as fileService from "../../../js/file-service.js";
import * as songService from "../../../js/song-service.js";

const spinner = document.getElementById("spinner");

document.addEventListener('DOMContentLoaded', function() {
    const viewButtons = document.querySelectorAll('.btn-view');
    const btnAdd = document.getElementById('btn-add');

    btnAdd.addEventListener("click" , () => {
        if(validateForm()){
            showSpinner();
            addSong();
        }
    })

    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', function() {
            const songId = this.getAttribute('data-song-id'); // Lấy ID của bài hát từ attribute của nút

            swal({
                title: "Bạn có chắc là muốn xóa bài hát?",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
                    fetch(`http://localhost:8080/api/song/delete/${songId}`, {
                        method: 'POST',
                    }).then(response => {
                        if (response.ok) {
                            swal(
                                'Đã xóa!',
                                'Nhạc đã được xóa.',
                                'success'
                            ).then(accept => {
                                window.location.reload();
                            })
                        } else {
                            swal("Xảy ra lỗi khi xóa bài hát!", {
                                icon: "error",
                            });
                        }
                    });
                }
            });
        });
    });

    document.querySelectorAll('.btn-edit').forEach(button => {
        button.addEventListener('click', function() {
            const songId = this.getAttribute('data-song-id');

            fetch(`http://localhost:8080/api/song/${songId}`)
                .then(response => response.json())
                .then(response => {
                    const data = response.data;
                    const releaseDate = convertDateToISO(data.releaseDate);

                    document.getElementById('updatedName').value = data.name || '';
                    document.getElementById('updatedLyrics').value = data.lyrics.replaceAll('<br>', '\n') || '';
                    document.getElementById('updatedReleaseDate').value = releaseDate || '';
                    document.getElementById('updatedIsPremium').checked = data.isPremium || false;
                    document.getElementById('old-audio-file').value = data.audioUrl;
                    document.getElementById('old-image-file').value = data.imageUrl;
                    document.getElementById('update-song-id').value = data.id;
                    document.getElementById('updatedAudioUrl').value = '';
                    document.getElementById('updatedImageUrl').value = '';

                    if (data.author) {
                        document.getElementById('updatedAuthor').value = data.author;
                    }

                    const artistSelect = document.getElementById('updatedArtistId');
                    Array.from(artistSelect.options).forEach(option => {
                        if (data.artists.some(artist => artist.id.toString() === option.value)) {
                            artistSelect.value = option.value; // Giả sử mỗi bài hát chỉ có một nghệ sĩ
                        }
                    });

                    const genreSelect = document.getElementById('updatedGenreId');
                    genreSelect.value = data.genre.id.toString();

                    if (data.album) {
                        const albumSelect = document.getElementById('updatedAlbumId');
                        albumSelect.value = data.album.id.toString();
                    }

                    $('#editSongModal').modal('show');
                })
                .catch(error => {
                    console.error('Error fetching song details:', error);
                });
        });
    });

    document.getElementById('btn-edit').addEventListener('click', async () => {
        const songId = document.getElementById('update-song-id').value;
        const updatedName = document.getElementById('updatedName').value;
        const updatedLyrics = document.getElementById('updatedLyrics').value.trim().replaceAll('\n', '<br>');
        const updatedArtistId = document.getElementById('updatedArtistId').value;
        const updatedGenreId = document.getElementById('updatedGenreId').value;
        const updatedAuthor = document.getElementById('updatedAuthor').value;
        const updatedReleaseDate = document.getElementById('updatedReleaseDate').value;
        const updatedIsPremium = document.getElementById('updatedIsPremium').checked;
        const updatedAlbumId = document.getElementById('updatedAlbumId').value;
        let audioUrl = document.getElementById('old-audio-file').value;
        let imageUrl = document.getElementById('old-image-file').value;

        if(validateUpdateSongDetails()){
            showSpinner();

            if (document.getElementById('updatedAudioUrl').files.length > 0) {
                const audioFile = document.getElementById('updatedAudioUrl').files[0];
                const formData = new FormData();
                formData.append('file', audioFile);
                const audioResponse = await fileService.uploadFile(formData, "audio");
                audioUrl = audioResponse.data;
            }

            if (document.getElementById('updatedImageUrl').files.length > 0) {
                const imageFile = document.getElementById('updatedImageUrl').files[0];
                const formData = new FormData();
                formData.append('file', imageFile);
                const imageResponse = await fileService.uploadFile(formData, "img");
                imageUrl = imageResponse.data;
            }

            console.log(imageUrl)
            console.log(audioUrl)

            const requestBody = {
                name: updatedName,
                lyrics: updatedLyrics,
                artistIds: [parseInt(updatedArtistId)],
                genreId: parseInt(updatedGenreId),
                albumId: parseInt(updatedAlbumId),
                isPremium: updatedIsPremium,
                releaseDate: formatDate(updatedReleaseDate),
                author: updatedAuthor,
                audioUrl: audioUrl,
                imageUrl: imageUrl
            };

            // console.log(requestBody);

            songService.updateSong(songId, requestBody)
                .then(result => {
                    if(result.status){
                        dismissSpinner();
                        swal(
                            'Đã cập nhật!',
                            'Nhạc đã được cập nhật.',
                            'success'
                        ).then(accept => {
                            window.location.reload();
                        })
                    }
                    showToast(result.message)
                })
        }
    });



    viewButtons.forEach(
btn => btn.addEventListener('click', function() {
                const songId = this.getAttribute('data-song-id');

                console.log(songId);

                fetch(`/api/song/${songId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        const songData = data.data;

                        document.getElementById('songImage').src = songData.imageUrl;
                        document.getElementById('songName').textContent = songData.name;
                        document.getElementById('releaseDateInfo').textContent = songData.releaseDate;
                        document.getElementById('genreInfo').textContent = songData.genre.name;
                        document.getElementById('artistName').textContent = songData.artistNameString;
                        document.getElementById('lyricsInfo').innerHTML = songData.lyrics.replace(/\n/g, '<br>');
                        document.getElementById('isPremiumInfo').textContent = songData.isPremium ? 'Có' : 'Không';
                        document.getElementById('favourites').textContent = songData.favourites;
                    })
                    .catch(error => console.error('Error:', error));
            })
        )
})

function validateUpdateSongDetails() {
    const updatedName = document.getElementById('updatedName').value;
    const updatedLyrics = document.getElementById('updatedLyrics').value;
    const updatedArtistId = document.getElementById('updatedArtistId').value;
    const updatedGenreId = document.getElementById('updatedGenreId').value;
    const updatedReleaseDate = document.getElementById('updatedReleaseDate').value;

    let isValid = true;
    const errorMessage = [];

    if (!updatedName.trim()) {
        errorMessage.push('Tên bài hát không được để trống.');
        isValid = false;
    }

    if (!updatedLyrics.trim()) {
        errorMessage.push('Lời bài hát không được để trống.');
        isValid = false;
    }

    if (updatedArtistId === '0') {
        errorMessage.push('Bạn chưa chọn nghệ sĩ.');
        isValid = false;
    }

    if (updatedGenreId === '0') {
        errorMessage.push('Bạn chưa chọn thể loại.');
        isValid = false;
    }

    if (!updatedReleaseDate) {
        errorMessage.push('Ngày phát hành không được để trống.');
        isValid = false;
    } else {
        const releaseDate = new Date(updatedReleaseDate);
        const today = new Date();
        if (releaseDate > today) {
            errorMessage.push('Ngày phát hành không thể là một ngày trong tương lai.');
            isValid = false;
        }
    }

    if (!isValid) {
        alert(errorMessage.join('\n'));
    }

    return isValid;
}


function addSong() {
    const audioFile = document.getElementById('audioUrl').files[0];
    const imageFile = document.getElementById('imageUrl').files[0];


    let requestBody = {
        name: document.getElementById('name').value.trim(),
        author: document.getElementById('author').value.trim(),
        releaseDate: formatDate(document.getElementById('releaseDate').value.trim()),
        lyrics: document.getElementById('lyrics').value.trim().replaceAll('\n', '<br>'),
        audioUrl: "",
        imageUrl: "",
        genreId: parseInt(document.getElementById('genreId').value),
        albumId: parseInt(document.getElementById('albumId').value),
        artistIds: [parseInt(document.getElementById('artistId').value)],
        isPremium: document.getElementById('isPremium').checked
    }

    async function uploadFilesAndSetRequestBody(audioFile, imageFile) {
        const audioFormData = new FormData();
        audioFormData.set("file", audioFile);

        const imageFormData = new FormData();
        imageFormData.set("file", imageFile);

        const [audioUploadResult, imageUploadResult] = await Promise.all([
            fileService.uploadFile(audioFormData, "audio"),
            fileService.uploadFile(imageFormData, "img")
        ]);

        requestBody.audioUrl = audioUploadResult.data;
        requestBody.imageUrl = imageUploadResult.data;
    }

    console.log(requestBody)

    uploadFilesAndSetRequestBody(audioFile, imageFile)
        .then(() => {
            console.log('Cả hai file đã được upload và requestBody đã được cập nhật');
            songService.addSong(requestBody)
                .then(result => {
                    spinner.classList.remove('show');
                    spinner.style.opacity = "0";
                    showToast(result.message, "", false)
                    swal(
                        'Đã thêm!',
                        'Nhạc đã được thêm.',
                        'success'
                    ).then(accept => {
                        window.location.reload();
                    })
                });
        })
        .catch(error => {
            dismissSpinner();
            console.error('Đã xảy ra lỗi trong quá trình upload:', error);
        });
}

function validateForm() {
    const name = document.getElementById('name').value.trim();
    const author = document.getElementById('author').value.trim();
    const releaseDate = document.getElementById('releaseDate').value.trim();
    const lyrics = document.getElementById('lyrics').value.trim();
    const audioUrl = document.getElementById('audioUrl').value;
    const imageUrl = document.getElementById('imageUrl').value;
    const genreId = document.getElementById('genreId').value;

    if (!name || !author || !releaseDate || !lyrics || !audioUrl || !imageUrl || !genreId) {
        alert('Vui lòng nhập đầy đủ thông tin.')
        return false;
    }

    const date = new Date(releaseDate);
    const today = new Date();
    if (date > today) {
        alert('Ngày phát hành không thể là một ngày trong tương lai.');
        return false;
    }

    return true;
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

function closeModal(){
    let myModalEl = document.getElementById('addSongModal');
    let modal = bootstrap.Modal.getInstance(myModalEl);
    modal.hide();
}

function formatDate(rawReleaseDate){
    const releaseDateObj = new Date(rawReleaseDate);

    function padTo2Digits(num) {
        return num.toString().padStart(2, '0');
    }

    return [
        releaseDateObj.getFullYear(),
        padTo2Digits(releaseDateObj.getMonth() + 1),
        padTo2Digits(releaseDateObj.getDate()),
    ].join('-');
}

function convertDateToISO(dateString) {
    const parts = dateString.split('/');
    return `${parts[2]}-${parts[1]}-${parts[0]}`;
}

function showSpinner(){
    spinner.classList.add('show');
    spinner.style.opacity = "0.5";
}

function dismissSpinner(){
    spinner.classList.remove('show');
    spinner.style.opacity = "0";
}