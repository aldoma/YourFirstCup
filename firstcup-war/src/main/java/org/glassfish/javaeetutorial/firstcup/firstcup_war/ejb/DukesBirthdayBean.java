/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.glassfish.javaeetutorial.firstcup.firstcup_war.ejb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.glassfish.javaeetutorial.firstcup.firstcup_war.entity.FirstcupUser;

/**
 * DukesBirthdayBean is a stateless session bean that calculates the age
 * difference between a user and Duke, who was born on May 23, 1995.
 */
@SuppressWarnings("nls")
@Stateless
public class DukesBirthdayBean {
  private static final Logger logger = Logger.getLogger("org.glassfish.javaeetutorial.firstcup.firstcup_war.ejb.DukesBirthdayBean");

  @PersistenceContext
  private EntityManager       em;

  public Double getAverageAgeDifference() {
    final Double avgAgeDiff = (Double) em.createNamedQuery("findAverageAgeDifferenceOfAllFirstcupUsers")
                                         .getSingleResult();
    logger.log(Level.INFO, "Average age difference is: {0}", avgAgeDiff);
    return avgAgeDiff;
  }

  public int getAgeDifference(final Date date) {
    int ageDifference;

    final Calendar theirBirthday = new GregorianCalendar();
    final Calendar dukesBirthday = new GregorianCalendar(1995, Calendar.MAY, 23);

    // Set the Calendar object to the passed-in Date
    theirBirthday.setTime(date);

    // Subtract the user's age from Duke's age
    ageDifference = dukesBirthday.get(Calendar.YEAR) - theirBirthday.get(Calendar.YEAR);
    logger.log(Level.INFO, "Raw ageDifference is: {0}", Integer.valueOf(ageDifference));
    // Check to see if Duke's birthday occurs before the user's. If so,
    // subtract one from the age difference
    if (dukesBirthday.before(theirBirthday) && ageDifference > 0) {
      ageDifference--;
    }

    // Create and store the user's birthday in the database
    final FirstcupUser user = new FirstcupUser(date, ageDifference);
    em.persist(user);

    logger.log(Level.INFO, "Final ageDifference is: {0}", Integer.valueOf(ageDifference));

    return ageDifference;
  }
}
