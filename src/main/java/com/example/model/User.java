package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "age", nullable = false)
    private byte age;
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "ownerId", cascade = CascadeType.ALL)
    @Column(name = "house_id", table = "owner_houses")
    private List<House> houseOwnerList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "residents", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "house_id", referencedColumnName = "id" )})
    private List<House> housesResidence;

    public User(String name, byte age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}
