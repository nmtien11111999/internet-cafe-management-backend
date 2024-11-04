package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.Set;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private boolean enabled = false;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;
    private Long time = 0L;
    @Column(unique = true,nullable = false)
//    @Pattern(regexp = "^\\d{12}$" , message = "Sai định dạng căn cước công dân")
    private Long identityCode;



    public User(String username, String password,Long identityCode ,Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.identityCode = identityCode;
        this.roles = roles;
    }

    public User(String username,Long time) {
        this.username = username;
        this.time = time;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String username, String password, boolean enabled, Set<Role> roles, Long time, Long identityCode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.time = time;
        this.identityCode = identityCode;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public void setIdentityCode(Long identityCode) {
        this.identityCode = identityCode;
    }

    public Long getIdentityCode() {
        return identityCode;
    }
}