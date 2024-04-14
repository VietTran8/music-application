let baseUrl = "http://localhost:8080/api/album"

export async function getSongsFromAlbum(albumId){
    try {
        const response = await fetch(baseUrl + `/${albumId}/songs`, {
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

export async function favouriteAlbum(albumId){
    try {
        const response = await fetch(baseUrl + "/favourite/" + albumId, {
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

export async function addAlbum(requestBody){
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