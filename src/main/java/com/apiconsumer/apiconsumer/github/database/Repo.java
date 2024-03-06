package com.apiconsumer.apiconsumer.github.database;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Repo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String ownerName;
    private String repoName;

    public Repo(String ownerName, String repoName) {
        this.ownerName = ownerName;
        this.repoName = repoName;
    }
}
