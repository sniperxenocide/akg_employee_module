package com.akg.employee_module.service;

import com.akg.employee_module.enums.Decision;
import com.akg.employee_module.enums.Status;
import com.akg.employee_module.model.*;
import com.akg.employee_module.repository.*;
import com.akg.employee_module.request_response.BroadcastRequest;
import com.akg.employee_module.request_response.BroadcastStatus;
import com.akg.employee_module.request_response.EmployeeProfile;
import com.akg.employee_module.request_response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Service
public class SeBroadcast {

    private final CommonMethodService commonMethodService;
    private final ReBroadcastMedia reBroadcastMedia;
    private final ReEmployee reEmployee;
    private final ReBroadcastHeader reBroadcastHeader;
    private final ReBroadcastDetail reBroadcastDetail;
    private final ReVonageMessagesLog reVonageMessagesLog;
    private final SeEmployee seEmployee;

    public SeBroadcast(CommonMethodService commonMethodService, ReBroadcastMedia reBroadcastMedia, ReEmployee reEmployee, ReBroadcastHeader reBroadcastHeader, ReBroadcastDetail reBroadcastDetail, ReVonageMessagesLog reVonageMessagesLog, SeEmployee seEmployee) {
        this.commonMethodService = commonMethodService;
        this.reBroadcastMedia = reBroadcastMedia;
        this.reEmployee = reEmployee;
        this.reBroadcastHeader = reBroadcastHeader;
        this.reBroadcastDetail = reBroadcastDetail;
        this.reVonageMessagesLog = reVonageMessagesLog;
        this.seEmployee = seEmployee;
    }

    public Response createBroadcast(BroadcastRequest broadcastRequest, HttpServletRequest request){
        System.out.println(broadcastRequest);
        User user = commonMethodService.getUser(request);
        if(user==null)
            return new Response(false,"Unauthorized");

        BroadcastHeader broadcastHeader = new BroadcastHeader(broadcastRequest.getMessageBody(),user.getId());
        try {
            broadcastHeader = reBroadcastHeader.save(broadcastHeader);
        }catch (Exception e){
            e.printStackTrace();
            return new Response(false,"Failed to Create Broadcast Request."+e.getMessage());
        }

        int broadcastDetailCount = 0;

        for(Long bmId:broadcastRequest.getBroadcastMedias()){
            BroadcastMedia bm = reBroadcastMedia.findById(bmId).orElse(null);
            if(bm==null) continue;
            for (Long empId:broadcastRequest.getEmployeeIds()){
                Employee employee = reEmployee.findById(empId).orElse(null);
                EmployeeProfile empProfile = seEmployee.getEmployeeProfile(employee);
                if (employee==null) continue;
                try {
                    String recipientContact = getRecipientContact(bm.getRecipientContactColumn(),employee);
                    if(recipientContact.trim().length()==0) continue;
                    // Checking for comma(,) separated phone number
                    for(String rc:recipientContact.split(",")){
                        BroadcastDetail bd = new BroadcastDetail(broadcastHeader.getId(),
                                bm.getId(),bm.getName(),bm.getCode(),
                                broadcastRequest.getMessageBody(), rc.trim(),
                                empId,employee.getCompanyId(),employee.getDepartmentId(),
                                employee.getDesignationId(),employee.getWorkLocationId(),
                                employee.getName(),empProfile.getCompany().getName(),
                                empProfile.getDepartment().getName(),empProfile.getDesignation().getName(),
                                empProfile.getWorkLocation().getName());
                        bd = reBroadcastDetail.save(bd);
                        broadcastDetailCount++;
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        }
        if(broadcastDetailCount==0){
            try {
                reBroadcastHeader.delete(broadcastHeader);
            }catch (Exception e){e.printStackTrace();}
            return new Response(false,"Failed to Create Broadcast Request");
        }
        initiateBroadcastThread(broadcastHeader);
        return new Response(true,"Broadcast Request Created Successfully for "+broadcastDetailCount+" Messages");
    }


    private String toCamelCase(String snakeCase){
        StringBuilder sb = new StringBuilder();
        String[] list = snakeCase.split("_");
        sb.append(list[0]);
        for(int i=1;i<list.length;i++){
            sb.append(list[i].toUpperCase().charAt(0)).append(list[i].substring(1));
        }
        return sb.toString();
    }

    private String getRecipientContact(String columnName,Employee employee){
        for(String col:columnName.split(",")){
            String camelCase = toCamelCase(col);
            try {
                Field field = Employee.class.getDeclaredField(camelCase);
                field.setAccessible(true);
                String contact = field.get(employee).toString();
                if(contact.length()>0) return contact.trim();
            }catch (Exception e){e.printStackTrace();}
        }
        return "";
    }


    private void initiateBroadcastThread(BroadcastHeader bh){
        new Thread(()->{
            System.out.println("Initiating Broadcast Thread");
            ArrayList<Long> companyIds = reBroadcastDetail.getDistinctCompanyId(bh.getId());
            ArrayList<Long> mediaIds = reBroadcastDetail.getDistinctMediaId(bh.getId());
            for (Long comId:companyIds){
                for(Long medId:mediaIds){
                    new Thread(()->{
                        int totalBd = reBroadcastDetail.countAllByBroadcastHeaderIdAndCompanyIdAndMediaId
                                (bh.getId(),comId,medId);
                        BroadcastMedia bm = reBroadcastMedia.findById(medId).orElse(null);
                        for(int i=0;i<totalBd;i++){
                            if(bm==null) break;
                            Pageable pageable = PageRequest.of(i, 1);
                            ArrayList<BroadcastDetail> bds = reBroadcastDetail.
                                    findAllByBroadcastHeaderIdAndCompanyIdAndMediaIdOrderByIdAsc
                                            (bh.getId(),comId,medId,pageable);
                            for(BroadcastDetail bd:bds) {
                                System.out.println(bd);
                                sendSingleBroadcastDetail(bm,bd);
                            }
                        }
                    }).start();
                }
            }
        }).start();
    }

    private void sendSingleBroadcastDetail(BroadcastMedia bm,BroadcastDetail bd){
        if (bd.getStatus()== Status.QUEUE && bm.getBroadcastEnabled()== Decision.YES){
            if(bm.getCode().equals("sms")) sendMobileSms(bm,bd);
            else if(bm.getCode().equals("whatsapp")) sendWhatsappMessage(bm, bd);
            // more conditions
        }
    }
//9

    private void sendMobileSms(BroadcastMedia bm,BroadcastDetail bd){
        StringBuilder sb = new StringBuilder();
        sb.append(bm.getApiBaseUrl()).append("?").append("userID=").append(bm.getApiUserId());
        sb.append("&").append("passwd=").append(bm.getApiPassword());
        sb.append("&").append("sender=").append(bm.getApiSenderId());
        sb.append("&").append("msisdn=").append(bd.getRecipientContact());
        String msg = "";
        try {
            msg = URLEncoder.encode(bd.getMessageBody(), "UTF-8");
        }catch (Exception e){ System.out.println("Encoding Problem"); }
        sb.append("&").append("message=").append(msg);
        String response = callAPI(sb.toString(),bm.getApiRequestMethod(),null,null,null);

        Status status;
        if(response.equals("Success Count : 1 and Fail Count : 0")) status = Status.SENT;
        else status = Status.FAILED;

        if (bd.getStatus()== Status.QUEUE)
            updateBroadcastDetail(bd,response,status);
    }

    private void sendWhatsappMessage(BroadcastMedia bm,BroadcastDetail bd){
        if(bd.getRecipientContact().length()<=11 && bd.getRecipientContact().startsWith("01")) {
            try {
                bd.setRecipientContact("88"+bd.getRecipientContact());
                reBroadcastDetail.save(bd);
            }catch (Exception e){e.printStackTrace();}
        }
        JSONObject header = new JSONObject()
                .put("Authorization","Bearer "+bm.getApiTokenKey());
        JSONObject body = new JSONObject().put("to",bd.getRecipientContact())
                .put("from",bm.getApiSenderId()).put("channel","whatsapp")
                .put("message_type","template")
                .put("whatsapp",new JSONObject()
                        .put("policy","deterministic").put("locale","en-US"))
                .put("template",new JSONObject()
                        .put("name",bm.getApiUserId())
                        .put("parameters",new JSONArray().put(bd.getMessageBody())));

        String response = callAPI(bm.getApiBaseUrl(),bm.getApiRequestMethod(),
                null,header,body);

        if (bd.getStatus()== Status.QUEUE)
            updateBroadcastDetail(bd,response,Status.QUEUE);
    }

    private void updateBroadcastDetail(BroadcastDetail bd,String response,Status status){
        bd.setApiResponse(response);
        bd.setApiResponseTime(LocalDateTime.now());
        bd.setStatus(status);
        reBroadcastDetail.save(bd);
    }

    private String callAPI(String baseUrl, String method, JSONObject param, JSONObject header, JSONObject body){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(baseUrl);
            if(param!=null){
                sb.append("?");
                for (Iterator<String> it = param.keys(); it.hasNext(); ) {
                    String key = it.next();
                    sb.append(key).append("=").append(URLEncoder.encode(param.getString(key), "UTF-8")).append("&");
                }
                sb.deleteCharAt(sb.length()-1);
            }
            URL url = new URL(sb.toString());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.1.11", 3128));
            System.out.println(url);
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            if(header!=null){  // Setting Header
                for (Iterator<String> it = header.keys(); it.hasNext(); ) {
                    String key = it.next();
                    connection.setRequestProperty(key,header.getString(key));
                }
            }
            if(body!=null){
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
                wr.write(body.toString());
                wr.flush();
                wr.close();
                connection.connect();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = in .readLine()) != null) {
                    response.append(readLine);
                } in .close();
                System.out.println("JSON String Result " + response.toString());
                return response.toString();
            } else {
                System.out.println("GET NOT WORKED");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    public Response getBroadcastStatus(){
        ArrayList<BroadcastHeader> headers = (ArrayList<BroadcastHeader>)reBroadcastHeader.findAll(Sort.by("id").descending());
        ArrayList<BroadcastStatus> broadcastStatusList = new ArrayList<>();
        for (BroadcastHeader header:headers){
            BroadcastStatus broadcastStatus = new BroadcastStatus();
            broadcastStatus.setBroadcastHeader(header);
            ArrayList<BroadcastStatus.Media> medias = new ArrayList<>();
            for(Long medId:reBroadcastDetail.getDistinctMediaId(header.getId())){
                BroadcastMedia bm = reBroadcastMedia.findById(medId).orElse(null);
                if(bm==null) continue;
                BroadcastStatus.Media media = broadcastStatus.new Media();
                media.setName(bm.getName());
                ArrayList<BroadcastStatus.StatusCount> statusCounts = new ArrayList<>();
                for(Status s:Status.values()){
                    BroadcastStatus.StatusCount sc = broadcastStatus.new StatusCount();
                    sc.setStatus(s);
                    sc.setCount(reBroadcastDetail.countAllByBroadcastHeaderIdAndMediaIdAndStatus(header.getId(),medId,s));
                    statusCounts.add(sc);
                }
                media.setStatusCounts(statusCounts);
                medias.add(media);
            }
            broadcastStatus.setBroadcastMedias(medias);
            User user = commonMethodService.getUser(header.getCreatedBy());
            if(user!=null) broadcastStatus.setCreatedByUser(user.getUserName());
            broadcastStatusList.add(broadcastStatus);
        }
        return new Response(true,"Success",broadcastStatusList);
    }

    //submitted, delivered, rejected, undeliverable or read
    public void vonageMessagesStatusLog(String body){
        try {
            //System.out.println(body);
            JSONObject jsonBody = new JSONObject(body);
            VonageMessagesLog log = new VonageMessagesLog(jsonBody.toString(), new Date());
            reVonageMessagesLog.save(log);
            String uuid = jsonBody.getString("message_uuid");
            ArrayList<BroadcastDetail> bds = reBroadcastDetail
                    .getBroadcastDetailByTypeAndMsgId("whatsapp",uuid);
            //System.out.println(bds);
            if(bds.size()==0) return;
            BroadcastDetail bd = bds.get(0);
            if(!bd.getStatus().equals(Status.QUEUE))
                return;
            bd.setApiResponse(jsonBody.toString());
            String status = jsonBody.getString("status");
            if(status.equals("rejected") || status.equals("undeliverable"))
                bd.setStatus(Status.FAILED);
            else if(status.equals("delivered") || status.equals("read"))
                bd.setStatus(Status.SENT);
            reBroadcastDetail.save(bd);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
