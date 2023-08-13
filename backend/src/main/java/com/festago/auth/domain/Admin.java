package com.festago.auth.domain;

import com.festago.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Member member;

    protected Admin() {
    }

    public Admin(String username, String password, Member member) {
        this(null, username, password, member);
    }

    public Admin(Long id, String username, String password, Member member) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getMemberId() {
        return getMember().getId();
    }

    private Member getMember() {
        return member;
    }
}
