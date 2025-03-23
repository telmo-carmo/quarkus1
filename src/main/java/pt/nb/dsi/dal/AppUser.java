package pt.nb.dsi.dal;

import java.time.LocalDate;
import java.util.UUID;


import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue
    public UUID id;
    
    public String username;
    public String passHash;
    public String role;
    public LocalDate createdAt;

    public AppUser() {
        this.createdAt = LocalDate.now();
    }

    public AppUser(String username, String passHash, String role, LocalDate createdAt) {
        this.username = username;
        this.passHash = passHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public String toString() {
        return String.format("AppUser{id=%s, username=%s, passHash=%s, role=%s, createdAt=%s}",
             id, username, passHash, role, createdAt);
    }
}
