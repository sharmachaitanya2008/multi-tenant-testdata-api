package com.example.testdataservice.domain; import jakarta.persistence.*; import org.hibernate.annotations.Immutable;
@Entity @Table(name = "TEST_DATA") @Immutable public class TestDataEntity {
    @Id @Column(name = "id") private Long id; @Column(name = "type") private String type; @Column(name = "payload") private String payload;
    public TestDataEntity(){} public Long getId(){return id;} public String getType(){return type;} public String getPayload(){return payload;}
}
