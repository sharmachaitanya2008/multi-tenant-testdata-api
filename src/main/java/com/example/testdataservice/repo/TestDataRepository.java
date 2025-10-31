package com.example.testdataservice.repo; import com.example.testdataservice.domain.TestDataEntity; import org.springframework.data.jpa.repository.JpaRepository;
public interface TestDataRepository extends JpaRepository<TestDataEntity, Long> {}
