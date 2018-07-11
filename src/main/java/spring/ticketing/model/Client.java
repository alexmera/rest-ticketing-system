package spring.ticketing.model;

import javax.annotation.Nonnull;

public interface Client {

  @Nonnull
  Integer getId();

  @Nonnull
  AppUser getAppUser();

  @Nonnull
  String getClientName();
}
