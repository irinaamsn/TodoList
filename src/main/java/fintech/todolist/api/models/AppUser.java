package fintech.todolist.api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    public AppUser(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }


    public AppUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public AppUser(String username) {
        this.username = username;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<Todolist> todolists;
}
