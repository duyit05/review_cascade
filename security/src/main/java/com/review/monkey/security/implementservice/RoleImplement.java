package com.review.monkey.security.implementservice;

import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.mapper.RoleMapper;
import com.review.monkey.security.model.Role;
import com.review.monkey.security.repository.RoleRepository;
import com.review.monkey.security.request.RoleRequest;
import com.review.monkey.security.response.RoleResponse;
import com.review.monkey.security.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleImplement implements RoleService {
    RoleRepository roleRepository;

    RoleMapper roleMapper;
    @Override
    public List<RoleResponse> getAllRole() {
        List<RoleResponse> roleResponseList = new ArrayList<>();
        List<Role> roleList = roleRepository.findAll();
        for (Role role : roleList) {
            RoleResponse roleResponse = new RoleResponse();
            roleResponse.setRoleId(role.getRoleId());
            roleResponse.setRoleName(role.getRoleName());
            roleResponse.setDescription(role.getDescription());
            roleResponseList.add(roleResponse);
        }
        return roleResponseList;
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {

        if(roleRepository.existsByRoleName(request.getRoleName()))
            throw new AppException(ErrorCode.ROLE_NAME_EXISTED);

        Role role = roleMapper.toRole(request);
        Role saveRole = roleRepository.save(role);
        System.out.println("Save role id : " + role.getRoleId());

        return roleMapper.toRoleResponse(saveRole);
    }

    @Override
    public RoleResponse updateRoleById(String roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("CAN'T UPDATE ROLE WITH ID : " + roleId));
        roleMapper.updateRole(role , request);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public void deleteRoleById(String roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("CAN'T DELETE ROLE WITH ID : " + roleId));
        roleRepository.delete(role);
    }

    @Override
    public RoleResponse getRoleById(String roleId) {

        return roleMapper.toRoleResponse(roleRepository.findById(roleId).orElseThrow( () -> new RuntimeException("CAN'T GET ROLE BY WITH ID : " + roleId)));
    }
}
