package com.pawar.auth.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pawar.auth.dto.PermissionDto;
import com.pawar.auth.entity.Permission;
import com.pawar.auth.exception.PermissionDeletionException;
import com.pawar.auth.exception.PermissionNotFoundException;
import com.pawar.auth.repository.PermissionRepository;

@Service
public class PermissionService {

	private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

	@Autowired
	private PermissionRepository permissionRepository;

	private final ObjectMapper objectMapper;

	public PermissionService() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Transactional
	public Permission savePermission(@Valid PermissionDto permissionDto) throws JsonProcessingException {
		logger.info("PermissionDto : {}", permissionDto.toString());
		Permission permission = new Permission(permissionDto);
		permission.setName(permissionDto.getName());
		permission.setCreatedDttm(LocalDateTime.now());
		permission.setLastUpdatedDttm(LocalDateTime.now());
		permission.setCreatedSource("PMS");
		permission.setLastUpdatedSource("PMS");
		Permission savedPermission = permissionRepository.save(permission);
		logger.info("New Permission : {}",savedPermission);
		logger.info("Permission saved successfully with ID: {}", savedPermission.getId());
		return savedPermission;
	}

	@Transactional(readOnly = true)
	public List<Permission> getAllPermissions() {
		return permissionRepository.findAll();
	}

	@Transactional
	public Permission updatePermission(Integer permissionId, @Valid PermissionDto permissionDto)
			throws PermissionNotFoundException, JsonProcessingException {
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(() -> new PermissionNotFoundException("Permission not found with ID: " + permissionId));
		permission.setName(permissionDto.getName());
		permission.setLastUpdatedDttm(LocalDateTime.now());
		Permission updatedPermission = permissionRepository.save(permission);
		logger.info("Permission updated successfully with ID: {}", updatedPermission.getId());
		logger.debug("Updated Permission : {}",updatedPermission);
		return updatedPermission;
	}

	@Transactional(readOnly = true)
	public Permission getPermissionById(Integer permissionId) throws PermissionNotFoundException {
		return permissionRepository.findById(permissionId)
				.orElseThrow(() -> new PermissionNotFoundException("Permission not found with ID: " + permissionId));
	}

	@Transactional
	public void deletePermission(Integer permissionId) throws PermissionNotFoundException, PermissionDeletionException {
		try {
			permissionRepository.deleteById(permissionId);
			logger.info("Permission deleted successfully with ID: {}", permissionId);
		} catch (Exception e) {
			logger.error("Error occurred while deleting permission with ID: {}", permissionId, e);
			throw new PermissionDeletionException("Could not delete permission with ID: " + permissionId, e);
		}

	}

}
