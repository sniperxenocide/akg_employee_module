let empArray = [];
let firstPage = 1;
let currentPage = 1;
let requestedPage = 1;

let lastPage = 0;
let pageSize = 0;
let totalEmp = 0;

function employeeListInit() {
    loadFilterMasterData()
}

function loadFilterMasterData() {
    if(MASTER_DATA != null){
        loadSelectOptionFromJsonArray(MASTER_DATA["company"]["data"],"filter_select_company","id","name","%","-ALL-");
        loadSelectOptionFromJsonArray(MASTER_DATA["department"]["data"],"filter_select_department","id","name","%","-ALL-");
        loadSelectOptionFromJsonArray(MASTER_DATA["designation"]["data"],"filter_select_designation","id","name","%","-ALL-");
        loadSelectOptionFromJsonArray(MASTER_DATA["work_location"]["data"],"filter_select_work_location","id","name","%","-ALL-");
    }
}


function searchDataWithKeyword() {
    loadFilterMasterData(); // resetting filter data
    let searchKeywordElement = document.getElementById("search_keyword");
    let searchTypeElement = document.getElementById("search_type");
    let keyword = searchKeywordElement.value.toString().trim();
    if(keyword.length > 0){
        let url = baseURL;
        if(searchTypeElement.value == 1) url += getOneEmployeeByOracleIdAPI ;
        else if(searchTypeElement.value == 2) url += getOneEmployeeByNidAPI ;
        else if(searchTypeElement.value == 3) url += getAllEmployeeByNameAPI ;
        else if(searchTypeElement.value == 4) url += getOneEmployeeByEmployeeNumberAPI ;
        else if(searchTypeElement.value == 5) url += getOneEmployeeByBirthCertificateAPI ;
        callAPI(url+keyword,GET,null,searchKeywordCallback);
    }
    function searchKeywordCallback(response) {
        document.getElementById('pagination-container').style.display = 'none';
        empArray = [];
        try {
            let json = JSON.parse(response);
            console.log(json);
            if(json['status']==true){
                if(Array.isArray(json['data'])) empArray = json['data'];
                else empArray.push(json['data']);
            }
        }catch (e) {console.log(e);}
        populateEmployeeTable();
    }
}

function applyFilterButtonAction(form) {
    requestedPage = 1;
    searchDataWithFilter(form);
    return false;
}

function searchDataWithFilter(form) {
    document.getElementById("search_keyword").value = ""; //resetting search keyword
    try {
        let formData = new FormData(form);
        let url = baseURL+getAllEmployeeAPI+"?";
        for (let pair of formData.entries()) {
            if(pair[1]!="%")url += pair[0]+"="+pair[1]+"&";
        }
        url += "page="+requestedPage;
        callAPI(url,GET,null,searchEmployeeCallback);
    } catch (e) {
        console.log(e);
    }
    return false;
}

function searchEmployeeCallback(response){
    try {
        let employeeArray = JSON.parse(response);
        if(employeeArray["status"]==true) {
            empArray = employeeArray["data"];
            setPaginationInfo(employeeArray["msg"]);
            populateEmployeeTable();
        }
        else throw "Error";
    }catch (e) {console.log(e);
        document.getElementById("table_info_label").innerText = "Something Went Wrong";
    }
}

function setPaginationInfo(json){
    let js = JSON.parse(json);
    currentPage = js['currentPage']; pageSize = js['pageSize'];
    lastPage = js['lastPage']; totalEmp = js['empCount'];

    document.getElementById('pagination-container').style.display = 'block';
    document.getElementById('crnt-pg-no').innerText = currentPage;
    document.getElementById('last-page-btn').innerText = lastPage;
}

function paginationClickAction(val){
    if(val.toString()==='c') requestedPage = firstPage;
    else if(val.toString()==='p'){
        if(currentPage>1) requestedPage = currentPage - 1;
    }
    else if(val.toString()==='n'){
        if(currentPage<lastPage) requestedPage = currentPage + 1;
    }
    else if(val.toString()==='l') requestedPage = lastPage;
    searchDataWithFilter(document.getElementById("emp-filter-form"));
}

function populateEmployeeTable() {
    let tbody = "";
    let colspan = "20";
    try {
        if(empArray.length == 0){
            document.getElementById("table_info_label").innerText = "Nothing Found";
        }
        else {
            let st = (currentPage-1)*pageSize;
            document.getElementById("table_info_label").innerText
                = "Showing "+(st+1)+'-'+(st+empArray.length)+' of '+totalEmp+" Employees. Click to View Detail";
        }
        let rowNum=1;
        for (let r in empArray){
            tbody+="<tr onclick='onClickEmployeeTableRow("+r+")'>";
            tbody += "<td ><b>"+rowNum+"</b></td>";
            let cls = "'true'";
            if(empArray[r]["employee"]["isVerified"]=="NO") cls = "'false'";
            tbody += "<td class="+cls+">"+empArray[r]["employee"]["isVerified"]+"</td>";
            cls = "'true'";
            if(empArray[r]["employee"]["isActive"]=="NO") cls = "'false'";
            tbody += "<td class="+cls+">"+empArray[r]["employee"]["isActive"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["name"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["oracleId"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["nid"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["employeeNumber"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["birthCertificate"]+"</td>";
            tbody += "<td >"+empArray[r]["company"]["name"]+"</td>";
            tbody += "<td >"+empArray[r]["department"]["name"]+"</td>";
            tbody += "<td >"+empArray[r]["designation"]["name"]+"</td>";
            tbody += "<td >"+empArray[r]["workLocation"]["name"]+"</td>";
            let dob = empArray[r]["employee"]["dateOfBirth"];
            dob = dob == null ? "" : dob.toString().split("T")[0];
            tbody += "<td >"+dob+"</td>";
            let doj = empArray[r]["employee"]["joiningDate"];
            doj = doj == null ? "" : doj.toString().split("T")[0];
            tbody += "<td >"+doj+"</td>";
            let mrgd = empArray[r]["employee"]["marriageDate"];
            mrgd = mrgd == null ? "" : mrgd.toString().split("T")[0];
            tbody += "<td >"+mrgd+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["officialPhone"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["personalPhone"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["emergencyContact"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["postalAddress"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["email"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["whatsappNumber"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["imoNumber"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["viberNumber"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["fbProfileLink"]+"</td>";
            tbody += "<td >"+empArray[r]["employee"]["createDate"]+"</td>";
            tbody += "<td >"+empArray[r]["createdByUser"]["userName"]+"</td>";
            rowNum++;
            tbody+="</tr>";
        }
        if (rowNum==1) tbody += "<tr><td colspan="+colspan+">NO DATA FOUND</td></tr>"
    }catch (e) {
        console.log(e);
        tbody += "<tr><td colspan="+colspan+">SOMETHING WENT WRONG</td></tr>"
    }
    document.getElementById("employee_table_body").innerHTML=tbody;
}

function onClickEmployeeTableRow(index) {
    console.log("Row Click Index: "+index);
    callAPI(baseURL+employeeRegistrationPageAPI,GET,null,regModalCallback);
    function regModalCallback(response) {
        document.getElementById("form_modal_content").innerHTML = response;
        modalAction();
        registrationPageInit(UPDATE_EMPLOYEE,empArray[index]);
    }
}

