let baseUrl = "/api/song";

export async function getSongById(songId) {
    try {
        const response = await fetch(`${baseUrl}/${songId}`);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log(data);

        return data.data;
    } catch (error) {
        console.error(`Some errors occurred: ${error}`);
    }
}

export async function getAllSong(page, limit) {
    try {
        const response = await fetch(baseUrl);

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log(data);

        return data.data;
    } catch (error) {
        console.error(`Some errors occurred: ${error}`);
    }
}

export async function addSongPlays(songId){
    try {
        const response = await fetch(baseUrl + `/add-plays/${songId}`,
            {
                method: 'POST'
            })

        if(!response.ok){
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log(data);

        return data.data;
    }catch (error) {
        console.error(`Some errors occurred: ${error}`);
    }
}

export async function addSong(requestBody){
    try {
        const response = await fetch(baseUrl + `/add`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            })

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };
    }catch (error) {
        console.error(`Some errors occurred: ${error}`);
        return { status: false, message: error.message };
    }
}

export async function favouriteSong(songId){
    try {
        const response = await fetch(baseUrl + "/favourite/" + songId, {
            method: 'POST'
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };

    }catch(error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function updateSongName(songId, songName){
    let requestBody = {
        "songId": songId,
        "newSongName": songName
    }
    try {
        const response = await fetch(baseUrl + "/update-name", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };

    }catch(error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function updateSongLyrics(songId, songLyrics){
    let requestBody = {
        "songId": songId,
        "newSongLyrics": songLyrics
    }
    try {
        const response = await fetch(baseUrl + "/update-lyrics", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };

    }catch(error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function updateSongImage(songId, songImage){
    let formData = new FormData();
    formData.append("songId", parseInt(songId));
    formData.append("file", songImage);

    try {
        const response = await fetch(baseUrl + "/update-image", {
            method: 'POST',
            body: formData
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };

    }catch(error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function deleteSong(songId){
    try {
        const response = await fetch(baseUrl + "/delete/" + songId, {
            method: 'POST',
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };

    }catch(error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function updateSong(songId, requestBody){
    console.log(requestBody);
    try {
        const response = await fetch(baseUrl + `/update/${songId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };

    }catch(error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}