let flag = {};
let flagSelectAll = {};
let selectedOptionsCount = {};
let checkBoxContainer = null;
let current_map_key = "";
let map_keys = ["company","department","designation","work_location","broadcast_media"];
let filteredEmpList = [] ;
let empIdList = [];

function broadcastPageInit() {
    initAll();
}

function initPartialFlags(val) {
    try{
        flag[current_map_key] = new Array(MASTER_DATA[current_map_key]["data"].length).fill(val);
    }catch (e) {console.log(e)}
}

function initAll() {
    checkBoxContainer = document.getElementById("select_option_checkboxes");
    filteredEmpList = [] ;
    empIdList = [];
    try {
        for (let j in map_keys){
            flagSelectAll[map_keys[j]] = false;
            selectedOptionsCount[map_keys[j]] = 0;
            flag[map_keys[j]] = new Array(MASTER_DATA[map_keys[j]]["data"].length).fill(false);
        }
    }catch (e) {console.log(e)}
}

function onClickSelectModal(header_text,key_index) {
    current_map_key = map_keys[key_index];
    document.getElementById("select_option_header").innerText = header_text;
    generateSelectAllCheckBox();
    generateCheckBoxes();
    modalAction();
}

function onCheckSelectAll() {
    flagSelectAll[current_map_key] = !flagSelectAll[current_map_key];
    initPartialFlags(flagSelectAll[current_map_key]);
    if(flagSelectAll[current_map_key])
        selectedOptionsCount[current_map_key]=flag[current_map_key].length;
    else selectedOptionsCount[current_map_key] = 0;

    generateSelectAllCheckBox();
    generateCheckBoxes();
    updateBtnText();
    clearTable();
}

function onClickCheck(position) {
    flag[current_map_key][position] = !flag[current_map_key][position];
    if(flag[current_map_key][position]) selectedOptionsCount[current_map_key]++;
    else selectedOptionsCount[current_map_key]--;

    document.getElementById("select_all_cb").checked = false;
    flagSelectAll[current_map_key] = false;
    if(selectedOptionsCount[current_map_key]==flag[current_map_key].length){
        document.getElementById("select_all_cb").checked = true;
        flagSelectAll[current_map_key] = true;
    }

    updateBtnText();
    clearTable();
}

function clearTable() {
    // Not Clearing Table for Broadcast Media Selection
    if(current_map_key == map_keys[4]) return;

    // Clearing Table when Filter Option Changes
    if(filteredEmpList.length > 0){
        filteredEmpList = [];
        populateTable();
    }
}

function updateBtnText() {
    let text = "SELECT OPTION";
    if(selectedOptionsCount[current_map_key]>0)
        text = selectedOptionsCount[current_map_key]+" Selected";
    document.getElementById("btn_"+current_map_key).innerText = text;
}

function generateSelectAllCheckBox() {
    let checkedSA = flagSelectAll[current_map_key]?"checked":"";
    checkBoxContainer.innerHTML =
        "<div class='row'><div class='col-5'><label></label></div>"+
        "<div class='col-5' ><input id='select_all_cb' type='checkbox' "+checkedSA+" onclick='onCheckSelectAll()' ></div>" +
        "<div class='col-75'><b>SELECT ALL</b></div></div>";
}

function generateCheckBoxes() {
    let dataList = MASTER_DATA[current_map_key]['data'];
    for(let i in dataList){
        let checked = flag[current_map_key][i]?"checked":"";
        checkBoxContainer.innerHTML += "<div class='row'>"+
            "<div class='col-5'>"+(parseInt(i)+1)+".</div>"+
            "<div class='col-5'><input type='checkbox' onclick='onClickCheck("+i+")' "+checked+" ></div>" +
            "<div class='col-75'>"+dataList[i]['name']+"</div></div>";
    }
}

function applyFilter() {
    try {
        let body = {};
        // Index 0,1,2,3 except broadcast_media(4)
        for(let i=0;i<=3;i++){
            body[map_keys[i]] = [];
            for(let j in flag[map_keys[i]]){
                if(flag[map_keys[i]][j]){
                    body[map_keys[i]].push(MASTER_DATA[map_keys[i]]["data"][j]["id"]);
                }
            }
        }
        console.log(body);
        callAPI(baseURL+getAllQueriedEmployeeAPI,POST,body,applyFilterCallback);
    }catch (e) {console.log(e)}
}


function applyFilterCallback(response) {
    try {
        filteredEmpList = JSON.parse(response)["data"];
        populateTable();
    }catch (e) {console.log(e);alert(e);filteredEmpList=[];empIdList=[];}
}

function populateTable() {
    empIdList = [];
    document.getElementById("broadcast_table_header").innerHTML
        = "<b> Total Filtered Employees : "+filteredEmpList.length+"</b>";
    let tbody = "";
    for (let i in filteredEmpList){
        tbody+="<tr>";
        tbody+="<td><input type='checkbox' onclick='onCheckEmployee("+i+")' checked></td>";
        tbody+="<td>"+(parseInt(i)+1)+"</td>";
        tbody+="<td>"+filteredEmpList[i]['name']+"</td>";
        tbody+="<td>"+filteredEmpList[i]['company']+"</td>";
        tbody+="<td>"+filteredEmpList[i]['department']+"</td>";
        tbody+="<td>"+filteredEmpList[i]['designation']+"</td>";
        tbody+="<td>"+filteredEmpList[i]['work_location']+"</td>";
        tbody+="</tr>";
        empIdList.push(parseInt(filteredEmpList[i]['id']));
    }
    if(filteredEmpList.length==0)
        tbody+="<td colspan='7'>Apply Filter</td>";

    document.getElementById("employee_table_body").innerHTML=tbody;
    console.log(empIdList);
}

function onCheckEmployee(position) {
    let id = parseInt(filteredEmpList[position]['id']);
    if(empIdList.includes(id))
        empIdList.splice(empIdList.indexOf(id),1);
    else empIdList.push(id);

    console.log(empIdList);
}

function countAndShowMsgLength(val) {
    let len = val.toString().length;
    let element = document.getElementById('msg_length');
    if(len>0) element.innerText = "Length: "+len;
    else element.innerText = "";
}

function onClickSend() {
    try {
        let messageBody = document.getElementById('message_body').value;
        let broadcastRequestBody = {};
        // Index 4 broadcast_media
        let idx = 4;
        broadcastRequestBody['messageBody'] = messageBody;
        broadcastRequestBody['broadcastMedias'] = [];
        for(let j in flag[map_keys[idx]]){
            if(flag[map_keys[idx]][j]){
                broadcastRequestBody['broadcastMedias'].push(MASTER_DATA[map_keys[idx]]["data"][j]["id"]);
            }
        }
        broadcastRequestBody['employeeIds'] = empIdList;

        if(broadcastRequestBody['messageBody'].toString().trim().length==0){
            alert("Please Type Message"); return;
        }
        if(broadcastRequestBody['broadcastMedias'].length == 0){
            alert("Please Select Broadcast Media"); return;
        }
        if(broadcastRequestBody['employeeIds'].length == 0){
            alert("Please Select Employees"); return;
        }

        console.log(broadcastRequestBody);

        let confirmationMsgSpan = document.getElementById("broadcast_request_confirmation_span");
        let medias = parseInt(broadcastRequestBody['broadcastMedias'].length);
        let emps = parseInt(broadcastRequestBody['employeeIds'].length);
        let confirmationMsg = "Total "+medias*emps +" Messages will be sent to "+
                emps+" Employees in "+medias+" Selected Broadcast Medias Each."+
                "Please click SEND again to confirm.";
        if (confirmationMsgSpan.innerText.length >0 ){
            callAPI(baseURL+createBroadcastRequestAPI,POST,broadcastRequestBody,broadcastRequestCallback);
        }
        else {
            confirmationMsgSpan.innerText = confirmationMsg;
            setTimeout(function () {confirmationMsgSpan.innerText="";},7000);
        }
    }catch (e) {console.log(e)}
}

function broadcastRequestCallback(response) {
    console.log(response);
    try {
        let jsonResponse = JSON.parse(response);
        alert(jsonResponse['msg']);
        location.reload();
    }catch (e) {console.log(e);alert(e);}
}