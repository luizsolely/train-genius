package com.luizsolely.traingenius.model;

import com.luizsolely.traingenius.model.enums.AvailableDays;
import com.luizsolely.traingenius.model.enums.Goal;
import com.luizsolely.traingenius.model.enums.TrainingLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Double weight;

    @NotNull
    @Positive
    private Double height;

    @Enumerated(EnumType.STRING)
    private TrainingLevel trainingLevel;

    @ElementCollection
    private List<String> restrictions;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<AvailableDays> availableDays;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
