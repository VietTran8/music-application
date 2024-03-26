let baseUrl = "http://localhost:8080/api/playlist"

export async function addPlaylist(title, songIds) {
    const requestBody = {
        title: title,
        songIds: songIds
    };

    try {
        const response = await fetch(baseUrl + '/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function addSongToPlaylist(playlistId, songIds){
    const requestBody = {
        playlistId: playlistId,
        songIds: songIds
    };

    try {
        const response = await fetch(baseUrl + '/add-song', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function removePlaylist(playlistId){
    try {
        const response = await fetch(baseUrl + '/delete/' + playlistId, {
            method: 'POST'
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function renamePlaylist(playlistId, title){
    const requestBody = {
        title: title
    };

    try {
        const response = await fetch(baseUrl + '/rename/' + playlistId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}

export async function getSongsFromPlaylist(playlistId){
    try {
        const response = await fetch(baseUrl + `/${playlistId}/songs`, {
            method: 'GET'
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        console.log(data.data)

        return data.data;
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return null;
    }
}

export async function removeSongFromPlaylist(playlistId, songIds){
    let requestBody = {
        "playlistId": playlistId,
        "songIds": songIds
    }

    try {
        const response = await fetch(baseUrl + '/delete-song', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message };
    }
}