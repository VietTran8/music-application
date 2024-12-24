let baseUrl = "/api/user";

export async function followUser(userEmail){
    var requestBody = {
        "email": userEmail
    }
    try {
        const response = await fetch(baseUrl + "/follow", {
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

export async function uploadImage(formData, type) {
    try {
        const response = await fetch(baseUrl + '/upload/' + type, {
            method: 'POST',
            body: formData
        });

        const data = await response.json();

        if (!data.status) {
            throw new Error(data.message);
        }

        return { status: data.status, message: data.message, data: data.data.url };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message, data: null };
    }
}