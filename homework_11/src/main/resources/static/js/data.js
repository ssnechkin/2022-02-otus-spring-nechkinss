const setLinkView = (rows) => {
    for (var row in rows) {
        rows[row].link = {"method": "GET", "value": "/author/" + rows[row].id};
    }
};

const setColumns = (rows) => {
    for (var row in rows) {
        rows[row].columns = [rows[row].surname, rows[row].name, rows[row].patronymic];
    }
};

const subCallBack = (method, url, headers, response, isOk) => {
    if(method == 'put' && url.indexOf('/author/')==0) {
        notification("Автор сохранен");
    }
    if(method == 'delete' && url.indexOf('/author/')==0) {
        notification("Автор удален");
        http.get(getList, '/author');
    }else
    if(method == 'get' && url == '/author')  {
        let init = {
            "page_name": "Авторы",
            "management": [
                {
                    "position": 0,
                    "title": "Добавить",
                    "link": {
                        "method": "GET",
                        "value": "/author/add"
                    }
                }
            ],
            "table": {
                "labels": [
                    "Фамилия",
                    "Имя",
                    "Отчество"
                ],
                "rows": []
            }
          };
        init.table.rows = response;
        setLinkView(init.table.rows);
        setColumns(init.table.rows);
        contentReader(init);
    } else

    if(method == 'get' && url.indexOf('/author/add') == 0) {
        let init = {
            "page_name": "Автор - добавление",
            "management": [
                {
                    "position": 0,
                    "title": "Добавить",
                    "link": {
                        "method": "POST",
                        "value": "/author"
                    }
                }
            ],
            "form": {
                "fields": [
                    {
                        "type": "INPUT",
                        "label": "Фамилия",
                        "name": "surname",
                        "value": null,
                        "values": null
                    },
                    {
                        "type": "INPUT",
                        "label": "Имя",
                        "name": "name",
                        "value": null,
                        "values": null
                    },
                    {
                        "type": "INPUT",
                        "label": "Отчество",
                        "name": "patronymic",
                        "value": null,
                        "values": null
                    }
                ]
            }
           };
        contentReader(init);
    } else
    if(method == 'get' && url.indexOf('/author/') == 0) {
        viewItem(response, true);
    }else

    if(method == 'post' && url.indexOf('/author') == 0) {
        viewItem(response, true);
        notification("Автор добавлен");
    }
}

const viewItem = (response, isOk) => {
    console.log('viewItem ' + response);
    let init = {
        "page_name": "Автор",
        "management": [
            {
                "position": 1,
                "title": "Сохранить",
                "link": {
                    "method": "PUT",
                    "value": "/author/"+response.id
                }
            },
            {
                "position": 2,
                "title": "Удалить",
                "link": {
                    "method": "DELETE",
                    "value": "/author/"+response.id
                }
            }
        ],
        "form": {
            "fields": [
                {
                    "type": "INPUT",
                    "label": "Фамилия",
                    "name": "surname",
                    "value": response.surname
                },
                {
                    "type": "INPUT",
                    "label": "Имя",
                    "name": "name",
                    "value": response.name
                },
                {
                    "type": "INPUT",
                    "label": "Отчество",
                    "name": "patronymic",
                    "value": response.patronymic
                }
            ]
        }
       };
    contentReader(init);
}

const notification = (text) => {
    let init = {
        "notifications": [
            {
                "type": "INFO",
                "message": text
            }
        ]
       };
    contentReader(init);
}
