var menuButtons = document.getElementById("menu_buttons");
var pageName = document.getElementById("page_name");
var management = document.getElementById("management");
var content = document.getElementById("content");
var formFieldIds = [];

const contentReader = (response, isOk) => {
    console.log(JSON.stringify(response));
    if (response.menu) {
        menuButtons.innerHTML = genHtml.getButtons(response.menu, followLink);
    }
    if (response.page_name) {
        pageName.innerHTML = response.page_name;
    }
    if (response.table) {
        console.log("table");
        content.innerHTML = genHtml.getTable(response.table.labels, response.table.rows, followLink);
    }
    if (response.management && !response.form) {
        management.innerHTML = genHtml.getButtons(response.management, followLink);
    }
    if (response.form) {
        content.innerHTML = genHtml.getFields(response.form.fields);
        management.innerHTML = genHtml.getButtons(response.management, followFormLink);
        saveFormFieldIds(response.form.fields);
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

const saveFormFieldIds = (fields) => {
    formFieldIds = [];
    for (var prop in fields) {
        formFieldIds.push(fields[prop].name);
    }
};

const followLink = (method, link) => {
    http.send(method, contentReader, link);
};

const followFormLink = (method, link) => {
    var body = {};
    for (var prop in formFieldIds) {
        body[formFieldIds[prop]] = document.getElementById(formFieldIds[prop]).value;
    }
    http.send(method, contentReader, link, null,  JSON.stringify(body));
};

const initCallback = (response, isOk) => {
    contentReader(response);
}

http.post(initCallback, '/');