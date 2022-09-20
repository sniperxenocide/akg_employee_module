let broadcastStatusList = [];

function broadcastStatusInit() {
    broadcastStatusList = [];
    callAPI(baseURL+getAllBroadcastStatus,GET,null,callback);
    function callback(response) {
        try {
            let jsonResponse = JSON.parse(response);
            console.log(jsonResponse);
            if(jsonResponse['status']){
                broadcastStatusList = jsonResponse['data'];
                populateStatusTable();
            }
            else alert(jsonResponse['msg']);
        }catch (e) {console.log(e)}
    }
}

function populateStatusTable() {
    let colNames = ['SL','Message','Created By','Creation Time','Media'];
    try {
        let statusCountList = broadcastStatusList[0]['broadcastMedias'][0]['statusCounts'];
        for(let i in statusCountList){
            let statusName = statusCountList[i]['status'];
            colNames.push(statusName);
        }
    }catch (e) {console.log(e)}
    let thead = "";
    thead+="<thead><tr>";
    for (let i in colNames) thead+= "<th>"+colNames[i]+"</th>";
    thead+="</thead></tr>";

    let tbody = "";
    for(let i in broadcastStatusList){
        let thisStatus = broadcastStatusList[i];
        let rowSpan = thisStatus['broadcastMedias'].length;

        tbody+="<tr>";
        tbody+="<td rowspan='"+rowSpan+"'>"+(parseInt(i)+1)+"</td>";
        tbody+="<td rowspan='"+rowSpan+"'>"+thisStatus['broadcastHeader']['messageBody']+"</td>";
        tbody+="<td rowspan='"+rowSpan+"'>"+thisStatus['createdByUser']+"</td>";
        let time = thisStatus['broadcastHeader']['createTime'];
        tbody+="<td rowspan='"+rowSpan+"'>"+time.split('T')[0]+" "+time.split('T')[1]+"</td>";


        tbody+="<td>"+thisStatus['broadcastMedias'][0]['name']+"</td>";
        for(let k in thisStatus['broadcastMedias'][0]['statusCounts']){
            tbody+="<td>"+thisStatus['broadcastMedias'][0]['statusCounts'][k]['count']+"</td>";
        }

        tbody+="</tr>";

        for(let j in thisStatus['broadcastMedias']){
            if(j>0){
                tbody+="<tr>";
                tbody+="<td>"+thisStatus['broadcastMedias'][j]['name']+"</td>";
                for(let k in thisStatus['broadcastMedias'][j]['statusCounts'])
                    tbody+="<td>"+thisStatus['broadcastMedias'][j]['statusCounts'][k]['count']+"</td>";
                tbody+="</tr>";
            }
        }
    }

    document.getElementById('broadcast_status_table').innerHTML = thead+tbody;
}