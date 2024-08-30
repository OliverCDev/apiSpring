package com.prueba.login.autenticacion.login.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Comparar usando el campo 'id'
@ToString(includeFieldNames = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> roles; // Relación con la entidad UserRole// Relación con la entidad UserRole
    
    @PrePersist
    protected void onCreate() {
        // Inicialización previa a la persistencia, si es necesario
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Inicialización previa a la actualización, si es necesario
    }
    
    // Métodos adicionales si es necesario
    
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(userRole -> userRole.getRole().getName().equals(roleName));
    }
}
