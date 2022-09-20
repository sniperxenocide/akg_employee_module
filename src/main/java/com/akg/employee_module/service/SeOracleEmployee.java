package com.akg.employee_module.service;

import com.akg.employee_module.model.*;
import com.akg.employee_module.repository.*;
import com.akg.employee_module.request_response.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;


@Service
public class SeOracleEmployee {

    private final ReBusiness reBusiness;
    private final ReCompany reCompany;
    private final ReDepartment reDepartment;
    private final ReDesignation reDesignation;
    private final ReWorkLocation reWorkLocation;
    private final SeEmployee seEmployee;

    @Value("${akg.oracle.api.server}")
    private String akgOracleApiServer;

    public SeOracleEmployee(ReBusiness reBusiness, ReCompany reCompany, ReDepartment reDepartment, ReDesignation reDesignation, ReWorkLocation reWorkLocation, SeEmployee seEmployee) {
        this.reBusiness = reBusiness;
        this.reCompany = reCompany;
        this.reDepartment = reDepartment;
        this.reDesignation = reDesignation;
        this.reWorkLocation = reWorkLocation;
        this.seEmployee = seEmployee;
    }

    public Response createEmployeeFromOracle(HttpServletRequest request,Employee emp){
        try {
            if(emp.getOracleId()==null || emp.getOracleId().trim().length()==0)
                return new Response(false,"Invalid Oracle ID");
            Employee employee = getEmployee(emp.getOracleId(),emp);
            if(employee==null) return new Response(false,"No Employee Found for this Oracle ID");
            return seEmployee.create(request,employee);
            //System.out.println(employee);
            //return new Response(true,"Employee Created");
        }catch (Exception e){return new Response(false,e.getMessage());}
    }

    public Response getEmployeeFromOracle(String oracleId){
        Employee employee = getEmployee(oracleId,null);
        if(employee==null) return new Response(false,"No Employee Found for this Oracle ID",null);
        return new Response(true," Employee Found",employee);
    }

    private Employee getEmployee(String oracleId,Employee emp){
        JSONObject jsonObject = getData(oracleId);
        if(jsonObject == null) return null;

        Employee employee = new Employee();
        if(emp!=null) employee=emp;

        try{ employee.setName(jsonObject.getString("name")); }catch (Exception ignored){}
        try{ employee.setNid(jsonObject.getString("nid")); }catch (Exception ignored){}
        try{ employee.setOfficialPhone(jsonObject.getString("officialPhone")); }catch (Exception ignored){}
        try{ employee.setPersonalPhone(jsonObject.getString("personalPhone")); }catch (Exception ignored){}
        try{ employee.setOracleId(jsonObject.getString("oracleId")); }catch (Exception ignored){}
        try{ employee.setEmail(jsonObject.getString("emailAddress")); }catch (Exception ignored){}
        try{ employee.setEmergencyContact(jsonObject.getString("homePhone")); }catch (Exception ignored){}
        try { employee.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(jsonObject.getString("dateOfBirth")));
        }catch (Exception ignored){}
        try { employee.setJoiningDate(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(jsonObject.getString("joiningDate")));
        }catch (Exception ignored){}
        try {
            employee.setMarriageDate(jsonObject.getString("marriageDate").split("T")[0]);
        }catch (Exception ignored){}
        try {
            String postalAddress = jsonObject.getString("presentAddress") ;
            if(postalAddress==null || postalAddress.trim().length()==0)
                postalAddress = jsonObject.getString("permanentAddress") ;
            employee.setPostalAddress(postalAddress);
        }catch (Exception ignored){}

        //Setting Common Business
        Business commonBusiness = reBusiness.findByName("Abul Khair Group").orElse(null);
        if(commonBusiness == null){
            commonBusiness = new Business();
            commonBusiness.setName("Abul Khair Group");
            reBusiness.save(commonBusiness);
            System.out.println("Adding Common Business : Abul Khair Group");
        }

        Company commonCompany = reCompany.findByName("GROUP COMMON").orElse(null);
        if(commonCompany == null){
            commonCompany = new Company();
            commonCompany.setName("GROUP COMMON");
            commonCompany.setBusinessId(commonBusiness.getId());
            reCompany.save(commonCompany);
            System.out.println("Adding Common Company : GROUP COMMON");
        }

        Business business = null;
        try {
            String businessStr = jsonObject.get("business").toString();
            if(businessStr.toLowerCase().equals("null")) throw new Exception("Business is Null");
            business = reBusiness.findByName(businessStr).orElse(null);
            if(business==null){
                business = new Business();
                business.setName(businessStr);
                reBusiness.save(business);
                System.out.println("Adding Business: "+businessStr);
            }
            employee.setBusinessId(business.getId());
        }catch (Exception e){ System.out.println(e.getMessage()); }

        Company company = null;
        try {
            String companyStr = jsonObject.get("company").toString();
            if(companyStr.toLowerCase().equals("null")) company = commonCompany;
            else {
                company = reCompany.findByName(companyStr).orElse(null);
                if(company==null){
                    company = new Company();
                    company.setName(companyStr);
                    if (business == null) company.setBusinessId(commonBusiness.getId());
                    else company.setBusinessId(business.getId());
                    reCompany.save(company);
                    System.out.println("Adding Company: "+companyStr);
                }
            }
            employee.setCompanyId(company.getId());
        }catch (Exception e){System.out.println(e.getMessage()); }

        Department department = null;
        try {
            String departmentStr = jsonObject.get("department").toString();
            if(departmentStr.toLowerCase().equals("null")) throw new Exception("Department is Null");
            department = reDepartment.findByName(departmentStr).orElse(null);
            if(department==null){
                department = new Department();
                department.setName(departmentStr);
                reDepartment.save(department);
                System.out.println("Adding Department: "+departmentStr);
            }
            employee.setDepartmentId(department.getId());
        }catch (Exception e){ System.out.println(e.getMessage()); }

        Designation designation = null;
        try {
            String designationStr = jsonObject.get("designation").toString();
            if(designationStr.toLowerCase().equals("null")) throw new Exception("Designation is Null");
            designation = reDesignation.findByName(designationStr).orElse(null);
            if(designation==null){
                designation = new Designation();
                designation.setName(designationStr);
                reDesignation.save(designation);
                System.out.println("Adding Designation: "+designationStr);
            }
            employee.setDesignationId(designation.getId());
        }catch (Exception e){ System.out.println(e.getMessage()); }

        WorkLocation workLocation = null;
        try {
            String workLocationStr = jsonObject.get("workLocation").toString();
            if(workLocationStr.toLowerCase().equals("null")) throw new Exception("WorkLocation is Null");
            workLocation = reWorkLocation.findByName(workLocationStr).orElse(null);
            if(workLocation==null){
                workLocation = new WorkLocation();
                workLocation.setName(workLocationStr);
                reWorkLocation.save(workLocation);
                System.out.println("Adding WorkLocation: "+workLocationStr);
            }
            employee.setWorkLocationId(workLocation.getId());
        }catch (Exception e){ System.out.println(e.getMessage()); }

        return employee;
    }

    private JSONObject getData(String oracleId){
        try {
            URL urlForGetRequest = new URL(akgOracleApiServer+"/api/v1/employee/"+oracleId);
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            String auth = "city:city123";
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Basic "+new String(encodedAuth));
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = in .readLine()) != null) {
                    response.append(readLine);
                } in .close();
                // print result
                System.out.println("JSON String Result " + response.toString());
                JSONObject jsonObject = new JSONObject(response.toString());
                if(jsonObject.getBoolean("status")) {
                    return jsonObject.getJSONObject("data");
                }
            } else {
                System.out.println("GET NOT WORKED");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
