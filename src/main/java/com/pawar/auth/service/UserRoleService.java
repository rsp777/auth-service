package com.pawar.auth.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pawar.auth.dto.RoleDto;
import com.pawar.auth.entity.Role;
import com.pawar.auth.entity.User;
import com.pawar.auth.exception.UserNotFoundException;
import com.pawar.auth.repository.RoleRepository;
import com.pawar.auth.repository.UserRepository;

@Service
public class UserRoleService {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	private final ObjectMapper objectMapper;

	public UserRoleService() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
	}

	@Transactional
	public void assignRolesToUser(Long user_id, Integer role_id)
			throws UserNotFoundException, RoleNotFoundException, JsonProcessingException {
		User user = userRepository.findById(user_id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + user_id));

		Role assignedRole = roleRepository.findById(role_id)
				.orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + role_id));

		Set<Role> assignedRoles = new HashSet<>(user.getRoles());
		assignedRoles.add(assignedRole);
		user.setRoles(assignedRoles);
		User savedUser = userRepository.save(user);
		logger.info("Updated User : {} ", savedUser);
		logger.info("Roles assigned successfully to user ID: {}", user_id);
	}

	@Transactional
	public void unassignRolesToUser(Long userId, Integer roleId)
			throws UserNotFoundException, RoleNotFoundException, JsonProcessingException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

		user.getRoles().removeIf(r -> r.getRole_id().equals(roleId));
		User savedUser = userRepository.save(user);

		logger.info("Updated Role : {} ", savedUser);
		logger.info("Role ID: {} removed from user ID: {}", roleId, userId);
	}

	@Transactional(readOnly = true)
	public Set<RoleDto> getUserRoles(Long userId) throws UserNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

		return user.getRoles().stream().map(role -> new RoleDto(role.getRole_id(), role.getName()))
				.collect(Collectors.toSet());

	}

}