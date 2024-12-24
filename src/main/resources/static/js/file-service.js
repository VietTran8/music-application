let baseUrl = "/api/file";

export async function uploadFile(formData, type) {
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