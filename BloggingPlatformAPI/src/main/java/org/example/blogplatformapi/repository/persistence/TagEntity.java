package org.example.blogplatformapi.repository.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_name", nullable = false, unique = true, length = 50)
    private String tagName;

    @ManyToMany(mappedBy = "tagEntities")
    private List<PostEntity> postEntities;
}