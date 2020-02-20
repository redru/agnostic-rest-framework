package com.carspark.restapi;

import com.carspark.restapi.sql.TableEntity;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class RestApiApplicationTests {

    private final DSLContext dslContext;

    @Autowired
    public RestApiApplicationTests(DSLContext dslContext) {
        this.dslContext=dslContext;
    }

    @Test
    void contextLoads() {

        Map<String, List<String>> prj=new HashMap<>();
        prj.put("inc", Arrays.asList("film_id", "title", "description", "actors", "language"));
        prj.put("exc", Arrays.asList("film_id", "title", "release_year", "language_id", "original_language_id", "rental_duration"));


        Map<String, Boolean> sort=new HashMap<>();
        sort.put("film_id", true);
        sort.put("description", false);
        sort.put("original_language_id", true);


        TableEntity film= TableEntity.table("film", "film_id");

        film.relation("actors")
                .id("actor.actor_id")
                .link("film.film_id", "film_actor.film_id")
                .link("actor.actor_id", "film_actor.actor_id");

        film.relation("language")
                .id("film.language_id")
                .link("film.language_id", "language.language_id");


        film.fetch(this.dslContext, prj, sort, "film.rental_duration=3", 0, 10);

    }

}
