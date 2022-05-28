const http = {
    send: (method, callBack, url, headers, body) => {
        //console.log(method + " " + url);
        if (!headers) headers = {"Content-type": "application/json; charset=UTF-8"};
        switch (method) {
          case 'GET':
            http.get(callBack, url, headers);
            break;
          case 'POST':
            http.post(callBack, url, headers, body);
            break;
          case 'PUT':
            http.put(callBack, url, headers, body);
            break;
          case 'DELETE':
            http.del(callBack, url, headers, body);
            break;
          case 'PATCH':
            http.patch(callBack, url, headers, body);
            break;
          default:
            console.log("Метод не существует: " + method);
        }
    },
    get: (callBack, url, headers) => {
        fetch(url, {
             method: 'get',
             headers: headers
         })
        .then(data => data.json())
        .then((json) => {
            if (!json.ok) {
                callBack(json, false);
                if(subCallBack != undefined) subCallBack('get', url, headers, json, false);
            } else {
                callBack(json, true);
                if(subCallBack != undefined) subCallBack('get', url, headers, json, true);
            }
        }).catch(function (error) {
            callBack(error, false);
            if(subCallBack != undefined) subCallBack('get', url, headers, error, false);
        });
    },
    post: (callBack, url, headers, body) => {
        fetch(url, {
            method: 'post',
            headers: headers,
            body: body
        })
        .then(data => data.json())
        .then((json) => {
            if (!json.ok) {
                callBack(json, false);
                if(subCallBack != undefined) subCallBack('post', url, headers, json, false);
            } else {
                callBack(json, true);
                if(subCallBack != undefined) subCallBack('post', url, headers, json, true);
            }
        }).catch(function (error) {
            callBack(error, false);
            if(subCallBack != undefined) subCallBack('post', url, headers, error, false);
        });
    },
    put: (callBack, url, headers, body) => {
        fetch(url, {
            method: 'put',
            headers: headers,
            body: body
        })
        .then(data => data.json())
        .then((json) => {
            if (!json.ok) {
                callBack(json, false);
                if(subCallBack != undefined) subCallBack('put', url, headers, json, false);
            } else {
                callBack(json, true);
                if(subCallBack != undefined) subCallBack('put', url, headers, json, true);
            }
        }).catch(function (error) {
            callBack(error, false);
            if(subCallBack != undefined) subCallBack('put', url, headers, error, false);
        });
    },
    del: (callBack, url, headers, body) => {
        fetch(url, {
            method: 'delete',
            headers: headers,
            body: body
        })
        .then(data => data.json())
        .then((json) => {
            if (!json.ok) {
                callBack(json, false);
                if(subCallBack != undefined) subCallBack('delete', url, headers, json, false);
            } else {
                callBack(json, true);
                if(subCallBack != undefined) subCallBack('delete', url, headers, json, true);
            }
        }).catch(function (error) {
            callBack(error, false);
            if(subCallBack != undefined) subCallBack('delete', url, headers, error, false);
        });
    },
    patch: (callBack, url, headers, body) => {
        fetch(url, {
            method: 'patch',
            headers: headers,
            body: body
        })
        .then(data => data.json())
        .then((json) => {
            if (!json.ok) {
                callBack(json, false);
                if(subCallBack != undefined) subCallBack('patch', url, headers, json, false);
            } else {
                callBack(json, true);
                if(subCallBack != undefined) subCallBack('patch', url, headers, json, true);
            }
        }).catch(function (error) {
            callBack(error, false);
            if(subCallBack != undefined) subCallBack('patch', url, headers, error, false);
        });
    }
}