package com.akg.employee_module;

public class API {
    public static final String authenticate = "/authenticate";
    public static final String check = "/api/check";

    public static final String getAllMasterData = "/api/v1/master/all";

    public static final String getAllBusinessAPI = "/api/v1/business/all";
    public static final String getOneBusinessAPI = "/api/v1/business/{id}";

    public static final String getAllCompanyAPI = "/api/v1/company/all";
    public static final String getOneCompanyAPI = "/api/v1/company/{id}";

    public static final String getAllDepartmentAPI = "/api/v1/department/all";
    public static final String getOneDepartmentAPI = "/api/v1/department/{id}";

    public static final String getAllDesignationAPI = "/api/v1/designation/all";
    public static final String getOneDesignationAPI = "/api/v1/designation/{id}";

    public static final String getAllWorkLocationAPI = "/api/v1/work_location/all";
    public static final String getOneWorkLocationAPI = "/api/v1/work_location/{id}";

    public static final String getAllEmployeeAPI = "/api/v1/employee/all";
    public static final String getAllQueriedEmployeeAPI = "/api/v1/employee/query/all";
    public static final String getAllEmployeeByNameAPI = "/api/v1/employee/all/name/{name}";
    public static final String getOneEmployeeAPI = "/api/v1/employee/{id}";
    public static final String getOneOracleEmployeeAPI = "/api/v1/oracle/load/employee/{oracleId}";
    public static final String getOneEmployeeByNidAPI = "/api/v1/employee/nid/{nid}";
    public static final String getOneEmployeeByOracleIdAPI = "/api/v1/employee/oracle/{oracleId}";
    public static final String getOneEmployeeByEmployeeNumberAPI = "/api/v1/employee/empnum/{empNum}";
    public static final String getOneEmployeeByBirthCertificateAPI = "/api/v1/employee/bircert/{certNum}";

    public static final String createEmployeeAPI = "/api/v1/employee/create";
    public static final String updateEmployeeAPI = "/api/v1/employee/update/{id}";
    public static final String deleteEmployeeAPI = "/api/v1/employee/delete/{id}";
    public static final String createEmployeeFromOracleAPI = "/api/v1/employee/create/oracle";

    public static final String createBroadcastRequestAPI = "/api/v1/broadcast/create";
    public static final String getAllBroadcastStatusAPI = "/api/v1/broadcast/status/all";

    public static final String vonageMessagesStatusAPI = "/api/v1/vonage/messages/status";

    //Web Pages
    public static final String loginPageAPI = "/login";
    public static final String homePageAPI = "/home";
    public static final String allEmployeePageAPI = "/employee";
    public static final String registrationPageAPI = "/registration";
    public static final String broadcastPageAPI = "/broadcast";
    public static final String broadcastStatusPageAPI = "/broadcast_status";
}
