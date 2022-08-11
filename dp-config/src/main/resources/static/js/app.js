var topRight = document.getElementById("top_right");
var menuButtons = document.getElementById("menu_buttons");
var pageName = document.getElementById("page_name");
var management = document.getElementById("management");
var content = document.getElementById("content");
var formDataType;
var formFieldIds = [];

const contentReader = (response, isOk) => {
    if(menuButtons.innerHTML == '' && !response.menu) {
        http.get(initCallback, '/menu');
    }
    if (response.top_right) {
        var text = '';
        if (response.top_right.text) {
            text = '<span>'+response.top_right.text+'</span>';
        }
        topRight.innerHTML = text + genHtml.getButtons(response.top_right.buttons, followLink);
    }
    if (response.menu) {
        menuButtons.innerHTML = genHtml.getButtons(response.menu, followLink);
    }
    if (response.page_name) {
        pageName.innerHTML = response.page_name;
    }
    if (response.table) {
        content.innerHTML = genHtml.getTable(response.table.labels, response.table.rows, followLink);
    }
    if (response.management && !response.form) {
        management.innerHTML = genHtml.getButtons(response.management, followLink);
    }
    if (response.form) {
        content.innerHTML = genHtml.getFields(response.form.fields);
        management.innerHTML = genHtml.getButtons(response.management, followFormLink);
        saveFormFieldIds(response.form);
    }
    if (response.fields) {
        content.innerHTML = genHtml.getFields(response.fields, true);
    }
    if (response.fields && response.table) {
        content.innerHTML = genHtml.getFields(response.fields, true)
        + genHtml.getTable(response.table.labels, response.table.rows, followLink);
    }
    if (response.notifications) {
        for (var prop in response.notifications) {
            alert(response.notifications[prop].message);
        }
    }
    if (response.status == 403 && response.error == 'Forbidden') {
        alert('У Вас недостаточно прав для совершения операции');
    }
    if (response.status == 500 && response.error == 'Internal Server Error') {
        alert('Непредвиденная ошибка на сервере. Операция не выполнена');
    }
}

const saveFormFieldIds = (form) => {
    var fields = form.fields;
    formDataType = form.dataType;
    formFieldIds = [];
    for (var prop in fields) {
        formFieldIds.push(fields[prop].name);
    }
};

const followLink = (method, link) => {
    http.send(method, contentReader, link);
};

const followFormLink = (method, link) => {
    if (formDataType=='JSON') {
        var body = {};
        for (var prop in formFieldIds) {
            if(document.getElementById(formFieldIds[prop]).type == 'checkbox'){
                body[formFieldIds[prop]] = document.getElementById(formFieldIds[prop]).checked;
            } else {
                body[formFieldIds[prop]] = document.getElementById(formFieldIds[prop]).value;
            }
        }
        http.send(method, contentReader, link, {"Content-Type": "application/json"},  JSON.stringify(body));
    }
    if (formDataType=='QUERY_STRING_PARAMETERS') {
        var requestParams='?';
        for (var prop in formFieldIds) {
            if (requestParams=='?') {
                if(document.getElementById(formFieldIds[prop]).type == 'checkbox'){
                    requestParams+=formFieldIds[prop]+'='+document.getElementById(formFieldIds[prop]).checked;
                } else {
                    requestParams+=formFieldIds[prop]+'='+document.getElementById(formFieldIds[prop]).value;
                }
            } else {
                if(document.getElementById(formFieldIds[prop]).type == 'checkbox'){
                    requestParams+='&'+formFieldIds[prop]+'='+document.getElementById(formFieldIds[prop]).checked;
                } else {
                    requestParams+='&'+formFieldIds[prop]+'='+document.getElementById(formFieldIds[prop]).value;
                }
            }
        }
        http.send(method, contentReader, link+requestParams, null,  null);
    }
    if (formDataType=='FORM_DATA') {
        var formData = new FormData();
        for (var prop in formFieldIds) {
            if(document.getElementById(formFieldIds[prop]).type == 'checkbox'){
                formData.append(formFieldIds[prop], document.getElementById(formFieldIds[prop]).checked);
            } else {
                formData.append(formFieldIds[prop], document.getElementById(formFieldIds[prop]).value);
            }
        }
        http.send(method, contentReader, link, {"Content-Type": "application/x-www-form-urlencoded"},  new URLSearchParams(formData));
    }
};

const initCallback = (response, isOk) => {
    contentReader(response);
}

http.get(initCallback, '/menu');