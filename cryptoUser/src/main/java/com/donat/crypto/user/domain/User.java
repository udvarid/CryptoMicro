package com.donat.crypto.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq")
    private Long id;

    @Size(max=250)
    @NotNull
    @Column(name = "name")
    private String name;

    @Size(max=250)
    @NotNull
    @Column(name = "userId", unique = true)
    private String userId;

    @Size(max=250)
    @NotNull
    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Wallet> wallets = new HashSet<>();

    @Version
    @Column(name = "version")
    private long version;


    private LocalDateTime timeOfRegistration;
}