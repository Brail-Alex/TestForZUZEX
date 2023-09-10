package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "houses")
@EqualsAndHashCode
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "owner_houses", joinColumns = {@JoinColumn(name = "house_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private User ownerId;


    @ManyToMany(mappedBy = "housesResidence", cascade = CascadeType.ALL)
    @Column(name = "user_id", table = "residents")
    private List<User> residents;
}
