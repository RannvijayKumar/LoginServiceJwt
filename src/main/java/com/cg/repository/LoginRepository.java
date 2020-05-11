package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.models.Login;

public interface LoginRepository extends JpaRepository<Login, String> {

	Login findByAdminId(String adminId);

}
