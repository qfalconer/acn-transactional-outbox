package it.gov.acn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MockKafkaBroker {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private Instant creationDate;

  @Column(columnDefinition = "text")
  private String payload;
}
