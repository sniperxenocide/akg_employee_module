package com.akg.employee_module.service;
import com.akg.employee_module.model.Company;
import com.akg.employee_module.model.Employee;
import com.akg.employee_module.model.User;
import com.akg.employee_module.repository.*;
import com.akg.employee_module.request_response.EmployeeProfile;
import com.akg.employee_module.request_response.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class SeEmployee {
    private final ReEmployee repository;
    private final ReBusiness reBusiness;
    private final ReCompany reCompany;
    private final ReDepartment reDepartment;
    private final ReDesignation reDesignation;
    private final ReWorkLocation reWorkLocation;
    private final UserRepository userRepository;
    private final CommonMethodService commonMethodService;
    private final SeEventLog seEventLog;
    private final int pageSize = 20;

    public SeEmployee(ReEmployee repository, ReBusiness reBusiness, ReCompany reCompany, ReDepartment reDepartment, ReDesignation reDesignation, ReWorkLocation reWorkLocation, UserRepository userRepository, CommonMethodService commonMethodService, SeEventLog seEventLog) {
        this.repository = repository;
        this.reBusiness = reBusiness;
        this.reCompany = reCompany;
        this.reDepartment = reDepartment;
        this.reDesignation = reDesignation;
        this.reWorkLocation = reWorkLocation;
        this.userRepository = userRepository;
        this.commonMethodService = commonMethodService;
        this.seEventLog = seEventLog;
    }

    public int getLastPageNumber(long cnt){
        try {
            return (int) (cnt%pageSize==0?cnt/pageSize:cnt/pageSize+1);
        }catch (Exception ignored){return 1;}
    }

    public Response getAllFiltered(String businessId, String companyId, String departmentId,
               String designationId, String workLocationId,String isVerified,String isActive,int page){
        ArrayList<EmployeeProfile> profileList = new ArrayList<>();
        long filteredCount = repository.countAllFiltered(businessId,companyId,departmentId,designationId,workLocationId,isVerified,isActive);
        int lastPage = getLastPageNumber(filteredCount);
        int pageIndex = page <= 1 ? 0 : Math.min(page,lastPage)-1 ;
        for(Employee e:repository.findAllFiltered
                (businessId,companyId,departmentId,designationId,workLocationId,isVerified,isActive,pageIndex*pageSize,pageSize))
        {
            profileList.add(getEmployeeProfile(e));
        }
        JSONObject js = new JSONObject();
        js.put("pageSize",pageSize);js.put("currentPage",pageIndex+1);
        js.put("lastPage",lastPage);js.put("empCount",filteredCount);
        return new Response(true, js.toString(), profileList);
    }

    public Response getAllQueried(Map<String, List<Long>> body){
        System.out.println(body);
        try {
            ArrayList<Employee> employees = repository.findAllQueried(!body.get("company").isEmpty(),body.get("company"),
                    !body.get("department").isEmpty(),body.get("department"),
                    !body.get("designation").isEmpty(),body.get("designation"),
                    !body.get("work_location").isEmpty(),body.get("work_location"));
            ArrayList<Map<String,Object>> profileList = new ArrayList<>();
            for(Employee e:employees) {
                EmployeeProfile profile = getEmployeeProfile(e);
                Map<String,Object> map = new HashMap<>();
                map.put("id",profile.getEmployee().getId());
                map.put("name",profile.getEmployee().getName());
                map.put("business",profile.getBusiness().getName());
                map.put("company",profile.getCompany().getName());
                map.put("department",profile.getDepartment().getName());
                map.put("designation",profile.getDesignation().getName());
                map.put("work_location",profile.getWorkLocation().getName());
                profileList.add(map);
            }
            return new Response(true,"Success",profileList);
        }catch (Exception e){e.printStackTrace();}
        return new Response(false,"Invalid Body. Suggested: " +
                "{'company':[],'department':[],'designation':[],'work_location':[]}");
    }


    public Response getOne(Long id){
        Optional<Employee> optional = repository.findById(id);
        if(optional.isPresent()){
            Employee employee = optional.get();
            return new Response(true,"Success",getEmployeeProfile(employee));
        }
        return new Response(false, "Employee Not Found", null);
    }

    public Response getByNid(String nid){
        ArrayList<Employee> employees = repository.findByNid(nid);
        ArrayList<EmployeeProfile> profiles = new ArrayList<>();
        for (Employee e:employees){
            profiles.add(getEmployeeProfile(e));
        }
        return new Response(true, "Success", profiles);
    }

    public Response getByOracleId(String oracleId){
        ArrayList<Employee> employees = repository.findByOracleId(oracleId);
        ArrayList<EmployeeProfile> profiles = new ArrayList<>();
        for (Employee e:employees){
            profiles.add(getEmployeeProfile(e));
        }
        return new Response(true, "Success", profiles);
    }

    public Response getByEmployeeNumber(String employeeNumber){
        ArrayList<Employee> employees = repository.findByEmployeeNumber(employeeNumber);
        ArrayList<EmployeeProfile> profiles = new ArrayList<>();
        for (Employee e:employees){
            profiles.add(getEmployeeProfile(e));
        }
        return new Response(true, "Success", profiles);
    }

    public Response getByBirthCertificate(String birthCertificate){
        ArrayList<Employee> employees = repository.findByBirthCertificate(birthCertificate);
        ArrayList<EmployeeProfile> profiles = new ArrayList<>();
        for (Employee e:employees){
            profiles.add(getEmployeeProfile(e));
        }
        return new Response(true, "Success", profiles);
    }

    public Response getAllByName(String name){
        ArrayList<EmployeeProfile> profileList = new ArrayList<>();
        for(Employee e:repository.findAllByNameLike("%"+name+"%"))
        {
            profileList.add(getEmployeeProfile(e));
        }
        return new Response(true,"Success", profileList);
    }

    public Response create(HttpServletRequest request,Employee employee){
        try {
            User user = commonMethodService.getUser(request);
            employee.setCreatedBy(user.getId());
            if(employee.getBusinessId()==null){
                Optional<Company> company = reCompany.findById(employee.getCompanyId());
                company.ifPresent(value -> employee.setBusinessId(value.getBusinessId()));
            }

            Response duplicate = duplicateIdentityCheck(employee,null);
            if(!duplicate.getStatus()) return duplicate;

            repository.save(employee);

            seEventLog.createEventLog(request,"Create Employee",null,
                    "id="+employee.getId()+",oracleId="+employee.getOracleId()+",nid="+employee.getNid(),
            null);

            return new Response(true,"Employee Created Successfully");
        }catch (Exception e){ e.printStackTrace();}
        return new Response(false,"Failed to Create Employee");
    }

    public Response update(Long id,Employee employee,HttpServletRequest request){
        Optional<Employee> optional = repository.findById(id);
        if(optional.isPresent()){
            Employee currentEmployee = optional.get();
            if(employee.getBusinessId()==null){
                Optional<Company> company = reCompany.findById(employee.getCompanyId());
                company.ifPresent(value -> employee.setBusinessId(value.getBusinessId()));
            }

            Response duplicate = duplicateIdentityCheck(employee,currentEmployee);
            if(!duplicate.getStatus()) return duplicate;

            logUpdateEmployeeEvent(currentEmployee,employee,request);
            currentEmployee.updateEmployee(employee);
            repository.save(currentEmployee);
            return new Response(true,"Employee Updated Successfully");
        }
        return new Response(false,"Failed to Update Employee",null);
    }



    public Response delete(Long id,HttpServletRequest request){
        Optional<Employee> optional = repository.findById(id);
        if (optional.isPresent()){
            Employee employee = optional.get();
            //Logging Delete Event
            seEventLog.createEventLog(request,"Delete Employee",
                    "id="+employee.getId()+",oracleId="+employee.getOracleId()+",nid="+employee.getNid()
                    +",officialPhone="+employee.getOfficialPhone()+",personalPhone="+employee.getPersonalPhone(),
                    null,null);
            repository.delete(employee);
            return new Response(true,"Employee Deleted Successfully");
        }
        return new Response(false,"Employee Not Present");
    }

    private Response duplicateIdentityCheck(Employee employee,Employee currentEmployee){
        if(employee.getNid()!=null){
            if(employee.getNid().trim().length()>0){
                if(repository.findByNid(employee.getNid()).size()>0) {
                    if (currentEmployee==null) // Create
                        return new Response(false,"Employee Already Exists with The Same NID");
                    else if(!currentEmployee.getNid().equals(employee.getNid()))  //Update
                        return new Response(false,"Employee Already Exists with The Same NID");
                }
            }
        }

        if(employee.getOracleId()!=null){
            if(employee.getOracleId().trim().length()>0){
                if(repository.findByOracleId(employee.getOracleId()).size()>0) {
                    if (currentEmployee==null) // Create
                        return new Response(false,"Employee Already Exists with The Same Oracle Id");
                    else if(!currentEmployee.getOracleId().equals(employee.getOracleId()))  //Update
                        return new Response(false,"Employee Already Exists with The Same Oracle Id");
                }
            }
        }

        if(employee.getEmployeeNumber()!=null){
            if(employee.getEmployeeNumber().trim().length()>0){
                if(repository.findByEmployeeNumber(employee.getEmployeeNumber()).size()>0) {
                    if (currentEmployee==null) // Create
                        return new Response(false,"Employee Already Exists with The Same Employee Number");
                    else if(!currentEmployee.getEmployeeNumber().equals(employee.getEmployeeNumber()))  //Update
                        return new Response(false,"Employee Already Exists with The Same Employee Number");
                }
            }
        }

        if(employee.getBirthCertificate()!=null){
            if(employee.getBirthCertificate().trim().length()>0){
                if(repository.findByBirthCertificate(employee.getBirthCertificate()).size()>0) {
                    if (currentEmployee==null) // Create
                        return new Response(false,"Employee Already Exists with The Same Birth Certificate");
                    else if(!currentEmployee.getBirthCertificate().equals(employee.getBirthCertificate()))  //Update
                        return new Response(false,"Employee Already Exists with The Same Birth Certificate");
                }
            }
        }
        return new Response(true,"");
    }


    public EmployeeProfile getEmployeeProfile(Employee employee){
        EmployeeProfile employeeProfile = new EmployeeProfile(employee);
        if(employee.getBusinessId()!=null)
            employeeProfile.setBusiness(reBusiness.findById(employee.getBusinessId()).orElse(null));
        if(employee.getCompanyId()!=null)
            employeeProfile.setCompany(reCompany.findById(employee.getCompanyId()).orElse(null));
        if(employee.getDepartmentId()!=null)
            employeeProfile.setDepartment(reDepartment.findById(employee.getDepartmentId()).orElse(null));
        if(employee.getDesignationId()!=null)
            employeeProfile.setDesignation(reDesignation.findById(employee.getDesignationId()).orElse(null));
        if(employee.getWorkLocationId()!=null)
            employeeProfile.setWorkLocation(reWorkLocation.findById(employee.getWorkLocationId()).orElse(null));
        if(employee.getCreatedBy()!=null)
            employeeProfile.setCreatedByUser(userRepository.findById(employee.getCreatedBy()).orElse(null));
        return employeeProfile;
    }

    private void logUpdateEmployeeEvent(Employee prvEmp,Employee newEmp,HttpServletRequest request){
        try {
            StringBuilder before = new StringBuilder();
            StringBuilder after = new StringBuilder();
            for (Field f:Employee.class.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    String fieldName = f.getName();
                    if(fieldName.equals("createDate") || fieldName.equals("createdBy") || fieldName.equals("id"))
                        continue;

                    if(fieldName.equals("dateOfBirth") || fieldName.equals("joiningDate")){
                        String p = f.get(prvEmp).toString().split("T")[0];
                        String n = f.get(newEmp).toString().split("T")[0];
                        if (!p.equals(n)){
                            before.append(fieldName).append("=").append(p).append(",");
                            after.append(fieldName).append("=").append(n).append(",");
                        }
                        continue;
                    }

                    if( (f.get(prvEmp)!= null && f.get(newEmp)==null) ||
                            (f.get(prvEmp)== null && f.get(newEmp)!=null)){
                        before.append(fieldName).append("=").append(f.get(prvEmp)).append(",");
                        after.append(fieldName).append("=").append(f.get(newEmp)).append(",");
                    }
                    else if(f.get(prvEmp)!= null && f.get(newEmp)!=null){
                        if(!f.get(prvEmp).equals(f.get(newEmp))){
                            before.append(fieldName).append("=").append(f.get(prvEmp)).append(",");
                            after.append(fieldName).append("=").append(f.get(newEmp)).append(",");
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }
            seEventLog.createEventLog(request,"Update Employee",before.toString(),
                    after.toString(),null);
        }catch (Exception e){
            System.out.println("Failed to log event");
        }
    }
}
