package spring.ticketing.model;

import javax.annotation.Nonnull;

public interface TicketDraft {

  @Nonnull
  Client getClient();

  @Nonnull
  TicketChannel getChannel();

  @Nonnull
  String getContactName();

  @Nonnull
  String getSubject();
}
