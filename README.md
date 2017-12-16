# Allegro Client

---

![Travis CI status](https://travis-ci.org/zaza/allegro-client.svg?branch=master)

A Java client for Allegro.

```
<dependency>
    <groupId>com.github.zaza</groupId>
    <artifactId>allegro-client</artifactId>
    <version>0.0.4</version>
</dependency>
```

Sample usage:

```
new AllegroClient(<login>, <password>, <webapikey>)
	.searchByString("xbox one").priceTo(500).condition(Condition.USED).search()
	.stream().forEach(i -> System.out.println(i.getItemTitle());
```

Go [here](https://github.com/zaza/allegro-client/issues) for open issues and upcoming features.