package com.moonspoon.moonspoon.test;

import org.springframework.data.jpa.repository.JpaRepository;

interface TestRepository extends JpaRepository<Test, Long> {
}
