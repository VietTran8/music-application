const baseUrl = "/api/package"

export async function createPayment(packageId, amount){
    let requestBody = {
        "packageId": packageId,
        "paymentMethod": "Thanh toán chuyển khoản qua VNPAY",
        "amount": amount
    }

    try {
        const response = await fetch(baseUrl + '/create-payment', {
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

        return { status: data.status, message: data.message, data: data.data };
    } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        return { status: false, message: error.message, data: null };
    }
}