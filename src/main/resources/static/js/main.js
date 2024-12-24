import * as songService from './song-service.js'
import * as genreScript from './genre.js';
import * as playlistService from './playlist-service.js'
import * as userService from "./user-service.js"
import * as fileService from "./file-service.js"
import * as albumService from "./album-service.js"

let currentSong = null;
let currentPage = 1;
let songs = await songService.getAllSong(currentSong, 6);
let songIndex = 0;
let isRepeatInList = true;
let totalListenedTime = 0;
let lastUpdateTime = 0;
let isNewPlayCount = true;
const listenThreshold = 0.9;

const playBtn = document.querySelector("#playBtn");
const musicContainer = document.querySelector("#music-container")
const prevBtn = document.querySelector("#prevBtn");
const nextBtn = document.querySelector("#nextBtn");
const forwardBtn = document.querySelector("#forwardBtn");
const modeBtn = document.querySelector("#modeBtn");
const cover = document.querySelector("#cover");
const progress = document.querySelector("#progress");
const progressContainer = document.querySelector("#progress-container");
const audio = document.querySelector("#audio");
const currTime = document.querySelector("#currentTime");
const durTime = document.querySelector("#duration");
const title = document.querySelector("#title");
const artist = document.querySelector("#artist");
const newPlaylist = document.querySelector("#new-playlist-btn");

window.addEventListener('popstate', function(event) {
    console.log("Trở lại trang trước!");
    loadContentByUrl(window.location.href);
});

(function ($) {
    "use strict";
    beginLoadContent()

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Sidebar Toggler
    $('.sidebar-toggler').click(function () {
        $('.sidebar, .content').toggleClass("open");
        return false;
    });

    // Progress Bar
    $('.pg-bar').waypoint(function () {
        $('.progress .progress-bar').each(function () {
            $(this).css("width", $(this).attr("aria-valuenow") + '%');
        });
    }, {offset: '80%'});


    // Calender
    $('#calender').datetimepicker({
        inline: true,
        format: 'L'
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        items: 1,
        dots: true,
        loop: true,
        nav : false
    });


    // Chart Global Color
    Chart.defaults.color = "#6C7293";
    Chart.defaults.borderColor = "#000000";

    
})(jQuery);
function userDetailScript(){
    const $ = document.querySelector.bind(document);
    const $$ = document.querySelectorAll.bind(document);

    const tabs = $$(".tab-item");
    const panes = $$(".tab-pane");

    const tabActive = $(".tab-item.active");
    const line = $(".tabs .line");

    tabs.forEach((tab, index) => {
        const pane = panes[index];

        tab.onclick = function () {
            $(".tab-item.active").classList.remove("active");
            $(".tab-pane.active").classList.remove("active");

            line.style.left = this.offsetLeft + "px";
            line.style.width = this.offsetWidth + "px";

            this.classList.add("active");
            pane.classList.add("active");
        };
    });

    const seeMoreSongs = document.getElementById("see-more-songs");
    const seeMoreAlbums = document.getElementById("see-more-albums");
    const seeMoreFollowers = document.getElementById("see-more-followers");

    const spinner = document.getElementById("spinner");
    const avatarInput = document.getElementById("avatar-input");
    const headerInput = document.getElementById("header-input");

    seeMoreSongs.addEventListener("click", () => {
        seeMoreClickHandler(1);
    });

    seeMoreAlbums.addEventListener("click", () => {
        seeMoreClickHandler(2);
    });

    seeMoreFollowers.addEventListener("click", () => {
        seeMoreClickHandler(3);
    });


    if(avatarInput)
        avatarInput.addEventListener("change", () => {
            const file = avatarInput.files[0];
            const userAvatar = document.getElementsByClassName("user-avatar")[0]

            const formData = new FormData();
            formData.append('file', file);

            spinner.classList.add('show');
            spinner.style.opacity = "0.5";
            userService.uploadImage(formData, "avatar")
                .then(result => {
                    spinner.classList.remove('show');
                    spinner.style.opacity = "0";
                    showToast(result.message);
                    userAvatar.setAttribute("src", result.data);
                    loadContentByUrl(window.location.href);
                })
        })

    if(headerInput)
        headerInput.addEventListener("change", () => {
            const file = headerInput.files[0];

            const formData = new FormData();
            formData.append('file', file);

            spinner.classList.add('show');
            spinner.style.opacity = "0.5";
            userService.uploadImage(formData, "header")
                .then(result => {
                    spinner.classList.remove('show');
                    spinner.style.opacity = "0";
                    showToast(result.message);
                    loadContentByUrl(window.location.href);
                })
        })

    function seeMoreClickHandler(index){
        const pane = panes[index];

        $(".tab-item.active").classList.remove("active");
        $(".tab-pane.active").classList.remove("active");

        line.style.left = tabs[index].offsetLeft + "px";
        line.style.width = tabs[index].offsetWidth + "px";

        tabs[index].classList.add("active");
        pane.classList.add("active");
    }
}


function artistDetailsScript(){
    const $ = document.querySelector.bind(document);
    const $$ = document.querySelectorAll.bind(document);

    const seeMoreSongs = document.getElementById("see-more-songs");
    const seeMoreAlbums = document.getElementById("see-more-albums");
    const seeMoreFollowers = document.getElementById("see-more-followers");
    const btnAction = document.getElementById("btn-action");

    const btnAdd = document.getElementById('btn-add');
    const artistId = document.getElementById('artist-id').value;

    const spinner = document.getElementById("spinner");
    const avatarInput = document.getElementById("avatar-input");
    const headerInput = document.getElementById("header-input");

    const userEmail = document.getElementById("user-email").value;

    const tabs = $$(".tab-item");
    const panes = $$(".tab-pane");

    const tabActive = $(".tab-item.active");
    const line = $(".tabs .line");

    let selectedSongIds = [];

    tabs.forEach((tab, index) => {
        const pane = panes[index];

        tab.onclick = function () {
            $(".tab-item.active").classList.remove("active");
            $(".tab-pane.active").classList.remove("active");

            line.style.left = this.offsetLeft + "px";
            line.style.width = this.offsetWidth + "px";

            this.classList.add("active");
            pane.classList.add("active");
        };
    });

    seeMoreSongs.addEventListener("click", () => {
        seeMoreClickHandler(1);
    });

    seeMoreAlbums.addEventListener("click", () => {
        seeMoreClickHandler(2);
    });

    if(seeMoreFollowers){
        seeMoreFollowers.addEventListener("click", () => {
            seeMoreClickHandler(3);
        });
    }

    if(btnAction){
        btnAction.addEventListener("click", () => {
            if(btnAction.classList.contains("follow")){
                userService.followUser(userEmail).then(
                    result => {
                        showToast(result.message, "", false);
                        loadContentByUrl(window.location.href)
                    }
                )
            }
        })
    }

    if(btnAdd)
        btnAdd.addEventListener("click" , () => {
            if(validateForm()){
                spinner.classList.add('show');
                spinner.style.opacity = "0.5";
                addSong();
            }
        })

    if(avatarInput)
        avatarInput.addEventListener("change", () => {
            const file = avatarInput.files[0];
            const userAvatar = document.getElementsByClassName("user-avatar")[0]

            const formData = new FormData();
            formData.append('file', file);

            spinner.classList.add('show');
            spinner.style.opacity = "0.5";
            userService.uploadImage(formData, "avatar")
                .then(result => {
                    spinner.classList.remove('show');
                    spinner.style.opacity = "0";
                    showToast(result.message);
                    userAvatar.setAttribute("src", result.data);
                    loadContentByUrl(window.location.href);
                })
        })

    if(headerInput)
        headerInput.addEventListener("change", () => {
            const file = headerInput.files[0];

            const formData = new FormData();
            formData.append('file', file);

            spinner.classList.add('show');
            spinner.style.opacity = "0.5";
            userService.uploadImage(formData, "header")
                .then(result => {
                    spinner.classList.remove('show');
                    spinner.style.opacity = "0";
                    showToast(result.message);
                    loadContentByUrl(window.location.href);
                })
        })


    document.getElementById('add-btn').addEventListener('click', function() {
        const selectedSongs = document.querySelectorAll('.choose-song:checked');

        const songContainer = document.querySelector('.song-container');

        songContainer.innerHTML = '';

        selectedSongIds = [];

        selectedSongs.forEach((checkbox) => {
            const songId = checkbox.getAttribute('data-song-id'); // Giả sử bạn có ID bài hát để dùng khi cần
            const songName = checkbox.closest('.ds-baihat').querySelector('.song-name').textContent;
            const songArtist = checkbox.closest('.ds-baihat').querySelector('.song-artist').textContent;
            const songImg = checkbox.closest('.ds-baihat').querySelector('.song-img').src;

            selectedSongIds.push(parseInt(songId));

            const songElement = `
                <div style="overflow: visible" class="horizontal-card ds-baihat">
                    <div style="display: flex; justify-content: center; align-items: center; padding-top: 5px; padding-bottom: 5px;">
                        <img style="width: 50px; height: 50px; margin-left: 12px; object-fit: cover;" src="${songImg}" class="song-img" alt="image">
                        <div style="margin-left: 8px;">
                            <h6 style="margin: 0px; color: #000000;">
                                <a class="song-name">${songName}</a>
                            </h6>
                            <p style="margin-bottom: 0px; margin-top: 4px; font-size: smaller" class="song-artist">${songArtist}</p>
                        </div>
                    </div>
                </div>
            `;

            songContainer.insertAdjacentHTML('beforeend', songElement);
        });
    });

    document.getElementById("btn-add-album").addEventListener('click',  () => {
        console.log(selectedSongIds);
        const releaseDate = document.getElementById('albumReleaseDate').value;
        const description = document.getElementById('albumDescription').value;
        const title = document.getElementById('albumTitle').value;
        const img = document.getElementById('albumImage');

        if(validateAlbumForm()){
            spinner.classList.add('show');
            spinner.style.opacity = "0.5";
            let formData = new FormData();
            formData.append("file", img.files[0])
            fileService.uploadFile(formData, "img").then(
                response => {
                    let requestBody = {
                        "releasedDate": releaseDate,
                        "description": description,
                        "title": title,
                        "imageUrl": response.data,
                        "artistId": parseInt(artistId),
                        "songIds": selectedSongIds
                    }

                    albumService.addAlbum(requestBody)
                        .then(result => {
                            spinner.classList.remove('show');
                            spinner.style.opacity = "0";
                            showToast(result.message, '', false);
                            loadContentByUrl(window.location.href);
                            closeAlbumModal();
                        })
                }
            );
        }
    })

    function seeMoreClickHandler(index){
        const pane = panes[index];

        $(".tab-item.active").classList.remove("active");
        $(".tab-pane.active").classList.remove("active");

        line.style.left = tabs[index].offsetLeft + "px";
        line.style.width = tabs[index].offsetWidth + "px";

        tabs[index].classList.add("active");
        pane.classList.add("active");
    }
    function validateForm() {
        const name = document.getElementById('name').value.trim();
        const author = document.getElementById('author').value.trim();
        const releaseDate = document.getElementById('releaseDate').value.trim();
        const lyrics = document.getElementById('lyrics').value.trim();
        const audioUrl = document.getElementById('audioUrl').value; // File input không cần trim
        const imageUrl = document.getElementById('imageUrl').value; // File input không cần trim
        const genreId = document.getElementById('genreId').value;

        if (!name || !author || !releaseDate || !lyrics || !audioUrl || !imageUrl || !genreId) {
            alert('Vui lòng nhập đầy đủ thông tin.')
            return false;
        }

        return true;
    }

    function validateAlbumForm(){
        const releaseDate = document.getElementById('albumReleaseDate').value.trim();
        const description = document.getElementById('albumDescription').value.trim();
        const title = document.getElementById('albumTitle').value.trim();
        const img = document.getElementById('albumImage').value;

        if(!releaseDate || !description || !title || !img || selectedSongIds.length === 0) {
            alert('Vui lòng nhập đầy đủ thông tin.')
            return false;
        }

        return true;
    }

    function addSong() {
        const audioFile = document.getElementById('audioUrl').files[0]; // File input không cần trim
        const imageFile = document.getElementById('imageUrl').files[0]; // File input không cần trim

        let requestBody = {
            name: document.getElementById('name').value.trim(),
            author: document.getElementById('author').value.trim(),
            releaseDate: formatDate(document.getElementById('releaseDate').value.trim()),
            lyrics: document.getElementById('lyrics').value.trim().replaceAll('\n', '<br>'),
            audioUrl: "",
            imageUrl: "",
            genreId: parseInt(document.getElementById('genreId').value),
            albumId: parseInt(document.getElementById('genreId').value),
            artistIds: [parseInt(artistId)],
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
                        closeModal()
                        loadContentByUrl(window.location.href)
                    });
            })
            .catch(error => {
                spinner.classList.remove('show');
                spinner.style.opacity = "0";
                console.error('Đã xảy ra lỗi trong quá trình upload:', error);
            });
    }
    function closeModal(){
        let myModalEl = document.getElementById('addSongModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }

    function closeAlbumModal(){
        let myModalEl = document.getElementById('addAlbumModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
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

function albumDetailScript(){
    const playAllBtn = document.getElementById("btn-play-all");
    const albumId = document.getElementById("album-id").value;
    const favBtn = document.getElementById("fav-btn")

    playAllBtn.addEventListener("click", async () => {
        let fetchedSongs = await albumService.getSongsFromAlbum(albumId);
        if(fetchedSongs.length > 0){
            songs = fetchedSongs;
            songIndex = 0;
            currentSong = songs[songIndex];
            console.log(songs)
            loadSong(currentSong);
            playSong();
        }
    })

    favBtn.addEventListener('click', () => {
        albumService.favouriteAlbum(albumId)
            .then(resp => {
                showToast(resp.message, "", false)
                loadContentByUrl(window.location.href);
            })
    })
}

function playlistDetailScript() {
    const playlistId = document.getElementById("playlist-id").value;
    const playAllBtn = document.getElementById("btn-play-all");
    const addSongToPlaylist = document.getElementById("add-btn");
    let songItems = document.querySelectorAll('.song-menu li');
    let songId = 0;

    songItems.forEach(item => {
        item.addEventListener('click', function () {
            songId = this.getAttribute('data-song-id');
            swal({
                title: "Xác nhận xóa",
                text: "Bạn có chắc là muốn xóa bài hát này khỏi danh sách phát?",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        playlistService.removeSongFromPlaylist(playlistId, [songId])
                            .then(resp => {
                                if (resp.status) {
                                    showToast(resp.message, "", false)
                                    loadContentByUrl(window.location.href)
                                } else {
                                    showToast(resp.message, "", false)
                                }
                            })
                    }
                });
        });
    })

    playAllBtn.addEventListener("click", async () => {
        songs = await playlistService.getSongsFromPlaylist(playlistId);
        songIndex = 0;
        currentSong = songs[songIndex];
        console.log(songs)
        loadSong(currentSong);
        playSong();
    })

    addSongToPlaylist.addEventListener('click', async () => {
        let checkedSongs = []

        const checkboxes = document.querySelectorAll('.choose-song');

        checkboxes.forEach(function (checkbox) {
            if (checkbox.checked) {
                checkedSongs.push(checkbox.getAttribute('data-song-id'));
            }
        });

        let result = await playlistService.addSongToPlaylist(playlistId, checkedSongs);
        showToast(result.message, "", false);
        if(result.status){
            closeModal();
            loadContentByUrl(window.location.href)
        }
    })

    function closeModal(){
        let myModalEl = document.getElementById('favourite-songs');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function myPlaylistScript(){
    let playlistItems = document.querySelectorAll('#playlist-menu li');

    playlistItems.forEach(function (item) {
        item.addEventListener('click',

            function () {
            let playlistId = this.getAttribute('data-playlist-id');
            if (this.classList.contains('delete')) {
                swal({
                    title: "Xác nhận xóa",
                    text: "Bạn có chắc là muốn xóa playlist này?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                })
                    .then((willDelete) => {
                        if (willDelete) {
                            playlistService.removePlaylist(playlistId)
                            .then(resp => {
                                if(resp.status){
                                    showToast(resp.message, "", false)
                                    loadContentByUrl("/my-playlist")
                                }else{
                                    showToast(resp.message, "", false)
                                }
                            })
                        }
                    });
            }else {
                swal("Nhập tên playlist mới:", {
                    content: "input",
                })
                    .then((value) => {
                        if(value)
                            playlistService.renamePlaylist(playlistId, value)
                                .then(resp => {
                                    if(resp.status){
                                        showToast(resp.message, "", false)
                                        loadContentByUrl("/my-playlist")
                                    }else{
                                        showToast(resp.message, "", false)
                                    }
                                })
                    });
            }
        });
    });
}


async function homeScript() {
    const playInvokers = document.getElementsByClassName("play-btn");
    for (let playInvoker of playInvokers) {
        playInvoker.addEventListener('click', async e => {
            songs = await songService.getAllSong(currentSong, 6);
            const songId = playInvoker.getAttribute('data-song-id');
            console.log(songId);
            currentSong = await songService.getSongById(songId);
            let currentSongIndex = songs.findIndex(song => song.id === currentSong.id);
            songIndex = currentSongIndex !== -1 ? currentSongIndex : 0;
            loadSong(currentSong);
            playSong()
        })
    }
}

function favouriteSongScript(){
    let songItems = document.querySelectorAll('#song-menu li');
    let songId = 0;
    const newPlaylistNameInput = document.getElementById("new-playlist-name");
    const existPlaylistNameInput = document.getElementById("exists-playlist-name");

    songItems.forEach(function (item) {
        item.addEventListener('click', function () {
            songId = this.getAttribute('data-song-id');
            if (this.classList.contains('delete')) {
                swal({
                    title: "Xác nhận xóa",
                    text: "Bạn có chắc là muốn xóa bài hát này khỏi danh sách yêu thích?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                })
                    .then((willDelete) => {
                        if (willDelete) {
                            songService.favouriteSong(songId)
                                .then(resp => {
                                    if(resp.status){
                                        showToast(resp.message, "", false)
                                        loadContentByUrl("/favourite-songs")
                                    }else{
                                        showToast(resp.message, "", false)
                                    }
                                })
                        }
                    });
            }
        });
    });

    document.querySelectorAll('input[name="to-playlist"]').forEach(input => {
        input.addEventListener('change', function() {
            if(this.checked) {
                if(this.id === 'new-playlist') {
                    existPlaylistNameInput.classList.add("hide");
                    newPlaylistNameInput.classList.remove("hide");
                } else if(this.id === 'exists-playlist') {
                    existPlaylistNameInput.classList.remove("hide");
                    newPlaylistNameInput.classList.add("hide");
                }
            }
        });
    });

    document.getElementById('add').addEventListener('click', function() {
        const isNewPlaylistSelected = document.getElementById('new-playlist').checked;
        const isExistingPlaylistSelected = document.getElementById('exists-playlist').checked;

        if (isNewPlaylistSelected) {
            const newPlaylistName = document.getElementById('new-playlist-name').value;
            playlistService.addPlaylist(newPlaylistName, [songId]).then(resp => {
                showToast(resp.message, "", false)
                closeModal()
                loadContentByUrl(window.location.href);
            })
        } else if (isExistingPlaylistSelected) {
            const existingPlaylistId = document.getElementById('exists-playlist-name').value;
            playlistService.addSongToPlaylist(existingPlaylistId, [songId]).then(resp => {
                showToast(resp.message, "", false)
                closeModal()
                loadContentByUrl(window.location.href);
            })
        }
    });

    function closeModal(){
        let myModalEl = document.getElementById('add-to-playlist');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function songDetailsScript() {
    const playBtn = document.getElementById("play-song-btn")
    const favBtn = document.getElementById("fav-btn")
    const newPlaylistNameInput = document.getElementById("new-playlist-name");
    const existPlaylistNameInput = document.getElementById("exists-playlist-name");
    const songId = document.getElementById("detail-song-id").value;

    const spinner = document.getElementById("spinner");

    const editNameBtn = document.getElementById("edit-name");
    const editLyrics = document.getElementById("edit-lyrics");
    const imageInput = document.getElementById("song-img");
    const delSong = document.getElementById("delete-song");

    console.log(parseInt(songId) === currentSong.id)
    console.log(songId)
    console.log(currentSong.id)

    if(playBtn.tagName === "I"){
        if(parseInt(songId) === currentSong.id && musicContainer.classList.contains('play')){
            playBtn.classList.remove("fa-circle-play");
            playBtn.classList.add("fa-circle-pause")
        }else{
            playBtn.classList.add("fa-circle-play");
            playBtn.classList.remove("fa-circle-pause")
        }
    }

    document.querySelectorAll('input[name="to-playlist"]').forEach(input => {
        input.addEventListener('change', function() {
            if(this.checked) {
                if(this.id === 'new-playlist') {
                    existPlaylistNameInput.classList.add("hide");
                    newPlaylistNameInput.classList.remove("hide");
                } else if(this.id === 'exists-playlist') {
                    existPlaylistNameInput.classList.remove("hide");
                    newPlaylistNameInput.classList.add("hide");
                }
            }
        });
    });

    playBtn.addEventListener('click', async () => {
        if (playBtn.tagName === "I") {
            if(currentSong.id !== parseInt(songId)){
                currentSong = await songService.getSongById(songId);
                let currentSongIndex = songs.findIndex(song => song.id === currentSong.id);
                songIndex = currentSongIndex !== -1 ? currentSongIndex : 0;
                loadSong(currentSong);
            }

            if(playBtn.classList.contains("fa-circle-play")){
                songs = await songService.getAllSong(currentSong, 6);
                playBtn.classList.remove("fa-circle-play");
                playBtn.classList.add("fa-circle-pause");
                playSong();
            }else{
                playBtn.classList.add("fa-circle-play");
                playBtn.classList.remove("fa-circle-pause");
                pauseSong();
            }
        }
    })

    document.getElementById('add').addEventListener('click', function() {
        const isNewPlaylistSelected = document.getElementById('new-playlist').checked;
        const isExistingPlaylistSelected = document.getElementById('exists-playlist').checked;

        if (isNewPlaylistSelected) {
            const newPlaylistName = document.getElementById('new-playlist-name').value;
            playlistService.addPlaylist(newPlaylistName, [songId]).then(resp => {
                showToast(resp.message, "", false)
                closeModal()
                loadContentByUrl(window.location.href);
            })
        } else if (isExistingPlaylistSelected) {
            const existingPlaylistId = document.getElementById('exists-playlist-name').value;
            playlistService.addSongToPlaylist(existingPlaylistId, [songId]).then(resp => {
                showToast(resp.message, "", false)
                closeModal()
                loadContentByUrl(window.location.href);
            })
        }
    });

    favBtn.addEventListener('click', () => {
        let liked = favBtn.classList.contains("fa-solid")
        if(!liked){
            songService.favouriteSong(songId)
                .then(resp => {
                    showToast(resp.message, "", false)
                    loadContentByUrl(window.location.href);
                })
        }else{
            songService.favouriteSong(songId)
                .then(resp => {
                    showToast(resp.message, "", false)
                    loadContentByUrl(window.location.href);
                })
        }
    })

    if(editNameBtn){
        editNameBtn.addEventListener('click', () => {
            swal("Nhập tên bài hát:", {
                content: {
                    element: "input",
                    attributes: {
                        value: document.getElementById("song-name").textContent,
                    },
                },
            })
            .then((value) => {
                if(value){
                    swal("Bạn có chắc là muốn thay đổi tên bài hát?", {
                        buttons: {
                            cancel: "Hủy",
                            ok: "Đồng ý",
                        },
                    })
                    .then(clicked => {
                        switch (clicked){
                            case "ok" :
                                songService.updateSongName(songId, value)
                                    .then(result => {
                                        loadContentByUrl(window.location.href);
                                        showToast(result.message, "", false)
                                    });
                                break;
                        }
                    });
                }
            });

        });

        imageInput.addEventListener('change', () => {
            const file = imageInput.files[0];

            spinner.classList.add('show');
            spinner.style.opacity = "0.5";
            songService.updateSongImage(songId, file)
                .then(result => {
                    spinner.classList.remove('show');
                    spinner.style.opacity = "0";
                    showToast(result.message);
                    loadContentByUrl(window.location.href);
                })
        })

        let lyrics = document.getElementById("song-lyrics").innerHTML.replaceAll("<br>", "\n");
        console.log(lyrics)

        editLyrics.addEventListener('click', () => {
            swal("Chỉnh sửa lời bài hát:", {
                content: {
                    element: "textarea",
                    attributes: {
                        value: lyrics,
                        style: "height: 347px;"
                    },
                },
            })
            .then((value) => {
                if(value){
                    var editedLyrics = document.querySelector(".swal-content__textarea").value
                    editedLyrics =  editedLyrics.toString().replaceAll("\n", "<br>")
                    swal("Bạn có chắc là muốn thay đổi lời bài hát?", {
                        buttons: {
                            cancel: "Hủy",
                            ok: "Đồng ý",
                        },
                    })
                        .then(clicked => {
                            switch (clicked){
                                case "ok" :
                                    songService.updateSongLyrics(songId, editedLyrics)
                                        .then(result => {
                                            loadContentByUrl(window.location.href);
                                            showToast(result.message, "", false)
                                        });
                                    break;
                            }
                        });
                }
            });
        });

        delSong.addEventListener('click', () => {
            swal("Bạn có chắc là muốn xóa bài hát này?", {
                buttons: {
                    cancel: "Hủy",
                    ok: "Đồng ý",
                },
            })
                .then(clicked => {
                    switch (clicked){
                        case "ok" :
                            songService.deleteSong(songId)
                                .then(result => {
                                    loadContentByUrl("http://localhost:3000/home");
                                    showToast(result.message, "", false)
                                });
                            break;
                    }
                });
        })
    }

    function closeModal(){
        let myModalEl = document.getElementById('add-to-playlist');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}
function hideAllDivs() {
    var overlay = document.getElementById("overlay");
    var premiumDiv = document.querySelector('.premium-advert');

    if (premiumDiv.style.display === "none") {
        overlay.style.display = "none";
    } else {
        overlay.style.display = "block";
    }
}
function hideDiv() {
    var premiumDiv = document.querySelector('.premium-advert');
    premiumDiv.style.display = 'none';
    hideAllDivs();
}
function hideDiv2() {
    var mediumDiv = document.querySelector('.medium-advert');
    mediumDiv.style.display = 'none';
    hideAllDivs();
}
function showMediumAds(){
    let mediumDiv = document.querySelector('.medium-advert');
    if(mediumDiv){
        mediumDiv.style.display = 'block';
    }
}

function showPreAds(){
    let overlay = document.getElementById("overlay");
    let premiumDiv = document.querySelector('.premium-advert');

    if(premiumDiv && overlay) {
        premiumDiv.style.display = 'block';

        if (premiumDiv.style.display === "none") {
            overlay.style.display = "none";
        } else {
            overlay.style.display = "block";
        }
    }
}

function loadSong(song){
    title.innerText = song.name;
    title.setAttribute("href", "http://localhost:3000/song-details?song_id=" + song.id);
    title.addEventListener("click", e => {
        e.preventDefault();
        let url = title.getAttribute('href');
        loadContentByUrl(url);
    })
    artist.innerText = song.artists[0].artistName;
    artist.setAttribute("href", "http://localhost:3000/artist-details?artist_id=" + song.artists[0].id);
    artist.addEventListener("click", e => {
        e.preventDefault();
        let url = artist.getAttribute('href');
        loadContentByUrl(url);
    })

    audio.src = song.audioUrl;
    cover.src = song.imageUrl;

    currentSong = song

    totalListenedTime = 0;
    lastUpdateTime = 0;
    isNewPlayCount = true;

    localStorage.setItem("playedSong", song.id)
}
async function beginLoadContent() {
    var url = window.location.href
    let strSplit = url.split("?")
    var requestUrl = strSplit[0]

    if (url === "http://localhost:3000/") {
        requestUrl += "home/component"
    } else if (url === "http://localhost:3000") {
        requestUrl += "/home/component"
    } else {
        requestUrl += "/component"
    }

    requestUrl += (strSplit[1] ? "?" + strSplit[1] : "");


    fetch(requestUrl)
        .then(response => response.text())
        .then(html => {
            document.getElementById('root').innerHTML = html;
            window.history.pushState({path: url}, '', url);
            executeScripts(url)
        });

    if(localStorage.getItem("playedSong") !== null){
        currentSong = await songService.getSongById(localStorage.getItem("playedSong"))
        loadSong(currentSong)
    }
}

function executeScripts(url) {
    if (url.includes("user-details")) {
        userDetailScript()
    } else if (url.includes("home")) {
        homeScript()
    } else if (url.includes("genre")) {
        genreScript.execute()
        homeScript()
    } else if (url.includes("song-details")) {
        songDetailsScript()
    } else if (url.includes("my-playlist")) {
        myPlaylistScript()
    } else if (url.includes("favourite-songs")) {
        favouriteSongScript();
    } else if (url.includes("playlist-details")) {
        playlistDetailScript();
    } else if(url.includes("artist-details")) {
        artistDetailsScript();
        homeScript();
    }else if(url.includes("album-details")){
        albumDetailScript();
    }
}

function loadContent(e, t){
    e.preventDefault();
    const searchResultElement = document.getElementById('custom-scrollbar');
    let url = t.getAttribute('href');

    searchResultElement.style.display = 'none';
    document.getElementById('search').value = '';

    clearSearchResults();
    loadContentByUrl(url);
}

function loadContentByUrl(url){
    let navItems = document.getElementsByClassName("nav-item");

    for (let i = 0; i < navItems.length; i++) {
        navItems[i].classList.remove("active")
    }

    const mappings = {
        "/home": "nav-home",
        "/albums": "nav-album",
        "/artists": "nav-artists",
        "/genres": "nav-genre",
        "/my-playlist": "nav-my-playlist",
        "/favourite-songs": "nav-fav-song",
        "/favourite-album": "nav-fav-album",
        "/following-artists": "nav-following-artists"
    };

    Object.entries(mappings).forEach(([key, value]) => {
        if(url.includes(key)) {
            document.getElementById(value).classList.add("active");
            return;
        }
    });

    let strSplit = url.split("?")
    let requestUrl = strSplit[0] + "/component" + (strSplit[1] ? "?" + strSplit[1] : "")

    fetch(requestUrl)
        .then(response => response.text())
        .then(html => {
            window.history.pushState({ path: url }, '', url);
            const root = document.getElementById('root');
            root.innerHTML = html;
            executeScripts(url)
        });
}

function playSong(){
    musicContainer.classList.add('play');
    playBtn.querySelector('i.fas').classList.remove('fa-play');
    playBtn.querySelector('i.fas').classList.add('fa-pause');

    audio.play();
}

function pauseSong(){
    musicContainer.classList.remove('play');
    playBtn.querySelector('i.fas').classList.add('fa-play');
    playBtn.querySelector('i.fas').classList.remove('fa-pause');

    audio.pause();
}

// Previous song
function prevSong() {
    songIndex--;

    if (songIndex < 0) {
        songIndex = songs.length - 1;
    }

    loadSong(songs[songIndex]);

    playSong();
}

// Next song
async function nextSong() {
    if(isRepeatInList){
        songIndex++;
        if (songIndex > songs.length - 1) {
            songIndex = 0;
        }
    }
    loadSong(songs[songIndex]);

    playSong();
}

function updateProgress(e) {
    const { duration, currentTime } = e.srcElement;
    const progressPercent = (currentTime / duration) * 100;

    progress.style.width = `${progressPercent}%`;

    updatePlayCount()
}

function setProgress(e) {
    const width = this.clientWidth;
    const clickX = e.offsetX;
    const duration = audio.duration;

    audio.currentTime = (clickX / width) * duration;
}

function DurTime (e) {
    const {duration,currentTime} = e.srcElement;
    var sec;
    var sec_d;

    // define minutes currentTime
    let min = (currentTime==null)? 0:
        Math.floor(currentTime/60);
    min = min <10 ? '0'+min:min;

    // define seconds currentTime
    function get_sec (x) {
        if(Math.floor(x) >= 60){

            for (var i = 1; i<=60; i++){
                if(Math.floor(x)>=(60*i) && Math.floor(x)<(60*(i+1))) {
                    sec = Math.floor(x) - (60*i);
                    sec = sec <10 ? '0'+sec:sec;
                }
            }
        }else{
            sec = Math.floor(x);
            sec = sec <10 ? '0'+sec:sec;
        }
    }

    get_sec (currentTime,sec);

    // change currentTime DOM
    currTime.innerHTML = min +':'+ sec;

    // define minutes duration
    let min_d = (isNaN(duration) === true)? '0':
        Math.floor(duration/60);
    min_d = min_d <10 ? '0'+min_d:min_d;


    function get_sec_d (x) {
        if(Math.floor(x) >= 60){

            for (var i = 1; i<=60; i++){
                if(Math.floor(x)>=(60*i) && Math.floor(x)<(60*(i+1))) {
                    sec_d = Math.floor(x) - (60*i);
                    sec_d = sec_d <10 ? '0'+sec_d:sec_d;
                }
            }
        }else{
            sec_d = (isNaN(duration) === true)? '0':
                Math.floor(x);
            sec_d = sec_d <10 ? '0'+sec_d:sec_d;
        }
    }

    // define seconds duration

    get_sec_d (duration);

    // change duration DOM
    durTime.innerHTML = min_d +':'+ sec_d;

}
function forwardSong() {
    audio.currentTime += 10;
}
function changeMode(){
    if(modeBtn.classList.contains('bi-repeat')){
        modeBtn.classList.remove('bi-repeat')
        modeBtn.classList.add('bi-repeat-1')
    }else{
        modeBtn.classList.add('bi-repeat')
        modeBtn.classList.remove('bi-repeat-1')
    }
    isRepeatInList = modeBtn.classList.contains('bi-repeat')
}

function updatePlayCount() {
    const currentTime = audio.currentTime;
    if (currentTime - lastUpdateTime < 2) {
        totalListenedTime += currentTime - lastUpdateTime;
    }
    lastUpdateTime = currentTime;

    const listenedRatio = totalListenedTime / audio.duration;

    if (listenedRatio >= listenThreshold && isNewPlayCount) {
        songService.addSongPlays(currentSong.id)
        isNewPlayCount = false;
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

function fetchAndDisplayAds() {
    fetch('/api/ad-package/for-display')
        .then(response => response.json())
        .then(data => {
            const preAdImg = document.getElementById('preAdImg');
            if(preAdImg){
                if (data.data.preAdImg) {
                    preAdImg.src = data.data.preAdImg;
                    preAdImg.parentElement.style.display = '';
                    showPreAds();
                } else {
                    let overlay = document.getElementById("overlay");
                    if(overlay){
                        document.getElementById("overlay").style.display = 'none';
                    }
                    preAdImg.parentElement.style.display = 'none';
                }
            }

            const medAdImg = document.getElementById('medAdImg');
            if(medAdImg){
                if (data.data.medAdImg) {
                    medAdImg.src = data.data.medAdImg;
                    medAdImg.parentElement.style.display = '';
                    showMediumAds();
                } else {
                    medAdImg.parentElement.style.display = 'none';
                }
            }

            const norAdImg = document.getElementById('norAdImg');
            if(norAdImg){
                if (data.data.norAdImg) {
                    norAdImg.src = data.data.norAdImg;
                    norAdImg.parentElement.style.display = '';
                } else {
                    norAdImg.parentElement.style.display = 'none';
                }
            }

            console.log(data);
        })
        .catch(error => console.error('Error fetching ad data:', error));
}

fetchAndDisplayAds();

function fetchAdsRandomly() {
    fetchAndDisplayAds();
    console.log("Ads loaded at: " + new Date().toLocaleTimeString());

    let randomDelay = Math.random() * (120000 - 60000) + 60000;

    setTimeout(fetchAdsRandomly, randomDelay);
}

async function fetchSearchResults(searchKey) {
    const url = `/api/search?key=${encodeURIComponent(searchKey)}`;

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();

        const songResults = document.querySelector('.song-result');
        songResults.innerHTML = data.songs.map(song => `
            <div>
                <a onclick="loadContent(event, this)" class="search-item" href="/song-details?song_id=${song.id}">
                    <img src="${song.imageUrl}" alt="anh">
                    <div class="song-info">
                        <h6 class="single-line-text song-name">${song.name}</h6>
                        <span class="artist">${song.artists.map(artist => artist.artistName).join(", ")}</span>
                    </div>
                </a>
            </div>
        `).join('');

        const albumResults = document.querySelector('.album-result');
        albumResults.innerHTML = data.albums.map(album => `
            <div>
                <a onclick="loadContent(event, this)" class="search-item" href="/album-details?album_id=${album.id}">
                    <img src="${album.imageUrl}" alt="anh">
                    <div class="song-info">
                        <h6 class="single-line-text album-name">${album.title}</h6>
                        <span class="artist">${album.artist}</span>
                    </div>
                </a>
            </div>
        `).join('');

        const artistResults = document.querySelector('.artist-result');
        artistResults.innerHTML = data.artists.map(artist => `
            <div>
                <a onclick="loadContent(event, this)" class="search-item" href="/artist-details?artist_id=${artist.id}">
                    <img src="${artist.image}" alt="anh">
                    <div class="song-info">
                        <h6 class="single-line-text artist-name">${artist.artistName}</h6>
                    </div>
                </a>
            </div>
        `).join('');

    } catch (error) {
        console.error('Failed to fetch search results:', error);
    }
}

function debounce(callback, delay) {
    let timer;
    return function(...args) {
        clearTimeout(timer);
        timer = setTimeout(() => {
            callback.apply(this, args);
        }, delay);
    };
}

function clearSearchResults() {
    document.querySelector('.song-result').innerHTML = '';
    document.querySelector('.album-result').innerHTML = '';
    document.querySelector('.artist-result').innerHTML = '';
}

function sortApproachStat(data){
    const sortedArray = Object.entries(data).sort((a, b) => {
        const dateA = new Date(a[0].split('/').reverse().join('/'));
        const dateB = new Date(b[0].split('/').reverse().join('/'));

        return dateA - dateB;
    });

    return Object.fromEntries(sortedArray);
}

const debouncedFetchSearchResults = debounce(fetchSearchResults, 300);

let noApproachChart;

let requestInfos = document.querySelectorAll('.request-info');
requestInfos.forEach(modal => {
    modal.addEventListener('click', function (event) {
        let requestId = this.getAttribute('data-request-id');
        let isActive = this.getAttribute('data-request-active');
        let apiUrl = '/api/ad-package/ad/' + requestId;
        let approachStatWrapper = document.getElementById('approachStatWrapper');
        approachStatWrapper.style.display = "none";

        if(isActive.toLowerCase() === 'true'){
            if(noApproachChart){
                noApproachChart.destroy();
            }
            approachStatWrapper.style.display = "block";

            const approachStatisticsData = {
                labels: [],
                datasets: [
                    {
                        label: "Lượt tiếp cận quảng cáo",
                        borderColor: "blue",
                        data: [],
                        tension: 0.4,
                    }
                ],
            }
            const approachStatisticsConfig = {
                type: "line",
                data: approachStatisticsData,
            }

            let noApproach = document.getElementById('approachStatistics');
            noApproachChart = new Chart(noApproach, approachStatisticsConfig)

            fetch(`/api/statistic/ad/${requestId}`)
                .then(response => response.json())
                .then(data => {
                    if (data.code === 200 && data.status) {
                        let stat = sortApproachStat(data.data);

                        noApproachChart.data.labels = Object.keys(stat)
                        noApproachChart.data.datasets[0].data = Object.values(stat);
                        noApproachChart.update();
                    }
                })
                .catch(error => console.error('Error fetching annual statistics:', error));
        }

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

document.getElementById('search').addEventListener('input', function() {
    const searchKey = this.value.trim();
    const searchResultElement = document.getElementById('custom-scrollbar');

    if (searchKey) {
        searchResultElement.style.display = 'block';
        debouncedFetchSearchResults(searchKey);
    } else {
        clearSearchResults();
        searchResultElement.style.display = 'none';
    }
});

document.getElementsByClassName('search-close-btn')[0].addEventListener('click', () => {
    const searchResultElement = document.getElementById('custom-scrollbar');

    clearSearchResults();
    searchResultElement.style.display = 'none';
})

fetchAdsRandomly();

newPlaylist.addEventListener('click', () => {
    swal("Nhập tên playlist:", {
        content: "input",
    })
        .then((value) => {
            if(value)
                playlistService.addPlaylist(value, [])
                    .then(result => {
                        if(result.status){
                            showToast("Thêm thành công!", "", false)
                            loadContentByUrl("/my-playlist")
                        }else{
                            showToast(result.message, "", false)
                        }
                    })
        });
})

modeBtn.addEventListener('click', changeMode)

forwardBtn.addEventListener('click', forwardSong);

playBtn.addEventListener('click', () => {
    const isPlaying = musicContainer.classList.contains('play');

    if (isPlaying) {
        pauseSong();
    } else {
        playSong();
    }
});

prevBtn.addEventListener('click', prevSong)
nextBtn.addEventListener('click', nextSong)

// Time/song update
audio.addEventListener('timeupdate', updateProgress);

// Click on progress bar
progressContainer.addEventListener('click', setProgress);

audio.addEventListener('timeupdate',DurTime);
audio.addEventListener('ended', nextSong);

window.hideDiv = hideDiv;
window.hideDiv2 = hideDiv2;
window.loadContent = loadContent;
window.hideAllDivs = hideAllDivs;