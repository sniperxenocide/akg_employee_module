const GET = "GET";
const POST = "POST";
const PUT = "PUT";
const DELETE = "DELETE";
const CREATE_EMPLOYEE = 0;
const UPDATE_EMPLOYEE = 1;
let MASTER_DATA = null;

const baseURL = location.origin;
const loginAPI = "/authenticate";
const homePageAPI = "/home";
const allEmployeePageAPI = "/employee";
const employeeRegistrationPageAPI = "/registration";
const broadcastPageAPI = "/broadcast";
const broadcastStatusPageAPI = "/broadcast_status";

const masterDataAPI = "/api/v1/master/all";
const createEmployeeAPI = "/api/v1/employee/create";
const getAllEmployeeAPI = "/api/v1/employee/all";
const getAllQueriedEmployeeAPI = "/api/v1/employee/query/all";
const getAllEmployeeByNameAPI = "/api/v1/employee/all/name/";
const getOneEmployeeByOracleIdAPI = "/api/v1/employee/oracle/";
const getOneEmployeeByNidAPI = "/api/v1/employee/nid/";
const getOneEmployeeByEmployeeNumberAPI = "/api/v1/employee/empnum/";
const getOneEmployeeByBirthCertificateAPI = "/api/v1/employee/bircert/";
const updateEmployeeAPI = "/api/v1/employee/update/";
const deleteEmployeeAPI = "/api/v1/employee/delete/";
const loadOracleEmployeeDataAPI = "/api/v1/oracle/load/employee/";
const createBroadcastRequestAPI = "/api/v1/broadcast/create";
const getAllBroadcastStatus = "/api/v1/broadcast/status/all";

function showLoader() {
    document.getElementById("progress_loader_modal").style.display="block";
}

function hideLoader() {
    document.getElementById("progress_loader_modal").style.display="none";
}

function callAPI(url, method, body,callback) {
    showLoader();
    try {
        let xmlHttp = new XMLHttpRequest();
        xmlHttp.open(method, url, true);
        xmlHttp.setRequestHeader("Content-Type","application/json");
        let token = sessionStorage.getItem("token");
        if(token!=null){
            xmlHttp.setRequestHeader("Authorization", "Bearer "+token);
        }
        xmlHttp.onload = function (e) {
            hideLoader();
            if (xmlHttp.readyState === 4) {
                if (xmlHttp.status === 200) {
                    callback(xmlHttp.responseText);
                } else {
                    console.error(xmlHttp.statusText);
                    alert("Error: "+xmlHttp.statusText);
                }
            }
        };
        xmlHttp.onerror = function (e) {
            console.error(e);
            hideLoader();
        };
        xmlHttp.send(JSON.stringify(body));
        console.log(url + " " + xmlHttp.status);
    }catch (e) {hideLoader()}
}

function createSelectOption(value,text) {
    let option = document.createElement("option");
    option.text = text;
    option.value = value;
    return option;
}

function fetchAndLoadSelectOptions(url,selectID,value,text) {
    let x = document.getElementById(selectID);
    x.innerHTML = "";
    x.add(createSelectOption("%","ALL"));
    if (url.toString().endsWith("%")) {   // Checking if "ALL" selected
        try{x.onchange();}catch(e){}
        return;
    }
    callAPI(url,GET,null,fetchAndLoadCallback);
    function fetchAndLoadCallback(response) {
        try {
            let jsonArray = JSON.parse(response);
            if (jsonArray == null) return;
            for ( i in jsonArray) {
                x.add(createSelectOption(jsonArray[i][value],jsonArray[i][text]));
            }
        } catch (e) {
            console.log(e);
        }
        try{x.onchange();}catch(e){}
    }
}

function loadSelectOptionFromJsonArray(jsonArray,selectId,value,text,defaultValue,defaultText) {
    let select = document.getElementById(selectId);
    select.innerHTML = "";
    if(defaultValue != null)
        select.appendChild(createSelectOption(defaultValue,defaultText));

    try {
        if (jsonArray == null) return;
        for ( let i in jsonArray) {
            select.appendChild(createSelectOption(jsonArray[i][value],jsonArray[i][text]));
        }
    } catch (e) {
        console.log(e);
    }
}

function allowOnlyIntegerInput(event){
    let c = parseInt(event.which || event.key);
    event.returnValue = c >= 48 && c <= 57;
}


function modalAction(){
    let modal = document.getElementById("modal");
    let span = document.getElementsByClassName("close")[0];
    modal.style.display = "block";
    span.onclick = function() {
        modal.style.display = "none";
    }
}

function table_to_array(table_id) {
    let myData = document.getElementById(table_id).rows;
    let my_list = [];
    for (let i = 0; i < myData.length; i++) {
        let el = myData[i].children;
        let my_el = [];
        for (let j = 0; j < el.length; j++) {
            my_el.push(el[j].innerText);
        }
        my_list.push(my_el);

    }
    return my_list;
}

function exportToExcel(tableId) {
    let wb = XLSX.utils.book_new();
    let data = table_to_array(tableId);
    let ws = XLSX.utils.aoa_to_sheet(data);
    XLSX.utils.book_append_sheet(wb, ws, "Data");
    XLSX.writeFile(wb, "Data.xlsx");
}