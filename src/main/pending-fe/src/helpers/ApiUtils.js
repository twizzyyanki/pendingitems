export const queryApi = async (url = "", data = {}, method = "GET") => {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json'
        }
    };
    if (data && Object.keys(data).length > 0 && data.constructor === Object) {
        options.body = JSON.stringify(data);
    }

    const response = await fetch(url, options);
    const json =  await response.json();
    return json;
}

export const deleteApi = async (url = "") => {
    const options = {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const response = await fetch(url, options);
    return response;
}
