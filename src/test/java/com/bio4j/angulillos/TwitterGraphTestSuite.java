package com.bio4j.angulillos.test;

import java.util.stream.Stream;
import java.util.Optional;
import java.util.Date;
import java.net.URL;

import com.bio4j.angulillos.*;

public abstract class TwitterGraphTestSuite<RV,RE> {

  protected Twitter<RV,RE> g;

  //////////////////////////////////////////

  // Trying to use some API and see that it returns correct type without any conversions:
  public final Twitter<RV,RE>.User u =
    g.user.fromRaw(null)
      .set(g.user.name, "Bob")
      .set(g.user.age, 42);

  public final String name = u.get(g.user.name);

  public final Twitter<RV,RE>.Tweet t =
    g.tweet.fromRaw(null)
      .set(g.tweet.text, "blah-bluh");

  //////////////////////////////////////////

  // Examples with edges:
  public final Twitter<RV,RE>.Posted p =
    g.posted.fromRaw(null)
      .set(g.posted.when, null);

  public final Twitter<RV,RE>.User poster = p.source();

  public final Stream<Twitter<RV,RE>.Follows> fe = u.outE(g.follows);

  public final Stream<Date> dates = fe.map(edge -> edge.get(g.follows.since));

  public final Stream<Twitter<RV,RE>.Tweet> ts = u.outV(g.posted);

  public final Stream<String> texts = ts.map(tweet -> tweet.get(g.tweet.text));

  public final Optional<Twitter<RV,RE>.Tweet> fromURL(URL url) { return g.tweet.byUrl.find(url); }

  public Stream<Twitter<RV,RE>.User> tweetedTheTweetsThatTweeted(Twitter<RV,RE>.User user) {

    return user.outV(g.posted).flatMap(
      tw -> tw.inV(g.posted)
    );
  }

  /* This uses arity-specific methods to return **the** user that tweeted a tweet. */
  public Twitter<RV,RE>.User tweeted(Twitter<RV,RE>.Tweet tweet) {

    return tweet.inOneV(g.posted);
  }
}
