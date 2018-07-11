package spring.ticketing.model;

import javax.annotation.Nonnull;

public interface AppUser {

  @Nonnull
  Integer getId();

  @Nonnull
  String getUserName();

  @Nonnull
  String getUserEmail();

  @Nonnull
  AppUserRol getRol();
}
