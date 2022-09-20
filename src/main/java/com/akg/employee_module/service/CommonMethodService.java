package com.akg.employee_module.service;

import com.akg.employee_module.config.JwtTokenUtil;
import com.akg.employee_module.model.*;
import com.akg.employee_module.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CommonMethodService {

    public static class Hierarchy{
        public String division;
        public String region;
        public String territory;
        public int territoryId;
        public Hierarchy() { }
    }

    private final UserRoleRepository userRoleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CommonMethodService(JwtTokenUtil jwtTokenUtil, UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository)
    {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    public String getTokenFromCookie(HttpServletRequest request){
        if(request.getCookies()!=null){
            for (Cookie c:request.getCookies()) {
                if (c.getName().equals("Authorization")){
                    return "Bearer "+c.getValue();
                }
            }
        }
        return null;
    }

    public String getTokenFromHeader(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    public String getToken(HttpServletRequest request){
        String tokenHeader = getTokenFromHeader(request);
        if(tokenHeader==null) tokenHeader = getTokenFromCookie(request);

        if(tokenHeader==null) return null;
        else if(tokenHeader.startsWith("Bearer")) return tokenHeader.substring(7);
        return null;
    }

    public User getUser(HttpServletRequest request){
        try {
            String token = getToken(request);
            String userName = jwtTokenUtil.getUsernameFromToken(token);
            return userRepository.findByUserName(userName);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User getUser(String userName){
        try {
            return userRepository.findByUserName(userName);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }



    public boolean isValidAdmin(HttpServletRequest request){
        try {
            String token = getToken(request);
            if (token==null) return false;
            String userName = jwtTokenUtil.getUsernameFromToken(token);
            User admin = userRepository.findByUserName(userName);
            UserRole userRole = userRoleRepository.findByUserId(admin.id.intValue()).orElse(null);
            if(userRole!=null){
                if(userRole.roleId==1) return true;  // Checking if Role is Admin
            }
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    public boolean isValidAdmin(int userId){
        try {
            User admin = userRepository.findById((long)userId).orElse(null);
            if (admin==null) return false;
            UserRole userRole = userRoleRepository.findByUserId(admin.id.intValue()).orElse(null);
            if(userRole!=null){
                if(userRole.roleId==1) return true;  // Checking if Role is Admin
            }
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    public Role getUserRole(int userId){
        UserRole userRole = userRoleRepository.findByUserId(userId).orElse(null);
        if(userRole != null){
            return roleRepository.findById((long)userRole.roleId).orElse(null);
        }
        return roleRepository.findById((long)2).orElse(null);
    }
}
