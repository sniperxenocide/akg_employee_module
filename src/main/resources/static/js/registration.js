let MODE;
let employee;

function registrationPageInit(mode,selected_employee) {
    MODE = mode;
    employee = selected_employee;
    loadEmployeeFormMasterData();
    updateUI();
}

function updateUI() {
    if(MODE == UPDATE_EMPLOYEE){
        populateFormData();
        document.getElementById("emp_form_header").innerText = "Update Employee Info";
        document.getElementById("form_field_set").disabled = true;
        document.getElementById("registration_btn").style.display = "none";
        document.getElementById("edit_delete_btn_div").style.display = "block";
        document.getElementById("load_from_oracle_btn").style.display = "none";
    }
}

function alternateRequiredControl(element,targetId) {
    let target = document.getElementById(targetId);
    if (element.value.toString().length>0){
        element.required = true;
        target.required = false;
        element.parentElement.getElementsByTagName('span')[0].hidden = false;
        target.parentElement.getElementsByTagName('span')[0].hidden = true;
    }
}

function populateFormData() {
    let form = document.getElementById("employee_registration_form");
    let formData = new FormData(form);
    for (let key of formData.keys()){
        try {
            let val = employee["employee"][key];
            val = (key == 'dateOfBirth' || key == 'joiningDate') ?
                val.toString().split("T")[0]:val;
            form.elements[key].value = val;
            try {form.elements[key].onchange();}catch (e) {}  //Trigger onChange event
        }catch (e) {console.log(e);}
    }
}

function loadEmployeeFormMasterData() {
    if(MASTER_DATA != null) {
        loadSelectOptionFromJsonArray(MASTER_DATA["company"]["data"],"select_company","id","name","","-SELECT OPTION-");
        loadSelectOptionFromJsonArray(MASTER_DATA["department"]["data"],"select_department","id","name","","-SELECT OPTION-");
        loadSelectOptionFromJsonArray(MASTER_DATA["designation"]["data"],"select_designation","id","name","","-SELECT OPTION-");
        loadSelectOptionFromJsonArray(MASTER_DATA["work_location"]["data"],"select_work_location","id","name","","-SELECT OPTION-");
    }
}

function onClickEditBtn() {
    document.getElementById("form_field_set").disabled = false;
    document.getElementById("emp_update_btn").style.display = "block";
    document.getElementById("edit_delete_btn_div").style.display = "none";
}

function onClickDeleteBtn() {
    let deleteEmpMsgSpan = document.getElementById("delete_emp_msg_span");
    let confirmationMsg = "Are You Sure? Click Again To Delete This Employee";
    if (deleteEmpMsgSpan.innerText.length >0 ){
        callAPI(baseURL+deleteEmployeeAPI+employee["employee"]["id"],DELETE,
            null,deleteEmployeeCallback);
    }
    else {
        deleteEmpMsgSpan.innerText = confirmationMsg;
        setTimeout(function () {deleteEmpMsgSpan.innerText="";},5000);
    }
    function deleteEmployeeCallback(response) {
        try {
            let json = JSON.parse(response);
            if (json['status']==true){
                alert(json['msg']);
                location.reload();
            }
            else if(json['status']==false){
                alert(json['msg']);
            }
        }catch (e) {console.log(e);alert("Something Went Wrong!!!")}
    }
}

function performSubmitAction(form) {
    performOperation(form);
    return false;
}

function performOperation(form) {
    document.getElementById("registration_btn").disabled = true;
    document.getElementById("emp_update_btn").disabled = true;
    enableFormElements(form); // enabling disabled elements
    try {
        let formData = new FormData(form);
        let postBody = {};
        for (let pair of formData.entries()) postBody[pair[0]] = pair[1];
        console.log(JSON.stringify(postBody));
        if(MODE == CREATE_EMPLOYEE){
            callAPI(baseURL+createEmployeeAPI,POST,postBody,callback);
        }
        else if(MODE == UPDATE_EMPLOYEE) {
            let id = employee["employee"]["id"];
            callAPI(baseURL+updateEmployeeAPI+id,PUT,postBody,callback);
        }
    }catch (e) {console.log(e); alert(e);}
    document.getElementById("registration_btn").disabled = false;
    document.getElementById("emp_update_btn").disabled = false;

    function callback(response){
        try {
            console.log(response);
            let json = JSON.parse(response);
            alert(json["msg"]);
        }catch (e) {console.log(e);alert(e.toString()+" Failed!!!Something Went Wrong");}
        location.reload();
    }

}


function makeFormElementDisabledIfNotNullAfterOracleLoad(form) {
    try {
        for(let element of form.elements){
            try {
                if(element.value != null) {
                    if(element.value.toString().trim().length>0) element.disabled = true;
                }
            }catch (e) {console.log(e)}
        }
        form.elements['isVerified'].value = 'YES';
        form.elements['isActive'].value = 'YES';
    }catch (e) {console.log(e)}
}

function enableFormElements(form) {
    try {
        for(let element of form.elements){
            if(element.disabled == true) element.disabled = false;
        }
    }catch (e) {console.log(e)}
}

function importEmployeeFromOracle(btn) {
    let oracleIdMsgSpan = document.getElementById("oracle_id_import_msg");
    let oracleTextElement = document.getElementById("oracleId");
    let oracleId = oracleTextElement.value;
    if(oracleId.toString().trim()=="") setSpanMsg("Please type Oracle ID");
    else {
        callAPI(baseURL+loadOracleEmployeeDataAPI+oracleId,GET,null,loadFromOracleCallback);
        function loadFromOracleCallback(response) {
            try {
                let json = JSON.parse(response);
                if(json["status"]==true){
                    btn.disabled = true;
                    oracleTextElement.readOnly = true;
                    employee = {'employee':json['data']};
                    callAPI(baseURL+masterDataAPI,GET,null,masterDataCallbackNow);
                    function masterDataCallbackNow(response) {
                        let jsonObjectMaster  = JSON.parse(response);
                        if(jsonObjectMaster["status"]==true) MASTER_DATA = jsonObjectMaster["data"];
                        loadEmployeeFormMasterData();
                        populateFormData();
                        makeFormElementDisabledIfNotNullAfterOracleLoad(document.getElementById('employee_registration_form'));
                    }
                }
                else if(json["status"]==false){
                    setSpanMsg(json["msg"]);
                }
            }catch (e) {setSpanMsg("Something Went Wrong");}
        }
    }

    function setSpanMsg(msg) {
        oracleIdMsgSpan.innerText = msg;
        clearSpanMsg();
    }

    function clearSpanMsg() {
        setTimeout(function () {
            oracleIdMsgSpan.innerText="";
        },3000)
    }
}

