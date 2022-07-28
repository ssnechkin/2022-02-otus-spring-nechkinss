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
    if (response.notifications) {
        for (var prop in response.notifications) {
            alert(response.notifications[prop].message);
        }
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
            body[formFieldIds[prop]] = document.getElementById(formFieldIds[prop]).value;
        }
        http.send(method, contentReader, link, {"Content-Type": "application/json"},  JSON.stringify(body));
    }
    if (formDataType=='QUERY_STRING_PARAMETERS') {
        var requestParams='?';
        for (var prop in formFieldIds) {
            if (requestParams=='?') {
                requestParams+=formFieldIds[prop]+'='+document.getElementById(formFieldIds[prop]).value;
            } else {
                requestParams+='&'+formFieldIds[prop]+'='+document.getElementById(formFieldIds[prop]).value;
            }
        }
        http.send(method, contentReader, link+requestParams, null,  null);
    }
    if (formDataType=='FORM_DATA') {
        var formData = new FormData();
        for (var prop in formFieldIds) {
            formData.append(formFieldIds[prop], document.getElementById(formFieldIds[prop]).value);
        }
        http.send(method, contentReader, link, {"Content-Type": "application/x-www-form-urlencoded"},  new URLSearchParams(formData));
    }
};

const initCallback = (response, isOk) => {
    contentReader(response);
}

http.get(initCallback, '/menu');