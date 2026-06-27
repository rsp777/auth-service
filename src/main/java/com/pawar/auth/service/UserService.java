package com.pawar.auth.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pawar.auth.dto.PermissionDto;
import com.pawar.auth.dto.RoleDto;
import com.pawar.auth.dto.UserDto;
import com.pawar.auth.entity.Role;
import com.pawar.auth.entity.User;
import com.pawar.auth.exception.RoleDeletionException;
import com.pawar.auth.exception.UserAlreadyExistException;
import com.pawar.auth.exception.UserNotFoundException;
import com.pawar.auth.repository.RoleRepository;
import com.pawar.auth.repository.UserRepository;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private KafkaTemplate<String, String> userKafkaTemplate;

	@Autowired
	private KafkaTemplate<String, String> userRoleKafkaTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final ObjectMapper objectMapper;

	private static final String TO_DO_LOGGED_IN_USER = "TO.DO.LOGGED.IN.USER";

	public UserService() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Transactional
	public User registerNewUserAccount(UserDto userDto, Set<RoleDto> roleDtos)
			throws UserAlreadyExistException, JsonProcessingException {
		logger.debug("Registering user account with username: {}", userDto.getUsername());
		logger.debug("User Email: {}", userDto.getEmail());
		logger.debug("User Password: {}", userDto.getPasswordHash());
		if (emailExist(userDto.getEmail())) {
			logger.warn("User registration failed. Email already exists: {}", userDto.getEmail());
			throw new UserAlreadyExistException("There is an account with that email address: " + userDto.getEmail());
		}

		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPasswordHash(passwordEncoder.encode(userDto.getPasswordHash()));
		user.setFirstName(userDto.getFirstName());
		user.setMiddleName(userDto.getMiddleName());
		user.setLastName(userDto.getLastName());
		user.setCreatedAt(Date.valueOf(LocalDate.now()));
		user.setUpdatedAt(Date.valueOf(LocalDate.now()));
		Set<Role> roles = roleDtos.stream().map(this::findRoleByName).collect(Collectors.toSet());

		user.setRoles(roles);

		userDto.setRoles(roleDtos);
		User registeredUser = userRepository.save(user);
		User new_user = userRepository.findByUsername(registeredUser.getUsername()).get();
		logger.info("created date time : {}", new_user.getCreatedAt());
		logger.info("update date time : {}", new_user.getUpdatedAt());
		logger.info("User registered successfully with username: {}", userDto.getUsername());
		return registeredUser;
	}

	@Transactional
	public User updateUser(Long userId, @Valid UserDto userDto)
			throws UserNotFoundException, RoleNotFoundException, JsonProcessingException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RoleNotFoundException("User not found with ID: " + userId));
		user.setUsername(userDto.getUsername());
		user.setPasswordHash(userDto.getPasswordHash());
		user.setEmail(userDto.getEmail());

		User updatedUser = userRepository.save(user);
		logger.info("User updated successfully with ID: {}", updatedUser.getUser_id());
		return updatedUser;
	}

	@Transactional
	public void deleteUser(Long userId) throws UserNotFoundException, RoleDeletionException {
		try {
			userRepository.deleteById(userId);
			logger.info("User deleted successfully with ID: {}", userId);

		} catch (Exception e) {
			logger.error("Error occurred while deleting user with ID: {}", userId, e);
			throw new RoleDeletionException("Could not delete user with ID: " + userId, e);
		}
	}

	private Integer getPermissionIdFromPermissionDtos(Set<RoleDto> roleDtos) {
		logger.info("RoleDtos : {}", roleDtos.toString());
		for (RoleDto roleDto : roleDtos) {
			Set<PermissionDto> permissionDtos = roleDto.getPermissions();
			if (permissionDtos != null) {
				for (PermissionDto permissionDto : permissionDtos) {
					Integer permissionId = permissionDto.getId();
					logger.info("permissionId : {}", permissionId);

					if (permissionId != null) {
						logger.info("permissionId is not null : {}", permissionId);
						return permissionId;
					}
				}
			} else {
				return null;
			}
		}
		return null;
	}

	private Integer getRoleIdFromRoleDtos(Set<RoleDto> roleDtos) {

		logger.info("RoleDtos : {}", roleDtos.toString());

		for (RoleDto roleDto : roleDtos) {
			Integer roleId = roleDto.getRole_id();
			logger.info("roleId : {}", roleId);

			if (roleId != null) {
				logger.info("roleId is not null : {}", roleId);
				return roleId;
			}
		}
		return null;
	}

	@Transactional
	public List<User> getAllUsers() throws UserNotFoundException {
		return userRepository.findAll();
	}

	private Role findRoleByName(RoleDto roleDto) {
		return roleRepository.findByName(roleDto.getName()).orElseThrow(() -> new RuntimeException("Role not found"));
	}

	private boolean emailExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	@Transactional
	public User getUserById(Long userId) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
	}

	@Transactional
	public Set<RoleDto> defaultUserRole() {

		Optional<Role> roleOptional = roleRepository.findByName("USER");
		logger.info("roleOptional : {}", roleOptional);
		if (roleOptional.isPresent()) {
			Role role = roleOptional.get();

			logger.info("DEFAULT ROLE : {}", role);

			RoleDto defaultRole = new RoleDto();
			// Assuming RoleDto has similar fields as Role
			defaultRole.setRole_id(role.getRole_id());
			defaultRole.setName(role.getName());
			// Set other fields as necessary
			return Collections.singleton(defaultRole);
		} else {
			// Handle the case where the role is not found
			throw new RuntimeException("Role not found");
		}

	}

	public UserDto getUserByuserName(String userName) throws UserNotFoundException {

		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found with userName: " + userName));
		UserDto userDto = new UserDto();
		userDto.setUserId(user.getUser_id());
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setPasswordHash(user.getPasswordHash());
		userDto.setFirstName(user.getFirstName());
		userDto.setMiddleName(user.getMiddleName());
		userDto.setLastName(user.getLastName());
		userDto.setCreatedAt(user.getCreatedAt());
		userDto.setUpdatedAt(user.getUpdatedAt());
		userDto.setLoggedIn(user.getLoggedIn());
		userDto.setRoles(user.convertEntityToDto(user.getRoles()));
		return userDto;

	}

	public User convertDtoToEntity(UserDto userDto) {
		logger.info("User Dto : {}",userDto);
		User user = new User(userDto);
		return user;
	}
}
