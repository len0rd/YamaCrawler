package net.lenords.yama.model.extract;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegexPatternTest {

  @Test
  void basicPattern() {
    String matchAgainst = "<h2><a href=\"https://arstechnica.com/gadgets/2018/03/galaxy-s9-review-faster-specs-better-biometrics-in-a-familiar-package/\">Galaxy S9+ review—Faster specs, better biometrics in a familiar package</a></h2>";

    RegexPattern pattern = new RegexPattern("basicPattern");

    pattern.add(".*href=\"").add(new RegexExtractor("link", "[^\"']*"))
        .add("\">").add(new RegexExtractor("name", "[^<>]*"));
    pattern.build();


    List<Map<String, String>> results = pattern.execute(matchAgainst);

    System.out.println(pattern.getGeneratedRegex());

    assert results != null;
    assert !results.isEmpty();
    Assertions.assertEquals("https://arstechnica.com/gadgets/2018/03/galaxy-s9-review-faster-specs-better-biometrics-in-a-familiar-package/", results.get(0).get("link"));
    Assertions.assertEquals("Galaxy S9+ review—Faster specs, better biometrics in a familiar package", results.get(0).get("name"));

  }

  @Test
  void usingCommonExtractors() {
    String matchAgainstSuccess = "Hello There! General Kenobi! You are a bold one! Here's my "
        + "email: grievous.general@bionic-man.net";

    RegexPattern emailMatcher = new RegexPattern("emailMatcher");
    emailMatcher.add(".*?").add(new RegexExtractor("name", "(K|k)enobi")).add(".*?")
        .add(new RegexExtractor("email", CommonRegex.EMAIL_ADDRESS));

    String regex = emailMatcher.build();

    System.out.println(regex);

    List<Map<String, String>> results = emailMatcher.execute(matchAgainstSuccess);

    assert results != null;
    assert !results.isEmpty();
    Assertions.assertEquals("Kenobi", results.get(0).get("name"));
    Assertions.assertEquals("grievous.general@bionic-man.net", results.get(0).get("email"));

  }

  @Test
  void runTidiers() {
    String matchAgainst = "<h2><a href=\"https://arstechnica.com/gadgets/2018/03/galaxy-s9-review-faster-specs-better-biometrics-in-a-familiar-package/\">  Galaxy S9+ review—Faster specs, better biometrics in a familiar package  </a></h2>";

    RegexPattern tidyTest = new RegexPattern("tidyTest");
    tidyTest.add("<h2>").add((new RegexExtractor("articleTitle", ".*?")).stripHtml().trim())
        .add("</h2>");

    String regex = tidyTest.build();
    System.out.println(regex);

    List<Map<String, String>> results = tidyTest.execute(matchAgainst);

    assert results != null;
    assert !results.isEmpty();
    Assertions.assertEquals("Galaxy S9+ review—Faster specs, better biometrics in a familiar package", results.get(0).get("articleTitle"));

  }

}
