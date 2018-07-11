package spring.ticketing.model;

import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Ticket extends TicketDraft {

  @Nonnull
  Integer getId();

  @Nonnull
  AppUser getOperator();

  @Nullable
  AppUser getCoordinator();

  @Nullable
  String getDescription();

  @Nonnull
  TicketStatus getStatus();

  @Nullable
  Resolution getResolution();

  @Nullable
  String getResolutionInfo();

  @Nonnull
  boolean isEscalated();

  @Nonnull
  LocalDateTime getCreationDate();

  @Nonnull
  LocalDateTime getModificationDate();

  @Nullable
  LocalDateTime getClosingDate();
}
