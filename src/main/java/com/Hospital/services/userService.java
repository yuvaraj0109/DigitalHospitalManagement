package com.Hospital.services;

import com.Hospital.models.userModel;
import com.Hospital.repository.userRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class userService {

	private final userRepository userRepository;

	public userService(userRepository userRepository) {
		this.userRepository = userRepository;
	}

	// REGISTER
	public userModel register(userModel user) {
		return userRepository.save(user);
	}

	// LOGIN
	public userModel login(String name, String password) {
		return userRepository.findByNameAndPassword(name, password);
	}

	// CHECK USER EXISTS
	public boolean existsByName(String name) {
		return userRepository.existsByName(name);
	}

	public void saveUser(userModel user) {
		userRepository.save(user);
	}

	public List<userModel> getUsersByRole(String role) {
		return userRepository.findByRole(role);
	}

	public userModel findByNameAndRole(String name, String role) {
		return userRepository.findByNameAndRole(name, role);
	}

	public userModel getById(int id) {
		return userRepository.findById(id).orElse(null);
	}



}
