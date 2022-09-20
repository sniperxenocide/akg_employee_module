package com.akg.employee_module.repository;

import com.akg.employee_module.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReEmployee extends JpaRepository<Employee,Long> {

    @Query(
            value = " select * from employee  where business_id like ?1  " +
                    " and company_id like ?2  and department_id like ?3  " +
                    " and designation_id like ?4   and work_location_id like ?5  " +
                    " and is_verified like ?6 and is_active like ?7 "+
                    " LIMIT ?9 offset ?8 ",
            nativeQuery = true
    )
    ArrayList<Employee> findAllFiltered
            (String businessId, String companyId, String departmentId,
             String designationId, String workLocationId,String isVerified,
             String isActive,int offset,int limit);

    @Query(
            value = " select count(*) from employee  where business_id like ?1  " +
                    " and company_id like ?2  and department_id like ?3  " +
                    " and designation_id like ?4   and work_location_id like ?5  " +
                    " and is_verified like ?6 and is_active like ?7 ",
            nativeQuery = true
    )
    long countAllFiltered
            (String businessId, String companyId, String departmentId,
             String designationId, String workLocationId,String isVerified,
             String isActive);


    @Query(
            value = "select * from employee where 1=1 "+
                    " and (case when ?1 then company_id in ?2 else true end) "+
                    " and (case when ?3 then department_id in ?4 else true end) "+
                    " and (case when ?5 then designation_id in ?6 else true end) "+
                    " and (case when ?7 then work_location_id in ?8 else true end) ",
            nativeQuery = true
    )
    ArrayList<Employee> findAllQueried(boolean flag1,List<Long> companies,
                                       boolean flag2,List<Long> departments,
                                       boolean flag3,List<Long> designations,
                                       boolean flag4,List<Long> workLocations);

    ArrayList<Employee> findByNid(String nid);
    ArrayList<Employee> findByOracleId(String oracleId);
    ArrayList<Employee> findByEmployeeNumber(String employeeNumber);
    ArrayList<Employee> findByBirthCertificate(String birthCertificate);
    ArrayList<Employee> findAllByNameLike(String name);
}
