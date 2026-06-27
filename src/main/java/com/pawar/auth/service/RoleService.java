package com.pawar.auth.service;

import java.util.List;

import javax.management.relation.RoleNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pawar.auth.dto.RoleDto;
import com.pawar.auth.entity.Role;
import com.pawar.auth.events.RoleDeleteEvent;
import com.pawar.auth.exception.RoleDeletionException;
import com.pawar.auth.repository.RoleRepository;

@Service
public class RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleRepository;

	private final ObjectMapper objectMapper;

	public RoleService() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Transactional
	public Role saveRole(RoleDto roleDto) throws JsonProcessingException {
		logger.info("RoleDto : {}", roleDto.toString());
		Role role = new Role();
		role.setName(roleDto.getName());
		Role savedRole = roleRepository.save(role);
		logger.info("Role saved successfully with ID: {}", savedRole.getRole_id());
		return savedRole;
	}

	@Transactional
	public Role updateRole(Integer roleId, RoleDto roleDto) throws RoleNotFoundException, JsonProcessingException {
		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
		role.setName(roleDto.getName());
		Role updatedRole = roleRepository.save(role);
		logger.info("Role updated successfully with ID: {}", updatedRole.getRole_id());
		return updatedRole;
	}

	@Transactional
	public void deleteRole(Integer roleId) throws RoleDeletionException, RoleNotFoundException {
		try {
			roleRepository.deleteById(roleId);
			logger.info("Role deleted successfully with ID: {}", roleId);
		} catch (Exception e) {
			logger.error("Error occurred while deleting role with ID: {}", roleId, e);
			throw new RoleDeletionException("Could not delete role with ID: " + roleId, e);
		}
	}

	@Transactional(readOnly = true)
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Role getRoleById(Integer roleId) throws RoleNotFoundException {
		return roleRepository.findById(roleId)
				.orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
	}
}