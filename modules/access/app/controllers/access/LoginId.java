package controllers.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import models.access.User;

/**
 * Marks the login id field. To be used in a {@link User} subtype on a field of type {@link String}.
 * Without this annotation, the inherited field {@link User#username} is used as login id.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LoginId {

}
