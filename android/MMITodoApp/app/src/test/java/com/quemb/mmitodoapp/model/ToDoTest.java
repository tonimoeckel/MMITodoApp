package com.quemb.mmitodoapp.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by tonimockel on 04.07.16.
 */

public class ToDoTest {

    @Test
    public void should_be_equal() throws Exception {

        ToDo toDo1 = new ToDo();
        ToDo toDo2 = new ToDo();

        toDo1.setId(1L);
        toDo2.setId(1L);

        assertThat(toDo1.equals(toDo2), is(true));


    }

    @Test
    public void should_not_be_equal() throws Exception {

        ToDo toDo1 = new ToDo();
        ToDo toDo2 = new ToDo();

        toDo1.setId(1L);
        toDo2.setId(2L);

        assertThat(toDo1.equals(toDo2), is(false));
        assertThat(toDo1.equals(null), is(false));

    }
}
