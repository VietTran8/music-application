import * as songService from './song-service.js'
import * as genreScript from './genre.js';
import * as playlistService from './playlist-service.js'
import * as userService from "./user-service.js"
import {removeSongFromPlaylist} from "./playlist-service.js";

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

    const fileInput = document.getElementById("image-upload");

    const imageIcon = document.querySelector(".image-container .image-overlay i");
}

function artistDetailsScript(){
    const $ = document.querySelector.bind(document);
    const $$ = document.querySelectorAll.bind(document);

    const seeMoreSongs = document.getElementById("see-more-songs");
    const seeMoreAlbums = document.getElementById("see-more-albums");
    const seeMoreFollowers = document.getElementById("see-more-followers");
    const btnAction = document.getElementById("btn-action");

    const userEmail = document.getElementById("user-email").value;

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

    seeMoreSongs.addEventListener("click", () => {
        seeMoreClickHandler(1);
    });

    seeMoreAlbums.addEventListener("click", () => {
        seeMoreClickHandler(2);
    });

    seeMoreFollowers.addEventListener("click", () => {
        seeMoreClickHandler(3);
    });

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

    const fileInput = document.getElementById("image-upload");

    const imageIcon = document.querySelector(".image-container .image-overlay i");

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
    function closeModal(){
        let myModalEl = document.getElementById('add-to-playlist');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}
function hideAllDivs() {
    var overlay = document.getElementById("overlay");
    var premiumDiv = document.querySelector('.premium-advert');
    var mediumDiv = document.querySelector('.medium-advert');

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
function loadSong(song){
    title.innerText = song.name;
    artist.innerText = song.artists[0].artistName;
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

    if (url === "http://localhost:8080/") {
        requestUrl += "home/component"
    } else if (url === "http://localhost:8080") {
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
    }
}

function loadContent(e, t){
    e.preventDefault();
    let url = t.getAttribute('href');
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
            const root = document.getElementById('root');
            root.innerHTML = html;
            executeScripts(url)
            window.history.pushState({ path: url }, '', url);
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