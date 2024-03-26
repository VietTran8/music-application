let baseUrl = "http://localhost:8080/api/song";

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

    