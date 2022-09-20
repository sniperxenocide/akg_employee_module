
let menu_buttons = ["employee","registration","broadcast","broadcast_status"];

function init() {
    callAPI(baseURL+masterDataAPI,GET,null,masterDataCallback);
    function masterDataCallback(response) {
        let jsonObject  = JSON.parse(response);
        if(jsonObject["status"]==true) MASTER_DATA = jsonObject["data"];
        document.getElementById(menu_buttons[0]).click();
    }
}

function menuButtonColorChange(id) {
    for(btn_id in menu_buttons){
        document.getElementById(menu_buttons[btn_id]).classList.remove("active");
    }
    document.getElementById(id).classList.add("active");
}

function loadRegistrationPage(btn) {
    menuButtonColorChange(btn.id);
    callAPI(baseURL + employeeRegistrationPageAPI, GET, null,callback);
    function callback(response) {
        document.getElementById("home_content").innerHTML = response;
        registrationPageInit(CREATE_EMPLOYEE,null);
    }
}

function loadEmployeePage(btn) {
    menuButtonColorChange(btn.id);
    callAPI(baseURL + allEmployeePageAPI, GET, null,callback);
    function callback(response) {
        document.getElementById("home_content").innerHTML = response;
        employeeListInit();
    }
}

function loadBroadcastPage(btn) {
    menuButtonColorChange(btn.id);
    callAPI(baseURL + broadcastPageAPI, GET, null,callback);
    function callback(response) {
        document.getElementById("home_content").innerHTML = response;
        broadcastPageInit();
    }
}

function loadBroadcastStatusPage(btn) {
    menuButtonColorChange(btn.id);
    callAPI(baseURL + broadcastStatusPageAPI, GET, null,callback);
    function callback(response) {
        document.getElementById("home_content").innerHTML = response;
        broadcastStatusInit();
    }
}
