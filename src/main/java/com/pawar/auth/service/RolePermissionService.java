package com.pawar.auth.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pawar.auth.dto.RoleDto;
import com.pawar.auth.entity.Permission;
import com.pawar.auth.entity.Role;
import com.pawar.auth.entity.User;
import com.pawar.auth.exception.PermissionNotFoundException;
import com.pawar.auth.exception.UserNotFoundException;
import com.pawar.auth.repository.PermissionRepository;
import com.pawar.auth.repository.RoleRepository;
import com.pawar.auth.repository.UserRepository;

import jakarta.persistence.EntityManager;

@Service
public class RolePermissionService {

	private static final Logger logger = LoggerFactory.getLogger(RolePermissionService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	private final ObjectMapper objectMapper;


	public RolePermissionService() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
	}

	@Transactional
	public void assignPermissionsToRoles(Integer role_id, Integer permission_id) throws RoleNotFoundException,
			PermissionNotFoundException, JsonProcessingException, InvalidDefinitionException {

		Role role = roleRepository.findById(role_id)
				.orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + role_id));

		Permission assignedPermission = permissionRepository.findById(permission_id)
				.orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + permission_id));

		Set<Permission> assignedPermissions = new HashSet<>(role.getPermissions());
		assignedPermissions.add(assignedPermission);
		role.setPermissions(assignedPermissions);
		Role savedRole = roleRepository.save(role);

		logger.info("Updated Role : {} ", savedRole);
		logger.info("Permissions {} assigned successfully to Role ID: {}", assignedPermission.getName(), role_id);

	}

	@Transactional
	public void unassignPermissionToRole(Integer role_id, Integer permission_id)
			throws RoleNotFoundException, PermissionNotFoundException, JsonProcessingException {
		Role role = roleRepository.findById(role_id)
				.orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + role_id));
		role.getPermissions().removeIf(p -> p.getId().equals(permission_id));
		Role savedRole = roleRepository.save(role);
		logger.info("Updated Role : {} ", savedRole);
		logger.info("Permission ID: {} removed from user ID: {}", role_id, permission_id);
	}

	@Transactional(readOnly = true)
	public Set<RoleDto> getUserRoles(Long userId) throws UserNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

		return user.getRoles().stream().map(role -> new RoleDto(role.getRole_id(), role.getName()))
				.collect(Collectors.toSet());

	}

	@Transactional
	public void removeRoleFromUser(Long userId, Integer roleId) throws UserNotFoundException, RoleNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

		user.getRoles().removeIf(r -> r.getRole_id().equals(roleId));
		userRepository.save(user);

		logger.info("Role ID: {} removed from user ID: {}", roleId, userId);
	}

}