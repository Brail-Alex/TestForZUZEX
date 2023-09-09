package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
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
    @Column(name = "house_list_owner")
    private List<House> houseList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "residents", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "house_id", referencedColumnName = "id")})
    private House houseId;

}
