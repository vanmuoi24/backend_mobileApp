package com.example.chart_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.chart_backend.entity.FileMgmt;

@Repository
public interface FileMgmtRepository extends JpaRepository<FileMgmt, String> {

    Optional<FileMgmt> findByOwnerId(String ownerId);
}